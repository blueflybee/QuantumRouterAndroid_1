package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/07
 *      desc: 门锁配对 弹窗
 *      version: 1.0
 * </pre>
 */

public class ConnectLockPopupWindow extends PopupWindow implements View.OnClickListener {
    private View mView;
    private TextView mDialogPos, mDialogTitle,mDialogContent;
    private RelativeLayout mDialogNeg;

    OnPositiveClickListener mOnPositiveClickListener;

    public void setOnPositiveClickListener(OnPositiveClickListener onPositiveClickListener) {
        mOnPositiveClickListener = onPositiveClickListener;
    }

    public ConnectLockPopupWindow(final Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.dialog_connect_lock, null);

        mDialogNeg = (RelativeLayout) mView.findViewById(R.id.rl_close);
        mDialogPos = (TextView) mView.findViewById(R.id.dialog_pos);
        mDialogTitle = (TextView) mView.findViewById(R.id.dialog_title);
        mDialogContent = (TextView) mView.findViewById(R.id.dialog_content);

        mDialogTitle.setText("配对门锁");
        //mDialogNeg.setText("取消");
        mDialogPos.setText("开始配对");
        mDialogContent.setText("配对门锁后，当收到门铃时，系统会提供快\n捷的远程开门方式");
        //mDialogPrompt.setText("需要输入包含11位数字的电话号码");

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
                // ACTION_UP 离开触屏
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height || x < left || x > right || y > bottom) {
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
            case R.id.rl_close:
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
