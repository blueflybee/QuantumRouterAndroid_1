/*
 *
 *  * Copyright (C) 2014 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityConnectLockBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerDeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMineComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MineModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.deviceshare.GetSharedMemListPresenter;
import com.qtec.mapp.model.rsp.ConnectLockResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetLockListResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ConnectLockActivity extends BaseActivity implements GetLockListView, ConnectLockView {
  private ActivityConnectLockBinding mBinding;
  private ConnectLockListAdapter adapter;
  private int REQUEST_CONNECT_LOCK_CODE = 0x11;

  @Inject
  GetLockListPresenter mGetLockListPresenter;
  @Inject
  ConnectLockPresenter mConnectLockPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_connect_lock);

    initView();

    initializeInjector();

    initPresenter();

    mGetLockListPresenter.getLockList();
  }

  private void initializeInjector() {
    DaggerDeviceComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .deviceModule(new DeviceModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
    mGetLockListPresenter.setView(this);
    mConnectLockPresenter.setView(this);
  }

  private void initView() {
    initTitleBar("配对门锁");
  }

  @Override
  public void getLockList(List<GetLockListResponse> response) {

    adapter = new ConnectLockListAdapter(this, response, R.layout.item_connect_lock_list);
    mBinding.lvConnectLockList.setAdapter(adapter);

    adapter.setOnConnectLockClickListener(new ConnectLockListAdapter.OnConnectLockClickListener() {
      @Override
      public void onConnectLockClick(int position) {
        System.out.println("catEye: 猫眼和门锁配对中… activity");
        mConnectLockPresenter.connectLock(response.get(position).getDeviceSerialNo(),GlobleConstant.getgCatEyeId());
      }
    });
  }

  @Override
  public void connectLockSuccess(ConnectLockResponse response) {
    if("0".equals(getFlag())){
      //首次配置
      Intent intent = new Intent(this,MainActivity.class);
      startActivity(intent);
    }else {
      DialogUtil.showConectLockDialog(getContext(), "配对成功", () -> {
        this.setResult(REQUEST_CONNECT_LOCK_CODE);
        finish();
      });
    }

  }

  @Override
  public void connectLockFailed() {
    Toast.makeText(this, "配对失败", Toast.LENGTH_SHORT).show();
  }

  private String getFlag(){
    return getIntent().getStringExtra("IS_FIRST_CONFIG");// 0 首次配置
  }
}
