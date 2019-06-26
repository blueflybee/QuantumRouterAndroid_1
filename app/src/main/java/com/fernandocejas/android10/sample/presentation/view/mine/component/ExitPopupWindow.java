package com.fernandocejas.android10.sample.presentation.view.mine.component;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/07
 *      desc: 退出登录的弹窗
 *      version: 1.0
 * </pre>
 */

public class ExitPopupWindow extends PopupWindow implements View.OnClickListener {
    private View mView;
    private TextView mDialogMsg;
    private TextView mDialogNeg;
    private TextView mDialogPos;

    OnPositiveClickListener mOnPositiveClickListener;

    public void setOnPositiveClickListener(OnPositiveClickListener onPositiveClickListener) {
        mOnPositiveClickListener = onPositiveClickListener;
    }

    public ExitPopupWindow(final Context context, String msg, String neg, String pos) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.dialog_exit, null);

        mDialogMsg = (TextView) mView.findViewById(R.id.dialog_msg);
        mDialogNeg = (TextView) mView.findViewById(R.id.dialog_neg);
        mDialogPos = (TextView) mView.findViewById(R.id.dialog_pos);

        mDialogMsg.setText(msg);
        mDialogNeg.setText(neg);
        mDialogPos.setText(pos);

        mDialogNeg.setOnClickListener(this);
        mDialogPos.setOnClickListener(this);

        this.setContentView(mView);
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        this.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        //this.setAnimationStyle(R.style.PopScaleAnimation);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);

        // mView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mView.findViewById(R.id.exit_outer_layout).getTop();
                int bottom = mView.findViewById(R.id.exit_outer_layout).getBottom();
                int left = mView.findViewById(R.id.exit_outer_layout).getLeft();
                int right = mView.findViewById(R.id.exit_outer_layout).getRight();
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_neg:
                dismiss();
                break;
            case R.id.dialog_pos:
                if (mOnPositiveClickListener != null) {
                    mOnPositiveClickListener.onPositiveClick();
                    dismiss();
                }
                break;
            default:
                break;
        }
    }

    public interface OnPositiveClickListener {

        void onPositiveClick();
    }

    public LinearLayout getOuterLayout() {
        return (LinearLayout) mView.findViewById(R.id.exit_outer_layout);
    }
}
