package com.fernandocejas.android10.sample.presentation.view.device.router.tools.specialattention;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAddAttentionBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter.AddAttentionListAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.SpecialAttentionBean;
import com.qtec.router.model.req.PostSpecialAttentionRequest;
import com.qtec.router.model.rsp.PostSpecialAttentionResponse;
import com.qtec.router.model.rsp.RouterStatusResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 添加关注
 *      version: 1.0
 * </pre>
 */

public class AddAttentionActivity extends BaseActivity implements RouterDeviceListView,PostSpecialAttentionView {
  private ActivityAddAttentionBinding mBinding;
  private Boolean isWifiSwitch = false;
  private AddAttentionListAdapter adapter;
  private List<RouterStatusResponse.Status> attentionList;
  private List<RouterStatusResponse.Status> selectedList;

  @Inject
  RouterDeviceListPresenter mRouterDeviceListPresenter;
  @Inject
  PostSpecialAttentionPresenter mPostSpecialAttentionPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_attention);
    initTitleBar("添加关注");

    initializeInjector();
    initPresenter();
    initView();
    queryRouterStatus();//查询设备列表

  }

  private void queryRouterStatus() {
    mRouterDeviceListPresenter.getRouterStatus(GlobleConstant.getgDeviceId());
  }

  private void postSpecialAttention(String msg) {
    PostSpecialAttentionRequest bean = new PostSpecialAttentionRequest();
    bean.setSpecialcare(msg);
    
    mPostSpecialAttentionPresenter.postSpecialAttention(GlobleConstant.getgDeviceId(),bean);
  }

  private List<SpecialAttentionBean> getAleadyAttentioned(){
    return (List<SpecialAttentionBean>)getIntent().getSerializableExtra("AleadyAttentioned");
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
    mPostSpecialAttentionPresenter.setView(this);
  }

  private void initView() {
    attentionList = new ArrayList<>();
    selectedList = new ArrayList<>();
  }

  @Override
  public void showRouterStatus(RouterStatusResponse<List<RouterStatusResponse.Status>> response) {
    attentionList.clear();

    if(response.getStalist().size() > 0){
      attentionList.addAll(response.getStalist());
      //已经添加过的设备不再显示在里面
      if(getAleadyAttentioned().size()>0){
        for (int i = 0; i < getAleadyAttentioned().size(); i++) {
          for (int j = 0; j < attentionList.size(); j++) {
            if((getAleadyAttentioned().get(i).getMac()).equals(attentionList.get(j).getMacaddr())){
             attentionList.remove(j);
              break;
            }
          }
        }
      }

      if(attentionList.size() > 0){
        mBinding.lvAddAttention.setVisibility(View.VISIBLE);
        mBinding.tvNoAttention.setVisibility(View.GONE);

        adapter = new AddAttentionListAdapter(this, attentionList, R.layout.item_add_attention);
        mBinding.lvAddAttention.setAdapter(adapter);

        mTitleBar.setRightAs("确定", new View.OnClickListener() {
          @Override
          public void onClick(View v) {

            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < getAleadyAttentioned().size(); i++) {
              buffer.append(""+getAleadyAttentioned().get(i).getMac()+"|"+getAleadyAttentioned().get(i).getMode()+" ");
            }

            for (int i = 0; i < adapter.getCount(); i++) {
              if (adapter.checkedMap.get(i)) {
                selectedList.add(attentionList.get(i));
                buffer.append(""+attentionList.get(i).getMacaddr()+"|0"+" ");
              }
            }
            if(buffer.length() > 0){
              buffer.deleteCharAt(buffer.length()-1);//去掉最后一个空格
              postSpecialAttention(buffer.toString());
            }

          }
        });

      }else {
        mBinding.lvAddAttention.setVisibility(View.GONE);
        mBinding.tvNoAttention.setVisibility(View.VISIBLE);

      }
    }

  }

  @Override
  public void postSpecialAttention(PostSpecialAttentionResponse response) {
    Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
    this.setResult(2);
    finish();
  }
}
