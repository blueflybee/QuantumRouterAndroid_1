package com.fernandocejas.android10.sample.presentation.view.component;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/07/26
 *     desc   : EditText输入监听，可以自定义各种输入条件，可以对多个EditText同时进行条件判断
 *     version: 1.0
 * </pre>
 */
public class InputWatcher {

  private Map<EditText, Boolean> mEts = new HashMap<>();

  private InputListener mInputListener;

  public InputWatcher() {
  }

  public void addEt(EditText editText) {
    editText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        mEts.put(editText, TextUtils.isEmpty(s.toString()));
        if (mInputListener != null) {
          mInputListener.onInput(mEts.containsValue(true));
        }
      }
    });

    mEts.put(editText, true);
  }

  public void addEt(EditText editText, WatchCondition condition) {
    editText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable editable) {
        boolean satisfy = condition.satisfy(editable);
        mEts.put(editText, !satisfy);
        if (mInputListener != null) {
          mInputListener.onInput(mEts.containsValue(true));
        }
      }
    });

    mEts.put(editText, true);
  }

  public void setInputListener(InputListener listener) {
    mInputListener = listener;
    mInputListener.onInput(mEts.containsValue(true));
  }

  public void clearEts() {
    mEts.clear();
  }

  public interface InputListener {
    void onInput(boolean satisfy);
  }

  public static class WatchCondition {

    private int length;
    private int minLength;

    private InputRange range;
    private InputByteRange byteRange;
    private InputRegular inputRegular;

    public int getLength() {
      return length;
    }

    public void setLength(int length) {
      this.length = length;
    }

    public int getMinLength() {
      return minLength;
    }

    public void setMinLength(int minLength) {
      this.minLength = minLength;
    }

    public InputRange getRange() {
      return range;
    }

    public void setRange(InputRange range) {
      this.range = range;
    }

    public void setByteRange(InputByteRange byteRange) {
      this.byteRange = byteRange;
    }

    public InputByteRange getByteRange() {
      return byteRange;
    }

    public InputRegular getInputRegular() {
      return inputRegular;
    }

    public void setInputRegular(InputRegular inputRegular) {
      this.inputRegular = inputRegular;
    }

    boolean satisfy(Editable editable) {

      boolean lengthSatisfy = true;
      boolean minLengthSatisfy = true;
      boolean rangeSatisfy = true;
      boolean byteRangeSatisfy = true;
      boolean inputRegularSatisfy = true;
      String input = editable.toString();

      if (length > 0) {
        lengthSatisfy = length == input.length();
      }

      if (minLength > 0) {
        minLengthSatisfy = input.length() >= minLength;
      }

      if (range != null) {
        rangeSatisfy = range.inRange(input);
      }

      if (byteRange != null) {
        byteRangeSatisfy = byteRange.inRange(input);
        if (!byteRangeSatisfy && input.getBytes().length >= byteRange.getEndLength()) {
          editable.delete(input.length() - 1, input.length());
          byteRangeSatisfy = true;
        }
      }

      if (inputRegular != null) {
        inputRegularSatisfy = inputRegular.match(input);
      }

      return lengthSatisfy && minLengthSatisfy && rangeSatisfy && byteRangeSatisfy && inputRegularSatisfy;
    }
  }

  /**
   * 输入长度范围
   */
  public static class InputRange {

    public InputRange(int startLength, int endLength) {
      this.startLength = startLength;
      this.endLength = endLength;
    }

    int startLength;
    int endLength;

    boolean inRange(String input) {
      int length;
      if (TextUtils.isEmpty(input)) {
        length = 0;
      } else {
        length = input.length();
      }
      return length >= startLength && length <= endLength;

    }

    public int getStartLength() {
      return startLength;
    }

    public void setStartLength(int startLength) {
      this.startLength = startLength;
    }

    public int getEndLength() {
      return endLength;
    }

    public void setEndLength(int endLength) {
      this.endLength = endLength;
    }
  }

  /**
   * 输入字节长度范围
   */
  public static class InputByteRange extends InputRange {

    public InputByteRange(int startLength, int endLength) {
      super(startLength, endLength);
    }

    @Override
    boolean inRange(String input) {
      int length;
      if (TextUtils.isEmpty(input)) {
        length = 0;
      } else {
        length = input.getBytes().length;
      }
      return length >= startLength && length <= endLength;
    }

  }

  /**
   * 输入正则表达式
   */
  public static class InputRegular {

    String regular;

    public InputRegular(String regular) {
      this.regular = regular;
    }

    public String getRegular() {
      return regular;
    }

    boolean match(String input) {
      if (TextUtils.isEmpty(input) || TextUtils.isEmpty(regular)) return false;
      Pattern p = Pattern.compile(regular);
      Matcher m = p.matcher(input);
      boolean isMatch = m.matches();
      System.out.println("input = " + input);
      System.out.println("isMatch = " + isMatch);
      return isMatch;
    }

  }

}
