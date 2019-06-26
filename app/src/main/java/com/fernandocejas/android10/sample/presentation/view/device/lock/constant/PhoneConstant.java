package com.fernandocejas.android10.sample.presentation.view.device.lock.constant;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/07/03null
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PhoneConstant {

  public static final String BRAND_XIAO_MI = "Xiaomi";
  public static final String RED_MI_4A = "Redmi4A";

  public static final String BRAND_SAMSUNG = "samsung";
  public static final String GALAXY_S8 = "SM-G9550";

//  12-11 15:48:09.881 7429-7429/com.qtec.router I/System.out: manufacturer = vivo
//12-11 15:48:09.881 7429-7429/com.qtec.router I/System.out: model = vivoY66

//  12-25 10:02:20.350 23503-23503/com.qtec.router I/System.out: manufacturer = samsung
//12-25 10:02:20.350 23503-23503/com.qtec.router I/System.out: model = SM-G9550

  public static final String BRAND_VIVO = "vivo";
  public static final String VIVO_Y66 = "vivoY66";

  public static boolean isRedmi4A(String manufacturer, String model) {
    return BRAND_XIAO_MI.equalsIgnoreCase(manufacturer)
        && RED_MI_4A.equalsIgnoreCase(model);
  }

  public static boolean isVivoY66(String manufacturer, String model) {
    return BRAND_VIVO.equalsIgnoreCase(manufacturer)
        && VIVO_Y66.equalsIgnoreCase(model);
  }

  public static boolean isGalaxyS8(String manufacturer, String model) {
    return BRAND_SAMSUNG.equalsIgnoreCase(manufacturer)
        && GALAXY_S8.equalsIgnoreCase(model);
  }
}
