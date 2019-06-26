package com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ItemDevRouterToolBinding;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.RouterTool;

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
public class RouterToolsAdapter extends BaseAdapter {

  private Context mContext;
  private List<RouterTool> mRouterTools = new ArrayList<>();

  public RouterToolsAdapter(@NonNull Context context,String[] titles,Integer[] icons) {
    mContext = context;

    mRouterTools = getTools(titles,icons);

  }


  @Override
  public int getCount() {
    return mRouterTools.size();
  }

  @Override
  public RouterTool getItem(int position) {
    return mRouterTools.get(position);
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
      ItemDevRouterToolBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_dev_router_tool, parent, false);
      holder = new ViewHolder(binding);
      holder.bind(getItem(position));
      convertView = binding.getRoot();
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.binding.ivIcon.setBackgroundResource(getItem(position).getIconResId());


    return convertView;
  }


  private class ViewHolder {

    private final ItemDevRouterToolBinding binding;

    ViewHolder(ItemDevRouterToolBinding binding) {
      this.binding = binding;
    }

    public void bind(RouterTool routerTool) {
      binding.setRouterTool(routerTool);
      binding.executePendingBindings();
    }
  }

  private List<RouterTool> getTools(String[] titles,Integer[] icons) {
    List<RouterTool> tools = new ArrayList<>();
    for (int i = 0; i < titles.length; i++) {
      RouterTool tool = new RouterTool();
      tool.setName(titles[i]);
      tool.setIconResId(icons[i]);
      tools.add(tool);
    }

    return tools;

  }
}
