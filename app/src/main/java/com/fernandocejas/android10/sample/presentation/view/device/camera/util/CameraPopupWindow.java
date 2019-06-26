package com.fernandocejas.android10.sample.presentation.view.device.camera.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/07
 *      desc: 摄像机 弹窗
 *      version: 1.0
 * </pre>
 */

public class CameraPopupWindow extends PopupWindow implements View.OnClickListener {
    private View mView;
    private TextView mDialogTitle,mDialogContent,mProgressNum;
    private Button mDialogNeg,mDialogPos;
    private RelativeLayout mDialogClose,mChooseAccuracy;
    private SeekBar mSeekBar;
    private int mProgress = 0;

    OnPositiveClickListener mOnPositiveClickListener;

    public void setOnPositiveClickListener(OnPositiveClickListener onPositiveClickListener) {
        mOnPositiveClickListener = onPositiveClickListener;
    }

    public CameraPopupWindow(final Context context,String type,int data) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.dialog_choose_accuracy_pop, null);

        mDialogClose = (RelativeLayout) mView.findViewById(R.id.rl_close);
        mDialogPos = (Button) mView.findViewById(R.id.dialog_pos);
        mDialogNeg = (Button) mView.findViewById(R.id.dialog_neg);

        mDialogTitle = (TextView) mView.findViewById(R.id.dialog_title);
        mSeekBar = (SeekBar)mView.findViewById(R.id.dialog_seek_bar);
        mProgressNum = (TextView) mView.findViewById(R.id.dialog_progress_num);
        mChooseAccuracy = (RelativeLayout) mView.findViewById(R.id.rl_choose_accuracy);
        mDialogContent = (TextView) mView.findViewById(R.id.dialog_format_content);

        mDialogNeg.setOnClickListener(this);
        mDialogPos.setOnClickListener(this);
        mDialogClose.setOnClickListener(this);

        if("0".equals(type)){
            //移动精度
            mChooseAccuracy.setVisibility(View.VISIBLE);
            mDialogContent.setVisibility(View.GONE);

            mDialogTitle.setText("移动侦测灵敏度");
            mProgress = data;

            mSeekBar.setMax(100);

            mProgressNum.setText(""+mProgress);
            mSeekBar.setProgress(mProgress);


            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mProgress = progress;
                   /* if(mProgress < 3){
                        mProgress = 3;//最小值为3
                    }*/

                    mProgressNum.setText(""+mProgress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }else if("1".equals(type)){
            //格式化
            mChooseAccuracy.setVisibility(View.GONE);
            mDialogContent.setVisibility(View.VISIBLE);
            mDialogTitle.setText("格式化");

            mDialogContent.setText("请先选择停止录像保存后，再进行格式化");
        }else {
            //云台调节速度
            mChooseAccuracy.setVisibility(View.VISIBLE);
            mDialogContent.setVisibility(View.GONE);

            mDialogTitle.setText("云台调节速度");
            mProgress = data;

            mSeekBar.setMax(255);

            mProgressNum.setText(""+mProgress);
            mSeekBar.setProgress(mProgress);

            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mProgress = progress;
                    if(mProgress < 3){
                        mProgress = 3;//最小值为3
                    }

                    mProgressNum.setText(""+mProgress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        this.setContentView(mView);
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        this.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        //this.setAnimationStyle(R.style.PopScaleAnimation);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);

        // mView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mView.findViewById(R.id.ll_outer_layout).getTop();
                int bottom = mView.findViewById(R.id.ll_outer_layout).getBottom();
                int left = mView.findViewById(R.id.ll_outer_layout).getLeft();
                int right = mView.findViewById(R.id.ll_outer_layout).getRight();
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

            case R.id.dialog_neg:
                dismiss();
                break;

            case R.id.dialog_pos:
                if ((mOnPositiveClickListener != null)) {
                    mOnPositiveClickListener.onPositiveClick(mProgress,mDialogPos);
                    dismiss();
                }
                break;

            default:
                break;
        }
    }

    public interface OnPositiveClickListener {
        void onPositiveClick(int progress,View v);
    }

    public LinearLayout getOuterLayout() {
        return (LinearLayout) mView.findViewById(R.id.ll_outer_layout);
    }

    public Button getPosBtn() {
        return (Button) mView.findViewById(R.id.dialog_pos);
    }

    public int getmProgress() {
        return mProgress;
    }

}
