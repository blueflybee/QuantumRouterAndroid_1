package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.blankj.utilcode.util.TimeUtils;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityOtherUploadBinding;
import com.fernandocejas.android10.sample.presentation.utils.UploadCacheManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.SambaUtils;
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
 *      desc: 音频多选
 *      version: 1.0
 * </pre>
 */

public class AudiosUploadActivity extends BaseActivity implements View.OnClickListener {
  private LocalFileAdapter adapter;
  private ActivityOtherUploadBinding mBinding;
  private Map<String, Downloader> uploaders = new HashMap<String, Downloader>();
  private List<FileBean> mLocalFileBeans = null;
  // 存放已选择的网盘文件列表
  public static String mLocalPath = "";
  private Boolean isSelectAll = false;
  private SmbClient mClient;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_other_upload);
    initTitleBar("选择视频");
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
    mLocalFileBeans = new ArrayList<>();
    mClient = AndroidApplication.getSambaClient(this);
  }

  private void initData() {
    mLocalPath = Environment.getExternalStorageDirectory().toString() + "/";

    uploaders = ((AndroidApplication) getApplication()).getUploaders();

    searchSDVideos();
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
          Toast.makeText(AudiosUploadActivity.this, "请先选择文件再上传", Toast.LENGTH_SHORT).show();
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

        System.out.println("上传器 AudioUploadActivity 添加数据量 uploaders.size() = " + uploaders.size());

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

              SambaUtils.UploadTask uploadTask = new SambaUtils.UploadTask(uploaders, AudiosUploadActivity.this, mClient, UploadFragment.mHandlerUpload);
              uploadTask.execute(sourcePath, despath, "1");
            } else {
              break;
            }

          } catch (Exception e) {
            e.printStackTrace();
          }
        }

        System.out.println("批量上传 otherFileFragment between uploaders.size = " + uploaders.size());

        Intent intent = new Intent();
        intent.putExtra("SHAREPATH", getRemoteCurrentSharePath());
        setResult(1235, intent);
        finish();

       /* } else {
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
            String desPath1 = desPath+".upload";//为了首页不显示还未传输完的文件，加一个标志
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
   * 方法一：遍历所有文件夹，通过判断文件的扩展名来确定是否为要找的文件
   * 方法二：Android在开机时对所有的视频、音频文件进行扫描，并将其存在本地媒体库MediaStore中，所以我们可以通过访问MediaStore来获得本地视频文件。
   *
   * @param
   * @return
   */
  public void searchSDVideos() {
    mLocalFileBeans.clear();

    new Thread(new Runnable() {
      @Override
      public void run() {

        getAudioList();

        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            adapter = new LocalFileAdapter(AudiosUploadActivity.this, mLocalFileBeans, mBinding.btnUploadFile);
            mBinding.lvFiles.setAdapter(adapter);
            setCheckVisibility(true);
          }
        });
      }
    }).start();

  }

  /**
   * 遍历本地文件
   * 方法一：遍历所有文件夹，通过判断文件的扩展名来确定是否为要找的文件
   * 方法二：Android在开机时对所有的视频、音频文件进行扫描，并将其存在本地媒体库MediaStore中，所以我们可以通过访问MediaStore来获得本地视频文件。
   * UploadUtils.getVideoFile(mLocalFileBeans,file);
   *
   * @param
   * @return
   */

  public void getAudioList() {
    if (this != null) {
      Cursor cursor = this.getContentResolver().query(
          MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
          null, null);
      if (cursor != null) {
        while (cursor.moveToNext()) {
          String displayName = cursor
              .getString(cursor
                  .getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));

          String path = cursor
              .getString(cursor
                  .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
          String date = cursor
              .getString(cursor
                  .getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED));

          long size = cursor
              .getLong(cursor
                  .getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
          FileBean bean = new FileBean();

          bean.setLastModifyTime(TimeUtils.millis2String(new File(path).lastModified()));

          if (size == 0) {
            //已损害的文件大小为0
            bean.setSize("0K");
          } else {
            bean.setSize(SambaUtils.bytes2kb(size));
          }

          bean.setPath(path);
          bean.setName(displayName);
          bean.setFileType(false);
          mLocalFileBeans.add(bean);
        }
        cursor.close();
      }
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    return super.onKeyDown(keyCode, event);
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
