package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Selection;
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

public class SambaPopupWindow extends PopupWindow implements View.OnClickListener {
  private View mView;
  private TextView mDialogTitle, mDialogContent;
  private Button mDialogNeg, mDialogPos;
  private RelativeLayout mDialogClose;
  private LinearLayout mLlEdit;
  private int mIndex = 0;
  private EditText mEditText;

  OnPositiveClickListener mOnPositiveClickListener;
  OnNegativeClickListener mOnNegativeClickListener;

  public void setOnPositiveClickListener(OnPositiveClickListener onPositiveClickListener) {
    mOnPositiveClickListener = onPositiveClickListener;
  }

  public void setOnNegativeClickListener(OnNegativeClickListener mOnNegativeClickListener) {
    this.mOnNegativeClickListener = mOnNegativeClickListener;
  }

  public SambaPopupWindow(final Context context, int operateCode, String specialInfo) {
    super(context);
    mIndex = operateCode;
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mView = inflater.inflate(R.layout.dialog_samba_pop, null);

    mDialogClose = (RelativeLayout) mView.findViewById(R.id.rl_close);
    mDialogPos = (Button) mView.findViewById(R.id.dialog_pos);
    mDialogNeg = (Button) mView.findViewById(R.id.dialog_neg);

    mDialogTitle = (TextView) mView.findViewById(R.id.dialog_title);

    mEditText = ((EditText) mView.findViewById(R.id.dialog_new_name));

    mLlEdit = (LinearLayout) mView.findViewById(R.id.ll_edit);
    mDialogContent = (TextView) mView.findViewById(R.id.dialog_content);

    mDialogNeg.setOnClickListener(this);
    mDialogPos.setOnClickListener(this);
    mDialogClose.setOnClickListener(this);

    this.setContentView(mView);
    this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
    this.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
    //this.setAnimationStyle(R.style.PopScaleAnimation);
    ColorDrawable dw = new ColorDrawable(0xb0000000);
    this.setBackgroundDrawable(dw);

    if (operateCode == 1) {
      //新建文件夹
      editTextWatchEvent(context);

      mDialogContent.setVisibility(View.GONE);
      mLlEdit.setVisibility(View.VISIBLE);
      mDialogTitle.setText("新建文件夹");
      mEditText.setText("新建文件夹");

      Selection.setSelection(mEditText.getText(), mEditText.getText().length());
      SpannableString hintStr = new SpannableString("新建文件夹");
      mEditText.setHint(hintStr);

      //默认名称为新建文件夹并且光标移到最后

    } else if (operateCode == 2) {
      //重命名
      editTextWatchEvent(context);

      mDialogContent.setVisibility(View.GONE);
      mLlEdit.setVisibility(View.VISIBLE);
      mDialogTitle.setText("重命名");
      //旧文件名并且光标移到最后
      mEditText.setText(specialInfo);
      Selection.setSelection(mEditText.getText(), mEditText.getText().length());

      SpannableString hintStr = new SpannableString("重命名");
      mEditText.setHint(hintStr);

    } else if (operateCode == 3) {
      //删除历史记录
      mLlEdit.setVisibility(View.GONE);
      mDialogContent.setVisibility(View.VISIBLE);
      mDialogContent.setText("您确定要删除搜索历史吗？");
      mDialogTitle.setText("删除搜索历史");
    } else if (operateCode == 4) {
      //删除文件

      mLlEdit.setVisibility(View.GONE);
      mDialogContent.setVisibility(View.VISIBLE);

      if ("isDownload".equals(specialInfo)) {
        mDialogContent.setText("您确定要删除该文件下载记录吗？");
        mDialogTitle.setText("删除下载记录");
      } else if ("isUpload".equals(specialInfo)) {
        mDialogContent.setText("您确定要删除该文件上传记录吗？");
        mDialogTitle.setText("删除上传记录");
      } else {
        mDialogContent.setText("您确定要删除该文件吗？");
        mDialogTitle.setText("删除文件");
      }
    } else if (operateCode == 5) {
      //重复下载文件

      mLlEdit.setVisibility(View.GONE);
      mDialogContent.setVisibility(View.VISIBLE);

      mDialogPos.setText("继续");

      mDialogContent.setText("您已经下载过该文件，重新下载将覆盖原" + "\n" + "文件,是否要继续？");
      mDialogTitle.setText("提示");
    }

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

  private void editTextWatchEvent(Context context) {

    //控制表情输入
    mEditText.setFilters(InputUtil.emojiFilters);

    InputWatcher watcher = new InputWatcher();
    /*InputWatcher.WatchCondition nameCondition = new InputWatcher.WatchCondition();
    nameCondition.setLength(11);*/
    InputWatcher.WatchCondition pwdCondition = new InputWatcher.WatchCondition();
//    pwdCondition.setMinLength(1);
    pwdCondition.setRange(new InputWatcher.InputRange(1, 32));
    watcher.addEt(mEditText, pwdCondition);
    watcher.setInputListener(isEmpty -> {
      mDialogPos.setClickable(!isEmpty);
      if (isEmpty) {
        mDialogPos.setBackgroundColor(context.getResources().getColor(R.color.white_bbdefb));
      } else {
        mDialogPos.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });

//    InputUtil.inhibit(mEditText, InputUtil.REGULAR_CHINESE_AND_SPACE, 20);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.rl_close:
        dismiss();
        break;

      case R.id.dialog_neg:
        if (mIndex == 5) {
          if (mOnNegativeClickListener != null) {
            mOnNegativeClickListener.onNegtiveClick();
            dismiss();
          }
        } else {
          dismiss();
        }

        break;

      case R.id.dialog_pos:
        if ((mIndex == 1) || (mIndex == 2)) {
          if ((mOnPositiveClickListener != null) && (!TextUtils.isEmpty(getNewNameEdit().getText().toString().trim()))) {

            mOnPositiveClickListener.onPositiveClick();
            dismiss();
          }
        } else if (mOnPositiveClickListener != null) {
          mOnPositiveClickListener.onPositiveClick();
          dismiss();
        }
        break;
      default:
        break;
    }
  }

  public interface OnPositiveClickListener {

    void onPositiveClick();
  }

  public interface OnNegativeClickListener {

    void onNegtiveClick();
  }

  public LinearLayout getOuterLayout() {
    return (LinearLayout) mView.findViewById(R.id.ll_outer_layout);
  }

  public EditText getNewNameEdit() {
    return mEditText;
  }
}
