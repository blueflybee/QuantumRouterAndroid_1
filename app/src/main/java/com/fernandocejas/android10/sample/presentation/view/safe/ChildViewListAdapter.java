package com.fernandocejas.android10.sample.presentation.view.safe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.SingalFileOperateBean;

import java.util.List;

import kale.adapter.CommonAdapter;

/**
 * GridView 适配器
 */
public class ChildViewListAdapter extends CommAdapter1<DeviceEntity.ChildEntity> {
  private Context context;
  private Boolean isHasKey = false;
  public ChildViewListAdapter(Context context, List<DeviceEntity.ChildEntity> data,Boolean hasKey, int layoutId) {
    super(context, data, layoutId);
    this.context = context;
    isHasKey = hasKey;
  }

  @Override
  public void convert(BaseViewHolder baseViewHolder, DeviceEntity.ChildEntity childEntity, int mPostition) {
    if(isHasKey){
      baseViewHolder.getView(R.id.ll_has_key).setVisibility(View.VISIBLE);
      baseViewHolder.getView(R.id.tv_has_no_key).setVisibility(View.GONE);

      TextView content = (TextView)baseViewHolder.getView(R.id.tv_safe_child_view_title);
      ImageView img = (ImageView) baseViewHolder.getView(R.id.img_safe_child);

      if(childEntity.getOpened()){
        content.setText(childEntity.getContent()+": 已开启");
      }else{
        content.setText(childEntity.getContent()+": 已关闭");
      }

      if(childEntity.getTestState() == 0){
        img.setVisibility(View.GONE);
        content.setTextColor(context.getResources().getColor(R.color.gray_dddddd));
      }else if(childEntity.getTestState() == 1){
        img.setVisibility(View.VISIBLE);
        img.setBackground(context.getResources().getDrawable(R.drawable.test_obing));
        content.setTextColor(context.getResources().getColor(R.color.blue_2196f3));
      }else{
        img.setVisibility(View.VISIBLE);
        img.setBackground(context.getResources().getDrawable(R.drawable.test_pass));
        content.setTextColor(context.getResources().getColor(R.color.black_424242));
      }
    }else {
/*
      baseViewHolder.getView(R.id.ll_has_key).setVisibility(View.GONE);
      baseViewHolder.getView(R.id.tv_has_no_key).setVisibility(View.VISIBLE);

      TextView content = (TextView)baseViewHolder.getView(R.id.tv_has_no_key);
      content.setText("暂时没有载入密钥");*/
    }

  }
}
