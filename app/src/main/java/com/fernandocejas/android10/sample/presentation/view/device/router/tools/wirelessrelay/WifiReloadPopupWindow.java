package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/07
 *      desc: Samba文件操作的弹窗
 *      version: 1.0
 * </pre>
 */

public class WifiReloadPopupWindow extends PopupWindow {
  private View mView;
  private TextView mDialogTitle, mDialogContent,mDialogTime;
  private ImageView mHead, mRouter;

  public WifiReloadPopupWindow(final Context context) {
    super(context);
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mView = inflater.inflate(R.layout.dialog_wifi_reload_pop, null);

    mDialogTitle = (TextView) mView.findViewById(R.id.dialog_wifi_title);
    mDialogContent = (TextView) mView.findViewById(R.id.dialog_wifi_content);
    mDialogTime = (TextView) mView.findViewById(R.id.tv_time);

    mHead = (ImageView) mView.findViewById(R.id.dialog_img_head);
    mRouter = (ImageView) mView.findViewById(R.id.dialog_img_router);

    this.setContentView(mView);
    this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
    this.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
    //this.setAnimationStyle(R.style.PopScaleAnimation);
    ColorDrawable dw = new ColorDrawable(0xb0000000);
    this.setBackgroundDrawable(dw);

    mDialogTitle.setText("正在重启WIFI");
    mDialogContent.setText("重启后将自动连接WIFI，或手动连接WIFI");

    mHead.setBackground(context.getResources().getDrawable(R.drawable.circle_blue));
    mRouter.setBackground(context.getResources().getDrawable(R.drawable.ic_router_blue));

    Animation animation = AnimationUtils.loadAnimation(context, R.anim.img_animation);
    LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
    animation.setInterpolator(lin);
    mHead.startAnimation(animation);

    final int[] time = {180};
    final Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        if (time[0]>0) {
          time[0]--;
          mDialogTime.setText(time[0]+"s");
          handler.postDelayed(this, 990);//每隔1s轮询一次
        } else {
          GlobleConstant.isTimeZero = true;
          handler.removeCallbacks(this);//移除定时器
          System.out.println("无线中继 倒计时为0 isTimeZero = "+GlobleConstant.isTimeZero);
        }
      }
    }, 10);

    // mView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框

  }

  public LinearLayout getOuterLayout() {
    return (LinearLayout) mView.findViewById(R.id.outer_layout);
  }

}
