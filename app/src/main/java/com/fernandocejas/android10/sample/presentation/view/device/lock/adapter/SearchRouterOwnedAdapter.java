package com.fernandocejas.android10.sample.presentation.view.device.lock.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ItemSearchRouterOwnBinding;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse.DeviceBean;

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
public class SearchRouterOwnedAdapter extends BaseAdapter {

  private Context mContext;
  private List<GetDevTreeResponse<List<DeviceBean>>> mGetDevTreeResponses = new ArrayList<>();

  private OnBindBtnClickListener mOnBindBtnClickListener;


  public SearchRouterOwnedAdapter(@NonNull Context context) {
    mContext = context;
  }

  @Override
  public int getCount() {
    return mGetDevTreeResponses.size();
  }

  @Override
  public GetDevTreeResponse getItem(int position) {
    return mGetDevTreeResponses.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    GetDevTreeResponse ownedRouter = getItem(position);
    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(mContext);
      ItemSearchRouterOwnBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_search_router_own, parent, false);
      holder = new ViewHolder(binding);
      holder.bind(ownedRouter);
      convertView = binding.getRoot();
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.binding.tvRouterName.setText(ownedRouter.getDeviceName());
    holder.binding.btnStartBind.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mOnBindBtnClickListener != null) {
          mOnBindBtnClickListener.onClick(v, ownedRouter);
        }
      }
    });

    return convertView;
  }

  public void update(List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> responses) {
    if (responses == null || responses.isEmpty()) return;
    mGetDevTreeResponses = responses;
    notifyDataSetChanged();
  }

  public void setOnBindBtnClickListener(OnBindBtnClickListener onBindBtnClickListener) {
    mOnBindBtnClickListener = onBindBtnClickListener;
  }

  private class ViewHolder {

    private final ItemSearchRouterOwnBinding binding;

    ViewHolder(ItemSearchRouterOwnBinding binding) {
      this.binding = binding;
    }

    public void bind(GetDevTreeResponse response) {
      binding.executePendingBindings();
    }
  }

  public interface OnBindBtnClickListener {
    void onClick(View view, GetDevTreeResponse device);
  }


}
