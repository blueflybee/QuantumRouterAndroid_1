package com.fernandocejas.android10.sample.presentation.view.key;

import com.fernandocejas.android10.sample.data.data.BleLock;

import org.junit.Test;

import java.util.LinkedHashMap;

import sun.misc.BASE64Encoder;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class JavaTest {

  @Test
  public void decrypt() {
    byte[] keyByte = new byte[]{0X01, 0X23, 0X45, 0X67, (byte) 0X89, (byte) 0XAB, (byte) 0XCD, (byte) 0XEF, (byte) 0XFE, (byte) 0XDC, (byte) 0XBA, (byte) 0X98, 0X76, 0X54, 0X32, 0X10};
    String encode = new BASE64Encoder().encode(keyByte);
    System.out.println("encode = " + encode);

  }

  @Test
  public void testLockMapContainer() {
    BleLock bleLock1 = new BleLock();
    bleLock1.setId("lock_1");
    bleLock1.setMac("04:A3:16:4D:4E:CA");
    bleLock1.setBindRouterId("router_1");

    BleLock bleLock2 = new BleLock();
    bleLock2.setId("lock_2");
    bleLock2.setMac("04:A3:16:4D:4E:CB");
    bleLock2.setBindRouterId("router_2");

    LinkedHashMap<String, BleLock> hashMap = new LinkedHashMap<>();
    hashMap.put(bleLock1.getMac(), bleLock1);
    hashMap.put(bleLock2.getMac(), bleLock2);

    System.out.println("hashMap = " + hashMap);

    System.out.println("hashMap.containsValue(bleLock1) = " + hashMap.containsValue(bleLock1));
    System.out.println("hashMap.containsValue(bleLock2) = " + hashMap.containsValue(bleLock2));


  }

  @Test
  public void testCompareString() {
    int encryptPayloadLeng = 1008;
    int remainder = encryptPayloadLeng % 16;
    int dataLength = 0;
    if (remainder > 0) {
      dataLength = (encryptPayloadLeng / 16 + 1) * 16;
    } else if (remainder == 0) {
      dataLength = encryptPayloadLeng;
    }
    System.out.println("dataLength = " + dataLength);

  }

}