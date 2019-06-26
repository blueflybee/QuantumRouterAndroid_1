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
import com.fernandocejas.android10.sample.presentation.databinding.ItemServiceProviderBinding;
import com.fernandocejas.android10.sample.presentation.view.device.router.data.Router.BaseInfo.InfoItem;
import com.fernandocejas.android10.sample.presentation.view.device.router.data.ServiceProvider;

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
public class ServiceProviderAdapter extends BaseAdapter {

  private Context mContext;
  private List<ServiceProvider> mSps = new ArrayList<>();


  public ServiceProviderAdapter(@NonNull Context context, @NonNull List<ServiceProvider> sps) {
    mContext = context;
    mSps = sps;
  }

  @Override
  public int getCount() {
    return mSps.size();
  }

  @Override
  public ServiceProvider getItem(int position) {
    return mSps.get(position);
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
      ItemServiceProviderBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_service_provider, parent, false);
      holder = new ViewHolder(binding);
      holder.bind(getItem(position));
      convertView = binding.getRoot();
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }


    return convertView;
  }

  private class ViewHolder {

    private final ItemServiceProviderBinding binding;

    ViewHolder(ItemServiceProviderBinding binding) {
      this.binding = binding;
    }

    public void bind(ServiceProvider sp) {
      binding.setSp(sp);
      binding.executePendingBindings();
    }
  }
}
