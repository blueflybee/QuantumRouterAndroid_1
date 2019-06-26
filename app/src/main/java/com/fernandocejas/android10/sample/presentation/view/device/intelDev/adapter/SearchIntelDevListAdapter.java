package com.fernandocejas.android10.sample.presentation.view.device.intelDev.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ItemSearchIntelDevListBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.addIntelDev.AddIntelDevVerifyActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.adapter.IntelDeviceListAdapter;
import com.qtec.router.model.rsp.SearchIntelDevResponse;
import com.qtec.router.model.rsp.SearchIntelDevResponse.IntelDev;

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
public class SearchIntelDevListAdapter extends RecyclerView.Adapter<SearchIntelDevListAdapter.ViewHolder> {

  private Context mContext;
  private List<IntelDev> mDevs = new ArrayList<>();


  private OnItemClickListener mOnItemClickListener;

  public SearchIntelDevListAdapter(Context context, List<IntelDev> devs) {
    mContext = context;
    mDevs = devs == null ? new ArrayList<>() : devs;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(mContext);
    ItemSearchIntelDevListBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_search_intel_dev_list, parent, false);
    return new ViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, final int position) {
    holder.bind(mDevs.get(position));
    holder.binding.rvSearchLocker.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mOnItemClickListener.onItemClick(v, holder.binding.getDev());

      }
    });
  }

  @Override
  public int getItemCount() {
    return mDevs.size();
  }

  public void notifyDataSetChanged(List<IntelDev> devs) {
    mDevs = devs == null ? new ArrayList<>() : devs;
    notifyDataSetChanged();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    final ItemSearchIntelDevListBinding binding;

    ViewHolder(ItemSearchIntelDevListBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(IntelDev dev) {
      binding.setDev(dev);
      binding.executePendingBindings();
    }
  }

  public void setOnItemClickListener(@NonNull OnItemClickListener onItemClickListener) {
    mOnItemClickListener = onItemClickListener;
  }


  public interface OnItemClickListener {

    void onItemClick(View v, IntelDev dev);
  }

}
