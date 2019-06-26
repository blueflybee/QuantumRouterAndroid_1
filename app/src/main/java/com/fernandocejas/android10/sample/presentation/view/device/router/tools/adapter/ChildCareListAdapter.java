package com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.ChildCareBean;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.SpecialAttentionBean;
import com.qtec.router.model.rsp.RouterStatusResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class ChildCareListAdapter extends CommAdapter1<ChildCareBean> {
  private Context mContext;

  public ChildCareListAdapter(Context context, List<ChildCareBean> data, int layoutId) {
    super(context, data, layoutId);
    this.mContext = context;
  }

  @Override
  public void convert(BaseViewHolder baseViewHolder, ChildCareBean bean, int position) {
    TextView name = (TextView) baseViewHolder.getView(R.id.tv_child_care_name);
    TextView state = (TextView) baseViewHolder.getView(R.id.tv_state);
    //cb_special_attention
    ImageView head = (ImageView) baseViewHolder.getView(R.id.img_child_care_head);

    if (TextUtils.isEmpty(bean.getStaname())) {
      name.setText("unknow");
    } else {
      name.setText(bean.getStaname());
    }

    if (bean.getIsEnable() == 0) {
      state.setText("关闭");
    } else {
      state.setText("打开");
    }


    switch (bean.getDevicetype()) {
      case 0:
        //computer
        head.setBackground(mContext.getResources().getDrawable(R.drawable.ic_mac_blue));
        break;

      case 1:
        //android
        head.setBackground(mContext.getResources().getDrawable(R.drawable.ic_phone));
        break;

      case 2:
        // iphone
        head.setBackground(mContext.getResources().getDrawable(R.drawable.ic_phone));
        break;

      case 3:
        // iphone
        head.setBackground(mContext.getResources().getDrawable(R.drawable.ic_phone));
        break;

      default:
        break;


    }

  }
}
