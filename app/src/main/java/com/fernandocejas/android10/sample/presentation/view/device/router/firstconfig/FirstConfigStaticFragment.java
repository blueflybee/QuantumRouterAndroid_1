package com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentFirstConfigStaticBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.RouterComponent;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.StaticIPPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.StaticIPView;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.qtec.router.model.rsp.SetStaticIPResponse;

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
public class FirstConfigStaticFragment extends BaseFragment implements StaticIPView {

  private FragmentFirstConfigStaticBinding mBinding;

  @Inject
  StaticIPPresenter mStaticIPPresenter;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(RouterComponent.class).inject(this);
  }

  public static FirstConfigStaticFragment newInstance() {
    return new FirstConfigStaticFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_first_config_static, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initView();
    initPresenter();
  }

  private void initPresenter() {
    mStaticIPPresenter.setView(this);
  }

  private void initView() {
    mBinding.btnNext.setOnClickListener(v -> {
      mStaticIPPresenter.setStaticIp(
          getText(mBinding.etIp),
          getText(mBinding.etSubnetMask),
          getText(mBinding.etGateway),
          getText(mBinding.etFirstDns),
          getText(mBinding.etSecondDns), 0);
    });
    InputWatcher watcher = new InputWatcher();

    InputWatcher.WatchCondition ipCondition = new InputWatcher.WatchCondition();
    ipCondition.setInputRegular(new InputWatcher.InputRegular(InputUtil.REGULAR_IP));

    InputWatcher.WatchCondition maskCondition = new InputWatcher.WatchCondition();
    maskCondition.setInputRegular(new InputWatcher.InputRegular(InputUtil.REGULAR_MASK));

    watcher.addEt(mBinding.etIp, ipCondition);
    watcher.addEt(mBinding.etSubnetMask, maskCondition);
    watcher.addEt(mBinding.etGateway, ipCondition);
    watcher.addEt(mBinding.etFirstDns, ipCondition);
    watcher.addEt(mBinding.etSecondDns, ipCondition);


    watcher.setInputListener(isEmpty -> {
      mBinding.btnNext.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnNext.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnNext.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });


  }

  @Override
  public void showSetStaticIpSuccess(SetStaticIPResponse response) {
    mNavigator.navigateTo(getContext(), FirstConfigWifiActivity.class);
  }
}