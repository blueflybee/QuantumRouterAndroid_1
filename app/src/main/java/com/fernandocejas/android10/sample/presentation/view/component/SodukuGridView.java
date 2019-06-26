package com.fernandocejas.android10.sample.presentation.view.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author shaojun
 * @name SodukuGridView
 * @package com.fernandocejas.android10.sample.presentation.view.component
 * @date 15-10-29
 */
public class SodukuGridView extends GridView {

  public SodukuGridView(Context context) {
    super(context);
  }

  public SodukuGridView(Context context, AttributeSet attrs) {
    super(context, attrs);

  }

  public SodukuGridView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
    super.onMeasure(widthMeasureSpec, expandSpec);
  }

}
