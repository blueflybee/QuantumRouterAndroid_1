package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;

/**
 * Created by 86068122 on 2017/2/7.
 * 文件排序
 */
public class SortFilePopWindow extends PopupWindow implements View.OnClickListener {
    private View mView;
    private TextView textview_upload_msg;
    private Button upload_canel;
    private TextView time,name,defaultSort;

    OnTimeClickListener onTimeClickListener;
    OnNameClickListener onNameClickListener;
    OnDefaultClickListener onDefaultClickListener;

    //参数传入接口
    public void setOnTimeClickListener(OnTimeClickListener onTimeClickListener){
        this.onTimeClickListener = onTimeClickListener;
    }
    public void setOnNameClickListener(OnNameClickListener onNameClickListener){
        this.onNameClickListener = onNameClickListener;
    }
    public void setOnDefaultClickListener(OnDefaultClickListener onDefaultClickListener){
        this.onDefaultClickListener = onDefaultClickListener;
    }

    public SortFilePopWindow(final Context context,int sortFlag) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pop_sort_file, null);

        time = (TextView) mView.findViewById(R.id.tv_sort_time);
        name = (TextView) mView.findViewById(R.id.tv_sort_name);
        defaultSort = (TextView) mView.findViewById(R.id.tv_sort_default);

        time.setText("按时间倒序排序");
        name.setText("按文件名称排序");
        defaultSort.setText("默认排序");

        if(sortFlag == 0){
            defaultSort.setTextColor(context.getResources().getColor(R.color.blue_2196f3));
            name.setTextColor(context.getResources().getColor(R.color.common_text_black));
            time.setTextColor(context.getResources().getColor(R.color.common_text_black));
        }else if(sortFlag == 1){
            name.setTextColor(context.getResources().getColor(R.color.blue_2196f3));
            defaultSort.setTextColor(context.getResources().getColor(R.color.common_text_black));
            time.setTextColor(context.getResources().getColor(R.color.common_text_black));
        }else {
            time.setTextColor(context.getResources().getColor(R.color.blue_2196f3));
            defaultSort.setTextColor(context.getResources().getColor(R.color.common_text_black));
            name.setTextColor(context.getResources().getColor(R.color.common_text_black));
        }

        defaultSort.setOnClickListener(this);
        time.setOnClickListener(this);
        name.setOnClickListener(this);

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
            case R.id.tv_sort_time:
                if(onTimeClickListener != null){
                    onTimeClickListener.timeClickListener();
                    dismiss();
                }
                break;
            case R.id.tv_sort_name:
                if(onNameClickListener != null){
                    onNameClickListener.nameClickListener();
                    dismiss();
                }
                break;
            case R.id.tv_sort_default:
                if(onDefaultClickListener != null){
                    onDefaultClickListener.defaultClickListener();
                    dismiss();
                }
                break;

            default:
                break;
        }
    }

    //接口里全是抽象方法
    public interface OnTimeClickListener{
        void timeClickListener();
    }
    public interface OnNameClickListener{
        void nameClickListener();
    }
    public interface OnDefaultClickListener{
        void defaultClickListener();
    }

    public LinearLayout getOuterLayout(){
        return (LinearLayout) mView.findViewById(R.id.outer_layout);
    }

}
