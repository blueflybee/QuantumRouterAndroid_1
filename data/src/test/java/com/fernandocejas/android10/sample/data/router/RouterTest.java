package com.fernandocejas.android10.sample.data.router;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blueflybee.keystore.data.LZKey;
import com.blueflybee.sm2sm3sm4.library.SM4Byte;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.domain.utils.BleHexUtil;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.lock.model.core.SubBlePkg;
import com.qtec.lock.model.rsp.BleGetLockInfoResponse;

import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.jce.provider.JCEMac;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import sun.misc.BASE64Encoder;

import static org.junit.Assert.assertEquals;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class RouterTest {

  private byte[] keyByte = new byte[]{0X01, 0X23, 0X45, 0X67, (byte) 0X89, (byte) 0XAB, (byte) 0XCD, (byte) 0XEF, (byte) 0XFE, (byte) 0XDC, (byte) 0XBA, (byte) 0X98, 0X76, 0X54, 0X32, 0X10};

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testMd5() {
    String md5ToString = MD5Util.encryption("111111");
    System.out.println("md5ToString = " + md5ToString);


  }

  @Test
  public void testStringToBytes() {
    String ss = "12345哈哈";
    byte[] buff = new byte[0];
    try {
      buff = ss.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    int f = buff.length;
    System.out.println(f);

  }

}