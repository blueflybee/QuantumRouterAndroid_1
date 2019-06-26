package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLockUserManageBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockUserModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.adapter.LockUserManageAdapter;
import com.qtec.mapp.model.rsp.DeleteLockUserResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetLockUsersResponse;
import com.qtec.mapp.model.rsp.GetUserRoleResponse;

import java.util.List;

import javax.inject.Inject;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * <pre>
 *      author: wusj
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/08
 *      desc: 门锁用户列表
 *      version: 1.0
 * </pre>
 */
public class LockUserManageActivity extends BaseActivity implements LockUserView {

  private ActivityLockUserManageBinding mBinding;

  @Inject
  LockUserPresenter mLockUserPresenter;
  private LockUserManageAdapter mAdapter;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_lock_user_manage);

    initializeInjector();
    initPresenter();

    initView();

    getLockUsers();

  }

  private void getLockUsers() {
    mLockUserPresenter.getLockUsers(getLock().getDeviceSerialNo());
  }

  private void initView() {
    initTitleBar("用户管理");
    initPullToRefreshListView();
  }

  private void initPullToRefreshListView() {
    mBinding.ptrLayout.setLastUpdateTimeRelateObject(this);
    mBinding.ptrLayout.setDurationToClose(1000);
    mBinding.ptrLayout.disableWhenHorizontalMove(true);//解决横向滑动冲突
    mBinding.ptrLayout.setPtrHandler(new PtrDefaultHandler2() {
      @Override
      public void onRefreshBegin(PtrFrameLayout frame) {
        getLockUsers();
      }

      @Override
      public void onLoadMoreBegin(PtrFrameLayout frame) {}

      @Override
      public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
        return super.checkCanDoLoadMore(frame, mBinding.lvUserList, footer);
      }

      @Override
      public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return super.checkCanDoRefresh(frame, mBinding.lvUserList, header);
      }
    });

    mAdapter = new LockUserManageAdapter(getContext());
    mBinding.lvUserList.setAdapter(mAdapter);
    mBinding.lvUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra(Navigator.EXTRA_LOCK_USER, mAdapter.getItem(position));
        intent.putExtra(Navigator.EXTRA_BLE_LOCK, getLock());
        mNavigator.navigateTo(getContext(), LockUserManageDetailActivity.class, intent);
      }
    });

  }

  private void initializeInjector() {
    DaggerLockComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .lockUserModule(new LockUserModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
    mLockUserPresenter.setView(this);
  }

  @Override
  public void showUserRole(GetUserRoleResponse response) {}

  @Override
  public void showLockUsers(List<GetLockUsersResponse> response) {
    mBinding.ptrLayout.refreshComplete();
    mAdapter.update(response);
  }

  @Override
  public void onDeleteLockUserSuccess(DeleteLockUserResponse response) {

  }

  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> getLock() {
    return (GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>) getIntent().getSerializableExtra(Navigator.EXTRA_BLE_LOCK);
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    getLockUsers();
  }
}