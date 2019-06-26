package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/08/02
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class LoginConfig {
  public static String USER_NAME = "network";
/*  public static String USER_PWD = "123456";
  public static String LAN_IP = "192.168.1.1";  //局域网*/

  public static String USER_PWD = "S6QE510O";
  public static String LAN_IP = "192.168.1.1";  //局域网
  public static int OS_PORT = 0;  //端口号

  public static String OS_IP = "192.168.92.59"; //中转云

  public static String OS_REMOTE_ROOT_IP = "smb://192.168.92.55:"+LoginConfig.OS_PORT+"/qtec/"; //外网
  public static String LAN_REMOTE_ROOT_IP = "smb://192.168.1.1/qtec/";  //内网

  public static String LAN_REMOTE_SHARE_IP = "qtec";

}
