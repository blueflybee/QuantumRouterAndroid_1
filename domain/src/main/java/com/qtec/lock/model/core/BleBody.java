package com.qtec.lock.model.core;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/29
 *     desc   : 蓝牙通信数据部分payload
 *     version: 1.0
 * </pre>
 */
public class BleBody {

  public static final String RSP_OK = "00";
  public static final String RSP_LOCK_KEY_INVALID = "02";//无效的密钥id
  public static final String RSP_INVALID_USER = "03";//无效的用户id（用户满了）
  public static final String RSP_CONFIG_MODE_NOT_OPEN = "04";//未在管理模式下操作（没有按管理按键）
  public static final String RSP_NO_SUCH_CMD = "05";//无效的操作码
  public static final String RSP_PWD_DUPLICATE = "06";//添加密码重复
  public static final String RSP_PWD_IS_FULL = "07";//密码已满
  public static final String RSP_FP_IS_FULL = "08";//指纹已满
  public static final String RSP_CARD_IS_FULL = "0f";//卡片已满
  public static final String RSP_SYS_LOCKED = "09";//系统锁定
  public static final String RSP_UPDATING = "0a";//门锁升级中 操作无效
  public static final String RSP_LOCK_FACTORY_RESET = "0b";//门锁恢复出厂设置

  public static final String RSP_NO_ZIGBEE = "0d";//未获取到ZigBee信息（33接口返回，门锁硬件问题导致，可能是无zigbee模块）
  public static final String RSP_NO_MAC_ADDRESS = "0e";//门锁恢复出厂设置（33接口返回，门锁硬件问题）
  public static final String RSP_ILLEGAL_CMD = "11";//指令非法（不符合协议要求，一般是解密失败，作密钥失效处理）

  private String cmd;

  private String payload;

  private int leng;

  public String getCmd() {
    return cmd;
  }

  public void setCmd(String cmd) {
    this.cmd = cmd;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  public int getLeng() {
    return leng;
  }

  public void setLeng(int leng) {
    this.leng = leng;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    BleBody body = (BleBody) o;

    if (leng != body.leng) return false;
    if (cmd != null ? !cmd.equals(body.cmd) : body.cmd != null) return false;
    return payload != null ? payload.equals(body.payload) : body.payload == null;

  }

  @Override
  public int hashCode() {
    int result = cmd != null ? cmd.hashCode() : 0;
    result = 31 * result + (payload != null ? payload.hashCode() : 0);
    result = 31 * result + leng;
    return result;
  }

  @Override
  public String toString() {
    return "BleBody{" +
        "cmd='" + cmd + '\'' +
        ", payload='" + payload + '\'' +
        ", leng=" + leng +
        '}';
  }
}
