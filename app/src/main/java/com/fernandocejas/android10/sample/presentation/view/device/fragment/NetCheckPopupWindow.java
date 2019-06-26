package com.fernandocejas.android10.sample.presentation.view.device.fragment;

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
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/07
 *      desc: Samba文件操作的弹窗
 *      version: 1.0
 * </pre>
 */

public class NetCheckPopupWindow extends PopupWindow implements View.OnClickListener {
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

  public NetCheckPopupWindow(final Context context) {
    super(context);
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mView = inflater.inflate(R.layout.dialog_check_net_pop, null);

    mDialogClose = (RelativeLayout) mView.findViewById(R.id.rl_close);
    mDialogPos = (Button) mView.findViewById(R.id.dialog_pos);

    mDialogTitle = (TextView) mView.findViewById(R.id.dialog_title);

    mDialogContent = (TextView) mView.findViewById(R.id.dialog_content);

    mDialogPos.setOnClickListener(this);
    mDialogClose.setOnClickListener(this);

    this.setContentView(mView);
    this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
    this.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
    //this.setAnimationStyle(R.style.PopScaleAnimation);
    ColorDrawable dw = new ColorDrawable(0xb0000000);
    this.setBackgroundDrawable(dw);

    mDialogTitle.setText("网络连接错误");
    mDialogContent.setText("当前网络不支持网络安全存储功能，请连接到网关内网后使用");

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
