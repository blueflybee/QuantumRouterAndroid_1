package com.fernandocejas.android10.sample.presentation.view.device.adapter;

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
 *     desc   : 设备fragment网关工具箱
 *     version: 1.0
 * </pre>
 */
public class DevToolsAdapter extends BaseAdapter {

  private Context mContext;
  private List<RouterTool> mRouterTools = new ArrayList<>();


  public DevToolsAdapter(@NonNull Context context) {
    mContext = context;
    mRouterTools = getRouterTools();
  }

  public DevToolsAdapter(@NonNull Context context, @NonNull List<RouterTool> routerTools) {
    mContext = context;
    mRouterTools = routerTools;
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

  private List<RouterTool> getRouterTools() {
    // TODO: 2017/9/13  防火墙和灯光控制暂时未开放 底层不支持 后续版本迭代
    List<RouterTool> tools = new ArrayList<>();
    RouterTool tool = new RouterTool();
    tool.setName("当前状态");
    tool.setIconResId(R.drawable.btn_tool_status_selector);
    tools.add(tool);

    tool = new RouterTool();
    tool.setName("信号调节");
    tool.setIconResId(R.drawable.btn_tool_signal_selector);
    tools.add(tool);

    tool = new RouterTool();
    tool.setName("智能宽带");
    tool.setIconResId(R.drawable.btn_tool_intelligent_selector);
    tools.add(tool);

    tool = new RouterTool();
    tool.setName("特别关注");
    tool.setIconResId(R.drawable.btn_tool_special_selector);
    tools.add(tool);

    tool = new RouterTool();
    tool.setName("定时开关");
    tool.setIconResId(R.drawable.btn_tool_switch_selector);
    tools.add(tool);

    tool = new RouterTool();
    tool.setName("安全存储");
    tool.setIconResId(R.drawable.btn_tool_save_selector);
    tools.add(tool);

    tool = new RouterTool();
    tool.setName("一键体检");
    tool.setIconResId(R.drawable.btn_tool_check_selector);
    tools.add(tool);

    tool = new RouterTool();
    tool.setName("更多");
    tool.setIconResId(R.drawable.btn_tool_more_selector);
    tools.add(tool);

    return tools;

  }

  public static List<RouterTool> getLockTools() {
    List<RouterTool> tools = new ArrayList<>();
    RouterTool tool = new RouterTool();
    tool.setName("当前状态");
    tool.setIconResId(R.drawable.btn_lock_tool_status_selector);
    tools.add(tool);

    tool = new RouterTool();
    tool.setName("安全管理");
    tool.setIconResId(R.drawable.btn_lock_tool_security_manage_selector);
    tools.add(tool);

    tool = new RouterTool();
    tool.setName("远程开门");
    tool.setIconResId(R.drawable.btn_lock_tool_remote_selector);
    tools.add(tool);

    tool = new RouterTool();
    tool.setName("开门记录");
    tool.setIconResId(R.drawable.btn_lock_tool_open_record_selector);
    tools.add(tool);

    return tools;

  }

  public static List<RouterTool> getCatEyeTools() {
    List<RouterTool> tools = new ArrayList<>();
    RouterTool tool = new RouterTool();
    tool.setName("实况查看");
    tool.setIconResId(R.drawable.indexic_see);
    tools.add(tool);

    tool = new RouterTool();
    tool.setName("开启对讲");
    tool.setIconResId(R.drawable.indexic_lsay);
    tools.add(tool);

    tool = new RouterTool();
    tool.setName("门铃记录");
    tool.setIconResId(R.drawable.indexic_doorbell);
    tools.add(tool);

    tool = new RouterTool();
    tool.setName("设置");
    tool.setIconResId(R.drawable.indexic_doorstatus);
    tools.add(tool);

    return tools;

  }

  public static List<RouterTool> getCameraTools() {
    List<RouterTool> tools = new ArrayList<>();
    RouterTool tool = new RouterTool();
    tool.setName("实况查看");
    tool.setIconResId(R.drawable.sx_shikuang);
    tools.add(tool);

    tool = new RouterTool();
    tool.setName("视频回放");
    tool.setIconResId(R.drawable.sx_replay);
    tools.add(tool);

    tool = new RouterTool();
    tool.setName("报警记录");
    tool.setIconResId(R.drawable.sx_callpolice);
    tools.add(tool);

    tool = new RouterTool();
    tool.setName("设置");
    tool.setIconResId(R.drawable.sx_set);
    tools.add(tool);

    return tools;

  }
}
