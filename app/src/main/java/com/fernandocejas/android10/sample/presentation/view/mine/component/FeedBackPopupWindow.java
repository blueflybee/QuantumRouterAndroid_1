package com.fernandocejas.android10.sample.presentation.view.mine.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
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

import com.blankj.utilcode.util.KeyboardUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback.FeedBackAdviceActivity;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/07
 *      desc: 提交意见反馈联系方式的弹窗
 *      version: 1.0
 * </pre>
 */

public class FeedBackPopupWindow extends PopupWindow implements View.OnClickListener {
    private View mView;
    private TextView mDialogMsg, mDialogPos, mDialogNum, mDialogTitle;
    private RelativeLayout mDialogNeg;
    private Context context;

    OnPositiveClickListener mOnPositiveClickListener;

    public void setOnPositiveClickListener(OnPositiveClickListener onPositiveClickListener) {
        mOnPositiveClickListener = onPositiveClickListener;
    }

    public FeedBackPopupWindow(final Context context) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.dialog_feedback, null);

        mDialogMsg = (TextView) mView.findViewById(R.id.dialog_msg);
        mDialogNeg = (RelativeLayout) mView.findViewById(R.id.rl_close);
        mDialogPos = (TextView) mView.findViewById(R.id.dialog_pos);
        //mDialogNum = (TextView) mView.findViewById(R.id.et_phoneNum);
        mDialogTitle = (TextView) mView.findViewById(R.id.dialog_title);

        mDialogMsg.setText("您的反馈我们已经收到，为了配合我们技术人员的\n后续工作，请提供您的联系方式！");
        mDialogPos.setText("确认");

        mDialogTitle.setText("您的反馈已提交");

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
                    if (y < height || x < left || x > right) {
                        dismiss(); //PopuWindow以上
                    }
                }
                return true;
            }
        });

        //控制表情输入
        /*InputUtil.watchEditTextNoClear((EditText) mView.findViewById(R.id.et_phoneNum));*/
        ((EditText) mView.findViewById(R.id.et_phoneNum)).setFilters(InputUtil.emojiFilters);

        //控制button置灰与否
        InputWatcher watcher = new InputWatcher();
       /* InputWatcher.WatchCondition numCondition = new InputWatcher.WatchCondition();
        numCondition.setRange(new InputWatcher.InputRange(1, 31));*/
        watcher.addEt((EditText) mView.findViewById(R.id.et_phoneNum));
        watcher.setInputListener(isEmpty -> {
            mDialogPos.setClickable(!isEmpty);
            if (isEmpty) {
                mDialogPos.setBackgroundColor(context.getResources().getColor(R.color.gray));
            } else {
                mDialogPos.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
            }
        });

        InputUtil.allowLetterNumberChinese(getPhoneNumEdit(), 30);

        //显示键盘
        //KeyboardUtils.showSoftInput((EditText) mView.findViewById(R.id.et_phoneNum));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_close:
                dismiss();
                //隐藏键盘
                new Handler().postDelayed(
                    () -> KeyboardUtils.hideSoftInput((Activity) context), 180);
                break;

            case R.id.dialog_pos:
                if (mOnPositiveClickListener != null) {
                    mOnPositiveClickListener.onPositiveClick();
                    /*// 只有满足输入正确的手机号点击确定才会消失
                    String phoneNum = getPhoneNumEdit().toString().trim();
                    if((!TextUtils.isEmpty(phoneNum))&&(phoneNum.length() == 11)){
                        dismiss();
                    }*/
                    dismiss();

                    //KeyboardUtils.hideSoftInput((Activity) context);//隐藏键盘
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

    public EditText getPhoneNumEdit() {
        return (EditText) mView.findViewById(R.id.et_phoneNum);
    }
}
