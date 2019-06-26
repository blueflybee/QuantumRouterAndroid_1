package com.fernandocejas.android10.sample.presentation.view.mine.myinfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.mapper.JsonMapper;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.fernandocejas.android10.sample.presentation.view.mine.data.UpdateData;
import com.fruit.updatelib.DownloadService;
import com.fruit.updatelib.UpdateChecker;
import com.qtec.mapp.model.req.CheckAppVersionRequest;
import com.qtec.mapp.model.rsp.CheckAppVersionResponse;
import com.qtec.model.core.QtecEncryptInfo;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@PerActivity
public class VersionInfoPresenter implements Presenter {

  private static boolean isChecked = false;

  private final UseCase checkAppVersionUseCase;
  private VersionInfoView mVersionInfoView;

  private CheckAppVersionResponse mVersionResponse;


  @Inject
  public VersionInfoPresenter(@Named(CloudUseCaseComm.CHECK_APP_VERSION) UseCase checkAppVersionUseCase) {
    this.checkAppVersionUseCase = checkNotNull(checkAppVersionUseCase, "checkAppVersionUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }


  @Override
  public void destroy() {
    checkAppVersionUseCase.unsubscribe();
  }

  public void setView(VersionInfoView versionInfoView) {
    this.mVersionInfoView = versionInfoView;
  }

  public void checkVersion() {
    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(null);
//    mVersionInfoView.showLoading();

    checkAppVersionUseCase.execute(encryptInfo, new AppSubscriber<CheckAppVersionResponse>(mVersionInfoView) {
      @Override
      protected void doNext(CheckAppVersionResponse response) {
        mVersionResponse = response;
        mVersionInfoView.showVersionInfo(response);
      }
    });
  }

  public void update() {
    if (mVersionResponse == null) {
      mVersionResponse = new CheckAppVersionResponse();
    }
    UpdateData updateData = new UpdateData();
    updateData.setUrl(mVersionResponse.getDownloadUrl());
    updateData.setVersionCode(mVersionResponse.getVersionNum());
    updateData.setUpdateMessage(mVersionResponse.getVersionStatement());
    UpdateChecker.checkForDownloadImmediate(mVersionInfoView.getContext(), new JsonMapper().toJson(updateData), mHandler);
    ToastUtils.showShort("请在通知栏查看下载进度");
  }

  public static boolean isChecked() {
    return isChecked;
  }

  public static void setIsChecked(boolean isChecked) {
    VersionInfoPresenter.isChecked = isChecked;
  }


  @SuppressLint("HandlerLeak")
  private Handler mHandler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case DownloadService.MSG_DOWNLOAD_SUCCESS:
          onDownloadSuccess((String) msg.obj);
          break;

        case DownloadService.MSG_DOWNLOAD_FAILED:
          ToastUtils.showShort("安装文件下载失败");
          break;

        default:
          break;
      }
    }
  };

  private void onDownloadSuccess(String filePath) {
    if (TextUtils.isEmpty(filePath)) {
      ToastUtils.showShort("安装文件下载失败");
      return;
    }
    ToastUtils.showShort("安装文件下载成功");
    install(filePath);
  }

  public void install(String filePath) {
    File file = new File(filePath);
    try {
      String[] command = {"chmod", "777", file.getPath()};
      ProcessBuilder builder = new ProcessBuilder(command);
      builder.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Intent intent = new Intent(Intent.ACTION_VIEW);
    // 由于没有在Activity环境下启动Activity,设置下面的标签
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    Context context = mVersionInfoView.getContext();
    if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上 //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致 参数3 共享的文件
      String authority = context.getPackageName() + ".fileprovider";
      System.out.println("authority = " + authority);
      Uri apkUri = FileProvider.getUriForFile(context, authority, file); //添加这一句表示对目标应用临时授权该Uri所代表的文件
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
    } else {
      intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
    }
    context.startActivity(intent);
  }

}
