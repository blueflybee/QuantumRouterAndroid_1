package com.fernandocejas.android10.sample.presentation.view.device.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 *      desc: 载入密钥之前检查网络是否可用的弹窗
 *      version: 1.0
 * </pre>
 */

public class NetAvaivablePopupWindow extends PopupWindow implements View.OnClickListener {
  private View mView;
  private TextView mDialogTitle, mDialogContent;
  private Button mDialogNeg, mDialogPos;
  private RelativeLayout mDialogClose;
  private LinearLayout mLlEdit;
  private int mIndex = 0;

  OnPositiveClickListener mOnPositiveClickListener;

  public void setOnPositiveClickListener(OnPositiveClickListener onPositiveClickListener) {
    mOnPositiveClickListener = onPositiveClickListener;
  }

  public NetAvaivablePopupWindow(final Context context) {
    super(context);
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mView = inflater.inflate(R.layout.dialog_check_net_avaiable_pop, null);

    mDialogPos = (Button) mView.findViewById(R.id.dialog_pos);

    mDialogTitle = (TextView) mView.findViewById(R.id.dialog_title);

    mDialogContent = (TextView) mView.findViewById(R.id.dialog_content);

    mDialogPos.setOnClickListener(this);

    this.setContentView(mView);
    this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
    this.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
    //this.setAnimationStyle(R.style.PopScaleAnimation);
    ColorDrawable dw = new ColorDrawable(0xb0000000);
    this.setBackgroundDrawable(dw);

    mDialogTitle.setText("连接断开");
    mDialogContent.setText("请保持手机处于网关网络环境下");

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
      case R.id.dialog_pos:
      /*  if (mOnPositiveClickListener != null) {
          mOnPositiveClickListener.onPositiveClick();
          dismiss();
        }*/
        dismiss();
        break;
      default:
        break;
    }
  }

  public interface OnPositiveClickListener {
    void onPositiveClick();
  }

  public LinearLayout getOuterLayout() {
    return (LinearLayout) mView.findViewById(R.id.ll_outer_layout);
  }

}
