package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.system.StructStat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.blankj.utilcode.util.TimeUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityMoveFileBinding;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.TitleBar;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.SambaUtils;
import com.google.android.sambadocumentsprovider.TaskManager;
import com.google.android.sambadocumentsprovider.base.OnTaskFinishedCallback;
import com.google.android.sambadocumentsprovider.cache.DocumentCache;
import com.google.android.sambadocumentsprovider.document.DocumentMetadata;
import com.google.android.sambadocumentsprovider.document.LoadChildrenTask;
import com.google.android.sambadocumentsprovider.nativefacade.SmbClient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.system.OsConstants.S_ISDIR;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/08/18
 *      desc: 移动samba文件 先复制 粘贴成功再返回删除源文件
 *      version: 1.0
 * </pre>
 */

public class MoveFileActivity extends BaseActivity implements View.OnClickListener{
  private ActivityMoveFileBinding mBinding;
  private String moveRootIp;//根目录
  private static boolean isExit_Back = true; // true=退出,false=后退
  private List<FileBean> mRemoteFileBeans = null;
  private String remoteCurrentPath = "";
  private String remoteCurrentSharePath;
  private MoveFileAdapter1 adapter;
  private SambaPopupWindow mSambaPopWin;
  protected final static int REQUEST_MOVE_FILE = 1236;
  private List<Uri> mUriList = null;
  private List<String> mMovePaths;
  private SmbClient mClient;
  private DocumentCache mCache;
  private TaskManager mTaskManager;
  private MyProgressDialog mDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_move_file);
    initMoveFileTitleBar("我的云盘");
    initView();
    initData();

    mTitleBar.setRightAs("取消", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  private void initData() {
    mBinding.btnMoveFile.setText("移动("+getMoveFilePathList().length+")");

    fetchAtPath();
  }

  private void initView() {
    mCache = AndroidApplication.getDocumentCache(this);
    mTaskManager = AndroidApplication.getTaskManager(this);
    mClient = AndroidApplication.getSambaClient(this);

    mRemoteFileBeans = new ArrayList<>();
    mUriList = new ArrayList<>();
    mMovePaths = new ArrayList<>();
    mBinding.btnMoveFile.setOnClickListener(this);
    mBinding.btnNewFolder.setOnClickListener(this);

    mTitleBar.setOnSambaBackListener(new TitleBar.OnSambaBackClickListener() {
      @Override
      public void onSambaBackClick() {
        if(isExit_Back){
          MoveFileActivity.this.finish();
        }else{
          remoteCurrentSharePath = remoteCurrentSharePath.substring(0,remoteCurrentSharePath.lastIndexOf("/"));
          fetchAtPath();
        }
      }
    });

    if (GlobleConstant.isSambaExtranetAccess) {
      //外网
      moveRootIp = LoginConfig.OS_REMOTE_ROOT_IP;
    } else {
      moveRootIp = LoginConfig.LAN_REMOTE_ROOT_IP;
    }

    remoteCurrentSharePath = LoginConfig.LAN_REMOTE_SHARE_IP;

  }

  /**
   * 获取文件列表
   *
   * @param
   * @return
   */
  private void fetchAtPath() {

    String connect_ip = "";

    if(GlobleConstant.isSambaExtranetAccess){
      connect_ip = LoginConfig.OS_IP+":"+LoginConfig.OS_PORT;
    }else {
      connect_ip = LoginConfig.LAN_IP;
    }

    DocumentMetadata metadata = DocumentMetadata.createShare(connect_ip, remoteCurrentSharePath);

    final LoadChildrenTask task =
        new LoadChildrenTask(metadata, mClient, mCache, new OnTaskFinishedCallback<DocumentMetadata>() {
          @Override
          public void onTaskFinished(int status, @Nullable DocumentMetadata metadata, @Nullable Exception exception) {
            System.out.println("LoadChildrenTask = " + " 回调回来了");
            initSmbFileList(metadata);
          }
        });

    mTaskManager.runTask(metadata.getUri(), task);
  }

  private void initSmbFileList(DocumentMetadata metadata) {
    // remoteRootIp: smb://192.168.1.1/qtec/
    remoteCurrentPath = metadata.getUri().toString() + "/";

    System.out.println("当前路径：remoteCurrentPath = " + remoteCurrentPath);
    System.out.println("当前路径：remoteCurrentSharePath = " + remoteCurrentSharePath);

    if (remoteCurrentPath.equals(moveRootIp))
      isExit_Back = true;
    else
      isExit_Back = false;

    System.out.println("当前路径：isExit_Back = " + isExit_Back);

    new initSmbFileTask_1(metadata).execute();
  }

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
        mRemoteFileBeans.clear();
        mUriList.clear();

        Map<Uri, DocumentMetadata> children = metadata.getChildren();
        Iterator it = children.keySet().iterator();

        while (it.hasNext()) {
          Uri key;
          key = (Uri) it.next();
          // 名称 日期 大小 路径 类型：文件夹或文件
          mUriList.add(key);
        }

        FileSort(mClient,mUriList, mRemoteFileBeans);//选出所有文件夹

        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            //判断根路径下是否有文件，无文件时显示空视图
            adapter = new MoveFileAdapter1(MoveFileActivity.this, mRemoteFileBeans,R.layout.item_move_file);
            mBinding.lvMoveFile.setAdapter(adapter);

            mBinding.lvMoveFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 如果是文件夹，则直接进入该文件夹，查看文件目录

                remoteCurrentSharePath = remoteCurrentSharePath + "/" + mRemoteFileBeans.get(position).getName();

                fetchAtPath();

              }
            });

          }
        });

      } catch (Exception e) {
        System.out.println("initSmbFile 初始化 异常");
        e.printStackTrace();
      }

      return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
    }
  }

  /**
   * 新建文件夹
   *
   * @param
   * @return
   */
  private void newFolderClick() {

    mSambaPopWin = new SambaPopupWindow(MoveFileActivity.this, 1,"");
    mSambaPopWin.setFocusable(true);
    mSambaPopWin.showAtLocation(mSambaPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);

    mSambaPopWin.setOnPositiveClickListener(new SambaPopupWindow.OnPositiveClickListener() {
      @Override
      public void onPositiveClick() {
        String newName = getText(mSambaPopWin.getNewNameEdit());

        Boolean isAdd = true;

        for (int i = 0; i < mRemoteFileBeans.size(); i++) {
          if(mRemoteFileBeans.get(i).getFileType()){
            //文件夹
            if ((mRemoteFileBeans.get(i).getName()).equals(newName)) {
              Toast.makeText(MoveFileActivity.this, "文件夹已经存在", Toast.LENGTH_SHORT).show();
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
      Boolean result = SambaUtils.createSambaFolder(mClient, remoteCurrentPath, newName);
      return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
      if (result) {
        Toast.makeText(MoveFileActivity.this, "新建成功", Toast.LENGTH_SHORT).show();
        fetchAtPath();
      } else {
        if(GlobleConstant.isSambaExtranetAccess){
          //外网  资源被回收之后新建失败
          Toast.makeText(MoveFileActivity.this, "网络发生异常，新建失败，请重试", Toast.LENGTH_SHORT).show();
          fetchAtPath();
        }else {
          Toast.makeText(MoveFileActivity.this, "新建失败，文件夹已存在", Toast.LENGTH_SHORT).show();
        }
      }
    }
  }

  /**
   * 移动文件或文件夹
   *
   * @param
   * @return
   */
  class moveFileTask extends AsyncTask<String, Boolean, Boolean> {
    private String movePath = "";

    public moveFileTask(String path) {
      movePath = path;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Boolean doInBackground(String... params) {
      Boolean  result = SambaUtils.removeSmbFiles(mClient,movePath,remoteCurrentPath);
      return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
      //因为可以移动成功 但一直报错 所以忽略flag标志位
      System.out.println("移动文件11 result = "+result);

      mMovePaths.remove(0);

      if(mMovePaths.size() == 0){

        if(mDialog != null){
          mDialog.dismiss();
        }

        if(result){
          Toast.makeText(MoveFileActivity.this, "移动成功", Toast.LENGTH_SHORT).show();
        }else {
          Toast.makeText(MoveFileActivity.this, "移动失败,"+SambaUtils.getSambaErrorMsg(), Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent();
        intent.putExtra("SHAREPATH", getRemoteCurrentPath());
        MoveFileActivity.this.setResult(REQUEST_MOVE_FILE,intent);
        finish();
      }else {
        new moveFileTask(mMovePaths.get(0)).execute();
      }
    }
  }

  /**
   * 文件移动路径
   *
   * @param
   * @return
   */
  private String[] getMoveFilePathList(){
    return  getIntent().getExtras().getStringArray("MOVE_FILE_PATH_LIST");
  }

  private String getRemoteCurrentPath(){
    return  getIntent().getExtras().getString("CurrentRemoteFilePath");
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      //如果是根目录的话则退出
      if (isExit_Back) {
        //最好提示是否退出的提示框
        finish();
      } else {
        if(remoteCurrentSharePath.contains("/")){
          remoteCurrentSharePath = remoteCurrentSharePath.substring(0,remoteCurrentSharePath.lastIndexOf("/"));
        }

        fetchAtPath();
      }
      return true;
    } else {
      return super.onKeyDown(keyCode, event);
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.btn_newFolder:
        newFolderClick();
        break;

      case R.id.btn_moveFile:
        System.out.println("当前目录 moveRootIp = " + moveRootIp);
        System.out.println("当前目录 remoteCurrentPath = " + remoteCurrentPath);

        mDialog = new MyProgressDialog(MoveFileActivity.this);
        mDialog.setMessage("正在移动中…");
        mDialog.show();

        mMovePaths.clear();
        for (int i = 0; i < getMoveFilePathList().length; i++) {
          mMovePaths.add(getMoveFilePathList()[i]);
        }

        new moveFileTask(mMovePaths.get(0)).execute();

        break;

      default:
        break;
    }
  }

  /**
   * 筛选出文件夹
   *
   * @param
   * @return
   */
  public static void FileSort(SmbClient smbClient, List<Uri> mUriList, List<FileBean> fileBeans) throws Exception {
    List<Uri> directoryFile = new ArrayList<>();
    List<Uri> otherFile = new ArrayList<>();
    int directoryFileCount = 0;

    try{
      for (int i = 0; i < mUriList.size(); i++) {
        DocumentMetadata document = DocumentMetadata.fromUri(mUriList.get(i), smbClient);
        StructStat stat = smbClient.stat(mUriList.get(i).toString());
        if (S_ISDIR(stat.st_mode)) { //路径
          // 隐藏lost+found文件夹
          if(!"lost+found".equals(document.getDisplayName())){
            directoryFile.add(mUriList.get(i));
            directoryFileCount++;
          }
        } else {
          //过滤以.开头的无效文件
          if (!(document.getDisplayName()).startsWith(".")) {
            otherFile.add(mUriList.get(i));
          }
        }
      }

      for (int i = 0; i < directoryFileCount; i++) {
        FileBean bean = new FileBean();
        DocumentMetadata document = DocumentMetadata.fromUri(directoryFile.get(i), smbClient);
        StructStat stat = smbClient.stat(directoryFile.get(i).toString());

        bean.setPath(document.getUri().toString());
        bean.setName(document.getDisplayName());
        bean.setDate(TimeUtils.millis2String(stat.st_ctime * 1000));
        bean.setLastModifyTime(TimeUtils.millis2String(stat.st_mtime*1000));

        bean.setSize(bytes2kb(document.getSize()));
        bean.setFileType(true);

        fileBeans.add(bean);
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }

  /**
   * @param bytes
   * @return
   * @Description long转文件大小M单位方法
   * @author temdy
   */
  public final static String bytes2kb(long bytes) {
    DecimalFormat df = new DecimalFormat("#.00");
    String fileSizeString = "";
    if (bytes < 1024) {
      fileSizeString = df.format((double) bytes) + "B";
    } else if (bytes < 1048576) {
      fileSizeString = df.format((double) bytes / 1024) + "K";
    } else if (bytes < 1073741824) {
      fileSizeString = df.format((double) bytes / 1048576) + "M";
    } else {
      fileSizeString = df.format((double) bytes / 1073741824) + "G";
    }
    return fileSizeString;
  }

}
