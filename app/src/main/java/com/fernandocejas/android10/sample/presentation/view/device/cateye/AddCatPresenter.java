package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.mapper.JsonMapper;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.req.CommitAddRouterInfoRequest;
import com.qtec.mapp.model.rsp.CommitAddRouterInfoResponse;
import com.qtec.model.core.QtecEncryptInfo;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@PerActivity
public class AddCatPresenter implements Presenter {

  private final UseCase commitAddCatInfoUseCase;
  private AddCatView addCatView;

  @Inject
  public AddCatPresenter(
      @Named(CloudUseCaseComm.COMMIT_ADD_ROUTER_INFO) UseCase commitAddCatInfoUseCase) {
    this.commitAddCatInfoUseCase = checkNotNull(commitAddCatInfoUseCase, "commitAddCatInfoUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {
  }

  @Override
  public void destroy() {
    commitAddCatInfoUseCase.unsubscribe();
  }

  public void setView(AddCatView addCatView) {
    this.addCatView = addCatView;
  }

  public void addCatCloud(String deviceSerialNo) {
    CommitAddRouterInfoRequest request = new CommitAddRouterInfoRequest();
    if (AppConstant.DEVICE_TYPE_CAT.equals(GlobleConstant.getgDeviceType())) {
      request.setDeviceType(AppConstant.DEVICE_TYPE_CAT);
      request.setDeviceName("电子猫眼");
    } else {
      request.setDeviceType(AppConstant.DEVICE_TYPE_CAMERA);
      request.setDeviceName("智能摄像机");
    }

    request.setDeviceSerialNo(deviceSerialNo);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

    //生成一个6位的随机码 上报服务器
    int randPwd = (int) ((Math.random() * 9 + 1) * 100000);
    HashMap<String, String> map = new HashMap<>();
    map.put("devicePass", String.valueOf(randPwd));
    request.setDeviceDetail(new JsonMapper().toJson(map));

    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);

    addCatView.showLoading();

    commitAddCatInfoUseCase.execute(encryptInfo, new AppSubscriber<CommitAddRouterInfoResponse>(addCatView) {
      @Override
      public void onError(Throwable e) {
        super.handleLoginInvalid(e);
        addCatView.onAddCatFailed();
      }

      @Override
      protected void doNext(CommitAddRouterInfoResponse response) {
        addCatView.onAddCatSuccess(response);
      }

    });
  }
}
