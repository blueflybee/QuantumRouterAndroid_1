package com.fernandocejas.android10.sample.presentation.view.device.lock.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.blelibrary.BleGattCharacteristic;
import com.blueflybee.blelibrary.BleGattService;
import com.blueflybee.blelibrary.BleRequest;
import com.blueflybee.blelibrary.BleService;
import com.blueflybee.blelibrary.IBle;
import com.blueflybee.keystore.KeystoreRepertory;
import com.blueflybee.uilibrary1.wheelview.OnItemSelectedListener;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentLockSettingBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DeviceComponent;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock.AboutHardwareActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.BindRouterToLockActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockBindRouterInfoActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockCheckVersionActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockUserManageActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockUserPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockUserView;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.PassagewayModeActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.TempPwdActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.UnlockModeActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.UnlockModeActivityForV15;
import com.fernandocejas.android10.sample.presentation.view.device.lock.component.AdjustVolumePopup;
import com.fernandocejas.android10.sample.presentation.view.device.lock.constant.LockConstant;
import com.fernandocejas.android10.sample.presentation.view.device.lock.utils.BleLockUtils;
import com.fernandocejas.android10.sample.presentation.view.device.router.data.Router;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.RouterInfoPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.RouterInfoView;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.manageDevice.ModifyLockNameActivity;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.mapp.model.rsp.AdjustLockVolumeResponse;
import com.qtec.mapp.model.rsp.DeleteLockUserResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetLockUsersResponse;
import com.qtec.mapp.model.rsp.GetRouterInfoCloudResponse;
import com.qtec.mapp.model.rsp.GetUserRoleResponse;
import com.qtec.mapp.model.rsp.UnbindLockOfAdminResponse;
import com.qtec.mapp.model.rsp.UnbindRouterResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import static com.blueflybee.blelibrary.BleRequest.CmdType.ADJUST_VOLUME;
import static com.blueflybee.blelibrary.BleRequest.CmdType.UNBIND_LOCK;

/**
 * <pre>
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc: 门锁基础设置
 *      version: 1.0
 * </pre>
 */
public class LockSettingFragment extends UnbindLockFragment implements LockSettingView, RouterInfoView, LockUserView {

  private static final String TAG = LockSettingFragment.class.getSimpleName();

  private static final int REQUEST_CODE_UNBIND_ROUTER = 100;
  private static final int REQUEST_CODE_BIND_ROUTER = 101;
  private static final int REQUEST_CODE_MODIFY_LOCK_NAME = 102;
  private FragmentLockSettingBinding mBinding;

  @Inject
  LockSettingPresenter mLockSettingPresenter;

  @Inject
  LockUserPresenter mLockUserPresenter;

  @Inject
  RouterInfoPresenter mRouterInfoPresenter;

  private String mNewLockName;
  private String mVolume;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(DeviceComponent.class).inject(this);
  }

  public static LockSettingFragment newInstance() {
    LockSettingFragment fragment = new LockSettingFragment();
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_lock_setting, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initData();
    initView();
    initPresenter();
    getRouterInfo();
    getUserRole();
    mLockSettingPresenter.getLockVolume(getLock().getDeviceSerialNo());
  }

  private void initData() {
    mDeviceAddress = getLock().getMac();
    mBleLock = LockManager.getLock(getContext(), mDeviceAddress);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mLockUserPresenter.destroy();
    mLockSettingPresenter.destroy();
    mRouterInfoPresenter.destroy();
  }

  private void getUserRole() {
    mLockUserPresenter.getUserRole(getLock().getDeviceSerialNo());
  }

  private void getRouterInfo() {
    if (!TextUtils.isEmpty(getLockBindRouterId())) {
      mRouterInfoPresenter.getRouterInfoCloud(getLockBindRouterId());
    }
  }

  private void initPresenter() {
    mLockSettingPresenter.setView(this);
    mRouterInfoPresenter.setView(this);
    mLockUserPresenter.setView(this);
  }

  private void initView() {

    String lockName = getLock().getDeviceName();
    mBinding.tvLockName.setText(TextUtils.isEmpty(mNewLockName) ? lockName : mNewLockName);

    mIsAdmin = GetDevTreeResponse.ADMIN.equals(getLock().getUserRole());
    initAdminUI(mIsAdmin);

    mBinding.rlBindDevice.setOnClickListener(v -> {
      if (TextUtils.isEmpty(getLockBindRouterId())) {
        Intent intent = new Intent();
        intent.putExtra(Navigator.EXTRA_BIND_ROUTER_TO_LOCK_ONLY, true);
        intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, getLock().getMac());
        intent.setClass(getContext(), BindRouterToLockActivity.class);
        startActivityForResult(intent, REQUEST_CODE_BIND_ROUTER);
      } else {
        Intent intent = new Intent();
        intent.putExtra(Navigator.EXTRA_LOCK_BIND_ROUTER_ID, getLockBindRouterId());
        intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, getLock().getMac());
        intent.setClass(getContext(), LockBindRouterInfoActivity.class);
        startActivityForResult(intent, REQUEST_CODE_UNBIND_ROUTER);
      }

    });
    mBinding.btnUnbindLock.setOnClickListener(v -> {
      if (mIsAdmin) {
        mLockUserPresenter.getLockUsers(getLock().getDeviceSerialNo());
      } else {
        DialogUtil.showConfirmCancelDialog(getContext(), "提示", "确定要解绑门锁吗？", "确定", "取消", new View.OnClickListener() {
          @Override
          public void onClick(View v1) {
            unbindLockBle();
          }
        }, null);
      }

    });

    mBinding.rlUnlockMode.setOnClickListener(v -> {
      String lockVersion = getLock().getDeviceVersion();
      if (LockConstant.LOCK_VERSION_16.compareToIgnoreCase(lockVersion) >= 0) {
        Intent intent = new Intent();
        intent.putExtra(Navigator.EXTRA_BLE_LOCK, getLock());
        mNavigator.navigateTo(getActivity(), UnlockModeActivityForV15.class, intent);
      } else {
        Intent intent = new Intent();
        intent.putExtra(Navigator.EXTRA_BLE_LOCK, getLock());
        mNavigator.navigateTo(getActivity(), UnlockModeActivity.class, intent);
      }
    });

    mBinding.rlPassagewayMode.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_BLE_LOCK, getLock());
      mNavigator.navigateTo(getActivity(), PassagewayModeActivity.class, intent);
    });

    mBinding.rlDeviceName.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_MODIFY_PROPERTY, getText(mBinding.tvLockName));
      intent.putExtra(Navigator.EXTRA_MODIFY_HINT, "请输入设备名称");
      intent.putExtra(Navigator.EXTRA_BLE_LOCK, getLock());
      intent.setClass(getContext(), ModifyLockNameActivity.class);
      startActivityForResult(intent, REQUEST_CODE_MODIFY_LOCK_NAME);
    });

    mBinding.rlUserManage.setOnClickListener(v -> {
      DialogUtil.showLoginPasswordDialog(getContext(), "请输入登录密码", (view, pwd) -> {
        String inputPwd = EncryptUtils.encryptMD5ToString(pwd).toLowerCase();
        String userPwd = PrefConstant.getUserPwd();
        if (!inputPwd.equals(userPwd)) {
          ToastUtils.showShort("密码错误");
          return;
        }
        Intent intent = new Intent();
        intent.putExtra(Navigator.EXTRA_BLE_LOCK, getLock());
        mNavigator.navigateTo(getActivity(), LockUserManageActivity.class, intent);
      });

    });

    mBinding.rlAboutHardware.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_BLE_LOCK, getLock());
      mNavigator.navigateTo(getActivity(), AboutHardwareActivity.class, intent);
    });

    mBinding.rlLockFirmwareUpdate.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, getLock().getMac());
      mNavigator.navigateTo(getActivity(), LockCheckVersionActivity.class, intent);
    });

    mBinding.rlTempPwd.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_BLE_LOCK, getLock());
      mNavigator.navigateTo(getActivity(), TempPwdActivity.class, intent);
    });

    mBinding.rlVolume.setOnClickListener(v -> {
      String[] items = {"1", "2", "3", "4", "5", "6", "7", "8"};
      String volume = getText(mBinding.tvVolume);
      int initPosition = 0;
      for (int i = 0; i < items.length; i++) {
        if (!items[i].equals(volume)) continue;
        initPosition = i;
        break;
      }
      AdjustVolumePopup popup = new AdjustVolumePopup(getActivity(), items);
      popup.setInitPosition(initPosition);
      popup.setOnFinishClickListener(volume1 -> {
        mVolume = volume1;
        adjustVolume(volume1);
//        ToastUtils.showShort(mVolume);
      });

      popup.showAtLocation(mBinding.llOuterLayout, Gravity.BOTTOM, 0, 0);

    });

    String lockVersion = getLock().getDeviceVersion();
    if (LockConstant.LOCK_VERSION_21.compareToIgnoreCase(lockVersion) > 0) {
      mBinding.rlVolume.setVisibility(View.GONE);
      mBinding.lineVolume.setVisibility(View.GONE);
      mBinding.rlTempPwd.setVisibility(View.GONE);
      mBinding.lineTempPwd.setVisibility(View.GONE);
    } else {
      mBinding.rlVolume.setVisibility(View.VISIBLE);
      mBinding.lineVolume.setVisibility(View.VISIBLE);
      mBinding.rlTempPwd.setVisibility(View.VISIBLE);
      mBinding.lineTempPwd.setVisibility(View.VISIBLE);
    }
  }

  protected void adjustVolume(String volume) {
    if (!BleLockUtils.isBleEnable()) return;
    showLoading();
    getBaseActivity().pauseBleTimer();
    getBaseActivity().setUserUsingBle(true, mDeviceAddress);
    getBaseActivity().clearBleRequest();
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        BlePkg pkg = new BlePkg();
        pkg.setHead("aa");
        pkg.setLength("0c");
        pkg.setKeyId(BleMapper.KEY_STORTE_ENCRYPTION);
        pkg.setUserId(PrefConstant.getUserIdInHexString());
        pkg.setSeq("00");
        BleBody body = new BleBody();
        body.setCmd(BleMapper.BLE_CMD_ADJUST_VOLUME);
        body.setPayload("0" + volume);
        pkg.setBody(body);
        sendBlePkg(pkg, ADJUST_VOLUME);
      }
    }, 2000);
  }

  private void initAdminUI(boolean isAdmin) {
    mBinding.rlUserManage.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
    mBinding.lineUserManager.setVisibility(isAdmin ? View.VISIBLE : View.GONE);

    mBinding.rlUnlockMode.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
    mBinding.lineUnlockMode.setVisibility(isAdmin ? View.VISIBLE : View.GONE);

    String lockVersion = getLock().getDeviceVersion();
    boolean highThan16 = LockConstant.LOCK_VERSION_16.compareToIgnoreCase(lockVersion) < 0;
    mBinding.rlPassagewayMode.setVisibility((isAdmin && highThan16) ? View.VISIBLE : View.GONE);
    mBinding.linePassagewayMode.setVisibility((isAdmin && highThan16) ? View.VISIBLE : View.GONE);
  }

  private String getLockBindRouterId() {
    return getActivity().getIntent().getStringExtra(Navigator.EXTRA_LOCK_BIND_ROUTER_ID);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    //super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case REQUEST_CODE_UNBIND_ROUTER:
        if (resultCode == Activity.RESULT_OK) {
          mBinding.tvBindDevName.setText("");
          getActivity().getIntent().putExtra(Navigator.EXTRA_LOCK_BIND_ROUTER_ID, "");
        }
        break;

      case REQUEST_CODE_BIND_ROUTER:
        if (resultCode == Activity.RESULT_OK) {
          String bindRouterId = data != null ? data.getStringExtra(Navigator.EXTRA_BINDED_ROUTER_ID) : "";
          if (TextUtils.isEmpty(bindRouterId)) return;
          getActivity().getIntent().putExtra(Navigator.EXTRA_LOCK_BIND_ROUTER_ID, bindRouterId);
          getRouterInfo();
        }
        break;

      case REQUEST_CODE_MODIFY_LOCK_NAME:
        if (resultCode != Activity.RESULT_OK) return;
        mNewLockName = data.getStringExtra(Navigator.EXTRA_NEW_LOCK_NAME);
        mBinding.tvLockName.setText(mNewLockName);
        ((LockMainActivity) getActivity()).setNewLockName(mNewLockName);
        break;
    }
  }

  @Override
  public void unbindLockSuccess(UnbindRouterResponse response, String mac) {
    BleLock lock = LockManager.getLock(getActivity(), getLock().getMac());
    KeystoreRepertory.getInstance().clear(lock.getKeyRepoId());
    LockManager.delete(getActivity(), getLock().getMac());
    mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class);
    ToastUtils.showLong("门锁解除绑定成功");
  }

  @Override
  public void unbindLockOfAdminSuccess(UnbindLockOfAdminResponse response, String mac) {
    BleLock lock = LockManager.getLock(getActivity(), getLock().getMac());
    KeystoreRepertory.getInstance().clear(lock.getKeyRepoId());
    LockManager.delete(getActivity(), getLock().getMac());
    mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class);
    ToastUtils.showLong("门锁解除绑定成功");
  }

  @Override
  public void showLockVolume(String volume) {
    mBinding.tvVolume.setText(volume);
  }

  @Override
  public void onAdjustLockVolume(AdjustLockVolumeResponse response) {
    ToastUtils.showShort("门锁音量设置成功");
    mBinding.tvVolume.setText(mVolume);
  }

  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> getLock() {
    return ((LockMainActivity) getActivity()).getLock();
  }

  @Override
  public void showRouterInfo(Router.BaseInfo baseInfo) {
  }

  @Override
  public void showRouterInfoCloud(GetRouterInfoCloudResponse baseInfo) {
    mBinding.tvBindDevName.setText(baseInfo.getRouterNickName());
  }

  @Override
  public void showUserRole(GetUserRoleResponse response) {
    mIsAdmin = GetUserRoleResponse.ADMIN.equals(response.getUserRole());
    initAdminUI(mIsAdmin);
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
  public void onResume() {
    super.onResume();
    getContext().registerReceiver(mBleReceiver, BleService.getIntentFilter());
  }

  @Override
  public void onPause() {
    super.onPause();
    getContext().unregisterReceiver(mBleReceiver);
  }


  @Override
  public void onDeleteLockUserSuccess(DeleteLockUserResponse response) {
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
        BleRequest.CmdType bleCmdType = getBaseActivity().getBleCmdType(intent);
        getBaseActivity().handleBleDisconnect(bleCmdType != UNBIND_LOCK && bleCmdType != ADJUST_VOLUME, false, mDeviceAddress);
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
        BleRequest.CmdType bleCmdType = getBaseActivity().getBleCmdType(intent);
        getBaseActivity().handleBleFail(intent, bleCmdType != UNBIND_LOCK && bleCmdType != ADJUST_VOLUME, false, mDeviceAddress);
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
            mLockSettingPresenter.unbindLockOfAdmin(getLock().getDeviceSerialNo(), mSelectedLockUser.getUserUniqueKey());
          } else {
            mLockSettingPresenter.unbindLock(getLock().getDeviceSerialNo());
          }
        } else {
          ToastUtils.showShort("解绑门锁失败");
        }
      } else if (BleService.BLE_ADJUST_VOLUME.equals(action)) {
        getBaseActivity().setUserUsingBle(false, mDeviceAddress);
        hideLoading();
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BlePkg blePkg = bleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());

        String payload = bleMapper.payload(blePkg);
        if (TextUtils.isEmpty(payload)) {
          ToastUtils.showShort("门锁音量设置失败");
          return;
        }

        if (!getBaseActivity().bleRspFail(payload, true, mDeviceAddress)) {
          getBaseActivity().startBleTimer();
          mLockSettingPresenter.adjustLockVolume(getLock().getDeviceSerialNo(), mVolume);
        } else if (BleBody.RSP_CONFIG_MODE_NOT_OPEN.equals(payload)) {
          DialogUtil.showConfirmDialog(getContext(), "门锁未在配置状态", "请触碰门锁背后的按钮开启配置状态。", R.drawable.ic_buttonlock, null);
        } else {
          ToastUtils.showShort("门锁音量设置失败");
        }
      }
    }
  };
}