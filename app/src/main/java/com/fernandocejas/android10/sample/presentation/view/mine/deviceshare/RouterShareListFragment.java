package com.fernandocejas.android10.sample.presentation.view.mine.deviceshare;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentRouterListBinding;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentWaitingAuthBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.RouterComponent;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.device.router.addrouter.AddRouterActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter.WaitAuthListAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet.GetWaitingAuthDeviceListPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet.IGetAuthDeviceListView;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet.WaitAuthDetailActivity;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.router.model.rsp.GetWaitingAuthDeviceListResponse;

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
public class RouterShareListFragment extends BaseFragment implements AdapterView.OnItemClickListener {
  private FragmentRouterListBinding mBinding;
  private RouterShareListAdapter mRouterShareListAdapter;
  private List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> mRouterList;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public static RouterShareListFragment newInstance() {
    RouterShareListFragment fragment = new RouterShareListFragment();
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_router_list, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    initView();
  }

  private void initView() {
    // 获取网关列表
    mRouterList = new ArrayList<>();

    // 先分出网关和门锁来
    for (int i = 0; i < getDevTree().size() ; i++) {
      if((AppConstant.DEVICE_TYPE_ROUTER).equals(getDevTree().get(i).getDeviceType())){
        //网关
        mRouterList.add(getDevTree().get(i));
      }
    }

    if(mRouterList.size() == 0){
      //空视图
      mBinding.lvRouterList.setEmptyView(getActivity().findViewById(R.id.include_router_list_empty_view));
      getActivity().findViewById(R.id.include_router_list_empty_view).findViewById(R.id.btn_empty_add_router).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(getActivity(), AddRouterActivity.class);
          startActivity(intent);
        }
      });
      return;
    }

    mRouterShareListAdapter = new RouterShareListAdapter(getActivity(),mRouterList,R.layout.item_share_router_list);
    mBinding.lvRouterList.setAdapter(mRouterShareListAdapter);

    mBinding.lvRouterList.setOnItemClickListener(this);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Intent intent = new Intent(getActivity(),ShareMemberListActivity.class);
    intent.putExtra(mNavigator.EXTR_ROUTER_SERIAL_NUM,mRouterList.get(position).getDeviceSerialNo());
    startActivity(intent);
  }

  private List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> getDevTree() {
    List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> devTreeResponses = (List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>>)
        getActivity().getIntent().getSerializableExtra(mNavigator.EXTR_ROUTER_LIST);
    return devTreeResponses;
  }

}