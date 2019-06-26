package com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.SpecialAttentionBean;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.WifiTimeIntervalBean;
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

public class AddAttentionListAdapter extends CommAdapter1<RouterStatusResponse.Status> {
  public Map<Integer, Boolean> checkedMap; // 保存checkbox是否被选中的状态
  private Context mContext;

  public AddAttentionListAdapter(Context context, List<RouterStatusResponse.Status> data, int layoutId) {
    super(context, data, layoutId);
    mContext = context;
    checkedMap = new HashMap<Integer, Boolean>();
    for (int i = 0; i < data.size(); i++) {
      checkedMap.put(i, false);//checked未被选中
    }
  }

  @Override
  public void convert(BaseViewHolder baseViewHolder, RouterStatusResponse.Status bean, int position) {
    TextView name = (TextView) baseViewHolder.getView(R.id.tv_attention_name);
    //cb_special_attention
    ImageView head = (ImageView) baseViewHolder.getView(R.id.img_attention_head);

    if(TextUtils.isEmpty(bean.getStaname())){
      name.setText("未知设备");
    }else{
      name.setText(bean.getStaname());
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


    CheckBox mCB = (CheckBox) baseViewHolder.getView(R.id.cb_special_attention);

    try {
      mCB.setTag(position);
      mCB.setChecked(checkedMap.get(position));
    } catch (Exception e) {
      e.printStackTrace();
    }

    mCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        checkedMap.put((Integer) buttonView.getTag(), isChecked);
      }
    });
  }
}
