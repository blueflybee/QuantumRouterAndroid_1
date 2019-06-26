package com.fernandocejas.android10.sample.data.constant;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/07/03
 *     desc   : 错误码
 *     version: 1.0
 * </pre>
 */
public class ExceptionConstant {

  /**
   * code对应的msg
   *
   * @param
   * @return
   */
  public static String convertCodeToMsg(int code, String  expMsg) {
    String msg = "";

    switch (code) {
      case 1:
        msg = "您配置的时间段和已有时间段冲突";
        break;

      case 2:
        msg = "输入参数不全";
        break;

      case 3:
        msg = "规则超过最大限制";
        break;

      case -73:
        msg = "待认证设备已离线或已拉入黑名单";
        break;

      case -74:
        msg = "当前网关正在被配置，请稍候再试";
        break;

      case -75:
        msg = "获取升级信息失败";
        break;

      case -76:
        msg = "不支持多条规则生效";
        break;

      case -77:
        msg = "与已有规则冲突";
        break;

      case -78:
        msg = "不在内网IP网段";
        break;

      case -79:
        msg = "获取升级进度失败";
        break;

      case -80:
        msg = "未获得文件访问权限";
        break;

      case -81:
        msg = "网关还在产生密钥，请稍候进行该操作";
        break;

      case -82:
        msg = "请连接该网关的WiFi后重试";
        break;

      case -83:
        msg = "WiFi密码长度不足";
        break;

      case -84:
        msg = "WiFi密码太简单";
        break;

      case -85:
        msg = "测速端口未开启";
        break;

     /* case -86:
        msg = "";
        break;
*/
      case -87:
        msg = "没有权限";
        break;

      case -88:
        msg = "WAN端服务未开启";
        break;

      case -89:
        msg = "WAN端无法接入网络";
        break;

      case -90:
        msg = "WAN端无法获得DNS";
        break;

      case -91:
        msg = "WAN端网关不可达";
        break;

      case -92:
        msg = "WAN端IP地址缺失";
        break;

      case -93:
        msg = "密码错误，请重新输入";
        break;

      case -95:
        msg = "内部逻辑错误";
        break;

      case -97:
        msg = "请求参数缺失";
        break;

      case -99:
        msg = "不支持的路径";
        break;

   /*   case -94:
        msg = "";
        break;



      case -96:
        msg = "";
        break;



      case -98:
        msg = "";
        break;



      case -100:
        msg = "";
        break;*/

      default:
        msg = expMsg;
        break;
    }

    return msg;
  }
}
