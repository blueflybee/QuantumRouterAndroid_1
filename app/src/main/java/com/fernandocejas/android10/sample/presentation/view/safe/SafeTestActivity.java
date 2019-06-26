package com.fernandocejas.android10.sample.presentation.view.safe;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.blueflybee.keystore.KeystoreRepertory;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySafeTestBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.router.model.rsp.PostInspectionResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 安全检测
 *      version: 1.0
 * </pre>
 */

public class SafeTestActivity extends BaseActivity implements View.OnClickListener, PostSafeTestView, ExpandableListView.OnGroupClickListener, ExpandableListView.OnGroupExpandListener {
  private ActivitySafeTestBinding mBinding;
  private SafeTestAdapter adapter;
  private Boolean isTesting = false,isTestDone = false;
  private List<DeviceEntity<List<DeviceEntity.ChildEntity>>> deviceLists;
  private String[] lockContents = {"量子密钥通道",
      "蓝牙网络加密连接",
      "ZigBee网络加密连接"};

  private String[] routerContents = {"Ddos服务",
      "Telnet协议",
      "FTP协议",
      "Samba协议",
      "虚拟服务",
      "DMZ主机",
      "UPNP开关",};

  private int DELAY = 500;//延时基数
  private int mRequestIndex = 0;

  @Inject
  PostSafeTestPresenter mSafeTestPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_safe_test);
    initSpecialAttentionTitleBar("安全体检");
    initializeInjector();
    initPresenter();
    initData();

    //没有密钥的不体检
    safeTestRequest(deviceLists.get(mRequestIndex).getKey());

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
    mSafeTestPresenter.setView(this);
  }

  private void initData() {
    deviceLists = new ArrayList<>();

    isTesting = true;

    mBinding.btnSafeTest.setOnClickListener(this);

    mBinding.lvSafeTest.setOnGroupClickListener(this);
    mBinding.lvSafeTest.setOnGroupExpandListener(this);

    for (int i = 0; i < getDevTree().size(); i++) {
      if ((AppConstant.DEVICE_TYPE_ROUTER).equals(getDevTree().get(i).getDeviceType())) {
        DeviceEntity<List<DeviceEntity.ChildEntity>> entity = new DeviceEntity<>();

        List<DeviceEntity.ChildEntity> childBeanList = new ArrayList<>();
        childBeanList.clear();

  /*      entity.setType(0);
        for (int j = 0; j < 3; j++) {

          DeviceEntity.ChildEntity childBean = new DeviceEntity.ChildEntity();
          childBean.setTestState(0);//不可见
          childBean.setContent(lockContents[j]);
          childBean.setOpened(false);
          childBeanList.add(childBean);
        }*/

        entity.setType(1);
        for (int j = 0; j < routerContents.length; j++) {
          DeviceEntity.ChildEntity childBean = new DeviceEntity.ChildEntity();
          childBean.setTestState(0);//不可见
          childBean.setContent(routerContents[j]);
          childBean.setOpened(false);
          childBeanList.add(childBean);
        }

        entity.setChilBeans(childBeanList);
        entity.setName(getDevTree().get(i).getDeviceName());
        entity.setKey(getDevTree().get(i).getDeviceSerialNo());

        if (KeystoreRepertory.getInstance().has(getDevTree().get(i).getDeviceSerialNo() + "_" + PrefConstant.getUserUniqueKey(getDevTree().get(i).getDeviceType()))){
          entity.setHasKey(true); //载入了密钥
        }else {
          //没有密钥的情况
          entity.setHasKey(false); //载入了密钥
        }

        if (i == 0) {
          entity.setItemExpanded(true);//第一个默认展开
          entity.setState(1);
          entity.getChilBeans().get(0).setTestState(1);//第一项的第一子项显示正在体检
        } else {
          entity.setItemExpanded(false);//其他都是未展开
          entity.setState(0);//默认不在检测
        }
        deviceLists.add(entity);
      }

    }

    adapter = new SafeTestAdapter(this, deviceLists);
    mBinding.lvSafeTest.setAdapter(adapter);

    //不延时的话ui来不及更新
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        mBinding.lvSafeTest.expandGroup(0, true);
      }
    }, 600);
  }

  /**
   * 更新网关检测状态
   *
   */
  private void updateRouterViewState(int groupPosition, PostInspectionResponse response) {
    deviceLists.get(groupPosition).getChilBeans().get(0).setTestState(2);//完成

    mBinding.tvSafeTestPassCount.setText("已完成" + "1项体检");
    deviceLists.get(groupPosition).getChilBeans().get(1).setTestState(1);//正在检测
    mBinding.tvSafeTestingTitle.setText("正在进行: " + routerContents[1]);
    adapter.notifyDataSetChanged();

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (response.getDdos() == 0) {
          deviceLists.get(groupPosition).getChilBeans().get(0).setOpened(false);
        } else {
          deviceLists.get(groupPosition).getChilBeans().get(0).setOpened(true); //开启
        }

        mBinding.tvSafeTestPassCount.setText("已完成" + "2项体检");

        deviceLists.get(groupPosition).getChilBeans().get(1).setTestState(2);//完成
        deviceLists.get(groupPosition).getChilBeans().get(2).setTestState(1);//正在检测
        mBinding.tvSafeTestingTitle.setText("正在进行: " + routerContents[2]);
        /*itemAdapter.notifyDataSetChanged();*/
        adapter.notifyDataSetChanged();
      }
    }, DELAY);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (response.getTelent() == 0) {
          deviceLists.get(groupPosition).getChilBeans().get(1).setOpened(false);
        } else {
          deviceLists.get(groupPosition).getChilBeans().get(1).setOpened(true);
        }

        mBinding.tvSafeTestPassCount.setText("已完成" + "3项体检");
        deviceLists.get(groupPosition).getChilBeans().get(2).setTestState(2);//完成
        deviceLists.get(groupPosition).getChilBeans().get(3).setTestState(1);//正在检测
        mBinding.tvSafeTestingTitle.setText("正在进行: " + routerContents[3]);
        /*itemAdapter.notifyDataSetChanged();*/
        adapter.notifyDataSetChanged();
      }
    }, (DELAY + 500));

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (response.getFtp() == 0) {
          deviceLists.get(groupPosition).getChilBeans().get(2).setOpened(false);
        } else {
          deviceLists.get(groupPosition).getChilBeans().get(2).setOpened(true);
        }
        mBinding.tvSafeTestPassCount.setText("已完成" + "4项体检");
        deviceLists.get(groupPosition).getChilBeans().get(3).setTestState(2);//完成
        deviceLists.get(groupPosition).getChilBeans().get(4).setTestState(1);//正在检测
        mBinding.tvSafeTestingTitle.setText("正在进行: " + routerContents[4]);
        /*itemAdapter.notifyDataSetChanged();*/
        adapter.notifyDataSetChanged();
      }
    }, (DELAY + 1000));

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (response.getSamba() == 0) {
          deviceLists.get(groupPosition).getChilBeans().get(3).setOpened(false);
        } else {
          deviceLists.get(groupPosition).getChilBeans().get(3).setOpened(true);
        }
        mBinding.tvSafeTestPassCount.setText("已完成" + "5项体检");
        deviceLists.get(groupPosition).getChilBeans().get(4).setTestState(2);//完成
        deviceLists.get(groupPosition).getChilBeans().get(5).setTestState(1);//正在检测
        mBinding.tvSafeTestingTitle.setText("正在进行: " + routerContents[5]);
        /*itemAdapter.notifyDataSetChanged();*/
        adapter.notifyDataSetChanged();
      }
    }, (DELAY + 1500));


    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (response.getVirtualservice() == 0) {
          deviceLists.get(groupPosition).getChilBeans().get(4).setOpened(false);
        } else {
          deviceLists.get(groupPosition).getChilBeans().get(4).setOpened(true);
        }
        mBinding.tvSafeTestPassCount.setText("已完成" + "6项体检");
        deviceLists.get(groupPosition).getChilBeans().get(5).setTestState(2);//完成
        deviceLists.get(groupPosition).getChilBeans().get(6).setTestState(1);//正在检测
        mBinding.tvSafeTestingTitle.setText("正在进行: " + routerContents[6]);
        /*itemAdapter.notifyDataSetChanged();*/
        adapter.notifyDataSetChanged();
      }
    }, (DELAY + 2000));

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (response.getDmz() == 0) {
          deviceLists.get(groupPosition).getChilBeans().get(5).setOpened(false);
        } else {
          deviceLists.get(groupPosition).getChilBeans().get(5).setOpened(true);
        }
        mBinding.tvSafeTestPassCount.setText("已完成" + "7项体检");
        deviceLists.get(groupPosition).getChilBeans().get(6).setTestState(2);//完成
        mBinding.lvSafeTest.collapseGroup(groupPosition);
        deviceLists.get(groupPosition).setState(2);
        if((groupPosition+1) < deviceLists.size()){
          deviceLists.get(groupPosition + 1).setState(1);//下一项正在检测
        }
      }
    }, (DELAY + 2500));

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if((groupPosition+1) < deviceLists.size()){
          mBinding.lvSafeTest.expandGroup(1 + groupPosition);
          adapter.notifyDataSetChanged();
          mRequestIndex++;
          if(mRequestIndex < deviceLists.size()){
            safeTestRequest(deviceLists.get(mRequestIndex).getKey());
          }
        }else {
          mBinding.tvSafeTestingTitle.setText("您的设备非常安全");
          mBinding.tvSafeTestPassCount.setText("已完成全部安全检查");
          mBinding.btnSafeTest.setText("体检完成");
          isTestDone = true;
          mBinding.imgSafeHead.setBackground(getResources().getDrawable(R.drawable.test_done));
          mBinding.imgSafeHead.clearAnimation();
          isTesting = false;
        }
      }
    }, (DELAY + 2800));

  }

  /**
   * 更新门锁体检状态
   *
   * @param
   * @return
   */
  private void updateLockViewState(int groupPosition) {
    mBinding.tvSafeTestPassCount.setText("已完成" + (1 + 7 + groupPosition * 11) + "项体检");

    deviceLists.get(1).getChilBeans().get(0).setTestState(1);//正在检测
    mBinding.tvSafeTestingTitle.setText("正在进行:" + lockContents[0]);
    adapter.notifyDataSetChanged();
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        deviceLists.get(groupPosition).getChilBeans().get(0).setOpened(false);

        mBinding.tvSafeTestPassCount.setText("已完成" + (2 + 7 + groupPosition * 11) + "项体检");
        deviceLists.get(groupPosition).getChilBeans().get(0).setTestState(2);//完成
        deviceLists.get(groupPosition).getChilBeans().get(1).setTestState(1);//正在检测
        mBinding.tvSafeTestingTitle.setText("正在进行:" + lockContents[1]);
        /*itemAdapter.notifyDataSetChanged();*/
        adapter.notifyDataSetChanged();
      }
    }, (DELAY + 3000) * (groupPosition + 1));

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        deviceLists.get(groupPosition).getChilBeans().get(1).setOpened(false);

        mBinding.tvSafeTestPassCount.setText("已完成" + (3 + 7 + groupPosition * 11) + "项体检");
        deviceLists.get(groupPosition).getChilBeans().get(1).setTestState(2);//完成
        deviceLists.get(groupPosition).getChilBeans().get(2).setTestState(1);//正在检测
        mBinding.tvSafeTestingTitle.setText("正在进行:" + lockContents[2]);
        /*itemAdapter.notifyDataSetChanged();*/
        adapter.notifyDataSetChanged();
      }
    }, (DELAY + 3200) * (groupPosition + 1));

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        deviceLists.get(groupPosition).getChilBeans().get(2).setOpened(false);

        mBinding.tvSafeTestPassCount.setText("已完成" + (4 + 7 + groupPosition * 11) + "项体检");
        deviceLists.get(groupPosition).getChilBeans().get(2).setTestState(2);//完成
        /*itemAdapter.notifyDataSetChanged();*/
        mBinding.lvSafeTest.collapseGroup(groupPosition + 1);
        deviceLists.get(groupPosition).setState(2);//检测完成
        deviceLists.get(groupPosition + 1).setState(1);//下一项正在检测
        mBinding.btnSafeTest.setText("重新体检");
      }
    }, (DELAY + 3500) * (groupPosition + 1));

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        mBinding.lvSafeTest.expandGroup(groupPosition + 2);
        adapter.notifyDataSetChanged();
      }
    }, DELAY);
  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_safe_test:
        if (isTesting) {
          finish();
        } else {
          if(isTestDone){
            finish();
          }else {
            safeTestRequest(deviceLists.get(0).getKey());
          }

        }
        //取消体检
        break;

      default:
        break;
    }
  }

  private void safeTestRequest(String key) {
    if (deviceLists.size() == 0 || mRequestIndex == deviceLists.size()) {
      return;
    }

    setImgAnimation(mBinding.imgSafeHead);

    if(!deviceLists.get(mRequestIndex).getHasKey()){
      // 没有有密钥 //0：等待检测 1：检测中 2：检测完成 3:设备没有密钥
      deviceLists.get(mRequestIndex).setState(3);
      System.out.println("mRequestIndex pre = " + mRequestIndex);
      //打开第二项，关闭第一项
      //updateRouterViewState();
      mBinding.lvSafeTest.collapseGroup(mRequestIndex);

      mRequestIndex++;

      if(mRequestIndex < deviceLists.size()){
        System.out.println("mRequestIndex now= " + mRequestIndex);
        mBinding.lvSafeTest.expandGroup(mRequestIndex);

        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            safeTestRequest(deviceLists.get(mRequestIndex).getKey());
          }
        },1500);

      }else{
        mBinding.tvSafeTestingTitle.setText("您的设备非常安全");
        mBinding.tvSafeTestPassCount.setText("已完成全部安全检查");
        mBinding.btnSafeTest.setText("体检完成");
        isTestDone = true;
        mBinding.imgSafeHead.setBackground(getResources().getDrawable(R.drawable.test_done));
        mBinding.imgSafeHead.clearAnimation();
        isTesting = false;
        System.out.println("mRequestIndex finish= " + mRequestIndex);
        //所有都展开
        for (int i = 0; i < deviceLists.size(); i++) {
          mBinding.lvSafeTest.expandGroup(i);
        }

      }

      /*mBinding.lvSafeTest.expandGroup(mRequestIndex);*/

    }else{
      mBinding.tvSafeTestingTitle.setText("正在进行:" + routerContents[0]);
      if (TextUtils.isEmpty(GlobleConstant.getgDeviceId())) {
        GlobleConstant.setgDeviceId(key);
        String keyRepoId = key + "_" + PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER);
        GlobleConstant.setgKeyRepoId(keyRepoId);
      }
      mSafeTestPresenter.safeInspection(key);//第一个体检成功后再发第二次体检
    }

  }

  private List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> getDevTree() {
    List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> devTreeResponses = (List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>>)
        getIntent().getSerializableExtra(mNavigator.EXTR_ROUTER_LIST);
    return devTreeResponses;
  }

  @Override
  public void safeInspection(PostInspectionResponse response) {
    //动画
    setImgAnimation(mBinding.imgSafeHead);

    //默认第一个是网关
    updateRouterViewState(mRequestIndex, response);

    /*new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        mBinding.tvSafeTestingTitle.setText("您的设备非常安全");
        mBinding.tvSafeTestPassCount.setText("已完成全部安全检查");
        mBinding.btnSafeTest.setText("体检完成");
        mBinding.imgSafeHead.setBackground(getResources().getDrawable(R.drawable.test_done));
        isTesting = false;
        adapter.notifyDataSetChanged();
      }
    }, (DELAY + 3600) * deviceLists.size());*/

  }

  @Override
  public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
    return false;
  }

  @Override
  public void onGroupExpand(int groupPosition) {
  }

  private void setImgAnimation(ImageView img){
    Animation animation = AnimationUtils.loadAnimation(this, R.anim.img_animation);
    LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
    animation.setInterpolator(lin);
    img.startAnimation(animation);
  }

}
