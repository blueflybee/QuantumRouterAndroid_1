package com.fernandocejas.android10.sample.presentation.view.device.router.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ItemRouterSettingBinding;
import com.fernandocejas.android10.sample.presentation.view.device.router.data.Router.RouterSetting;

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
public class RouterSettingAdapter extends BaseAdapter {

  private Context mContext;
  private List<RouterSetting> mRouterSettings = new ArrayList<>();


  public RouterSettingAdapter(@NonNull Context context, @NonNull List<RouterSetting> settings) {
    mContext = context;
    mRouterSettings = settings;
  }

  @Override
  public int getCount() {
    return mRouterSettings.size();
  }

  @Override
  public RouterSetting getItem(int position) {
    return mRouterSettings.get(position);
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
      ItemRouterSettingBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_router_setting, parent, false);
      holder = new ViewHolder(binding);
      holder.bind(getItem(position));
      convertView = binding.getRoot();
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    RouterSetting setting = getItem(position);

    Drawable leftDrawable = mContext.getResources().getDrawable(setting.getIconId());
    leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
    holder.binding.tvSettingName.setCompoundDrawables(leftDrawable, null, null, null);
    holder.binding.tvSettingName.setCompoundDrawablePadding(20);

    return convertView;
  }

  private class ViewHolder {

    private final ItemRouterSettingBinding binding;

    ViewHolder(ItemRouterSettingBinding binding) {
      this.binding = binding;
    }

    public void bind(RouterSetting setting) {
      binding.setSetting(setting);
      binding.executePendingBindings();
    }
  }
}
