package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

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

import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentDownloadBinding;
import com.fernandocejas.android10.sample.presentation.utils.DownloadCacheManager;
import com.fernandocejas.android10.sample.presentation.utils.UploadCacheManager;
import com.fernandocejas.android10.sample.presentation.view.component.ListViewForScrollView;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.dbhelper.DatabaseUtil;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.CustomCircleProgress;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.SambaUtils;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.google.android.sambadocumentsprovider.nativefacade.SmbClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *      author:
 *      e-mail:
 *      time: 2017/06/22
 *      desc: 下载列表
 *      version: 1.0
 * </pre>
 */
public class DownloadFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
  private FragmentDownloadBinding mBinding;
  private List<FileUploadBean> mFinishedFilesFromDB = null; //来自数据库的上传成功的文件
  private List<FileUploadBean> mSelectAllUnfinishFile, mSelectAllFinishedFile, mSelectAllWaitingFile;
  private CustomCircleProgress circleProgress;
  private ListViewForScrollView mLvDownload, mLvFinishDownload, mLvDownloadWaiting;
  private static DownloadUnfinishAdapter mDownloadUnfinishAdapter;
  private static DownloadFinishedAdapter mFinishDownloadAdapter;
  private TextView mLoadingTitle, mFinishTitle;
  private DatabaseUtil mDBUtil;
  //设置下载线程数为4，这里是我为了方便随便固定的
  private String threadcount = "1";
  // 存放各个下载器
  private static Map<String, Downloader> downloaders = new HashMap<String, Downloader>();
  private static Boolean isAllDownload = true;
  private SambaPopupWindow mSambaPopWin;
  private static SmbClient mClient;
  Boolean isDownloadingEdit = false, isDownloadedEdit = false, isLoadingSelectAll = false, isLoadedSelectAll = false;

  /**
   * 批量上传 同时支持3个AnsyTask操作
   */
  public static Handler mHandlerDownload = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);

      downloaders = AndroidApplication.getDownloaders();
      mClient = AndroidApplication.getSambaClient();

/*      if(!isAllDownload) return;*/

      if (msg.what == Downloader.DOWNLOAD_FINISHED) {
        String url = (String) msg.obj;
        String exceptionFlag = msg.getData().getString("LOADING_EXCEPTION");
        FileUploadBean bean = (FileUploadBean) msg.getData().getSerializable("bean");
        System.out.println("DownloadFragmentHandler handleMessage ==》 url = " + url + "    ==》 uploaders.size = " + downloaders.size());

        //下载完成之后移除文件
        DownloadCacheManager.delete(AndroidApplication.getApplication(), url);
        //下载完之后移除内存downloader
        Downloader downloader = downloaders.get(url);
        if (downloader != null) {
          if (TextUtils.isEmpty(exceptionFlag)) {
            downloader.delete(url, AndroidApplication.getApplication());
            downloader.reset();
            downloaders.remove(url);
          }else {
            downloader.setState(Downloader.FAILED);
            DownloadCacheManager.updateDownloaderInfos(AndroidApplication.getApplication(), url, downloader);
          }
        }

        if(SambaUtils.mDownloadingIndex > 0){
          SambaUtils.mDownloadingIndex--;
        }

        System.out.println("SambaUtils.mDownloadingIndex = " + SambaUtils.mDownloadingIndex);

        if (mDownloadUnfinishAdapter != null) {
          if (TextUtils.isEmpty(exceptionFlag)) {
            //没发生异常
            mDownloadUnfinishAdapter.remove(url);
            mFinishDownloadAdapter.add(bean);
          } else {
            /*mDownloadUnfinishAdapter.remove(url);*/
            mDownloadUnfinishAdapter.updateFailed(url);
            if(SambaUtils.errorNum == 0x01){
              Toast.makeText(AndroidApplication.getApplication(), "源文件不存在或已经被删除", Toast.LENGTH_SHORT).show();
            }
          }
        }

        if (downloaders.size() > 0) {
          //启动下一个
          Boolean isHasLoading = false;//标志是否有传输数据
          Iterator it = downloaders.keySet().iterator();//获取所有的健值
          while (it.hasNext()) {
            try {
              String key;
              key = (String) it.next();

              String sourcePath = key;
              String despath = downloaders.get(key).getLocalfile();

              //暂停状态的任务跳过
              if (downloaders.get(key).getState() == Downloader.PAUSE) continue;

              if(downloaders.get(key).getState() == Downloader.WAITING){

                if(SambaUtils.mDownloadingIndex >= 3){
                  System.out.println("SambaUtils.mDownloadingIndex 超过3个 break循环 = " + SambaUtils.mDownloadingIndex);
                  break;
                }else {
                  //启动下一个等待状态的任务
                  isHasLoading = true;
                  SambaUtils.DownloadTask downloadTask = new SambaUtils.DownloadTask(downloaders, AndroidApplication.getApplication(), mClient, mHandlerDownload);
                  downloadTask.execute(sourcePath, despath, "1");

                  if(mDownloadUnfinishAdapter != null){
                    FileUploadBean bean1 = mDownloadUnfinishAdapter.getNext(key);
                    if(bean1 != null){
                      bean1.setState(2);//更新状态
                      mDownloadUnfinishAdapter.notifyDataSetChanged();
                    }
                  }

                  System.out.println("SambaUtils.mDownloadingIndex = " + SambaUtils.mDownloadingIndex+" 下载文件 onhandler 新启动的 key(urlstr)="+key+" state= "+downloaders.get(key).getState());

                }

              }

              //查询一个有没下载完的文件没有更新状态刷新
              if(mDownloadUnfinishAdapter != null){
                for (int i = 0; i < mDownloadUnfinishAdapter.getCount(); i++) {
                  FileUploadBean bean5 = mDownloadUnfinishAdapter.getItem(i);
                  if(bean5.getState() == Downloader.DOWNLOADING){
                    if(bean5.getSumSize()>0 && (bean5.getSumSize() == bean5.getFinishedSize())){
                      mDownloadUnfinishAdapter.remove(bean5.getDownloadsKey());
                      mFinishDownloadAdapter.add(bean5);
                      System.out.println("DownloadFragmentHandlerUpload handleMessage ==》扫描发现已完成 ");
                    }
                  }
                }
              }

            } catch (Exception e) {
              e.printStackTrace();
            }
          }

          if(!isHasLoading){
            //没有下载数据
            if(mDownloadUnfinishAdapter != null){
              mDownloadUnfinishAdapter.updateDownloadBtnState(false);
            }
          }
        }else {
          SambaUtils.mDownloadingIndex = 0;

          //检查一下是否有刷新异常的文件
          if(mDownloadUnfinishAdapter != null){
            for (int i = 0; i < mDownloadUnfinishAdapter.getCount(); i++) {
              FileUploadBean bean1 = mDownloadUnfinishAdapter.getItem(i);
              if(bean1.getState() == Downloader.FAILED) continue;
              mDownloadUnfinishAdapter.remove(bean1.getDownloadsKey());
              mFinishDownloadAdapter.add(bean1);

            }
          }
        }
      } else if (msg.what == Downloader.DOWNLOAD_LOADING) {
        String url = (String) msg.obj;
        String speed = msg.getData().getString("LOADING_SPEED");
        long completeSize = msg.getData().getLong("LOADING_COMPLETESIZE");

        if (mDownloadUnfinishAdapter != null) {

          //更新btn状态
          mDownloadUnfinishAdapter.updateDownloadBtnState(isAllDownload);

          if(!isAllDownload) return;

          FileUploadBean bean = mDownloadUnfinishAdapter.getNext(url);
          if (bean != null) {
            bean.setLoadingSpeed(speed);
            bean.setFinishedSize(completeSize);
            bean.setState(Downloader.DOWNLOADING);  //保证没有状态及时更新
            mDownloadUnfinishAdapter.notifyDataSetChanged();
          }
        }
      }
    }
  };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public static DownloadFragment newInstance() {
    DownloadFragment fragment = new DownloadFragment();
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_download, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initView();
    mDBUtil = AndroidApplication.getDBUtil();
    initData();
  }

  private void initItemEvent() {
    //下载完成
    mBinding.lvFinishDownload.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //长按删除文件
        mSambaPopWin = new SambaPopupWindow(getActivity(), 4, "isDownload");
        mSambaPopWin.setFocusable(true);
        mSambaPopWin.showAtLocation(mSambaPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);
        mSambaPopWin.setOnPositiveClickListener(new SambaPopupWindow.OnPositiveClickListener() {
          @Override
          public void onPositiveClick() {

            if (mDBUtil != null) {
              mDBUtil.deleteDownloadUrls(mFinishedFilesFromDB.get(position).getPath());
              mFinishedFilesFromDB.remove(position);
              Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
              updateFinishFiles();//刷新
            }
          }
        });
        return true;
      }
    });

    //下载中
    mBinding.lvDownload.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //长按删除文件
        mSambaPopWin = new SambaPopupWindow(getActivity(), 4, "isDownload");
        mSambaPopWin.setFocusable(true);
        mSambaPopWin.showAtLocation(mSambaPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);
        mSambaPopWin.setOnPositiveClickListener(new SambaPopupWindow.OnPositiveClickListener() {
          @Override
          public void onPositiveClick() {
            String url = mDownloadUnfinishAdapter.getItem(position).getDownloadsKey();
            if (TextUtils.isEmpty(url)) return;

            if (downloaders.get(url) != null) {
              downloaders.get(url).delete(url, getActivity());
              downloaders.get(url).reset();
              downloaders.remove(url);
              //mDBUtil.deleteKey(url);//下载完就删除key
            }

            //上传完成之后移除文件
            DownloadCacheManager.delete(getActivity(), url);

            mDownloadUnfinishAdapter.remove(position);

            updateTitleState();

            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();

          }
        });
        return true;
      }
    });

  }

  private void updateFinishFiles() {
    if (mFinishDownloadAdapter != null) {
      mFinishDownloadAdapter.notifyDataSetChanged();
    }

    updateTitleState();
  }

  private void initData() {

    downloaders = AndroidApplication.getDownloaders();

    mFinishedFilesFromDB.clear();

    System.out.println("下载器 DownloadFragment downloaders.size() = " + downloaders.size());

    //查询完成记录
    try {
      mFinishedFilesFromDB.addAll(mDBUtil.queryAll());
    } catch (Exception e) {
      e.printStackTrace();
    }

    if ((downloaders.size()) == 0 && (mFinishedFilesFromDB.size() == 0)) {
      mBinding.tvEmptyDownload.setVisibility(View.VISIBLE);
      mBinding.scrollViewDownload.setVisibility(View.GONE);
      return;
    } else {
      mBinding.tvEmptyDownload.setVisibility(View.GONE);
      mBinding.scrollViewDownload.setVisibility(View.VISIBLE);
    }

    mFinishDownloadAdapter = new DownloadFinishedAdapter(getContext(), mFinishedFilesFromDB, mFinishTitle);
    mLvFinishDownload.setAdapter(mFinishDownloadAdapter);
    updateTitleState();

    // 下载器只有下载的时候才存在，下载完成之后就会删除
    if ((downloaders.size()) == 0) {
      System.out.println("\"提示：\" = " + "暂无下载任务");
      /*mBinding.btnAllDownload.setText("全部开始下载");*/
      mBinding.btnAllDownload.setVisibility(View.GONE);
    } else {
      //为避免异常 启用Task
      new QueryUnFinishDataTask().execute();
    }

    initItemEvent();

  }

  /**
   * 查询正在下载中的记录
   *
   * @param
   * @return
   */
  class QueryUnFinishDataTask extends AsyncTask<Void, Void, Void> {

    public QueryUnFinishDataTask() {

    }

    @Override
    protected void onPreExecute() {
      mDownloadUnfinishAdapter = new DownloadUnfinishAdapter(getActivity(), mLoadingTitle,mBinding.btnAllDownload);
      mLvDownload.setAdapter(mDownloadUnfinishAdapter);
    }

    @Override
    protected Void doInBackground(Void... params) {
      Iterator it = downloaders.keySet().iterator();//获取所有的健值
      while (it.hasNext()) {
        String key;
        key = (String) it.next();
        // 此处出现异常  java.util.ConcurrentModificationException 原因：调用list.remove()方法导致modCount和expectedModCount的值不一致导致
        try {
          long completeSize = downloaders.get(key).getCompeleteSize();
          long fileSize = downloaders.get(key).getFileSize();

          FileUploadBean fileBean = new FileUploadBean();
          /*fileBean.setName(DocumentMetadata.fromUri(Uri.parse(key),mClient).getDisplayName());*/
          fileBean.setName(downloaders.get(key).getName());

          fileBean.setSumSize(fileSize);
          fileBean.setState(downloaders.get(key).getState());
          fileBean.setFinishedSize(completeSize);
          fileBean.setDownloadsKey(key);

          getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
              //单条数据刷新
              mDownloadUnfinishAdapter.add(fileBean);
            }
          });

        } catch (Exception e) {
          //暂停下载的文件被删除的 此处getName会发生异常
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
    mLvDownload = mBinding.lvDownload;
    mLvFinishDownload = mBinding.lvFinishDownload;
    mFinishedFilesFromDB = new ArrayList<>();
    mSelectAllUnfinishFile = new ArrayList<>();
    mSelectAllFinishedFile = new ArrayList<>();
    mBinding.lvFinishDownload.setOnItemClickListener(this);
    mClient = AndroidApplication.getSambaClient(getActivity());

    Intent intent = new Intent();
    intent.putExtra("SHAREPATH", getRemoteCurrentPath());
    getActivity().setResult(1238, intent);
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
    if (mDownloadUnfinishAdapter != null) {
      unFinishedCount = mDownloadUnfinishAdapter.getCount();
    }

    mLoadingTitle.setText("正在下载(" + (unFinishedCount) + ")");
    try {
      mFinishTitle.setText("下载成功(" + mFinishedFilesFromDB.size() + ")");
    } catch (Exception e) {
      e.printStackTrace();
    }

    if ((unFinishedCount) > 0) {
      mBinding.btnAllDownload.setVisibility(View.VISIBLE);
      /*updateDownloadBtnState();*/
    } else {
      mBinding.btnAllDownload.setVisibility(View.GONE);
    }

    if ((unFinishedCount) == 0 && mFinishedFilesFromDB.size() == 0) {
      mBinding.tvEmptyDownload.setVisibility(View.VISIBLE);
      mBinding.scrollViewDownload.setVisibility(View.GONE);
    } else {
      mBinding.tvEmptyDownload.setVisibility(View.GONE);
      mBinding.scrollViewDownload.setVisibility(View.VISIBLE);

      if ((unFinishedCount) != 0) {
        mBinding.btnEditDownloading.setVisibility(View.VISIBLE);
      } else {
        mBinding.btnEditDownloading.setVisibility(View.GONE);
      }

      if (mFinishedFilesFromDB.size() != 0) {
        mBinding.btnEditDownloaded.setVisibility(View.VISIBLE);
      } else {
        mBinding.btnEditDownloaded.setVisibility(View.GONE);
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
    mBinding.btnEditDownloading.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isDownloadingEdit) {
          isDownloadingEdit = false;
          mBinding.btnSelectallDownloading.setVisibility(View.GONE);
          mBinding.btnEditDownloading.setText("编辑");
          setLoadingCheckVisibility(false);
        } else {
          isDownloadingEdit = true;
          mBinding.btnEditDownloading.setText("取消");
          mBinding.btnSelectallDownloading.setVisibility(View.VISIBLE);
          setLoadingCheckVisibility(true);
        }

        initDeleteBtnClick(isDownloadingEdit, isDownloadedEdit);
      }
    });

    // 全选
    mBinding.btnSelectallDownloading.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isLoadingSelectAll) {
          isLoadingSelectAll = false;

          for (int i = 0; i < mDownloadUnfinishAdapter.getCount(); i++) {
            mDownloadUnfinishAdapter.checkedMap.put(i, false);
          }
          mDownloadUnfinishAdapter.notifyDataSetChanged();

        } else {
          isLoadingSelectAll = true;

          for (int i = 0; i < mDownloadUnfinishAdapter.getCount(); i++) {
            mDownloadUnfinishAdapter.checkedMap.put(i, true);
          }
          mDownloadUnfinishAdapter.notifyDataSetChanged();

        }

      }
    });

    // 下载完成 编辑
    mBinding.btnEditDownloaded.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if (isDownloadedEdit) {
          isDownloadedEdit = false;
          mBinding.btnSelectallDownloaded.setVisibility(View.GONE);
          mBinding.btnEditDownloaded.setText("编辑");
          setLoadedCheckVisibility(false);
        } else {
          isDownloadedEdit = true;
          mBinding.btnSelectallDownloaded.setVisibility(View.VISIBLE);
          mBinding.btnEditDownloaded.setText("取消");
          setLoadedCheckVisibility(true);
        }

        initDeleteBtnClick(isDownloadingEdit, isDownloadedEdit);

      }
    });

    // 全选
    mBinding.btnSelectallDownloaded.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isLoadedSelectAll) {
          isLoadedSelectAll = false;

          for (int i = 0; i < mFinishDownloadAdapter.getCount(); i++) {
            mFinishDownloadAdapter.checkedMap.put(i, false);
          }
          mFinishDownloadAdapter.notifyDataSetChanged();
        } else {
          isLoadedSelectAll = true;

          for (int i = 0; i < mFinishDownloadAdapter.getCount(); i++) {
            mFinishDownloadAdapter.checkedMap.put(i, true);
          }
          mFinishDownloadAdapter.notifyDataSetChanged();
        }
      }
    });

    initDeleteBtnClick(isDownloadingEdit, isDownloadedEdit);

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

    mBinding.btnDeleteDownload.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //下载中
        mSelectAllUnfinishFile.clear();
        mSelectAllFinishedFile.clear();

        if (mDownloadUnfinishAdapter != null) {
          for (int i = 0; i < mDownloadUnfinishAdapter.getCount(); i++) {
            if (mDownloadUnfinishAdapter.checkedMap.get(i)) {
              mSelectAllUnfinishFile.add(mDownloadUnfinishAdapter.getItem(i));
            }
          }
        }

        if (mFinishDownloadAdapter != null) {
          //下载完成
          for (int i = 0; i < mFinishDownloadAdapter.getCount(); i++) {
            if (mFinishDownloadAdapter.checkedMap.get(i) != null && mFinishDownloadAdapter.checkedMap.get(i)) {
              mSelectAllFinishedFile.add(mFinishDownloadAdapter.getItem(i));
            }
          }
        }

        if ((mSelectAllUnfinishFile.size() + mSelectAllFinishedFile.size()) == 0) {
          Toast.makeText(getActivity(), "请先选择文件", Toast.LENGTH_SHORT).show();
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
              mDownloadUnfinishAdapter.removeContainData(mSelectAllUnfinishFile.get(i), downloaders, mDBUtil);
            }
            updateTitleState();

            //上传完成
            for (int i = 0; i < mSelectAllFinishedFile.size(); i++) {
              mFinishDownloadAdapter.removeContainData(mSelectAllFinishedFile.get(i), mDBUtil);
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
    mBinding.btnSelectallDownloading.setVisibility(View.GONE);
    mBinding.btnSelectallDownloaded.setVisibility(View.GONE);
    mBinding.btnEditDownloading.setText("编辑");
    mBinding.btnEditDownloaded.setText("编辑");
    isLoadedSelectAll = false;
    isDownloadedEdit = false;
    isLoadingSelectAll = false;
    isDownloadingEdit = false;
    setLoadingCheckVisibility(false);
    setLoadedCheckVisibility(false);
  }

  public void setLoadingCheckVisibility(boolean isCheck) {
    if (mDownloadUnfinishAdapter != null) {
      for (int i = 0; i < mDownloadUnfinishAdapter.getCount(); i++) {
        if (isCheck) {
          mDownloadUnfinishAdapter.checkedMap.put(i, false);
          mDownloadUnfinishAdapter.visibleMap.put(i, CheckBox.VISIBLE);
        } else {
          mDownloadUnfinishAdapter.checkedMap.put(i, false);
          mDownloadUnfinishAdapter.visibleMap.put(i, CheckBox.GONE);
        }
      }
      mDownloadUnfinishAdapter.notifyDataSetChanged();
    }
  }

  public void setLoadedCheckVisibility(boolean isCheck) {
    if (mFinishDownloadAdapter != null) {
      for (int i = 0; i < mFinishDownloadAdapter.getCount(); i++) {
        if (isCheck) {
          mFinishDownloadAdapter.checkedMap.put(i, false);
          mFinishDownloadAdapter.visibleMap.put(i, CheckBox.VISIBLE);
        } else {
          mFinishDownloadAdapter.checkedMap.put(i, false);
          mFinishDownloadAdapter.visibleMap.put(i, CheckBox.GONE);
        }
      }
      mFinishDownloadAdapter.notifyDataSetChanged();
    }

  }

/*  private void updateDownloadBtnState() {
    if (mDownloadUnfinishAdapter != null && isAllDownload) {
      if (mDownloadUnfinishAdapter.getCount() > 0) {
        isAllDownload = true;
        mBinding.btnAllDownload.setText("全部暂停下载");
      } else {
        isAllDownload = false;
        mBinding.btnAllDownload.setText("全部开始下载");
      }
    }

    System.out.println("isAllUpload = " + isAllDownload);
  }*/

  private void initEvent() {
    updateTitleState();

    if (mDownloadUnfinishAdapter.getCount() > 0) {
      mBinding.btnAllDownload.setVisibility(View.VISIBLE);
      // 初始化状态，正在下载状态，暂停状态 INIT = 1; DOWNLOADING = 2; PAUSE = 3;

      /*updateDownloadBtnState();*/

    } else {
      mBinding.btnAllDownload.setVisibility(View.GONE);
    }

    mDownloadUnfinishAdapter.setOnUploadCircleProgress(new DownloadUnfinishAdapter.OnUploadCircleProgress() {
      @Override
      public void onUploadCircleProgress(View v, int position) {

        try {
          circleProgress = (CustomCircleProgress) v;

          FileUploadBean bean = mDownloadUnfinishAdapter.getItem(position);

          String sambaPath = bean.getPath();
          String path = bean.getDownloadsKey();

          if (circleProgress.getStatus() == CustomCircleProgress.Status.Starting) {//如果是开始状态
            SambaUtils.pauseDownload(path, downloaders);
            //点击则变成关闭暂停状态
            System.out.println("\"下载CircleProgress：\" = " + "暂停下载");
            circleProgress.setStatus(CustomCircleProgress.Status.End);
            //定义三种下载的状态：初始化状态，正在下载状态，暂停状态 INIT = 1; DOWNLOADING = 2; PAUSE = 3;
            mDownloadUnfinishAdapter.getItem(position).setState(3);
            mDownloadUnfinishAdapter.notifyDataSetChanged();

            isAllDownload = false;
            mBinding.btnAllDownload.setText("全部开始下载");

          } else {

            SambaUtils.DownloadTask downloadTask = new SambaUtils.DownloadTask(downloaders, getActivity(), mClient, mHandlerDownload);
            // UploadUtils.genLocalPath(mUnfinishFiles.get(position).getName(), AppConstant.SAMBA_DOWNLOAD_PATH)
            downloadTask.execute(path, sambaPath, threadcount);
            //点击则变成开启状态
            circleProgress.setStatus(CustomCircleProgress.Status.Starting);
            mDownloadUnfinishAdapter.getItem(position).setState(2);
            mDownloadUnfinishAdapter.notifyDataSetChanged();

            isAllDownload = true;
            mBinding.btnAllDownload.setText("全部暂停下载");

          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    mBinding.btnAllDownload.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          //全部下载

          if (isAllDownload) {
            //全部暂停
            isAllDownload = false;
            mBinding.btnAllDownload.setText("全部开始下载");

            SambaUtils.mDownloadingIndex = 0;

            System.out.println("isAllDownload = " + isAllDownload);

            for (int i = 0; i < mDownloadUnfinishAdapter.getCount(); i++) {
              FileUploadBean bean = mDownloadUnfinishAdapter.getItem(i);
              SambaUtils.pauseDownload(downloaders.get(bean.getDownloadsKey()).getUrlstr(), downloaders);
              //点击则变成关闭暂停状态
              mDownloadUnfinishAdapter.updateInfo(i, false);
            }
          } else {
            //全部下载：找出暂停的进行开始下载
            isAllDownload = true;
            mBinding.btnAllDownload.setText("全部暂停下载");

            //把所有的状态恢复成等待状态
            if (downloaders.size() > 0) {
              //启动下一个
              int count = 0;
              Iterator it = downloaders.keySet().iterator();//获取所有的健值
              while (it.hasNext()) {
                try {
                  String key;
                  key = (String) it.next();

                  downloaders.get(key).setState(Downloader.WAITING);
                  DownloadCacheManager.saveDownloaders(getActivity(), downloaders);

                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            }

            System.out.println("isAllDownload = " + isAllDownload);

            for (int i = 0; i < mDownloadUnfinishAdapter.getCount(); i++) {
              // pause = 3 downloading = 2
              FileUploadBean bean = mDownloadUnfinishAdapter.getItem(i);

              String sambaPath = bean.getPath();
              String path = bean.getDownloadsKey();

              //暂停-->继续下载
              if (i < 3) {
                SambaUtils.DownloadTask downloadTask = new SambaUtils.DownloadTask(downloaders, getActivity(), mClient, mHandlerDownload);
                downloadTask.execute(path, sambaPath, threadcount);

                mDownloadUnfinishAdapter.getItem(i).setState(2);
                mDownloadUnfinishAdapter.notifyDataSetChanged();
              } else {
                mDownloadUnfinishAdapter.updateInfo(i, true);
              }
            }
          }
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
    try {

      File file = new File(mFinishedFilesFromDB.get(position).getPath());

      if (file.length() <= 0) {
        Toast.makeText(getActivity(), "文件已损坏", Toast.LENGTH_SHORT).show();
      } else {
        SambaUtils.openFile(getActivity(), file);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  public void onDestroy() {
    System.out.println("DownloadFragment.onDestroy");
    mDownloadUnfinishAdapter = null;
    mFinishDownloadAdapter = null;
    super.onDestroy();
  }
}