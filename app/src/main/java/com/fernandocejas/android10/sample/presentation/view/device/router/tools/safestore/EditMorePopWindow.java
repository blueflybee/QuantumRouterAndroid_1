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
 * 文件编辑弹框
 */
public class EditMorePopWindow extends PopupWindow implements View.OnClickListener{

    private View mView;
    private LinearLayout mLlEdit,mLlPictureRestore;

    I_OnEditClickListener I_EditClickListener;
    I_OnPictureRestoreClickListener I_PictureRestoreClickListener;

    //参数传入接口
    public void setOnEditClickListener(I_OnEditClickListener I_EditClickListener){
        this.I_EditClickListener = I_EditClickListener;
    }
    public void setOnPictureRestoreClickListener(I_OnPictureRestoreClickListener I_PictureRestoreClickListener){
        this.I_PictureRestoreClickListener = I_PictureRestoreClickListener;
    }

    public EditMorePopWindow(final Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.edit_pop_window, null);

        mLlEdit = (LinearLayout) mView.findViewById(R.id.ll_edit);
        mLlPictureRestore = (LinearLayout) mView.findViewById(R.id.ll_picRestore);

        mLlEdit.setOnClickListener(this);
        mLlPictureRestore.setOnClickListener(this);

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
            case R.id.ll_edit:
                if(I_EditClickListener != null){
                    I_EditClickListener.eidtClickListener();
                    dismiss();
                }
                break;

            case R.id.ll_picRestore:
                if(I_PictureRestoreClickListener != null){
                    I_PictureRestoreClickListener.pictureRestoreClickListener();
                    dismiss();
                }
                break;

            default:
                break;
        }
    }

    //接口里全是抽象方法
    public interface I_OnEditClickListener{
        void eidtClickListener();
    }
    public interface I_OnPictureRestoreClickListener{
        void pictureRestoreClickListener();
    }

    public LinearLayout getOuterLayout(){
        return (LinearLayout) mView.findViewById(R.id.outer_layout);
    }

}
