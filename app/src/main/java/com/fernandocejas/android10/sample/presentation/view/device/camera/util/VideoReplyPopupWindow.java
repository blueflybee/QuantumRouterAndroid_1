package com.fernandocejas.android10.sample.presentation.view.device.camera.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blueflybee.uilibrary1.progressbar.VideoDownloadProgressBar;
import com.fernandocejas.android10.sample.presentation.R;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/07
 *      desc: 摄像机 弹窗
 *      version: 1.0
 * </pre>
 */

public class VideoReplyPopupWindow extends PopupWindow implements View.OnClickListener {
    private View mView;
    private TextView mDialogTitle,mDialogContent,mDialogDownloadState;
    private VideoDownloadProgressBar mDialodProgressBar;
    private int mProgress = 0;
    private RelativeLayout mDialogClose;

    OnPositiveClickListener mOnPositiveClickListener;

    public void setOnPositiveClickListener(OnPositiveClickListener onPositiveClickListener) {
        mOnPositiveClickListener = onPositiveClickListener;
    }

    public VideoReplyPopupWindow(final Context context,int downLoadFileSize,int hasDownLoadSize) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.dialog_video_reply_pop, null);

        mDialogClose = (RelativeLayout) mView.findViewById(R.id.rl_close);

        mDialogTitle = (TextView) mView.findViewById(R.id.dialog_title);

        mDialogContent = (TextView) mView.findViewById(R.id.dialog_content);
        mDialodProgressBar = mView.findViewById(R.id.dialog_download_progress);
        mDialogDownloadState = (TextView) mView.findViewById(R.id.dialog_download_state);

        mDialogClose.setOnClickListener(this);

        //视频下载
        mDialogTitle.setText("下载录像");
        mDialogContent.setText("正在全力下载录像，请等待");
        updateDownloadProgress(downLoadFileSize,hasDownLoadSize);

        this.setContentView(mView);
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        this.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        //this.setAnimationStyle(R.style.PopScaleAnimation);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);

        // mView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
   /*     mView.setOnTouchListener(new View.OnTouchListener() {

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
        });*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_close:
                dismiss();
                break;

        /*    case R.id.dialog_neg:
                dismiss();
                break;

            case R.id.dialog_pos:
                if ((mOnPositiveClickListener != null)) {
                    mOnPositiveClickListener.onPositiveClick(mProgress);
                    dismiss();
                }
                break;*/

            default:
                break;
        }
    }

    public interface OnPositiveClickListener {
        void onPositiveClick(int progress);
    }

    public LinearLayout getOuterLayout() {
        return (LinearLayout) mView.findViewById(R.id.ll_outer_layout);
    }

    /**
    * 更新下载进度
    *
    * @param
    * @return
    */
    public void updateDownloadProgress(int mDownLoadFileSize,int mHasDownLoadSize){
        mDialogDownloadState.setText(mHasDownLoadSize+"KB / "+mDownLoadFileSize+"KB");
        if(mHasDownLoadSize < mDownLoadFileSize){
            mDialodProgressBar.setProgress((int)(100*((float)mHasDownLoadSize / mDownLoadFileSize)));
        }else {
            mDialodProgressBar.setProgress(100);
        }

    }

}
