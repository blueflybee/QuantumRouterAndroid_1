package com.fernandocejas.android10.sample.presentation.view.safe;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentSafetyTestBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.MainComponent;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.device.router.addrouter.AddRouterActivity;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.fernandocejas.android10.sample.presentation.view.mine.MineFragment;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.router.model.rsp.SearchRouterResponse;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc: 安全模块
 *      version: 1.0
 * </pre>
 */
public class SafeFragment extends BaseFragment implements GetRouterConnectDirectIDView {

  private FragmentSafetyTestBinding mBinding;
 /* @Inject
  GetDeviceTreeListPresenter mGetDeviceTreeListPresenter;*/
  @Inject
  GetRouterConnectDirectIdPresenter mAddRouterPresenter;
  private List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> mResponse;
  private Boolean isRequesting = false; //判断是否正在进行请求

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(MainComponent.class).inject(this);
  }

  public static SafeFragment newInstance() {
    Bundle args = new Bundle();
    SafeFragment fragment = new SafeFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_safety_test, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initPresenter();
    initView();
  }

  /*private void queryDeviceTreeList() {
    mGetDeviceTreeListPresenter.getRouterList();
  }*/

  private void initView() {
    mBinding.imgInspection.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //queryDeviceTreeList();
        Boolean isHasRouter = false;

        if(isRequesting){
          Toast.makeText(getActivity(), "网络繁忙，请稍候…", Toast.LENGTH_SHORT).show();
          return;
        }

        if(GlobleConstant.getDeviceTrees() != null){
          for (int i = 0; i < GlobleConstant.getDeviceTrees().size(); i++) {
            if((AppConstant.DEVICE_TYPE_ROUTER).equals(GlobleConstant.getDeviceTrees().get(i).getDeviceType())){
              isHasRouter = true;
              break;
            }
          }
        }

        if(GlobleConstant.getDeviceTrees() != null && isHasRouter){
          mResponse = GlobleConstant.getDeviceTrees();
          isRequesting = true;
          if(mAddRouterPresenter != null){
            mAddRouterPresenter.searchRouter();
          }

        }else{
          mBinding.tvSafeTestPrompt.setText("还没有安全设备，请先添加安全设备 +");
          Toast.makeText(getActivity(), "还没有安全设备，请先添加安全设备", Toast.LENGTH_SHORT).show();

          mBinding.tvSafeTestPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getActivity(), AddRouterActivity.class));
            }
          });
        }
      }
    });
  }

  private void initPresenter() {
   // mGetDeviceTreeListPresenter.setView(this);
    mAddRouterPresenter.setView(this);
  }

/*  @Override
  public void getRouterList(List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> response) {
*//*    if(response.size() > 0){
      mResponse = response;

      mAddRouterPresenter.searchRouter();

    }else{
      mBinding.tvSafeTestPrompt.setText("还没有安全设备，请先添加安全设备 +");
      Toast.makeText(getActivity(), "还没有安全设备，请先添加安全设备", Toast.LENGTH_SHORT).show();

      mBinding.tvSafeTestPrompt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          startActivity(new Intent(getActivity(), AddRouterActivity.class));
        }
      });
    }*//*

  }*/

  @Override
  public void showSearchSuccess(SearchRouterResponse response) {
    isRequesting = false;
    //搜索到路由器 用直连路由器的密钥库
    String routerId = response.getSerialnum();
    GlobleConstant.setgDeviceId(routerId);
    String keyRepoId = routerId + "_" + PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER);
    GlobleConstant.setgKeyRepoId(keyRepoId);
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTR_ROUTER_LIST, (Serializable) mResponse);
    mNavigator.navigateTo(getContext(), SafeTestActivity.class, intent);
  }

  @Override
  public void showSearchFailed(Throwable e) {
    isRequesting = false;
    //没有搜索到路由器 用设备列表中第一台路由器（如存在）的密钥库
    GlobleConstant.setgDeviceId(null);
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTR_ROUTER_LIST, (Serializable) mResponse);
    mNavigator.navigateTo(getContext(), SafeTestActivity.class, intent);
  }
}