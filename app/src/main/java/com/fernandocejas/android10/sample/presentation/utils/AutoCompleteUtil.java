package com.fernandocejas.android10.sample.presentation.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

/**
 * @author shaojun
 * @name AutoCompleteUtil
 * @package com.fernandocejas.android10.sample.presentation.view.utils
 * @date 15-11-5
 */
public class AutoCompleteUtil {
  private static final String PREF = "MyHistory";

  /**
   * 初始化AutoCompleteTextView，最多显示5项提示，使 AutoCompleteTextView在一开始获得焦点时自动提示
   *
   * @param field 保存在sharedPreference中的字段名(key)
   * @param auto 要操作的AutoCompleteTextView
   */
  public static void initAutoComplete(final Context context, String field,
      AutoCompleteTextView auto) {
    SharedPreferences sp = context.getSharedPreferences(PREF, 0);
    String longhistory = sp.getString(field, "nothing");

    String[] hisArrays = longhistory.split(",");

    ArrayAdapter<String> adapter =
        new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, hisArrays);
    // 只保留最近的50条的记录
    if (hisArrays.length > 50) {
      String[] newArrays = new String[50];
      System.arraycopy(hisArrays, 0, newArrays, 0, 50);
      adapter =
          new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, newArrays);
    }
    auto.setAdapter(adapter);
    auto.setDropDownHeight(350);
    auto.setThreshold(1);
    auto.setCompletionHint("最近的5条记录");
    auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      public void onFocusChange(View v, boolean hasFocus) {
        AutoCompleteTextView view = (AutoCompleteTextView) v;
        if (hasFocus) {
          view.showDropDown();
        }
        SharedPreferences sp = context.getSharedPreferences(PREF, 0);
        String longhistory = sp.getString("history", "nothing");

        String[] hisArrays = longhistory.split(",");

        ArrayAdapter<String> adapter =
            new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line,
                hisArrays);
      }
    });
  }

  /**
   * 保存数据时选择一个固定值做　key
   * 这样再读取时才知道通过什么key来取值。
   */
  public static void saveHistory(Context context, String field, AutoCompleteTextView auto) {
    String addText = auto.getText().toString();
    SharedPreferences sp = context.getSharedPreferences(PREF, 0);
    SharedPreferences.Editor edit = sp.edit();
    String longhistory = sp.getString(field, "nothing");

    if (!longhistory.contains(addText + ",")) {
      StringBuilder sb = new StringBuilder(longhistory);
      sb.insert(0, addText + ",");
      edit.putString("history", sb.toString());
      edit.commit();
    }
  }
}
