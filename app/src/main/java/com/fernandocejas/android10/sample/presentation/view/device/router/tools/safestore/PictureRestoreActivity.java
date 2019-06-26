package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityPictureRestoreBinding;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.dbhelper.DatabaseUtil;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.SambaUtils;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.UploadUtils;
import com.google.android.sambadocumentsprovider.nativefacade.SmbClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author :
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   : 图片备份
 *     version: 1.0
 * </pre>
 */
public class PictureRestoreActivity extends BaseActivity {
  private ActivityPictureRestoreBinding mBinding;
  private int REQUEST_PICTURE_RESTORE = 1239;//照片备份
  public static List<FileBean> picInfoList;
  private DatabaseUtil dataBase;
  public static final int PERMISSION_REQUEST_CODE = 0;
  private static final String TAG = "PictureRestoreActivity";
  private SmbClient mClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_picture_restore);

    initView();

    initData();
  }

  private void initView() {
    initTitleBar("照片备份");

    if (PrefConstant.getPictureRestoreState()) {
      mBinding.switchBtnPicRestore.setChecked(true);
    } else {
      mBinding.switchBtnPicRestore.setChecked(false);
    }

    picInfoList = new ArrayList<>();
    dataBase = new DatabaseUtil(this);
    mClient = AndroidApplication.getSambaClient(this);
  }

  private void initData() {


    Intent intent = new Intent();
    intent.putExtra("SHAREPATH", getRemoteCurrentPath());
    this.setResult(REQUEST_PICTURE_RESTORE,intent);

    mBinding.switchBtnPicRestore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          Toast.makeText(PictureRestoreActivity.this, "图片备份已开启，为您备份到文件夹" + "\n" + "\"" + "来自: " + DeviceUtils.getModel() + "\"", Toast.LENGTH_SHORT).show();

          //存到缓存中
          PrefConstant.putPictureRestoreState(true);
          PrefConstant.putPictureRestoreRouterId(GlobleConstant.getgDeviceId());

          new newFolderTask("来自：" + DeviceUtils.getModel()).execute();//已存在的文件夹不会重新新建
        } else {
          //存到缓存中
          PrefConstant.putPictureRestoreState(false);
          PrefConstant.putPictureRestoreRouterId("");

          UploadUtils.stopPicRestoreTimer();//关闭定时器

          GlobleConstant.isLastPicRestored = true;

          GlobleConstant.isFinishedPicRestored = false;

          GlobleConstant.isStopRestorePicture = false;

          Toast.makeText(PictureRestoreActivity.this, "图片备份已关闭", Toast.LENGTH_SHORT).show();
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
      Boolean result = false;

      if (GlobleConstant.isSambaExtranetAccess){
         result = SambaUtils.createSambaFolder(mClient, LoginConfig.OS_REMOTE_ROOT_IP, newName);
      }else {
         result = SambaUtils.createSambaFolder(mClient, LoginConfig.LAN_REMOTE_ROOT_IP, newName);
      }

      return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
      if (result) {
        System.out.println("\"新建文件夹:\" = " + "新建成功！");
      } else {
        System.out.println("\"新建文件夹:\" = " + "文件夹已存在！");
      }

      requestPermissions();

      UploadUtils.getLocalPhoneList(PictureRestoreActivity.this);

      if (PrefConstant.getPictureRestoreState()) {
        if (GlobleConstant.getPicturePathList() != null && GlobleConstant.getPicturePathList().size() > 0) {
          UploadUtils.startPicRestoreTimer(mClient,PictureRestoreActivity.this);//开启定时器
        }
      }

    }

    private void requestPermissions() {
      String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
      PermissionUtils.requestPermissions(getContext(), PERMISSION_REQUEST_CODE, perms, new PermissionUtils.OnPermissionListener() {
        @Override
        public void onPermissionGranted() {
          Log.i(TAG, "允许权限");
        }

        @Override
        public void onPermissionDenied(String[] deniedPermissions) {
          Log.i(TAG, "拒绝权限");
        }
      });
    }
  }

  private String getRemoteCurrentPath() {
    return getIntent().getStringExtra("remoteSharePath");
  }
}
