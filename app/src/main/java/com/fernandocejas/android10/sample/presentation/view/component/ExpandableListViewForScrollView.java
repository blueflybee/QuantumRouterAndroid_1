package com.fernandocejas.android10.sample.presentation.view.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/08/31
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class ExpandableListViewForScrollView extends ExpandableListView {
  public ExpandableListViewForScrollView(Context context) {
    super(context);
    // TODO Auto-generated constructor stub
  }

  public ExpandableListViewForScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
    // TODO Auto-generated constructor stub
  }

  public ExpandableListViewForScrollView(Context context, AttributeSet attrs,
                                  int defStyle) {
    super(context, attrs, defStyle);
    // TODO Auto-generated constructor stub
  }
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // TODO Auto-generated method stub
    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
        MeasureSpec.AT_MOST);
    super.onMeasure(widthMeasureSpec, expandSpec);
  }
}
