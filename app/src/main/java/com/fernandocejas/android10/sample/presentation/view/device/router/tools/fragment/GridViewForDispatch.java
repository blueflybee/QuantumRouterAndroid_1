package com.fernandocejas.android10.sample.presentation.view.device.router.tools.fragment;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/09/11
 *      desc: 禁止滑动
 *      version: 1.0
 * </pre>
 */

public class GridViewForDispatch extends GridView {
  public GridViewForDispatch(Context context) {
    super(context);
  }

  public GridViewForDispatch(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public GridViewForDispatch(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public GridViewForDispatch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    if(ev.getAction() == MotionEvent.ACTION_MOVE){
      return true;//禁止滑动
    }

    return super.dispatchTouchEvent(ev);
  }
}
