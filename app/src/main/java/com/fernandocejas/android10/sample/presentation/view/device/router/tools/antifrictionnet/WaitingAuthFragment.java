package com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentWaitingAuthBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.RouterComponent;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter.WaitAuthListAdapter;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.qtec.router.model.rsp.GetWaitingAuthDeviceListResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class WaitingAuthFragment extends BaseFragment implements AdapterView.OnItemClickListener, IGetAuthDeviceListView {

  private FragmentWaitingAuthBinding mBinding;
  private WaitAuthListAdapter adapter;
  private List<GetWaitingAuthDeviceListResponse> waitAuthList;

  @Inject
  GetWaitingAuthDeviceListPresenter mWaitingAuthListPresenter;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(RouterComponent.class).inject(this);
  }

  public static WaitingAuthFragment newInstance() {
    WaitingAuthFragment fragment = new WaitingAuthFragment();
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_waiting_auth, container, false);
    System.out.println("防蹭网1:" + "onCreateView");
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    System.out.println("防蹭网1:" + "onViewCreated");
    initView();
    initPresenter();

    getWaitingAuthDeviceList();

  }

  @Override
  public void onResume() {
    super.onResume();
    System.out.println("防蹭网1:" + "onResume");
  }

  @Override
  public void onPause() {
    super.onPause();
    System.out.println("防蹭网1:" + "onPause");
  }

  private void initView() {
    waitAuthList = new ArrayList<>();
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Intent intent = new Intent();
    intent.putExtra("DEVICEDETAIL",waitAuthList.get(position));
    mNavigator.navigateTo(getActivity(),WaitAuthDetailActivity.class,intent);
  }

  /**
   * 获取待认证设备列表
   *
   * @param
   * @return
   */
  @Override
  public void getWaitingAuthDeviceList(List<GetWaitingAuthDeviceListResponse> response) {
    waitAuthList.clear();
    waitAuthList.addAll(response);

    if (waitAuthList.size() == 0 || waitAuthList == null) {
      return;
    }

    adapter = new WaitAuthListAdapter(getActivity(), waitAuthList, R.layout.item_wait_auth);
    mBinding.lvWaitAuth.setAdapter(adapter);
    mBinding.lvWaitAuth.setOnItemClickListener(this);
  }

  private void initPresenter() {
    mWaitingAuthListPresenter.setView(this);
  }

  private void getWaitingAuthDeviceList() {
    mWaitingAuthListPresenter.getWaitingAuthDeviceList(GlobleConstant.getgDeviceId());
  }

}