package com.fernandocejas.android10.sample.presentation.view.device.router.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ItemIntelDeviceBinding;
import com.qtec.mapp.model.rsp.IntelDeviceListResponse;

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
public class IntelDeviceListAdapter extends RecyclerView.Adapter<IntelDeviceListAdapter.ViewHolder> {

  private Context mContext;
  private List<IntelDeviceListResponse> mDevices = new ArrayList<>();
  private OnItemClickListener mOnItemClickListener;


  public IntelDeviceListAdapter(Context context) {
    mContext = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
    ItemIntelDeviceBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_intel_device, parent, false);
    return new ViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, final int position) {
    holder.bind(mDevices.get(position));
    holder.binding.rvDevice.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mOnItemClickListener != null) {
          mOnItemClickListener.onItemClick(holder.binding.rvDevice, mDevices.get(position));
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return mDevices.size();
  }

  public void notifyDataSetChanged(List<IntelDeviceListResponse> statuses) {
    mDevices = statuses == null ? new ArrayList<>() : statuses;
    notifyDataSetChanged();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    final ItemIntelDeviceBinding binding;

    public ViewHolder(ItemIntelDeviceBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(IntelDeviceListResponse deviceList) {
      binding.setDeviceBean(deviceList);
      binding.executePendingBindings();
    }
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    mOnItemClickListener = listener;
  }

  public interface OnItemClickListener {
    void onItemClick(View view, IntelDeviceListResponse device);
  }
}
