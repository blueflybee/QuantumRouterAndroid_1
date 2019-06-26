package com.fernandocejas.android10.sample.presentation.view.device.lock.component;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.DialogWheelViewBinding;

import java.util.Arrays;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/07
 *      desc: 退出登录的弹窗
 *      version: 1.0
 * </pre>
 */

public class AdjustVolumePopup extends PopupWindow {

  private final DialogWheelViewBinding mBinding;
  private final Activity mActivity;
  private String mVolume;
  private OnFinishClickListener mOnFinishClickListener;
  private final String[] mItems;

  public AdjustVolumePopup(final Activity activity, String[] items) {
    super(activity);
    mActivity = activity;
    mBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_wheel_view, null, false);
    this.setContentView(mBinding.getRoot());
    this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
    this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    this.setAnimationStyle(R.style.Animation_Bottom_Rising);
    ColorDrawable dw = new ColorDrawable(Color.WHITE);
    this.setBackgroundDrawable(dw);
    setOutsideTouchable(true);
    setFocusable(true);
    // 设置原始数据
    mItems = items;
    mBinding.loopView.setItems(Arrays.asList(mItems));
    mBinding.loopView.setListener(i -> mVolume = mItems[i]);

    mBinding.tvFinish.setOnClickListener(v -> {
      dismiss();
      if (mOnFinishClickListener != null) {
        mOnFinishClickListener.onClick(mVolume);
      }
    });
  }

  public final void setInitPosition(int initPosition) {
    mBinding.loopView.setInitPosition(initPosition);
    mVolume = mItems[initPosition];
  }

  @Override
  public void showAtLocation(View parent, int gravity, int x, int y) {
    super.showAtLocation(parent, gravity, x, y);
    WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
    lp.alpha = 0.5f;
    mActivity.getWindow().setAttributes(lp);
  }

  @Override
  public void dismiss() {
    super.dismiss();
    WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
    lp.alpha = 1f;
    mActivity.getWindow().setAttributes(lp);
  }

  public void setOnFinishClickListener(OnFinishClickListener listener) {

    mOnFinishClickListener = listener;
  }

  public interface OnFinishClickListener {
    void onClick(String volume);
  }
}
