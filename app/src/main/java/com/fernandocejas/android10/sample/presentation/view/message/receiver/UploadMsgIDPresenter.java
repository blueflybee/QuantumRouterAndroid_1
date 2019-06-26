package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import android.os.Handler;
import android.text.TextUtils;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.req.UploadMsgDeviceIDRequest;
import com.qtec.mapp.model.rsp.UploadMsgDeviceIDResponse;
import com.qtec.model.core.QtecEncryptInfo;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author shaojun
 * @name LoginPresenter
 * @package com.fernandocejas.android10.sample.presentation.presenter
 * @date 15-9-10
 */
@PerActivity
public class UploadMsgIDPresenter implements Presenter {

  private final UseCase uploadMsgIDUseCase;
  private IUploadMsgIdView uploadMsgIDView;

  @Inject
  public UploadMsgIDPresenter(@Named(CloudUseCaseComm.UPLOAD_MSG_ID) UseCase uploadMsgIDUseCase) {
    this.uploadMsgIDUseCase = uploadMsgIDUseCase;
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    uploadMsgIDUseCase.unsubscribe();
  }

  public void setView(IUploadMsgIdView uploadMsgIDView) {
    this.uploadMsgIDView = uploadMsgIDView;
  }

  public void uploadMsgId() {
    UploadMsgDeviceIDRequest request = new UploadMsgDeviceIDRequest();
    request.setDeviceSerialNo(PrefConstant.getMsgDeviceID());
    request.setPlatform(AppConstant.PLATFORM_ANDROID);
    request.setUserPhone(PrefConstant.getUserPhone());

    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);

    uploadMsgIDView.showLoading();

    uploadMsgIDUseCase.execute(encryptInfo, new AppSubscriber<UploadMsgDeviceIDResponse>(uploadMsgIDView) {
      @Override
      public void onError(Throwable e) {
        System.out.println("设备ID上传失败，重新轮询 传至服务端");
        uploadMsgId();//上传失败 继续上传

        super.onError(e);
      }

      @Override
      protected void doNext(UploadMsgDeviceIDResponse uploadResponse) {
        System.out.println("++++++++++++++++++=" + uploadResponse);
        uploadMsgIDView.uploadMsgID(uploadResponse);
      }
    });
  }

  /**
   * 登录之后判断消息设备序号是否为空,为空则记录下来定时去取直到取到并上传至服务器
   *
   * @param
   * @return
   */
  public void isMsgDeviceIdEmpty() {

    if (TextUtils.isEmpty(PrefConstant.getMsgDeviceID())) {
      final Handler handler = new Handler();

      handler.postDelayed(new Runnable() {
        @Override
        public void run() {

          if (TextUtils.isEmpty(PrefConstant.getMsgDeviceID())) {
            handler.postDelayed(this, 2000);//每隔2s轮询一次
            /*System.out.println("查看阿里云返回ID是否为空");*/
            System.out.println("阿里云设备ID  为空，启动轮询");
          } else {
            handler.removeCallbacks(this);//移除定时器
            //上传至服务端
            uploadMsgId();
            /*System.out.println("阿里云设备ID不为空，开始  传至服务端");*/
          }
        }
      }, 500);
    }else {
      System.out.println("阿里云设备ID  不为空，不启动轮询");
    }
  }
}
