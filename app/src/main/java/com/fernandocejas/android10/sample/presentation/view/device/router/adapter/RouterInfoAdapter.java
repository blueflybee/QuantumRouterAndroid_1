package com.fernandocejas.android10.sample.presentation.view.device.router.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ItemRouterInfoBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ItemRouterSettingBinding;
import com.fernandocejas.android10.sample.presentation.view.device.router.data.Router;
import com.fernandocejas.android10.sample.presentation.view.device.router.data.Router.*;
import com.fernandocejas.android10.sample.presentation.view.device.router.data.Router.BaseInfo.*;

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
public class RouterInfoAdapter extends BaseAdapter {

  private Context mContext;
  private List<InfoItem> mInfoItems = new ArrayList<>();

  public RouterInfoAdapter(@NonNull Context context, @NonNull List<InfoItem> infoItems) {
    mContext = context;
    mInfoItems = infoItems;
  }

  public RouterInfoAdapter(@NonNull Context context) {
    mContext = context;
  }

  @Override
  public int getCount() {
    return mInfoItems.size();
  }

  @Override
  public InfoItem getItem(int position) {
    return mInfoItems.get(position);
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
      ItemRouterInfoBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_router_info, parent, false);
      holder = new ViewHolder(binding);
      holder.bind(getItem(position));
      convertView = binding.getRoot();
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

//    holder.binding.ivArrow.setVisibility(position <= 2 ? View.VISIBLE : View.INVISIBLE);


    return convertView;
  }

  public void notifyDataSetChanged(List<InfoItem> infoItems) {
    if (infoItems == null) return;
    mInfoItems = infoItems;
    notifyDataSetChanged();
  }

  private class ViewHolder {

    private final ItemRouterInfoBinding binding;

    ViewHolder(ItemRouterInfoBinding binding) {
      this.binding = binding;
    }

    public void bind(InfoItem infoItem) {
      binding.setInfo(infoItem);
      binding.executePendingBindings();
    }
  }
}
