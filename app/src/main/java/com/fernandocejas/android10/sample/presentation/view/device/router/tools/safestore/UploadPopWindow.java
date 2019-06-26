package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.fernandocejas.android10.sample.presentation.R;

/**
 * Created by 86068122 on 2017/2/7.
 * 版本更新弹出框
 */
public class UploadPopWindow extends PopupWindow implements View.OnClickListener {
    private View mView;
    private LinearLayout upload_picture,upload_video,upload_music,upload_other;

    I_OnPictureUploadClickListener I_PictureUploadClickListener;
    I_OnVideoUploadClickListener I_VideoUploadClickListener;
    I_OnMusicUploadClickListener I_MusicUploadClickListener;
    I_OnOtherUploadClickListener I_OtherUploadClickListener;

    //参数传入接口
    public void SetOnPictureUploadClickListener(I_OnPictureUploadClickListener pictureUploadClickListener){
        this.I_PictureUploadClickListener = pictureUploadClickListener;
    }
    public void SetOnVideoUploadClickListener(I_OnVideoUploadClickListener videoUploadClickListener){
        this.I_VideoUploadClickListener = videoUploadClickListener;
    }
    public void SetOnMusicUploadClickListener(I_OnMusicUploadClickListener musicUploadClickListener){
        this.I_MusicUploadClickListener = musicUploadClickListener;
    }
    public void SetOnOtherUploadClickListener(I_OnOtherUploadClickListener otherUploadClickListener){
        this.I_OtherUploadClickListener = otherUploadClickListener;
    }

    public UploadPopWindow(final Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.upload_pop_window, null);

        upload_picture = (LinearLayout) mView.findViewById(R.id.ll_upload_picture);
        upload_video = (LinearLayout) mView.findViewById(R.id.ll_upload_video);
        upload_music = (LinearLayout) mView.findViewById(R.id.ll_upload_music);
        upload_other = (LinearLayout) mView.findViewById(R.id.ll_upload_other);

        upload_picture.setOnClickListener(this);
        upload_video.setOnClickListener(this);
        upload_music.setOnClickListener(this);
        upload_other.setOnClickListener(this);

        this.setContentView(mView);
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        this.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        //this.setAnimationStyle(R.style.PopScaleAnimation);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);

        // mView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mView.findViewById(R.id.outer_layout).getTop();
                int bottom = mView.findViewById(R.id.outer_layout).getBottom();
                int left = mView.findViewById(R.id.outer_layout).getLeft();
                int right = mView.findViewById(R.id.outer_layout).getRight();
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
            case R.id.ll_upload_picture:
                if(I_PictureUploadClickListener != null){
                    I_PictureUploadClickListener.pictureClickListener();
                    dismiss();
                }
                break;
            case R.id.ll_upload_video:
                if(I_VideoUploadClickListener != null){
                    I_VideoUploadClickListener.videoUploadClickListener();
                    dismiss();
                }
                break;

            case R.id.ll_upload_music:
                if(I_MusicUploadClickListener != null){
                    I_MusicUploadClickListener.musicUploadClickListener();
                    dismiss();
                }
                break;

            case R.id.ll_upload_other:
                if(I_OtherUploadClickListener != null){
                    I_OtherUploadClickListener.otherUploadClickListener();
                    dismiss();
                }
                break;

            default:
                break;
        }
    }

    //接口里全是抽象方法
    public interface I_OnPictureUploadClickListener{
        void pictureClickListener();
    }
    public interface I_OnVideoUploadClickListener{
        void videoUploadClickListener();
    }
    public interface I_OnMusicUploadClickListener{
        void musicUploadClickListener();
    }
    public interface I_OnOtherUploadClickListener{
        void otherUploadClickListener();
    }

    public LinearLayout getOuterLayout(){
        return (LinearLayout) mView.findViewById(R.id.outer_layout);
    }

}
