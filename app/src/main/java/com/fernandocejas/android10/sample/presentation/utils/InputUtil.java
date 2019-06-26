package com.fernandocejas.android10.sample.presentation.utils;

import android.Manifest;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.PermissionUtils;
import com.fernandocejas.android10.sample.presentation.data.KVPair;
import com.fernandocejas.android10.sample.presentation.data.SpinnerItem;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock.ExceptionWarnMoreActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.ServiceProviderActivity;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 和spinner结合使用
 */
public class InputUtil {

  public static final int PERMISSION_REQUEST_CODE = 0;
  private static final String TAG = InputUtil.class.getName();

  //匹配中文和空格
  public static final String REGULAR_CHINESE_AND_SPACE = "[\\u4E00-\\u9fa5\\s]";
  public static final String REGULAR_CHINESE = "[\\u4E00-\\u9fa5]";

  //主机IPV4地址正则表达式
  public static final String REGULAR_IP = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
      + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
      + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
      + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
  //微信号
  public static final String REGULAR_WECHAT = "/^[a-zA-Z]{1}[-_a-zA-Z0-9]{5,19}$/";

  //子网掩码
  public static final String REGULAR_MASK = "^(254|252|248|240|224|192|128|0)\\.0\\.0\\.0|255\\.(254|252|248|240|224|192|128|0)\\.0\\.0|255\\.255\\.(254|252|248|240|224|192|128|0)\\.0|255\\.255\\.255\\.(254|252|248|240|224|192|128|0)$";

  //网关
  public static final String REGULAR_GATEWAY = "^((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))$";
  //域名
  public static final String REGULAR_DOMAIN = "^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$";

  /**
   * 禁止EditText输入空格
   *
   * @param editText
   */
  public static void inhibitSpace(EditText editText) {
    InputFilter filter = new InputFilter() {
      @Override
      public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (source.equals(" ")) return "";
        else return null;
      }
    };
    editText.setFilters(new InputFilter[]{filter});
  }

  /**
   * 只能输入数字、字母和汉字
   *
   * @param editText
   */
  public static void allowLetterNumberChinese(EditText editText) {
    InputFilter filter = (source, start, end, dest, dstart, dend) -> {
      String speChat = "[a-zA-Z0-9\\u4e00-\\u9fa5]+";
      Pattern pattern = Pattern.compile(speChat);
      Matcher matcher = pattern.matcher(source.toString());
      if (!matcher.find()) return "";
      else return null;
    };
    editText.setFilters(new InputFilter[]{filter});
  }

  /**
   * 只能输入数字、字母和汉字
   *
   * @param editText
   */
  public static void allowLetterNumberChinese(EditText editText, int maxLength) {
    allowLetterNumberChinese(editText);
    addMaxLengthFilter(editText, maxLength);
  }

  /**
   * 禁止指定正则表达式的输入
   *
   * @param editText
   */
  public static void inhibit(EditText editText, String regular, int maxLength) {
    inhibit(editText, regular);
    addMaxLengthFilter(editText, maxLength);
  }

  /**
  * 只能输入两位小数
  *
  * @param
  * @return
  */
  public static void setTwoPointLimit(final EditText editText) {
    editText.addTextChangedListener(new TextWatcher() {

      @Override
      public void onTextChanged(CharSequence s, int start, int before,
                                int count) {
        if (s.toString().contains(".")) {
          if (s.length() - 1 - s.toString().indexOf(".") > 2) {
            s = s.toString().subSequence(0,
                s.toString().indexOf(".") + 3);
            editText.setText(s);
            editText.setSelection(s.length());
          }
        }
        if (s.toString().trim().substring(0).equals(".")) {
          s = "0" + s;
          editText.setText(s);
          editText.setSelection(2);
        }

        if (s.toString().startsWith("0")
            && s.toString().trim().length() > 1) {
          if (!s.toString().substring(1, 2).equals(".")) {
            editText.setText(s.subSequence(0, 1));
            editText.setSelection(1);
            return;
          }
        }
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count,
                                    int after) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

      }

    });

  }

  /**
   * 禁止指定正则表达式的输入
   *
   * @param editText
   */
  public static void inhibit(EditText editText, String regular) {
    InputFilter filter = (source, start, end, dest, dstart, dend) -> {
      Pattern pattern = Pattern.compile(regular);
      Matcher matcher = pattern.matcher(source.toString());
      if (matcher.find()) return "";
      else return null;
    };
    editText.setFilters(new InputFilter[]{filter});
  }

  /**
   * 只允许指定正则表达式的输入
   *
   * @param editText
   */
  public static void allow(EditText editText, String regular) {
    InputFilter filter = new InputFilter() {
      @Override
      public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Pattern pattern = Pattern.compile(regular);
        Matcher matcher = pattern.matcher(source.toString());
        if (!matcher.find()) return "";
        else return source;
      }

    };
    editText.setFilters(new InputFilter[]{filter});
  }

  /**
   * 只允许指定正则表达式的输入
   *
   * @param editText
   */
  public static void allow(EditText editText, String regular, int maxLength) {
    allow(editText, regular);
    addMaxLengthFilter(editText, maxLength);
  }

  /**
   * 隐藏或显示密码
   *
   * @param et
   */
  public static void showHidePwd(EditText et) {
    int selectionStart = et.getSelectionStart();
    int hide = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
    if (et.getInputType() == hide) {
      et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    } else {
      et.setInputType(hide);
    }
    et.setSelection(selectionStart);
  }

  public static void addMaxLengthFilter(EditText editText, int maxLength) {
    InputFilter[] filters = editText.getFilters();
    InputFilter[] inputFilters = new InputFilter[filters.length + 1];
    for (int i = 0; i < filters.length; i++) {
      inputFilters[i] = filters[i];
    }
    inputFilters[filters.length] = new InputFilter.LengthFilter(maxLength);
    editText.setFilters(inputFilters);
  }

  public static void setMaxLengthFilter(EditText editText, int maxLength) {
    InputFilter[] inputFilters = new InputFilter[1];
    inputFilters[0] = new InputFilter.LengthFilter(maxLength);
    editText.setFilters(inputFilters);
  }

  /**
   * 该方法主要使用正则表达式来判断字符串中是否包含字母
   */
  public static boolean judgeContainsLetter(String cardNum) {
    String regex=".*[a-zA-Z]+.*";
    Matcher m=Pattern.compile(regex).matcher(cardNum);
    return m.matches();
  }

  /**
   * 限制 EditText 输入表情,同时显示clear按钮
   *
   * @param
   * @return
   */
  public static void watchEditText(EditText editView, ImageView clearView) {
    editView.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
          clearView.setVisibility(View.GONE);
        } else {
          clearView.setVisibility(View.VISIBLE);
          clearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              editView.getText().clear();
            }
          });
        }
      }

      @Override
      public void afterTextChanged(Editable editable) {
        int index = editView.getSelectionStart() - 1;
        if (index > 0) {
          if (isEmojiCharacter(editable.charAt(index))) {
            Editable edit = editView.getText();
            if (index == 1) {
              edit.clear();
            } else {
              edit.delete(index, index + 1);
            }
          }
        }
      }
    });
  }

  /**
   * 限制 EditText 输入表情,无clear按钮
   *
   * @param
   * @return
   */
  /*public static void watchEditTextNoClear(EditText editView) {
    editView.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
   *//*     if(TextUtils.isEmpty(s)){
          clearView.setVisibility(View.GONE);
        }else{
          clearView.setVisibility(View.VISIBLE);
          clearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              editView.getText().clear();
            }
          });
        }*//*
      }

      @Override
      public void afterTextChanged(Editable editable) {
        int index = editView.getSelectionStart() - 1;
        if (index > 0) {
          if (isEmojiCharacter(editable.charAt(index))) {
            Editable edit = editView.getText();
            if (index == 1) {
              edit.clear();
            } else {
              edit.delete(index, index + 1);
            }
          }
        }
      }
    });
  }*/

  /**
  * 控制表情输入
  *
  * @param
  * @return
  */
  private static InputFilter emojiFilter = new InputFilter() {
    Pattern emoji = Pattern.compile(
        "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
        Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
      Matcher emojiMatcher = emoji.matcher(source);
      if (emojiMatcher.find()) {
        return "";
      }
      return null;
    }
  };

  public static InputFilter[] emojiFilters = {emojiFilter};

  /**
   * 判断是否是表情
   *
   * @param
   * @return
   */
  public static boolean isEmojiCharacter(char codePoint) {
    return !((codePoint == 0x0)
        || (codePoint == 0x9)
        || (codePoint == 0xA)
        || (codePoint == 0xD)
        || ((codePoint >= 0x20) && codePoint <= 0xD7FF))
        || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
        || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
  }

  /**
   * EditText光标移动到最后
   *
   * @param
   * @return
   */
  public static void moveEditCursorToEnd(EditText editText) {
    Selection.setSelection(editText.getText(), editText.getText().length());
  }

  public static void requestPermissions(Context context) {
    String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    PermissionUtils.requestPermissions(context, PERMISSION_REQUEST_CODE, perms, new PermissionUtils.OnPermissionListener() {
      @Override
      public void onPermissionGranted() {
        Log.i(TAG, "允许权限");
      }

      @Override
      public void onPermissionDenied(String[] deniedPermissions) {
        Log.i(TAG, "拒绝权限");
      }
    });
  }

  /**
  * 解析域名为IP
  *
  * @param
  * @return
  */
  public static String convertDomainToIp(String host){
    try{
      System.out.println("域名解析成功");
      return InetAddress.getByName(host).getHostAddress();
    }catch (UnknownHostException e){
      e.printStackTrace();
      System.out.println("域名解析失败");
      return null;
    }
  }
}
