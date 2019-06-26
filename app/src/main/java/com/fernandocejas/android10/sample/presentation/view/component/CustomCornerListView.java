package com.fernandocejas.android10.sample.presentation.view.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import com.fernandocejas.android10.sample.presentation.utils.DensityUtil;

public class CustomCornerListView extends LinearLayout {
  private BaseAdapter mAdapter;
  private Context mContext;

  private OnItemViewClickListener mListener;

  public CustomCornerListView(Context context) {
    super(context);
    init(context);
  }

  public CustomCornerListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  private void init(Context context) {
    mContext = context;
    setOrientation(VERTICAL);
  }

  public BaseAdapter getAdapter() {
    return mAdapter;
  }

  public void setAdapter(BaseAdapter adapter) {
    this.mAdapter = adapter;
    bindLinearLayout();
  }

  /**
   * 绑定布局
   */
  public void bindLinearLayout() {
    removeAllViews();
    int count = mAdapter.getCount();

    for (int i = 0; i < count; i++) {
      View v = mAdapter.getView(i, null, null);
      v.setTag(i);
      //int resid = -1;
      //if(i == 0){
      //	resid = R.drawable.shape_top_corner_no_bottom_line;
      //}else if(i == count - 1){
      //	if(i % 2 != 0){
      //		resid = R.drawable.shape_bottom_corner_no_top_line_a;
      //	}else{
      //		resid = R.drawable.shape_bottom_corner_no_top_line;
      //	}
      //}else{
      //	if(i % 2 != 0){
      //		resid = R.drawable.shape_no_corner_a;
      //	}else{
      //		resid = R.drawable.shape_no_corner;
      //	}
      //}

      //v.setBackgroundResource(resid);
      int px = DensityUtil.dip2px(mContext, 10);
      v.setPadding(0, px, 0, px);

      final int finalI = i;
      v.setOnClickListener(new OnClickListener() {
        @Override public void onClick(View view) {

          if (mListener != null) {
            mListener.onItemClicked(finalI, mAdapter.getItem(finalI));
          }
        }
      });

      addView(v);
    }
  }

  public void setOnItemViewClickListener(OnItemViewClickListener listener) {
    this.mListener = listener;
  }

  public interface OnItemViewClickListener {
    void onItemClicked(int position, Object item);
  }
}
