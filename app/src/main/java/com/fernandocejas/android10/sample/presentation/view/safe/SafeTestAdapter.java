package com.fernandocejas.android10.sample.presentation.view.safe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.GridViewAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.SingalFileOperateBean;

import java.util.ArrayList;
import java.util.List;

//自定义Adapter内部类
public class SafeTestAdapter extends BaseExpandableListAdapter {
  private Context mContext;
  private static final int ITEMCOUNT = 2;// 类型的总数
  private List<DeviceEntity<List<DeviceEntity.ChildEntity>>> coll;//
  private LayoutInflater mInflater;
  OnDeviceContentExpanded onDeviceContentExpaned;
  OnDeviceTitle onDeviceTitle;
  DeviceEntity.ChildEntity bean;
  List<DeviceEntity.ChildEntity> beans = new ArrayList<>();

  public static interface IDeviceViewType {
    int DEVIDCE_LOCK = 0;// lock
    int DEVICE_ROUTER = 1;// router
  }

  public SafeTestAdapter(Context context, List<DeviceEntity<List<DeviceEntity.ChildEntity>>> colls) {
    this.coll = colls;
    mInflater = LayoutInflater.from(context);
    this.mContext = context;

    bean = new DeviceEntity.ChildEntity();
    bean.setOpened(false);
    bean.setTestState(2);
    bean.setContent("");
  }

  @Override
  public int getGroupCount() {
    return coll.size();
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return 1;
  }

  @Override
  public Object getGroup(int groupPosition) {
    return coll.get(groupPosition);
  }

  @Override
  public Object getChild(int groupPosition, int childPosition) {
    return coll.get(groupPosition).getChilBeans().get(childPosition);
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
    GroupViewHolder viewHolder = null;

    if (convertView == null) {

      convertView = mInflater.inflate(
          R.layout.item_safe_test_title, null);

      viewHolder = new GroupViewHolder();

      viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_safe_group_name);
      viewHolder.tvState = (TextView) convertView.findViewById(R.id.tv_safe_group_state);

      convertView.setTag(viewHolder);
    } else {
      viewHolder = (GroupViewHolder) convertView.getTag();
    }

    viewHolder.tvName.setText(coll.get(groupPosition).getName());

    /*    viewHolder.rlDeviceTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(coll.get(groupPosition).getItemExpanded()){
                    coll.get(groupPosition).setItemExpanded(false);
                }else{
                    coll.get(groupPosition).setItemExpanded(true);
                }

            }
        });*/

    if(coll.get(groupPosition).getState() == 0){
      viewHolder.tvState.setText("等待检测");
    }else if(coll.get(groupPosition).getState() == 1){
      viewHolder.tvState.setText("检测中");
    }else if(coll.get(groupPosition).getState() == 2){
      viewHolder.tvState.setText("检测完成");
    }else {
      viewHolder.tvState.setText("设备没有密钥");
    }

    return convertView;
  }

  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    ChildViewHolder viewHolder1;
    ChildViewListAdapter itemAdapter = null;

    if (convertView == null) {
      convertView = mInflater.inflate(
          R.layout.item_safe_child, null);

      viewHolder1 = new ChildViewHolder();

      viewHolder1.lv_safe_test_child = (ListView) convertView.findViewById(R.id.lv_safe_test_child);

      convertView.setTag(viewHolder1);
    } else {
      viewHolder1 = (ChildViewHolder) convertView.getTag();
    }

    if(coll.get(groupPosition).getHasKey()){
      // 有密钥
      itemAdapter = new ChildViewListAdapter(mContext,coll.get(groupPosition).getChilBeans(),coll.get(groupPosition).getHasKey(),R.layout.item_safe_test_child);
    }else {
      //没有密钥的情况是造的假数据
      beans.clear();
      beans.add(bean);

      itemAdapter = new ChildViewListAdapter(mContext,beans,coll.get(groupPosition).getHasKey(),R.layout.item_safe_test_child);
    }


    viewHolder1.lv_safe_test_child.setAdapter(itemAdapter);

    return convertView;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }

  // 用于存储列表每一行元素的图片和文本
  class GroupViewHolder {
    public RelativeLayout rlDeviceTitle;
    public TextView tvName;
    public TextView tvState;
  }

  class ChildViewHolder {
    public ListView lv_safe_test_child;
    public TextView tv_no_key;
  }

  protected interface OnDeviceContentExpanded {
    void deviceContentExpanded(View v, int groupPosition,int childPosition);
  }

  protected interface OnDeviceTitle {
    void deviceTitle(View v, int groupPosition);
  }

  protected void setDeviceContentExpaned(OnDeviceContentExpanded onDeviceContentExpaned) {
    this.onDeviceContentExpaned = onDeviceContentExpaned;
  }

  protected void setDeviceTitle(OnDeviceTitle onDeviceTitle) {
    this.onDeviceTitle = onDeviceTitle;
  }

}
