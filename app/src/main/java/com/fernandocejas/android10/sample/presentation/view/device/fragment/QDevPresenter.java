package com.fernandocejas.android10.sample.presentation.view.device.fragment;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetDevTreeUseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.DeviceCacheManager;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.qtec.mapp.model.req.GetDevTreeRequest;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse.DeviceBean;
import com.qtec.model.core.QtecEncryptInfo;

import java.util.List;

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
public class QDevPresenter implements Presenter {

  private final UseCase getDevTreeUseCase;
  private QDevView qDevView;
  private static boolean mNodeviceGuideShowed = false;
  private static boolean mdeviceGuideShowed = false;

  @Inject
  public QDevPresenter(@Named(CloudUseCaseComm.DEV_TREE) UseCase getDevTreeUseCase) {
    this.getDevTreeUseCase = checkNotNull(getDevTreeUseCase, "getDevTreeUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    getDevTreeUseCase.unsubscribe();
  }

  public void setView(QDevView qDevView) {
    this.qDevView = qDevView;
  }

  /**
   *
   * @param deviceType "0,1..."
   */
  public void getDevTree(String deviceType) {

    GetDevTreeRequest data = new GetDevTreeRequest();
    data.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    data.setDeviceType("");
    QtecEncryptInfo<GetDevTreeRequest> request = new QtecEncryptInfo<>();
    request.setData(data);
//    qDevView.showLoading();
    ((GetDevTreeUseCase) getDevTreeUseCase).setDeviceType(deviceType);
    getDevTreeUseCase.execute(request, new AppSubscriber<List<GetDevTreeResponse<List<DeviceBean>>>>(qDevView) {

      @Override
      public void onError(Throwable e) {
        super.handleLoginInvalid(e);
      }

      @Override
      protected void doNext(List<GetDevTreeResponse<List<DeviceBean>>> response) {
        if (response == null || response.isEmpty()) {
          qDevView.showNoDevice();
          if (!mNodeviceGuideShowed) {
            try {
              qDevView.showNoDeviceGuide();
            } catch (Exception e) {
              e.printStackTrace();
            }
            mNodeviceGuideShowed = true;
          }
        } else {
          LockManager.saveLocksLocal(qDevView.getContext(), response);
          LockManager.setUnlockMode(qDevView.getContext(), response);
          qDevView.showDevTree(response);
          if (!mdeviceGuideShowed) {
            try {
              qDevView.showDeviceGuide();
            } catch (Exception e) {
              e.printStackTrace();
            }
            mdeviceGuideShowed = true;
          }
        }

      }
    });

  }


}
