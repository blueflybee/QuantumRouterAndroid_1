package com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentFirstConfigPppoeBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.RouterComponent;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.PPPOEPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.PPPOEView;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.qtec.router.model.rsp.SetPPPOEResponse;

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
public class FirstConfigPPPOEFragment extends BaseFragment implements PPPOEView {

  private FragmentFirstConfigPppoeBinding mBinding;

  @Inject
  PPPOEPresenter mPPPOEPresenter;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(RouterComponent.class).inject(this);
  }

  public static FirstConfigPPPOEFragment newInstance() {
    return new FirstConfigPPPOEFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_first_config_pppoe, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initView();
    initPresenter();
  }

  private void initPresenter() {
    mPPPOEPresenter.setView(this);
  }

  private void initView() {
    mBinding.btnNext.setOnClickListener(v -> {
      mPPPOEPresenter.setPPPOE(getText(mBinding.etAccount), getText(mBinding.etPwd), 0);
    });
    mBinding.ivShowPwd.setOnClickListener(v -> InputUtil.showHidePwd(mBinding.etPwd));

    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition = new InputWatcher.WatchCondition();
    condition.setRange(new InputWatcher.InputRange(4, 20));
    watcher.addEt(mBinding.etAccount, condition);
    watcher.addEt(mBinding.etPwd, condition);
    watcher.setInputListener(isEmpty -> {
      mBinding.btnNext.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnNext.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnNext.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });

    InputUtil.inhibit(mBinding.etAccount, InputUtil.REGULAR_CHINESE_AND_SPACE, 20);
    InputUtil.inhibit(mBinding.etPwd, InputUtil.REGULAR_CHINESE_AND_SPACE, 20);
  }

  @Override
  public void showSetPPPOESuccess(SetPPPOEResponse response) {
    mNavigator.navigateTo(getContext(), FirstConfigWifiActivity.class);
  }
}