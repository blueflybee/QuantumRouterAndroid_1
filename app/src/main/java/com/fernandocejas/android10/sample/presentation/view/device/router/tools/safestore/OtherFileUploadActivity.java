package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityOtherUploadBinding;
import com.fernandocejas.android10.sample.presentation.utils.UploadCacheManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.dbhelper.DatabaseUtil;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.SambaUtils;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.UploadUtils;
import com.google.android.sambadocumentsprovider.nativefacade.SmbClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/08/03
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class OtherFileUploadActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
  private LocalFileAdapter adapter;
  private ActivityOtherUploadBinding mBinding;
  private Map<String, Downloader> uploaders = new HashMap<String, Downloader>();
  private List<FileBean> mLocalFileBeans = null;
  public static String mLocalPath = "";
  // SD卡根目录，初始化为手机本地的SD卡
  private String mSDCard = "";
  // 后退还是退出
  private static boolean isExit_Back = true; // true=退出,false=后退
  private Boolean isSelectAll = false;
  private SmbClient mClient;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_other_upload);
    initTitleBar("上传其他文件");
    initView();

    initData();

    mTitleBar.setRightAs("全选", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (adapter != null) {

          if (isSelectAll) {
            isSelectAll = false;
            mTitleBar.getRightBtn().setText("全选");
            mBinding.btnUploadFile.setVisibility(View.GONE);
          } else {
            isSelectAll = true;
            mTitleBar.getRightBtn().setText("取消");
            mBinding.btnUploadFile.setVisibility(View.VISIBLE);
          }

          for (int i = 0; i < adapter.getCount(); i++) {
            if (isSelectAll) {
              adapter.checkedMap.put(i, true);
            } else {
              adapter.checkedMap.put(i, false);
            }
          }
          adapter.notifyDataSetChanged();
        }
      }
    });
  }

  private void initView() {
    mBinding.btnUploadFile.setOnClickListener(this);
    mBinding.lvFiles.setOnItemClickListener(this);
    mLocalFileBeans = new ArrayList<>();
    mClient = AndroidApplication.getSambaClient(this);

  }

  private void initData() {
    //super.run();
    mLocalPath = Environment.getExternalStorageDirectory().toString() + "/";
    // mSDCard = smb://192.168.3.1/
    mSDCard = mLocalPath;

    uploaders = ((AndroidApplication) getApplication()).getUploaders();

    initFileListInfo(mSDCard);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_upload_file:
        //上传逻辑处理
     /*   if (isSelect()) {*/

        Boolean isHasExceptionFile = false,isHasSelectFile = false;
        for (int i = 0; i < adapter.getCount(); i++) {
          if (adapter.checkedMap.get(i)) {
            isHasSelectFile = true;
            String path = adapter.getItem(i).getPath();
            File file = new File(path);
            if (file.length() == 0) {
              isHasExceptionFile = true;
              break;
            }
          }

        }

        if (!isHasSelectFile) {
          Toast.makeText(OtherFileUploadActivity.this, "请先选择文件再上传", Toast.LENGTH_SHORT).show();
          return;
        }

        if (isHasExceptionFile) {
          Toast.makeText(this, "暂不支持大小为0的异常文件上传,请先选择文件", Toast.LENGTH_SHORT).show();
          return;
        }

        if(SambaUtils.mIOException){
          SambaUtils.mIOException = false;
          Toast.makeText(this, "上传发生异常，请检查文件是否正在被操作或磁盘空间已满", Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < adapter.getCount(); i++) {
          //遍历checkedMap：选中状态,如果选中
          if (adapter.checkedMap.get(i)) {
            try {

              if (adapter.getItem(i).getFileType()) continue;

              String path = adapter.getItem(i).getPath();
              File file = new File(path);

              Downloader downloader = uploaders.get(path);
              if (downloader == null) {
                String desPath = SambaUtils.wrapSmbFileUrl(getCurrentRemoteFilePath(), new File(path).getName());
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

            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }

        System.out.println("上传器 otherFileActivity 添加数据量 uploaders.size() = " + uploaders.size());

        Toast.makeText(this, "成功添加上传任务", Toast.LENGTH_SHORT).show();
        mBinding.btnUploadFile.setText("上传");

        //启动三个
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

              SambaUtils.UploadTask uploadTask = new SambaUtils.UploadTask(uploaders, OtherFileUploadActivity.this, mClient, UploadFragment.mHandlerUpload);
              uploadTask.execute(sourcePath, despath, "1");
            } else {
              break;
            }

          } catch (Exception e) {
            e.printStackTrace();
          }
        }

        //启动一个定时器去监听
        // UploadUtils.startBatchUploadTimer(mClient,this,getCurrentRemoteFilePath(),uploaders);//开启定时器

        System.out.println("批量上传 otherFileFragment between uploaders.size = " + uploaders.size());

        Intent intent = new Intent();
        intent.putExtra("SHAREPATH", getRemoteCurrentSharePath());
        setResult(1235, intent);
        finish();

      /*  } else {
          Toast.makeText(this, "请先选择文件再上传", Toast.LENGTH_SHORT).show();
        }*/
        break;
    }
  }

/*  private boolean isSelect() {
    boolean isSelect = false;
    for (int i = 0; i < adapter.getCount(); i++) {
      //遍历checkedMap：选中状态,如果选中
      if (adapter.checkedMap.get(i)) {
        try {

          String path = adapter.getItem(i).getPath();
          File file = new File(path);
          if (adapter.getItem(i).getFileType()) continue;
          if (file.length() == 0) continue;

          Downloader downloader = uploaders.get(path);
          if (downloader == null) {
            String desPath = SambaUtils.wrapSmbFileUrl(getCurrentRemoteFilePath(), new File(path).getName());
            String desPath1 = desPath + ".upload";//为了首页不显示还未传输完的文件，加一个标志
            downloader = new Downloader(path, desPath1, 1);
            downloader.setState(Downloader.WAITING);//等待状态
            downloader.setCompeleteSize(0);
            downloader.setFileSize(file.length());
            downloader.setUrlstr(path);
            downloader.setLocalfile(desPath1);
            uploaders.put(path, downloader);
            //排除大小为0的损坏的文件
            isSelect = true;
            UploadCacheManager.putUploader(this, path, downloader);//写到文件
          }

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return isSelect;
  }*/

  /**
   * 遍历本地文件
   *
   * @param
   * @return
   */
  public boolean initFileListInfo(String path) {
    // mSDCard:smb://192.168.3.1/
    mLocalFileBeans.clear();

    if (path.equals(mSDCard))
      isExit_Back = true;
    else
      isExit_Back = false;

    try {
      final File mFile = new File(path);

      if (mFile.isDirectory()) {
        // 遍历出该文件夹路径下的所有文件/文件夹
        File[] mFiles = mFile.listFiles();
        // 对文件/文件夹进行排序
        if (mFile.length() != 0) {
          File[] newFile = UploadUtils.FileSort(mFiles, mLocalFileBeans);
            /* 将所有文件信息添加到集合中 */
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              adapter = new LocalFileAdapter(OtherFileUploadActivity.this, mLocalFileBeans, mBinding.btnUploadFile);
              mBinding.lvFiles.setAdapter(adapter);

              //进入默认显示选择框
              if (adapter != null) {
                setCheckVisibility(true);
              }
            }
          });

          mLocalPath = path;

          System.out.println("path = " + path);
          System.out.println("mCurrentFilePath = " + mLocalPath);
          System.out.println("========================");

          return true;
        }

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    final File mFile;

    try {
      mFile = new File(mLocalFileBeans.get(position).getPath());

      // 如果该文件是可读的，我们进去查看文件
      if (mFile.canRead()) {
        if (mFile.isDirectory()) {
          if (isSelectAll) {
            //编辑状态下不允许操作文件夹
            return;
          }
          // 如果是文件夹，则直接进入该文件夹，查看文件目录
          initFileListInfo(mLocalFileBeans.get(position).getPath());
        } else {
          //下载成功后直接打开:可配置
          // SambaUtils.openFile(OtherFileUploadActivity.this,mFile);
        }
      } else {
        // 如果该文件不可读，我们给出提示不能访问，防止用户操作系统文件造成系统崩溃等
        Toast.makeText(this, "文件不可读", Toast.LENGTH_SHORT).show();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      //如果是根目录的话则退出
      if (isExit_Back) {
        //最好提示是否退出的提示框
        finish();
      } else {
        // 子字符串从指定的字符位置开始且具有指定的长度。
        if (mLocalPath.lastIndexOf(File.separator) == (mLocalPath.length() - 1)) {

          String temp = mLocalPath.substring(0, mLocalPath.length() - 2);
          int lastIndexPosition = 0;
          lastIndexPosition = temp.lastIndexOf(File.separator);
          String fileName = temp.substring(0, lastIndexPosition + 1);
          initFileListInfo(fileName);
          System.out.println("keydown->fileName = " + fileName);

        } else {

          int lastIndexPosition = 0;
          lastIndexPosition = mLocalPath.lastIndexOf(File.separator);
          String fileName = mLocalPath.substring(0, lastIndexPosition + 1);
          initFileListInfo(fileName);
          System.out.println("keydown->fileName = " + fileName);
        }
      }
      return true;
    } else {
      return super.onKeyDown(keyCode, event);
    }
  }

  public void setCheckVisibility(boolean isCheck) {
    for (int i = 0; i < adapter.getCount(); i++) {
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

  private String getCurrentRemoteFilePath() {
    return getIntent().getStringExtra("CurrentRemoteFilePath");
  }

  private String getRemoteCurrentSharePath() {
    return getIntent().getStringExtra("remoteCurrentSharePath");
  }

}
