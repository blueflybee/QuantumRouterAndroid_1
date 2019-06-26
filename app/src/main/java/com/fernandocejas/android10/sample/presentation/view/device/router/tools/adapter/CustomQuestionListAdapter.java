package com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.WaitAuthBean;

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

public class CustomQuestionListAdapter extends CommAdapter1<WaitAuthBean> {
  private int mIndexSelected = 0;
  private Context mContext;

  public CustomQuestionListAdapter(Context context, List<WaitAuthBean> data, int layoutId, int checkItemPosition) {
    super(context, data, layoutId);
    this.mContext = context;
    this.mIndexSelected = checkItemPosition;
  }

  @Override
  public void convert(BaseViewHolder baseViewHolder, WaitAuthBean bean, int position) {
    TextView content = (TextView) baseViewHolder.getView(R.id.tv_content);
    content.setText(bean.getName());

    if (position != mIndexSelected) {
      ((TextView) baseViewHolder.getView(R.id.tv_content)).setTextColor(mContext.getResources().getColor(R.color.black_424242));
      ((ImageView) baseViewHolder.getView(R.id.img_chooseLogo)).setVisibility(View.GONE);
    } else {
      ((TextView) baseViewHolder.getView(R.id.tv_content)).setTextColor(mContext.getResources().getColor(R.color.blue_2196f3));
      ((ImageView) baseViewHolder.getView(R.id.img_chooseLogo)).setVisibility(View.VISIBLE);
    }

  }

  public void initOtherItem(int index) {
    mIndexSelected = index;
    notifyDataSetChanged();
  }
}
