package com.fernandocejas.android10.sample.presentation.view.device.router.tools.childcare;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityChildCareBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter.ChildCareListAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.ChildCareBean;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.SpecialAttentionBean;
import com.qtec.router.model.rsp.ChildCareListResponse;
import com.qtec.router.model.rsp.RouterStatusResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 儿童关怀
 *      version: 1.0
 * </pre>
 */

public class ChildCareActivity extends BaseActivity implements AdapterView.OnItemClickListener,RouterDeviceListView,ChildCareListView{
  private ActivityChildCareBinding mBinding;
  private Boolean isWifiSwitch = false;
  private ChildCareListAdapter adapter;
  private List<ChildCareBean> childCareList;
  private List<SpecialAttentionBean> selectedList;
  private int mTopViewHeight =  0;
  private Boolean isGetInfoRequest = false; //get请求不成功才显示断网视图

  @Inject
  RouterDeviceListPresenter mRouterDeviceListPresenter;
  @Inject
  ChildCareListPresenter mChildCareListPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_child_care);
    initSpecialAttentionTitleBar("儿童关怀");

    initializeInjector();
    initPresenter();
    initView();

    mBinding.llChildcareOffline.setVisibility(View.GONE);
    mBinding.scrollViewChild.setVisibility(View.VISIBLE);
    queryRouterStatus();//查询设备列表

  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
    mRouterDeviceListPresenter.setView(this);
    mChildCareListPresenter.setView(this);
  }

  private void queryRouterStatus() {
    isGetInfoRequest = true;
    mRouterDeviceListPresenter.getRouterStatus(GlobleConstant.getgDeviceId());
  }

  private void queryChildCareList() {
    mChildCareListPresenter.getRouterStatus(GlobleConstant.getgDeviceId());
  }

  private void initView() {
    childCareList = new ArrayList<>();
  }


  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Intent intent = new Intent(this, ChildCareDetailActivity.class);
    intent.putExtra(Navigator.EXTRA_ROUTER_ID, GlobleConstant.getgDeviceId());
    intent.putExtra("ChildDetail",childCareList.get(position));
    startActivityForResult(intent,5);
  }

  @Override
  public void showRouterStatus(RouterStatusResponse<List<RouterStatusResponse.Status>> response) {
    childCareList.clear();

    for (int i = 0; i < response.getStalist().size(); i++) {
      ChildCareBean bean = new ChildCareBean();
      bean.setMacaddr(response.getStalist().get(i).getMacaddr());
      bean.setDevicetype(response.getStalist().get(i).getDevicetype());
      bean.setStaname(response.getStalist().get(i).getStaname());
      bean.setAdd(true);//默认是新增
      bean.setIsEnable(0);//默认关闭
      childCareList.add(bean);
    }

    if(childCareList.size() == 0){
      Toast.makeText(this, "暂无设备", Toast.LENGTH_SHORT).show();
      return;
    }

    queryChildCareList(); // 查询保护规则列表，确定是否是新增
  }

  @Override
  public void showChildCareList(List<ChildCareListResponse> response) {
    Boolean isHasOtherMac = true;
    for (int i = 0; i < response.size(); i++) {
      for (int j = 0; j < childCareList.size(); j++) {
        //先扫描一遍所有的mac是否包含此mac，如果不包含说明是新增的
        if(response.get(i).getMacaddr().equals(childCareList.get(j).getMacaddr())){
          childCareList.get(j).setAdd(false);//
          childCareList.get(j).setIsEnable(response.get(i).getEnabled());
          childCareList.get(j).setWeekdays(response.get(i).getWeekdays());
          childCareList.get(j).setStop_time(response.get(i).getStoptime());
          childCareList.get(j).setStart_time(response.get(i).getStarttime());
          isHasOtherMac = false;
          break;
        }
      }
      if(isHasOtherMac)
      {
        //可能返回不是列表下的mac
        ChildCareBean bean = new ChildCareBean();
        bean.setMacaddr(response.get(i).getMacaddr());
        bean.setDevicetype(3);//未知设备
        bean.setStaname("unknow");
        bean.setStart_time(response.get(i).getStarttime());
        bean.setAdd(false);//
        bean.setIsEnable(response.get(i).getEnabled());
        bean.setWeekdays(response.get(i).getWeekdays());
        bean.setStop_time(response.get(i).getStoptime());
        childCareList.add(bean);
      }
    }

    adapter = new ChildCareListAdapter(this, childCareList, R.layout.item_child_care);
    mBinding.lvChildCare.setAdapter(adapter);
    mBinding.lvChildCare.setOnItemClickListener(this);
  }

  @Override
  public void onError(String message) {

    if(!TextUtils.isEmpty(message)){
      super.onError(message);
    }

    if(isGetInfoRequest){
      isGetInfoRequest = false;

      mBinding.scrollViewChild.setVisibility(View.GONE);
      mBinding.llChildcareOffline.setVisibility(View.VISIBLE);
      mBinding.llChildcareClickOffline.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mBinding.llChildcareOffline.setVisibility(View.GONE);
          mBinding.scrollViewChild.setVisibility(View.VISIBLE);
          queryRouterStatus();//查询设备列表
        }
      });
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    queryRouterStatus();
  }
}
