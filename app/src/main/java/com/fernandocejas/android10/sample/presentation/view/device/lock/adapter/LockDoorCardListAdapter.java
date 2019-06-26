package com.fernandocejas.android10.sample.presentation.view.device.lock.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ItemSecureManageFingerprintBinding;
import com.qtec.mapp.model.rsp.GetDoorCardsResponse;

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
public class LockDoorCardListAdapter extends BaseAdapter {
  private Context mContext;
  private boolean mShowDetail;
  private List<GetDoorCardsResponse> mDoorCards = new ArrayList<>();


  public LockDoorCardListAdapter(@NonNull Context context) {
    this(context, true);
  }

  public LockDoorCardListAdapter(@NonNull Context context, boolean showDetail) {
    mContext = context;
    mShowDetail = showDetail;
  }

  @Override
  public int getCount() {
    return mDoorCards.size();
  }

  @Override
  public GetDoorCardsResponse getItem(int position) {
    return mDoorCards.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    GetDoorCardsResponse item = getItem(position);
    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(mContext);
      ItemSecureManageFingerprintBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_secure_manage_fingerprint, parent, false);
      holder = new ViewHolder(binding);
      holder.bind(item);
      convertView = binding.getRoot();
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.binding.tvName.setText(item.getDoorcardName());
    holder.binding.ivArrow.setVisibility(mShowDetail ? View.VISIBLE : View.GONE);
    return convertView;
  }

  private class ViewHolder {

    private final ItemSecureManageFingerprintBinding binding;

    ViewHolder(ItemSecureManageFingerprintBinding binding) {
      this.binding = binding;
    }

    public void bind(GetDoorCardsResponse doorCard) {
      binding.executePendingBindings();
    }
  }

  public void update(List<GetDoorCardsResponse> doorCardsResponses) {
    mDoorCards = doorCardsResponses == null ? mDoorCards : doorCardsResponses;
    notifyDataSetChanged();
  }

  public void clear() {
    mDoorCards.clear();
    notifyDataSetChanged();
  }

}
