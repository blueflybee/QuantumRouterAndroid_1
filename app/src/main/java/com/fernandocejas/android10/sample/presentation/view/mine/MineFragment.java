package com.fernandocejas.android10.sample.presentation.view.mine;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SPUtils;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentMineBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.MainComponent;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.GlideUtil;
import com.fernandocejas.android10.sample.presentation.utils.TimeUtil;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.fernandocejas.android10.sample.presentation.view.mine.aboutus.AboutUsActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback.AdviceFeedBackActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.deviceshare.DeviceShareMainActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.deviceshare.GetRouterListPresenter;
import com.fernandocejas.android10.sample.presentation.view.mine.deviceshare.IGetRouterListView;
import com.fernandocejas.android10.sample.presentation.view.mine.myinfo.MyInfoActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.myinfo.VersionInfoActivity;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetDeviceCountResponse;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class MineFragment extends BaseFragment implements IGetDeviceCountView, IGetRouterListView {
  public static final int REQUEST_REFRESH_NICK_NAME = 100;
  private FragmentMineBinding mBinding;

  @Inject
  GetDeviceCountPresenter mGetDeviceCountPresenter;
  @Inject
  GetRouterListPresenter mGetRouterListPresenter;

  public static MineFragment newInstance() {
    Bundle args = new Bundle();
    MineFragment fragment = new MineFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(MainComponent.class).inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initPresenter();
    initView();
    requestDevicesCount();
  }

  private void initView() {
    mBinding.rlAboutUs.setOnClickListener(v -> mNavigator.navigateTo(getContext(), AboutUsActivity.class));
    mBinding.rlAdviceFeedBack.setOnClickListener(v -> mNavigator.navigateTo(getContext(), AdviceFeedBackActivity.class));
    mBinding.rlDeviceShare.setOnClickListener(v -> mGetRouterListPresenter.getRouterList());
    mBinding.rlVersionInfo.setOnClickListener(v -> mNavigator.navigateTo(getContext(), VersionInfoActivity.class));
    initUserInfoView();
  }

  private void requestDevicesCount() {
    mGetDeviceCountPresenter.getDeviceCount();
  }

  private void initPresenter() {
    mGetDeviceCountPresenter.setView(this);
    mGetRouterListPresenter.setView(this);
  }

  private void initUserInfoView() {
    SPUtils spUtils = new SPUtils(PrefConstant.SP_NAME);
    String formatStr = TimeUtil.timeFormatStr(Calendar.getInstance());

    mBinding.tvNickName.setText(PrefConstant.getUserNickName());
    mBinding.tvLabel.setText("，" + formatStr + "好!");


    mBinding.imgHead.setOnClickListener(v -> {
      Intent intent = new Intent(getActivity(), MyInfoActivity.class);
      startActivityForResult(intent, REQUEST_REFRESH_NICK_NAME);
    });

    GlideUtil.loadCircleHeadImage(getContext(), spUtils.getString(PrefConstant.SP_USER_HEAD_IMG), mBinding.imgHead);
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);  //这个super可不能落下，否则可能回调不了
    switch (requestCode) {
      case REQUEST_REFRESH_NICK_NAME:
        if (resultCode == RESULT_OK) {
          initUserInfoView();
        }
        break;
    }
  }

  @Override
  public void getDeviceCount(GetDeviceCountResponse response) {
    if (TextUtils.isEmpty(response.getRouterNum())) {
      mBinding.tvDeviceCount.setText("您现在拥有0个设备");
    } else {
      mBinding.tvDeviceCount.setText("您现在拥有" + response.getRouterNum() + "个设备");
    }

  }

  @Override
  public void getRouterList(List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> response) {
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTR_ROUTER_LIST, (Serializable) response);

    /*mNavigator.navigateTo(getContext(), RouterListActivity.class, intent);*/
    mNavigator.navigateTo(getContext(), DeviceShareMainActivity.class, intent);
  }
}
