package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.CreateRouterGroupRequest;
import com.qtec.mapp.model.req.GetRouterGroupsRequest;
import com.qtec.mapp.model.req.ModifyRouterGroupRequest;
import com.qtec.mapp.model.rsp.CreateRouterGroupResponse;
import com.qtec.mapp.model.rsp.GetRouterGroupsResponse;
import com.qtec.mapp.model.rsp.ModifyRouterGroupResponse;
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
class RouterGroupPresenter implements Presenter {

  private final UseCase getRouterGroupsUseCase;
  private final UseCase modifyRouterGroupUseCase;
  private final UseCase createRouterGroupUseCase;
  private RouterGroupView mRouterGroupView;

  @Inject
  public RouterGroupPresenter(@Named(CloudUseCaseComm.GET_ROUTER_GROUPS) UseCase getRouterGroupsUseCase,
                              @Named(CloudUseCaseComm.MODIFY_ROUTER_GROUP) UseCase modifyRouterGroupUseCase,
                              @Named(CloudUseCaseComm.CREATE_ROUTER_GROUP) UseCase createRouterGroupUseCase) {
    this.getRouterGroupsUseCase = checkNotNull(getRouterGroupsUseCase, "getRouterInfoUseCase cannot be null!");
    this.modifyRouterGroupUseCase = checkNotNull(modifyRouterGroupUseCase, "modifyRouterGroupUseCase cannot be null!");
    this.createRouterGroupUseCase = checkNotNull(createRouterGroupUseCase, "createRouterGroupUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    getRouterGroupsUseCase.unsubscribe();
    modifyRouterGroupUseCase.unsubscribe();
    createRouterGroupUseCase.unsubscribe();
  }

  public void setView(RouterGroupView routerGroupView) {
    this.mRouterGroupView = routerGroupView;
  }

  void getRouterGroups() {

    GetRouterGroupsRequest request = new GetRouterGroupsRequest();
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mRouterGroupView.showLoading();
    getRouterGroupsUseCase.execute(encryptInfo, new AppSubscriber<List<GetRouterGroupsResponse>>(mRouterGroupView) {

      @Override
      protected void doNext(List<GetRouterGroupsResponse> response) {
        mRouterGroupView.showRouterGroups(response);
      }
    });

  }
  void modifyRouterGroup(String groupId) {

    ModifyRouterGroupRequest request = new ModifyRouterGroupRequest();
    request.setGroupId(groupId);
    request.setRouterSerialNo(GlobleConstant.getgDeviceId());
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mRouterGroupView.showLoading();
    modifyRouterGroupUseCase.execute(encryptInfo, new AppSubscriber<ModifyRouterGroupResponse>(mRouterGroupView) {

      @Override
      protected void doNext(ModifyRouterGroupResponse response) {
        mRouterGroupView.showModifyRouterGroupSuccess();
      }
    });

  }


  public void addGroup(String groupName) {
    CreateRouterGroupRequest request = new CreateRouterGroupRequest();
    request.setGroupName(groupName);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mRouterGroupView.showLoading();
    createRouterGroupUseCase.execute(encryptInfo, new AppSubscriber<CreateRouterGroupResponse>(mRouterGroupView) {

      @Override
      protected void doNext(CreateRouterGroupResponse response) {

        mRouterGroupView.showCreateRouterGroupSuccess();
      }
    });
  }
}
