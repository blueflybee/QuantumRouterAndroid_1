package com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.constant;

public class MenuConstant {
  public static final String APPLY_MORE = "APPLY_MORE";
  public static final String APPLY_MINE = "APPLY_MINE";

  /**
   * 应用id获取应用类型
   *
   * @param id
   * @return
   */
  public static String getType(String id) {
    switch (id) {
      case "100001":
        return "dqzt";
      case "100002":
        return "xhtj";
      case "100003":
        return "znkd";
      case "100004":
        return "yjtj";
      case "100005":
        return "wxzj";
      case "100006":
        return "fcw";
      case "100007":
        return "kdcs";
      case "100008":
        return "tbgz";
      case "100009":
        return "dskg";
      case "1000010":
        return "fkwf";
      case "1000011":
        return "etgh";
      case "1000012":
        return "aqcc";
    }
    return "else";
  }
}
