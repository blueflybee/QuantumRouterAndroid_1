package com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.BindRouterToLockActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.SearchLockActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.utils.BleLockUtils;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.router.model.rsp.QueryBindRouterToLockResponse;

import java.util.List;

import static com.blueflybee.blelibrary.BleRequest.CmdType.BIND_ROUTER_ZIGBEE;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class LiteBindLockActivity extends BindRouterToLockActivity {

  @Override
  protected void initData() {
    super.initData();
    mDevTreeResponse = getLiteGw();
  }

  @Override
  protected void getDevTree() {
    mQDevPresenter.getDevTree(AppConstant.DEVICE_TYPE_LOCK);
  }

  @Override
  protected void initView() {
    super.initView();
    mTitleBar.hideRight();
    mBinding.ivDevice.setBackgroundResource(R.drawable.pic2);
    mBinding.tvContent.setText("您还未绑定门锁，请尽快绑定～");
    mAdapter.setOnBindBtnClickListener((view, lock) -> {
      if (!BleLockUtils.isBleEnable()) return;
      mDeviceAddress = lock.getMac();
      mBleLock = LockManager.getLock(getContext(), mDeviceAddress);
      if (TextUtils.isEmpty(mDeviceAddress) || mBleLock == null) {
        ToastUtils.showShort("该门锁不存在");
        return;
      }
      showLoading();
      pauseBleTimer();
      setUserUsingBle(true, mDeviceAddress);
      clearBleRequest();
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          BlePkg pkg = new BlePkg();
          pkg.setHead("aa");
          pkg.setLength("28");
          pkg.setKeyId(BleMapper.KEY_STORTE_ENCRYPTION);
          pkg.setUserId(PrefConstant.getUserIdInHexString());
          pkg.setSeq("00");
          BleBody body = new BleBody();
          body.setCmd(BleMapper.BLE_CMD_BIND_ROUTER_ZIGBEE);
          body.setPayload("");
          pkg.setBody(body);
          sendBlePkg(pkg, BIND_ROUTER_ZIGBEE);
        }
      }, 2000);

    });
  }

  @Override
  public void bind(View view) {
    mNavigator.navigateTo(getContext(), SearchLockActivity.class);
  }

  @Override
  public void skip(View view) {
    finish();
  }

  @Override
  public void onQuery(QueryBindRouterToLockResponse response) {
    if (BIND_LOCK_SUCCESS.equals(response.getEncrypdata())) {
      showBindSuccess();
    } else {
      showBindFail();
    }
  }

  @Override
  public void showBindSuccess() {
    DialogUtil.showSuccessDialog(getContext(), "绑定成功", () -> finish());
  }

  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> getLiteGw() {
    GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> data =
        (GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> ) getIntent().getSerializableExtra(Navigator.EXTRA_LITE_GATEWAY);
    return data == null ? new GetDevTreeResponse<>() : data;
  }
}
