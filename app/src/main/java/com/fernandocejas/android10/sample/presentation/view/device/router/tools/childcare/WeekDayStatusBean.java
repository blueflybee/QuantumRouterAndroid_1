package com.fernandocejas.android10.sample.presentation.view.device.router.tools.childcare;

import android.widget.Button;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/09/05
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class WeekDayStatusBean {
  private Boolean flag;
  private Button btn;

  public Boolean getFlag() {
    return flag;
  }

  public void setFlag(Boolean flag) {
    this.flag = flag;
  }

  public Button getBtn() {
    return btn;
  }

  public void setBtn(Button btn) {
    this.btn = btn;
  }

  @Override
  public String toString() {
    return "WeekDayStatusBean{" +
        "flag=" + flag +
        ", btn=" + btn +
        '}';
  }
}
