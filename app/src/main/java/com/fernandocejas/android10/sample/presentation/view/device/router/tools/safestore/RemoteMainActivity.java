package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.system.StructStat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.net.RouterRestApi;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityRemoteMainBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.DownloadCacheManager;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.utils.UploadCacheManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.TitleBar;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.dbhelper.DatabaseUtil;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.SambaUtils;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.UploadUtils;
import com.google.android.sambadocumentsprovider.ShareManager;
import com.google.android.sambadocumentsprovider.TaskManager;
import com.google.android.sambadocumentsprovider.base.AuthFailedException;
import com.google.android.sambadocumentsprovider.base.OnTaskFinishedCallback;
import com.google.android.sambadocumentsprovider.cache.DocumentCache;
import com.google.android.sambadocumentsprovider.document.DocumentMetadata;
import com.google.android.sambadocumentsprovider.document.LoadChildrenTask;
import com.google.android.sambadocumentsprovider.mount.MountServerTask;
import com.google.android.sambadocumentsprovider.nativefacade.SmbClient;
import com.qtec.mapp.model.rsp.GetExtraNetPortResponse;
import com.qtec.router.model.rsp.GetSambaAccountResponse;
import com.qtec.router.model.rsp.QueryDiskStateResponse;
import com.yanzhenjie.album.Album;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static android.system.OsConstants.S_ISDIR;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/08/15
 *      desc: 安全存储主界面
 *      version: 1.0
 *      info:
 * </pre>
 */

public class RemoteMainActivity extends BaseActivity implements IGetSambaAccountView, IQueryDiskStateView, IGetExtraNetPortView, View.OnClickListener, ExpandableListView.OnGroupExpandListener {
  /**
   * ======= common variable =====
   */
  private ActivityRemoteMainBinding mBinding;
  private static boolean isExit_Back = true; // true=退出,false=后退
  private DatabaseUtil mDBUtil;
  private Boolean isInitFileFailed = false;  // 判断是否接了u盘
  public static final int REQUEST_FORMATING_CODE = 0x09, ACTIVITY_REQUEST_SELECT_PHOTO = 0x11;
  private String deleteFolderPath; //记录删除的当前的文件夹及位置
  private Boolean isDeleteFolder = false;
  private Boolean isFirstRefresh = true;//标志是否首次刷新
  private Boolean isRefresh = false;
  private int oldVisibleItem;

  /**
   * ======= remote variable =====
   */
  private String remoteRootIp = "smb://192.168.1.1/qtec/";//根目录
  private String remoteCurrentPath = remoteRootIp;
  private String remoteCurrentSharePath;
  private SambaPopupWindow mSambaPopWin;
  private DocumentMetadata mRemoteCurrentMedeta;
  private boolean isFileSortPressed = false;// 标记顶部标题栏是否点击
  private RemoteMainAdapter adapter = null;
  private SortFilePopWindow mSortFilePopWin;
  private UploadPopWindow mUploadPopWindow;
  private EditMorePopWindow mEditMorePopWindow;
  private List<String> mDeleteFolderList;//记录删除文件夹时里面的文件夹的路径
  private final int isSortByTime = 2;
  private final int isSortByName = 1;
  private final int isSortByDefault = 0;
  private int mSortFlag = 0; //默认先排文件夹再文件
  protected final static int REQUEST_CODE_UPLOAD_FILE = 1234;
  protected final static int REQUEST_UPLOAD_OTHER_FILE = 1235;
  protected final static int REQUEST_MOVE_FILE = 1236;
  protected final static int REQUEST_SEARCH_FILE = 1237;
  protected final static int REQUEST_TRANSMIT_FILE = 1238;
  protected final static int REQUEST_PICTURE_RESTORE = 1239;//照片备份
  // 存放已选择的网盘文件列表
  private List<FileBean> selectSmbFiles = null;
  protected boolean isEdit = false;
  protected boolean isSelectAll = false;
  // 存放各个下载器
  private Map<String, Downloader> downloaders = new HashMap<String, Downloader>();
  private Map<String, Downloader> uploaders = new HashMap<String, Downloader>();
  private MyProgressDialog mQueryDiskDialog;
  private Handler mHandler,mTransHandler;
  private Runnable mRunnbale,mTransRunnable;


  /**
   * ======= local variable =====
   */
  private Boolean isClickBatch = false; //点击了批量删除的按钮
  private int mQueryDiskStateTimes = 0; //查询磁盘状态的次数

  /******samba google 改造 ********/
  private DocumentCache mCache;
  private TaskManager mTaskManager;
  private ShareManager mShareManager;
  private SmbClient mClient;

  @Inject
  GetSambaAccountPresenter mGetSambaAccountPresenter;
  @Inject
  public QueryDiskStatePresenter mQueryDiskStatePresenter;
  @Inject
  GetExtraNetPortPresenter mGetPortPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_remote_main);
    initializeInjector();
    initPresenter();

    initTitleBar("所有文件");

    SambaUtils.mUserPause = true;

    queryDiskState();
  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
    mGetSambaAccountPresenter.setView(this);
    mGetPortPresenter.setView(this);
    mQueryDiskStatePresenter.setView(this);
  }

  /**
   * 请求 samba的登录账号和密码
   *
   * @param
   * @return
   */
  private void querySambaLoginInfo() {
    mGetSambaAccountPresenter.getSambaAccount();
  }

  /**
   * 查询磁盘状态
   *
   * @param
   * @return
   */
  private void queryDiskState() {
    mQueryDiskStatePresenter.queryDiskState();

    if (mQueryDiskDialog == null) {
      mQueryDiskDialog = new MyProgressDialog(RemoteMainActivity.this);
      mQueryDiskDialog.setMessage("查询磁盘状态中…");
      mQueryDiskDialog.show();
    }
    System.out.println("samba 查询磁盘状态");
  }

  /**
   * 外网访问查询端口号
   *
   * @param
   * @return
   */
  private void queryExtraNetPort() {
    mGetPortPresenter.getExtraNetPort();
  }


  /**
   * 标题栏点击事件
   *
   * @param
   * @return
   */
  private void initTopBarClick() {
    initCenterTitle("所有文件", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //弹出选项opWindow
        if (isFileSortPressed) {
          isFileSortPressed = false;
          mSortFilePopWin.getOuterLayout().setVisibility(View.GONE);
        } else {

          mSortFilePopWin = new SortFilePopWindow(RemoteMainActivity.this, mSortFlag);
          mSortFilePopWin.showAtLocation(mSortFilePopWin.getOuterLayout(), Gravity.NO_GRAVITY, 0, 0);

          mSortFilePopWin.setOnTimeClickListener(new SortFilePopWindow.OnTimeClickListener() {
            @Override
            public void timeClickListener() {
              // 时间分类
              isFileSortPressed = true;
              mSortFlag = isSortByTime;
              refreshEvent();
            }
          });

          mSortFilePopWin.setOnNameClickListener(new SortFilePopWindow.OnNameClickListener() {
            @Override
            public void nameClickListener() {
              //按名称排序
              isFileSortPressed = true;
              mSortFlag = isSortByName;
              refreshEvent();
            }
          });

          mSortFilePopWin.setOnDefaultClickListener(new SortFilePopWindow.OnDefaultClickListener() {
            @Override
            public void defaultClickListener() {
              isFileSortPressed = true;
              mSortFlag = isSortByDefault;
              refreshEvent();
            }
          });
        }
      }
    });

    mTitleBar.setRightAsMore(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 上传
        uploadBtnClick();
      }
    }, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //编辑
        editPopWinClick();
      }
    });

    //左上角返回按钮
    mTitleBar.setOnSambaBackListener(new TitleBar.OnSambaBackClickListener() {
      @Override
      public void onSambaBackClick() {
        if (isExit_Back) {
          continueTransmitFile(2000);
          RemoteMainActivity.this.finish();
        } else {
          if (remoteCurrentSharePath.contains("/")) {
            remoteCurrentSharePath = remoteCurrentSharePath.substring(0, remoteCurrentSharePath.lastIndexOf("/"));
          }
          System.out.println("左上角 回退 当前路径 remoteCurrentSharePath = " + remoteCurrentSharePath);
          refreshEvent();
        }
      }
    });

  }

  private void initView() {

    selectSmbFiles = new ArrayList<FileBean>();
    mDeleteFolderList = new ArrayList<>();

    mDBUtil = AndroidApplication.getDBUtil();

    downloaders = AndroidApplication.getDownloaders();
    uploaders = AndroidApplication.getUploaders();

    remoteRootIp = LoginConfig.LAN_REMOTE_ROOT_IP;

    initSambaGoogle();

    findViewById(R.id.img_new_folder).setOnClickListener(this);
    mBinding.btnLoading.setOnClickListener(this);

    mBinding.lvRemoteFile.setOnGroupExpandListener(this);

    mBinding.etSearch.setOnClickListener(this);

  }

  private void initData() {
    tryMount();
  }

  private void initSambaGoogle() {
    mCache = AndroidApplication.getDocumentCache(this);
    mTaskManager = AndroidApplication.getTaskManager(this);
    mShareManager = AndroidApplication.getServerManager(this);
    mClient = AndroidApplication.getSambaClient(this);

    if (mClient != null) {
      if (GlobleConstant.isSambaExtranetAccess) {
        //外网
        System.out.println("initSambaGoogle native: OS_PORT = " + LoginConfig.OS_PORT);
      }
    }
  }

  /**
   * 连接服务器
   *
   * @param
   * @return
   */
  private void tryMount() {
    final String[] path = parseSharePath();
    final String host = path[0];
    final String share = path[1];

    System.out.println("登录信息： host= " + host + "  share= " + share + "  remoteRootIp= " + remoteRootIp);

    final DocumentMetadata metadata = DocumentMetadata.createShare(host, share);

    mCache.put(metadata);

    adapter = new RemoteMainAdapter(RemoteMainActivity.this);
    mBinding.lvRemoteFile.setAdapter(adapter);

    final OnTaskFinishedCallback<Void> callback = new OnTaskFinishedCallback<Void>() {
      @Override
      public void onTaskFinished(int status, @Nullable Void item, Exception exception) {
        switch (status) {
          case SUCCEEDED:
            System.out.println("samba 获取列表成功");

            remoteCurrentSharePath = share;
            mRemoteCurrentMedeta = metadata;

            System.out.println("onTaskFinished 回退 当前路径 remoteCurrentSharePath = " + remoteCurrentSharePath);

            if (PrefConstant.getPictureRestoreState()) {

              //备份开关开启但是被删除的场景
              String newName = "来自：" + DeviceUtils.getModel();
              if (GlobleConstant.isSambaExtranetAccess){
                SambaUtils.createSambaFolder(mClient, LoginConfig.OS_REMOTE_ROOT_IP, newName);
              }else {
                SambaUtils.createSambaFolder(mClient, LoginConfig.LAN_REMOTE_ROOT_IP, newName);
              }

              //总开关打开 启动定时器
              if (!GlobleConstant.isStopRestorePicture) {
                UploadUtils.startPicRestoreTimer(mClient, RemoteMainActivity.this);//检查图片备份定时器
              }
            }

            dealWithRefresh();
            break;
          case FAILED:
            mCache.remove(metadata.getUri());
            if ((exception instanceof AuthFailedException)) {
              Toast.makeText(RemoteMainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
              finish();
            } else {
              Toast.makeText(RemoteMainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
              finish();
            }
            break;
        }
      }
    };

    final MountServerTask task = new MountServerTask(
        metadata, host, LoginConfig.USER_NAME, LoginConfig.USER_PWD, mClient, mCache, mShareManager, callback);

    mTaskManager.runTask(metadata.getUri(), task);

  }

  private void initSmbFileList(DocumentMetadata metadata) {
    try {
      // remoteRootIp: smb://192.168.1.1/qtec/
      Uri uri = metadata.getUri();
      if (uri == null) return;
      remoteCurrentPath = uri.toString() + "/";

      System.out.println("当前路径：remoteCurrentPath = " + remoteCurrentPath);
      System.out.println("当前路径：remoteCurrentSharePath = " + remoteCurrentSharePath + " rootIp = " + remoteRootIp);

      if (remoteCurrentPath.equals(remoteRootIp))
        isExit_Back = true;
      else
        isExit_Back = false;

      System.out.println("当前路径：isExit_Back = " + isExit_Back);

      if(remoteCurrentSharePath.contains("/")){
        //取/以后的部分
        mTitleBar.getCenterTextView().setText(remoteCurrentSharePath.substring(remoteCurrentSharePath.lastIndexOf("/")+1,remoteCurrentSharePath.length()));
      }else {
        mTitleBar.getCenterTextView().setText("所有文件");
      }

      new initSmbFileTask_1(metadata).executeOnExecutor(Executors.newCachedThreadPool());
    } catch (Exception e) {
      e.printStackTrace();
      ToastUtils.showShort("不合法的远程文件路径，请重新连接");
    }

  }

  private String[] parseSharePath() {
    if (GlobleConstant.isSambaExtranetAccess) {
      //外网
      LoginConfig.OS_REMOTE_ROOT_IP = "smb://" + LoginConfig.OS_IP + ":" + LoginConfig.OS_PORT + "/qtec/";
      remoteRootIp = LoginConfig.OS_REMOTE_ROOT_IP;
      System.out.println("samba登录信息： remoteRootIp 外网 = " + remoteRootIp);
    } else {
      //身份认证
      LoginConfig.LAN_REMOTE_ROOT_IP = "smb://" + LoginConfig.LAN_IP + "/qtec/";

      remoteRootIp = LoginConfig.LAN_REMOTE_ROOT_IP;

      System.out.println("samba登录信息： remoteRootIp 内网 = " + remoteRootIp);
    }

    final String path = remoteRootIp;

    if (path.startsWith("\\")) {
      // Possibly Windows share path
      final int endCharacter = path.endsWith("\\") ? path.length() - 1 : path.length();
      final String[] components = path.substring(2, endCharacter).split("\\\\");
      return components.length == 2 ? components : null;
    } else {
      // Try SMB URI
      final Uri smbUri = Uri.parse(path);

      final String host = smbUri.getAuthority();
      if (TextUtils.isEmpty(host)) {
        return null;
      }

      final List<String> pathSegments = smbUri.getPathSegments();
      if (pathSegments.size() != 1) {
        return null;
      }
      final String share = pathSegments.get(0);
      return new String[]{host, share};
    }
  }

  private void initEvent() {

    //单击文件夹进入子目录
    adapter.setOnFolderListener(new RemoteMainAdapter.OnFolderClickListener() {
      @Override
      public void onFolderClick(View v, int position, String path, Uri key, Boolean isFolder) {

        if (isEdit) {
          //编辑状态下不支持操作
          if (adapter != null) {

            if (adapter.checkedMap.get(position)) {
              adapter.checkedMap.put(position, false);
            } else {
              adapter.checkedMap.put(position, true);
            }

            adapter.notifyDataSetChanged();
          }
          return;
        }

        System.out.println("单击文件夹路径进入：" + path);

        try {
          // 如果该文件是可读的，我们进去查看文件

          FileBean group = adapter.getGroup(position);

          if (isFolder) {
            // 如果是文件夹，则直接进入该文件夹，查看文件目录
            remoteCurrentSharePath = remoteCurrentSharePath + "/" + group.getName();

            System.out.println("isFolder子项点击 回退 当前路径 remoteCurrentSharePath = " + remoteCurrentSharePath);

            refreshEvent();

            System.out.println("group.getName() = " + group.getName());

          } else {
            /**
             * 文件预览
             * 原理：下载到本地指定的某个目录，然后定期清除缓存,无需支持断点续载
             */
            //损坏的文件提示不可下载
            if (("0K".equals(group.getSize()))) {
              Toast.makeText(RemoteMainActivity.this, "文件已损坏", Toast.LENGTH_SHORT).show();
              return;
            }
            // 判断文件是否预览过 是的话直接从本地取 否的话下载
            try {
              File f = new File(UploadUtils.genLocalPath(path, AppConstant.SAMBA_DOWNLOAD_CACHE_PATH));
              if (f.exists() && f.length() > 0 && f.length() == group.getLongSize() ) {
                SambaUtils.openFile(RemoteMainActivity.this, f);
                return;
              } else {
                new SambaUtils.DownloadTaskForPreview(mClient, path, RemoteMainActivity.this).execute();
              }
            } catch (Exception e) {
              e.printStackTrace();
            }

            //先判断缓存路径是否文件操作30个，是的话进行删除
            if (SambaUtils.getCacheCount() >= 30) {
              //太大会出现删除时间过长
              UploadUtils.deleteLocalFile(AppConstant.SAMBA_DOWNLOAD_CACHE_PATH);
            }

          }
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
    });

    //单击childView的Item选项
    adapter.setOnDetailListener(new RemoteMainAdapter.OnDetailClickListener() {
      @Override
      public void onDetailClick(View view, int groupPosition, int childPosition, Boolean isFolder) {
        itemViewClickEvent(view, groupPosition, childPosition, isFolder);
      }
    });

    mBinding.lvRemoteFile.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(AbsListView view, int scrollState) {
      }

      @Override
      public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem > oldVisibleItem) {
          // 向上滑动
          System.out.println("onScroll 向上滑动 firstVisibleItem-oldVisibleItem= "+(firstVisibleItem-oldVisibleItem)+" visibleItemCount= "+visibleItemCount);
          mBinding.llSearch.setVisibility(View.GONE);
        }else if (firstVisibleItem < oldVisibleItem) {
          // 向下滑动
          System.out.println("onScroll 向下滑动 oldVisibleItem-firstVisibleItem= "+(oldVisibleItem-firstVisibleItem)+" visibleItemCount= "+visibleItemCount);
          mBinding.llSearch.setVisibility(View.VISIBLE);
        }

        oldVisibleItem = firstVisibleItem;
      }
    });
  }

  /**
   * 单个文件操作：下载 重命名 移动 删除
   *
   * @param
   * @return
   */
  private void itemViewClickEvent(View v, int groupPosition, int childPosition, Boolean isFolder) {

    FileBean bean = adapter.getGroup(groupPosition);

    switch (childPosition) {
      case 0:
        //下载

        //损坏的文件提示不可下载
        if (("0K".equals(bean.getSize()))) {
          Toast.makeText(RemoteMainActivity.this, "文件已损坏，暂不支持下载", Toast.LENGTH_SHORT).show();
          //关闭展开
          mBinding.lvRemoteFile.collapseGroup(groupPosition);
          return;
        }

        System.out.println("groupPosition+\"  \"+childPosition = " + groupPosition + "  " + childPosition);
        // 下载文件
        //设置下载线程数为4，这里是我为了方便随便固定的
        String threadcount = "1";
        try {
          System.out.println("isDownloading = " + "开始下载");
          //每次下载之前检查一下数据库里的url是否已经删除
          String urlstr = bean.getPath();

          if (downloaders.get(urlstr) != null) {
            //初始化状态1，正在下载状态2，暂停状态3
            if (downloaders.get(urlstr).getState() != 1) {
              Toast.makeText(this, "下载任务已存在", Toast.LENGTH_SHORT).show();
              //关闭展开
              mBinding.lvRemoteFile.collapseGroup(groupPosition);
              return;
            } else {
              downloaders.get(urlstr).delete(urlstr, this);
              downloaders.get(urlstr).reset();
              downloaders.remove(urlstr);
              //mDBUtil.deleteKey(urlstr);//下载完就删除key
              DownloadCacheManager.delete(this, urlstr);
            }
          }

          //看下是否已经下载过
          File f = new File(UploadUtils.genLocalPath(bean.getName(), AppConstant.SAMBA_DOWNLOAD_PATH));
          if (f.exists() && f.length() > 0 && f.length() == bean.getLongSize()) {
            //弹窗
            System.out.println("下载 弹窗 文件已存在");
            mSambaPopWin = new SambaPopupWindow(RemoteMainActivity.this, 5, "");
            mSambaPopWin.setFocusable(true);
            mSambaPopWin.showAtLocation(mSambaPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);

            mSambaPopWin.setOnPositiveClickListener(new SambaPopupWindow.OnPositiveClickListener() {
              @Override
              public void onPositiveClick() {

                try {
                  //判断该文件是否存在
                  DocumentMetadata.fromUri(Uri.parse(urlstr), mClient).getSize();
                } catch (IOException e) {
                  //文件不存在
                  e.printStackTrace();
                  mBinding.lvRemoteFile.collapseGroup(groupPosition);
                  Toast.makeText(RemoteMainActivity.this, "下载失败，请检查文件是否存在", Toast.LENGTH_SHORT).show();
                  return;
                }

                //覆盖原文件
                System.out.println("下载 弹窗 继续下载");
                System.out.println("\"下载:\" = " + "开始下载");
                Toast.makeText(RemoteMainActivity.this, "成功添加下载任务", Toast.LENGTH_SHORT).show();

                String path = bean.getPath();

                Downloader downloader = downloaders.get(path);

                String desPath = UploadUtils.genLocalPath(bean.getName(), AppConstant.SAMBA_DOWNLOAD_PATH);

                if (downloader == null) {

                  downloader = new Downloader(path, desPath, 1);
                  downloader.setState(Downloader.WAITING);//等待状态
                  downloader.setCompeleteSize(0);
                  downloader.setName(bean.getName());
                  downloader.setFileSize(bean.getLongSize());
                  downloader.setUrlstr(path);
                  downloader.setLocalfile(desPath);
                  downloaders.put(path, downloader);
                  //排除大小为0的损坏的文件
                  DownloadCacheManager.putDownloader(RemoteMainActivity.this, path, downloader);//写到文件
                }

                SambaUtils.DownloadTask downloadTask = new SambaUtils.DownloadTask(downloaders, RemoteMainActivity.this, mClient, DownloadFragment.mHandlerDownload);
                downloadTask.execute(bean.getPath(), desPath, threadcount);
              }
            });

            mSambaPopWin.setOnNegativeClickListener(new SambaPopupWindow.OnNegativeClickListener() {
              @Override
              public void onNegtiveClick() {
                //取消
                //关闭展开
                System.out.println("下载 弹窗 取消下载");
                mBinding.lvRemoteFile.collapseGroup(groupPosition);
                return;
              }
            });

          } else {

            try {
              //判断该文件是否存在
              DocumentMetadata.fromUri(Uri.parse(urlstr), mClient).getSize();
            } catch (IOException e) {
              //文件不存在
              e.printStackTrace();
              mBinding.lvRemoteFile.collapseGroup(groupPosition);
              Toast.makeText(RemoteMainActivity.this, "下载失败，请检查文件是否存在", Toast.LENGTH_SHORT).show();
              return;
            }

            System.out.println("\"下载:\" = " + "开始下载");
            Toast.makeText(this, "成功添加下载任务", Toast.LENGTH_SHORT).show();

            String path = bean.getPath();

            Downloader downloader = downloaders.get(path);

            String desPath = UploadUtils.genLocalPath(bean.getName(), AppConstant.SAMBA_DOWNLOAD_PATH);

            if (downloader == null) {

              downloader = new Downloader(path, desPath, 1);
              downloader.setState(Downloader.WAITING);//等待状态
              downloader.setCompeleteSize(0);
              downloader.setName(bean.getName());
              downloader.setFileSize(bean.getLongSize());
              downloader.setUrlstr(path);
              downloader.setLocalfile(desPath);
              downloaders.put(path, downloader);
              //排除大小为0的损坏的文件
              DownloadCacheManager.putDownloader(RemoteMainActivity.this, path, downloader);//写到文件
            }

            SambaUtils.DownloadTask downloadTask = new SambaUtils.DownloadTask(downloaders, RemoteMainActivity.this, mClient, DownloadFragment.mHandlerDownload);
            downloadTask.execute(bean.getPath(), desPath, threadcount);

          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;

      case 1:
        //重命名
        //损坏的文件提示不可下载
        if (("0K".equals(bean.getSize()))) {
          Toast.makeText(RemoteMainActivity.this, "文件已损坏，暂不支持重命名", Toast.LENGTH_SHORT).show();
          //关闭展开
          mBinding.lvRemoteFile.collapseGroup(groupPosition);
          return;
        }

        String oldName = "";

        if (bean.getFileType()) {
          //文件夹的旧文件名截取最后的/
          oldName = bean.getName().substring(0, bean.getName().length());
        } else {
          //文件 截取掉最后一个.之后的
          oldName = bean.getName();
          oldName = oldName.substring(0, oldName.lastIndexOf("."));
        }

        System.out.println("groupPosition+\"  \"+childPosition = " + groupPosition + "  " + childPosition);
        //Toast.makeText(RemoteMainActivity.this, "重命名", Toast.LENGTH_SHORT).show();
        mSambaPopWin = new SambaPopupWindow(RemoteMainActivity.this, 2, oldName);
        mSambaPopWin.setFocusable(true);
        mSambaPopWin.showAtLocation(mSambaPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);

        mSambaPopWin.setOnPositiveClickListener(new SambaPopupWindow.OnPositiveClickListener() {
          @Override
          public void onPositiveClick() {
            String newName = getText(mSambaPopWin.getNewNameEdit());
            //不能包含的字符
            if (newName.contains("\\") ||
                newName.contains("/") || newName.contains(":") ||
                newName.contains("*") || newName.contains("?") ||
                newName.contains("\"") || newName.contains("<") ||
                newName.contains(">") || newName.contains("|")
                ) {
              Toast.makeText(RemoteMainActivity.this, "文件名不能包含下列任何字符:\n" + " \\  / : * ? \" < > | ", Toast.LENGTH_SHORT).show();
              return;
            }

            new RenameTask(groupPosition, newName, isFolder).execute();
          }
        });
        break;

      case 2:
        //移动
        String[] movePathList = new String[1];
        movePathList[0] = bean.getPath();
        Bundle bundle = new Bundle();
        bundle.putStringArray("MOVE_FILE_PATH_LIST", movePathList);
        bundle.putString("CurrentRemoteFilePath", remoteCurrentSharePath);
        Intent intent = new Intent(RemoteMainActivity.this, MoveFileActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_MOVE_FILE);
        break;

      case 3:
        //删除
        System.out.println("groupPosition+\"  \"+childPosition = " + groupPosition + "  " + childPosition);
        mSambaPopWin = new SambaPopupWindow(RemoteMainActivity.this, 4, "");
        mSambaPopWin.setFocusable(true);
        mSambaPopWin.showAtLocation(mSambaPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);

        mSambaPopWin.setOnPositiveClickListener(new SambaPopupWindow.OnPositiveClickListener() {
          @Override
          public void onPositiveClick() {
            //正在上传 下载的文件不能被删除
            String urlstr = bean.getPath();
            MyProgressDialog mDialog = SambaUtils.showDialog(RemoteMainActivity.this, "正在删除中…");
            // 删除文件夹：空文件夹可直接删除，非空文件夹先遍历删除子项之后再进行文件夹删除
            if (bean.getFileType()) {
              //文件夹
              isDeleteFolder = true;
              deleteFolderPath = urlstr;

              mDeleteFolderList.clear();
              mDeleteFolderList.add(urlstr);

              deleteFolder(urlstr, mDialog);
            } else {
              //文件
              isDeleteFolder = false;
              new DeleteTask(urlstr, false, mDialog).execute();
            }

          }
        });
        break;

      default:
        break;
    }

    //关闭展开
    mBinding.lvRemoteFile.collapseGroup(groupPosition);

  }

  /**
   * 删除文件夹(递归删除)
   *
   * @param
   * @return
   */
  private void deleteFolder(String path, MyProgressDialog myDialog) {
    //   smb://192.168.1.1/    qtec/新建文件夹xh0312      qtec/新建文件夹xh0312/文件夹xx        share=qtec/新建文件夹xh0312;
    String sharePath = path.substring(getCharacterPosition(path) + 1);

    String connect_ip = "";

    if (GlobleConstant.isSambaExtranetAccess) {
      connect_ip = LoginConfig.OS_IP + ":" + LoginConfig.OS_PORT;
    } else {
      connect_ip = LoginConfig.LAN_IP;
    }

    DocumentMetadata metadata = DocumentMetadata.createShare(connect_ip, sharePath);
    System.out.println("删除文件夹 sharePath = " + sharePath);

    final LoadChildrenTask task =
        new LoadChildrenTask(metadata, mClient, mCache, new OnTaskFinishedCallback<DocumentMetadata>() {
          @Override
          public void onTaskFinished(int status, @Nullable DocumentMetadata metadata, @Nullable Exception exception) {
            try {
              if (exception != null) {
                if (exception.getClass().getName().equals("java.io.FileNotFoundException")) {
                  System.out.println("deleteFolder LoadChildrenTask 文件夹不存在");
                  Toast.makeText(RemoteMainActivity.this, "文件夹已被删除", Toast.LENGTH_SHORT).show();
                  myDialog.dismiss();
                  refreshEvent();
                  return;
                }
              }

              Map<Uri, DocumentMetadata> children = metadata.getChildren();

              if (children == null) {
                return;
              }

              Iterator it = children.keySet().iterator();

              while (it.hasNext()) {
                Uri key;
                key = (Uri) it.next();
                // 名称 日期 大小 路径 类型：文件夹或文件
                DocumentMetadata document = DocumentMetadata.fromUri(key, mClient);
                StructStat stat = mClient.stat(key.toString());
                if (S_ISDIR(stat.st_mode)) { //路径
                  // 隐藏lost+found文件夹
                  if (!"lost+found".equals(document.getDisplayName())) {
                    //文件夹
                    System.out.println("删除文件夹 遍历删除文件夹 = " + document.getUri());
                    mDeleteFolderList.add(document.getUri() + "");
                    deleteFolder(document.getUri() + "", myDialog);
                  }
                } else {
                  //文件
                  System.out.println("删除文件夹 删除文件 = " + document.getUri());
                  new DeleteTask(document.getUri() + "", false, myDialog).execute();
                }
              }

              //删除完成之后再删除该空文件夹(倒序)
              for (int i = mDeleteFolderList.size() - 1; i >= 0; i--) {
                new DeleteTask(mDeleteFolderList.get(i), true, myDialog).execute();
              }

            } catch (FileNotFoundException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }


        });

    mTaskManager.runTask(metadata.getUri(), task);
  }

  /**
   * 获取第N次出现的字符
   *
   * @param
   * @return
   */
  public static int getCharacterPosition(String string) {
    //这里是获取"/"符号的位置
    Matcher slashMatcher = Pattern.compile("/").matcher(string);
    int mIdx = 0;
    while (slashMatcher.find()) {
      mIdx++;
      //当"/"符号第三次出现的位置
      if (mIdx == 3) {
        break;
      }
    }
    return slashMatcher.start();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.img_new_folder:
        newFolderClick();
        break;

      case R.id.btn_loading:
        //先关闭定时器

        SambaUtils.mUserPause = false;
        System.out.println("SambaUtils.mUserPause = " + SambaUtils.mUserPause);

        continueTransmitFile(10);

        Intent intent1 = new Intent(this, TransmitMainActivity.class);
        intent1.putExtra("CurrentRemoteFilePath", remoteCurrentSharePath);
        System.out.println("remoteCurrentPath111 = " + remoteCurrentPath);
        startActivityForResult(intent1, REQUEST_TRANSMIT_FILE);
        break;

      case R.id.et_search:
        Intent intent = new Intent(this, FileSearchActivity.class);
        intent.putExtra("CurrentRemoteFilePath", remoteCurrentSharePath);
        startActivityForResult(intent, REQUEST_SEARCH_FILE);
        break;

      default:
        break;
    }
  }

  private boolean isSelect(Boolean isDeleteAll) {
    boolean isSelect = false;
    selectSmbFiles.clear();
    for (int i = 0; i < adapter.getGroupCount(); i++) {
      //遍历checkedMap：选中状态,如果选中
      if (adapter.checkedMap.get(i)) {
        try {
          FileBean bean = adapter.getGroup(i);

          if (bean.getFileType()) {
            //文件夹
            if (isDeleteAll) {
              selectSmbFiles.add(bean);
            }
          } else {
            selectSmbFiles.add(bean);
          }

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    if (selectSmbFiles.size() == 0) {
      isSelect = false;
    } else {
      isSelect = true;
    }

    return isSelect;
  }

  /**
   * 新建文件夹
   *
   * @param
   * @return
   */
  private void newFolderClick() {
    mSambaPopWin = new SambaPopupWindow(RemoteMainActivity.this, 1, "");
    mSambaPopWin.setFocusable(true);
    mSambaPopWin.showAtLocation(mSambaPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);

    mSambaPopWin.setOnPositiveClickListener(new SambaPopupWindow.OnPositiveClickListener() {
      @Override
      public void onPositiveClick() {
        String newName = getText(mSambaPopWin.getNewNameEdit());//文件夹

        Boolean isAdd = true;

        for (int i = 0; i < adapter.getGroupCount(); i++) {
          if (adapter.getGroup(i).getFileType()) {
            //文件夹
            if ((adapter.getGroup(i).getName()).equals(newName)) {
              Toast.makeText(RemoteMainActivity.this, "文件夹已经存在", Toast.LENGTH_SHORT).show();
              isAdd = false;
              break;
            }
          }
        }

        if (isAdd) {
          new newFolderTask(newName).execute();
        } else {
          //1s后重新弹出新建文件夹的窗口
          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              newFolderClick();
            }
          }, 900);
        }

      }
    });
  }

  /**
   * 获取账号 密码
   *
   * @param
   * @return
   */
  @Override
  public void getSambaAccount(GetSambaAccountResponse response) {
    System.out.println("Samba登录信息 response.getUsername() = " + response.getUsername());
    System.out.println("Samba登录信息 response.getPassword() = " + response.getPassword());

    //存到缓存
    PrefConstant.putSambaAccount(response.getUsername());
    PrefConstant.putSambaPwd(response.getPassword());

    LoginConfig.USER_NAME = response.getUsername();
    LoginConfig.USER_PWD = response.getPassword();

    new LoginTask().execute();

  }

  @Override
  public void getExtraNetPort(GetExtraNetPortResponse response) {

    LoginConfig.OS_PORT = response.getPort();
    LoginConfig.OS_IP = response.getIp();

    System.out.println("samba登录信息 外网访问 os_port = " + response.getPort() + " os_ip = " + response.getIp());

    AndroidApplication.getSambaClient(this).reset(LoginConfig.OS_PORT + "");

    querySambaLoginInfo();
  }

  @Override
  public void getExtraNetPortFailed() {
    Toast.makeText(this, "连接失败，请检查网络状态", Toast.LENGTH_SHORT).show();
    finish();
  }

  @Override
  public void queryDiskState(QueryDiskStateResponse response) {

    if(isRefresh){
      isRefresh = false;
      mBinding.tvDiskInfo.setText("磁盘已使用：" + response.getDisk_used() + "/" + response.getDisk_size());
      return;
    }

    if (response.getStatus_errorcode() == 0) {

      mBinding.tvDiskInfo.setVisibility(View.VISIBLE);
      mBinding.tvDiskInfo.setText("磁盘已使用：" + response.getDisk_used() + "/" + response.getDisk_size());

      if (mQueryDiskDialog != null) {
        mQueryDiskDialog.dismiss();
      }

      findViewById(R.id.rl_format_disk).setVisibility(View.GONE);
      mBinding.llSearch.setVisibility(View.VISIBLE);
      mBinding.ptrLayout.setVisibility(View.VISIBLE);

      //判断是否是外网访问
      if (GlobleConstant.isSambaExtranetAccess) {
        // 外网访问 请求端口号
        queryExtraNetPort();
      } else {
        AndroidApplication.getSambaClient(this).reset("0");//切换网络后初始化端口号
        querySambaLoginInfo();//刷列表
      }

    } else if (response.getStatus_errorcode() == 3000 || response.getStatus_errorcode() == 3001) {
      mBinding.tvDiskInfo.setVisibility(View.GONE);
      if (mQueryDiskStateTimes > 5) {
        mQueryDiskStateTimes = 0;
        Toast.makeText(this, "未监测到网关上外接的存储设备", Toast.LENGTH_SHORT).show();
        finish();
      } else {
        startTimesQueryDiskState();
      }
    } else if (response.getStatus_errorcode() == 3002) {
      if (mQueryDiskDialog != null) {
        mQueryDiskDialog.dismiss();
      }
      Toast.makeText(this, "未监测到网关上外接的存储设备", Toast.LENGTH_SHORT).show();
      finish();
    } else if (response.getStatus_errorcode() == 3003 || response.getStatus_errorcode() == 3004) {
      //格式化磁盘入口
      showFormatDiskUi();
    } else if (response.getStatus_errorcode() == 3005) {
      //格式化磁盘入口
      Toast.makeText(this, "磁盘格式化失败", Toast.LENGTH_SHORT).show();
      finish();
    }

  }

  @Override
  public void queryDiskStateFailed() {
    if (mQueryDiskDialog != null) {
      mQueryDiskDialog.dismiss();
    }
    Toast.makeText(this, "未检测到磁盘信息，请重试", Toast.LENGTH_SHORT).show();
    //延时2s
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        RemoteMainActivity.this.finish();
      }
    }, 2000);
  }

  private void showFormatDiskUi() {
    if (mQueryDiskDialog != null) {
      mQueryDiskDialog.dismiss();
    }

    findViewById(R.id.rl_format_disk).setVisibility(View.VISIBLE);
    mBinding.llSearch.setVisibility(View.GONE);
    mBinding.ptrLayout.setVisibility(View.GONE);

    findViewById(R.id.btn_format_disk).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //格式化磁盘 入口
        Intent intent = new Intent(RemoteMainActivity.this, FormatingDiskActivity.class);
        /*startActivityForResult(intent, REQUEST_FORMATING_CODE);*/
        startActivity(intent);
      }
    });
  }

  private void startTimesQueryDiskState() {
    mHandler = new Handler();

    mRunnbale = new Runnable() {
      @Override
      public void run() {
        mQueryDiskStateTimes++;
        queryDiskState();
      }
    };

    mHandler.postDelayed(mRunnbale, 1900);//2s查一次
  }

  /**
   * 新建文件夹
   *
   * @param
   * @return
   */
  class newFolderTask extends AsyncTask<String, String, Boolean> {
    String newName = "";

    public newFolderTask(String newName) {
      this.newName = newName;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Boolean doInBackground(String... params) {
      System.out.println("新建文件夹: remoteCurrentPath= " + remoteCurrentPath + " newName= " + newName);
      Boolean result = SambaUtils.createSambaFolder(mClient, remoteCurrentPath, newName);
      return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
      if (result) {
        Toast.makeText(RemoteMainActivity.this, "新建成功", Toast.LENGTH_SHORT).show();
        refreshEvent();
      } else {
        if (GlobleConstant.isSambaExtranetAccess) {
          //外网  资源被回收之后新建失败
          Toast.makeText(RemoteMainActivity.this, "网络发生异常，新建失败，请重试", Toast.LENGTH_SHORT).show();
          refreshEvent();
        } else {
          Toast.makeText(RemoteMainActivity.this, "新建失败，文件夹已存在", Toast.LENGTH_SHORT).show();
        }
      }
    }
  }

  /**
   * 上传操作
   *
   * @param
   * @return
   */
  private void uploadBtnClick() {
    //弹出选项opWindow
    mUploadPopWindow = new UploadPopWindow(this);
    mUploadPopWindow.showAtLocation(mUploadPopWindow.getOuterLayout(), Gravity.BOTTOM, 0, 0);
    mUploadPopWindow.SetOnPictureUploadClickListener(new UploadPopWindow.I_OnPictureUploadClickListener() {
      @Override
      public void pictureClickListener() {
        //上传图片操作
        // 1. 使用默认风格，并指定选择数量：
        // 第一个参数Activity/Fragment； 第二个request_code； 第三个允许选择照片的数量，不填可以无限选择。
        Album.startAlbum(RemoteMainActivity.this, ACTIVITY_REQUEST_SELECT_PHOTO, Integer.MAX_VALUE);
        // 3. 指定风格，并指定选择数量，如果不想限制数量传入Integer.MAX_VALUE;
      }
    });

    mUploadPopWindow.SetOnMusicUploadClickListener(new UploadPopWindow.I_OnMusicUploadClickListener() {
      @Override
      public void musicUploadClickListener() {
        //上传音乐操作
        System.out.println("当前路径 remoteCurrentSharePath = " + remoteCurrentSharePath);
        System.out.println("当前路径 remoteCurrentPath = " + remoteCurrentPath);

        Intent intent = new Intent(RemoteMainActivity.this, AudiosUploadActivity.class);
        intent.putExtra("CurrentRemoteFilePath", remoteCurrentPath);
        intent.putExtra("remoteCurrentSharePath", remoteCurrentSharePath);
        startActivityForResult(intent, REQUEST_UPLOAD_OTHER_FILE);
      }
    });

    mUploadPopWindow.SetOnVideoUploadClickListener(new UploadPopWindow.I_OnVideoUploadClickListener() {
      @Override
      public void videoUploadClickListener() {
        //上传视频操作
        System.out.println("当前路径 remoteCurrentSharePath = " + remoteCurrentSharePath);
        System.out.println("当前路径 remoteCurrentPath = " + remoteCurrentPath);

        Intent intent = new Intent(RemoteMainActivity.this, VideosUploadActivity.class);
        intent.putExtra("CurrentRemoteFilePath", remoteCurrentPath);
        intent.putExtra("remoteCurrentSharePath", remoteCurrentSharePath);
        startActivityForResult(intent, REQUEST_UPLOAD_OTHER_FILE);

      }
    });

    mUploadPopWindow.SetOnOtherUploadClickListener(new UploadPopWindow.I_OnOtherUploadClickListener() {
      @Override
      public void otherUploadClickListener() {
        //上传其他操作
        System.out.println("当前路径 remoteCurrentSharePath = " + remoteCurrentSharePath);
        System.out.println("当前路径 remoteCurrentPath = " + remoteCurrentPath);

        Intent intent = new Intent(RemoteMainActivity.this, OtherFileUploadActivity.class);
        intent.putExtra("CurrentRemoteFilePath", remoteCurrentPath);
        intent.putExtra("remoteCurrentSharePath", remoteCurrentSharePath);
        startActivityForResult(intent, REQUEST_UPLOAD_OTHER_FILE);
      }
    });
  }

  /**
   * 编辑弹窗
   *
   * @param
   * @return
   */
  private void editPopWinClick() {
    //弹出选项opWindow
    mEditMorePopWindow = new EditMorePopWindow(this);

    mEditMorePopWindow.showAtLocation(mEditMorePopWindow.getOuterLayout(), Gravity.BOTTOM, 0, 0);
    mEditMorePopWindow.setOnEditClickListener(new EditMorePopWindow.I_OnEditClickListener() {
      @Override
      public void eidtClickListener() {
        //编辑

        if (adapter.getGroupCount() == 0) {
          Toast.makeText(RemoteMainActivity.this, "该路径暂无文件，不支持编辑", Toast.LENGTH_SHORT).show();
          return;
        }

        mTitleBar.getRightRelayout().setVisibility(View.GONE);
        editBtnClickEvent();
      }
    });

    mEditMorePopWindow.setOnPictureRestoreClickListener(new EditMorePopWindow.I_OnPictureRestoreClickListener() {
      @Override
      public void pictureRestoreClickListener() {
        //图片备份
        /*new newFolderTask("来自："+DeviceUtils.getModel()).execute();*/

        mTitleBar.getRightRelayout().setVisibility(View.VISIBLE);
        Intent intent = new Intent(RemoteMainActivity.this, PictureRestoreActivity.class);
        intent.putExtra("remoteSharePath", remoteCurrentSharePath);
        startActivityForResult(intent, REQUEST_PICTURE_RESTORE);

      }
    });
  }

  /**
   * 编辑操作
   *
   * @param
   * @return
   */
  private void editBtnClickEvent() {

    mTitleBar.getUploadBtn().setVisibility(View.GONE);
    mTitleBar.getCenterTextView().setVisibility(View.GONE);
    initTitleBar("所有文件");

    if (isEdit) {
      updateEditedState();
    } else {
      isEdit = true;
      mTitleBar.getTitleView().setVisibility(View.VISIBLE);
      mTitleBar.getCenterTextView().setVisibility(View.GONE);
      mBinding.llBottom.setVisibility(View.VISIBLE);
      mTitleBar.getRightBtn().setVisibility(View.VISIBLE);
      mTitleBar.getRightBtn().setText("取消");

      mTitleBar.getRvRightBtn().setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (isEdit) {
            updateEditedState();
          } else {
            editPopWinClick();
          }
        }
      });

      mTitleBar.setLeftAs("全选", new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (isSelectAll) {
            isSelectAll = false;
            for (int i = 0; i < adapter.getGroupCount(); i++) {
              adapter.checkedMap.put(i, false);
            }

            adapter.notifyDataSetChanged();
          } else {
            isSelectAll = true;
            for (int i = 0; i < adapter.getGroupCount(); i++) {
              adapter.checkedMap.put(i, true);
            }

            adapter.notifyDataSetChanged();

          }
        }
      });

      mBinding.llDeleteAll.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          isClickBatch = true;
          System.out.println("点击了批量操作:" + isClickBatch);
          //删除全部
          if (isSelect(true)) {
            mSambaPopWin = new SambaPopupWindow(RemoteMainActivity.this, 4, "");
            mSambaPopWin.setFocusable(true);
            mSambaPopWin.showAtLocation(mSambaPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);

            mSambaPopWin.setOnPositiveClickListener(new SambaPopupWindow.OnPositiveClickListener() {
              @Override
              public void onPositiveClick() {

                MyProgressDialog mDialog = SambaUtils.showDialog(RemoteMainActivity.this, "正在删除中…");

                if (selectSmbFiles.get(0).getFileType()) {
                  //文件夹
                  isDeleteFolder = true;
                  deleteFolderPath = selectSmbFiles.get(0).getPath();

                  mDeleteFolderList.clear();
                  mDeleteFolderList.add(selectSmbFiles.get(0).getPath());

                  deleteFolder(selectSmbFiles.get(0).getPath(), mDialog);
                } else {
                  //文件
                  isDeleteFolder = false;
                  new DeleteTask(selectSmbFiles.get(0).getPath(), false, mDialog).execute();
                }
              }
            });

          } else {
            Toast.makeText(RemoteMainActivity.this, "请先选择文件", Toast.LENGTH_SHORT).show();
          }
        }
      });

      mBinding.llDownloadAll.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //全部下载
          System.out.println("下载器 remoteMainActivity downloaders.size() = " + downloaders.size());

          getSelectFiles();

          if (selectSmbFiles.size() == 0) {
            Toast.makeText(RemoteMainActivity.this, "请先选择文件再下载", Toast.LENGTH_SHORT).show();
            return;
          }

          Boolean isHasFolder = false;
          for (int i = 0; i < selectSmbFiles.size(); i++) {
            if (selectSmbFiles.get(i).getFileType()) {
              isHasFolder = true;
              break;
            }
          }

          if (isHasFolder) {
            Toast.makeText(RemoteMainActivity.this, "暂不支持文件夹下载,请先选择文件", Toast.LENGTH_SHORT).show();
            return;
          }

          Boolean isHasAlreadyDownloaded = false;//判断是否有已经下载过的文件
          for (int i = 0; i < selectSmbFiles.size(); i++) {
            //看下是否已经下载过
            File f = new File(UploadUtils.genLocalPath(selectSmbFiles.get(i).getName(), AppConstant.SAMBA_DOWNLOAD_PATH));
            if (f.exists() && f.length() > 0 && f.length() == selectSmbFiles.get(i).getLongSize()) {
              isHasAlreadyDownloaded = true;
              break;
            }
          }

          if (isHasAlreadyDownloaded) {

            mSambaPopWin = new SambaPopupWindow(RemoteMainActivity.this, 5, "");
            mSambaPopWin.setFocusable(true);
            mSambaPopWin.showAtLocation(mSambaPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);

            mSambaPopWin.setOnPositiveClickListener(new SambaPopupWindow.OnPositiveClickListener() {
              @Override
              public void onPositiveClick() {
                dealBatchDownload();
              }
            });

            mSambaPopWin.setOnNegativeClickListener(new SambaPopupWindow.OnNegativeClickListener() {
              @Override
              public void onNegtiveClick() {
                //取消
                updateEditedState(); //UI恢复
                return;
              }
            });
            return;
          }

          dealBatchDownload();

        }
      });

      mBinding.llMoveAll.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //全部移动
          isClickBatch = true;

          getSelectFiles();

          if (selectSmbFiles.size() == 0) {
            Toast.makeText(RemoteMainActivity.this, "请先选择文件再移动", Toast.LENGTH_SHORT).show();
            return;
          }

          Boolean isHasFolder = false;
          for (int i = 0; i < selectSmbFiles.size(); i++) {
            if (selectSmbFiles.get(i).getFileType()) {
              isHasFolder = true;
              break;
            }
          }

          if (isHasFolder) {
            Toast.makeText(RemoteMainActivity.this, "暂不支持文件夹移动,请先选择文件", Toast.LENGTH_SHORT).show();
            return;
          }

          String[] movePathList = new String[selectSmbFiles.size()];

          for (int i = 0; i < selectSmbFiles.size(); i++) {
            //判断是否还有文件夹
            movePathList[i] = selectSmbFiles.get(i).getPath();
          }

          //移动文件
          Bundle bundle = new Bundle();
          bundle.putStringArray("MOVE_FILE_PATH_LIST", movePathList);
          bundle.putString("CurrentRemoteFilePath", remoteCurrentSharePath);
          Intent intent = new Intent(RemoteMainActivity.this, MoveFileActivity.class);
          intent.putExtras(bundle);
          System.out.println("remoteCurrentSharePath11 = " + remoteCurrentSharePath);
          startActivityForResult(intent, REQUEST_MOVE_FILE);
        }
      });

      setCheckVisibility(true);

    }
  }

  /**
   * 处理批量下载
   *
   * @param
   * @return
   */
  private void dealBatchDownload() {
    for (int i = 0; i < selectSmbFiles.size(); i++) {
      //遍历checkedMap：选中状态,如果选中
      try {

        FileBean bean = selectSmbFiles.get(i);
        String path = bean.getPath();

        if (bean.getLongSize() == 0) continue;

        Downloader downloader = downloaders.get(path);
        if (downloader == null) {

          String desPath = UploadUtils.genLocalPath(bean.getName(), AppConstant.SAMBA_DOWNLOAD_PATH);

          downloader = new Downloader(path, desPath, 1);
          downloader.setState(Downloader.WAITING);//等待状态
          downloader.setCompeleteSize(0);
          downloader.setName(bean.getName());
          downloader.setFileSize(bean.getLongSize());
          downloader.setUrlstr(path);
          downloader.setLocalfile(desPath);
          downloaders.put(path, downloader);
          //排除大小为0的损坏的文件
          DownloadCacheManager.putDownloader(RemoteMainActivity.this, path, downloader);//写到文件
        } else {
          continue;
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    updateEditedState(); //UI恢复

    Toast.makeText(RemoteMainActivity.this, "成功添加下载任务", Toast.LENGTH_SHORT).show();

    //启动三个
    int count = 0;
    Iterator it = downloaders.keySet().iterator();//获取所有的健值
    while (it.hasNext()) {
      try {
        String key;
        key = (String) it.next();

        String sourcePath = key;
        String despath = downloaders.get(key).getLocalfile();

        System.out.println("sourcePath  = " + sourcePath + "  despath= " + despath);

        if (count < 3) {
          count++;

          SambaUtils.DownloadTask downloadTask = new SambaUtils.DownloadTask(downloaders, RemoteMainActivity.this, mClient, DownloadFragment.mHandlerDownload);
          downloadTask.execute(sourcePath, despath, "1");
        } else {
          break;
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }


  }

  /**
   * @param
   * @return
   */
  private void getSelectFiles() {
    selectSmbFiles.clear();
    for (int i = 0; i < adapter.getGroupCount(); i++) {
      //遍历checkedMap：选中状态,如果选中
      if (adapter.checkedMap.get(i)) {
        try {
          FileBean bean = adapter.getGroup(i);
          /*      if(bean.getFileType()) continue;*/
          selectSmbFiles.add(bean);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 更新编辑后的状态
   *
   * @param
   * @return
   */
  private void updateEditedState() {
    isEdit = false;
    isSelectAll = false;
    mBinding.llBottom.setVisibility(View.GONE);
    mTitleBar.getRightBtn().setVisibility(View.GONE);
    mTitleBar.getRightRelayout().setVisibility(View.VISIBLE);
    mTitleBar.getTitleView().setVisibility(View.GONE);
    mTitleBar.getCenterTextView().setVisibility(View.VISIBLE);
    mTitleBar.getUploadBtn().setVisibility(View.VISIBLE);

    if (mUploadPopWindow != null) {
      mUploadPopWindow.dismiss();
    }

    mTitleBar.setLeftAsBackButtonForSamba();

    //左上角返回按钮
    mTitleBar.setOnSambaBackListener(new TitleBar.OnSambaBackClickListener() {
      @Override
      public void onSambaBackClick() {
        System.out.println("左上角 回退 点击事件");
        if (isExit_Back) {
          RemoteMainActivity.this.finish();
        } else {
          if (remoteCurrentSharePath.contains("/")) {
            remoteCurrentSharePath = remoteCurrentSharePath.substring(0, remoteCurrentSharePath.lastIndexOf("/"));
          }
          System.out.println("左上角 回退 当前路径 remoteCurrentSharePath = " + remoteCurrentSharePath);
          refreshEvent();
        }
      }
    });

    setCheckVisibility(false);
  }

  public void setCheckVisibility(boolean isCheck) {
    if (adapter != null) {
      for (int i = 0; i < adapter.getGroupCount(); i++) {
        if (isCheck) {
          adapter.checkedMap.put(i, false);
          adapter.visibleMap.put(i, CheckBox.VISIBLE);
        } else {
          adapter.checkedMap.put(i, false);
          adapter.visibleMap.put(i, CheckBox.GONE);
        }
      }

      adapter.notifyDataSetChanged();
    }

  }

  @Override
  public void onGroupExpand(int groupPosition) {
    if (adapter.getGroupCount() == 0) {
      return;
    }

    for (int i = 0, count = mBinding.lvRemoteFile.getExpandableListAdapter().getGroupCount(); i < count; i++) {
      if (groupPosition != i) {
        // 关闭其他分组
        mBinding.lvRemoteFile.collapseGroup(i);
      }
    }

  }

  private void dealWithRefresh() {

    //下拉刷新支持时间
    mBinding.ptrLayout.setLastUpdateTimeRelateObject(this);
    mBinding.ptrLayout.setDurationToClose(1000);
    mBinding.ptrLayout.disableWhenHorizontalMove(true);//解决横向滑动冲突
    //进入Activity自动下拉刷新
    mBinding.ptrLayout.postDelayed(new Runnable() {
      @Override
      public void run() {
        mBinding.ptrLayout.autoRefresh();
      }
    }, 300);

    mBinding.ptrLayout.setPtrHandler(new PtrDefaultHandler2() {
      // 上拉加载的回调方法
      @Override
      public void onLoadMoreBegin(PtrFrameLayout frame) {
        mBinding.ptrLayout.postDelayed(new Runnable() {
          @Override
          public void run() {
            refreshEvent();
            System.out.println("刷新 上拉加载……");
          }
        }, 1000);
      }

      // 下拉刷新的回调方法
      @Override
      public void onRefreshBegin(PtrFrameLayout frame) {
        mBinding.ptrLayout.postDelayed(new Runnable() {
          @Override
          public void run() {

            isRefresh = true;

            mQueryDiskStatePresenter.queryDiskState();

            if (isFirstRefresh) {
              isFirstRefresh = false;

              initSmbFileList(mRemoteCurrentMedeta);
              mBinding.ptrLayout.refreshComplete();
              updateEditedState();//编辑状态下刷新 恢复UI
              System.out.println("刷新 首次 下拉加载……");

            } else {
              System.out.println("刷新 非首次 下拉加载……");
              refreshEvent();
            }

          }
        }, 100);
      }

      @Override
      public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
        mBinding.tvEmptyFile.setVisibility(View.GONE);
        return super.checkCanDoLoadMore(frame, mBinding.lvRemoteFile, footer);
      }

      @Override
      public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        mBinding.tvEmptyFile.setVisibility(View.GONE);
        return super.checkCanDoRefresh(frame, mBinding.lvRemoteFile, header);
      }
    });
  }

  /**
   * 刷新（上拉、下拉）逻辑处理
   *
   * @param
   * @return
   */
  private void refreshEvent() {
    String connect_ip = "";
    if (GlobleConstant.isSambaExtranetAccess) {
      connect_ip = LoginConfig.OS_IP + ":" + LoginConfig.OS_PORT;
    } else {
      connect_ip = LoginConfig.LAN_IP;
    }
    System.out.println("当前路径 fetchAtPath connect_ip = " + connect_ip);
    DocumentMetadata metadata = DocumentMetadata.createShare(connect_ip, remoteCurrentSharePath);

    DialogUtil.showProgress(getContext());
    mBinding.tvEmptyFile.setVisibility(View.GONE);

    adapter = new RemoteMainAdapter(RemoteMainActivity.this);
    mBinding.lvRemoteFile.setAdapter(adapter);

    SambaUtils.mUserPause = true;
    System.out.println("SambaUtils.mUserPause = " + SambaUtils.mUserPause);

    removeTransmitHandler();

    long time1 = System.currentTimeMillis();

    final LoadChildrenTask task =
        new LoadChildrenTask(metadata, mClient, mCache, new OnTaskFinishedCallback<DocumentMetadata>() {

          @Override
          public void onTaskFinished(int status, @Nullable DocumentMetadata metadata, @Nullable Exception exception) {
            long time2 = System.currentTimeMillis();
            System.out.println("(time2-time1) LoadChildrenTask = " + (time2 - time1) + " mUserPause = " + SambaUtils.mUserPause);
            mRemoteCurrentMedeta = metadata;
            initSmbFileList(mRemoteCurrentMedeta);
            mBinding.ptrLayout.refreshComplete();
            updateEditedState();//编辑状态下刷新 恢复UI
          }


        });

    mTaskManager.runTask(metadata.getUri(), task);


    //Task改成Thread
/*    Thread myThread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          metadata.loadChildren(mClient);
          metadata.getChildren();
        } catch (IOException e) {
          e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            long time2 = System.currentTimeMillis();
            System.out.println("(time2-time1) loadThread = " + (time2 - time1) + " mUserPause = " + SambaUtils.mUserPause);
            mRemoteCurrentMedeta = metadata;
            initSmbFileList(mRemoteCurrentMedeta);
            mBinding.ptrLayout.refreshComplete();
            updateEditedState();//编辑状态下刷新 恢复UI
          }
        });
      }
    });

    myThread.setPriority(1);
    myThread.start();*/

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    updateEditedState();

    if (requestCode == REQUEST_CODE_UPLOAD_FILE) {

    } else if (requestCode == ACTIVITY_REQUEST_SELECT_PHOTO) {
      //上传图片 多选
      if (data == null) {
        System.out.println("图片选择: data = null");
        return;
      } else {

        if (SambaUtils.mIOException) {
          SambaUtils.mIOException = false;
          Toast.makeText(this, "上传发生异常，请检查文件是否正在被操作或磁盘空间已满", Toast.LENGTH_SHORT).show();
        }

        List<String> pathList = Album.parseResult(data);
        for (int i = 0; i < pathList.size(); i++) {

          String path = pathList.get(i);

          File file = new File(path);

          if (file.length() == 0) continue;

          Downloader downloader = uploaders.get(path);
          if (downloader == null) {
            String desPath = SambaUtils.wrapSmbFileUrl(remoteCurrentPath, new File(path).getName());
            String desPath1 = desPath + ".upload";//为了首页不显示还未传输完的文件，加一个标志
            downloader = new Downloader(path, desPath1, 1);
            downloader.setState(Downloader.WAITING);//等待状态
            downloader.setCompeleteSize(0);
            downloader.setFileSize(file.length());
            downloader.setUrlstr(path);
            downloader.setLocalfile(desPath1);
            uploaders.put(path, downloader);
            //排除大小为0的损坏的文件
            UploadCacheManager.putUploader(this, path, downloader);//写到文件
          }
        }

        //开启三个上传Task
        int count = 0;
        Iterator it = uploaders.keySet().iterator();//获取所有的健值
        while (it.hasNext()) {
          try {
            String key;
            key = (String) it.next();

            String sourcePath = key;
            String despath = uploaders.get(key).getLocalfile();

            System.out.println("sourcePath  = " + sourcePath + "  despath= " + despath);

            if (count < 3) {
              count++;

              SambaUtils.UploadTask uploadTask = new SambaUtils.UploadTask(uploaders, this, mClient, UploadFragment.mHandlerUpload);
              uploadTask.execute(sourcePath, despath, "1");
            } else {
              break;
            }

          } catch (Exception e) {
            e.printStackTrace();
          }
        }

      }

      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          //上传3S后刷新一次
          dealWithRefresh();
        }
      }, 200);

    } else if ((requestCode == REQUEST_UPLOAD_OTHER_FILE) /*|| (requestCode == REQUEST_TRANSMIT_FILE)*/) {
      if (data != null) {
        remoteCurrentSharePath = data.getStringExtra("SHAREPATH");
        System.out.println("REQUEST_UPLOAD_OTHER_FILE REQUEST_TRANSMIT_FILE  当前路径 remoteCurrentSharePath = " + remoteCurrentSharePath);

        System.out.println("当前路径 remoteCurrentSharePath = " + remoteCurrentSharePath);
        System.out.println("当前路径 activity返回 mRemoteCurrentMedeta = " + mRemoteCurrentMedeta);
        System.out.println("当前路径 ===================================================");

      }
    } else if ((requestCode == REQUEST_TRANSMIT_FILE)) {
      if (data != null) {
        remoteCurrentSharePath = data.getStringExtra("SHAREPATH");
        /*initSmbFileListInfo(remoteCurrentPath);*/
        System.out.println("REQUEST_UPLOAD_OTHER_FILE REQUEST_TRANSMIT_FILE  当前路径 remoteCurrentSharePath = " + remoteCurrentSharePath);

        System.out.println("当前路径 remoteCurrentSharePath = " + remoteCurrentSharePath);
        System.out.println("当前路径 activity返回 mRemoteCurrentMedeta = " + mRemoteCurrentMedeta);
        System.out.println("当前路径 ===================================================");

        refreshEvent();
      }
    } else if ((requestCode == REQUEST_MOVE_FILE) || (requestCode == REQUEST_SEARCH_FILE)) {

      if (isClickBatch) {
        //批量移动的时候恢复UI
        isClickBatch = false;
        updateEditedState();
      }
      //初始化
      if (data != null) {
        remoteCurrentSharePath = data.getStringExtra("SHAREPATH");
        System.out.println("remoteCurrentSharePath11  = " + remoteCurrentSharePath);
        refreshEvent();
      }

    } else if (requestCode == REQUEST_PICTURE_RESTORE) {
      remoteCurrentSharePath = data.getStringExtra("SHAREPATH");

      System.out.println("REQUEST_UPLOAD_OTHER_FILE REQUEST_TRANSMIT_FILE  当前路径 remoteCurrentSharePath = " + remoteCurrentSharePath);

      refreshEvent();
    } /*else if (requestCode == REQUEST_FORMATING_CODE) {

      if (mQueryDiskDialog != null) {
        mQueryDiskDialog.dismiss();
      }

      //0代表格式化失败  1 成功
      String diskData = data.getStringExtra("DiskData");
      if ("0".equals(diskData)) {
        //失败
        showFormatDiskUi();
      } else {
        findViewById(R.id.rl_format_disk).setVisibility(View.GONE);
        mBinding.llSearch.setVisibility(View.VISIBLE);
        mBinding.ptrLayout.setVisibility(View.VISIBLE);

        //判断是否是外网访问
        if (GlobleConstant.isSambaExtranetAccess) {
          // 外网访问 请求端口号
          queryExtraNetPort();
        } else {
          querySambaLoginInfo();//刷列表
        }

      }
    }*/

  }

  public class DeleteTask extends AsyncTask<String, Boolean, Boolean> {
    String path = "";
    Boolean fileType = false;
    MyProgressDialog mDialog;

    public DeleteTask(String path, Boolean fileType, MyProgressDialog mDialog) {
      this.path = path;
      this.mDialog = mDialog;
      this.fileType = fileType;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(String... params) {

      Boolean result = SambaUtils.deleteSmbFiles(mClient, path, fileType);
      return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {

      if (isClickBatch) {
        //批量操作
        if (result) {
          System.out.println("\"文件删除：\" = " + "成功");
        } else {
          Toast.makeText(RemoteMainActivity.this, SambaUtils.getSambaErrorMsg(), Toast.LENGTH_SHORT).show();
          System.out.println("\"文件删除:\" = " + "失败");
        }

        selectSmbFiles.remove(0);
        if (selectSmbFiles.size() > 0) {

          if (selectSmbFiles.get(0).getFileType()) {
            //文件夹
            isDeleteFolder = true;
            deleteFolderPath = selectSmbFiles.get(0).getPath();

            mDeleteFolderList.clear();
            mDeleteFolderList.add(selectSmbFiles.get(0).getPath());

            deleteFolder(selectSmbFiles.get(0).getPath(), mDialog);
          } else {
            //文件
            new DeleteTask(selectSmbFiles.get(0).getPath(), false, mDialog).execute();
          }

        } else {
          isClickBatch = false;
          updateEditedState();
          SambaUtils.hideDialog(mDialog);
          refreshEvent();
        }

      } else {

        if (isDeleteFolder) {
          //删除文件夹
          //当删除根文件夹时才开始提示，其他情况都不提示
          if (deleteFolderPath.equals(path)) {

            if (result) {

              isDeleteFolder = false;

              SambaUtils.hideDialog(mDialog);

              Toast.makeText(RemoteMainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
              refreshEvent();
            } else {
              Toast.makeText(RemoteMainActivity.this, SambaUtils.getSambaErrorMsg(), Toast.LENGTH_SHORT).show();
            }
          }
        } else {
          SambaUtils.hideDialog(mDialog);

          if (result) {
            Toast.makeText(RemoteMainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            refreshEvent();
          } else {
            Toast.makeText(RemoteMainActivity.this, SambaUtils.getSambaErrorMsg(), Toast.LENGTH_SHORT).show();
            refreshEvent();
          }
        }
      }
    }
  }

  /**
   * 重命名
   *
   * @param
   * @return
   */
  class RenameTask extends AsyncTask<String, Boolean, Boolean> {
    private MyProgressDialog mDialog;
    private int groupPosition = 0;
    private String newName = "";
    private Boolean isFolder = false;


    public RenameTask(int groupPosition, String newName, Boolean isFolder) {
      this.groupPosition = groupPosition;
      this.newName = newName;
      this.isFolder = isFolder;
    }

    @Override
    protected void onPreExecute() {
      mDialog = new MyProgressDialog(RemoteMainActivity.this);
      mDialog.setMessage("正在重命名…");
      mDialog.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
      Boolean isRename = false;
      try {
        isRename = SambaUtils.renameSmbFiles(mClient, adapter.getGroup(groupPosition).getPath(), newName, isFolder);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return isRename;
    }

    @Override
    protected void onPostExecute(Boolean result) {
      mDialog.dismiss();

      if (result) {
        //重新设置文件的信息 时间
        Toast.makeText(RemoteMainActivity.this, "重命名成功", Toast.LENGTH_SHORT).show();
        refreshEvent();
      } else {
        Toast.makeText(RemoteMainActivity.this, "重命名失败", Toast.LENGTH_SHORT).show();
      }
    }
  }

  /**
   * 登录验证 等待解析出IP地址才能去mount 同步操作
   *
   * @param
   * @return
   */
  class LoginTask extends AsyncTask<String, Integer, Integer> {
    //参数说明：第一个参数是doInabckGround的参数 第二个参数是onProgressUpdate的参数  第三个是onPostExecute的参数,即doInabckGround的返回参数

    public LoginTask() {
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Integer doInBackground(String... params) {
      try {
        System.out.println("Samba登录信息  解析IP地址= " + InputUtil.convertDomainToIp(RouterRestApi.URL_FOR_SAMBA_IP));
        if (!TextUtils.isEmpty(InputUtil.convertDomainToIp(RouterRestApi.URL_FOR_SAMBA_IP))) {
          LoginConfig.LAN_IP = InputUtil.convertDomainToIp(RouterRestApi.URL_FOR_SAMBA_IP);
          System.out.println("解析所得ip地址 = " + LoginConfig.LAN_IP);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
      if (("".equals(LoginConfig.USER_NAME)) || ("".equals(LoginConfig.USER_PWD))) {
        Toast.makeText(RemoteMainActivity.this, "账号或者密码错误，请重新进入", Toast.LENGTH_SHORT).show();
        finish();
      } else {
        initTitleBar("");
        initTopBarClick();
        initView();
        initData();
      }
    }
  }

  /**
   * 遍历文件 适配7.0
   *
   * @param
   * @return
   */
  class initSmbFileTask_1 extends AsyncTask<String, Integer, Integer> {
    //参数说明：第一个参数是doInabckGround的参数 第二个参数是onProgressUpdate的参数  第三个是onPostExecute的参数,即doInabckGround的返回参数
    DocumentMetadata metadata = null;

    public initSmbFileTask_1(DocumentMetadata metadata) {
      this.metadata = metadata;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Integer doInBackground(String... params) {

      try {
        Map<Uri, DocumentMetadata> children = metadata.getChildren();
        Iterator<Uri> it = children.keySet().iterator();

        while (it.hasNext()) {
          Uri key = it.next();
          // 名称 日期 大小 路径 类型：文件夹或文件
          DocumentMetadata document = children.get(key);
          String name = document.getDisplayName();

          if (name.indexOf(".upload") != -1) continue;//过滤文件

          FileBean bean = new FileBean();
          bean.setPath(document.getUri().toString());
          bean.setName(name);

          StructStat stat = mClient.stat(bean.getPath());
          bean.setLongDate(stat.st_ctime * 1000);
          bean.setDate(TimeUtils.millis2String(stat.st_ctime * 1000));
          bean.setLastModifyTime(TimeUtils.millis2String(stat.st_ctime * 1000));
          bean.setLongSize(stat.st_size);

          if (S_ISDIR(stat.st_mode)) { //路径
            // 隐藏lost+found文件夹
            if ("lost+found".equals(name)) {
              //过滤
              continue;
            } else {
              bean.setFileType(true);
            }
          } else {
            //过滤以.开头的无效文件
            if (!(name).startsWith(".")) {
              if (stat.st_size <= 0) {
                //已损害的文件大小为0
                bean.setSize("0K");
              } else {
                bean.setSize(SambaUtils.bytes2kb(stat.st_size));
              }
              bean.setFileType(false);
            } else {
              continue;
            }
          }

          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              DialogUtil.hideProgress();
              adapter.add(bean, mSortFlag);
              System.out.println("mSortFlag = " + mSortFlag);
            }
          });
        }

      } catch (Exception e) {
        DialogUtil.hideProgress();
        SambaUtils.mUserPause = false;
       /* SambaUtils.mUserPauseUploadUrl = null;
        SambaUtils.mUserPauseDownloadUrl = null;*/
        System.out.println("initSmbFile 初始化 异常");
        isInitFileFailed = true;
        e.printStackTrace();
      }
      return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {

      if (adapter.getGroupCount() == 0) {
        //empty view
        mBinding.tvEmptyFile.setVisibility(View.VISIBLE);
      } else {
        mBinding.tvEmptyFile.setVisibility(View.GONE);
      }

      initEvent();

      SambaUtils.mUserPause = false;
   /*   SambaUtils.mUserPauseUploadUrl = null;
      SambaUtils.mUserPauseDownloadUrl = null;*/

      continueTransmitFile(6000);

      System.out.println("SambaUtils.mUserPause RemoteMainActivity= " + SambaUtils.mUserPause);

      DialogUtil.hideProgress();

      if (isInitFileFailed) {
        isInitFileFailed = false;

        //外网访问端口号被回收，进行重连即可
        if (GlobleConstant.isSambaExtranetAccess) {
          queryExtraNetPort();
          System.out.println("外网 端口号被回收 重新获取端口号");
        } else {
          Toast.makeText(RemoteMainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
          RemoteMainActivity.this.finish();
        }
      }
    }
  }

  private void removeTransmitHandler(){
    if(mTransHandler != null && mTransRunnable!= null){
      mTransHandler.removeCallbacks(mTransRunnable);
    }
  }

  private void continueTransmitFile(int millionTime) {

    if(mTransRunnable == null){
      mTransRunnable = new Runnable() {
        @Override
        public void run() {
          //上传文件
          if (uploaders.size() > 0) {
            //启动下一个
            System.out.println("上传TransmitMainActivity isFolder ");
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

                if (uploaders.get(key).getState() == Downloader.WAITING) {

                  if (SambaUtils.mUploadingIndex >= 3) {
                    System.out.println("SambaUtils.mUploadingIndex 超过3个 break循环 = " + SambaUtils.mUploadingIndex);
                    break;
                  } else {
                    //启动下一个等待状态的任务
                    SambaUtils.UploadTask uploadTask = new SambaUtils.UploadTask(uploaders, AndroidApplication.getApplication(), mClient, UploadFragment.mHandlerUpload);
                    uploadTask.execute(sourcePath, despath, "1");

                    System.out.println("SambaUtils.mUploadingIndex = " + SambaUtils.mUploadingIndex + " 上传文件 onhandler 主页=== 新启动的 key(urlstr)=" + key + " state= " + uploaders.get(key).getState());

                  }
                }
              } catch (Exception e) {
                System.out.println("上传文件 mhandler 迭代异常");
                e.printStackTrace();
              }
            }
          } else {
            SambaUtils.mUploadingIndex = 0;
          }

          //下载文件
          if (downloaders.size() > 0) {
            //启动下一个
            Iterator it = downloaders.keySet().iterator();//获取所有的健值
            while (it.hasNext()) {
              try {
                String key;
                key = (String) it.next();

                String sourcePath = key;
                String despath = downloaders.get(key).getLocalfile();

                //暂停状态的任务跳过
                if (downloaders.get(key).getState() == Downloader.PAUSE) continue;

                if (downloaders.get(key).getState() == Downloader.WAITING) {

                  if (SambaUtils.mDownloadingIndex >= 3) {
                    System.out.println("SambaUtils.mDownloadingIndex 超过3个 break循环 = " + SambaUtils.mDownloadingIndex);
                    break;
                  } else {
                    //启动下一个等待状态的任务
                    SambaUtils.DownloadTask downloadTask = new SambaUtils.DownloadTask(downloaders, AndroidApplication.getApplication(), mClient, DownloadFragment.mHandlerDownload);
                    downloadTask.execute(sourcePath, despath, "1");

                    System.out.println("SambaUtils.mUploadingIndex = " + SambaUtils.mUploadingIndex + "主页 上传文件 onhandler 新启动的 key(urlstr)=" + key + " state= " + uploaders.get(key).getState());
                  }
                }

              } catch (Exception e) {
                e.printStackTrace();
              }
            }

          } else {
            SambaUtils.mDownloadingIndex = 0;
          }

          if(uploaders.size() == 0 && downloaders.size() == 0){
            removeTransmitHandler();
          }

        }
      };
    }

    if(mTransHandler == null){
      mTransHandler = new Handler();
    }

    mTransHandler.removeCallbacks(mTransRunnable);
    mTransHandler.postDelayed(mTransRunnable, millionTime);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      //如果是根目录的话则退出
      if (isExit_Back) {
        //最好提示是否退出的提示框
        continueTransmitFile(2000);
        finish();
      } else {
        // File.separator:  windows是\,unix是/
        updateEditedState();//编辑状态下按back键

        if (!TextUtils.isEmpty(remoteCurrentSharePath) && remoteCurrentSharePath.contains("/")) {
          remoteCurrentSharePath = remoteCurrentSharePath.substring(0, remoteCurrentSharePath.lastIndexOf("/"));
        }

        System.out.println("onKeyDown  当前路径 remoteCurrentSharePath = " + remoteCurrentSharePath);

        refreshEvent();
      }
      return true;
    } else {
      return super.onKeyDown(keyCode, event);
    }
  }

  @Override
  protected void onDestroy() {
    removeHandlerTimer();
    super.onDestroy();
  }

  @Override
  protected void onPause() {
    removeHandlerTimer();
    super.onPause();
  }

  /**
   * 移除定时器
   *
   * @param
   * @return
   */
  private void removeHandlerTimer() {
    if (mHandler != null) {
      if (mRunnbale != null) {
        mHandler.removeCallbacks(mRunnbale);
      }
    }
  }
}
