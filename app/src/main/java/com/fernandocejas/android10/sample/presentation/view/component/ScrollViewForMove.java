package com.fernandocejas.android10.sample.presentation.view.component;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * @author 监听顶部标题栏的变化
 * @name ListViewForScrollView
 * @package com.fernandocejas.android10.sample.presentation.view.component
 * @date 15-11-8
 */
public class ScrollViewForMove extends ScrollView{
  private onScrollChangedListener mListener;

  public ScrollViewForMove(Context context) {
    super(context);
  }

  public ScrollViewForMove(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ScrollViewForMove(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public ScrollViewForMove(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected void onScrollChanged(int x, int y, int oldx, int oldy) {
    super.onScrollChanged(x, y, oldx, oldy);

    if (mListener != null) {
      mListener.onScrollChanged(x,y,oldx,oldy);
    }
  }

  public void addOnScrollChangedListener(onScrollChangedListener listener) {
    mListener = listener;
  }

  public interface onScrollChangedListener {
    void onScrollChanged(int x, int y, int oldx, int oldy);
  }

}
