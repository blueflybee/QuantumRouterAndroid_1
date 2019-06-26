package com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentFirstConfigDhcpBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.RouterComponent;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.DHCPPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.DHCPView;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.qtec.router.model.rsp.SetDHCPResponse;

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
public class FirstConfigDHCPFragment extends BaseFragment implements DHCPView {

  private FragmentFirstConfigDhcpBinding mBinding;

  @Inject
  DHCPPresenter mDHCPPresenter;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(RouterComponent.class).inject(this);
  }

  public static FirstConfigDHCPFragment newInstance() {
    return new FirstConfigDHCPFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_first_config_dhcp, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initView();
    initPresenter();
//    queryRouterStatus();
  }

  private void initPresenter() {
    mDHCPPresenter.setView(this);
  }

  private void initView() {
    mBinding.btnNext.setOnClickListener(v -> {
      mDHCPPresenter.setDHCP(0);
    });
  }

  @Override
  public void showSetDHCPSuccess(SetDHCPResponse response) {
    mNavigator.navigateTo(getContext(), FirstConfigWifiActivity.class);
  }
}