package com.fernandocejas.android10.sample.presentation.view.device.lock.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ItemSelectLockManagerBinding;
import com.fernandocejas.android10.sample.presentation.utils.GlideUtil;
import com.fernandocejas.android10.sample.presentation.view.device.lock.data.LockUser;
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
public class SelectLockManagerAdapter extends BaseAdapter {

  private Context mContext;
  private List<GetLockUsersResponse> mLockUsers = new ArrayList<>();

  private int mSelectedPosition;

  public SelectLockManagerAdapter(@NonNull Context context, @NonNull List<GetLockUsersResponse> lockUsers) {
    mContext = context;
    mLockUsers = filterLockUsers(lockUsers);
  }

  private List<GetLockUsersResponse> filterLockUsers(List<GetLockUsersResponse> lockUsers) {
    if (lockUsers == null || lockUsers.isEmpty()) return new ArrayList<>();
    if (lockUsers.size() <= 1) return lockUsers;
    for (int i = lockUsers.size() - 1; i >= 0; i--) {
      GetLockUsersResponse lockUser = lockUsers.get(i);
      if (!GetUserRoleResponse.ADMIN.equals(lockUser.getUserRole())) continue;
      lockUsers.remove(i);
    }
    return lockUsers;
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
      ItemSelectLockManagerBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_select_lock_manager, parent, false);
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
//    boolean isAdmin = GetUserRoleResponse.ADMIN.equals(user.getUserRole());
//    holder.binding.tvAdminTip.setVisibility(isAdmin ? View.VISIBLE : View.GONE);

    holder.binding.ivArrow.setVisibility(position == mSelectedPosition ? View.VISIBLE : View.GONE);


    return convertView;
  }

  public void updateCheckPosition(int position) {
    mSelectedPosition = position;
    notifyDataSetChanged();
  }

  public GetLockUsersResponse getSelectedUser() {
    return getItem(mSelectedPosition);
  }


  private class ViewHolder {

    private final ItemSelectLockManagerBinding binding;

    ViewHolder(ItemSelectLockManagerBinding binding) {
      this.binding = binding;
    }

    public void bind(GetLockUsersResponse lockUser) {
      binding.executePendingBindings();
    }
  }

}
