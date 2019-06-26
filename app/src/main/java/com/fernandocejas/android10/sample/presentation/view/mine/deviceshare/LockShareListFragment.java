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
import com.fernandocejas.android10.sample.presentation.databinding.FragmentLockListBinding;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.TouchLockActivity;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.0
 * </pre>
 */
public class LockShareListFragment extends BaseFragment implements AdapterView.OnItemClickListener {
  private FragmentLockListBinding mBinding;

  private RouterShareListAdapter mLockListAdapter;
  private List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> mLockList;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public static LockShareListFragment newInstance() {
    LockShareListFragment fragment = new LockShareListFragment();
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_lock_list, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    initView();
  }

  private void initView() {
    // 获取网关列表
    mLockList = new ArrayList<>();

    // 先分出网关和门锁来
    for (int i = 0; i < getDevTree().size() ; i++) {
      if((AppConstant.DEVICE_TYPE_LOCK).equals(getDevTree().get(i).getDeviceType())){
        //门锁
        mLockList.add(getDevTree().get(i));
      }
    }

    if(mLockList.size() == 0){
      //空视图
      mBinding.lvLockList.setEmptyView(getActivity().findViewById(R.id.include_lock_list_empty_view));
      getActivity().findViewById(R.id.include_lock_list_empty_view).findViewById(R.id.btn_empty_add_lock).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(getActivity(), TouchLockActivity.class);
          startActivity(intent);
        }
      });
      return;
    }

    mLockListAdapter = new RouterShareListAdapter(getActivity(),mLockList,R.layout.item_share_lock_list);
    mBinding.lvLockList.setAdapter(mLockListAdapter);

    mBinding.lvLockList.setOnItemClickListener(this);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Intent intent = new Intent(getActivity(),ShareMemberListActivity.class);
    intent.putExtra(mNavigator.EXTR_ROUTER_SERIAL_NUM,mLockList.get(position).getDeviceSerialNo());
    startActivity(intent);
  }

  private List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> getDevTree() {
    List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> devTreeResponses = (List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>>)
        getActivity().getIntent().getSerializableExtra(mNavigator.EXTR_ROUTER_LIST);
    return devTreeResponses;
  }

}