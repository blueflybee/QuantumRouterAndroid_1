package com.fernandocejas.android10.sample.presentation.view.device.router.status;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentRouterStatusBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.RouterComponent;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.device.router.adapter.RouterStatusClientAdapter;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.qtec.router.model.rsp.RouterStatusResponse;
import com.qtec.router.model.rsp.RouterStatusResponse.Status;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import kale.adapter.util.rcv.RcvOnItemClickListener;

/**
 * <pre>
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.0
 * </pre>
 */
public class RouterStatusFragment extends BaseFragment implements RouterStatusView {

  private FragmentRouterStatusBinding mBinding;
  List<RouterStatusResponse.Status> mMemDetails;
  private List<RouterStatusResponse.Status> mOffLineDevice;
  private Boolean isEnterItem = false;//判断是否进入成员详情或黑名单

  private Handler mHandler = new Handler();
  private Runnable mRunnable = new Runnable() {
    @Override
    public void run() {
      queryRouterStatus();//两秒刷一次
      mHandler.postDelayed(this, 1900);//每隔3s轮询一次
    }
  };

  @Inject
  RouterStatusPresenter mRouterStatusPresenter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(RouterComponent.class).inject(this);
  }

  public static RouterStatusFragment newInstance() {
    return new RouterStatusFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_router_status, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initView();
    initPresenter();

    mHandler.postDelayed(mRunnable, 100);
    System.out.println("主页 onViewCreated");
    isEnterItem = false;

  }

  private void initPresenter() {
    mRouterStatusPresenter.setView(this);
  }

  private void initView() {
    mMemDetails = new ArrayList<>();
    mOffLineDevice = new ArrayList<>();
    mBinding.tvRouterName.setText(getRouterName() + "\n" + getRouterModel());

    mBinding.rvRouterStatus.setLayoutManager(new LinearLayoutManager(getActivity()));
    mBinding.rvRouterStatus.setAdapter(new RouterStatusClientAdapter(getContext()));

    mBinding.rvRouterStatus.addOnItemTouchListener(new RcvOnItemClickListener(getContext(), new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mMemDetails.size() > 0){
          isEnterItem = true;
          Intent intent = new Intent(getContext(), RouterMemDetailActivity.class);
          intent.putExtra("Detail", mMemDetails.get(position));
          intent.putExtra(Navigator.EXTRA_ROUTER_ID, GlobleConstant.getgDeviceId());
          startActivity(intent);
        }
      }
    }));

    mBinding.btnRouterStatusBlacklist.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        isEnterItem = true;
        Intent intent = new Intent();
        intent.putExtra(Navigator.EXTRA_ROUTER_ID, GlobleConstant.getgDeviceId());
        startActivity(new Intent(getActivity(), BlackListActivity.class));
      }
    });
  }

  private void queryRouterStatus() {
    mRouterStatusPresenter.getRouterStatus(GlobleConstant.getgDeviceId());
  }

  @Override
  public void showRouterStatus(RouterStatusResponse<List<Status>> response) {
    mMemDetails.clear();
    mOffLineDevice.clear();

    mBinding.btnRouterStatusBlacklist.setVisibility(View.VISIBLE);

    //作一个排序：在线设备在前 离线设备在后
    for (int i = 0; i < response.getStalist().size(); i++) {
      //0 offline ; >0 OnLine
      if(response.getStalist().get(i).getStastatus() == 0){
        mOffLineDevice.add(response.getStalist().get(i));
      }else {
        mMemDetails.add(response.getStalist().get(i));
      }
    }

    if(mOffLineDevice.size() > 0){
      for (int i = 0; i < mOffLineDevice.size(); i++) {
        mMemDetails.add(mOffLineDevice.get(i));
      }
    }
    //返回字节,保留两位小数
    //bean.setSpeed(String.format("%.1f", Float.parseFloat(response.getDownload().trim())/8));//保留一位小数

    mBinding.tvRouterDownspeed.setText(String.format("%.2f",Float.parseFloat((response.getRouterrx()*1f/1024)+"")) + " KB/s");
    mBinding.tvRouterUpspeed.setText(String.format("%.2f",Float.parseFloat((response.getRoutertx()*1f/1024)+"")) + " KB/s");

    ((RouterStatusClientAdapter) mBinding.rvRouterStatus.getAdapter()).update(mMemDetails);
    mBinding.setRouterStatus(response);
  }

 /* private String getRouterId() {
    return getActivity().getIntent().getStringExtra(Navigator.EXTRA_ROUTER_ID);
  }*/

  private String getRouterName() {
    return getActivity().getIntent().getStringExtra(Navigator.EXTRA_ROUTER_NAME);
  }

  private String getRouterModel() {
    return getActivity().getIntent().getStringExtra(Navigator.EXTRA_ROUTER_MODEL);
  }

  private String getRouterType() {
    return getActivity().getIntent().getStringExtra(Navigator.EXTRA_ROUTER_TYPE);
  }

  @Override
  public void onError(String message) {
    super.onError(message);

    if(getActivity() != null){
      getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
          mBinding.btnRouterStatusBlacklist.setVisibility(View.GONE);
          mBinding.rvRouterStatus.setVisibility(View.GONE);
          mBinding.tvRouterOutline.setVisibility(View.VISIBLE);
        }
      });
    }

    if(mRunnable != null){
      System.out.println("主页error onDestory");
      System.out.println("主页error 清除定时器");
      mHandler.removeCallbacks(mRunnable);
    }

  }

  @Override
  public void onPause() {
    System.out.println("主页 onPause");
    if(isEnterItem){
      //进入成员详情和黑名单不清楚定时器
      isEnterItem = false;
      System.out.println("主页1 不清除定时器");
    }else {
      System.out.println("主页1 清除定时器");
      if(mRunnable != null){
        mHandler.removeCallbacks(mRunnable);
      }
    }

    super.onPause();
  }

  @Override
  public void onResume() {
    System.out.println("主页 onResume");
    super.onResume();
  }

  @Override
  public void onDestroy() {
    System.out.println("主页 onDestory");
    System.out.println("主页1 清除定时器");
    if(mRunnable != null){
      mHandler.removeCallbacks(mRunnable);
    }

    super.onDestroy();
    /*mHandler.removeCallbacks(mRunnable);*/
  }
}