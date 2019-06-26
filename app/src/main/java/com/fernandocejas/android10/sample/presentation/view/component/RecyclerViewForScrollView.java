package com.fernandocejas.android10.sample.presentation.view.component;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author shaojun
 * @name ListViewForScrollView
 * @package com.fernandocejas.android10.sample.presentation.view.component
 * @date 15-11-8
 */
public class RecyclerViewForScrollView extends RecyclerView{
  public RecyclerViewForScrollView(Context context) {
    super(context);
  }

  public RecyclerViewForScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RecyclerViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  /**
   * 重写该方法，达到使RecyclerView适应ScrollView的效果
   */
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
        MeasureSpec.AT_MOST);
    super.onMeasure(widthMeasureSpec, expandSpec);
  }
}
