package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.keystore.KeystoreRepertory;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentRouterSettingBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.RouterComponent;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.device.router.adapter.RouterSettingAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.data.Router;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.mapp.model.rsp.UnbindRouterResponse;
import com.qtec.router.model.rsp.FactoryResetResponse;
import com.qtec.router.model.rsp.RestartRouterResponse;

import javax.inject.Inject;

/**
 * <pre>
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.0
 * </pre>
 */
public class RouterSettingFragment extends BaseFragment implements RouterSettingView {

  private FragmentRouterSettingBinding mBinding;

  @Inject
  RouterSettingPresenter mRouterSettingPresenter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(RouterComponent.class).inject(this);
  }

  public static RouterSettingFragment newInstance() {
    return new RouterSettingFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_router_setting, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initView();
    initPresenter();
  }

  private void initPresenter() {
    mRouterSettingPresenter.setView(this);
  }

  private void initView() {
    mBinding.listRouterSetting.setAdapter(new RouterSettingAdapter(getContext(), new Router().getRouterSettings()));
    mBinding.listRouterSetting.setOnItemClickListener((parent, view, position, id) -> {
      switch (position) {
        case 0:
          mNavigator.navigateTo(getContext(), RouterInfoActivity.class);
          break;
        case 1:
          mNavigator.navigateTo(getContext(), WifiSettingActivity.class);
          break;
        case 2:
          mNavigator.navigateTo(getContext(), NetModeActivity.class);
          break;
        case 3:
          mNavigator.navigateTo(getContext(), AdminPwdActivity.class);
          break;
        case 4:
          mNavigator.navigateTo(getContext(), RestartRouterActivity.class);
          break;
        case 5:
          mNavigator.navigateTo(getContext(), UpdateFirmwareActivity.class);
          break;
        case 6:
          Intent intent = new Intent();
          intent.putExtra(Navigator.EXTRA_ROUTER_ID,getRouterId());
          mNavigator.navigateTo(getContext(), VPNActivity.class,intent);
          break;

        default:
          break;
      }
    });
    mBinding.btnRestartRouter.setOnClickListener(v -> {
      DialogUtil.showOkCancelAlertDialogWithMessage(
          getContext(), "重启网关", "您确认要重启网关吗？",
          v1 -> mRouterSettingPresenter.restartRouter(), null);

    });
    mBinding.btnRestoreSettings.setOnClickListener(v -> {
      DialogUtil.showOkCancelAlertDialogWithMessage(
          getContext(), "恢复出厂设置", "您确认要恢复出厂设置吗？",
          v1 -> mRouterSettingPresenter.factoryReset(), null);
    });

    mBinding.btnUnbindRouter.setOnClickListener(v -> {
      DialogUtil.showOkCancelAlertDialogWithMessage(
          getContext(), "解绑网关", "确认要解绑网关吗？",
          v1 -> mRouterSettingPresenter.unbindRouter(), null);
    });
  }

  @Override
  public void restartRouterSuccess(RestartRouterResponse response) {
    mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class);
    ToastUtils.showLong("网关正在重启，请稍候...");
  }

  @Override
  public void factoryResetSuccess(FactoryResetResponse response) {
    KeystoreRepertory.getInstance().clear(GlobleConstant.getgKeyRepoId());
    mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class);
    ToastUtils.showLong("网关正在恢复出厂设置，请稍候...");
  }

  @Override
  public void unbindRouterSuccess(UnbindRouterResponse response, String deviceId) {
    String keyRepoId = deviceId + "_" + PrefConstant.getUserUniqueKey("0");
    KeystoreRepertory.getInstance().clear(keyRepoId);
    mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class);
    ToastUtils.showLong("网关解除绑定成功");
  }
  private String getRouterId() {
    return getActivity().getIntent().getStringExtra(Navigator.EXTRA_ROUTER_ID);
  }


}