package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/07
 *      desc: 门锁配对 弹窗
 *      version: 1.0
 * </pre>
 */

public class AboutCatEyePopupWindow extends PopupWindow implements View.OnClickListener {
    private View mView;
    private TextView mDialogTitle,mDialogContent;
    private Button mDialogPos,mDialogNeg;
    private RelativeLayout mRelayoutClose;
    private LinearLayout mLlCameraPosition;
    private ImageView mImgZheng,mImgDao;
    private int mIndex = 0;

    OnPositiveClickListener mOnPositiveClickListener;

    public void setOnPositiveClickListener(OnPositiveClickListener onPositiveClickListener) {
        mOnPositiveClickListener = onPositiveClickListener;
    }

    public AboutCatEyePopupWindow(final Context context,int flag,Boolean data) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.dialog_about_cat_eye, null);

        mRelayoutClose = (RelativeLayout) mView.findViewById(R.id.rl_close);
        mDialogPos = (Button) mView.findViewById(R.id.dialog_pos);
        mDialogNeg = (Button) mView.findViewById(R.id.dialog_neg);
        mDialogTitle = (TextView) mView.findViewById(R.id.dialog_title);
        mDialogContent = (TextView) mView.findViewById(R.id.dialog_content);
        mLlCameraPosition = (LinearLayout) mView.findViewById(R.id.ll_camera_position);
        mImgZheng = (ImageView) mView.findViewById(R.id.dialog_img_zheng);
        mImgDao = (ImageView) mView.findViewById(R.id.dialog_img_dao);

        if(flag == 0){
            //重启
            mLlCameraPosition.setVisibility(View.GONE);

            if(AppConstant.DEVICE_TYPE_CAT.equals(GlobleConstant.getgDeviceType())){
                mDialogTitle.setText("重启猫眼");
                mDialogPos.setText("确认");
                mDialogNeg.setText("取消");
                mDialogContent.setText("确定重启猫眼吗？");
            }else if(AppConstant.DEVICE_TYPE_CAMERA.equals(GlobleConstant.getgDeviceType())){
                mDialogTitle.setText("重启摄像机");
                mDialogPos.setText("确认");
                mDialogNeg.setText("取消");
                mDialogContent.setText("确定重启摄像机吗？");
            }

        }else if(flag == 1){
            //重置
            mLlCameraPosition.setVisibility(View.GONE);

            if(AppConstant.DEVICE_TYPE_CAT.equals(GlobleConstant.getgDeviceType())){
                mDialogTitle.setText("重置猫眼");
                mDialogPos.setText("确认");
                mDialogNeg.setText("取消");
                mDialogContent.setText("重置后设备将恢复出厂设置，确定重置设备吗？");
            }else if(AppConstant.DEVICE_TYPE_CAMERA.equals(GlobleConstant.getgDeviceType())){
                mDialogTitle.setText("重置摄像机");
                mDialogPos.setText("确认");
                mDialogNeg.setText("取消");
                mDialogContent.setText("重置后设备将恢复出厂设置，确定重置设备吗？");
            }

        }else if(flag == 2){
            //重新配置网络
            mLlCameraPosition.setVisibility(View.GONE);

            mDialogTitle.setText("重新配置网络");
            mDialogPos.setText("确认");
            mDialogNeg.setText("取消");
            mDialogContent.setText("您已绑定该设备，是否重新配置网络?");
        }else if(flag == 3){
            //摄像机摆放方式
            mLlCameraPosition.setVisibility(View.VISIBLE);
            mDialogTitle.setText("摄像机摆放方式");
            mDialogContent.setText("请选择摄像机摆放方式来调整画面方向");

            if(data){
                //倒着放
                mImgDao.setBackground(context.getResources().getDrawable(R.drawable.ic_pic_dao_pressed));
                mImgZheng.setBackground(context.getResources().getDrawable(R.drawable.ic_pic_zheng));
            }else {
                //正着放
                mImgDao.setBackground(context.getResources().getDrawable(R.drawable.ic_pic_dao));
                mImgZheng.setBackground(context.getResources().getDrawable(R.drawable.ic_pic_zheng_pressed));
            }

            mImgZheng.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImgDao.setBackground(context.getResources().getDrawable(R.drawable.ic_pic_dao));
                    mImgZheng.setBackground(context.getResources().getDrawable(R.drawable.ic_pic_zheng_pressed));
                    mIndex = 1;
                }
            });

            mImgDao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImgDao.setBackground(context.getResources().getDrawable(R.drawable.ic_pic_dao_pressed));
                    mImgZheng.setBackground(context.getResources().getDrawable(R.drawable.ic_pic_zheng));
                    mIndex = 0;
                }
            });

        }else if(flag == 4){
            //分享md5校验
            mLlCameraPosition.setVisibility(View.GONE);

            mDialogTitle.setText("扫描不成功");
            mDialogPos.setText("确认");
            mDialogNeg.setText("取消");
            mDialogContent.setText("请确认是产品二维码或家人分享的设备邀请码");
        }

        mDialogNeg.setOnClickListener(this);
        mDialogPos.setOnClickListener(this);
        mRelayoutClose.setOnClickListener(this);

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
                    mOnPositiveClickListener.onPositiveClick(mIndex);
                    dismiss();
                }
                break;

            case R.id.dialog_neg:
                dismiss();
                break;
            default:
                break;
        }
    }

    public interface OnPositiveClickListener {
        void onPositiveClick(int index);
    }


    public LinearLayout getOuterLayout() {
        return (LinearLayout) mView.findViewById(R.id.exit_outer_layout);
    }

}
