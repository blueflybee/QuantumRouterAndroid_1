package com.fernandocejas.android10.sample.presentation.view.device.router.status;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.qtec.router.model.rsp.GetBlackListResponse;

import java.util.List;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/09/13
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class BlackListAdapter extends CommAdapter1<GetBlackListResponse>{
  private Context mContext;
  OnRemoveBlackMem onRemoveBlackMem;

  public BlackListAdapter(Context context, List<GetBlackListResponse> data, int layoutId) {
    super(context, data, layoutId);
    mContext = context;
  }

  @Override
  public void convert(BaseViewHolder baseViewHolder, GetBlackListResponse bean, int mPostition) {
    TextView name = (TextView) baseViewHolder.getView(R.id.tv_black_list_name);
    TextView mask  = (TextView) baseViewHolder.getView(R.id.tv_black_list_mask);
    ImageView type = (ImageView) baseViewHolder.getView(R.id.img_black_list_head);
    TextView move = (TextView) baseViewHolder.getView(R.id.tv_black_list_move);

    if(!TextUtils.isEmpty(bean.getName())){
      name.setText(bean.getName());
    }else{
      name.setText("未知设备");
    }
    if(!TextUtils.isEmpty(bean.getMacaddr())){
      mask.setText(bean.getMacaddr());
    }

    /*switch (bean.getType()) {
      case 0:
        //computer
        type.setBackground(mContext.getResources().getDrawable(R.drawable.ic_mac_blue));
        break;

      case 1:
        //android
        type.setBackground(mContext.getResources().getDrawable(R.drawable.ic_phone));
        break;

      case 2:
        // iphone
        type.setBackground(mContext.getResources().getDrawable(R.drawable.ic_phone));
        break;

      default:
        type.setBackground(mContext.getResources().getDrawable(R.drawable.ic_phone));
        break;
    }*/
    
    move.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onRemoveBlackMem.removeBlackMem(mPostition);
      }
    });
  }

  public interface OnRemoveBlackMem{
     void removeBlackMem(int position);
  }

  public void setOnRemoveBlackMem(OnRemoveBlackMem onRemoveBlackMem){
    this.onRemoveBlackMem = onRemoveBlackMem;
  }
}
