package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.widget.TextView;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/08/17
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class SearchFileHistoryBean {
  private TextView textView;
  private String name;

  public SearchFileHistoryBean() {
  }

  public SearchFileHistoryBean(TextView textView, String name) {
    this.textView = textView;
    this.name = name;
  }

  public TextView getTextView() {
    return textView;
  }

  public void setTextView(TextView textView) {
    this.textView = textView;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "SearchFileHistoryBean{" +
        "textView=" + textView +
        ", name='" + name + '\'' +
        '}';
  }
}
