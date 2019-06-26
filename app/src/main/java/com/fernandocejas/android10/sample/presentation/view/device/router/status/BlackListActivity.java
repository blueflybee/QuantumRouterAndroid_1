package com.fernandocejas.android10.sample.presentation.view.device.router.status;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityBlackListBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.childcare.RouterDeviceListPresenter;
import com.qtec.router.model.req.RemoveBlackMemRequest;
import com.qtec.router.model.rsp.GetBlackListResponse;
import com.qtec.router.model.rsp.RemoveBlackMemResponse;
import com.qtec.router.model.rsp.RouterStatusResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 黑名单管理
 *      version: 1.0
 * </pre>
 */

public class BlackListActivity extends BaseActivity implements GetBlackListView, DeviceListView, RemoveBlackMemView {
  private ActivityBlackListBinding mBinding;
  private BlackListAdapter adapter;
  private List<GetBlackListResponse> mBlackLists;
  private int mRemovePosition = 0;
  private RouterStatusResponse<List<RouterStatusResponse.Status>> mRouterStatus;

  @Inject
  GetBlackListPresenter mGetBlackListPresenter;
  @Inject
  DeviceListPresenter mDeviceListPresenter;
  @Inject
  RemoveBlackMemPresenter mRemoveMemPresenter;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_black_list);
    initTitleBar("黑名单");
    initView();
    initializeInjector();
    initPresenter();

    //queryRouterStatus();

    queryBlackList();
  }

  private void queryBlackList() {
    mGetBlackListPresenter.getBlackList(GlobleConstant.getgDeviceId());
  }

  private void queryRouterStatus() {
    mDeviceListPresenter.getRouterStatus(GlobleConstant.getgDeviceId());
  }

  private void deleteMemRequest(RemoveBlackMemRequest bean) {
    mRemoveMemPresenter.deleteMem(GlobleConstant.getgDeviceId(), bean);
  }

  private void initView() {
    mBlackLists = new ArrayList<>();
  }

  /**
   * 获取黑名单
   *
   * @param
   * @return
   */
  @Override
  public void getBlackList(List<GetBlackListResponse> response) {
    if (response.size() > 0) {
  /*    for (int i = 0; i < response.size(); i++) {
        BlackListBean bean = new BlackListBean();
        bean.setMac(response.get(i).getMacaddr());
        bean.setEnable(response.get(i).getEnabled());
        //获取Type和Name
        for (int j = 0; j < mRouterStatus.getStalist().size(); j++) {
          if((response.get(i).getMacaddr()).equals(mRouterStatus.getStalist().get(j).getMacaddr())){
            bean.setType(mRouterStatus.getStalist().get(j).getDevicetype());
            bean.setName(mRouterStatus.getStalist().get(j).getStaname());
            break;
          }
        }

        mBlackLists.add(bean);
      }*/
      mBlackLists.clear();
      mBlackLists.addAll(response);

      adapter = new BlackListAdapter(this, mBlackLists, R.layout.item_black_list);
      mBinding.lvBlackList.setAdapter(adapter);

      adapter.setOnRemoveBlackMem(new BlackListAdapter.OnRemoveBlackMem() {
        @Override
        public void removeBlackMem(int position) {
          //移除操作
          mRemovePosition = position;
          RemoveBlackMemRequest bean = new RemoveBlackMemRequest();
          bean.setEnabled(mBlackLists.get(position).getEnabled());
          bean.setMacaddr(mBlackLists.get(position).getMacaddr());
          bean.setName(mBlackLists.get(position).getName());

          deleteMemRequest(bean);
        }
      });
    } else {
      //空视图
      mBinding.lvBlackList.setEmptyView(findViewById(R.id.include_empty_black_list));
    }
  }

  private void initPresenter() {
    mGetBlackListPresenter.setView(this);
    mDeviceListPresenter.setView(this);
    mRemoveMemPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  @Override
  public void showRouterStatus(RouterStatusResponse<List<RouterStatusResponse.Status>> response) {
 /*   mRouterStatus = response;

    queryBlackList();*/

  }

  @Override
  public void removeBlackMem(RemoveBlackMemResponse response) {
    Toast.makeText(this, "移除成功", Toast.LENGTH_SHORT).show();
    mBlackLists.remove(mRemovePosition);
    adapter.notifyDataSetChanged();
  }
}

