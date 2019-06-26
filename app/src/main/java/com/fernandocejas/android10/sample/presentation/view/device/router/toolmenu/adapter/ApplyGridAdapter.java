package com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.obj.ApplyMenu;

import java.util.ArrayList;
import java.util.List;


public class ApplyGridAdapter extends BaseAdapter {
  private List<ApplyMenu> mMenus;
  private Context mContext;
  private LayoutInflater mInflater;

  public ApplyGridAdapter(Context context, List<ApplyMenu> menus) {
    this.mContext = context;
    mMenus = menus;
    mInflater = LayoutInflater.from(mContext);
  }

  @Override
  public int getCount() {
    return mMenus.size();
  }

  @Override
  public ApplyMenu getItem(int i) {
    return mMenus.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup group) {
    ViewHolder viewHolder;
    if (convertView == null) {
      viewHolder = new ViewHolder();
      convertView = mInflater.inflate(R.layout.item_router_tool_menu, group, false);
      viewHolder.mImageView = convertView.findViewById(R.id.iv_icon);
      viewHolder.mTextView = convertView.findViewById(R.id.tv_name);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    viewHolder.mImageView.setImageResource(mMenus.get(position).getImgRes());
    viewHolder.mTextView.setText(mMenus.get(position).getName());
    return convertView;
  }

  public void update(ArrayList<ApplyMenu> menus) {
    mMenus.clear();
    mMenus.addAll(menus);
    notifyDataSetChanged();
  }

  public static class ViewHolder {
    ImageView mImageView;
    TextView mTextView;
  }
}