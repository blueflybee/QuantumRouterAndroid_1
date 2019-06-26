package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自定义 GridView
 */
public class GridViewForExpandableListView extends GridView {

    public GridViewForExpandableListView(Context context) {
        super(context);
    }

    public GridViewForExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewForExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写该方法，达到使 GridView 适应 ExpandableListView 的效果
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
