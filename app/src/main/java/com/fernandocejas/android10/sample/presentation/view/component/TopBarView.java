package com.fernandocejas.android10.sample.presentation.view.component;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class TopBarView extends LinearLayout {

    private RelativeLayout mLeftLayout, mMiddleLayout;
    private Button mRightBtn;
    private View mBottomView;
    private TextView mTitleTxt;
    private ImageView mLeftImg;

    public TopBarView(Context context) {
        super(context);
        init(context);
    }

    public TopBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public TopBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.top_bar_view, this, true);

        mLeftLayout = (RelativeLayout) findViewById(R.id.left_layout);
        mMiddleLayout = (RelativeLayout) findViewById(R.id.middle_layout);
        mTitleTxt = (TextView) findViewById(R.id.title_txt);

        mRightBtn = (Button) findViewById(R.id.right_btn);
        mBottomView = findViewById(R.id.bottom_view);
    }

    public void setBackground(@ColorRes int color) {

        findViewById(R.id.top_bar_out_layout).setBackgroundColor(getResources().getColor(color));
    }

    public RelativeLayout getLeftLayout() {
        return mLeftLayout;
    }

    public RelativeLayout getMiddleLayout() {
        return mMiddleLayout;
    }

    /**
     * 获取右侧按钮控件
     *
     * @return
     */
    public Button getRightBtn() {
        return mRightBtn;
    }

    /**
     * 获取底部线条控件
     *
     * @return
     */
    public View getBottomView() {
        return mBottomView;
    }

    /**
     * 获取标题控件
     *
     * @return
     */
    public TextView getTitleTxt() {
        return mTitleTxt;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        mTitleTxt.setText(title);
    }

    public void setTitleColor(int resource) {
        mTitleTxt.setTextColor(getResources().getColor(resource));
    }

    /**
     * 设置右侧按钮文字
     *
     * @param text
     */
    public void setRightBtnText(String text) {
        mRightBtn.setText(text);
    }

    /**
     * 设置左侧图片背景
     *
     * @param resource
     */
    public void setLeftImgBg(int resource) {
        mLeftImg.setImageResource(resource);
    }

    /**
     * 设置左侧View显隐
     *
     * @param visibility
     */
    public void setLeftLayoutVisibility(int visibility) {
        mLeftLayout.setVisibility(visibility);
    }

    /**
     * 设置中间View显隐
     *
     * @param visibility
     */
    public void setMiddleLayoutVisibility(int visibility) {
        mMiddleLayout.setVisibility(visibility);
    }

    /**
     * 设置底部View显隐
     *
     * @param visibility
     */
    public void setBottomViewVisibility(int visibility) {
        mBottomView.setVisibility(visibility);
    }

    /**
     * 设置右侧按钮显隐
     *
     * @param visibility
     */
    public void setRightBtnVisibility(int visibility) {
        mRightBtn.setVisibility(visibility);
    }

}
