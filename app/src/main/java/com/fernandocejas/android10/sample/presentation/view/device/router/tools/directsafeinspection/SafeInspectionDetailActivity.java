package com.fernandocejas.android10.sample.presentation.view.device.router.tools.directsafeinspection;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySafeInspectionDetailBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.router.model.rsp.PostInspectionResponse;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 一键体检
 *      version: 1.0
 * </pre>
 */

public class SafeInspectionDetailActivity extends BaseActivity implements PostInspectionView {
  private ActivitySafeInspectionDetailBinding mBinding;
  private Boolean isTesting = false;
  private int DELAY = 50;//延时基数

  @Inject
  PostInspectionPresenter mInspectionPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_safe_inspection_detail);
    initSpecialAttentionTitleBar("一键体检");
    initializeInjector();
    initPresenter();

    initData();

    safeInspctionRequest();

    mBinding.tvSafeInspecTitle1.setText("正在进行：Ddos服务");
    mBinding.tvSafeInspecTitle2.setText("已完成0项体检");
    mBinding.btnInspection.setText("取消体检");

    mBinding.btnInspection.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  private void setImgAnimation(ImageView img){
    Animation animation = AnimationUtils.loadAnimation(this, R.anim.img_animation);
    LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
    animation.setInterpolator(lin);
    img.startAnimation(animation);
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
    mInspectionPresenter.setView(this);
  }

  private void safeInspctionRequest() {
    mInspectionPresenter.safeInspection(GlobleConstant.getgDeviceId());
  }

  private void initData() {

  }

  @Override
  public void safeInspection(PostInspectionResponse response) {
    //1-10之间的随机数进行扫描 打钩代表体检过
   /* int time = new Random().nextInt(10);
    int second = (time+1)*1000;*/
    //isTesting = true;

/*    mBinding.imgWifi.setVisibility(View.VISIBLE);
    setImgAnimation(mBinding.imgWifi);
    mBinding.tvSafeInspecTitle1.setText("正在进行：wifi密码防火墙");
    //动画
    setImgAnimation(mBinding.imgHead);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        mBinding.imgWifi.clearAnimation();
        mBinding.imgWifi.setBackground(getResources().getDrawable(R.drawable.test_pass));
        mBinding.tvWifi.setText("wifi密码防火墙: 已关闭");
        mBinding.tvSafeInspecTitle2.setText("已完成1项体检");


      }
    },500);*/

    //动画
    setImgAnimation(mBinding.imgHead);
    mBinding.imgDdos.setVisibility(View.VISIBLE);
    setImgAnimation(mBinding.imgDdos);
    mBinding.tvSafeInspecTitle1.setText("正在进行：Ddos服务");
    mBinding.tvDdos.setText("Ddos服务: 检测中");
    mBinding.tvDdos.setTextColor(getResources().getColor(R.color.blue_2196f3));

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (response.getDdos() == 0) {
          mBinding.tvDdos.setText("Ddos服务: 已关闭");
        } else {
          mBinding.tvDdos.setText("Ddos服务: 已开启");
        }
        mBinding.tvDdos.setTextColor(getResources().getColor(R.color.black_424242));

        mBinding.tvSafeInspecTitle2.setText("已完成2项体检");
        mBinding.imgDdos.clearAnimation();

        mBinding.imgDdos.setBackground(getResources().getDrawable(R.drawable.test_pass));
        mBinding.imgTelnet.setVisibility(View.VISIBLE);

        mBinding.tvTelnet.setText("Telnet协议: 检测中");
        mBinding.tvTelnet.setTextColor(getResources().getColor(R.color.blue_2196f3));
        setImgAnimation(mBinding.imgTelnet);
        mBinding.tvSafeInspecTitle1.setText("正在进行：Telnet协议");
      }
    }, 1000);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (response.getTelent() == 0) {
          mBinding.tvTelnet.setText("Telnet协议: 已关闭");
        } else {
          mBinding.tvTelnet.setText("Telnet协议: 已开启");
        }
        mBinding.tvTelnet.setTextColor(getResources().getColor(R.color.black_424242));
        mBinding.tvSafeInspecTitle2.setText("已完成3项体检");
        mBinding.imgTelnet.clearAnimation();
        mBinding.imgTelnet.setBackground(getResources().getDrawable(R.drawable.test_pass));

        mBinding.imgFtp.setVisibility(View.VISIBLE);
        mBinding.tvFtp.setText("FTP协议: 检测中");
        mBinding.tvFtp.setTextColor(getResources().getColor(R.color.blue_2196f3));
        setImgAnimation(mBinding.imgFtp);
        mBinding.tvSafeInspecTitle1.setText("正在进行：FTP协议");
      }
    }, 1500);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (response.getFtp() == 0) {
          mBinding.tvFtp.setText("FTP协议: 已关闭");
        } else {
          mBinding.tvFtp.setText("FTP协议: 已开启");
        }
        mBinding.tvFtp.setTextColor(getResources().getColor(R.color.black_424242));

        mBinding.tvSafeInspecTitle2.setText("已完成4项体检");
        mBinding.imgFtp.clearAnimation();
        mBinding.imgFtp.setBackground(getResources().getDrawable(R.drawable.test_pass));
        mBinding.tvSamba.setText("Samba协议: 检测中");
        mBinding.tvSamba.setTextColor(getResources().getColor(R.color.blue_2196f3));
        mBinding.imgSamba.setVisibility(View.VISIBLE);
        setImgAnimation(mBinding.imgSamba);
        mBinding.tvSafeInspecTitle1.setText("正在进行：Samba协议");
      }
    }, 2500);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (response.getSamba() == 0) {
          mBinding.tvSamba.setText("Samba协议: 已关闭");
        } else {
          mBinding.tvSamba.setText("Samba协议: 已开启");
        }
        mBinding.tvSamba.setTextColor(getResources().getColor(R.color.black_424242));
        mBinding.tvSafeInspecTitle2.setText("已完成5项体检");
        mBinding.imgSamba.clearAnimation();
        mBinding.imgSamba.setBackground(getResources().getDrawable(R.drawable.test_pass));
        mBinding.imgVirtual.setVisibility(View.VISIBLE);
        mBinding.tvVirtual.setText("虚拟服务: 检测中");
        mBinding.tvVirtual.setTextColor(getResources().getColor(R.color.blue_2196f3));
        setImgAnimation(mBinding.imgVirtual);
        mBinding.tvSafeInspecTitle1.setText("正在进行：虚拟服务");
      }
    }, 3000);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (response.getVirtualservice() == 0) {
          mBinding.tvVirtual.setText("虚拟服务: 已关闭");
        } else {
          mBinding.tvVirtual.setText("虚拟服务: 已开启");
        }
        mBinding.tvVirtual.setTextColor(getResources().getColor(R.color.black_424242));
        mBinding.tvSafeInspecTitle2.setText("已完成6项体检");
        mBinding.imgVirtual.clearAnimation();
        mBinding.imgVirtual.setBackground(getResources().getDrawable(R.drawable.test_pass));
        mBinding.tvDmz.setText("虚拟服务: 检测中");
        mBinding.tvDmz.setTextColor(getResources().getColor(R.color.blue_2196f3));
        mBinding.imgDma.setVisibility(View.VISIBLE);
        setImgAnimation(mBinding.imgDma);
        mBinding.tvSafeInspecTitle1.setText("正在进行：DMZ主机");
      }
    }, 3500);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (response.getDmz() == 0) {
          mBinding.tvDmz.setText("DMZ主机: 已关闭");
        } else {
          mBinding.tvDmz.setText("DMZ主机: 已开启");
        }
        mBinding.tvDmz.setTextColor(getResources().getColor(R.color.black_424242));
        mBinding.tvSafeInspecTitle2.setText("已完成7项体检");
        mBinding.imgDma.clearAnimation();
        mBinding.imgDma.setBackground(getResources().getDrawable(R.drawable.test_pass));
        mBinding.imgUpnp.setVisibility(View.VISIBLE);
        mBinding.tvUpnp.setText("UPNP开关: 检测中");
        mBinding.tvUpnp.setTextColor(getResources().getColor(R.color.blue_2196f3));
        setImgAnimation(mBinding.imgUpnp);
        mBinding.tvSafeInspecTitle1.setText("正在进行：UPNP开关");
      }
    }, 4000);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (response.getDmz() == 0) {
          mBinding.tvUpnp.setText("UPNP开关: 已关闭");
        } else {
          mBinding.tvUpnp.setText("UPNP开关: 已开启");
        }
        mBinding.tvUpnp.setTextColor(getResources().getColor(R.color.black_424242));
        mBinding.tvSafeInspecTitle2.setText("已完成所有体检");
        mBinding.imgUpnp.clearAnimation();
        mBinding.imgUpnp.setBackground(getResources().getDrawable(R.drawable.test_pass));
        mBinding.tvSafeInspecTitle1.setText("您的网关非常安全");

        mBinding.btnInspection.setText("体检完成");
        mBinding.imgHead.setBackground(getResources().getDrawable(R.drawable.test_done));

      }
    }, 4500);


  }
}
