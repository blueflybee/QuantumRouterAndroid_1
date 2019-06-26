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
import android.widget.TextView;

import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.fernandocejas.android10.sample.presentation.R;
import com.qtec.mapp.model.rsp.GetMsgDetailResponse;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/07
 *      desc: 邀请消息的弹窗
 *      version: 1.0
 * </pre>
 */

public class MessagePopupWindow extends PopupWindow implements View.OnClickListener {
    private View mView;
    private TextView mDialogNickName,mDialogContent,mDialogNeg, mDialogPos;
    private ImageView mDialogHead;

    OnPositiveClickListener mOnPositiveClickListener;

    public void setOnPositiveClickListener(OnPositiveClickListener onPositiveClickListener) {
        mOnPositiveClickListener = onPositiveClickListener;
    }

    public MessagePopupWindow(final Context context, CPushMessage response) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.dialog_message_notification, null);

        mDialogHead = (ImageView) mView.findViewById(R.id.dialog_head);
        mDialogNickName = (TextView) mView.findViewById(R.id.dialog_nickName);
        mDialogContent = (TextView) mView.findViewById(R.id.dialog_content);
        mDialogNeg = (TextView) mView.findViewById(R.id.dialog_neg);
        mDialogPos = (TextView) mView.findViewById(R.id.dialog_pos);

        mDialogNeg.setText("取消");
        mDialogPos.setText("接受");
        if(response != null){
            /*mDialogContent.setText(response.getMessageContent());
            mDialogNickName.setText(response.getShareUserName());*/
        }


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
            case R.id.dialog_neg:
                dismiss();
                break;

            case R.id.dialog_pos:
                if ((mOnPositiveClickListener != null)) {
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
