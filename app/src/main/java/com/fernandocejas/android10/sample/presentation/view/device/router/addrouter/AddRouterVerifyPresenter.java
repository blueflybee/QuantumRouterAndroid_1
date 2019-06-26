package com.fernandocejas.android10.sample.presentation.view.device.router.addrouter;

import android.os.Build;

import com.blankj.utilcode.util.DeviceUtils;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.google.common.base.Strings;
import com.qtec.mapp.model.req.CommitAddRouterInfoRequest;
import com.qtec.mapp.model.rsp.CommitAddRouterInfoResponse;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.AddRouterVerifyRequest;
import com.qtec.router.model.rsp.AddRouterVerifyResponse;

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
public class AddRouterVerifyPresenter implements Presenter {


  private final UseCase addRouterVerifyUseCase;
  private final UseCase commitAddRouterInfoUseCase;
  private AddRouterVerifyView addRouterVerifyView;

  @Inject
  public AddRouterVerifyPresenter(
      @Named(RouterUseCaseComm.ADD_ROUTER_VERIFY) UseCase addRouterVerifyUseCase,
      @Named(CloudUseCaseComm.COMMIT_ADD_ROUTER_INFO) UseCase commitAddRouterInfoUseCase) {
    this.addRouterVerifyUseCase = checkNotNull(addRouterVerifyUseCase, "addRouterVerifyUseCase cannot be null!");
    this.commitAddRouterInfoUseCase = checkNotNull(commitAddRouterInfoUseCase, "commitAddRouterInfoUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {
  }

  @Override
  public void destroy() {
    addRouterVerifyUseCase.unsubscribe();
    commitAddRouterInfoUseCase.unsubscribe();
  }

  public void setView(AddRouterVerifyView addRouterVerifyView) {
    this.addRouterVerifyView = addRouterVerifyView;
  }

  void addRouterVerify(String routerId, String pwd, int configured) {
    if (configured != 0 && Strings.isNullOrEmpty(pwd)) {
      addRouterVerifyView.showAdminPwdEmp();
      return;
    }
    addRouterVerifyView.showLoading();

    AddRouterVerifyRequest data = new AddRouterVerifyRequest();
    data.setPassword(pwd);
    data.setUsername(PrefConstant.getUserPhone());
    data.setStamac(DeviceUtils.getMacAddress());
    data.setStaname(Build.MODEL);
    data.setStasysinfo(Build.VERSION.RELEASE);

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerId, data, RouterUrlPath.PATH_ADD_ROUTER_VERIFY, RouterUrlPath.METHOD_POST);

    addRouterVerifyUseCase.execute(multiEncryptInfo, new AppSubscriber<AddRouterVerifyResponse>(addRouterVerifyView) {
      @Override
      protected void doNext(AddRouterVerifyResponse response) {
        // TODO: 17-8-1 可以使用rxjava链式调用
        addRouterVerifyView.onAddSuccess(response);
      }
    });
  }

  void commitRouterInfo(QtecEncryptInfo<CommitAddRouterInfoRequest> request) {
    addRouterVerifyView.showLoading();
    commitAddRouterInfoUseCase.execute(request, new AppSubscriber<CommitAddRouterInfoResponse>(addRouterVerifyView) {
      @Override
      protected void doNext(CommitAddRouterInfoResponse response) {
        addRouterVerifyView.onCommitSuccess(response);
      }
    });

  }

}
