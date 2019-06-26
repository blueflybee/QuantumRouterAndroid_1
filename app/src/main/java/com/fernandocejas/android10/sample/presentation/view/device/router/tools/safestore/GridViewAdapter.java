package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;

import java.util.List;

/**
 * GridView 适配器
 */
public class GridViewAdapter extends BaseAdapter {

  private Context mContext;
  private List<SingalFileOperateBean> lists;
  private Boolean isFolder;

  /**
   * 每个分组下的每个子项的 GridView 数据集合
   */

  public GridViewAdapter(Context mContext, List<SingalFileOperateBean> lists,Boolean isFolder) {
    this.mContext = mContext;
    this.lists = lists;
    this.isFolder = isFolder;
  }

  @Override
  public int getCount() {
    return lists.size();
  }

  @Override
  public Object getItem(int position) {
    return lists.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (null == convertView) {
      convertView = View.inflate(mContext, R.layout.item_gridview, null);
    }

    TextView title = (TextView) convertView.findViewById(R.id.tv_gridview);
    ImageView img = (ImageView) convertView.findViewById(R.id.img_gridview);
    LinearLayout llChildView = (LinearLayout) convertView.findViewById(R.id.ll_child_gridview);

    title.setText(lists.get(position).getTitle());

    // 文件夹不支持下载和移动操作 置灰不可点击
    if (isFolder) {
      //文件夹
      if (position == 0) {
        title.setTextColor(mContext.getResources().getColor(R.color.gray));
        llChildView.setClickable(false);
        llChildView.setEnabled(false);
        img.setBackground(mContext.getResources().getDrawable(R.drawable.nas_filedownload));
      }else if(position == 2){
        title.setTextColor(mContext.getResources().getColor(R.color.gray));
        llChildView.setClickable(false);
        llChildView.setEnabled(false);
        img.setBackground(mContext.getResources().getDrawable(R.drawable.nas_filemove));
      }else{
        img.setImageResource(lists.get(position).getImg());
      }
    }else {
      img.setImageResource(lists.get(position).getImg());
    }

    return convertView;
  }
}
