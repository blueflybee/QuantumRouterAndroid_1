package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityVpnBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.MyProgressDialog;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.adapter.VPNAdapter;
import com.qtec.router.model.req.SetVpnRequest;
import com.qtec.router.model.rsp.GetVpnListResponse;
import com.qtec.router.model.rsp.SetVpnResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class VPNActivity extends BaseActivity implements GetVpnListView, SetVpnView, View.OnClickListener {
  private ActivityVpnBinding mBinding;
  private Boolean isVpnOpen = false;
  private VPNAdapter adapter;
  private List<GetVpnListResponse.VpnBean> vpns;
  private int mPosition = 0;
  private Boolean isConnecting = false;
  private MyProgressDialog myDialog;
  private String mSelectedIfName = "";
  private Boolean isConnectClick = false, isSumSwitchClick = false; //点击连接 再次点击取消连接,判断是否是总开关设置

  @Inject
  GetVpnListPresenter mGetVpnListPresenter;
  @Inject
  SetVpnPresenter mSetVpnPresenter;
  private int mRefreshCount = 0;

  private Handler mHandler = new Handler();

  private Runnable mRunnable = new Runnable() {
    @Override
    public void run() {
      queryVpnList(); //5s刷一次,共刷60s
      mRefreshCount++;
      mHandler.postDelayed(this, 4900);//每隔3s轮询一次
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_vpn);

    initializeInjector();

    initPresenter();

    initView();

    queryVpnList();

  }

  private void queryVpnList() {
    mGetVpnListPresenter.getVpnList(GlobleConstant.getgDeviceId());
  }

  private void setVpnRequest(SetVpnRequest<List<SetVpnRequest.VpnBean>> bean) {
    mSetVpnPresenter.setVpn(GlobleConstant.getgDeviceId(), bean);
  }

 /* private String routerId() {
    return getIntent().getStringExtra(Navigator.EXTRA_ROUTER_ID);
  }*/

  private void initView() {
    initTitleBar("VPN设置");
    mBinding.rlVpnSwitch.setOnClickListener(this);
    mBinding.rlAddVpn.setOnClickListener(this);
    vpns = new ArrayList<>();

  }

  private void initPresenter() {
    mGetVpnListPresenter.setView(this);
    mSetVpnPresenter.setView(this);
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
  public void getVpnList(GetVpnListResponse<List<GetVpnListResponse.VpnBean>> responses) {
    vpns.clear();
    vpns.addAll(responses.getVpn_list());

    if (isConnecting) {
      if ("down".equals(responses.getVpn_list().get(mPosition).getStatus())) {
        if (mRunnable != null) {
          if (mRefreshCount > 12) {
            mHandler.removeCallbacks(mRunnable);
            isConnecting = false;
            mRefreshCount = 0;
            /*myDialog.dismiss();*/
          }
        }
      } else if ("up".equals(responses.getVpn_list().get(mPosition).getStatus())) {
          /*myDialog.dismiss();*/
        isConnecting = false;
        if (mRunnable != null) {
          mRefreshCount = 0;
          mHandler.removeCallbacks(mRunnable);
        }
      }
    } else {
      if (mRunnable != null) {
        mRefreshCount = 0;
        mHandler.removeCallbacks(mRunnable);
      }
    }

    if (responses.getEnable() == 1) {
      isVpnOpen = true;
      mBinding.rlVpnSwitch.setVisibility(View.VISIBLE);

      mBinding.imgSwitchVpn.setBackgroundResource(R.drawable.ic_open);
      mBinding.lvVpn.setVisibility(View.VISIBLE);

      /*if (responses.getVpn_list().size() != 0) {*/

      adapter = new VPNAdapter(getContext(), vpns, R.layout.item_vpn_layout, mSelectedIfName, isConnecting);

      mBinding.lvVpn.setAdapter(adapter);

      adapter.setOnVpnDetailClickListener(new VPNAdapter.OnVpnDetailClickListener() {
        @Override
        public void onVpnDetailClick(int position) {
          Intent intent = new Intent(VPNActivity.this, AddVPNActivity.class);
          intent.putExtra("FLAG", "1");//修改
          intent.putExtra("VpnListSize", responses.getVpn_list().size());
          intent.putExtra("VPNINFO", responses.getVpn_list().get(position));//修改
          startActivityForResult(intent, 6);
        }
      });

      adapter.setOnVpnConnectClickListener(new VPNAdapter.OnVpnConnectClickListener() {
        @Override
        public void onVpnConnectClick(View v, int position) {
          // 点击连接 再次点击取消连接
          if (isConnectClick) {
            Toast.makeText(VPNActivity.this, "取消连接", Toast.LENGTH_SHORT).show();

            isConnectClick = false;

            isConnecting = false;

            mSelectedIfName = vpns.get(position).getIfname();

            SetVpnRequest<List<SetVpnRequest.VpnBean>> beans = new SetVpnRequest<List<SetVpnRequest.VpnBean>>();

            List<SetVpnRequest.VpnBean> beanList = new ArrayList<SetVpnRequest.VpnBean>();
            for (int i = 0; i < vpns.size(); i++) {
              SetVpnRequest.VpnBean bean = new SetVpnRequest.VpnBean();
              bean.setEnable(0);
              bean.setIfname(vpns.get(i).getIfname());
              beanList.add(bean);
            }
            beans.setVpn(beanList);
            beans.setEnable(1);
            setVpnRequest(beans);


          } else {
            Toast.makeText(VPNActivity.this, "连接中", Toast.LENGTH_SHORT).show();

            isConnectClick = true;

            isConnecting = true;

            mSelectedIfName = vpns.get(position).getIfname();

           /* myDialog = new MyProgressDialog(VPNActivity.this);
            myDialog.setMessage("vpn连接中…");
            myDialog.show();*/

            SetVpnRequest<List<SetVpnRequest.VpnBean>> beans = new SetVpnRequest<List<SetVpnRequest.VpnBean>>();

            List<SetVpnRequest.VpnBean> beanList = new ArrayList<SetVpnRequest.VpnBean>();
            for (int i = 0; i < vpns.size(); i++) {
              SetVpnRequest.VpnBean bean = new SetVpnRequest.VpnBean();
              if (i == position) {
                bean.setEnable(1);
                bean.setIfname(vpns.get(i).getIfname());
              } else {
                bean.setEnable(0);
                bean.setIfname(vpns.get(i).getIfname());
              }
              beanList.add(bean);
            }
            beans.setVpn(beanList);
            beans.setEnable(1);
            setVpnRequest(beans);
          }
        }
      });


    } else {
      isVpnOpen = false;
      mBinding.imgSwitchVpn.setBackgroundResource(R.drawable.ic_off);
      mBinding.lvVpn.setVisibility(View.GONE);
    }

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.rl_add_vpn:
        Intent intent = new Intent(this, AddVPNActivity.class);
        intent.putExtra("FLAG", "0");//添加
        intent.putExtra("VpnListSize", vpns.size());
        startActivityForResult(intent, 6);
        break;

      case R.id.rl_vpn_switch:

        isSumSwitchClick = true;
        isConnecting = false;

        SetVpnRequest<List<SetVpnRequest.VpnBean>> beans = new SetVpnRequest<List<SetVpnRequest.VpnBean>>();
        List<SetVpnRequest.VpnBean> beanList = new ArrayList<SetVpnRequest.VpnBean>();
        beanList.clear();

        for (int i = 0; i < vpns.size(); i++) {
          SetVpnRequest.VpnBean bean = new SetVpnRequest.VpnBean();
          if ("down".equals(vpns.get(i).getStatus())) {
            //未连接
            bean.setEnable(0);
          } else {
            bean.setEnable(1);
          }

          bean.setIfname(vpns.get(i).getIfname());
          beanList.add(bean);
        }
        beans.setVpn(beanList);
        if (isVpnOpen) {
          beans.setEnable(0);
        } else {
          beans.setEnable(1);
        }
        setVpnRequest(beans);
        break;

      default:
        break;
    }
  }

  /**
   * 连接vpn
   *
   * @param
   * @return
   */
  @Override
  public void setVpn(SetVpnResponse response) {
    /*queryVpnList();*/
    if (isSumSwitchClick) {
      //总开关设置
      isSumSwitchClick = false;
      System.out.println("vpn开关 总开关");
      if (isVpnOpen) {
        isVpnOpen = false;
        mBinding.imgSwitchVpn.setBackgroundResource(R.drawable.ic_off);
        mBinding.lvVpn.setVisibility(View.GONE);
      } else {
        isVpnOpen = true;
        mBinding.imgSwitchVpn.setBackgroundResource(R.drawable.ic_open);
        mBinding.lvVpn.setVisibility(View.VISIBLE);
        queryVpnList();
      }
    } else {
      //连接Vpn
      mHandler.postDelayed(mRunnable, 100);
      System.out.println("vpn开关 连接Vpn");
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == 6) {
      queryVpnList();
    }
  }

  @Override
  protected void onDestroy() {
    if (mRunnable != null) {
      mHandler.removeCallbacks(mRunnable);
    }
    super.onDestroy();
  }

  @Override
  protected void onPause() {
    if (mRunnable != null) {
      mHandler.removeCallbacks(mRunnable);
    }
    super.onPause();
  }
}
