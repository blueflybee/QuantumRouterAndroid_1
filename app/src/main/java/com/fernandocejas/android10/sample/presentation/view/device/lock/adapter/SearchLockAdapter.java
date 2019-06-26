package com.fernandocejas.android10.sample.presentation.view.device.lock.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ItemAddLockerBinding;

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
public class SearchLockAdapter extends BaseAdapter {

  private Context mContext;
  private List<BluetoothDevice> mDevices = new ArrayList<>();
  private List<Integer> mRssis = new ArrayList<>();


  public SearchLockAdapter(@NonNull Context context) {
    mContext = context;
  }

  @Override
  public int getCount() {
    return mDevices.size();
  }

  @Override
  public BluetoothDevice getItem(int position) {
    return mDevices.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    BluetoothDevice device = getItem(position);

    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(mContext);
      ItemAddLockerBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_add_locker, parent, false);
      holder = new ViewHolder(binding);
      holder.bind(device);
      convertView = binding.getRoot();
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.binding.tvLockerName.setText(device.getName() + "  " + device.getAddress() + " " + mRssis.get(position));

    return convertView;
  }

  public void clear() {
    mDevices.clear();
    mRssis.clear();
    notifyDataSetChanged();
  }

  public void update(BluetoothDevice device, int rssi) {
    if (!mDevices.contains(device)) {
      mDevices.add(device);
      mRssis.add(rssi);
      notifyDataSetChanged();
    }
  }


  private class ViewHolder {

    private final ItemAddLockerBinding binding;

    ViewHolder(ItemAddLockerBinding binding) {
      this.binding = binding;
    }

    public void bind(BluetoothDevice device) {
      binding.executePendingBindings();
    }
  }

}
