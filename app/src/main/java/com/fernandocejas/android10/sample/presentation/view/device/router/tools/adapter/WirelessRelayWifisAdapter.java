package com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ItemWirelessRelayWifiBinding;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.WirelessRelayWifi;
import com.qtec.router.model.rsp.GetWirelessListResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class WirelessRelayWifisAdapter extends BaseAdapter {

  private Context mContext;
  /*private List<WirelessRelayWifi> mRelayWifis = new ArrayList<>();*/
  List<GetWirelessListResponse.WirelessBean> mRelayWifis;
  OnConnectClickListener onConnectClickListener;
  OnDetailClickListener onDetailClickListener;


  public WirelessRelayWifisAdapter(@NonNull Context context, List<GetWirelessListResponse.WirelessBean> bean) {
    mContext = context;
    /*mRelayWifis = getWifis();*/
    mRelayWifis = bean;
  }

  @Override
  public int getCount() {
    return mRelayWifis.size();
  }

  @Override
  public GetWirelessListResponse.WirelessBean getItem(int position) {
    return mRelayWifis.get(position);
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
      ItemWirelessRelayWifiBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_wireless_relay_wifi, parent, false);
      holder = new ViewHolder(binding);
      holder.bind(getItem(position));
      convertView = binding.getRoot();
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    // void android.widget.TextView.setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom)

    if(mRelayWifis.get(position).getPower()<50){

      Drawable leftDrawable = mContext.getResources().getDrawable(R.drawable.wifi1);
      leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());

      holder.binding.tvWirelessSsid.setCompoundDrawables(leftDrawable, null, null, null);

    }else if((mRelayWifis.get(position).getPower()>=50)&&(mRelayWifis.get(position).getPower()<=75)){

      Drawable leftDrawable = mContext.getResources().getDrawable(R.drawable.wifi2);
      leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());

      holder.binding.tvWirelessSsid.setCompoundDrawables(leftDrawable, null, null, null);
    }else{

      Drawable leftDrawable = mContext.getResources().getDrawable(R.drawable.ic_wifi_black);
      leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());

      holder.binding.tvWirelessSsid.setCompoundDrawables(leftDrawable, null, null, null);
    }

    holder.binding.ivArrow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if ( onDetailClickListener != null) {
          onDetailClickListener.onDetailClick(position);
        }
      }
    });

    holder.binding.rlConnect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if ( onConnectClickListener != null) {
          onConnectClickListener.onConnectClick(position);
        }
      }
    });



    return convertView;
  }


  private class ViewHolder {

    private final ItemWirelessRelayWifiBinding binding;

    ViewHolder(ItemWirelessRelayWifiBinding binding) {
      this.binding = binding;
    }

    public void bind(GetWirelessListResponse.WirelessBean wifi) {
      binding.setWifi(wifi);
      binding.executePendingBindings();
    }
  }

  private List<WirelessRelayWifi> getWifis() {
    List<WirelessRelayWifi> relayWifis = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      WirelessRelayWifi wifi = new WirelessRelayWifi();
      wifi.setName("九州量子网关" + i);
      relayWifis.add(wifi);
    }

    return relayWifis;

  }

  public void setOnConnectClickListener(OnConnectClickListener onConnectClickListener) {
    this.onConnectClickListener = onConnectClickListener;
  }

  public void setOnDetailClickListener(OnDetailClickListener onDetailClickListener) {
    this.onDetailClickListener = onDetailClickListener;
  }

  public interface OnConnectClickListener {
    void onConnectClick(int position);
  }

  public interface OnDetailClickListener {
    void onDetailClick(int position);
  }
}
