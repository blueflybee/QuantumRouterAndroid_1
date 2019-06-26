package com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ItemWirelessRelayDetailBinding;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.WirelessRelayWifi;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay.ConnectedWifiBean;
import com.qtec.router.model.rsp.GetWifiDetailResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class WirelessRelayWifiDetailAdapter extends BaseAdapter {

  private Context mContext;
  private List<Map<String , String>> mMaps = new ArrayList<>();


  public WirelessRelayWifiDetailAdapter(@NonNull Context context, ConnectedWifiBean bean) {
    mContext = context;
    mMaps = getMaps(bean);
  }

  @Override
  public int getCount() {
    return mMaps.size();
  }

  @Override
  public Map<String , String> getItem(int position) {
    return mMaps.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    Map<String, String> map = getItem(position);
    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(mContext);
      ItemWirelessRelayDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_wireless_relay_detail, parent, false);
      holder = new ViewHolder(binding);
      holder.bind(map);
      convertView = binding.getRoot();
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    String key = map.keySet().iterator().next();
    holder.binding.tvName.setText(key);
    holder.binding.tvContent.setText(map.get(key));

    return convertView;
  }


  private class ViewHolder {

    private final ItemWirelessRelayDetailBinding binding;

    ViewHolder(ItemWirelessRelayDetailBinding binding) {
      this.binding = binding;
    }

    public void bind(Map<String , String> map) {
//      binding.setWifi(wifi);
      binding.executePendingBindings();
    }
  }

  private List<Map<String , String>> getMaps(ConnectedWifiBean bean) {
    List<Map<String , String>> maps = new ArrayList<>();
    HashMap<String, String> map = new HashMap<>();

    switch (bean.getPower()){
      case 0:
        map.put("信号强度", "弱");
        break;
      case 1:
        map.put("信号强度", "一般");
        break;
      case 2:
        map.put("信号强度", "强");
        break;

      default:
        break;
    }

    maps.add(map);

    map = new HashMap<>();
    map.put("Mac地址", bean.getMac());
    maps.add(map);

    map = new HashMap<>();
    map.put("信道", ""+bean.getChannel());
    maps.add(map);

    map = new HashMap<>();
    map.put("安全性", bean.getEncrypt());
    maps.add(map);

    map = new HashMap<>();
    map.put("IP地址", bean.getIpaddr());
    maps.add(map);

    map = new HashMap<>();
    map.put("子网掩码", bean.getNetmask());
    maps.add(map);

    map = new HashMap<>();
    map.put("网关地址", bean.getGateway());
    maps.add(map);

    map = new HashMap<>();
    map.put("DNS", bean.getDns());
    maps.add(map);

    return maps;
  }
}
