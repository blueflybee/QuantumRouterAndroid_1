package com.fernandocejas.android10.sample.presentation.view.component;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.sax.RootElement;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.RemoteMainAdapter;

import org.w3c.dom.Text;

import java.lang.reflect.Field;


/**
 * 自定义TitleBar（集成 返回，选择，删除等功能）
 * 有利于页面风格一致
 */
public class TitleBar extends LinearLayout {
  private Context mContext;
  private TextView mRightBtn;
  private RelativeLayout mRvRightBtn;
  private ImageButton imbUpload;
  private RelativeLayout mRvRight;
  private Button mLeftBtn;
  private TextView mTitle, mTitle_Samba;
  OnSambaBackClickListener onSambaBackClickListener;

  public Toolbar getToolbar() {
    return mToolbar;
  }

  private Toolbar mToolbar;

  public TitleBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.mContext = context;
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    // 左侧图标默认为 返回按钮
    //toolbar setNavigationIcon需要放在 setSupportActionBar之后才会生效。
//        setLeftAsBackButton();

    mToolbar = (Toolbar) findViewById(R.id.tool_bar);

    mRightBtn = (TextView) findViewById(R.id.btn_right);
    mRvRightBtn = (RelativeLayout) findViewById(R.id.rv_btn_right);
    imbUpload = (ImageButton) findViewById(R.id.imb_upload_samba);
    mRvRight = (RelativeLayout) findViewById(R.id.rv_right);
    mLeftBtn = (Button) findViewById(R.id.btn_left);
    mTitle = (TextView) findViewById(R.id.tv_title);
    mTitle_Samba = (TextView) findViewById(R.id.tv_title_samba);

  }

  public void setTitleCenter(String title) {
    mToolbar.setTitle("");
    mTitle.setText(title);
  }

  public void setCenterAs(String text, OnClickListener listener) {
    mToolbar.setTitle("");

    mTitle_Samba.setVisibility(VISIBLE);
    mTitle_Samba.setText(text);

    mTitle_Samba.setOnClickListener(listener);
  }

  /**
   * 左侧设置为“回首页”按钮
   */
  public void setLeftAsHomeButton() {
//        mLeftButton.setVisibility(View.VISIBLE);
    // mLeftButton.setBackgroundResource(R.drawable.title_button_selector);
    // mLeftButton.setText(mContext.getResources().getString(R.string.home));
//        mLeftButton.setOnClickListener(mHomeClickListener);
  }

  public void hideNavigation() {
    mToolbar.setNavigationIcon(null);
    mToolbar.setNavigationOnClickListener(null);
  }

  public void setLeftAs(String text, OnClickListener listener) {
    hideNavigation();
    mLeftBtn.setVisibility(VISIBLE);
    mLeftBtn.setText(text);
    mLeftBtn.setOnClickListener(listener);
  }

  /**
   * 左侧设置为“返回”按钮
   */
  public void setLeftAsBackButton() {
    setLeftAsBackButton(new OnClickListener() {
      @Override
      public void onClick(View v) {
        ((Activity) mContext).finish();
      }
    });
    mLeftBtn.setVisibility(GONE);
  }

  /**
   * 返回的白色图标
   */
  public void setLeftAsBackButton(int resId) {
    setLeftAsBackButton(resId, new OnClickListener() {
      @Override
      public void onClick(View v) {
        ((Activity) mContext).finish();
      }
    });
    mLeftBtn.setVisibility(GONE);
  }


  public void setLeftAsBackButtonForSamba() {
    setLeftAsBackButton(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (onSambaBackClickListener != null) {
          onSambaBackClickListener.onSambaBackClick();
        }
      }
    });
    mLeftBtn.setVisibility(GONE);
  }

  public void setLeftAsBackButton(OnClickListener clickListener) {
    setLeftAsBackButton(R.drawable.topbar_back, clickListener);
  }

  private void setLeftAsBackButton(int resId, OnClickListener clickListener) {
    this.mToolbar.setNavigationIcon(resId);
    this.mToolbar.setNavigationOnClickListener(clickListener);
    expandViewTouchDelegate(getToolBarNavButtonView(), 80,80,80,80);
  }

  public void setRightAs(String text, OnClickListener listener) {
    mRightBtn.setVisibility(VISIBLE);
    mRightBtn.setText(text);
    mRvRightBtn.setOnClickListener(listener);
  }

  public void hideRight() {
    mRightBtn.setVisibility(GONE);
  }

  public void setRightTextAs(String text, int textColor, OnClickListener listener) {
    setRightAs(text, listener);
    mRightBtn.setTextColor(textColor);
  }


  private ImageButton getToolBarNavButtonView() {
    Class<? extends Toolbar> c = mToolbar.getClass();
    try {
      Field field = c.getDeclaredField("mNavButtonView");
//      Field fieldTextView=c.getDeclaredField("mTitleTextView");
      field.setAccessible(true);
//      fieldTextView.setAccessible(true);
      Object obj = null;//拿到对应的Object
//      Object objTextView = null;//拿到对应的Object
      try {
        obj = field.get(mToolbar);
        if (obj == null) return null;
        if (!(obj instanceof ImageButton)) return null;
        //          mNavButtonView.setImageTintMode(PorterDuff.Mode.ADD);
        return (ImageButton) obj;

//        objTextView = fieldTextView.get(toolbar);
//        if (objTextView == null) return;
//        if (objTextView instanceof TextView) {
//          mTitleTextView = (TextView) objTextView;
//        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();
        return null;
      }
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 显示两个view以上
   *
   */
  public void setRightAsMore(OnClickListener listener1, OnClickListener listener2) {
    /*mRightBtn.setVisibility(VISIBLE);
    mRightBtn.setText(text);*/
    mRvRight.setVisibility(VISIBLE);
    imbUpload.setVisibility(VISIBLE);

    imbUpload.setOnClickListener(listener1);
    mRvRight.setOnClickListener(listener2);
    /*mRightBtn.setOnClickListener(listener2);*/
  }

  public void setRightAsSelect(String text, OnClickListener listener) {
    imbUpload.setVisibility(GONE);
    mRightBtn.setVisibility(VISIBLE);
    mRightBtn.setText(text);

    mRvRightBtn.setOnClickListener(listener);
  }

  public void setRightAs(String text, int resid, OnClickListener listener) {
    mRightBtn.setVisibility(VISIBLE);
    mRightBtn.setText(text);
    mRightBtn.setBackgroundResource(resid);
    mRvRightBtn.setOnClickListener(listener);
  }

  public void setRightAs(int resid, OnClickListener listener) {
    mRightBtn.setVisibility(VISIBLE);
    mRightBtn.setBackgroundResource(resid);
    mRvRightBtn.setOnClickListener(listener);
  }

  public void setRightEnable(boolean enabled, int textColor) {
    mRightBtn.setEnabled(enabled);
    mRightBtn.setTextColor(textColor);
    mRvRightBtn.setEnabled(enabled);
  }

  public TextView getRightBtn() {
    return mRightBtn;
  }

  public RelativeLayout getRvRightBtn() {
    return mRvRightBtn;
  }

  public RelativeLayout getRightRelayout() {
    return mRvRight;
  }

  public TextView getCenterTextView() {
    return mTitle_Samba;
  }

  public View getDividerView() {
    return (View) findViewById(R.id.v_title_bar);
  }

  public TextView getTitleView() {
    return mTitle;
  }

  public ImageButton getUploadBtn() {
    return imbUpload;
  }

   /* public void setLeftKeyVisiable(){

    }*/

  public interface OnSambaBackClickListener {
    void onSambaBackClick();
  }

  public void setOnSambaBackListener(OnSambaBackClickListener onSambaBackClickListener) {
    this.onSambaBackClickListener = onSambaBackClickListener;
  }

  /**
   * 扩大View的触摸和点击响应范围,最大不超过其父View范围
   *
   * @param view
   * @param top
   * @param bottom
   * @param left
   * @param right
   */
  public static void expandViewTouchDelegate(final View view, final int top,
                                             final int bottom, final int left, final int right) {
    if (view == null) return;

    ((View) view.getParent()).post(new Runnable() {
      @Override
      public void run() {
        Rect bounds = new Rect();
        view.setEnabled(true);
        view.getHitRect(bounds);

        bounds.top -= top;
        bounds.bottom += bottom;
        bounds.left -= left;
        bounds.right += right;

        TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

        if (View.class.isInstance(view.getParent())) {
          ((View) view.getParent()).setTouchDelegate(touchDelegate);
        }
      }
    });
  }

  /**
   * 还原View的触摸和点击响应范围,最小不小于View自身范围
   *
   * @param view
   */
  public static void restoreViewTouchDelegate(final View view) {

    ((View) view.getParent()).post(new Runnable() {
      @Override
      public void run() {
        Rect bounds = new Rect();
        bounds.setEmpty();
        TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

        if (View.class.isInstance(view.getParent())) {
          ((View) view.getParent()).setTouchDelegate(touchDelegate);
        }
      }
    });
  }

}
