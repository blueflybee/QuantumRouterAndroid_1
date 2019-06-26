package com.fernandocejas.android10.sample.presentation.view.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author shaojun
 * @name ListViewForScrollView
 * @package com.fernandocejas.android10.sample.presentation.view.component
 * @date 15-11-8
 */
public class ListViewForScrollView extends ListView{
  public ListViewForScrollView(Context context) {
    super(context);
  }

  public ListViewForScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ListViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  /**
   * 重写该方法，达到使ListView适应ScrollView的效果
   */
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
        MeasureSpec.AT_MOST);
    super.onMeasure(widthMeasureSpec, expandSpec);
  }
}
