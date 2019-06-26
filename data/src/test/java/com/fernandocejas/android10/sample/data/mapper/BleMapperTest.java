package com.fernandocejas.android10.sample.data.mapper;

import com.blueflybee.keystore.data.LZKey;
import com.fernandocejas.android10.sample.domain.utils.BleHexUtil;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.lock.model.core.SubBlePkg;
import com.qtec.lock.model.rsp.BleGetLockInfoResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
public class BleMapperTest {

  private BleMapper bleMapper;

  private byte[] keyByte = new byte[]{0X01, 0X23, 0X45, 0X67, (byte) 0X89, (byte) 0XAB, (byte) 0XCD, (byte) 0XEF, (byte) 0XFE, (byte) 0XDC, (byte) 0XBA, (byte) 0X98, 0X76, 0X54, 0X32, 0X10};


  //发送payload 50个字节 即加密部分长度为51字节
  private String data = "aa14ffffffff0000000100310102030405060708";
  private String subData_1 = "110033aa14ffffffff0000000100310102030405";
  private String subData_2 = "22000114ffef39f8f00000001003101020304050";
  private String subData_3 = "22000217ff070898673825a8b8f2902730efdca8";
  private String subData_4 = "440003983774384893ab7ec82638";


//  数据头 不是首包，代表包的索引值/首包代表加密部分有效数据长度(cmd + payload)   数据
//  Head  offset                                                    N_payload
//   1    2-3                                                          4-20


  @Before
  public void setUp() throws Exception {

    bleMapper = new BleMapper();
  }

  @After
  public void tearDown() throws Exception {
    bleMapper = null;
  }


  // 数据头 暂时不用（预留）     密钥key    用户id     序列号    命令     内容
//   head    len             Key_num   Usr_id    Seq     Cmdid    payload
//    1       2               3-6      7-10       11      12      13-20
  @Test
  public void testToByte() {
    String data = "31010203040514ffef39f8f0000000100310102030405017ff070898673825a8b8f2902730efdca8983774384893ab7ec8263800000000000000000000000000";
    byte[] bytes = BleHexUtil.hexStringToBytes(data);
    String byte2HexStr = BleHexUtil.byte2HexStr(keyByte).replace(" ", "").toLowerCase();
    System.out.println("byte2HexStr = " + byte2HexStr);

    byte[] bytes1 = BleHexUtil.hexStringToBytes(byte2HexStr);

    for (int i = 0; i < 16; i++)
      System.out.print(Integer.toHexString(bytes1[i] & 0xff) + "\t");

    byte[] bytes2 = subBytes(keyByte, 5, 10);
    String byte2HexStr2 = BleHexUtil.byte2HexStr(bytes2);
    System.out.println("byte2HexStr = " + byte2HexStr2);
//    System.out.println("");
//    String encodeHexStr = BleHexUtil.encodeHexStr(bytes);
//    System.out.println("encodeHexStr = " + encodeHexStr);
//    assertEquals(encodeHexStr, data);


//    byte[] out = new byte[data.length() / 2];
//    SM4Byte sm4Byte = new SM4Byte();
////    starttime = System.nanoTime();
//    sm4Byte.sms4(bytes, data.length() / 2, keyByte, out, 1);
//    System.out.print("密文: ");
//    for (int i = 0; i < 64; i++)
//      System.out.print(Integer.toHexString(out[i] & 0xff) + "\t");
//    System.out.println("");
//
//    String encodeHexStr = BleHexUtil.encodeHexStr(out);
//    System.out.println("encodeHexStr = " + encodeHexStr);
//    System.out.println("encodeHexStr = " + encodeHexStr.length());
//
//    sm4Byte.sms4(out, data.length() / 2, keyByte, out, 0);
//    System.out.print("明文: ");
//    for (int i = 0; i < 64; i++)
//      System.out.print(Integer.toHexString(out[i] & 0xff) + "\t");
//    System.out.println("");
//
//    encodeHexStr = BleHexUtil.encodeHexStr(out);
//    System.out.println("encodeHexStr = " + encodeHexStr);

  }

  private byte[] subBytes(byte[] src, int begin, int count) {
    byte[] bs = new byte[count];
    for (int i = begin; i < begin + count; i++) bs[i - begin] = src[i];
    return bs;
  }


  @Test
  public void testBlePkgToSubBlePkgsEncryption() {

    List<SubBlePkg> subBlePkgs = new ArrayList<>();
    SubBlePkg subBlePkg = new SubBlePkg();
    subBlePkg.setHead("11");
    subBlePkg.setIndex(0);
    subBlePkg.setOffset("0033");
    subBlePkg.setSubPayload("aa14ffff00000000000100cf98005b6de2");
    subBlePkgs.add(subBlePkg);

    subBlePkg = new SubBlePkg();
    subBlePkg.setHead("22");
    subBlePkg.setIndex(1);
    subBlePkg.setOffset("0001");
    subBlePkg.setSubPayload("8da86d50f48ec28b49e162ee73ccffdafd");
    subBlePkgs.add(subBlePkg);

    subBlePkg = new SubBlePkg();
    subBlePkg.setHead("22");
    subBlePkg.setIndex(2);
    subBlePkg.setOffset("0002");
    subBlePkg.setSubPayload("874813248a7334c3621c7390b08bd97d9c");
    subBlePkgs.add(subBlePkg);

    subBlePkg = new SubBlePkg();
    subBlePkg.setHead("22");
    subBlePkg.setIndex(3);
    subBlePkg.setOffset("0003");
    subBlePkg.setSubPayload("f704b63677d27f3e320c516be0fcf47fa9");
    subBlePkgs.add(subBlePkg);

    subBlePkg = new SubBlePkg();
    subBlePkg.setHead("44");
    subBlePkg.setIndex(4);
    subBlePkg.setOffset("0004");
    subBlePkg.setSubPayload("c39c2bbdf21a6a00000000000000000000");
    subBlePkgs.add(subBlePkg);

//    System.out.println("subBlePkgs = " + subBlePkgs);

    BlePkg pkg = new BlePkg();
    pkg.setHead("aa");
    pkg.setLength("14");
    pkg.setKeyId(BleMapper.DEFAULT_ENCRYPTION);
    pkg.setUserId("00000001");
    pkg.setSeq("00");
    BleBody body = new BleBody();
    body.setCmd("31");
    body.setPayload("010203040514ffef39f8f0000000100310102030405017ff070898673825a8b8f2902730efdca8983774384893ab7ec82638");
    pkg.setBody(body);

    List<SubBlePkg> actual = bleMapper.blePkgToSubPkgs(pkg, null);

    assertEquals(subBlePkgs, actual);

//    for (int i = 0; i < actual.size(); i++) {
//      SubBlePkg subPkg = actual.get(i);
//      byte[] bytes = subPkg.toBytes();
//      for (int j = 0; j < bytes.length; j++)
//        System.out.print(Integer.toHexString(bytes[j] & 0xff) + "\t");
//      System.out.println("");
//    }
  }

  @Test
  public void testBlePkgToSubBlePkgs() {

    List<SubBlePkg> subBlePkgs = new ArrayList<>();
    SubBlePkg subBlePkg = new SubBlePkg();
    subBlePkg.setHead("11");
    subBlePkg.setIndex(0);
    subBlePkg.setOffset("0033");
    subBlePkg.setSubPayload("aa14ffffffff0000000100310102030405");
    subBlePkgs.add(subBlePkg);

    subBlePkg = new SubBlePkg();
    subBlePkg.setHead("22");
    subBlePkg.setIndex(1);
    subBlePkg.setOffset("0001");
    subBlePkg.setSubPayload("14ffef39f8f00000001003101020304050");
    subBlePkgs.add(subBlePkg);

    subBlePkg = new SubBlePkg();
    subBlePkg.setHead("22");
    subBlePkg.setIndex(2);
    subBlePkg.setOffset("0002");
    subBlePkg.setSubPayload("17ff070898673825a8b8f2902730efdca8");
    subBlePkgs.add(subBlePkg);

    subBlePkg = new SubBlePkg();
    subBlePkg.setHead("44");
    subBlePkg.setIndex(3);
    subBlePkg.setOffset("0003");
    subBlePkg.setSubPayload("983774384893ab7ec82638000000000000");
    subBlePkgs.add(subBlePkg);

//    System.out.println("subBlePkgs = " + subBlePkgs);

    BlePkg pkg = new BlePkg();
    pkg.setHead("aa");
    pkg.setLength("14");
    pkg.setKeyId(BleMapper.NO_ENCRYPTION);
    pkg.setUserId("00000001");
    pkg.setSeq("00");
    BleBody body = new BleBody();
    body.setCmd("31");
    body.setPayload("010203040514ffef39f8f0000000100310102030405017ff070898673825a8b8f2902730efdca8983774384893ab7ec82638");
    pkg.setBody(body);

    List<SubBlePkg> actual = bleMapper.blePkgToSubPkgs(pkg, null);

    assertEquals(subBlePkgs, actual);

//    for (int i = 0; i < actual.size(); i++) {
//      SubBlePkg subPkg = actual.get(i);
//      byte[] bytes = subPkg.toBytes();
//      for (int j = 0; j < bytes.length; j++)
//        System.out.print(Integer.toHexString(bytes[j] & 0xff) + "\t");
//      System.out.println("");
//    }
  }


  @Test
  public void testBlePkgToSubBlePkgsOnePkg() {

    List<SubBlePkg> subBlePkgs = new ArrayList<>();
    SubBlePkg subBlePkg = new SubBlePkg();
    subBlePkg.setHead("11");
    subBlePkg.setIndex(0);
    subBlePkg.setOffset("0003");
    subBlePkg.setSubPayload("aa14ffffffff0000000100310102000000");
    subBlePkgs.add(subBlePkg);

    subBlePkg = new SubBlePkg();
    subBlePkg.setHead("44");
    subBlePkg.setIndex(1);
    subBlePkg.setOffset("0001");
    subBlePkg.setSubPayload("0000000000000000000000000000000000");
    subBlePkgs.add(subBlePkg);

//    System.out.println("subBlePkgs = " + subBlePkgs);

    BlePkg pkg = new BlePkg();
    pkg.setHead("aa");
    pkg.setLength("14");
    pkg.setKeyId(BleMapper.NO_ENCRYPTION);
    pkg.setUserId("00000001");
    pkg.setSeq("00");
    BleBody body = new BleBody();
    body.setCmd("31");
    body.setPayload("0102");
    pkg.setBody(body);

    List<SubBlePkg> actual = bleMapper.blePkgToSubPkgs(pkg, null);

    assertEquals(subBlePkgs, actual);

//    for (int i = 0; i < actual.size(); i++) {
//      SubBlePkg subPkg = actual.get(i);
//      byte[] bytes = subPkg.toBytes();
//      for (int j = 0; j < bytes.length; j++)
//        System.out.print(Integer.toHexString(bytes[j] & 0xff) + "\t");
//      System.out.println("");
//    }
  }

  @Test
  public void testBlePkgToSubBlePkgsOnePkgNoPayload() {

    List<SubBlePkg> subBlePkgs = new ArrayList<>();
    SubBlePkg subBlePkg = new SubBlePkg();
    subBlePkg.setHead("11");
    subBlePkg.setIndex(0);
    subBlePkg.setOffset("0001");
    subBlePkg.setSubPayload("aa14ffffffff0000000100310000000000");
    subBlePkgs.add(subBlePkg);

    subBlePkg = new SubBlePkg();
    subBlePkg.setHead("44");
    subBlePkg.setIndex(1);
    subBlePkg.setOffset("0001");
    subBlePkg.setSubPayload("0000000000000000000000000000000000");
    subBlePkgs.add(subBlePkg);

//    System.out.println("subBlePkgs = " + subBlePkgs);

    BlePkg pkg = new BlePkg();
    pkg.setHead("aa");
    pkg.setLength("14");
    pkg.setKeyId(BleMapper.NO_ENCRYPTION);
    pkg.setUserId("00000001");
    pkg.setSeq("00");
    BleBody body = new BleBody();
    body.setCmd("31");
    body.setPayload("");
    pkg.setBody(body);

    List<SubBlePkg> actual = bleMapper.blePkgToSubPkgs(pkg, null);

    assertEquals(subBlePkgs, actual);

//    for (int i = 0; i < actual.size(); i++) {
//      SubBlePkg subPkg = actual.get(i);
//      byte[] bytes = subPkg.toBytes();
//      for (int j = 0; j < bytes.length; j++)
//        System.out.print(Integer.toHexString(bytes[j] & 0xff) + "\t");
//      System.out.println("");
//    }
  }


  @Test
  public void testBytesToPkgEncryption() {
    BlePkg expected = new BlePkg();
    expected.setHead("aa");
    expected.setLength("14");
    expected.setKeyId(BleMapper.DEFAULT_ENCRYPTION);
    expected.setUserId("00000001");
    expected.setSeq("00");
    BleBody body = new BleBody();
    body.setLeng(51);
    body.setCmd("31");
    body.setPayload("010203040514ffef39f8f0000000100310102030405017ff070898673825a8b8f2902730efdca8983774384893ab7ec82638");
    expected.setBody(body);

    byte[] bytes1 = new byte[]{0x11, 0x00, 0x33, (byte) 0xaa, 0x14, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, (byte) 0xcf, (byte) 0x98, 0x00, 0x5b, 0x6d, (byte) 0xe2};
    byte[] bytes2 = new byte[]{0x22, 0x00, 0x01, (byte) 0x8d, (byte) 0xa8, 0x6d, 0x50, (byte) 0xf4, (byte) 0x8e, (byte) 0xc2, (byte) 0x8b, 0x49, (byte) 0xe1, 0x62, (byte) 0xee, 0x73, (byte) 0xcc, (byte) 0xff, (byte) 0xda, (byte) 0xfd};
    byte[] bytes3 = new byte[]{0x22, 0x00, 0x02, (byte) 0x87, 0x48, 0x13, 0x24, (byte) 0x8a, 0x73, 0x34, (byte) 0xc3, 0x62, 0x1c, 0x73, (byte) 0x90, (byte) 0xb0, (byte) 0x8b, (byte) 0xd9, 0x7d, (byte) 0x9c};
    byte[] bytes4 = new byte[]{0x22, 0x00, 0x03, (byte) 0xf7, 0x04, (byte) 0xb6, 0x36, 0x77, (byte) 0xd2, 0x7f, 0x3e, 0x32, 0x0c, 0x51, 0x6b, (byte) 0xe0, (byte) 0xfc, (byte) 0xf4, 0x7f, (byte) 0xa9};
    byte[] bytes5 = new byte[]{0x44, 0x00, 0x04, (byte) 0xc3, (byte) 0x9c, 0x2b, (byte) 0xbd, (byte) 0xf2, 0x1a, 0x6a};

    bleMapper.addSubPkg(bytes5);
    bleMapper.addSubPkg(bytes2);
    bleMapper.addSubPkg(bytes3);
    bleMapper.addSubPkg(bytes1);
    bleMapper.addSubPkg(bytes4);

    BlePkg actual = bleMapper.subPkgsToBlePkg(null);
    System.out.println("actual = " + actual);

    assertEquals(expected, actual);
//

  }

  @Test
  public void testBytesToPkg() {
    BlePkg expected = new BlePkg();
    expected.setHead("aa");
    expected.setLength("14");
    expected.setKeyId(BleMapper.NO_ENCRYPTION);
    expected.setUserId("00000001");
    expected.setSeq("00");
    BleBody body = new BleBody();
    body.setLeng(51);
    body.setCmd("31");
    body.setPayload("010203040514ffef39f8f0000000100310102030405017ff070898673825a8b8f2902730efdca8983774384893ab7ec82638");
    expected.setBody(body);

    byte[] bytes1 = new byte[]{0x11, 0x00, 0x33, (byte) 0xaa, 0x14, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00, 0x00, 0x00, 0x01, 0x00, 0x31, 0x01, 0x02, 0x03, 0x04, 0x05};
    byte[] bytes2 = new byte[]{0x22, 0x00, 0x01, 0x14, (byte) 0xff, (byte) 0xef, 0x39, (byte) 0xf8, (byte) 0xf0, 0x00, 0x00, 0x00, 0x10, 0x03, 0x10, 0x10, 0x20, 0x30, 0x40, 0x50};
    byte[] bytes3 = new byte[]{0x22, 0x00, 0x02, 0x17, (byte) 0xff, 0x07, 0x08, (byte) 0x98, 0x67, 0x38, 0x25, (byte) 0xa8, (byte) 0xb8, (byte) 0xf2, (byte) 0x90, 0x27, 0x30, (byte) 0xef, (byte) 0xdc, (byte) 0xa8};
    byte[] bytes4 = new byte[]{0x44, 0x00, 0x03, (byte) 0x98, 0x37, 0x74, 0x38, 0x48, (byte) 0x93, (byte) 0xab, 0x7e, (byte) 0xc8, 0x26, 0x38};

    bleMapper.addSubPkg(bytes3);
    bleMapper.addSubPkg(bytes2);
    bleMapper.addSubPkg(bytes1);
    bleMapper.addSubPkg(bytes4);

    BlePkg actual = bleMapper.subPkgsToBlePkg(null);
    System.out.println("actual = " + actual);

    assertEquals(expected, actual);
//

  }

  @Test
  public void testBytesToPkgOnePkg() {
    BlePkg expected = new BlePkg();
    expected.setHead("aa");
    expected.setLength("14");
    expected.setKeyId(BleMapper.NO_ENCRYPTION);
    expected.setUserId("00000001");
    expected.setSeq("00");
    BleBody body = new BleBody();
    body.setLeng(3);
    body.setCmd("31");
    body.setPayload("0102");
    expected.setBody(body);

    byte[] bytes1 = new byte[]{0x11, 0x00, 0x03, (byte) 0xaa, 0x14, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00, 0x00, 0x00, 0x01, 0x00, 0x31, 0x01, 0x02, 0x00, 0x00, 0x00};
    byte[] bytes2 = new byte[]{0x44, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    bleMapper.addSubPkg(bytes2);
    bleMapper.addSubPkg(bytes1);

    BlePkg actual = bleMapper.subPkgsToBlePkg(null);
    System.out.println("actual = " + actual);

    assertEquals(expected, actual);
//

  }

  @Test
  public void testBytesToPkgOnePkgNoPayload() {
    BlePkg expected = new BlePkg();
    expected.setHead("aa");
    expected.setLength("14");
    expected.setKeyId(BleMapper.NO_ENCRYPTION);
    expected.setUserId("00000001");
    expected.setSeq("00");
    BleBody body = new BleBody();
    body.setLeng(1);
    body.setCmd("31");
    body.setPayload("");
    expected.setBody(body);

    byte[] bytes1 = new byte[]{0x11, 0x00, 0x01, (byte) 0xaa, 0x14, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x00, 0x00, 0x00, 0x01, 0x00, 0x31, 0x00, 0x00, 0x00, 0x00, 0x00};
    byte[] bytes2 = new byte[]{0x44, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    bleMapper.addSubPkg(bytes2);
    bleMapper.addSubPkg(bytes1);

    BlePkg actual = bleMapper.subPkgsToBlePkg(null);
    System.out.println("actual = " + actual);

    assertEquals(expected, actual);
//

  }

  @Test
  public void testHexToUnicode() throws Exception {
    String s = BleHexUtil.str2HexStr("白日依山尽，黄河入海流");
    String s1 = BleHexUtil.hexStr2Str(s.replace(" ", "")+ "00000000000000000000000000").trim();
    String toLowerCase = s.toLowerCase().trim().replace(" ", "");
    System.out.println("s = " + s);
    System.out.println("s1 = " + s1);
    System.out.println("toLowerCase = " + toLowerCase);


  }

  @Test
  public void testGetKeysfromBytes() throws Exception {
    String data1 = "11003daa00ffffffff0000000101110000000100";
    String data2 = "2200010102030405060708090a0b0c0d0e0f0014";
    String data3 = "2200020002101112131415161718191a1b1c1d1e";
    String data4 = "2200031f00280003202122232425262728292a2b";
    String data5 = "4400042c2d2e2f00000001010101010101010101";
    byte[] bytes1 = BleHexUtil.hexStringToBytes(data1);
    byte[] bytes2 = BleHexUtil.hexStringToBytes(data2);
    byte[] bytes3 = BleHexUtil.hexStringToBytes(data3);
    byte[] bytes4 = BleHexUtil.hexStringToBytes(data4);
    byte[] bytes5 = BleHexUtil.hexStringToBytes(data5);

    bleMapper.addSubPkg(bytes3);
    bleMapper.addSubPkg(bytes2);
    bleMapper.addSubPkg(bytes1);
    bleMapper.addSubPkg(bytes4);
    bleMapper.addSubPkg(bytes5);
//
    BlePkg blePkg = bleMapper.subPkgsToBlePkg(null);
    System.out.println("blePkg = " + blePkg);
    List<LZKey> expected = new ArrayList<>();
    LZKey lzKey = new LZKey();
    lzKey.setId("00000001");
    lzKey.setKey("000102030405060708090a0b0c0d0e0f");
    expected.add(lzKey);

    lzKey = new LZKey();
    lzKey.setId("00140002");
    lzKey.setKey("101112131415161718191a1b1c1d1e1f");
    expected.add(lzKey);

    lzKey = new LZKey();
    lzKey.setId("00280003");
    lzKey.setKey("202122232425262728292a2b2c2d2e2f");
    expected.add(lzKey);

    System.out.println("expected = " + expected);


    List<LZKey> actual = bleMapper.getKeys(blePkg);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetLockInfoFromBytes() throws Exception {
    String data1 = "11003BAA14FFFFFFFF0000000100334c4f433239";
    String data2 = "22000138333476312e30000000006c6f636b3030";
    String data3 = "2200023100300031323334353637383930313233";
    String data4 = "2200033435363738393031323334353637383930";
    String data5 = "4400043132000000000000000000000000000000";
    byte[] bytes1 = BleHexUtil.hexStringToBytes(data1);
    byte[] bytes2 = BleHexUtil.hexStringToBytes(data2);
    byte[] bytes3 = BleHexUtil.hexStringToBytes(data3);
    byte[] bytes4 = BleHexUtil.hexStringToBytes(data4);
    byte[] bytes5 = BleHexUtil.hexStringToBytes(data5);

    bleMapper.addSubPkg(bytes3);
    bleMapper.addSubPkg(bytes2);
    bleMapper.addSubPkg(bytes1);
    bleMapper.addSubPkg(bytes4);
    bleMapper.addSubPkg(bytes5);

    BlePkg blePkg = bleMapper.subPkgsToBlePkg(null);
    System.out.println("blePkg = " + blePkg);

// 1234567812345678
// 76312e3000000000
// 6c6f636b30303100
// 0001
// 3132333435363738393031323334353637383930313233343536373839303132

    BleGetLockInfoResponse expected = new BleGetLockInfoResponse();
    expected.setDeviceId("LOC29834v1.0");
    expected.setVersion("lock001");
    expected.setModel("0 123456");
    expected.setType("78");
    expected.setName("901234567890123456789012");


    String s = BleHexUtil.str2HexStr("lock001");
    System.out.println("s = " + s.replace(" ", "").toLowerCase());

    BleGetLockInfoResponse actual = bleMapper.getLockInfo(blePkg);
    System.out.println("actual = " + actual);
    assertEquals(expected, actual);

  }

  @Test
  public void testRemoteUnlock() throws Exception {

    BlePkg pkg = new BlePkg();
    pkg.setHead("aa");
    pkg.setLength("0d");
    pkg.setKeyId(BleMapper.DEFAULT_ENCRYPTION);
    pkg.setUserId("00000001");
    pkg.setSeq("00");
    BleBody body = new BleBody();
    body.setCmd(BleMapper.BLE_CMD_UNLOCK_REMOTE);
    body.setPayload("00");
    pkg.setBody(body);

    List<SubBlePkg> subBlePkgs = bleMapper.blePkgToSubPkgs(pkg, null);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < subBlePkgs.size(); i++) {
      SubBlePkg subPkg = subBlePkgs.get(i);
      byte[] bytes = subPkg.toBytes();
      String hex = BleHexUtil.encodeHexStr(bytes);
      sb.append(hex);
    }
    String hexString = sb.toString();
    System.out.println("hexString = " + hexString);
    byte[] bytes = BleHexUtil.hexStringToBytes(hexString);
    for (int i = 0; i < 40; i++)
      System.out.print(Integer.toHexString(bytes[i] & 0xff) + "\t");
    System.out.println("");
    String encode = new BASE64Encoder().encode(bytes);
    System.out.println("encode = " + encode);

  }

  @Test
  public void testRemoteGetLockStatus() throws Exception {

    BlePkg pkg = new BlePkg();
    pkg.setHead("aa");
    pkg.setLength("11");
    pkg.setKeyId(BleMapper.NO_ENCRYPTION);
    pkg.setUserId("00000001");
    pkg.setSeq("00");
    BleBody body = new BleBody();
    body.setCmd(BleMapper.BLE_CMD_GET_LOCK_STATUS);
//    body.setPayload("0001010101");
    body.setPayload("0000000000");
    pkg.setBody(body);

    List<SubBlePkg> subBlePkgs = bleMapper.blePkgToSubPkgs(pkg, null);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < subBlePkgs.size(); i++) {
      SubBlePkg subPkg = subBlePkgs.get(i);
      byte[] bytes = subPkg.toBytes();
      String hex = BleHexUtil.encodeHexStr(bytes);
      sb.append(hex);
    }
    String hexString = sb.toString();
    System.out.println("hexString = " + hexString);
    byte[] bytes = BleHexUtil.hexStringToBytes(hexString);
    for (int i = 0; i < 40; i++)
      System.out.print(Integer.toHexString(bytes[i] & 0xff) + "\t");
    System.out.println("");
    String encode = new BASE64Encoder().encode(bytes);
    System.out.println("encode = " + encode);

  }

  /**
   * 测试pkg对象转换成"110001aa0cffffffff0000000100150000000000,4400010000000000000000000000000000000000"字符串
   * @throws Exception
   */
  @Test
  public void testPkgToReqString() throws Exception {
    BlePkg pkg = new BlePkg();
    pkg.setHead("aa");
    pkg.setLength("0c");
    pkg.setKeyId(BleMapper.NO_ENCRYPTION);
    pkg.setUserId("00000001");
    pkg.setSeq("00");
    BleBody body = new BleBody();
    body.setCmd(BleMapper.BLE_CMD_UNLOCK_APP_BESIDE);
    body.setPayload("");
    pkg.setBody(body);

    String expected = "110001aa0cffffffff0000000100150000000000,4400010000000000000000000000000000000000";

    String actual = bleMapper.pkgToReqString(pkg, "");

//    System.out.println("actual = " + actual);
    assertEquals(expected, actual);

  }

  @Test
  public void testMD5() throws Exception {

//    String hexStr = "A2 FF 44 56 13 4B 7D 87 AB 6D B0 2A D6 C6 7F E6 69 B0 55 45 6A ED F4 57 52 2D E6 B8 A7 16 74 0B";
//    hexStr = hexStr.replace(" ", "").toLowerCase();
//    byte[] hexStringToBytes = BleHexUtil.hexStringToBytes(hexStr);
//    System.out.println("hexStr = " + hexStr);
//    String encryption = EncryptUtils.encryptMD5ToString(hexStringToBytes);
//    System.out.println("encryption = " + encryption);

    String payload = "313131313131";
    String trim = BleHexUtil.hexStr2Str(payload.toUpperCase()).trim();
    System.out.println("trim = " + trim);

  }


}