package com.fernandocejas.android10.sample.presentation.view.mine.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.fernandocejas.android10.sample.presentation.R;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/07
 *      desc: 选择头像的弹窗
 *      version: 1.0
 * </pre>
 */

public class SelectHeadPopupWindow extends PopupWindow implements View.OnClickListener {
    private Button btn_take_photo;
    private Button btn_pick_photo;
    private View mMenuView;
    private Button mBtnCancel;

    /**
     * @param context
     * @param itemsOnClick
     * @param isSelectPic
     * @param strs
     */
    public SelectHeadPopupWindow(final Activity context, View.OnClickListener itemsOnClick, final boolean isSelectPic, String[] strs) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.ppt_select_head, null);

        btn_take_photo = (Button) mMenuView.findViewById(R.id.btn_take_photo);
        btn_pick_photo = (Button) mMenuView.findViewById(R.id.btn_pick_photo);
        mBtnCancel = (Button) mMenuView.findViewById(R.id.btn_pick_cacel);

        btn_take_photo.setText(strs[0]);
        btn_pick_photo.setText(strs[1]);
        mBtnCancel.setOnClickListener(this);

        if (!isSelectPic) {
            btn_take_photo.setVisibility(View.GONE);
            btn_pick_photo.setVisibility(View.GONE);
        }
        // 设置按钮监听
        btn_pick_photo.setOnClickListener(itemsOnClick);
        btn_take_photo.setOnClickListener(itemsOnClick);
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
//        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        // 设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
        //this.setAnimationStyle(R.style.PopAnimation);
        this.setBackgroundDrawable(new BitmapDrawable());
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int bottom = mMenuView.findViewById(R.id.pop_layout)
                        .getBottom();
                int left = mMenuView.findViewById(R.id.pop_layout).getLeft();
                int right = mMenuView.findViewById(R.id.pop_layout).getRight();
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height || y > bottom || x < left || x > right) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                //DeviceUtil.setBackgroundAlpha(context, 1f);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pick_cacel:
                dismiss();
                break;
            default:
                break;
        }
    }

    public LinearLayout getOuterLayout() {
        return (LinearLayout) mMenuView.findViewById(R.id.ll_outer);
    }

}
