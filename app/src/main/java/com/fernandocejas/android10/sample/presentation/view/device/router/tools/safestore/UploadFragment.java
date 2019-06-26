package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.DeviceUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentUploadBinding;
import com.fernandocejas.android10.sample.presentation.utils.UploadCacheManager;
import com.fernandocejas.android10.sample.presentation.view.component.ListViewForScrollView;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.dbhelper.DatabaseUtil;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.CustomCircleProgress;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.SambaUtils;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.UploadUtils;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.google.android.sambadocumentsprovider.nativefacade.SmbClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.UploadUtils.stopPicRestoreTimer;

/**
 * <pre>
 *      author:
 *      e-mail:
 *      time: 2017/06/22
 *      desc: 上传列表
 *      version: 1.0
 * </pre>
 */
public class UploadFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
  private FragmentUploadBinding mBinding;
  private List<FileUploadBean> mFinishedFilesFromDB = null; //来自数据库的上传成功的文件
  private CustomCircleProgress circleProgress;
  private ListViewForScrollView mLvUpload, mLvFinishUpload;
  private static UploadUnfinishAdapter mUploadUnfinishAdapter = null;
  private static UploadFinishedAdapter mFinishUploadAdapter;
  private TextView mLoadingTitle, mFinishTitle;
  private DatabaseUtil mDBUtil1;
  // 存放各个下载器
  private static Map<String, Downloader> uploaders = new HashMap<String, Downloader>();
  private static Boolean isAllUpload = true;
  private Handler mUploadUIHandler;
  private Runnable mUploadUIRunnable;
  private SambaPopupWindow mSambaPopWin;
  private static SmbClient mClient;
  private static Context sContext;
  Boolean isUploadingEdit = false, isUploadedEdit = false, isLoadingSelectAll = false, isLoadedSelectAll = false, isDeleteVisiable = false;
  private List<FileUploadBean> mSelectAllUnfinishFile, mSelectAllFinishedFile;

  /**
   * 批量上传 同时支持3个AnsyTask操作
   */
  public static Handler mHandlerUpload = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);

      uploaders = AndroidApplication.getUploaders();
      mClient = AndroidApplication.getSambaClient();

      System.out.println("上传文件 mhandler isAllUpload = " + isAllUpload);

      if (msg.what == Downloader.DOWNLOAD_FINISHED) {
        String url = (String) msg.obj;
        String exceptionFlag = msg.getData().getString("LOADING_EXCEPTION");
        FileUploadBean bean = (FileUploadBean) msg.getData().getSerializable("bean");
        System.out.println("UploadFragmentHandlerUpload handleMessage ==》 url = " + url + "    ==》 uploaders.size = " + uploaders.size()+" exceptFlag = "+exceptionFlag);

        //上传完成之后移除文件
        UploadCacheManager.delete(AndroidApplication.getApplication(), url);
        System.out.println("上传文件 uploaders.size() before = " + uploaders.size());
        //上传完之后移除内存downloader
        Downloader downloader1 = uploaders.get(url);
        if (downloader1 != null) {
          if (TextUtils.isEmpty(exceptionFlag)){
            //发生异常的时候不删除
            downloader1.delete(url, AndroidApplication.getApplication());
            downloader1.reset();
            uploaders.remove(url);
          }else {
            downloader1.setState(Downloader.FAILED);
            UploadCacheManager.updateUploaderInfos(AndroidApplication.getApplication(), url, downloader1);
          }
        }

        if(SambaUtils.mUploadingIndex > 0){
          SambaUtils.mUploadingIndex--;
        }

        System.out.println("SambaUtils.mUploadingIndex = " + SambaUtils.mUploadingIndex);

        System.out.println("上传文件 uploaders.size() after = " + uploaders.size());

        if (mUploadUnfinishAdapter != null) {
          if (TextUtils.isEmpty(exceptionFlag)) {
            //没有发生异常
            mUploadUnfinishAdapter.remove(url);
            mFinishUploadAdapter.add(bean);
          } else {
            /*mUploadUnfinishAdapter.remove(url);*/
            mUploadUnfinishAdapter.updateFailed(url);
            //上传发生异常的文件
            if(SambaUtils.mIOException){
              SambaUtils.mIOException = false;
              Toast.makeText(AndroidApplication.getApplication(), "上传发生异常，请检查文件是否正在被操作或磁盘空间已满", Toast.LENGTH_SHORT).show();
            }
          }
        }

        if (uploaders.size() > 0) {
          //启动下一个
          Boolean isHasLoading = false;//标志是否有传输数据
          Iterator it = uploaders.keySet().iterator();//获取所有的健值
          while (it.hasNext()) {
            try {
              String key;
              key = (String) it.next();

              String sourcePath = key;
              String despath = uploaders.get(key).getLocalfile();

              //暂停状态的任务跳过
              if (uploaders.get(key).getState() == Downloader.PAUSE) continue;

              //控制最大支持三个Task

              if(uploaders.get(key).getState() == Downloader.WAITING){

                if(SambaUtils.mUploadingIndex >= 3){
                  System.out.println("SambaUtils.mUploadingIndex 超过3个 break循环 = " + SambaUtils.mUploadingIndex);
                  break;
                }else {
                  //启动下一个等待状态的任务
                  SambaUtils.UploadTask uploadTask = new SambaUtils.UploadTask(uploaders, AndroidApplication.getApplication(), mClient, mHandlerUpload);
                  uploadTask.execute(sourcePath, despath, "1");
                  isHasLoading = true;

                  if(mUploadUnfinishAdapter != null){
                    FileUploadBean bean1 = mUploadUnfinishAdapter.getNext(key);
                    if(bean1 != null){
                      bean1.setState(2);//更新状态
                      mUploadUnfinishAdapter.notifyDataSetChanged();
                    }

                  }

                  System.out.println("SambaUtils.mUploadingIndex = " + SambaUtils.mUploadingIndex+" 上传文件 onhandler 新启动的 key(urlstr)="+key+" state= "+uploaders.get(key).getState());

                }
              }

              //查询一个有没下载完的文件没有更新状态刷新
              if(mUploadUnfinishAdapter != null){
                for (int i = 0; i < mUploadUnfinishAdapter.getCount(); i++) {
                  FileUploadBean bean5 = mUploadUnfinishAdapter.getItem(i);
                  if(bean5.getState() == Downloader.DOWNLOADING){
                    if(bean5.getSumSize()>0 && (bean5.getSumSize() == bean5.getFinishedSize())){
                      mUploadUnfinishAdapter.remove(bean5.getDownloadsKey());
                      mFinishUploadAdapter.add(bean5);
                      System.out.println("UploadFragmentHandlerUpload handleMessage ==》扫描发现已完成 ");
                    }
                  }
                }
              }

            } catch (Exception e) {
              System.out.println("上传文件 mhandler 迭代异常");
              e.printStackTrace();
            }
          }

          if(!isHasLoading){
            //没有下载数据
            if(mUploadUnfinishAdapter != null){
              mUploadUnfinishAdapter.updateUploadBtnState(false);
            }
          }

        }else {
          SambaUtils.mUploadingIndex = 0;
          System.out.println("SambaUtils.mUploadingIndex = " + SambaUtils.mUploadingIndex);
          System.out.println("UploadFragmentHandlerUpload handleMessage ==》 上传完成了 uploader.size()= "+uploaders.size());

          //检查一下是否有刷新异常的文件
          if(mUploadUnfinishAdapter != null){
            for (int i = 0; i < mUploadUnfinishAdapter.getCount(); i++) {
              FileUploadBean bean1 = mUploadUnfinishAdapter.getItem(i);
              System.out.println("UploadFragmentHandlerUpload handleMessage ==》 上传完成了 state = "+bean1.getState());
              if(bean1.getState() == Downloader.FAILED) continue;
              mUploadUnfinishAdapter.remove(bean1.getDownloadsKey());
              mFinishUploadAdapter.add(bean1);

            }
          }
        }
      } else if (msg.what == Downloader.DOWNLOAD_LOADING) {
        String url = (String) msg.obj;
        String speed = msg.getData().getString("LOADING_SPEED");
        long completeSize = msg.getData().getLong("LOADING_COMPLETESIZE");

        if (mUploadUnfinishAdapter != null) {

          //更新btn状态
          mUploadUnfinishAdapter.updateUploadBtnState(isAllUpload);

          if(!isAllUpload) return;

          FileUploadBean bean = mUploadUnfinishAdapter.getNext(url);

          if (bean != null) {
            bean.setLoadingSpeed(speed);
            bean.setFinishedSize(completeSize);
            bean.setState(Downloader.DOWNLOADING);  //保证没有状态及时更新
            mUploadUnfinishAdapter.notifyDataSetChanged();
          }
        }
      }else if(msg.what == Downloader.DOWNLOAD_USER_PAUSE){
        //用户暂停操作
        //设计思路：当用户操作时，保留第一个任务continue while循环，其他的任务都暂停掉，这样可解决卡顿问题
        //当用户停止操作时保留的循环操作完成之后会启动三个新的任务继续传输
      }
    }
  };


  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    sContext = context;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public static UploadFragment newInstance() {
    UploadFragment fragment = new UploadFragment();
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_upload, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initView();
    mDBUtil1 = AndroidApplication.getUploadDBUtil();
    initData();
  }

  private void initItemEvent() {
    mBinding.lvFinishUpload.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //长按删除文件
        mSambaPopWin = new SambaPopupWindow(getActivity(), 4, "isUpload");
        mSambaPopWin.setFocusable(true);
        mSambaPopWin.showAtLocation(mSambaPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);
        mSambaPopWin.setOnPositiveClickListener(new SambaPopupWindow.OnPositiveClickListener() {
          @Override
          public void onPositiveClick() {

            if (mDBUtil1 != null) {
              mDBUtil1.deleteUploadUrls(mFinishedFilesFromDB.get(position).getPath());
              mFinishedFilesFromDB.remove(position);
              Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
              updateFinishFiles();//刷新
            }
          }
        });
        return true;
      }
    });

    mBinding.lvUpload.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //长按删除文件
        mSambaPopWin = new SambaPopupWindow(getActivity(), 4, "isUpload");
        mSambaPopWin.setFocusable(true);
        mSambaPopWin.showAtLocation(mSambaPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);
        mSambaPopWin.setOnPositiveClickListener(new SambaPopupWindow.OnPositiveClickListener() {
          @Override
          public void onPositiveClick() {

            String url = mUploadUnfinishAdapter.getItem(position).getDownloadsKey();

            if (!TextUtils.isEmpty(url)) {
              if (uploaders.get(url) != null) {
                uploaders.get(url).delete(url, getActivity());
                uploaders.get(url).reset();
                uploaders.remove(url);
                //mDBUtil1.deleteKey(url);//下载完就删除key
              }

              //上传完成之后移除文件
              UploadCacheManager.delete(getActivity(), url);

              mUploadUnfinishAdapter.remove(position);

              updateTitleState();

              Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
            }

          }
        });
        return true;
      }
    });

  }

  private void updateFinishFiles() {
    if (mFinishUploadAdapter != null) {
      mFinishUploadAdapter.notifyDataSetChanged();
    }

    updateTitleState();
  }

  private void initData() {
    //照片备份
    initTimeTask();

    if (PrefConstant.getPictureRestoreState()) {
      //图片记录开关是否开启
      //备份开关开启但是被删除的场景
      String newName = "来自：" + DeviceUtils.getModel();
      if(GlobleConstant.isSambaExtranetAccess){
        //外网
        mBinding.llPictureRestore.setVisibility(View.GONE);
        SambaUtils.createSambaFolder(mClient, LoginConfig.OS_REMOTE_ROOT_IP, newName);
      }else {
        mBinding.llPictureRestore.setVisibility(View.VISIBLE);
        SambaUtils.createSambaFolder(mClient, LoginConfig.LAN_REMOTE_ROOT_IP, newName);
      }

      //初始状态 开启
      if (GlobleConstant.isStopRestorePicture) {
        if (GlobleConstant.getgPictureRestoredSumCount() - GlobleConstant.getgPictureRestoredCount() < 0) {
          ((TextView) getActivity().findViewById(R.id.tv_auto_restore_name)).setText("自动备份");
        } else {
          ((TextView) getActivity().findViewById(R.id.tv_auto_restore_name)).setText("自动备份(剩余" + (GlobleConstant.getgPictureRestoredSumCount() - GlobleConstant.getgPictureRestoredCount()) + "个)");
        }
        ((TextView) getActivity().findViewById(R.id.tv_auto_restore_size)).setText("暂停中…");
        ((CustomCircleProgress) getActivity().findViewById(R.id.tv_auto_restore_ccp)).setStatus(CustomCircleProgress.Status.End);
      } else {
        ((CustomCircleProgress) getActivity().findViewById(R.id.tv_auto_restore_ccp)).setStatus(CustomCircleProgress.Status.Starting);

        if (mUploadUIHandler != null) {
          //开启定时器 刷新图片备份UI
          mUploadUIHandler.postDelayed(mUploadUIRunnable, 60);
        }

      }

      // 点击暂停按钮 逻辑实现
      ((CustomCircleProgress) getActivity().findViewById(R.id.tv_auto_restore_ccp)).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (((CustomCircleProgress) getActivity().findViewById(R.id.tv_auto_restore_ccp)).getStatus() == CustomCircleProgress.Status.Starting) {//如果是开始状态
            //点击则变成关闭暂停状态
            ((CustomCircleProgress) getActivity().findViewById(R.id.tv_auto_restore_ccp)).setStatus(CustomCircleProgress.Status.End);

            ((TextView) getActivity().findViewById(R.id.tv_auto_restore_size)).setText("暂停中…");
            ((TextView) getActivity().findViewById(R.id.tv_auto_restore_speed)).setText("");

            stopPicRestoreTimer();//关掉定时器

            GlobleConstant.isStopRestorePicture = true;
            GlobleConstant.isLastPicRestored = true;

            if (mUploadUIHandler != null) {
              if (mUploadUIRunnable != null) {
                mUploadUIHandler.removeCallbacks(mUploadUIRunnable);//移除定时器
                System.out.println("图片备份 UploadFragment = " + "定时器移除 CustomCircleProgress 暂停");
              }
            }

          } else {
            //点击则变成开启状态
            ((CustomCircleProgress) getActivity().findViewById(R.id.tv_auto_restore_ccp)).setStatus(CustomCircleProgress.Status.Starting);

            GlobleConstant.isStopRestorePicture = false;

            if (mUploadUIHandler != null) {
              mUploadUIHandler.postDelayed(mUploadUIRunnable, 100);
            }

          }
        }
      });

    } else {
      mBinding.llPictureRestore.setVisibility(View.GONE);

      if (mUploadUIHandler != null) {
        if (mUploadUIRunnable != null) {
          mUploadUIHandler.removeCallbacks(mUploadUIRunnable);//移除定时器
          System.out.println("图片备份 UploadFragment = " + "定时器移除 mBinding.llPictureRestore.setVisibility(View.GONE)");
        }
      }
    }

    uploaders = AndroidApplication.getUploaders();

    mFinishedFilesFromDB.clear();

    try {
      mFinishedFilesFromDB.addAll(mDBUtil1.queryAllUploadData());
    } catch (Exception e) {
      e.printStackTrace();
    }

    if ((uploaders.size()) == 0 && (mFinishedFilesFromDB.size() == 0)) {
      if (PrefConstant.getPictureRestoreState()) {
        //如果开启了图片备份
        mBinding.tvEmptyUpload.setVisibility(View.GONE);
        mBinding.scrollViewUpload.setVisibility(View.GONE);
      } else {
        mBinding.tvEmptyUpload.setVisibility(View.VISIBLE);
        mBinding.scrollViewUpload.setVisibility(View.GONE);
      }
      return;
    } else {
      mBinding.tvEmptyUpload.setVisibility(View.GONE);
      mBinding.scrollViewUpload.setVisibility(View.VISIBLE);
    }

    //先查询上传历史
    mFinishUploadAdapter = new UploadFinishedAdapter(getActivity(), mFinishedFilesFromDB, mFinishTitle);
    mLvFinishUpload.setAdapter(mFinishUploadAdapter);
    updateTitleState();

    if (uploaders.size() == 0) {
      //批量上传记录也为空
      System.out.println("暂无上传任务");
      /*mBinding.btnAllUpload.setText("全部开始上传");*/
      mBinding.btnAllUpload.setVisibility(View.GONE);
    } else {
      // java.util.ConcurrentModificationException 避免异常
      new QueryUnFinishDataTask().execute();
      //此处用thread 由于：批量上传下载起的AsyncTask太多 需要等待排队 所以记录时隐时现
    }

    initItemEvent();

  }

  /**
   * 查询正在上传的数据
   *
   * @param
   * @return
   */
  class QueryUnFinishDataTask extends AsyncTask<Void, Void, Void> {

    public QueryUnFinishDataTask() {
    }

    @Override
    protected void onPreExecute() {

      mUploadUnfinishAdapter = new UploadUnfinishAdapter(getActivity(), mLoadingTitle,mBinding.btnAllUpload);
      mLvUpload.setAdapter(mUploadUnfinishAdapter);
    }

    @Override
    protected Void doInBackground(Void... params) {
      Iterator it = uploaders.keySet().iterator();//获取所有的健值
      while (it.hasNext()) {

        try {
          String key;
          key = (String) it.next();
          // 此处出现异常  java.util.ConcurrentModificationException 原因：调用list.remove()方法导致modCount和expectedModCount的值不一致导致
          long completeSize = uploaders.get(key).getCompeleteSize();
          long fileSize = uploaders.get(key).getFileSize();

          File file = new File(uploaders.get(key).getUrlstr());
          FileUploadBean fileBean = new FileUploadBean();
          fileBean.setName(file.getName());

          fileBean.setSumSize(fileSize);
          fileBean.setState(uploaders.get(key).getState());

          fileBean.setFinishedSize(completeSize);
          fileBean.setDownloadsKey(key);//源地址
          fileBean.setPath(uploaders.get(key).getLocalfile()); //目的地址

          getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
              //单条数据刷新
              if (mUploadUnfinishAdapter != null) {
                mUploadUnfinishAdapter.add(fileBean);
                updateTitleState();
              }
            }
          });

        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      initEvent();
      initItemEvent();
    }
  }

  private void initView() {
    mLoadingTitle = mBinding.tvLoadingTitle;
    mFinishTitle = mBinding.tvFinishTitle;
    mLvUpload = mBinding.lvUpload;
    mLvFinishUpload = mBinding.lvFinishUpload;
    mSelectAllFinishedFile = new ArrayList<>();
    mSelectAllUnfinishFile = new ArrayList<>();
    mClient = AndroidApplication.getSambaClient(getActivity());

    mFinishedFilesFromDB = new ArrayList<>();

    mBinding.lvFinishUpload.setOnItemClickListener(this);

    Intent intent = new Intent();
    intent.putExtra("SHAREPATH", getRemoteCurrentPath());
    getActivity().setResult(1238, intent);
  }

  /**
   * 定时器更新备份的UI
   *
   * @param
   * @return
   */
  public void initTimeTask() {
    //保证开关开启
    mUploadUIHandler = new Handler();

    mUploadUIRunnable = new Runnable() {
      @Override
      public void run() {

        if (PrefConstant.getPictureRestoreState()) {

          UploadUtils.startPicRestoreTimer(mClient, getActivity());

          if (GlobleConstant.isFinishedPicRestored) {
            //已经备份完成
            stopPicRestoreTimer();//关掉定时器
            removeHandleTimer();
            GlobleConstant.isLastPicRestored = true;
            ((TextView) getActivity().findViewById(R.id.tv_auto_restore_name)).setText("自动备份");
            ((TextView) getActivity().findViewById(R.id.tv_auto_restore_size)).setText("备份完成");
            ((CustomCircleProgress) getActivity().findViewById(R.id.tv_auto_restore_ccp)).setVisibility(View.GONE);
            ((TextView) getActivity().findViewById(R.id.tv_auto_restore_speed)).setVisibility(View.GONE);

            return;
          }

          //启动定时器去刷新UI

          ((CustomCircleProgress) getActivity().findViewById(R.id.tv_auto_restore_ccp)).setMax((int) GlobleConstant.gPictureTotalSize);
          ((CustomCircleProgress) getActivity().findViewById(R.id.tv_auto_restore_ccp)).setProgress((int) GlobleConstant.gPictureUploadedSize);

          if (GlobleConstant.gPictureUploadedSize == 0) {
            ((TextView) getActivity().findViewById(R.id.tv_auto_restore_size)).setText("0K/" + SambaUtils.bytes2kb(GlobleConstant.gPictureTotalSize));
            ((TextView) getActivity().findViewById(R.id.tv_auto_restore_speed)).setText("0K/S");
          } else {
            if (GlobleConstant.gPictureUploadedSize <= GlobleConstant.gPictureTotalSize) {

              ((TextView) getActivity().findViewById(R.id.tv_auto_restore_size)).setText(SambaUtils.bytes2kb(GlobleConstant.gPictureUploadedSize) + "/" + SambaUtils.bytes2kb(GlobleConstant.gPictureTotalSize));

              //更新速度 不为空且不以.开头
              if (!TextUtils.isEmpty(GlobleConstant.gPictureUploadSpeed) && !GlobleConstant.gPictureUploadSpeed.startsWith(".")) {
                ((TextView) getActivity().findViewById(R.id.tv_auto_restore_speed)).setText(GlobleConstant.gPictureUploadSpeed + "/S");
              }
            }
          }

          ((TextView) getActivity().findViewById(R.id.tv_auto_restore_name)).setText("自动备份(剩余" + (GlobleConstant.getgPictureRestoredSumCount() - GlobleConstant.getgPictureRestoredCount()) + "个)");

          mUploadUIHandler.postDelayed(this, 60);//每隔0.1s轮询一次
        } else {
          if (mUploadUIHandler != null) {
            mUploadUIHandler.removeCallbacks(this);//移除定时器
            System.out.println("图片备份 UploadFragment = " + "定时器移除 GlobleConstant.getPictureRestoreState()=false ");
          }
        }
      }
    };
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {

      default:
        break;
    }
  }

  private void updateTitleState() {
    int unFinishedCount = 0;
    if (mUploadUnfinishAdapter != null) {
      unFinishedCount = mUploadUnfinishAdapter.getCount();
    }

    mLoadingTitle.setText("正在上传(" + (unFinishedCount) + ")");

    mFinishTitle.setText("上传成功(" + mFinishedFilesFromDB.size() + ")");

    if ((unFinishedCount) > 0) {
      mBinding.btnAllUpload.setVisibility(View.VISIBLE);
    } else {
      mBinding.btnAllUpload.setVisibility(View.GONE);
    }

    if (unFinishedCount == 0 && mFinishedFilesFromDB.size() == 0) {
      mBinding.tvEmptyUpload.setVisibility(View.VISIBLE);
      mBinding.scrollViewUpload.setVisibility(View.GONE);
    } else {
      mBinding.tvEmptyUpload.setVisibility(View.GONE);
      mBinding.scrollViewUpload.setVisibility(View.VISIBLE);

      if (unFinishedCount != 0) {
        mBinding.btnEditUploading.setVisibility(View.VISIBLE);
      } else {
        mBinding.btnEditUploading.setVisibility(View.GONE);
      }

      if (mFinishedFilesFromDB.size() != 0) {
        mBinding.btnEditUploaded.setVisibility(View.VISIBLE);
      } else {
        mBinding.btnEditUploaded.setVisibility(View.GONE);
      }

      initEditClick();

    }
  }

  /**
   * 编辑事件
   *
   * @param
   * @return
   */
  private void initEditClick() {
    // 正在下载   编辑
    mBinding.btnEditUploading.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if (isUploadingEdit) {
          isUploadingEdit = false;
          mBinding.btnSelectallUploading.setVisibility(View.GONE);
          mBinding.btnEditUploading.setText("编辑");
          setLoadingCheckVisibility(false);
        } else {
          isUploadingEdit = true;
          mBinding.btnSelectallUploading.setVisibility(View.VISIBLE);
          mBinding.btnEditUploading.setText("取消");
          setLoadingCheckVisibility(true);
        }

        initDeleteBtnClick(isUploadingEdit, isUploadedEdit);

      }
    });

    // 全选
    mBinding.btnSelectallUploading.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isLoadingSelectAll) {
          isLoadingSelectAll = false;

          for (int i = 0; i < mUploadUnfinishAdapter.getCount(); i++) {
            mUploadUnfinishAdapter.checkedMap.put(i, false);
          }

          mUploadUnfinishAdapter.notifyDataSetChanged();
        } else {
          isLoadingSelectAll = true;

          for (int i = 0; i < mUploadUnfinishAdapter.getCount(); i++) {
            mUploadUnfinishAdapter.checkedMap.put(i, true);
          }

          mUploadUnfinishAdapter.notifyDataSetChanged();

        }

        initDeleteBtnClick(isUploadingEdit, isUploadedEdit);
      }
    });

    // 下载完成 编辑
    mBinding.btnEditUploaded.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isUploadedEdit) {
          isUploadedEdit = false;
          mBinding.btnSelectallUploaded.setVisibility(View.GONE);
          mBinding.btnEditUploaded.setText("编辑");

          setLoadedCheckVisibility(false);

        } else {
          isUploadedEdit = true;
          mBinding.btnSelectallUploaded.setVisibility(View.VISIBLE);
          mBinding.btnEditUploaded.setText("取消");

          setLoadedCheckVisibility(true);
        }

        initDeleteBtnClick(isUploadingEdit, isUploadedEdit);

      }
    });

    // 全选
    mBinding.btnSelectallUploaded.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isLoadedSelectAll) {
          isLoadedSelectAll = false;

          for (int i = 0; i < mFinishUploadAdapter.getCount(); i++) {
            mFinishUploadAdapter.checkedMap.put(i, false);
          }
          mFinishUploadAdapter.notifyDataSetChanged();
        } else {
          isLoadedSelectAll = true;

          for (int i = 0; i < mFinishUploadAdapter.getCount(); i++) {
            mFinishUploadAdapter.checkedMap.put(i, true);
          }
          mFinishUploadAdapter.notifyDataSetChanged();
        }

        initDeleteBtnClick(isUploadingEdit, isUploadedEdit);
      }
    });

  }

  /**
   * 删除按钮事件
   *
   * @param
   * @return
   */
  private void initDeleteBtnClick(Boolean isLoadingEdit, Boolean isLoadedEdit) {
    if (isLoadingEdit || isLoadedEdit) {
      mBinding.rlDelete.setVisibility(View.VISIBLE);
    } else {
      mBinding.rlDelete.setVisibility(View.GONE);
    }

    mBinding.btnDeleteUpload.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //下载中
        mSelectAllUnfinishFile.clear();
        mSelectAllFinishedFile.clear();

        if (mUploadUnfinishAdapter != null) {
          for (int i = 0; i < mUploadUnfinishAdapter.getCount(); i++) {
            if (mUploadUnfinishAdapter.checkedMap.get(i)) {
              mSelectAllUnfinishFile.add(mUploadUnfinishAdapter.getItem(i));
            }
          }
        }

        if (mFinishUploadAdapter != null) {
          //下载完成
          for (int i = 0; i < mFinishUploadAdapter.getCount(); i++) {
            if (mFinishUploadAdapter.checkedMap.get(i)) {
              mSelectAllFinishedFile.add(mFinishUploadAdapter.getItem(i));
            }
          }
        }

        if ((mSelectAllUnfinishFile.size() + mSelectAllFinishedFile.size()) == 0) {
          Toast.makeText(getActivity(), "请先选择文件", Toast.LENGTH_SHORT).show();
          mSelectAllUnfinishFile.clear();
          mSelectAllFinishedFile.clear();
          return;
        }

        mSambaPopWin = new SambaPopupWindow(getActivity(), 4, "isDownload");
        mSambaPopWin.setFocusable(true);
        mSambaPopWin.showAtLocation(mSambaPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);
        mSambaPopWin.setOnPositiveClickListener(new SambaPopupWindow.OnPositiveClickListener() {
          @Override
          public void onPositiveClick() {
            //正在上传
            for (int i = 0; i < mSelectAllUnfinishFile.size(); i++) {
              mUploadUnfinishAdapter.removeContainData(mSelectAllUnfinishFile.get(i), uploaders, mDBUtil1);
            }
            updateTitleState();

            //上传完成
            for (int i = 0; i < mSelectAllFinishedFile.size(); i++) {
              mFinishUploadAdapter.removeContainData(mSelectAllFinishedFile.get(i), mDBUtil1);
            }
            updateFinishFiles();//刷新

            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
            //删除成功后恢复编辑前的状态
            updateEditUi();
          }
        });
      }
    });
  }

  /**
   * 恢复编辑前的UI
   *
   * @param
   * @return
   */
  private void updateEditUi() {
    mBinding.rlDelete.setVisibility(View.GONE);
    mBinding.btnSelectallUploaded.setVisibility(View.GONE);
    mBinding.btnSelectallUploading.setVisibility(View.GONE);
    mBinding.btnEditUploaded.setText("编辑");
    mBinding.btnEditUploading.setText("编辑");
    isLoadedSelectAll = false;
    isUploadedEdit = false;
    isLoadingSelectAll = false;
    isUploadingEdit = false;
    setLoadingCheckVisibility(false);
    setLoadedCheckVisibility(false);
  }

  public void setLoadingCheckVisibility(boolean isCheck) {
    if (mUploadUnfinishAdapter != null) {
      for (int i = 0; i < mUploadUnfinishAdapter.getCount(); i++) {
        if (isCheck) {
          mUploadUnfinishAdapter.checkedMap.put(i, false);
          mUploadUnfinishAdapter.visibleMap.put(i, CheckBox.VISIBLE);
        } else {
          mUploadUnfinishAdapter.checkedMap.put(i, false);
          mUploadUnfinishAdapter.visibleMap.put(i, CheckBox.GONE);
        }
      }
      mUploadUnfinishAdapter.notifyDataSetChanged();
    }

  }

  public void setLoadedCheckVisibility(boolean isCheck) {
    if (mFinishUploadAdapter != null) {
      for (int i = 0; i < mFinishUploadAdapter.getCount(); i++) {
        if (isCheck) {
          mFinishUploadAdapter.checkedMap.put(i, false);
          mFinishUploadAdapter.visibleMap.put(i, CheckBox.VISIBLE);
        } else {
          mFinishUploadAdapter.checkedMap.put(i, false);
          mFinishUploadAdapter.visibleMap.put(i, CheckBox.GONE);
        }
      }
      mFinishUploadAdapter.notifyDataSetChanged();
    }

  }

/*  private void updateUploadBtnState() {

    if (mUploadUnfinishAdapter != null && isAllUpload) {
      if (mUploadUnfinishAdapter.getCount() > 0) {
        isAllUpload = true;
        mBinding.btnAllUpload.setText("全部暂停上传");
      } else {
        isAllUpload = false;
        mBinding.btnAllUpload.setText("全部开始上传");
      }
    }

    System.out.println("isAllUpload = " + isAllUpload);
  }*/

  private void initEvent() {
    updateTitleState();

    if (mUploadUnfinishAdapter.getCount() > 0) {
      // 初始化状态，正在下载状态，暂停状态 INIT = 1; DOWNLOADING = 2; PAUSE = 3;
      mBinding.btnAllUpload.setVisibility(View.VISIBLE);

      /*updateUploadBtnState();*/

    } else {
      mBinding.btnAllUpload.setVisibility(View.GONE);
    }

    mUploadUnfinishAdapter.setOnUploadCircleProgress(new UploadUnfinishAdapter.OnUploadCircleProgress() {
      @Override
      public void onUploadCircleProgress(View v, int position) {
        try {
          circleProgress = (CustomCircleProgress) v;

          FileUploadBean bean = mUploadUnfinishAdapter.getItem(position);

          String sambaPath = bean.getPath();
          String path = bean.getDownloadsKey();

          System.out.println("sambaPath = " + sambaPath + "  path = " + path);

          if (circleProgress.getStatus() == CustomCircleProgress.Status.Starting) {//如果是开始状态
            //正在下载 转为 暂停
            System.out.println("\"上传：\" = " + "暂停上传");
            SambaUtils.pauseDownload(path, uploaders);
            //点击则变成关闭暂停状态
            circleProgress.setStatus(CustomCircleProgress.Status.End);
            mUploadUnfinishAdapter.getItem(position).setState(3);
            mUploadUnfinishAdapter.notifyDataSetChanged();

            /*updateUploadBtnState();*/

            mBinding.btnAllUpload.setText("全部开始上传");
            isAllUpload = false;

          } else {
            // 开始下载

            SambaUtils.UploadTask uploadTask = new SambaUtils.UploadTask(uploaders, getActivity(), mClient, mHandlerUpload);
            uploadTask.execute(path, sambaPath, "1");

            //点击则变成开启状态
            circleProgress.setStatus(CustomCircleProgress.Status.Starting);
            mUploadUnfinishAdapter.getItem(position).setState(2);
            mUploadUnfinishAdapter.notifyDataSetChanged();

            /*updateUploadBtnState();*/
            mBinding.btnAllUpload.setText("全部暂停上传");
            isAllUpload = true;
          }
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
    });

    mBinding.btnAllUpload.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          //全部下载

          if (isAllUpload) {
            //全部暂停
            SambaUtils.mUploadingIndex = 0;
            isAllUpload = false;
            mBinding.btnAllUpload.setText("全部开始上传");

            for (int i = 0; i < mUploadUnfinishAdapter.getCount(); i++) {
              SambaUtils.pauseDownload(uploaders.get(mUploadUnfinishAdapter.getItem(i).getDownloadsKey()).getUrlstr(), uploaders);
              //点击则变成关闭暂停状态
              System.out.println("\"下载：\" = " + "暂停下载");
              mUploadUnfinishAdapter.updateInfo(i, false);
            }
          } else {
            //全部下载：找出暂停的进行开始下载
            isAllUpload = true;
            mBinding.btnAllUpload.setText("全部暂停上传");

            //把所有的状态恢复成等待状态
            if (uploaders.size() > 0) {
              //启动下一个
              int count = 0;
              Iterator it = uploaders.keySet().iterator();//获取所有的健值
              while (it.hasNext()) {
                try {
                  String key;
                  key = (String) it.next();

                  uploaders.get(key).setState(Downloader.WAITING);
                  UploadCacheManager.saveUploaders(getActivity(), uploaders);

                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            }


            for (int i = 0; i < mUploadUnfinishAdapter.getCount(); i++) {

              FileUploadBean bean = mUploadUnfinishAdapter.getItem(i);
              String sambaPath = bean.getPath();
              String path = bean.getDownloadsKey();

              //启动前3个 后面的状态改为等待
              if (i < 3) {
                //暂停-->继续上传
                //点击则变成开启状态
                SambaUtils.UploadTask uploadTask = new SambaUtils.UploadTask(uploaders, getActivity(), mClient, mHandlerUpload);
                uploadTask.execute(path, sambaPath, "1");

                mUploadUnfinishAdapter.getItem(i).setState(2);
                mUploadUnfinishAdapter.notifyDataSetChanged();

              } else {
                mUploadUnfinishAdapter.updateInfo(i, true);
              }
            }

          }

          System.out.println("isAllUpload = " + isAllUpload);
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
    });

  }

  private String getRemoteCurrentPath() {
    return getActivity().getIntent().getStringExtra("CurrentRemoteFilePath");
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Toast.makeText(getActivity(), "请在主界面预览远程文件", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onDestroy() {
    System.out.println("UploadFragment.onDestroy");
    mUploadUnfinishAdapter = null;
    mFinishUploadAdapter = null;
    removeHandleTimer();
    super.onDestroy();
  }

  private void removeHandleTimer() {
    if (mUploadUIHandler != null) {
      if (mUploadUIRunnable != null) {
        mUploadUIHandler.removeCallbacks(mUploadUIRunnable);//移除定时器
        System.out.println("图片备份记录 = " + "定时器移除 onDestroy ");
      }
    }
  }

}