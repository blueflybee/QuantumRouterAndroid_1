package com.fernandocejas.android10.sample.presentation.view.device.router.tools.specialattention;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySpecialAttentionBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter.AttentionedListAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.SpecialAttentionBean;
import com.qtec.router.model.req.PostSpecialAttentionRequest;
import com.qtec.router.model.rsp.GetSpecialAttentionResponse;
import com.qtec.router.model.rsp.PostSpecialAttentionResponse;
import com.qtec.router.model.rsp.RouterStatusResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 特别关注
 *      version: 1.0
 * </pre>
 */

public class SpecialAttentionActivity extends BaseActivity implements View.OnClickListener, RouterDeviceListView, GetSpecialAttentionView, PostSpecialAttentionView {
  private ActivitySpecialAttentionBinding mBinding;
  private AttentionedListAdapter adapter;
  private final int REQUEST_ADD_ATTENTION = 2;
  private List<SpecialAttentionBean> mAttentionList;
  private RemovePopupWindow mPopWin;
  RouterStatusResponse<List<RouterStatusResponse.Status>> mDeviceList;
  private Boolean isSaveBtnClick  = false;
  private Boolean isGetInfoRequest = false; //get请求不成功才显示断网视图

  //IGetAgreementView
  @Inject
  GetSpecialAttentionPresenter mGetAttentionPresenter;
  @Inject
  RouterDeviceListPresenter mRouterDeviceListPresenter;
  @Inject
  PostSpecialAttentionPresenter mPostSpecialAttentionPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_special_attention);
    initSpecialAttentionTitleBar("特别关注");

    initializeInjector();
    initPresenter();
    initData();

    mBinding.scrollView.setVisibility(View.VISIBLE);
    mBinding.llSpecialAttention.setVisibility(View.GONE);
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
    mGetAttentionPresenter.setView(this);
    mRouterDeviceListPresenter.setView(this);
    mPostSpecialAttentionPresenter.setView(this);
  }

  private void queryAttentionlist() {
    mGetAttentionPresenter.getSpecialAttention(GlobleConstant.getgDeviceId());
  }

  private void postSpecialAttention(String msg) {
    PostSpecialAttentionRequest bean = new PostSpecialAttentionRequest();
    bean.setSpecialcare(msg);

    mPostSpecialAttentionPresenter.postSpecialAttention(GlobleConstant.getgDeviceId(), bean);
  }

  private void initData() {
    mAttentionList = new ArrayList<>();
    mBinding.rlAddSpecialAttention.setOnClickListener(this);

    /*initEvent();*/
  }

  private void initEvent() {
    mBinding.lvSpecialAttention.setVisibility(View.VISIBLE);

    adapter = new AttentionedListAdapter(this, mAttentionList, R.layout.item_attentioned);
    mBinding.lvSpecialAttention.setAdapter(adapter);

    adapter.setOnDetailClickListener(new AttentionedListAdapter.OnRemoveClickListener() {
      @Override
      public void onRemoveClick(int position) {
        mPopWin = new RemovePopupWindow(SpecialAttentionActivity.this);
        mPopWin.setFocusable(true);
        mPopWin.showAtLocation(mPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);
        mPopWin.setOnPositiveClickListener(new RemovePopupWindow.OnPositiveClickListener() {
          @Override
          public void onPositiveClick() {

            //移除选中的，把其他的信息提交上去
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < mAttentionList.size(); i++) {
              if (i != position) {
                buffer.append(mAttentionList.get(i).getMac()).append("|").append(mAttentionList.get(i).getMode()).append(" ");
              }
            }

            if (buffer.length() > 0) {
              buffer.deleteCharAt(buffer.length() - 1);//去除最后一个空格
              postSpecialAttention(buffer.toString());
            }else{
              postSpecialAttention(buffer.toString());
            }

          }
        });
      }
    });

    adapter.setOnLineClickListener(new AttentionedListAdapter.OnOnLineClickListener() {
      @Override
      public void onOnlineClick(int position, Boolean isOnline) {
        if(isOnline){
          mAttentionList.get(position).setOnLine(true);
        }else{
          mAttentionList.get(position).setOnLine(false);
        }
      }
    });

    adapter.setOnOffLineClickListener(new AttentionedListAdapter.OnOffLineClickListener() {
      @Override
      public void onOffLineClick(int position, Boolean isOffLine) {
        if(isOffLine){
          mAttentionList.get(position).setOffLine(true);
        }else{
          mAttentionList.get(position).setOffLine(false);
        }
      }
    });

    mBinding.btnFinishAttention.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        StringBuffer buffer = new StringBuffer();

        isSaveBtnClick = true;

        for (int i = 0; i < mAttentionList.size(); i++) {
          if(mAttentionList.get(i).getOnLine()){
            if(mAttentionList.get(i).getOffLine()){
              mAttentionList.get(i).setMode("3");
            }else{
              mAttentionList.get(i).setMode("1");
            }
          }else{
            if(mAttentionList.get(i).getOffLine()){
              mAttentionList.get(i).setMode("2");
            }else{
              mAttentionList.get(i).setMode("0");
            }
          }

          buffer.append(mAttentionList.get(i).getMac()).append("|").append(mAttentionList.get(i).getMode()).append(" ");
        }

        if (buffer.length() > 0) {
          buffer.deleteCharAt(buffer.length() - 1);//去除最后一个空格
          postSpecialAttention(buffer.toString());
        }

      }
    });

    mTitleBar.setRightAs(R.drawable.ic_add_white_20dp, new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //   startActivityForResult(new Intent(SpecialAttentionActivity.this, AddAttentionActivity.class), REQUEST_ADD_ATTENTION);
        Intent intent = new Intent(SpecialAttentionActivity.this, AddAttentionActivity.class);
        intent.putExtra(Navigator.EXTRA_ROUTER_ID, GlobleConstant.getgDeviceId());
        intent.putExtra("AleadyAttentioned", (Serializable) mAttentionList);
        startActivityForResult(intent, REQUEST_ADD_ATTENTION);
      }
    });

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.rl_add_special_attention:
        Intent intent = new Intent(this, AddAttentionActivity.class);
        intent.putExtra(Navigator.EXTRA_ROUTER_ID, GlobleConstant.getgDeviceId());
        intent.putExtra("AleadyAttentioned", (Serializable) mAttentionList);

        startActivityForResult(intent, REQUEST_ADD_ATTENTION);
        break;

      default:
        break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == REQUEST_ADD_ATTENTION) {
      /*queryAttentionlist();*/
      mAttentionList.clear();
      queryRouterStatus();
      initEvent();
    }
  }

  private void queryRouterStatus() {
    isGetInfoRequest = true;
    mRouterDeviceListPresenter.getRouterStatus(GlobleConstant.getgDeviceId());
  }

  @Override
  public void getSpecialAttention(GetSpecialAttentionResponse response) {
    mAttentionList.clear();

    if (!TextUtils.isEmpty(response.getSpecialcare())) {
      String[] infos = response.getSpecialcare().split(" ");

      for (int i = 0; i < infos.length; i++) {
        SpecialAttentionBean bean = new SpecialAttentionBean();
        String[] info = infos[i].split("\\|");
        bean.setMac(info[0]);
        bean.setMode(info[1]);

        convertModeToFlag(bean,info[1]);

        for (int j = 0; j < mDeviceList.getStalist().size(); j++) {
          if ((bean.getMac()).equals(mDeviceList.getStalist().get(j).getMacaddr())) {
            bean.setType(mDeviceList.getStalist().get(j).getDevicetype());
            bean.setName(mDeviceList.getStalist().get(j).getStaname());
            break;
          }
        }
        mAttentionList.add(bean);
      }

      mBinding.btnFinishAttention.setVisibility(View.VISIBLE);
      mBinding.tvAttentionDecrip.setVisibility(View.VISIBLE);
      mBinding.rlAddSpecialAttention.setVisibility(View.GONE);
      initEvent();
    } else {
      mBinding.rlAddSpecialAttention.setVisibility(View.VISIBLE);
      mBinding.btnFinishAttention.setVisibility(View.GONE);
      mTitleBar.getRightBtn().setVisibility(View.GONE);
      mBinding.tvAttentionDecrip.setVisibility(View.GONE);

      if(adapter != null){
        adapter.notifyDataSetChanged();
      }

    }

  }

  @Override
  public void onError(String message) {
    if(!TextUtils.isEmpty(message)){
      super.onError(message);
    }

    if(isGetInfoRequest){
      isGetInfoRequest = false;

      mBinding.scrollView.setVisibility(View.GONE);
      mBinding.llSpecialAttention.setVisibility(View.VISIBLE);
      findViewById(R.id.ll_offline).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mBinding.scrollView.setVisibility(View.VISIBLE);
          mBinding.llSpecialAttention.setVisibility(View.GONE);
          queryRouterStatus();//查询设备列表
        }
      });
    }
  }

  /**
  * 将mode转换成上下线的标志
  *
  * @param
  * @return
  */
  private void convertModeToFlag(SpecialAttentionBean bean, String s) {
    if("0".equals(s)){
      bean.setOffLine(false);
      bean.setOnLine(false);
    }else if("1".equals(s)){
      bean.setOffLine(false);
      bean.setOnLine(true);
    }else if("2".equals(s)){
      bean.setOffLine(true);
      bean.setOnLine(false);
    }else if("3".equals(s)){
      bean.setOffLine(true);
      bean.setOnLine(true);
    }

  }

  @Override
  public void showRouterStatus(RouterStatusResponse<List<RouterStatusResponse.Status>> response) {
    mDeviceList = response;
    queryAttentionlist();
  }

  @Override
  public void postSpecialAttention(PostSpecialAttentionResponse response) {
    if(isSaveBtnClick){
      isSaveBtnClick = false;
      Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    }else {
      Toast.makeText(this, "移除成功", Toast.LENGTH_SHORT).show();
    }

    System.out.println("关注adapter = " + adapter);
    queryAttentionlist();

  }
}
