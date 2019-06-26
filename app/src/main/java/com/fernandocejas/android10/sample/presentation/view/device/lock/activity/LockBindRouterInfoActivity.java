package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityBindRouterInfoBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.data.Router;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.RouterInfoPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.RouterInfoView;
import com.qtec.mapp.model.rsp.CloudUnbindRouterLockResponse;
import com.qtec.mapp.model.rsp.GetRouterInfoCloudResponse;
import com.qtec.router.model.rsp.QueryBindRouterToLockResponse;
import com.qtec.router.model.rsp.UnbindRouterToLockResponse;

import javax.inject.Inject;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class LockBindRouterInfoActivity extends BaseActivity implements LockBindRouterInfoView, RouterInfoView {
  private ActivityBindRouterInfoBinding mBinding;

  @Inject
  LockBindRouterInfoPresenter mLockBindRouterInfoPresenter;

  @Inject
  RouterInfoPresenter mRouterInfoPresenter;

  private BleLock mBleLock;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_bind_router_info);

    initializeInjector();

    initPresenter();

    initData();

    initView();

    mRouterInfoPresenter.getRouterInfoCloud(getLockBindRouterId());

  }

  private void initView() {
    initTitleBar("网关信息");
  }

  public void unbindRouterFromLock(View view) {
    mLockBindRouterInfoPresenter.unbindRouter(getLockBindRouterId(), mBleLock.getId());
  }

  private void initPresenter() {
    mLockBindRouterInfoPresenter.setView(this);
    mRouterInfoPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerLockComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  private void initData() {
    mBleLock = LockManager.getLock(getContext(), lockMacAddress());
  }

  @Override
  public void showNotifyUnbindSuccess(UnbindRouterToLockResponse response) {
    DialogUtil.showQueryBindRouterDialog(getContext(), "正在解绑网关", "解绑网关可能需要30秒，请耐心等待", 30000, () -> {
      mLockBindRouterInfoPresenter.queryUnbindRouter(getLockBindRouterId(), mBleLock.getId());
    });
  }

  @Override
  public void showUnbindFail(QueryBindRouterToLockResponse response) {
    ToastUtils.showShort("解除绑定失败");
    DialogUtil.showCancelConfirmWithTipsDialog(getContext(),
        "提示", "无法连接设备，该网关可能不在线", "", "我知道了", "强制解帮", new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            mLockBindRouterInfoPresenter.unbindRouterCloud(mBleLock.getId());
          }
        });
  }

  @Override
  public void showUnbindSuccess(QueryBindRouterToLockResponse response) {
    ToastUtils.showShort("解除绑定成功");
    setResult(RESULT_OK);
    finish();
  }

  @Override
  public void showCloudUnbindSuccess(CloudUnbindRouterLockResponse response) {
    showUnbindSuccess(null);
  }

  private String getLockBindRouterId() {
    return getIntent().getStringExtra(Navigator.EXTRA_LOCK_BIND_ROUTER_ID);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mLockBindRouterInfoPresenter.destroy();
  }

  @Override
  public void showRouterInfo(Router.BaseInfo baseInfo) {

  }

  @Override
  public void showRouterInfoCloud(GetRouterInfoCloudResponse response) {
    mBinding.tvRouterName.setText(response.getRouterNickName());
//    mBinding.tvRouterGroup.setText(response.getGroupName());
    mBinding.tvDesc.setText(response.getDescription());
  }
}
