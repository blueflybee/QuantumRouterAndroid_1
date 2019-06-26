package com.fernandocejas.android10.sample.presentation.view.safe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;

import java.util.List;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class SafeTestAdapter_Test extends BaseAdapter {
  private Context context;
  OnDeviceContentExpanded onDeviceContentExpaned;

  public static interface IDeviceViewType {
    int DEVIDCE_LOCK  = 0;// lock
    int DEVICE_ROUTER = 1;// router
  }

  private static final int ITEMCOUNT = 2;// 类型的总数

  private List<DeviceEntity> coll;//

  private LayoutInflater mInflater;

  public SafeTestAdapter_Test(Context context, List<DeviceEntity> coll) {
    this.coll = coll;
    mInflater = LayoutInflater.from(context);
    this.context = context;
  }

  public int getCount() {
    return coll.size();
  }

  public Object getItem(int position) {
    return coll.get(position);
  }

  public long getItemId(int position) {
    return position;
  }

  /**
   * 得到Item的类型
   */
  public int getItemViewType(int position) {
    DeviceEntity entity = coll.get(position);

    if (entity.getType() == 0) {
      return IDeviceViewType.DEVIDCE_LOCK;
    } else {
      return IDeviceViewType.DEVICE_ROUTER;
    }
  }

  /**
   * Item类型的总数
   */
  public int getViewTypeCount() {
    return ITEMCOUNT;
  }

  public View getView(int position, View convertView, ViewGroup parent) {

    DeviceEntity entity = coll.get(position);
    int type = entity.getType();

    ViewHolder viewHolder = null;
    if (convertView == null) {
      if (type == 0) {
        convertView = mInflater.inflate(
            R.layout.item_safe_test_lock, null);
      } else {
        convertView = mInflater.inflate(
            R.layout.item_safe_test_router, null);
      }

      viewHolder = new ViewHolder();
           viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_safe_device_name);
           viewHolder.rlDeviceTitle = (RelativeLayout) convertView.findViewById(R.id.rl_safe_device_title);
           viewHolder.llDeviceContent = (LinearLayout) convertView.findViewById(R.id.ll_safe_device_content);

      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    viewHolder.tvName.setText(coll.get(position).getName());

    if(coll.get(position).getItemExpanded()){
      viewHolder.llDeviceContent.setVisibility(View.VISIBLE);//默认第一个展开
    }else {
      viewHolder.llDeviceContent.setVisibility(View.GONE);

    }

    viewHolder.rlDeviceTitle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

       if(onDeviceContentExpaned != null){
          onDeviceContentExpaned.deviceContentExpanded(v,position);
       }

      }
    });

    return convertView;
  }

  static class ViewHolder {
    public TextView tvName;
    public RelativeLayout rlDeviceTitle;
    public LinearLayout llDeviceContent;
  }

  protected interface OnDeviceContentExpanded{
    void deviceContentExpanded(View v,int position);
  }

  protected void setDeviceContentExpaned(OnDeviceContentExpanded onDeviceContentExpaned){
    this.onDeviceContentExpaned = onDeviceContentExpaned;
  }

}
