package com.fernandocejas.android10.sample.presentation.view.device.adapter;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.keystore.KeystoreRepertory;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ItemDevChildViewBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ItemDevParentViewBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.device.camera.activity.CameraMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.camera.activity.CameraSettingActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.CatEyeSettingActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.DoorBellRecordListActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.activity.CatEyeMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock.LockUseNoteMoreActivity;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity.LiteBindLockActivity;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity.LiteGatewayMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity.LiteGatewaySettingActivity;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.data.LiteGateway;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockMatchPinActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.loadkey.MatchPinActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.adapter.ApplyGridAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.constant.MenuConstant;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.obj.ApplyMenu;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.obj.MenuManager;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse.DeviceBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DevExpandAdapter extends BaseExpandableListAdapter {


  public static final String EMPTY_DEVICE = "empty device";
  private List<GetDevTreeResponse<List<DeviceBean>>> mRouters;
  private LayoutInflater mInflater;
  private Context mContext;
  private OnShareDeviceClickListener mOnShareDeviceClickListener;

  private OnUnbindRouterClickListener mOnUnbindDeviceClickListener;
  private OnUnbindLockClickListener mOnUnbindLockClickListener;
  private OnRouterGroupViewClickListener mOnRouterGroupViewClickListener;
  private OnRouterGridViewClickListener mOnRouterGridViewClickListener;

  private int mCurrentConnectedPosition = -1;

  private ApplyGridAdapter mApplyGridAdapter;
  private MenuManager mMenuManager = new MenuManager();

  public DevExpandAdapter(Context context, List<GetDevTreeResponse<List<DeviceBean>>> routers) {
    this.mContext = context;
    mRouters = addChildHeadData(routers);
    mInflater = LayoutInflater.from(context);
  }

  @Override
  public int getGroupCount() {
    return mRouters.size();
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return getGroup(groupPosition).getDeviceList().size();
  }

  @Override
  public GetDevTreeResponse<List<DeviceBean>> getGroup(int groupPosition) {
    return mRouters.get(groupPosition);
  }

  @Override
  public DeviceBean getChild(int groupPosition, int childPosition) {
    return getGroup(groupPosition).getDeviceList().get(childPosition);
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
    final ParentViewHolder holder;
    if (convertView == null) {
      ItemDevParentViewBinding binding = DataBindingUtil.inflate(mInflater, R.layout.item_dev_parent_view, parent, false);
      holder = new ParentViewHolder(binding);
      convertView = binding.getRoot();
      convertView.setTag(holder);
    } else {
      holder = (ParentViewHolder) convertView.getTag();
    }

    final GetDevTreeResponse<List<DeviceBean>> group = getGroup(groupPosition);
    holder.bind(group);

    if (isExpanded) {
      holder.binding.ivIndicator.setImageResource(R.drawable.indexic_arrowopen);
    } else {
      holder.binding.ivIndicator.setImageResource(R.drawable.indexic_arrowclose);
    }

    if (LiteGateway.DEVICE_TYPE_LITE_GATEWAY.equals(group.getDeviceType())) {
      holder.binding.ivIndicator.setVisibility(View.INVISIBLE);
    } else {
      holder.binding.ivIndicator.setVisibility(View.VISIBLE);
    }


    if ("0".equals(group.getDeviceType())) {
      holder.binding.ivDevIcon.setImageResource(R.drawable.indexic_router);
    } else if (AppConstant.DEVICE_TYPE_LOCK.equals(group.getDeviceType())) {
      holder.binding.ivDevIcon.setImageResource(R.drawable.indexic_doorlock);
    } else if ("2".equals(group.getDeviceType())) {
      holder.binding.ivDevIcon.setImageResource(R.drawable.logo_cat);
      devAddToServer(group.getDeviceSerialNo());//获取到猫眼就添加到服务器
    } else if ("3".equals(group.getDeviceType())) {
      holder.binding.ivDevIcon.setImageResource(R.drawable.logo_video);
      devAddToServer(group.getDeviceSerialNo());//获取到摄像机就添加到服务器
    } else if (LiteGateway.DEVICE_TYPE_LITE_GATEWAY.equals(group.getDeviceType())) {
      holder.binding.ivDevIcon.setImageResource(R.drawable.lite);
    }

    if (AppConstant.DEVICE_TYPE_LOCK.equals(group.getDeviceType())) {
      holder.binding.ivShare.setVisibility(GetDevTreeResponse.ADMIN.equals(group.getUserRole()) ? View.VISIBLE : View.GONE);
    } else {
      holder.binding.ivShare.setVisibility(View.VISIBLE);
    }

    if (LiteGateway.DEVICE_TYPE_LITE_GATEWAY.equals(group.getDeviceType())) {
      holder.binding.ivShare.setImageResource(R.drawable.ic_set);
      holder.binding.ivShare.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent();
          intent.putExtra(Navigator.EXTRA_LITE_GATEWAY, group);
          new Navigator().navigateTo(mContext, LiteGatewaySettingActivity.class, intent);
        }
      });
    } else {
      holder.binding.ivShare.setImageResource(R.drawable.indexic_share);
      holder.binding.ivShare.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //分享设备
          if (mOnShareDeviceClickListener != null) {
            mOnShareDeviceClickListener.onShareDeviceClick(groupPosition, mRouters.get(groupPosition).getDeviceSerialNo());
          }
        }
      });
    }

    holder.binding.rvLeft.setOnClickListener(v -> {
      String keyRepoId = group.getDeviceSerialNo() + "_" + PrefConstant.getUserUniqueKey(group.getDeviceType());
      GlobleConstant.setgDeviceType(group.getDeviceType());
      if ("0".equals(group.getDeviceType())) {
        if (mOnRouterGroupViewClickListener != null) {
          mOnRouterGroupViewClickListener.onClick(group, groupPosition, mCurrentConnectedPosition);
        }
      } else if ("1".equals(group.getDeviceType())) {
        if (KeystoreRepertory.getInstance().has(keyRepoId)) {
          goLockMainActivity(group);
        } else if (BluetoothAdapter.getDefaultAdapter() != null && BluetoothAdapter.getDefaultAdapter().isEnabled()) {
          Intent intent = new Intent();
          intent.putExtra(Navigator.EXTRA_ONLY_INJECT_KEY, true);
          intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, group.getMac());
          new Navigator().navigateTo(mContext, LockMatchPinActivity.class, intent);
        } else {
          ToastUtils.showShort("请先打开蓝牙载入密钥");
        }
      } else if ("2".equals(group.getDeviceType())) {
        //猫眼
        GlobleConstant.setgCatEyeId(group.getDeviceSerialNo());
        GlobleConstant.setgCatPwd(getDevicePwd(group));
        new Navigator().navigateTo(mContext, CatEyeMainActivity.class);

      } else if ("3".equals(group.getDeviceType())) {
        //摄像机
        GlobleConstant.setgCameraId(group.getDeviceSerialNo());
        GlobleConstant.setgCameraPwd(getDevicePwd(group));
        new Navigator().navigateTo(mContext, CameraMainActivity.class);

      } else if (LiteGateway.DEVICE_TYPE_LITE_GATEWAY.equals(group.getDeviceType())) {
        List<DeviceBean> deviceList = group.getDeviceList();
        Intent intent = new Intent();
        intent.putExtra(Navigator.EXTRA_LITE_GATEWAY, group);
        if (deviceList.size() <= 1) {
          new Navigator().navigateTo(mContext, LiteBindLockActivity.class, intent);
        } else {
          new Navigator().navigateTo(mContext, LiteGatewayMainActivity.class, intent);
        }
      }

    });

    holder.binding.tvStatus.setVisibility(View.VISIBLE);
    if (LiteGateway.DEVICE_TYPE_LITE_GATEWAY.equals(group.getDeviceType())) {
      holder.binding.tvStatus.setText("");
    } else {
      holder.binding.tvStatus.setText(group.getDeviceSerialNo());
    }

    holder.binding.tvCurrentConnect.setVisibility(mCurrentConnectedPosition == groupPosition ? View.VISIBLE : View.GONE);


    return convertView;
  }

  private void devAddToServer(String serialNo) {
    System.out.println("添加设备到服务器： " + serialNo);
    String[] ystArray = {serialNo};
    SovUtil.addYSTNOS(ystArray);
  }

  @Override
  public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    ChildViewHolder holder;
    if (convertView == null) {
      ItemDevChildViewBinding binding = DataBindingUtil.inflate(mInflater, R.layout.item_dev_child_view, parent, false);
      holder = new ChildViewHolder(binding);
      convertView = binding.getRoot();
      convertView.setTag(holder);
    } else {
      holder = (ChildViewHolder) convertView.getTag();
    }

    GetDevTreeResponse<List<DeviceBean>> group = getGroup(groupPosition);
    String routerId = group.getDeviceSerialNo();
    String keyRepoId = routerId + "_" + PrefConstant.getUserUniqueKey(group.getDeviceType());

    if (KeystoreRepertory.getInstance().has(keyRepoId)) {
      holder.binding.rvDevFragInjectKey.setVisibility(View.GONE);
      holder.binding.rvDevFragRouter.setVisibility(childPosition == 0 ? View.VISIBLE : View.GONE);
      holder.binding.rvDevFragDev.setVisibility(childPosition == 0 ? View.GONE : View.VISIBLE);
    } else {
      holder.binding.rvDevFragInjectKey.setVisibility(childPosition == 0 ? View.VISIBLE : View.GONE);
      holder.binding.rvDevFragRouter.setVisibility(View.GONE);
      holder.binding.rvDevFragDev.setVisibility(View.GONE);
    }

    int keyPercent = KeystoreRepertory.getInstance().getKeyPercent(keyRepoId);
    holder.binding.progressKeyUsage.setProgress(keyPercent);
    holder.binding.tvProgress.setText(keyPercent + "%");


    if ("0".equals(group.getDeviceType())) {
      holder.binding.rlKeyUsage.setVisibility(View.VISIBLE);
      ArrayList<ApplyMenu> mineMenus = mMenuManager.loadMenus(MenuConstant.APPLY_MINE, getMineMenuKey(routerId));
      mineMenus.add(new ApplyMenu("更多", "1000013", "gd", "", 12, true, R.drawable.indexic_more, 0));
      System.out.println("mineMenus = " + mineMenus);
      ApplyGridAdapter adapter = new ApplyGridAdapter(mContext, mineMenus);
      holder.binding.gvTools.setAdapter(adapter);
      holder.binding.gvTools.setOnItemClickListener((parent1, view, position, id) -> {
        GlobleConstant.setgDeviceId(routerId);
        GlobleConstant.setgKeyRepoId(keyRepoId);
        GlobleConstant.setgDeviceType(group.getDeviceType());
        if (mOnRouterGridViewClickListener != null) {
          mOnRouterGridViewClickListener.onClick(
              mRouters.get(groupPosition), adapter.getItem(position), groupPosition, position, mCurrentConnectedPosition);
        }
      });
    } else if ("1".equals(group.getDeviceType())) {
      holder.binding.rlKeyUsage.setVisibility(View.VISIBLE);
      holder.binding.gvTools.setAdapter(new DevToolsAdapter(mContext, DevToolsAdapter.getLockTools()));
      holder.binding.gvTools.setOnItemClickListener((parent1, view, position, id) -> {
        Intent intent;
        switch (position) {
          case 0:
            goLockMainActivity(group);
            break;
          case 1:
            intent = new Intent();
            intent.putExtra(Navigator.EXTRA_LOCK_PAGE, 1);
            intent.putExtra(Navigator.EXTRA_DEVICE_SERIAL_NO, group.getDeviceSerialNo());
            intent.putExtra(Navigator.EXTRA_DEVICE_NAME, group.getDeviceName());
            intent.putExtra(Navigator.EXTR_ROUTER_SERIAL_NUM, group.getDeviceSerialNo());
            intent.putExtra(Navigator.EXTRA_LOCK_BIND_ROUTER_ID, group.getRouterSerialNo());
            intent.putExtra(Navigator.EXTRA_BLE_LOCK, group);
            new Navigator().navigateTo(mContext, LockMainActivity.class, intent);
            break;

          case 2:
            goLockMainActivity(group);
            break;

          case 3:
            if (TextUtils.isEmpty(group.getRouterSerialNo())) {
              goLockMainActivity(group);
            } else {
              Intent intent3 = new Intent();
              intent3.putExtra(Navigator.EXTRA_DEVICE_SERIAL_NO, mRouters.get(groupPosition).getDeviceSerialNo());
              new Navigator().navigateTo(mContext, LockUseNoteMoreActivity.class, intent3);
            }

            break;

        }
      });

    } else if ("2".equals(group.getDeviceType())) {
      holder.binding.rvDevFragRouter.setVisibility(View.VISIBLE);
      holder.binding.rvDevFragInjectKey.setVisibility(View.GONE);
      holder.binding.rlKeyUsage.setVisibility(View.GONE);

      holder.binding.gvTools.setAdapter(new DevToolsAdapter(mContext, DevToolsAdapter.getCatEyeTools()));
      holder.binding.gvTools.setOnItemClickListener((parent1, view, position, id) -> {
        GlobleConstant.setgCatEyeId(group.getDeviceSerialNo());
        GlobleConstant.setgCatPwd(getDevicePwd(group));
        GlobleConstant.setgDeviceType(group.getDeviceType());
        switch (position) {
          case 0:
            //视频连接
            Intent intent = new Intent(mContext, CatEyeMainActivity.class);
            mContext.startActivity(intent);
            break;

          case 1:
            Toast.makeText(mContext, "开启对讲中……", Toast.LENGTH_SHORT).show();
            break;

          case 2:
            new Navigator().navigateTo(mContext, CatEyeMainActivity.class);
            break;

          case 3:
            Intent intent3 = new Intent(mContext, CatEyeSettingActivity.class);
            intent3.putExtra("isConnected", false);
            mContext.startActivity(intent3);
            break;
        }
      });
    } else if (AppConstant.DEVICE_TYPE_CAMERA.equals(group.getDeviceType())) {
      holder.binding.rvDevFragRouter.setVisibility(View.VISIBLE);
      holder.binding.rvDevFragInjectKey.setVisibility(View.GONE);
      holder.binding.rlKeyUsage.setVisibility(View.GONE);

      holder.binding.gvTools.setAdapter(new DevToolsAdapter(mContext, DevToolsAdapter.getCameraTools()));
      holder.binding.gvTools.setOnItemClickListener((parent1, view, position, id) -> {
        GlobleConstant.setgCameraId(group.getDeviceSerialNo());
        GlobleConstant.setgCameraPwd(getDevicePwd(group));
        GlobleConstant.setgDeviceType(group.getDeviceType());
        switch (position) {
          case 0:
            //视频连接
            Intent intent = new Intent(mContext, CameraMainActivity.class);
            mContext.startActivity(intent);
            break;

          case 1:
            Toast.makeText(mContext, "待开发...", Toast.LENGTH_SHORT).show();
            break;

          case 2:
            /*new Navigator().navigateTo(mContext, DoorBellRecordListActivity.class);*/
            Toast.makeText(mContext, "待开发...", Toast.LENGTH_SHORT).show();
            break;

          case 3:
            Intent intent3 = new Intent(mContext, CameraSettingActivity.class);
            intent3.putExtra("isConnected", false);
            mContext.startActivity(intent3);
            break;
        }
      });
    } else if (LiteGateway.DEVICE_TYPE_LITE_GATEWAY.equals(group.getDeviceType())) {
      holder.binding.rvDevFragRouter.setVisibility(View.GONE);
      holder.binding.rvDevFragInjectKey.setVisibility(View.GONE);
      holder.binding.rlKeyUsage.setVisibility(View.GONE);
    }


    if ("0".equals(group.getDeviceType())) {
      if (mCurrentConnectedPosition == groupPosition) {
        holder.binding.tvInjectKey.setText("您的网络匹配成功，可以轻松载入密钥");
        holder.binding.btnInjectKey.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      } else {
        holder.binding.tvInjectKey.setText("当前网络与网关网络不一致，请重新配置网络！");
        holder.binding.btnInjectKey.setBackgroundColor(mContext.getResources().getColor(R.color.white_bbdefb));
      }

      holder.binding.btnInjectKey.setOnClickListener(v -> {
        GlobleConstant.setgDeviceId(routerId);
        GlobleConstant.setgKeyRepoId(keyRepoId);
        GlobleConstant.setgDeviceType(group.getDeviceType());
        new Navigator().navigateTo(mContext, MatchPinActivity.class);
      });
      holder.binding.btnInjectKey.setClickable(mCurrentConnectedPosition == groupPosition);

      holder.binding.tvUnbindDevice.setText("解绑网关 >");
      holder.binding.tvUnbindDevice.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (mOnUnbindDeviceClickListener != null) {
            mOnUnbindDeviceClickListener.onClick(group);
          }
        }
      });
    } else if ("1".equals(group.getDeviceType())) {
      BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
      if (defaultAdapter == null || !defaultAdapter.isEnabled()) {
        holder.binding.tvInjectKey.setText("您还未打开蓝牙，请打开蓝牙载入密钥");
        holder.binding.btnInjectKey.setBackgroundColor(mContext.getResources().getColor(R.color.white_bbdefb));
        holder.binding.btnInjectKey.setClickable(false);
      } else {
        holder.binding.tvInjectKey.setText("您的蓝牙已打开，可以轻松载入密钥");
        holder.binding.btnInjectKey.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
        holder.binding.btnInjectKey.setClickable(true);
      }

      holder.binding.btnInjectKey.setOnClickListener(v -> {
        GlobleConstant.setgDeviceType(group.getDeviceType());
        Intent intent = new Intent();
        intent.putExtra(Navigator.EXTRA_ONLY_INJECT_KEY, true);
        intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, group.getMac());
        new Navigator().navigateTo(mContext, LockMatchPinActivity.class, intent);
      });

      holder.binding.btnInjectKey.setClickable(!(defaultAdapter == null || !defaultAdapter.isEnabled()));

      holder.binding.tvUnbindDevice.setText("解绑门锁 >");
      holder.binding.tvUnbindDevice.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (mOnUnbindLockClickListener != null) {
            mOnUnbindLockClickListener.onClick(group);
          }
        }
      });
    }

    holder.bind(getChild(groupPosition, childPosition));

    return convertView;
  }

  @NonNull
  private String getMineMenuKey(String routerId) {
    return MenuConstant.APPLY_MINE + "_" + routerId + "_" + PrefConstant.getUserUniqueKey("0");
  }

  /**
   * 获取猫眼摄像头的视频流密码
   *
   * @param
   * @return
   */
  private String getDevicePwd(GetDevTreeResponse<List<DeviceBean>> group) {
    String devicePwd = "";
    if (TextUtils.isEmpty(group.getDeviceDetail())) return "";

    String deviceDetail = group.getDeviceDetail();
    try {
      JSONObject jsonDeviceDetail = new JSONObject(deviceDetail);
      devicePwd = jsonDeviceDetail.getString("devicePass");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return devicePwd;
  }

  private void goLockMainActivity(GetDevTreeResponse<List<DeviceBean>> group) {
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_DEVICE_SERIAL_NO, group.getDeviceSerialNo());
    intent.putExtra(Navigator.EXTRA_DEVICE_NAME, group.getDeviceName());
    intent.putExtra(Navigator.EXTR_ROUTER_SERIAL_NUM, group.getDeviceSerialNo());
    intent.putExtra(Navigator.EXTRA_LOCK_BIND_ROUTER_ID, group.getRouterSerialNo());
    intent.putExtra(Navigator.EXTRA_BLE_LOCK, group);
    new Navigator().navigateTo(mContext, LockMainActivity.class, intent);
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }

  private class ChildViewHolder {
    final ItemDevChildViewBinding binding;

    ChildViewHolder(ItemDevChildViewBinding binding) {
      this.binding = binding;
    }

    public void bind(DeviceBean device) {
      binding.setDevice(device);
      binding.executePendingBindings();
    }
  }

  private class ParentViewHolder {

    final ItemDevParentViewBinding binding;

    ParentViewHolder(ItemDevParentViewBinding binding) {
      this.binding = binding;
    }

    public void bind(GetDevTreeResponse<List<DeviceBean>> router) {
      binding.setRouter(router);
      binding.executePendingBindings();
    }
  }

  private List<GetDevTreeResponse<List<DeviceBean>>> addChildHeadData(List<GetDevTreeResponse<List<DeviceBean>>> routers) {
    if (routers == null) return new ArrayList<>();
    for (GetDevTreeResponse<List<DeviceBean>> router : routers) {
      DeviceBean empDevice = new DeviceBean();
      empDevice.setDeviceName(EMPTY_DEVICE);
      router.getDeviceList().add(0, empDevice);
    }
    return routers;

  }

  public void setCurrentConnected(@NonNull String connectedDeviceId) {
    if (mRouters == null) return;
    for (int i = 0; i < mRouters.size(); i++) {
      GetDevTreeResponse<List<DeviceBean>> router = mRouters.get(i);
      if (!connectedDeviceId.equals(router.getDeviceSerialNo())) continue;
      mCurrentConnectedPosition = i;
      PrefConstant.ROUTER_ID_CONNECTED_DIRECT = router.getDeviceSerialNo();
      System.out.println("唯一ID = " + PrefConstant.ROUTER_ID_CONNECTED_DIRECT);
      break;
    }
    notifyDataSetChanged();
  }

  /**
   * 设备详情回调函数
   */
  public void setOnShareDeviceClickListener(OnShareDeviceClickListener onShareDeviceClickListener) {
    this.mOnShareDeviceClickListener = onShareDeviceClickListener;
  }

  public void setOnUnbindRouterClickListener(OnUnbindRouterClickListener listener) {
    mOnUnbindDeviceClickListener = listener;
  }

  public void setOnUnbindLockClickListener(OnUnbindLockClickListener onUnbindLockClickListener) {
    mOnUnbindLockClickListener = onUnbindLockClickListener;
  }

  public interface OnShareDeviceClickListener {
    void onShareDeviceClick(int groupPosition, String routerID);
  }

  public interface OnUnbindRouterClickListener {
    void onClick(GetDevTreeResponse<List<DeviceBean>> device);
  }

  public interface OnUnbindLockClickListener {
    void onClick(GetDevTreeResponse<List<DeviceBean>> device);
  }

  /**
   * 点击路由器的group item回调
   */
  public interface OnRouterGroupViewClickListener {
    void onClick(GetDevTreeResponse<List<DeviceBean>> router, int groupPosition, int currentConnectedPosition);
  }

  public void setOnRouterGroupViewClickListener(OnRouterGroupViewClickListener listener) {
    mOnRouterGroupViewClickListener = listener;
  }

  /**
   * 点击路由器的grid view item回调
   */
  public interface OnRouterGridViewClickListener {
    void onClick(GetDevTreeResponse<List<DeviceBean>> router, ApplyMenu applyMenu, int groupPosition, int gridItemPosition, int currentConnectedPosition);
  }

  public void setOnRouterGridViewClickListener(OnRouterGridViewClickListener clickListener) {
    mOnRouterGridViewClickListener = clickListener;
  }
}
