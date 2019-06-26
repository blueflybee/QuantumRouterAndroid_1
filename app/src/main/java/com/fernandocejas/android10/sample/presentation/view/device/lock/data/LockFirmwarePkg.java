package com.fernandocejas.android10.sample.presentation.view.device.lock.data;

import com.fernandocejas.android10.sample.domain.utils.BleHexUtil;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class LockFirmwarePkg {

  private String hexStr;
  private String seq;
  private String checkSum;

  public String getHexStr() {
    return hexStr;
  }

  public void setHexStr(String hexStr) {
    this.hexStr = hexStr;
    calculateCheckSum(hexStr);
  }

  private void calculateCheckSum(String hexStr) {
    byte byteSum = 0;
    byte[] bytes = BleHexUtil.hexStringToBytes(hexStr);
    for (byte aByte : bytes) {
      byteSum += aByte;
    }
    byte sum = (byte) (byteSum & 0XFF);
    byte[] b = new byte[1];
    b[0] = sum;
    this.checkSum = BleHexUtil.byte2HexStr(b).toLowerCase();
  }

  public String getSeq() {
    return seq;
  }

  public String getCheckSum() {
    return checkSum;
  }

  public void setSeq(int seqI) {
    String hexSeq = Integer.toHexString(seqI);
    int length = hexSeq.length();
    switch (length) {
      case 1:
        hexSeq = "000" + hexSeq;
        break;
      case 2:
        hexSeq = "00" + hexSeq;
        break;
      case 3:
        hexSeq = "0" + hexSeq;
        break;
      default:
        break;
    }

    this.seq = hexSeq;
  }

  public String pkgToString() {
    return getHexStr() + getSeq() + getCheckSum();
  }

  @Override
  public String toString() {
    return "LockFirmwarePkg{" +
        "hexStr='" + hexStr.substring(0, 20) + "..." + '\'' +
        ", seq='" + seq + '\'' +
        ", checkSum='" + checkSum + '\'' +
        '}';
  }
}
