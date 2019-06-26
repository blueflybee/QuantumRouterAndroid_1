package com.fernandocejas.android10.sample.presentation.view.device.lock.fragment;

import android.os.Handler;
import android.text.TextUtils;

import com.blueflybee.blelibrary.BleGattCharacteristic;
import com.blueflybee.blelibrary.BleGattService;
import com.blueflybee.blelibrary.BleRequest;
import com.blueflybee.blelibrary.IBle;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.presentation.view.device.lock.utils.BleLockUtils;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;

import java.util.UUID;

import static com.blueflybee.blelibrary.BleRequest.CmdType.UNBIND_LOCK;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2018/03/06
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class UnbindLockFragment extends BaseFragment {
  protected String mDeviceAddress = "";
  protected BleLock mBleLock;
  protected BlePkg mCurrentPkg;
  protected BleRequest.CmdType mCurrentCmdType;

  protected void unbindLockBle() {
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
        pkg.setKeyId(BleMapper.NO_ENCRYPTION);
        pkg.setUserId(PrefConstant.getUserIdInHexString());
        pkg.setSeq("00");
        BleBody body = new BleBody();
        body.setCmd(BleMapper.BLE_CMD_UNBIND_LOCK);
        body.setPayload(PrefConstant.getUserIdInHexString());
        pkg.setBody(body);
        sendBlePkg(pkg, UNBIND_LOCK);
      }
    }, 2000);
  }

  protected void sendBlePkg(BlePkg pkg, BleRequest.CmdType cmdType) {
    mCurrentPkg = pkg;
    mCurrentCmdType = cmdType;
    IBle ble = getBaseActivity().getBle();

    if (ble == null || TextUtils.isEmpty(mDeviceAddress)) return;
    BleGattService service = ble.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
    if (service == null) {
      ble.requestConnect(mDeviceAddress, cmdType, 30);
      return;
    }
    BleGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

    ble.requestCharacteristicNotification(mDeviceAddress, characteristic);
    String data = new BleMapper().pkgToReqString(mCurrentPkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
    ble.requestWriteCharacteristicToLock(mCurrentCmdType, mDeviceAddress, characteristic, data, true);

  }
}
