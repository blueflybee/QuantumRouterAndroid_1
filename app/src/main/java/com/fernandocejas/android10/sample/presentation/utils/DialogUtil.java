/**
 *
 */
package com.fernandocejas.android10.sample.presentation.utils;


import android.app.DatePickerDialog;
import android.app.Dialog;

import com.blueflybee.uilibrary1.wheelview.LoopView;
import com.blueflybee.uilibrary1.wheelview.OnItemSelectedListener;
import com.fernandocejas.android10.sample.presentation.view.component.timepicker.TimePickerDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.component.CustomProgressDialog;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.device.lock.adapter.SelectLockManagerAdapter;
import com.flybluebee.circleprogress.CircleProgress;
import com.qtec.mapp.model.rsp.GetLockUsersResponse;

import java.util.Arrays;
import java.util.List;

/**
 * Dialog 工具类
 */
public class DialogUtil {

  private static AlertDialog mAlertDialog = null;
  private static Dialog mProgressDialog = null;
  private static PopupWindow mProgressPop;

  /**
   * 取消所有弹出的对话框
   */
  public static void dismissDialog() {
    if (mAlertDialog != null)
      mAlertDialog.dismiss();
    mAlertDialog = null;
  }

//    /**
//     * 显示进度对话框（不可取消）
//     *
//     * @param title
//     * @param message
//     * @param onCancelListener
//     */
//    public static void showProgress(Context context, String title, String message,
//                                    DialogInterface.OnCancelListener onCancelListener) {
//        hideProgress();
//        mProgressDialog = new ProgressDialog(context);
//        if (title != null) {
//            mProgressDialog.setTitle(title);
//        }
//        if (message != null) {
//            ((ProgressDialog) mProgressDialog).setMessage(message);
//        }
//        if (onCancelListener != null) {
//            mProgressDialog.setOnCancelListener(onCancelListener);
//        }
//        mProgressDialog.setCancelable(false);
//        mProgressDialog.show();
//    }

//  /**
//   * 显示进度对话框
//   */
//  public static void showProgress(Activity act) {
//    if (mProgressPop == null) {
//      mProgressPop = new PopupWindow();
//      mProgressPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//      mProgressPop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//      mProgressPop.setFocusable(true);
//      mProgressPop.setContentView(LayoutInflater.from(act).inflate(R.file_paths.app_progress, null));
//    }
//
//    try {
//      mProgressPop.showAtLocation(act.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  public static void hideProgress() {
//    try {
//      mProgressPop.dismiss();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }

  /**
   * 显示进度对话框（可取消）
   */

  public static void showProgress(Context context) {
    try {
      hideProgress();
      mProgressDialog = CustomProgressDialog.createDialog(context);
      mProgressDialog.setCancelable(true);
      mProgressDialog.show();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * 显示进度对话框
   */
  public static void showProgress(Context context, boolean cancelable) {
    try {
      hideProgress();
      mProgressDialog = CustomProgressDialog.createDialog(context);
      mProgressDialog.setCancelable(cancelable);
      mProgressDialog.show();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static void hideProgress() {
    try {
      if (mProgressDialog != null) mProgressDialog.dismiss();
      mProgressDialog = null;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 显示”确定“ 对话框
   *
   * @param title
   * @param onOkClickListener
   */
  public static void showOkAlertDialog(Context context, String title,
                                       DialogInterface.OnClickListener onOkClickListener) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
    alertDialogBuilder.setMessage(title);
    alertDialogBuilder.setCancelable(false);
    alertDialogBuilder.setPositiveButton(context.getString(R.string.confirm), onOkClickListener);
    mAlertDialog = alertDialogBuilder.show();
  }

  /**
   * 显示”是“ ”否“ 对话框
   *
   * @param title
   * @param onYesClickListener
   * @param onNoClickListener
   */
  public static void showYesNoAlertDialog(Context context, String title,
                                          DialogInterface.OnClickListener onYesClickListener,
                                          DialogInterface.OnClickListener onNoClickListener) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
        context);
    if (title != null) {
      alertDialogBuilder.setTitle(title);
    }
    if (onYesClickListener != null) {
      alertDialogBuilder.setPositiveButton(
          context.getString(R.string.yes), onYesClickListener);
    }
    if (onNoClickListener != null) {
      alertDialogBuilder.setNegativeButton(
          context.getString(R.string.no), onNoClickListener);
    }
    alertDialogBuilder.setCancelable(false);
    mAlertDialog = alertDialogBuilder.show();
  }

  /**
   * 显示定义对话框
   *
   * @param title
   * @param onYesClickListener
   * @param onNoClickListener
   */
  public static void showCustomAlertDialog(Context context, String title,
                                           String confirmStr, String cancelStr,
                                           DialogInterface.OnClickListener onYesClickListener,
                                           DialogInterface.OnClickListener onNoClickListener) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
        context);
    if (title != null) {
      alertDialogBuilder.setTitle(title);
    }

    if (onYesClickListener != null && confirmStr != null) {
      alertDialogBuilder
          .setPositiveButton(confirmStr, onYesClickListener);
    }
    if (onNoClickListener != null && cancelStr != null) {
      alertDialogBuilder.setNegativeButton(cancelStr, onNoClickListener);
    }
    alertDialogBuilder.setCancelable(false);
    mAlertDialog = alertDialogBuilder.show();
  }

  /**
   * 显示定义对话框
   *
   * @param title
   * @param message
   * @param confirmStr
   * @param cancelStr
   * @param onYesClickListener
   * @param onNoClickListener
   */
  public static void showCustomAlertDialogWithMessage(Context context,
                                                      String title, String message, String confirmStr, String cancelStr,
                                                      OnClickListener onYesClickListener,
                                                      OnClickListener onNoClickListener) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
        context);
    if (title != null) {
      alertDialogBuilder.setTitle(title);
    }
    if (message != null) {
      alertDialogBuilder.setMessage(message);
    }
    if (onYesClickListener != null && confirmStr != null) {
      alertDialogBuilder
          .setPositiveButton(confirmStr, onYesClickListener);
    }
    if (onNoClickListener != null && cancelStr != null) {
      alertDialogBuilder.setNegativeButton(cancelStr, onNoClickListener);
    }
    alertDialogBuilder.setCancelable(false);
    mAlertDialog = alertDialogBuilder.show();
  }

  /**
   * 显示定义对话框
   *
   * @param title
   * @param message
   * @param onYesClickListener
   * @param onNoClickListener
   */
  public static void showOkCancelAlertDialogWithMessage(Context context,
                                                        String title, String message,
                                                        View.OnClickListener onYesClickListener,
                                                        View.OnClickListener onNoClickListener) {

    View view = LayoutInflater.from(context).inflate(R.layout.dialog_ok_cancel, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    if (title != null) {
      TextView titleTv = (TextView) view.findViewById(R.id.dialog_title);
      titleTv.setText(title);
    }
    if (message != null) {
      TextView msgTv = (TextView) view.findViewById(R.id.dialog_msg);
      msgTv.setText(message);
    }
    if (onYesClickListener != null) {
      TextView posTv = (TextView) view.findViewById(R.id.dialog_pos);
      posTv.setOnClickListener(v -> {
        onYesClickListener.onClick(v);
        mAlertDialog.dismiss();
      });
    }
    if (onNoClickListener != null) {
      TextView negTv = (TextView) view.findViewById(R.id.dialog_neg);
      negTv.setOnClickListener(v -> {
        onNoClickListener.onClick(v);
        mAlertDialog.dismiss();
      });
    } else {
      TextView negTv = (TextView) view.findViewById(R.id.dialog_neg);
      negTv.setOnClickListener(v -> {
        mAlertDialog.dismiss();
      });
    }
    builder.setCancelable(false);
    mAlertDialog = builder.show();

  }

  /**
   * 显示拨号对话框
   *
   * @param message
   * @param onYesClickListener
   * @param onNoClickListener
   */
  public static void showDiaDialogWithMessage(Context context, String message,
                                              View.OnClickListener onYesClickListener,
                                              View.OnClickListener onNoClickListener) {

    View view = LayoutInflater.from(context).inflate(R.layout.dialog_dia, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);

    if (message != null) {
      TextView msgTv = (TextView) view.findViewById(R.id.tv_tel);
      msgTv.setText(message);
    }
    if (onYesClickListener != null) {
      TextView posTv = (TextView) view.findViewById(R.id.dialog_pos);
      posTv.setOnClickListener(v -> {
        onYesClickListener.onClick(v);
        mAlertDialog.dismiss();
      });
    }
    if (onNoClickListener != null) {
      TextView negTv = (TextView) view.findViewById(R.id.dialog_neg);
      negTv.setOnClickListener(v -> {
        onNoClickListener.onClick(v);
        mAlertDialog.dismiss();
      });
    } else {
      TextView negTv = (TextView) view.findViewById(R.id.dialog_neg);
      negTv.setOnClickListener(v -> {
        mAlertDialog.dismiss();
      });
    }
    builder.setCancelable(false);
    mAlertDialog = builder.show();

  }


  public static void showOkAlertDialogWithMessage(Context context,
                                                  String title, String message,
                                                  View.OnClickListener onYesClickListener) {

    View view = LayoutInflater.from(context).inflate(R.layout.dialog_ok, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    if (title != null) {
      TextView titleTv = (TextView) view.findViewById(R.id.dialog_title);
      titleTv.setText(title);
    }
    if (message != null) {
      TextView msgTv = (TextView) view.findViewById(R.id.dialog_msg);
      msgTv.setText(message);
    }
    if (onYesClickListener != null) {
      TextView posTv = (TextView) view.findViewById(R.id.dialog_pos);
      posTv.setOnClickListener(v -> {
        onYesClickListener.onClick(v);
        mAlertDialog.dismiss();
      });
    }
    builder.setCancelable(false);
    mAlertDialog = builder.show();

  }

  public static void showCreateRouterGroupDialog(Context context, @NonNull OnGroupCreateListener onGroupCreateListener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_create_router_group, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    view.findViewById(R.id.rl_close).setOnClickListener(v -> mAlertDialog.dismiss());
    EditText etName = (EditText) view.findViewById(R.id.et_group_name);
    InputUtil.allowLetterNumberChinese(etName, 16);
    Button btnCreate = (Button) view.findViewById(R.id.btn_create_group);
    btnCreate.setOnClickListener(v -> {
      onGroupCreateListener.onClick(v, etName.getText().toString());
      mAlertDialog.dismiss();
    });

    InputWatcher watcher = new InputWatcher();
    watcher.addEt(etName);
    watcher.setInputListener(isEmpty -> {
      btnCreate.setClickable(!isEmpty);
      if (isEmpty) {
        btnCreate.setBackgroundColor(context.getResources().getColor(R.color.white_bbdefb));
      } else {
        btnCreate.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });
    builder.setCancelable(false);
    mAlertDialog = builder.show();

  }

  public static void showSkipKeyInjectDialog(Context context, @NonNull View.OnClickListener onSkipListener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_skip_key_inject, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    view.findViewById(R.id.rl_close).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_cancel).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
      onSkipListener.onClick(v);
      mAlertDialog.dismiss();
    });

    builder.setCancelable(false);
    mAlertDialog = builder.show();

  }

  public static void showSkipKeyInjectDialog(Context context, String content, @NonNull View.OnClickListener onSkipListener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_skip_key_inject, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    ((TextView) view.findViewById(R.id.tv_content)).setText(content);
    view.findViewById(R.id.rl_close).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_cancel).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
      onSkipListener.onClick(v);
      mAlertDialog.dismiss();
    });

    builder.setCancelable(false);
    mAlertDialog = builder.show();
  }

  public static void showConfirmCancelDialog(Context context, String title, String content, String confirmBtnText, String cancelBtnText,
                                             View.OnClickListener confirmListener, View.OnClickListener cancelListener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_skip_key_inject, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    ((TextView) view.findViewById(R.id.tv_title)).setText(title);
    ((TextView) view.findViewById(R.id.tv_content)).setText(content);
    ((Button) view.findViewById(R.id.btn_confirm)).setText(confirmBtnText);
    ((Button) view.findViewById(R.id.btn_cancel)).setText(cancelBtnText);
    view.findViewById(R.id.rl_close).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
      if (cancelListener != null) {
        cancelListener.onClick(v);
      }
      mAlertDialog.dismiss();
    });
    view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
      if (confirmListener != null) {
        confirmListener.onClick(v);
      }
      mAlertDialog.dismiss();
    });

    builder.setCancelable(false);
    mAlertDialog = builder.show();
  }

  public static void showCatEyeRecordDialog(Context context, String title, String videoPath, View.OnClickListener confirmListener, View.OnClickListener cancelListener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_cat_eye_record, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    AlertDialog alertDialog = builder.create();
    ((TextView) view.findViewById(R.id.tv_title)).setText(title);
    view.findViewById(R.id.rl_close).setOnClickListener(v -> alertDialog.dismiss());
    view.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
      if (cancelListener != null) {
        cancelListener.onClick(v);
      }
      alertDialog.dismiss();
    });
    View btnVideo = view.findViewById(R.id.btn_detail);
    btnVideo.setOnClickListener(v -> {
      if (confirmListener != null) {
        confirmListener.onClick(v);
      }
      alertDialog.dismiss();
    });
    if (TextUtils.isEmpty(videoPath)) {
      btnVideo.setVisibility(View.GONE);
    }

    builder.setCancelable(false);
    alertDialog.show();
  }


  public static void showPwdErrThreeTimesDialog(Context context, @NonNull View.OnClickListener onSkipListener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_password_err_more_than_three_times, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    view.findViewById(R.id.rl_close).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_cancel).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
      onSkipListener.onClick(v);
      mAlertDialog.dismiss();
    });

    builder.setCancelable(false);
    mAlertDialog = builder.show();

  }

  /**
   * 左确认右取消
   *
   * @param context
   * @param title
   * @param content
   * @param onConfirmClickListener
   */
  public static void showConfirmCancelDialog(Context context, String title, String content, @NonNull View.OnClickListener onConfirmClickListener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_cancel, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    ((TextView) view.findViewById(R.id.tv_title)).setText(title);
    ((TextView) view.findViewById(R.id.tv_content)).setText(content);
    view.findViewById(R.id.rl_close).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_cancel).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
      onConfirmClickListener.onClick(v);
      mAlertDialog.dismiss();
    });

    builder.setCancelable(false);
    mAlertDialog = builder.show();

  }

  /**
   * 左取消右确认
   *
   * @param context
   * @param title
   * @param content
   * @param onConfirmClickListener
   */
  public static void showCancelConfirmDialog(Context context, String title, String content, @NonNull View.OnClickListener onConfirmClickListener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_cancel_confirm, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    ((TextView) view.findViewById(R.id.tv_title)).setText(title);
    ((TextView) view.findViewById(R.id.tv_content)).setText(content);
    view.findViewById(R.id.rl_close).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_cancel).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
      onConfirmClickListener.onClick(v);
      mAlertDialog.dismiss();
    });

    builder.setCancelable(false);
    mAlertDialog = builder.show();

  }


  public static void showInputPinErrDialog(Context context, @NonNull View.OnClickListener onSkipListener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_pin_input_err, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    view.findViewById(R.id.rl_close).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
      onSkipListener.onClick(v);
      mAlertDialog.dismiss();
    });

    builder.setCancelable(false);
    mAlertDialog = builder.show();

  }

  public static void showInputPinErrDialog(Context context, String title, String content, @NonNull View.OnClickListener onSkipListener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_pin_input_err, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    ((TextView) view.findViewById(R.id.tv_title)).setText(title);
    ((TextView) view.findViewById(R.id.tv_content)).setText(content);
    view.findViewById(R.id.rl_close).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
      onSkipListener.onClick(v);
      mAlertDialog.dismiss();
    });

    builder.setCancelable(false);
    mAlertDialog = builder.show();

  }

  public static void showInputPinErrDialog(Context context, String title, String content, String btnText, @NonNull View.OnClickListener onClickListener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_pin_input_err, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    ((TextView) view.findViewById(R.id.tv_title)).setText(title);
    ((TextView) view.findViewById(R.id.tv_content)).setText(content);
    ((Button) view.findViewById(R.id.btn_confirm)).setText(btnText);
    view.findViewById(R.id.rl_close).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
      onClickListener.onClick(v);
      mAlertDialog.dismiss();
    });

    builder.setCancelable(false);
    mAlertDialog = builder.show();

  }

  public static void showRestartWifiDialog(Context context, String wifiName, String wifiPwd, @NonNull View.OnClickListener clickListener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_first_config_restart_wifi, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    ((TextView) view.findViewById(R.id.tv_wifi_name)).setText("WiFi名称：" + wifiName);
    ((TextView) view.findViewById(R.id.tv_wifi_pwd)).setText("WiFi密码：" + wifiPwd);
    view.findViewById(R.id.rl_close).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
      clickListener.onClick(v);
      mAlertDialog.dismiss();
    });

    builder.setCancelable(false);
    mAlertDialog = builder.show();

  }

  public static void showVersionUpdateDialog(Context context, boolean forceUpdate, String versionTitle, String content, @NonNull View.OnClickListener clickListener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_app_version_update, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    ((TextView) view.findViewById(R.id.tv_version_name)).setText(versionTitle + "更新");
    ((TextView) view.findViewById(R.id.tv_version_info)).setText(content);
    view.findViewById(R.id.iv_close).setVisibility(forceUpdate ? View.GONE : View.VISIBLE);
    view.findViewById(R.id.iv_close).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_update).setOnClickListener(v -> {
      clickListener.onClick(v);
      mAlertDialog.dismiss();
    });

    builder.setCancelable(false);
    mAlertDialog = builder.show();
    mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());

  }

//  public static void showFixLengthPasswordDialog(Context context, @NonNull OnPasswordInputFinishedListener finishedListener) {
//    View view = LayoutInflater.from(context).inflate(R.file_paths.dialog_password_input, null);
//    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//    builder.setView(view);
//    view.findViewById(R.id.rl_close).setOnClickListener(v -> mAlertDialog.dismiss());
//    PasswordInputView passwordInputView = (PasswordInputView) view.findViewById(R.id.view_password);
//    passwordInputView.setOnFinishListener(() -> {
//      if (passwordInputView.getOriginText().length() == passwordInputView.getMaxPasswordLength()) {
//        finishedListener.onFinished(passwordInputView, passwordInputView.getOriginText());
//        mAlertDialog.dismiss();
//      }
//    });
//
//    builder.setCancelable(false);
//    mAlertDialog = builder.show();
//  }

  public static void showLoginPasswordDialog(Context context, String title, @NonNull OnPasswordInputFinishedListener listener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_password_input, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    view.findViewById(R.id.rl_close).setOnClickListener(v -> mAlertDialog.dismiss());
    ((TextView) view.findViewById(R.id.tv_title)).setText(title);
    EditText etPwd = (EditText) view.findViewById(R.id.et_pwd);
    Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
    btnConfirm.setOnClickListener(v -> {
      mAlertDialog.dismiss();
      listener.onFinished(v, etPwd.getText().toString().trim());
    });
    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition = new InputWatcher.WatchCondition();
    condition.setRange(new InputWatcher.InputRange(6, 20));
    watcher.addEt(etPwd, condition);
    watcher.setInputListener(isEmpty -> {
      btnConfirm.setClickable(!isEmpty);
      if (isEmpty) {
        btnConfirm.setBackgroundColor(context.getResources().getColor(R.color.white_bbdefb));
      } else {
        btnConfirm.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });

    builder.setCancelable(false);
    mAlertDialog = builder.show();
  }

  public static void showLockAdminPasswordDialog(Context context, String title, @NonNull OnPasswordInputFinishedListener listener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_password_input, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    view.findViewById(R.id.rl_close).setOnClickListener(v -> mAlertDialog.dismiss());
    ((TextView) view.findViewById(R.id.tv_title)).setText(title);
    EditText etPwd = (EditText) view.findViewById(R.id.et_pwd);
    etPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)}); //最大输入长度
    Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
    btnConfirm.setOnClickListener(v -> {
      mAlertDialog.dismiss();
      listener.onFinished(v, etPwd.getText().toString().trim());
    });
    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition = new InputWatcher.WatchCondition();
    condition.setLength(8);
    watcher.addEt(etPwd, condition);
    watcher.setInputListener(isEmpty -> {
      btnConfirm.setClickable(!isEmpty);
      if (isEmpty) {
        btnConfirm.setBackgroundColor(context.getResources().getColor(R.color.white_bbdefb));
      } else {
        btnConfirm.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });

    builder.setCancelable(false);
    mAlertDialog = builder.show();
  }

  public static void showUpdateFirmwareDialog(Context context, @NonNull View.OnClickListener clickListener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_update_firmware, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    view.findViewById(R.id.rl_close).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_cancel).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
      clickListener.onClick(v);
      mAlertDialog.dismiss();
    });

    builder.setCancelable(false);
    mAlertDialog = builder.show();

  }

  public interface OnPasswordInputFinishedListener {
    void onFinished(View view, String pwd);
  }


  public interface OnGroupCreateListener {
    void onClick(View view, String group);
  }

  /**
   * 显示带提示的确定取消对话框
   *
   * @param context
   * @param title
   * @param content
   * @param tips
   * @param confirmListener
   */
  public static void showCancelConfirmWithTipsDialog(Context context, String title, String content, String tips,
                                                     String cancelStr, String confirmStr, View.OnClickListener confirmListener) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setCancelable(false);
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_logoff_lock_user, null);
    builder.setView(view);
    final AlertDialog alertDialog = builder.create();
    ((TextView) view.findViewById(R.id.tv_title)).setText(title);
    ((TextView) view.findViewById(R.id.tv_content)).setText(content);
    TextView tvTips = view.findViewById(R.id.tv_tips);
    tvTips.setText(tips);
    if (TextUtils.isEmpty(tips)) {
      tvTips.setVisibility(View.GONE);
    }
    view.findViewById(R.id.rl_close).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        alertDialog.dismiss();
      }
    });
    Button cancelBtn = ((Button) view.findViewById(R.id.btn_cancel));
    cancelBtn.setText(cancelStr);
    cancelBtn.setOnClickListener(v -> alertDialog.dismiss());
    Button confirmBtn = ((Button) view.findViewById(R.id.btn_confirm));
    confirmBtn.setText(confirmStr);
    confirmBtn.setOnClickListener(v -> {
      confirmListener.onClick(v);
      alertDialog.dismiss();
    });
    alertDialog.show();

  }

  public static void showLogoffLockUserDialog(Context context, View.OnClickListener confirmListener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_logoff_lock_user, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    view.findViewById(R.id.rl_close).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_cancel).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
      confirmListener.onClick(v);
      mAlertDialog.dismiss();
    });

    builder.setCancelable(false);
    mAlertDialog = builder.show();

  }

  public static void showSelectLockManagerDialog(Context context, List<GetLockUsersResponse> lockUsers, OnSelectLockManagerUnbindListener listener) {

    View view = LayoutInflater.from(context).inflate(R.layout.dialog_select_lock_manager, null);
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setView(view);
    ListView listView = (ListView) view.findViewById(R.id.lv_user_list);
    SelectLockManagerAdapter adapter = new SelectLockManagerAdapter(context, lockUsers);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.updateCheckPosition(position);
      }
    });
    view.findViewById(R.id.rl_close).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_cancel).setOnClickListener(v -> mAlertDialog.dismiss());
    view.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
      listener.onClick(v, adapter.getSelectedUser());
      mAlertDialog.dismiss();
    });

    builder.setCancelable(false);
    mAlertDialog = builder.show();

  }

  public interface OnSelectLockManagerUnbindListener {
    void onClick(View v, GetLockUsersResponse user);
  }


  /**
   * 显示列表对话框
   *
   * @param context
   * @param title
   * @param items
   * @param onItemClickListener
   * @param negativeTxt
   * @param onNegativeClickListener
   * @param positiveTxt
   * @param onPositiveClickListener
   */
  public static void showCustomListDialog(Context context, String title,
                                          String[] items,
                                          OnClickListener onItemClickListener,
                                          String negativeTxt, OnClickListener onNegativeClickListener,
                                          String positiveTxt, OnClickListener onPositiveClickListener) {

    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle(title).setItems(items, onItemClickListener)
        .setCancelable(false)
        .setNegativeButton(negativeTxt, onNegativeClickListener)
        .setPositiveButton(positiveTxt, onPositiveClickListener);

    AlertDialog dialog = builder.show();
  }

  /**
   * 显示列表对话框
   *
   * @param context
   * @param title
   * @param items
   * @param onItemClickListener
   */
  public static void showListDialog(Context context, String title,
                                    String[] items, OnClickListener onItemClickListener) {

    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle(title).setItems(items, onItemClickListener);

    Dialog dialog = builder.show();
  }

  /**
   * 显示日期选择对话框
   *
   * @param context
   * @param callBack
   * @param year
   * @param monthOfYear
   * @param dayOfMonth
   */
  public static void showDatePickDialog(Context context,
                                        DatePickerDialog.OnDateSetListener callBack, int year, int monthOfYear,
                                        int dayOfMonth) {
    new DatePickerDialog(context, callBack, year, monthOfYear, dayOfMonth).show();

  }

  public interface OnTimeSelectedListener {
    void onTimeSelected(String[] times);

    void onTimeSelectedInInt(int[] times);
  }

  public static void showTimePick(@NonNull Context context, @NonNull OnTimeSelectedListener listener, String hour, String minute) {
    int mHour = 0, mMinute = 0;
    try {
      mHour = Integer.valueOf(hour).intValue();
      mMinute = Integer.valueOf(minute).intValue();
    } catch (Exception e) {
      e.printStackTrace();
    }

    TimePickerDialog.Builder builder = new TimePickerDialog.Builder(context, mHour, mMinute);
    builder.setOnTimeSelectedListener(new TimePickerDialog.OnTimeSelectedListener() {
      @Override
      public void onTimeSelected(int[] times) {
        String[] result = TimeUtil.getStringTime(times);
        listener.onTimeSelected(result);
        listener.onTimeSelectedInInt(times);

      }
    }).create().show();
  }

  public static void showTimePick(@NonNull Context context, @NonNull OnTimeSelectedListener listener, String hour, String minute, String second) {
    int mHour = 0, mMinute = 0, mSecond = 0;
    try {
      mHour = Integer.valueOf(hour).intValue();
      mMinute = Integer.valueOf(minute).intValue();
      mSecond = Integer.valueOf(second).intValue();
    } catch (Exception e) {
      e.printStackTrace();
    }

    TimePickerDialog.Builder builder = new TimePickerDialog.Builder(context, mHour, mMinute, mSecond);
    builder.setOnTimeSelectedListener(new TimePickerDialog.OnTimeSelectedListener() {
      @Override
      public void onTimeSelected(int[] times) {
        String[] result = TimeUtil.getStringTimeWithSecond(times);
        listener.onTimeSelected(result);
        listener.onTimeSelectedInInt(times);

      }
    }).create(true).show();
  }

  public interface DialogCallback {
    void onDismiss();
  }

  public static void showSuccessDialog(@NonNull Context context, @NonNull String content, @NonNull DialogCallback callback) {
    View view = LayoutInflater.from(context).inflate(R.layout.view_toast_ok, null);
    ((TextView) view.findViewById(R.id.tv_content)).setText(content);
    AlertDialog dialog = new AlertDialog.Builder(context).setView(view).create();
    dialog.setCancelable(false);
    dialog.setCanceledOnTouchOutside(false);
    dialog.show();
    new Handler().postDelayed(() -> {
      dialog.dismiss();
      callback.onDismiss();
    }, 4000);

  }

  public static void showConectLockDialog(@NonNull Context context, @NonNull String content, @NonNull DialogCallback callback) {
    View view = LayoutInflater.from(context).inflate(R.layout.view_toast_ok, null);
    ((TextView) view.findViewById(R.id.tv_content)).setText(content);
    AlertDialog dialog = new AlertDialog.Builder(context).setView(view).create();
    dialog.setCancelable(false);
    dialog.setCanceledOnTouchOutside(false);
    dialog.show();
    new Handler().postDelayed(() -> {
      dialog.dismiss();
      callback.onDismiss();
    }, 3000);
  }

  public static void showBleTipsDialog(@NonNull Context context, @NonNull String content) {
    showBleTipsDialog(context, content, null);
  }

  public static void showBleTipsDialog(@NonNull Context context, @NonNull String content, @NonNull DialogCallback callback) {
    try {
      View view = LayoutInflater.from(context).inflate(R.layout.dialog_ble_tips, null);
      ((TextView) view.findViewById(R.id.tv_content)).setText(content);
      AlertDialog dialog = new AlertDialog.Builder(context).setView(view).create();
      dialog.setCancelable(false);
      dialog.setCanceledOnTouchOutside(false);
      dialog.show();
      new Handler().postDelayed(() -> {
        dialog.dismiss();
        if (callback != null) {
          callback.onDismiss();
        }
      }, 600);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void showConfirmDialog(@NonNull Context context, @NonNull String title, @NonNull String content, int icon, @NonNull View.OnClickListener listener) {
    showConfirmDialog(context, title, content, icon, "确认", listener);
  }

  public static void showConfirmDialog(@NonNull Context context, @NonNull String title, @NonNull String content, int icon, @NonNull String btnText, @NonNull View.OnClickListener listener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_ble_ok, null);
    ((TextView) view.findViewById(R.id.tv_title)).setText(title);
    TextView contentText = (TextView) view.findViewById(R.id.tv_content);
    contentText.setText(content);

    if (icon != -1) {
      Drawable drawable = context.getResources().getDrawable(icon);
      drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
      contentText.setCompoundDrawablePadding(30);
      contentText.setCompoundDrawables(null, drawable, null, null);
    }

    AlertDialog dialog = new AlertDialog.Builder(context).setView(view).create();
    Button button = (Button) view.findViewById(R.id.btn_confirm);
    button.setText(btnText);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
        if (listener != null) {
          listener.onClick(v);
        }
      }
    });
    view.findViewById(R.id.rl_close).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
    dialog.setCancelable(false);
    dialog.setCanceledOnTouchOutside(false);
    dialog.show();
  }


  public interface QueryBindFinishedCallback {
    void onQueryFinished();
  }

  public static void showQueryBindRouterDialog(@NonNull Context context, @NonNull String title, @NonNull String content, long time, @NonNull QueryBindFinishedCallback callback) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_query_lock_router_bind, null);
    ((TextView) view.findViewById(R.id.tv_title)).setText(title);
    ((TextView) view.findViewById(R.id.tv_content)).setText(content);
    CircleProgress circleProgress = (CircleProgress) view.findViewById(R.id.progress_bind);
    AlertDialog dialog = new AlertDialog.Builder(context).setView(view).create();
    dialog.setCancelable(false);
    dialog.setCanceledOnTouchOutside(false);
    dialog.show();
    new CountDownTimer(time, 50) {
      @Override
      public void onTick(long millisUntilFinished) {
        int progress = (int) (((time - millisUntilFinished) / 1000.0) * 100.0 / (time / 1000.0));
        circleProgress.setProgress(progress);
      }

      @Override
      public void onFinish() {
        circleProgress.setProgress(100);

        dialog.dismiss();
        callback.onQueryFinished();
      }
    }.start();


  }

  public interface OnCatEyeRestartListener {
    void onCatEyeRestart();
  }
  public static void showRestartCatEyeDialog(@NonNull Context context, @NonNull String title, @NonNull String content, int resId, long time, @NonNull OnCatEyeRestartListener listener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_restart_cat_eye, null);
    AlertDialog dialog = new AlertDialog.Builder(context).setView(view).create();
    ((TextView) view.findViewById(R.id.tv_title)).setText(title);
    ((TextView) view.findViewById(R.id.tv_content)).setText(content);
    ImageView imageView = view.findViewById(R.id.iv_icon);
    imageView.setBackgroundResource(resId);

    int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    imageView.measure(width, height);
    int measuredWidth = imageView.getMeasuredWidth();
    int measuredHeight = imageView.getMeasuredHeight();

    RotateAnimation animation = new RotateAnimation(0, 360 * 3, measuredWidth / 2, measuredHeight / 2);
    imageView.startAnimation(animation);
    animation.setDuration(time);
    animation.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {

      }

      @Override
      public void onAnimationEnd(Animation animation) {
        dialog.dismiss();
        if (listener != null) {
          listener.onCatEyeRestart();
        }

      }

      @Override
      public void onAnimationRepeat(Animation animation) {

      }
    });
    animation.start();

    dialog.setCancelable(false);
    dialog.setCanceledOnTouchOutside(false);
    dialog.show();

  }

  public static void showWheelDialog(@NonNull Context context, @NonNull String[] items, int initPosition, OnItemSelectedListener listener) {
    View view = LayoutInflater.from(context).inflate(R.layout.dialog_wheel_view, null);
    AlertDialog dialog = new AlertDialog.Builder(context).setView(view).create();

    LoopView loopView = view.findViewById(R.id.loopView);
//    ArrayList<String> list = new ArrayList<>();
//    for (int i = 0; i < 15; i++) {
//      list.add("item " + i);
//    }
    // 滚动监听
    loopView.setListener(listener);
    // 设置原始数据
    loopView.setItems(Arrays.asList(items));
    loopView.setInitPosition(initPosition);

//    dialog.setCancelable(false);
//    dialog.setCanceledOnTouchOutside(false);
    dialog.show();

  }
}
