package com.fernandocejas.android10.sample.presentation.view.device.router.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ItemAddLockerBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ItemAddRouterBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/22
 *     desc   : 网关基本信息
 *     version: 1.0
 * </pre>
 */
public class AddLockAdapter extends BaseAdapter {

  private Context mContext;
  private List<String> mLocks = new ArrayList<>();

  public AddLockAdapter(@NonNull Context context, @NonNull List<String> locks) {
    mContext = context;
    mLocks = locks;
  }

  public AddLockAdapter(@NonNull Context context) {
    mContext = context;
    initData();
  }

  private void initData() {
    for (int i = 0; i < 5; i++) {
      mLocks.add("九州智能门锁" + (i + 1) + ".0");
    }
  }

  @Override
  public int getCount() {
    return mLocks.size();
  }

  @Override
  public String getItem(int position) {
    return mLocks.get(position);
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
      ItemAddLockerBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_add_locker, parent, false);
      holder = new ViewHolder(binding);
      holder.bind(getItem(position));
      convertView = binding.getRoot();
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.binding.tvLockerName.setText(getItem(position));


    return convertView;
  }

  public void notifyDataSetChanged(List<String> infoItems) {
    if (infoItems == null) return;
    this.mLocks = infoItems;
    notifyDataSetChanged();
  }

  private class ViewHolder {

    private final ItemAddLockerBinding binding;

    ViewHolder(ItemAddLockerBinding binding) {
      this.binding = binding;
    }

    public void bind(String router) {
//      binding.setInfo(router);
      binding.executePendingBindings();
    }
  }
}
