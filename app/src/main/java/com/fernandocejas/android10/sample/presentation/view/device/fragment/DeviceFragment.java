package com.fernandocejas.android10.sample.presentation.view.device.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.listener.OnHighlightDrewListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.app.hubert.guide.model.HighlightOptions;
import com.app.hubert.guide.model.RelativeGuide;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.blelibrary.BleGattCharacteristic;
import com.blueflybee.blelibrary.BleGattService;
import com.blueflybee.blelibrary.BleService;
import com.blueflybee.blelibrary.IBle;
import com.blueflybee.keystore.KeystoreRepertory;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentDeviceBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.MainComponent;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DeviceCacheManager;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.GlideUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.utils.TimeUtil;
import com.fernandocejas.android10.sample.presentation.view.device.activity.SelectDevTypeActivity;
import com.fernandocejas.android10.sample.presentation.view.device.adapter.DevExpandAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.data.LiteGateway;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockUserPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockUserView;
import com.fernandocejas.android10.sample.presentation.view.device.lock.fragment.LockSettingPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.lock.fragment.LockSettingView;
import com.fernandocejas.android10.sample.presentation.view.device.lock.fragment.UnbindLockFragment;
import com.fernandocejas.android10.sample.presentation.view.device.router.RouterMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.addrouter.AddRouterPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.router.addrouter.AddRouterView;
import com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig.FirstConfigPromptActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig.GetRouterFirstConfigPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig.GetRouterFirstConfigView;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.RouterSettingPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.RouterSettingView;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.obj.ApplyMenu;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet.AntiFritNetMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet.SetQuestionActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.bandspeedmeasure.BandSpeedMeasureActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.childcare.ChildCareActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.directsafeinspection.SafeInspectionActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.fragment.AntiFritNetStatusPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.fragment.AntiFritNetStatusView;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.fragment.GetAntiQuestionPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.fragment.IGetAntiQuestionView;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.guestwifi.GuestWifiActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.intelligentband.IntelligentBandActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.RemoteMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.signalregulation.SignalRegulationActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.specialattention.SpecialAttentionActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.wifitimeswitch.WifiTimeSwitchActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay.WirelessRelayActivity;
import com.fernandocejas.android10.sample.presentation.view.message.receiver.MessageCenterActivity;
import com.fernandocejas.android10.sample.presentation.view.message.receiver.YunOsMessageReceiver;
import com.fernandocejas.android10.sample.presentation.view.mine.deviceshare.IPostInvitationView;
import com.fernandocejas.android10.sample.presentation.view.mine.deviceshare.InputPhoneNumActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.deviceshare.PostInvitationPresenter;
import com.fernandocejas.android10.sample.presentation.view.mine.myinfo.MyInfoActivity;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.mapp.model.req.PostInvitationRequest;
import com.qtec.mapp.model.rsp.AdjustLockVolumeResponse;
import com.qtec.mapp.model.rsp.DeleteLockUserResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse.DeviceBean;
import com.qtec.mapp.model.rsp.GetLockUsersResponse;
import com.qtec.mapp.model.rsp.GetMsgCountResponse;
import com.qtec.mapp.model.rsp.GetUserRoleResponse;
import com.qtec.mapp.model.rsp.PostInvitationResponse;
import com.qtec.mapp.model.rsp.UnbindLockOfAdminResponse;
import com.qtec.mapp.model.rsp.UnbindRouterResponse;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.router.model.rsp.FactoryResetResponse;
import com.qtec.router.model.rsp.GetAntiFritNetStatusResponse;
import com.qtec.router.model.rsp.GetAntiFritQuestionResponse;
import com.qtec.router.model.rsp.GetRouterFirstConfigResponse;
import com.qtec.router.model.rsp.GetSignalRegulationResponse;
import com.qtec.router.model.rsp.RestartRouterResponse;
import com.qtec.router.model.rsp.SearchRouterResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import static com.blueflybee.blelibrary.BleRequest.CmdType.UNBIND_LOCK;

/**
 * <pre>
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.0
 * </pre>
 */
public class DeviceFragment extends UnbindLockFragment implements QDevView, AddRouterView, IPostInvitationView, IGetSignalModeView, IGetMsgCountView, RouterSettingView, LockSettingView, LockUserView, GetRouterFirstConfigView, AntiFritNetStatusView, IGetAntiQuestionView {

  private static final String TAG = DeviceFragment.class.getSimpleName();

  private FragmentDeviceBinding mBinding;

  @Inject
  QDevPresenter mQDevPresenter;

  @Inject
  PostInvitationPresenter mPostInvitation;

  @Inject
  AddRouterPresenter mAddRouterPresenter;

  @Inject
  GetSignalModePresenter mGetSignalModePresenter;

  @Inject
  GetMsgCountPresenter mMsgCountPresenter;

  @Inject
  RouterSettingPresenter mRouterSettingPresenter;

  @Inject
  LockSettingPresenter mLockSettingPresenter;

  @Inject
  LockUserPresenter mLockUserPresenter;

  @Inject
  GetRouterFirstConfigPresenter mGetRouterFirstConfigPresenter;

  @Inject
  AntiFritNetStatusPresenter mAntiFritNetStatusPresenter;

  @Inject
  GetAntiQuestionPresenter mAntiQuestionPresenter;


  private DevExpandAdapter mAdapter;
  private String mRouterID = "";
  private boolean mDeviceGuideShowed = false;

  private BleStateChangeReceiver mBleStateChangeReceiver = new BleStateChangeReceiver();
  private GetDevTreeResponse<List<DeviceBean>> mSelecetLock;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(MainComponent.class).inject(this);
  }

  public static DeviceFragment newInstance() {
    Bundle args = new Bundle();
    DeviceFragment fragment = new DeviceFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_device, container, false);
    initPresenter();
    return mBinding.getRoot();
  }

  @Override
  public void onStart() {
    super.onStart();
    queryDevTree();
    quaryMsgCount();//调接口刷新白点
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initView();
  }

  private void initPresenter() {
    mQDevPresenter.setView(this);
    mAddRouterPresenter.setView(this);
    mPostInvitation.setView(this);
    mGetSignalModePresenter.setView(this);
    mMsgCountPresenter.setView(this);
    mRouterSettingPresenter.setView(this);
    mLockSettingPresenter.setView(this);
    mLockUserPresenter.setView(this);
    mGetRouterFirstConfigPresenter.setView(this);
    mAntiFritNetStatusPresenter.setView(this);
    mAntiQuestionPresenter.setView(this);
  }

  private void querySignalModeRequest(String routerId) {
    mGetSignalModePresenter.getSignalMode(routerId);
  }

  private void queryDevTree() {
    showCachedDevTree(DeviceCacheManager.fetchDevices(getContext()));
    mQDevPresenter.getDevTree("");
  }


  private void initView() {

    GlideUtil.loadCircleHeadImage(getContext(), PrefConstant.getUserHeadImg(), mBinding.imgHead);

    //判断是否有新消息
    YunOsMessageReceiver.setOnRefreshMsgListener(new YunOsMessageReceiver.OnRefreshMsgListener() {
      @Override
      public void refreshMsg() {
        System.out.println("消息 主页执行");
        mBinding.btnDeviceMessage.setBackground(getActivity().getResources().getDrawable(R.drawable.indexic_noticenew));
      }
    });

    if (AndroidApplication.IS_HAS_NEW_MSG) {
      // AndroidApplication.IS_HAS_NEW_MSG = false;
      System.out.println("消息 标志执行");
      mBinding.btnDeviceMessage.setBackground(getActivity().getResources().getDrawable(R.drawable.indexic_noticenew));
    } else {
      AndroidApplication.IS_HAS_NEW_MSG = false;
      mBinding.btnDeviceMessage.setBackground(getActivity().getResources().getDrawable(R.drawable.indexic_notice));
    }

    String formatStr = TimeUtil.timeFormatStr(Calendar.getInstance());
    mBinding.tvNickname.setText(formatStr + "好，" + PrefConstant.getUserNickName() + "！");

    mBinding.btnDeviceMessage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getActivity(), MessageCenterActivity.class);
        startActivityForResult(intent, 12);
      }
    });

    mBinding.btnDeviceAddRouter.setOnClickListener(v -> mNavigator.navigateTo(getActivity(), SelectDevTypeActivity.class));

    mBinding.llAddRouter.setOnClickListener(v -> mNavigator.navigateTo(getContext(), SelectDevTypeActivity.class));

    mBinding.imgHead.setOnClickListener(v -> mNavigator.navigateTo(getContext(), MyInfoActivity.class));

  }

  private void initData() {
    if (mAdapter != null && mAdapter.getGroupCount() != 0) {
      mAdapter.setOnShareDeviceClickListener((groupPosition, routerID) -> {
        // 分享设备
        mRouterID = routerID;
        startActivityForResult(new Intent(getActivity(), InputPhoneNumActivity.class), 11);
      });

      mAdapter.setOnUnbindRouterClickListener(new DevExpandAdapter.OnUnbindRouterClickListener() {
        @Override
        public void onClick(GetDevTreeResponse<List<DeviceBean>> device) {
          DialogUtil.showOkCancelAlertDialogWithMessage(getContext(), "解绑网关", "确认要解绑网关吗？",
              v1 -> {
                mRouterSettingPresenter.unbindRouter(device.getDeviceSerialNo());
              }, null);
        }
      });

      mAdapter.setOnUnbindLockClickListener(new DevExpandAdapter.OnUnbindLockClickListener() {
        @Override
        public void onClick(GetDevTreeResponse<List<DeviceBean>> device) {
          mSelecetLock = device;
          initBleData(device.getMac());
          mIsAdmin = GetUserRoleResponse.ADMIN.equals(device.getUserRole());
          if (mIsAdmin) {
            mLockUserPresenter.getLockUsers(device.getDeviceSerialNo());
          } else {
            DialogUtil.showConfirmCancelDialog(getContext(), "提示", "确定要解绑门锁吗？", "确定", "取消", new View.OnClickListener() {
              @Override
              public void onClick(View v1) {
                unbindLockBle();
              }
            }, null);
          }
        }
      });

      mAdapter.setOnRouterGroupViewClickListener(new DevExpandAdapter.OnRouterGroupViewClickListener() {
        @Override
        public void onClick(GetDevTreeResponse<List<DeviceBean>> router, int groupPosition, int currentConnectedPosition) {
          GlobleConstant.setgDeviceId(router.getDeviceSerialNo());
          mGetRouterFirstConfigPresenter.getFirstConfig();
          goRouter(router, groupPosition, currentConnectedPosition);
        }
      });

      mAdapter.setOnRouterGridViewClickListener(new DevExpandAdapter.OnRouterGridViewClickListener() {
        @Override
        public void onClick(GetDevTreeResponse<List<DeviceBean>> router, ApplyMenu menu, int groupPosition, int gridItemPosition, int currentConnectedPosition) {
          GlobleConstant.setgDeviceId(router.getDeviceSerialNo());
          mGetRouterFirstConfigPresenter.getFirstConfig();
          goRouterMenu(router, menu, groupPosition, gridItemPosition, currentConnectedPosition);
        }
      });
    }

  }

  private void goRouterMenu(GetDevTreeResponse<List<DeviceBean>> router, ApplyMenu menu, int groupPosition, int gridItemPosition, int currentConnectedPosition) {
    System.out.println("menu = " + menu);
    switch (menu.getId()) {
      case "100001":
        Intent intent = new Intent();
        intent.putExtra(Navigator.EXTRA_ROUTER_ID, router.getDeviceSerialNo());
        intent.putExtra(Navigator.EXTRA_ROUTER_NAME, router.getDeviceName());
        intent.putExtra(Navigator.EXTRA_ROUTER_MODEL, router.getDeviceModel());
        new Navigator().navigateTo(getContext(), RouterMainActivity.class, intent);
        break;

      case "100002":
        querySignalModeRequest(router.getDeviceSerialNo());
        break;

      case "100003":
        Intent intent2 = new Intent();
        intent2.putExtra(Navigator.EXTRA_ROUTER_ID, router.getDeviceSerialNo());
        new Navigator().navigateTo(getContext(), IntelligentBandActivity.class, intent2);
        break;

      case "100004":
        Intent intent6 = new Intent();
        intent6.putExtra(Navigator.EXTRA_ROUTER_ID, router.getDeviceSerialNo());
        new Navigator().navigateTo(getContext(), SafeInspectionActivity.class, intent6);
        break;

      case "100005":
        Intent intent5 = new Intent();
        mNavigator.navigateTo(getContext(), WirelessRelayActivity.class, intent5);
        break;

      case "100006":
        //防蹭网
        queryAntiFritNetStatus();
        break;

      case "100007":
        Intent intent7 = new Intent();
        mNavigator.navigateTo(getContext(), BandSpeedMeasureActivity.class, intent7);
        break;

      case "100008":
        Intent intent3 = new Intent();
        intent3.putExtra(Navigator.EXTRA_ROUTER_ID, router.getDeviceSerialNo());
        new Navigator().navigateTo(getContext(), SpecialAttentionActivity.class, intent3);
        break;

      case "100009":
        new Navigator().navigateTo(getContext(), WifiTimeSwitchActivity.class);
        break;

      case "1000010":
        Intent intent11 = new Intent();
        mNavigator.navigateTo(getContext(), GuestWifiActivity.class, intent11);
        break;

      case "1000011":
        Intent intent12 = new Intent();
        mNavigator.navigateTo(getContext(), ChildCareActivity.class, intent12);
        break;

      case "1000012":
        if (currentConnectedPosition == groupPosition) {
          GlobleConstant.isSambaExtranetAccess = false;
          System.out.println("GlobleConstant.isSambaExtranetAccess deviceFragment1 = " + GlobleConstant.isSambaExtranetAccess);
          new Navigator().navigateTo(getContext(), RemoteMainActivity.class);
        } else {
          GlobleConstant.isSambaExtranetAccess = true;
          System.out.println("GlobleConstant.isSambaExtranetAccess deviceFragment2 = " + GlobleConstant.isSambaExtranetAccess);
          new Navigator().navigateTo(getContext(), RemoteMainActivity.class);
        }
        break;

      case "1000013":
        Intent intentMore = new Intent();
        intentMore.putExtra(Navigator.EXTRA_ROUTER_ID, router.getDeviceSerialNo());
        intentMore.putExtra(Navigator.EXTRA_TO_FRAGMENT, 1);
        intentMore.putExtra(Navigator.EXTRA_ROUTER_NAME, router.getDeviceName());
        intentMore.putExtra(Navigator.EXTRA_ROUTER_MODEL, router.getDeviceModel());
        new Navigator().navigateTo(getContext(), RouterMainActivity.class, intentMore);
        break;
    }
  }

  private void goRouter(GetDevTreeResponse<List<DeviceBean>> router, int groupPosition, int currentConnectedPosition) {
    GlobleConstant.setgDeviceId(router.getDeviceSerialNo());
    String keyRepoId = router.getDeviceSerialNo() + "_" + PrefConstant.getUserUniqueKey(router.getDeviceType());
    GlobleConstant.setgKeyRepoId(keyRepoId);
    if (KeystoreRepertory.getInstance().has(keyRepoId)) {
      Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_ROUTER_ID, router.getDeviceSerialNo());
      intent.putExtra(Navigator.EXTRA_ROUTER_NAME, router.getDeviceName());
      intent.putExtra(Navigator.EXTRA_ROUTER_TYPE, router.getDeviceType());
      intent.putExtra(Navigator.EXTRA_ROUTER_MODEL, router.getDeviceModel());
      new Navigator().navigateTo(getContext(), RouterMainActivity.class, intent);
    }
  }

  private void initBleData(String mac) {
    mDeviceAddress = mac;
    mBleLock = LockManager.getLock(getContext(), mDeviceAddress);
  }

  @Override
  public void showDevTree(List<GetDevTreeResponse<List<DeviceBean>>> responses) {
    DeviceCacheManager.saveDevices(getContext(), responses);
    int routerCount = getRouterCount(responses);
    mBinding.tvStatus.setText(routerCount == 0 ? "" : routerCount + "台网关正在安全运行中...");
    mBinding.expandList.setVisibility(View.VISIBLE);
    mBinding.llAddRouter.setVisibility(View.GONE);

    //备份设备树
    GlobleConstant.setDeviceTrees(responses);

    mAdapter = new DevExpandAdapter(getContext(), responses);
    mBinding.expandList.setAdapter(mAdapter);

    //默认都展开
    for (int i = 0; i < mAdapter.getGroupCount(); i++) {
      mBinding.expandList.expandGroup(i);
    }

    mAddRouterPresenter.searchRouter();

    initData();

  }

  private int getRouterCount(List<GetDevTreeResponse<List<DeviceBean>>> responses) {
    int result = 0;
    for (GetDevTreeResponse<List<DeviceBean>> response : responses) {
      if (!AppConstant.DEVICE_TYPE_ROUTER.equals(response.getDeviceType())) continue;
      result++;
    }
    return result;
  }

  /**
   * 防蹭网状态 请求
   *
   * @param
   * @return
   */
  private void queryAntiFritNetStatus() {
    mAntiFritNetStatusPresenter.queryAntiFritNetStatus(GlobleConstant.getgDeviceId());
  }

  private void quaryMsgCount() {
    mMsgCountPresenter.getMsgCount();
  }

  @Override
  public void showNoDevice() {
    DeviceCacheManager.saveDevices(getContext(), null);
    mBinding.tvStatus.setText("还没有添加智能设备");
    mBinding.expandList.setVisibility(View.GONE);
    mBinding.llAddRouter.setVisibility(View.VISIBLE);

    GlobleConstant.setDeviceTrees(null);
  }

  @Override
  public void showNoDeviceGuide() {
    System.out.println("DeviceFragment.showNoDeviceGuide");
    HighlightOptions llOptions = new HighlightOptions.Builder()
        .setOnHighlightDrewListener(new OnHighlightDrewListener() {
          @Override
          public void onHighlightDrew(Canvas canvas, RectF rectF) {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setPathEffect(new DashPathEffect(new float[]{12, 8}, 0));
            rectF.inset(-10, -10);
            canvas.drawRoundRect(rectF, 10, 10, paint);
          }
        })
        .setRelativeGuide(new RelativeGuide(R.layout.view_guide_relative_add, Gravity.TOP, 20))
        .build();

    HighlightOptions imgAddOptions = new HighlightOptions.Builder()
        .setOnHighlightDrewListener(new OnHighlightDrewListener() {
          @Override
          public void onHighlightDrew(Canvas canvas, RectF rectF) {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setPathEffect(new DashPathEffect(new float[]{10, 8}, 0));
            canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 10, paint);
          }
        })
        .build();

    HighlightOptions messageOptions = new HighlightOptions.Builder()
        .setOnHighlightDrewListener(new OnHighlightDrewListener() {
          @Override
          public void onHighlightDrew(Canvas canvas, RectF rectF) {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setPathEffect(new DashPathEffect(new float[]{10, 8}, 0));
            canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2, paint);
          }
        })
        .setRelativeGuide(new RelativeGuide(R.layout.view_guide_relative_msg, Gravity.LEFT, 0))
        .build();

    NewbieGuide.with(this)
        .setLabel("no_device")
        .setShowCounts(1)//控制次数
//        .alwaysShow(true)//总是显示，调试时可以打开
        .addGuidePage(GuidePage.newInstance()
            .setEverywhereCancelable(false)
            .addHighLightWithOptions(mBinding.llAddRouter, HighLight.Shape.ROUND_RECTANGLE, 10, 10, llOptions)
            .addHighLightWithOptions(mBinding.btnDeviceAddRouter, HighLight.Shape.CIRCLE, 0, -19, imgAddOptions)
            .addHighLightWithOptions(mBinding.btnDeviceMessage, HighLight.Shape.CIRCLE, messageOptions)
            .setLayoutRes(R.layout.view_guide_simple, R.id.iv_know)
            .setBackgroundColor(getResources().getColor(R.color.alpha_50)))
        .show();
  }

  @Override
  public void showDeviceGuide() {
    HighlightOptions detailOptions = new HighlightOptions.Builder()
        .setOnHighlightDrewListener(new OnHighlightDrewListener() {
          @Override
          public void onHighlightDrew(Canvas canvas, RectF rectF) {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setPathEffect(new DashPathEffect(new float[]{12, 8}, 0));
            rectF.inset(-6, -6);
            canvas.drawRoundRect(rectF, 10, 10, paint);
          }
        })
        .setRelativeGuide(new RelativeGuide(R.layout.view_guide_relative_msg, Gravity.TOP, 20) {
          @Override
          protected void onLayoutInflated(View view) {
            super.onLayoutInflated(view);
            ImageView iv = view.findViewById(R.id.iv_icon);
            iv.setBackgroundResource(R.drawable.gn_xq);
          }
        })
        .build();

    HighlightOptions shareOptions = new HighlightOptions.Builder()
        .setOnHighlightDrewListener(new OnHighlightDrewListener() {
          @Override
          public void onHighlightDrew(Canvas canvas, RectF rectF) {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setPathEffect(new DashPathEffect(new float[]{12, 8}, 0));
            rectF.inset(-6, -6);
            canvas.drawRoundRect(rectF, 10, 10, paint);
          }
        })
        .setRelativeGuide(new RelativeGuide(R.layout.view_guide_relative_share, Gravity.LEFT, 20))
        .build();

    mBinding.expandList.postDelayed(new Runnable() {
      @Override
      public void run() {
        View firstView = mBinding.expandList.getChildAt(0);
        if (firstView == null) return;
        GetDevTreeResponse<List<DeviceBean>> group = mAdapter.getGroup(0);
        if (group == null) return;
        View detailView = firstView.findViewById(R.id.rv_left);
        View shareView = firstView.findViewById(R.id.iv_share);
        if (detailView == null || shareView == null) return;
        GuidePage guidePage = GuidePage.newInstance();
        guidePage
            .setEverywhereCancelable(false)
            .addHighLightWithOptions(detailView, HighLight.Shape.ROUND_RECTANGLE, 10, 5, detailOptions)
            .setLayoutRes(R.layout.view_guide_simple, R.id.iv_know)
            .setBackgroundColor(getResources().getColor(R.color.alpha_50));
        if (!LiteGateway.DEVICE_TYPE_LITE_GATEWAY.equals(group.getDeviceType())
            && shareView.getVisibility() == View.VISIBLE) {
          guidePage.addHighLightWithOptions(shareView, HighLight.Shape.ROUND_RECTANGLE, 10, -36, shareOptions);
        }

        NewbieGuide.with(DeviceFragment.this)
            .setLabel("device")
            .setShowCounts(1)//控制次数
//            .alwaysShow(true)//总是显示，调试时可以打开
            .addGuidePage(guidePage)
            .show();
      }
    }, 2000);
  }

  private void showCachedDevTree(List<GetDevTreeResponse<List<DeviceBean>>> devices) {
    if (devices == null || devices.isEmpty()) {
      showNoDevice();
    } else {
      showDevTree(devices);
    }
  }

  @Override
  public void showSearchSuccess(SearchRouterResponse response) {

    mAdapter.setCurrentConnected(response.getSerialnum());

    System.out.println("唯一ID showSearchSuccess = ");
  }

  @Override
  public void showSearchFailed(Throwable e) {
    /*mBinding.tvConnectState.setText("未连接到任何设备");*/
    System.out.println("唯一ID showSearchFailed = ");
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (data == null) {
      return;
    }

    if (requestCode == 11) {
      //选择通讯录回来
      String num = data.getStringExtra("PHONENUM");
      System.out.println("分享 num = " + num);
      System.out.println("分享 mRouterID = " + mRouterID);
      //邀请逻辑处理
      SPUtils spUtils = new SPUtils(PrefConstant.SP_NAME);
      String userPhone = spUtils.getString(PrefConstant.SP_USER_PHONE);

      PostInvitationRequest request = new PostInvitationRequest();
      request.setDeviceSerialNo(mRouterID);
      request.setSharedPhone(num);
      request.setSharePhone(userPhone);
      QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
      encryptInfo.setData(request);
      mPostInvitation.postInvitation(encryptInfo);
    } else if (requestCode == 12) {
      queryDevTree();

      quaryMsgCount();//调接口刷新白点
    }
  }

  @Override
  public void postInvitation(PostInvitationResponse response) {
    SnackbarUtils.showShort(mBinding.getRoot(), "发送成功，请用户登录App查看", Color.WHITE, getResources().getColor(R.color.green_48b04b));

  }

  @Override
  public void getSignalMode(GetSignalRegulationResponse response) {
    Intent intent1 = new Intent();
    intent1.putExtra(Navigator.EXTRA_ROUTER_ID, mRouterID);
    intent1.putExtra("MODE", response.getMode());
    mNavigator.navigateTo(getContext(), SignalRegulationActivity.class, intent1);
  }

  @Override
  public void getMsgCount(GetMsgCountResponse response) {
    if ("0".equals(response.getTotalMsgTipNum())) {
      //没有消息了
      mBinding.btnDeviceMessage.setBackground(getActivity().getResources().getDrawable(R.drawable.indexic_notice));
      AndroidApplication.IS_HAS_NEW_MSG = false;
    } else {
      mBinding.btnDeviceMessage.setBackground(getActivity().getResources().getDrawable(R.drawable.indexic_noticenew));
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    GlideUtil.loadCircleHeadImage(getContext(), PrefConstant.getUserHeadImg(), mBinding.imgHead);
    String formatStr = TimeUtil.timeFormatStr(Calendar.getInstance());
    mBinding.tvNickname.setText(formatStr + "好，" + PrefConstant.getUserNickName() + "！");

    initBleStateChangeReceiver();
    getContext().registerReceiver(mBleReceiver, BleService.getIntentFilter());
  }

  private void initBleStateChangeReceiver() {
    getActivity().registerReceiver(mBleStateChangeReceiver, createFilters());
  }

  @Override
  public void onPause() {
    super.onPause();
    getActivity().unregisterReceiver(mBleStateChangeReceiver);
    getContext().unregisterReceiver(mBleReceiver);
  }

  @Override
  public void restartRouterSuccess(RestartRouterResponse response) {
  }

  @Override
  public void factoryResetSuccess(FactoryResetResponse response) {
  }

  @Override
  public void unbindRouterSuccess(UnbindRouterResponse response, String deviceId) {
    queryDevTree();
    String keyRepoId = deviceId + "_" + PrefConstant.getUserUniqueKey("0");
    KeystoreRepertory.getInstance().clear(keyRepoId);
    ToastUtils.showLong("网关解除绑定成功");
  }

  @Override
  public void unbindLockSuccess(UnbindRouterResponse response, String mac) {
    queryDevTree();
    BleLock lock = LockManager.getLock(getActivity(), mac);
    KeystoreRepertory.getInstance().clear(lock.getKeyRepoId());
    LockManager.delete(getActivity(), lock.getMac());
    ToastUtils.showLong("门锁解除绑定成功");
  }

  @Override
  public void unbindLockOfAdminSuccess(UnbindLockOfAdminResponse response, String mac) {
    queryDevTree();
    BleLock lock = LockManager.getLock(getActivity(), mac);
    KeystoreRepertory.getInstance().clear(lock.getKeyRepoId());
    LockManager.delete(getActivity(), lock.getMac());
    ToastUtils.showLong("门锁解除绑定成功");
  }

  @Override
  public void showLockVolume(String volume) {
  }

  @Override
  public void onAdjustLockVolume(AdjustLockVolumeResponse response) {
  }


  //蓝牙监听需要添加的Action
  private IntentFilter createFilters() {
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
    intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
    intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF");
    intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON");
    return intentFilter;
  }

  @Override
  public void showUserRole(GetUserRoleResponse response) {

  }

  @Override
  public void showLockUsers(List<GetLockUsersResponse> response) {
    mIsMoreThanOneUser = response.size() > 1;

    if (mIsMoreThanOneUser) {
      DialogUtil.showSelectLockManagerDialog(getContext(), response, new DialogUtil.OnSelectLockManagerUnbindListener() {
        @Override
        public void onClick(View v, GetLockUsersResponse user) {
          mSelectedLockUser = user;
          unbindLockBle();
        }
      });
    } else {
      DialogUtil.showConfirmCancelDialog(getContext(), "提示", "确定要解绑门锁吗？", "确定", "取消", new View.OnClickListener() {
        @Override
        public void onClick(View v1) {
          unbindLockBle();
        }
      }, null);
    }

  }

  @Override
  public void onDeleteLockUserSuccess(DeleteLockUserResponse response) {
  }

  @Override
  public void startFirstConfig(GetRouterFirstConfigResponse response) {
    mNavigator.navigateTo(getContext(), FirstConfigPromptActivity.class);
  }

  @Override
  public void getAntiFritnetStatus(GetAntiFritNetStatusResponse response) {

    if (response.getEnable() == 0) {
      //关闭并且没有问题才去配置问题
      getQuestionRequest();
    } else {
      Intent intent6 = new Intent();
      intent6.putExtra("FragmentPosition", 0);
      mNavigator.navigateTo(getContext(), AntiFritNetMainActivity.class, intent6);
    }

  }

  private void getQuestionRequest() {
    mAntiQuestionPresenter.getAntiFritQuestion(GlobleConstant.getgDeviceId());
  }

  @Override
  public void getAntiFritQuestion(GetAntiFritQuestionResponse response) {

    if (TextUtils.isEmpty(response.getAnswer()) && TextUtils.isEmpty(response.getQuestion())) {
      Intent intent0 = new Intent();
      intent0.putExtra("FLAG", "0");//首次设置
      mNavigator.navigateTo(getContext(), SetQuestionActivity.class, intent0);
    } else {
      //有问题就不用设置问题
      Intent intent7 = new Intent();
      intent7.putExtra("FragmentPosition", 0);
      mNavigator.navigateTo(getContext(), AntiFritNetMainActivity.class, intent7);
    }

  }

  /**
   * 实现蓝牙状态广播接收
   */
  public class BleStateChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (!BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) return;
      int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
      if (blueState == BluetoothAdapter.STATE_OFF || blueState == BluetoothAdapter.STATE_ON) {
        if (mAdapter != null) {
          mAdapter.notifyDataSetChanged();
        }
      }
    }
  }

  private final BroadcastReceiver mBleReceiver = new BroadcastReceiver() {
    private BleMapper bleMapper = new BleMapper();

    @Override
    public void onReceive(Context context, Intent intent) {
      Bundle extras = intent.getExtras();
      if (!mDeviceAddress.equals(extras.getString(BleService.EXTRA_ADDR))) {
        return;
      }
      String action = intent.getAction();
      if (BleService.BLE_GATT_CONNECTED.equals(action)) {
        System.out.println(TAG + " connected +++++++++++++++++++++++++++++++");
      } else if (BleService.BLE_GATT_DISCONNECTED.equals(action)) {
        getBaseActivity().handleBleDisconnect(getBaseActivity().getBleCmdType(intent) != UNBIND_LOCK, false, mDeviceAddress);
        System.out.println(TAG + " disconnected +++++++++++++++++++++++++++++++");
      } else if (BleService.BLE_SERVICE_DISCOVERED.equals(action)) {
        System.out.println(TAG + " service discovered +++++++++++++++++++++++++++++++");

        IBle ble = getBaseActivity().getBle();
        if (ble == null) return;
        BleGattService service = ble.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
        if (service == null) {
          ble.requestConnect(mDeviceAddress, mCurrentCmdType, 30);
          return;
        }
        BleGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

        ble.requestCharacteristicNotification(mDeviceAddress, characteristic);
        String data = new BleMapper().pkgToReqString(mCurrentPkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        ble.requestWriteCharacteristicToLock(mCurrentCmdType, mDeviceAddress, characteristic, data, true);
      } else if (BleService.BLE_REQUEST_FAILED.equals(action)) {
        getBaseActivity().handleBleFail(intent, getBaseActivity().getBleCmdType(intent) != UNBIND_LOCK, false, mDeviceAddress);
      }

      //////////////////////////////////////////////////////////////////////////////////////////////
      else if (BleService.BLE_UNBIND_LOCK.equals(action)) {
        getBaseActivity().setUserUsingBle(false, mDeviceAddress);
        hideLoading();
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BlePkg blePkg = bleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());

        String payload = bleMapper.unbindUser(blePkg);
        if (TextUtils.isEmpty(payload)) {
          ToastUtils.showShort("解绑门锁失败");
          return;
        }

        if (!getBaseActivity().bleRspFail(payload, true, mDeviceAddress)) {
          getBaseActivity().startBleTimer();
          if (mIsAdmin && mIsMoreThanOneUser) {
            mLockSettingPresenter.unbindLockOfAdmin(mSelecetLock, mSelectedLockUser.getUserUniqueKey());
          } else {
            mLockSettingPresenter.unbindLock(mSelecetLock);
          }
        } else {
          ToastUtils.showShort("解绑门锁失败");
        }
      }
    }
  };

}
