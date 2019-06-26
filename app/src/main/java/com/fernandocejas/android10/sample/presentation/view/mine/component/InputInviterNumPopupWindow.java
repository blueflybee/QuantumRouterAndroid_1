package com.fernandocejas.android10.sample.presentation.view.mine.component;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/07
 *      desc: 输入邀请人号码的弹窗
 *      version: 1.0
 * </pre>
 */

public class InputInviterNumPopupWindow extends PopupWindow implements View.OnClickListener {
    private View mView;
    private TextView mDialogInputNum, mDialogSelectNum,mDialogPrompt,mDialogPos, mDialogTitle;
    private RelativeLayout mDialogNeg;

    OnPositiveClickListener mOnPositiveClickListener;
    OnSelectNumClickListener mOnSelectNumClickListener;

    public void setOnPositiveClickListener(OnPositiveClickListener onPositiveClickListener) {
        mOnPositiveClickListener = onPositiveClickListener;
    }

    public void setmOnSelectNumClickListener(OnSelectNumClickListener onSelectNumClickListener) {
        mOnSelectNumClickListener = onSelectNumClickListener;
    }

    public InputInviterNumPopupWindow(final Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.dialog_input_inviter_num, null);

        mDialogInputNum = (TextView) mView.findViewById(R.id.dialog_input_num);
        mDialogSelectNum = (TextView) mView.findViewById(R.id.dialog_select_num);
        //mDialogPrompt = (TextView) mView.findViewById(R.id.dialog_prompt);
        mDialogNeg = (RelativeLayout) mView.findViewById(R.id.rl_close);
        mDialogPos = (TextView) mView.findViewById(R.id.dialog_pos);
        mDialogTitle = (TextView) mView.findViewById(R.id.dialog_title);

        mDialogTitle.setText("邀请朋友");
        //mDialogNeg.setText("取消");
        mDialogPos.setText("邀请");
        //mDialogPrompt.setText("需要输入包含11位数字的电话号码");

        mDialogNeg.setOnClickListener(this);
        mDialogPos.setOnClickListener(this);
        mDialogSelectNum.setOnClickListener(this);

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

        //控制button置灰与否
        InputWatcher watcher = new InputWatcher();
        watcher.addEt((EditText) mView.findViewById(R.id.dialog_input_num));
        watcher.setInputListener(isEmpty -> {
            mDialogPos.setClickable(!isEmpty);
            if (isEmpty) {
                mDialogPos.setBackgroundColor(context.getResources().getColor(R.color.gray));
            } else {
                mDialogPos.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_close:
                dismiss();
                break;

            case R.id.dialog_select_num:
                if( mOnSelectNumClickListener != null){
                    mOnSelectNumClickListener.onSelectNumClick();
                   // dismiss();
                }
                break;

            case R.id.dialog_pos:
                if ((mOnPositiveClickListener != null)&&(!TextUtils.isEmpty(getInputNumEdit().getText().toString().trim()))) {
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

    public interface OnSelectNumClickListener {

        void onSelectNumClick();
    }

    public LinearLayout getOuterLayout() {
        return (LinearLayout) mView.findViewById(R.id.exit_outer_layout);
    }

    public EditText getInputNumEdit() {
        return (EditText) mView.findViewById(R.id.dialog_input_num);
    }
}
