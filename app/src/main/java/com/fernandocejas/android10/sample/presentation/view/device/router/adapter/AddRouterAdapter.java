package com.fernandocejas.android10.sample.presentation.view.device.router.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ItemAddRouterBinding;
import com.fernandocejas.android10.sample.presentation.view.device.router.data.Router.BaseInfo.InfoItem;

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
public class AddRouterAdapter extends BaseAdapter {

  private Context mContext;
  private List<String> mRouters = new ArrayList<>();

  public AddRouterAdapter(@NonNull Context context, @NonNull List<String> routers) {
    mContext = context;
    mRouters = routers;
  }

  public AddRouterAdapter(@NonNull Context context) {
    mContext = context;
    initData();
  }

  private void initData() {
    for (int i = 0; i < 5; i++) {
      mRouters.add("九州网关" + (i + 1) + ".0");
    }
  }

  @Override
  public int getCount() {
    return mRouters.size();
  }

  @Override
  public String getItem(int position) {
    return mRouters.get(position);
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
      ItemAddRouterBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_add_router, parent, false);
      holder = new ViewHolder(binding);
      holder.bind(getItem(position));
      convertView = binding.getRoot();
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.binding.tvRouterName.setText(getItem(position));


    return convertView;
  }

  public void notifyDataSetChanged(List<String> infoItems) {
    if (infoItems == null) return;
    this.mRouters = infoItems;
    notifyDataSetChanged();
  }

  private class ViewHolder {

    private final ItemAddRouterBinding binding;

    ViewHolder(ItemAddRouterBinding binding) {
      this.binding = binding;
    }

    public void bind(String router) {
//      binding.setInfo(router);
      binding.executePendingBindings();
    }
  }
}
