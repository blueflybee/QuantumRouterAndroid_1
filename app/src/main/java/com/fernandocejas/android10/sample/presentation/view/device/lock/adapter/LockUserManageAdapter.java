package com.fernandocejas.android10.sample.presentation.view.device.lock.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ItemLockUserManageBinding;
import com.fernandocejas.android10.sample.presentation.utils.GlideUtil;
import com.fernandocejas.android10.sample.presentation.view.device.lock.data.LockUser;
import com.fernandocejas.android10.sample.presentation.view.device.router.data.Router.RouterSetting;
import com.qtec.mapp.model.rsp.GetLockUsersResponse;
import com.qtec.mapp.model.rsp.GetUserRoleResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/22
 *     desc   : 网关状态界面下属客户端列表adpter
 *     version: 1.0
 * </pre>
 */
public class LockUserManageAdapter extends BaseAdapter {

  private Context mContext;
  private List<GetLockUsersResponse> mLockUsers = new ArrayList<>();


  public LockUserManageAdapter(@NonNull Context context) {
    mContext = context;
  }

  @Override
  public int getCount() {
    return mLockUsers.size();
  }

  @Override
  public GetLockUsersResponse getItem(int position) {
    return mLockUsers.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(mContext);
      ItemLockUserManageBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_lock_user_manage, parent, false);
      holder = new ViewHolder(binding);
      holder.bind(getItem(position));
      convertView = binding.getRoot();
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    GetLockUsersResponse user = getItem(position);
    GlideUtil.loadCircleHeadImage(mContext, user.getUserHeadImage(), holder.binding.imgHead, R.drawable.default_avatar);

    holder.binding.tvName.setText(user.getUserNickName());
    boolean isAdmin = GetUserRoleResponse.ADMIN.equals(user.getUserRole());
    holder.binding.tvAdminTip.setVisibility(isAdmin ? View.VISIBLE : View.GONE);

    return convertView;
  }

  public void update(List<GetLockUsersResponse> lockUsers) {
    if (lockUsers == null) return;
    mLockUsers = lockUsers;
    notifyDataSetChanged();
  }

  private class ViewHolder {

    private final ItemLockUserManageBinding binding;

    ViewHolder(ItemLockUserManageBinding binding) {
      this.binding = binding;
    }

    public void bind(GetLockUsersResponse lockUser) {
      binding.executePendingBindings();
    }
  }

}
