package com.fernandocejas.android10.sample.presentation.view.device.lock.fragment;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentLockStatusBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DeviceComponent;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.adapter.LockExceptionWarnListAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.adapter.LockUseNoteListAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.fragment.GetUnlockInfoListPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.fragment.IGetUnlockInfoListView;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock.ExceptionWarnMoreActivity;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock.LockUseNoteMoreActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.BindRouterToLockActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.RemoteLockOperationPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.RemoteLockOperationView;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.lock.model.rsp.BleGetRemoteLockStatusResponse;
import com.qtec.mapp.model.req.GetUnlockInfoListRequest;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetUnlockInfoListResponse;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.router.model.rsp.GetLockStatusResponse;

import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc: 门锁状态
 *      version: 1.0
 * </pre>
 */
public class LockStatusFragment extends BaseFragment implements LockStatusView, RemoteLockOperationView, IGetUnlockInfoListView, View.OnClickListener {

  private static final int REQUEST_CODE_BIND_ROUTER = 101;

  private FragmentLockStatusBinding mBinding;

  @Inject
  LockStatusPresenter mLockStatusPresenter;

  @Inject
  GetUnlockInfoListPresenter mGetUnlockInfoListPresenter;

  @Inject
  RemoteLockOperationPresenter mRemoteLockOperationPresenter;
  private boolean mZigBee = false;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(DeviceComponent.class).inject(this);
  }

  public static LockStatusFragment newInstance() {
    LockStatusFragment fragment = new LockStatusFragment();
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_lock_status, container, false);

    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    initView();
    initPresenter();

    queryLockStatus();

    refreshOpenLockList();

  }

  private void refreshOpenLockList() {
    if (TextUtils.isEmpty(getLockBindRouterId())) {
      mBinding.rlLockUseMore.setVisibility(View.GONE);
      mBinding.rlLockUseList.setVisibility(View.GONE);

      mBinding.rlLockExpMore.setVisibility(View.GONE);
      mBinding.rlLockExpList.setVisibility(View.GONE);
    } else {
      mBinding.rlLockUseMore.setVisibility(View.VISIBLE);
      mBinding.rlLockUseList.setVisibility(View.VISIBLE);

      mBinding.rlLockExpMore.setVisibility(View.VISIBLE);
      mBinding.rlLockExpList.setVisibility(View.VISIBLE);

      queryLockUseNoteList();
      queryExceptionWarningList();
    }
  }

  private void initView() {
    mBinding.imgOpenRemoteDoor.setOnClickListener(this);
    mZigBee = false;

    mBinding.tvLockUseMore.setOnClickListener(this);
    mBinding.tvExceptionWarnMore.setOnClickListener(this);

    mBinding.llLockHasRouter.setVisibility(TextUtils.isEmpty(getLockBindRouterId()) ? View.GONE : View.VISIBLE);
    mBinding.rlLockNoRouter.setVisibility(TextUtils.isEmpty(getLockBindRouterId()) ? View.VISIBLE : View.GONE);
    mBinding.btnBind.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_BIND_ROUTER_TO_LOCK_ONLY, true);
      intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, getLock().getMac());
      intent.setClass(getContext(), BindRouterToLockActivity.class);
      startActivityForResult(intent, REQUEST_CODE_BIND_ROUTER);
    });

    mBinding.tvRefreshLockStatus.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        queryLockStatus();
      }
    });
  }

  private void initPresenter() {
    mLockStatusPresenter.setView(this);
    mGetUnlockInfoListPresenter.setView(this);
    mRemoteLockOperationPresenter.setView(this);
  }

  private void queryLockStatus() {
    if (TextUtils.isEmpty(getLockBindRouterId())) return;
    BlePkg pkg = new BlePkg();
    pkg.setHead("aa");
    pkg.setLength("0c");
    pkg.setKeyId(BleMapper.KEY_STORTE_ENCRYPTION);
    pkg.setUserId(PrefConstant.getUserIdInHexString());
    pkg.setSeq("00");
    BleBody body = new BleBody();
    body.setCmd(BleMapper.BLE_CMD_GET_LOCK_STATUS);
    body.setPayload("");
    pkg.setBody(body);
    mRemoteLockOperationPresenter.remoteOpt(getLockBindRouterId(), getLock().getDeviceSerialNo(), pkg);
  }

  /**
   * 门锁使用记录
   */
  private void queryLockUseNoteList() {
    GetUnlockInfoListRequest request = new GetUnlockInfoListRequest();
    request.setDeviceSerialNo(getLock().getDeviceSerialNo());
    request.setPageSize("4");
    request.setStart("0");
    request.setType("0");
    request.setRecordUniqueKey("-1");
    //request.setRouterSerialNo(getDeviceId());
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);
    mGetUnlockInfoListPresenter.getLockUseNoteList(encryptInfo);
  }

  /**
   * 异常状态提醒
   */
  private void queryExceptionWarningList() {
    GetUnlockInfoListRequest request = new GetUnlockInfoListRequest();
    request.setDeviceSerialNo(getLock().getDeviceSerialNo());
    request.setRecordUniqueKey("-1");
    request.setPageSize("4");
    request.setStart("0");
    request.setType("1");
    // request.setRouterSerialNo(getDeviceId());
    QtecEncryptInfo encryptInfo1 = new QtecEncryptInfo();
    encryptInfo1.setData(request);
    mGetUnlockInfoListPresenter.getExceptionWarningList(encryptInfo1);
  }

  private String getLockBindRouterId() {
    return getActivity().getIntent().getStringExtra(Navigator.EXTRA_LOCK_BIND_ROUTER_ID);
  }

  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> getLock() {
    return ((LockMainActivity) getActivity()).getLock();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.img_open_remote_door:
        remoteUnlock();
        break;

      case R.id.tv_lock_use_more:
        Intent intent = new Intent();
        intent.putExtra(Navigator.EXTRA_DEVICE_SERIAL_NO, getLock().getDeviceSerialNo());
        mNavigator.navigateTo(getActivity(), LockUseNoteMoreActivity.class, intent);
        break;

      case R.id.tv_exception_warn_more:
        Intent intent1 = new Intent();
        intent1.putExtra(Navigator.EXTRA_DEVICE_SERIAL_NO, getLock().getDeviceSerialNo());
        mNavigator.navigateTo(getActivity(), ExceptionWarnMoreActivity.class, intent1);
        break;

      default:
        break;

    }
  }

  private void remoteUnlock() {
    if (!mZigBee) return;
    DialogUtil.showLoginPasswordDialog(getContext(), "请输入登录密码用于远程开锁", (view, pwd) -> {
      mBinding.scrollLockStatus.smoothScrollTo(0, 0);
      String userPwd = PrefConstant.getUserPwd();
      String inputPwd = EncryptUtils.encryptMD5ToString(pwd).toLowerCase();
      if (!inputPwd.equals(userPwd)) {
        ToastUtils.showShort("密码错误");
        return;
      }
      mBinding.imgOpenRemoteDoor.setVisibility(View.GONE);
      mBinding.gifOpening.setVisibility(View.VISIBLE);
      BlePkg pkg = new BlePkg();
      pkg.setHead("aa");
      pkg.setLength("0c");
      pkg.setKeyId(BleMapper.NO_ENCRYPTION);
      pkg.setUserId(PrefConstant.getUserIdInHexString());
      pkg.setSeq("00");
      BleBody body = new BleBody();
      body.setCmd(BleMapper.BLE_CMD_GET_UNLOCK_RADOM_CODE_REMOTE);
      body.setPayload("");
      pkg.setBody(body);
      mRemoteLockOperationPresenter.remoteOpt(getLockBindRouterId(), getLock().getDeviceSerialNo(), pkg);
    });
  }

  @Override
  public void openLockUseNoteList(List<GetUnlockInfoListResponse> lockUseNoteList) {
    if (lockUseNoteList.size() == 0) {
      mBinding.lvLockUseNote.setEmptyView(getActivity().findViewById(R.id.include_lock_empty_view));
      return;
    }

    // 门锁使用记录
    mBinding.lvLockUseNote.setAdapter(new LockUseNoteListAdapter(getContext(), lockUseNoteList, R.layout.item_lock_use_note));
    mBinding.lvLockUseNote.setEnabled(false);
  }

  @Override
  public void openExceptionWarningList(List<GetUnlockInfoListResponse> exceptionWarnList) {
    if (exceptionWarnList.size() == 0) {
      mBinding.lvExceptionWarning.setEmptyView(getActivity().findViewById(R.id.include_exception_empty_view));
      return;
    }

    // 异常状态提醒
    mBinding.lvExceptionWarning.setAdapter(new LockExceptionWarnListAdapter(getContext(), exceptionWarnList, R.layout.item_exception_warn_list));
    mBinding.lvExceptionWarning.setEnabled(false);
  }

  /**
   * 门锁状态
   */
  @Override
  public void showLockStatus(GetLockStatusResponse response) {
    if (TextUtils.isEmpty(response.getStatus())) {
      return;
    }
    if ("1".equals(response.getStatus())) {
      //mBinding.tvPosLockStatus.setText("主锁：已上锁");
      mBinding.imgOpenRemoteDoor.setBackgroundResource(R.drawable.ic_door_close);
      mBinding.imgOpenRemoteDoor.setClickable(true);

    } else {
      //mBinding.tvPosLockStatus.setText("主锁：未上锁");
      mBinding.imgOpenRemoteDoor.setBackgroundResource(R.drawable.ic_door_open);
      mBinding.imgOpenRemoteDoor.setClickable(false);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
//    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {

      case REQUEST_CODE_BIND_ROUTER:
        if (resultCode == Activity.RESULT_OK) {
          String bindRouterId = data != null ? data.getStringExtra(Navigator.EXTRA_BINDED_ROUTER_ID) : "";
          if (TextUtils.isEmpty(bindRouterId)) return;
          getActivity().getIntent().putExtra(Navigator.EXTRA_LOCK_BIND_ROUTER_ID, bindRouterId);
          mBinding.llLockHasRouter.setVisibility(TextUtils.isEmpty(getLockBindRouterId()) ? View.GONE : View.VISIBLE);
          mBinding.rlLockNoRouter.setVisibility(TextUtils.isEmpty(getLockBindRouterId()) ? View.VISIBLE : View.GONE);
          queryLockStatus();
          refreshOpenLockList();
        }
        break;
    }
  }

  @Override
  public void showRemoteLockOptFailed(String cmdType) {

    switch (cmdType) {
      case BleMapper.BLE_CMD_GET_UNLOCK_RADOM_CODE_REMOTE:
      case BleMapper.BLE_CMD_UNLOCK_REMOTE:
        ToastUtils.showShort("开门失败，请重试");
        mBinding.gifOpening.setVisibility(View.GONE);
        mBinding.imgOpenRemoteDoor.setVisibility(View.VISIBLE);
        mBinding.imgOpenRemoteDoor.setBackgroundResource(R.drawable.ic_door_close);
        queryLockStatus();
        break;
      case BleMapper.BLE_CMD_GET_LOCK_STATUS:
        break;

      default:
        break;
    }
  }

  @Override
  public void showRemoteLockOptSuccess(BlePkg blePkg, String cmdType) {

    switch (cmdType) {

      case BleMapper.BLE_CMD_GET_UNLOCK_RADOM_CODE_REMOTE:
        String payload = blePkg.getBody().getPayload();
        System.out.println("payload = " + payload);
        if (TextUtils.isEmpty(payload) || payload.length() < 2) return;
        String rspCode = payload.substring(0, 2);
        if (checkBleRsp(rspCode)) return;
        String randomCode = payload.substring(2);
        BlePkg pkg = new BlePkg();
        pkg.setHead("aa");
        pkg.setLength("00");
        pkg.setKeyId(BleMapper.DEFAULT_ENCRYPTION_WHEN_UNLOCK);
        pkg.setUserId(PrefConstant.getUserIdInHexString());
        pkg.setSeq("00");
        BleBody body = new BleBody();
        body.setCmd(BleMapper.BLE_CMD_UNLOCK_REMOTE);
        body.setPayload(randomCode);
        pkg.setBody(body);
        mRemoteLockOperationPresenter.remoteOpt(getLockBindRouterId(), getLock().getDeviceSerialNo(), pkg);
        break;

      case BleMapper.BLE_CMD_UNLOCK_REMOTE:
        if (checkBleRsp(blePkg.getBody().getPayload())) return;
        mBinding.gifOpening.setVisibility(View.GONE);
        mBinding.imgOpenRemoteDoor.setVisibility(View.VISIBLE);
        mBinding.imgOpenRemoteDoor.setBackgroundResource(R.drawable.ic_door_open);
        mBinding.tvLockStatus.setText("未上锁");
        mBinding.tvLockStatus.setCompoundDrawablesWithIntrinsicBounds(
            null, getResources().getDrawable(R.drawable.lock_no), null, null);
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            if (!isAdded()) return;
            mBinding.imgOpenRemoteDoor.setBackgroundResource(R.drawable.ic_door_close);
            mBinding.tvLockStatus.setText("已上锁");
            mBinding.tvLockStatus.setCompoundDrawablesWithIntrinsicBounds(
                null, getResources().getDrawable(R.drawable.lock_h), null, null);
          }
        }, 5000);

        break;

      case BleMapper.BLE_CMD_GET_LOCK_STATUS:
        BleGetRemoteLockStatusResponse lockStatus = new BleMapper().getRemoteLockStatus(blePkg);
        if (checkBleRsp(lockStatus.getCode())) return;
        mZigBee = lockStatus.isZigBee();
        mBinding.tvNetStatus.setText(mZigBee ? "已联网" : "未联网");
        mBinding.imgOpenRemoteDoor.setBackgroundResource(mZigBee ? R.drawable.ic_door_close : R.drawable.ic_wrong);
        int top = mZigBee ? R.drawable.wifi_h : R.drawable.wifi_none;
        mBinding.tvNetStatus.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(top), null, null);

        mBinding.tvLockStatus.setText(mZigBee ? (lockStatus.isLock() ? "已上锁" : "未上锁") : "未知");
        top = mZigBee ? (lockStatus.isLock() ? R.drawable.lock_h : R.drawable.lock_no) : R.drawable.lock_none;
        mBinding.tvLockStatus.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(top), null, null);

        mBinding.tvBatteryStatus.setText(mZigBee ? (lockStatus.isBattery() ? "正常" : "低电量") : "未知");
        top = mZigBee ? (lockStatus.isBattery() ? R.drawable.ele_h : R.drawable.ele_no) : R.drawable.ele_none;
        mBinding.tvBatteryStatus.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(top), null, null);

        break;

      default:
        break;
    }


  }

  @Override
  public void onRefreshComplete() {
    mBinding.tvRefreshLockStatus.setText("刷新");
    mBinding.tvRefreshLockStatus.setClickable(true);
    mBinding.progressRefresh.setVisibility(View.GONE);
  }

  @Override
  public void onRefreshStart() {
    mBinding.tvRefreshLockStatus.setText("刷新中...");
    mBinding.tvRefreshLockStatus.setClickable(false);
    mBinding.progressRefresh.setVisibility(View.VISIBLE);
  }

  @Override
  public void onRefreshError() {
    mBinding.tvRefreshLockStatus.setText("刷新");
    mBinding.tvRefreshLockStatus.setClickable(true);
    mBinding.progressRefresh.setVisibility(View.GONE);
  }
}