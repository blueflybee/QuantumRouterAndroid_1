package com.fernandocejas.android10.sample.data.mapper;

import android.text.TextUtils;

import com.blankj.utilcode.util.EncryptUtils;
import com.blueflybee.keystore.KeystoreRepertory;
import com.blueflybee.keystore.data.LZKey;
import com.blueflybee.sm2sm3sm4.library.SM4Byte;
import com.fernandocejas.android10.sample.domain.utils.BleHexUtil;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.lock.model.core.SubBlePkg;
import com.qtec.lock.model.rsp.BleAddLockFpResponse;
import com.qtec.lock.model.rsp.BleAddLockPwdResponse;
import com.qtec.lock.model.rsp.BleCheckLockAdminPwdResponse;
import com.qtec.lock.model.rsp.BleGetLockInfoResponse;
import com.qtec.lock.model.rsp.BleGetRemoteLockStatusResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author shaojun
 * @name JsonMapper
 * @package com.fernandocejas.android10.sample.data.entity.mapper
 * @date 15-9-11
 * <p>
 * Json mapper transforms json to object/object to json.
 */
public class BleMapper {

  public static final String ACTION_BLE_LOCK_KEY_INVALID = "android.intent.action_ble_lock_key_invalid";

  public static final String SERVICE_UUID = "0000ffb0-0000-1000-8000-00805f9b34fb";
  public static final String CHARACTERISTIC_UUID = "0000ffb2-0000-1000-8000-00805f9b34fb";

  public static final String ENCRYPT_SM4 = "SM4";
  public static final String ENCRYPT_AES = "AES";

  private static String mEncryptionType = ENCRYPT_AES;

  private byte[] mKeyByte =
      new byte[]{0X01, 0X23, 0X45, 0X67, (byte) 0X89, (byte) 0XAB, (byte) 0XCD, (byte) 0XEF, (byte) 0XFE, (byte) 0XDC, (byte) 0XBA, (byte) 0X98, 0X76, 0X54, 0X32, 0X10};

  private byte[] mKeyByteOfLockPin;

  //keys for update firmware
  private LZKey mEncryptKeyOfUpdateFirmware = null;

  //0x11 多包模式起始包标志
  public static final String MULTI_PKG_FLAG_START = "11";
  //0x22 多包模式中间包标志
  public static final String MULTI_PKG_FLAG_MIDDLE = "22";
  //0x44 多包模式结束包标志
  public static final String MULTI_PKG_FLAG_END = "44";
  public static final String SINGLE_PKG_FLAG_START = "aa";

  public static final String NO_ENCRYPTION = "ffffffff";
  public static final String DEFAULT_ENCRYPTION = "ffff0000";
  public static final String DEFAULT_ENCRYPTION_WITH_PIN_KEY = "ffff0001";
  public static final String DEFAULT_ENCRYPTION_WHEN_UNLOCK = "ffff0002";
  public static final String DEFAULT_ENCRYPTION_WHEN_UPDATE_FIRMWARE = "ffff0003";
  public static final String KEY_STORTE_ENCRYPTION = "key_storte_encryption";


  public static final String BLE_CMD_LOCK = "01"; //上锁
  //  public static final String BLE_CMD_UNLOCK_KEY            = "02"; //钥匙开锁
//  public static final String BLE_CMD_UNLOCK_FP             = "03"; //指纹开锁
//  public static final String BLE_CMD_UNLOCK_PWD            = "04"; //密码开锁
//  public static final String BLE_CMD_UNLOCK_BLE            = "05"; //蓝牙开锁
  public static final String BLE_CMD_UNLOCK_REMOTE = "06"; //远程开锁
  public static final String BLE_CMD_GET_UNLOCK_RADOM_CODE_REMOTE = "1b"; //远程开锁
  public static final String BLE_CMD_LOCK_REMOTE = "07"; //远程上锁
  public static final String BLE_CMD_ADD_FP = "08"; //添加指纹
  public static final String BLE_CMD_ADD_FP_CONFIRM = "16"; //添加指纹确认
  public static final String BLE_CMD_ADD_PWD_ON_LOCK = "09"; //门锁端添加密码（暂时不使用）
  public static final String BLE_CMD_ADD_PWD_BLE = "0a"; //通过蓝牙添加密码
  public static final String BLE_CMD_ADD_PWD_REMOTE = "17"; //远程添加密码（暂时不使用）
  public static final String BLE_CMD_ADD_PWD_CONFIRM = "18"; //添加密码确认
  public static final String BLE_CMD_DEL_SINGLE_FP = "0b"; //删除单一指纹
  public static final String BLE_CMD_DEL_SINGLE_PWD = "0c"; //删除单一密码
  public static final String BLE_CMD_DEL_ALL_FP = "0d"; //删除全部指纹
  public static final String BLE_CMD_DEL_ALL_PWD = "0e"; //删除全部密码
  public static final String BLE_CMD_DEL_MULTI_FP = "0f"; //删除批量指纹
  public static final String BLE_CMD_DEL_MULTI_PWD = "10"; //删除批量密码
  public static final String BLE_CMD_APPLY_KEYS = "11"; //申请密钥更新
  public static final String BLE_CMD_GET_LOCK_STATUS = "14"; //门锁状态查询
  public static final String BLE_CMD_UNLOCK_APP_BESIDE = "15"; //近场APP开锁
  public static final String BLE_CMD_BLE_HEARTBEAT = "30"; //蓝牙心跳通信
  public static final String BLE_CMD_BLE_BIND_CONFIRM = "31"; //蓝牙绑定确认
  public static final String BLE_CMD_LOCK_NET_STATUS = "32"; //门锁网络状态
  public static final String BLE_CMD_GET_LOCK_INFO = "33"; //获取门锁信息
  public static final String BLE_CMD_BIND_ROUTER_ZIGBEE = "34"; //开启ZigBee组网、门锁绑定网关
  public static final String BLE_CMD_FIRST_GET_KEY_SEND_PIN = "1a"; //
  public static final String BLE_CMD_GET_UNLOCK_RADOM_CODE = "19"; //
  public static final String BLE_CMD_UPDATE_USER_INFO_OF_LOCK = "1c"; //
  public static final String BLE_CMD_CONFIG_UNLOCK_MODE_V15 = "1d"; //V15以下门锁版本开锁方式配置
  public static final String BLE_CMD_CONFIG_UNLOCK_MODE = "26"; //V16以上门锁开锁方式配置


  public static final String BLE_CMD_ADD_DOOR_CARD = "1e"; //添加卡片
  public static final String BLE_CMD_ADD_DOOR_CARD_CONFIRM = "21"; //添加卡片确认
  public static final String BLE_CMD_DEL_SINGLE_DOOR_CARD = "1f"; //删除单一卡片
  public static final String BLE_CMD_DEL_ALL_DOOR_CARD = "20"; //删除全部卡片

  public static final String BLE_CMD_UPDATE_FIRMWARE_START = "40"; //固件升级开始
  public static final String BLE_CMD_UPDATE_FIRMWARE_TRANS = "41"; //固件文件传输
  public static final String BLE_CMD_UPDATE_FIRMWARE_END = "42"; //固件升级结束

  public static final String BLE_CMD_CHECK_ADMIN_PASSWORD = "36"; //添加门锁校验管理员密码
  public static final String BLE_CMD_CLEAR_FACTORY_RESET_FLAG = "22"; //清除恢复出厂设置标志位(云端删除确认)
  public static final String BLE_CMD_UNBIND_LOCK = "37"; //解绑门锁
  public static final String BLE_CMD_CONFIG_PASSAGEWAY = "23"; //通道模式配置
  public static final String BLE_CMD_GET_TEMP_PWD = "28"; //通道模式配置
  public static final String BLE_CMD_ADJUST_VOLUME = "27";

  public static final int ENCRYPT_LENGTH = 16;

  private LZKey mReuseKey = null;


  private List<SubBlePkg> mSubBlePkgs = new ArrayList<>();

  public BleMapper() {
  }

  private String blePkgToString(BlePkg pkg) {
    if (pkg == null) return "";
    StringBuilder sb = new StringBuilder();
    sb.append(pkg.getHead());
    sb.append(pkg.getLength());
    sb.append(pkg.getKeyId());
    sb.append(pkg.getUserId());
    sb.append(pkg.getSeq());
    sb.append(pkg.getBody().getCmd());
    sb.append(pkg.getBody().getPayload());
    return sb.toString();
  }

  public void addSubPkg(byte[] bytes) {
    try {
      String hexStr = BleHexUtil.encodeHexStr(bytes);
      if (hexStr == null || "".equals(hexStr)) return;

      SubBlePkg subBlePkg = new SubBlePkg();
      String head = hexStr.substring(0, 2);
      String offset = hexStr.substring(2, 6);
      String subPayload = hexStr.substring(6, hexStr.length());
      subBlePkg.setHead(head);
      subBlePkg.setOffset(offset);
      subBlePkg.setIndex(MULTI_PKG_FLAG_START.equals(head) ? 0 : Integer.parseInt(offset, 16));
      subBlePkg.setSubPayload(subPayload);
      mSubBlePkgs.add(subBlePkg);

    } catch (Exception e) {
      e.printStackTrace();
      mSubBlePkgs.add(new SubBlePkg());
    }
  }

  public BlePkg subPkgsToBlePkg(String keyRepoId) {
    BlePkg pkg = null;
    try {
      Collections.sort(mSubBlePkgs);
      int encryptPayloadLeng = 0;
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < mSubBlePkgs.size(); i++) {

        SubBlePkg subBlePkg = mSubBlePkgs.get(i);
        System.out.println("subBlePkg = " + subBlePkg);
        if (MULTI_PKG_FLAG_START.equals(subBlePkg.getHead())) {
          encryptPayloadLeng = Integer.parseInt(subBlePkg.getOffset(), 16);
        }
        sb.append(subBlePkg.getSubPayload());
      }

      String hexStr = sb.toString();
      if (hexStr == null || "".equals(hexStr)) return null;
      pkg = new BlePkg();

      String head = hexStr.substring(0, 2);
      String len = hexStr.substring(2, 4);
      String keyId = hexStr.substring(4, 12);
      String userId = hexStr.substring(12, 20);
      String seq = hexStr.substring(20, 22);
      String cmd = hexStr.substring(22, 24);
      String payload = hexStr.substring(24, hexStr.length());

      pkg.setHead(head);
      pkg.setLength(len);
      pkg.setKeyId(keyId);
      pkg.setUserId(userId);
      pkg.setSeq(seq);

      BleBody body = new BleBody();
      body.setLeng(encryptPayloadLeng);
      body.setCmd(cmd);
      body.setPayload(payload);

      byte[] key = null;
      if (NO_ENCRYPTION.equals(keyId)) {
        key = null;

      } else if (DEFAULT_ENCRYPTION.equals(keyId)) {
        if (mKeyByteOfLockPin != null) {
          key = mKeyByteOfLockPin;
        } else {
          key = mKeyByte;
        }

      } else {
        if (TextUtils.isEmpty(keyRepoId)) return pkg;
        LZKey lzKey = KeystoreRepertory.getInstance().key(keyRepoId, keyId);
        key = BleHexUtil.hexStringToBytes(lzKey.getKey());
      }

      if (key != null) {
        String encryptData = cmd + payload;
//        System.out.println("encryptPayloadLeng = " + encryptPayloadLeng);
        byte[] out = decrypt(key, encryptData, encryptPayloadLeng);

        String decryptStr = BleHexUtil.encodeHexStr(out);
        String decryptCmd = decryptStr.substring(0, 2);
        //获取密钥发现有丢包，对包总长进行判断
        if (BLE_CMD_APPLY_KEYS.equals(decryptCmd)) {

          System.out.println("body.getLeng() * 2 = " + body.getLeng() * 2);
          System.out.println("decryptStr.length() = " + decryptStr.length());
          if (decryptStr.length() < body.getLeng() * 2) {
            System.out.println("keys length is shorter than expected, drop pkg!!! +++++++ ");
            return null;
          }

        }
        decryptStr = decryptStr.substring(0, body.getLeng() * 2);

//        System.out.println("decryptStr = " + decryptStr);


        body.setCmd(decryptCmd);
        body.setPayload(decryptStr.substring(2));
      }

      int playloadLength = body.getLeng() * 2 - 2;
      body.setPayload(body.getPayload().substring(0, playloadLength));

      pkg.setBody(body);
    } catch (Exception e) {
      e.printStackTrace();
      return pkg;
    }

    return pkg;
  }


  public List<SubBlePkg> blePkgToSubPkgs(BlePkg pkg, String keyRepoId) {
    ArrayList<SubBlePkg> subBlePkgs = null;
    try {
      subBlePkgs = new ArrayList<>();
      if (pkg == null) return subBlePkgs;

      BleBody body = pkg.getBody();
      String payload = body.getPayload();
      if (payload == null) payload = "";
      int encryptByteLength = payload.length() / 2 + 1;
      body.setLeng(encryptByteLength);
      String keyId = pkg.getKeyId();
      byte[] key = null;
      if (NO_ENCRYPTION.equals(keyId)) {
        key = null;
      } else if (DEFAULT_ENCRYPTION.equals(keyId)) {
        key = mKeyByte;
      } else if (DEFAULT_ENCRYPTION_WITH_PIN_KEY.equals(keyId)) {
        key = mKeyByteOfLockPin;
        pkg.setKeyId(DEFAULT_ENCRYPTION);
      } else if (DEFAULT_ENCRYPTION_WHEN_UNLOCK.equals(keyId)) {
        String randomCode = pkg.getBody().getPayload();
        if (randomCode == null) randomCode = "";
        if (TextUtils.isEmpty(keyRepoId)) return subBlePkgs;
        LZKey lzKey = KeystoreRepertory.getInstance().key(keyRepoId);
        String unlockKey = lzKey.getKey();
        byte[] unlockMd5Bytes = BleHexUtil.hexStringToBytes(unlockKey + randomCode);
        String unlockMd5Str = EncryptUtils.encryptMD5ToString(unlockMd5Bytes);

        String unlockPayload = lzKey.getId() + unlockMd5Str;

        pkg.setKeyId(NO_ENCRYPTION);
        pkg.getBody().setPayload(unlockPayload);
        int unlockPayloadByteLength = unlockPayload.length() / 2 + 1;
        pkg.getBody().setLeng(unlockPayloadByteLength);
        key = null;
      } else if (DEFAULT_ENCRYPTION_WHEN_UPDATE_FIRMWARE.equals(keyId)) {
        if (TextUtils.isEmpty(keyRepoId)) return subBlePkgs;
        if (mEncryptKeyOfUpdateFirmware == null) {
          mEncryptKeyOfUpdateFirmware = KeystoreRepertory.getInstance().key(keyRepoId);
        }
        key = BleHexUtil.hexStringToBytes(mEncryptKeyOfUpdateFirmware.getKey());
        pkg.setKeyId(mEncryptKeyOfUpdateFirmware.getId());

      } else {
        String cmd = pkg.getBody().getCmd();
        if (BLE_CMD_APPLY_KEYS.equals(cmd)
            && mReuseKey != null
            && !TextUtils.isEmpty(mReuseKey.getId())
            && !TextUtils.isEmpty(mReuseKey.getKey())) {
          pkg.setKeyId(mReuseKey.getId());
          key = BleHexUtil.hexStringToBytes(mReuseKey.getKey());
        } else {
          if (TextUtils.isEmpty(keyRepoId)) return subBlePkgs;
          LZKey lzKey = KeystoreRepertory.getInstance().key(keyRepoId);
          if (!TextUtils.isEmpty(lzKey.getId())) {
            pkg.setKeyId(lzKey.getId());
            key = BleHexUtil.hexStringToBytes(lzKey.getKey());
          } else {
            pkg.setKeyId(DEFAULT_ENCRYPTION);
            key = mKeyByte;
          }
        }
      }


      if (key != null) {
        System.out.println("encryptByteLength = " + encryptByteLength);
        int encryptRemainder = encryptByteLength % ENCRYPT_LENGTH;
        System.out.println("encryptRemainder = " + encryptRemainder);
        int zeroFillLength = 0;
        if (encryptRemainder != 0) {
          zeroFillLength = ((encryptByteLength / ENCRYPT_LENGTH + 1) * ENCRYPT_LENGTH - encryptByteLength) * 2;
        }
        System.out.println("zeroFillLength = " + zeroFillLength);
        for (int i = 0; i < zeroFillLength; i++) {
          payload += "0";
        }
        body.setPayload(payload);
        System.out.println("pkg = " + pkg);
        String toEncryptData = body.getCmd() + body.getPayload();

        byte[] out = encrypt(key, toEncryptData);

        String encodeHexStr = BleHexUtil.encodeHexStr(out);
        body.setCmd(encodeHexStr.substring(0, 2));
        body.setPayload(encodeHexStr.substring(2));
      }


//      System.out.println("pkg = " + pkg);

      String encryptData = blePkgToString(pkg);

//      System.out.println("encryptData = " + encryptData);

//      System.out.println("encryptData.length() = " + encryptData.length());

      int subPkgSize = encryptData.length() / 34;
      int subPkgRemainder = encryptData.length() % 34;
      if (subPkgRemainder != 0) {
        subPkgSize++;
      }
//      System.out.println("subPkgSize = " + subPkgSize);

      for (int i = 0; i < subPkgSize; i++) {

        SubBlePkg subBlePkg = new SubBlePkg();
        String head;
        if (i == 0) {
          head = MULTI_PKG_FLAG_START;
        } else if (i == subPkgSize - 1) {
          head = MULTI_PKG_FLAG_END;
        } else {
          head = MULTI_PKG_FLAG_MIDDLE;
        }
        subBlePkg.setHead(head);
        subBlePkg.setIndex(i);
        String offset;

        int leng = i == 0 ? body.getLeng() : i;
        if (leng <= 15) {
          offset = "000" + Integer.toHexString(leng);
        } else if (leng > 15 && leng <= 255) {
          offset = "00" + Integer.toHexString(leng);
        } else if (leng > 255 && leng <= 4095) {
          offset = "0" + Integer.toHexString(leng);
        } else {
          offset = Integer.toHexString(leng);
        }
        subBlePkg.setOffset(offset);

        if (i == subPkgSize - 1) {
          String lastPayload = encryptData.substring(i * 34);
          int zeroFill = 34 - lastPayload.length();
          for (int j = 0; j < zeroFill; j++) {
            lastPayload += "0";
          }
          subBlePkg.setSubPayload(lastPayload);
        } else {
          subBlePkg.setSubPayload(encryptData.substring(i * 34, (i + 1) * 34));
        }
        subBlePkgs.add(subBlePkg);
      }

      if (subBlePkgs.size() == 1) {
        SubBlePkg subBlePkg = new SubBlePkg();
        subBlePkg.setHead("44");
        subBlePkg.setOffset("0001");
        subBlePkg.setIndex(1);
        subBlePkg.setSubPayload("0000000000000000000000000000000000");
        subBlePkgs.add(subBlePkg);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return new ArrayList<>();
    }

    return subBlePkgs;
  }

  private byte[] encrypt(byte[] key, String toEncryptData) {
    if (ENCRYPT_SM4.equals(mEncryptionType)) {
      byte[] bytes = BleHexUtil.hexStringToBytes(toEncryptData);
      byte[] out = new byte[bytes.length];
      SM4Byte sm4Byte = new SM4Byte();
      sm4Byte.sms4(bytes, bytes.length, key, out, 1);
      return out;
    } else if (ENCRYPT_AES.equals(mEncryptionType)) {
      byte[] bytes = BleHexUtil.hexStringToBytes(toEncryptData);
      byte[] out = EncryptUtils.encryptAES(bytes, key);
      return out;
    }
    return null;
  }

  private byte[] decrypt(byte[] key, String encryptData, int encryptPayloadLeng) {

    if (ENCRYPT_SM4.equals(mEncryptionType)) {
      int encryptDataLength = encryptData.length() / 2;
      byte[] out = new byte[encryptDataLength];
      SM4Byte sm4Byte = new SM4Byte();
      sm4Byte.sms4(BleHexUtil.hexStringToBytes(encryptData), encryptDataLength, key, out, 0);
      return out;
    } else if (ENCRYPT_AES.equals(mEncryptionType)) {
      //数据长度截取16整数倍
      int remainder = encryptPayloadLeng % 16;
      int dataLength = 0;
      if (remainder > 0) {
        dataLength = (encryptPayloadLeng / 16 + 1) * 16;
      } else if (remainder == 0) {
        dataLength = encryptPayloadLeng;
      }
//      System.out.println("dataLength = " + dataLength);
      int encryptDataLength = encryptData.length() / 2;
      if (encryptDataLength >= dataLength) {
        encryptData = encryptData.substring(0, dataLength * 2);
      }
//      System.out.println("encryptData = " + encryptData);
//      System.out.println("encryptData.length() = " + encryptData.length());
      byte[] bytes = BleHexUtil.hexStringToBytes(encryptData);
      byte[] out = EncryptUtils.decryptAES(bytes, key);
      return out;
    }
    return null;
  }

  public BlePkg stringValuesToPkg(ArrayList<String> values, String keyRepoId) {
    if (values == null || values.isEmpty()) return new BlePkg();
    clear();
    for (String value : values) {
      byte[] bytes = BleHexUtil.hexStringToBytes(value);
      addSubPkg(bytes);
    }
    return subPkgsToBlePkg(keyRepoId);
  }

  public void clear() {
    mSubBlePkgs.clear();
  }

  public List<SubBlePkg> getSubBlePkgs() {
    return mSubBlePkgs;
  }

  /////////////////////////////////以下为payload转换对象////////////////////////////////////////////////////////////////////////////////

  /**
   * 从pkg中提取key信息
   *
   * @param pkg
   * @return
   */
  public List<LZKey> getKeys(BlePkg pkg) {
    try {
      if (pkg == null || pkg.getBody() == null) return new ArrayList<>();
      ArrayList<LZKey> results = new ArrayList<>();
      BleBody body = pkg.getBody();
      int payloadLength = pkg.getBody().getLeng() - 1;
      int keyNumber = payloadLength / 20;
      String payload = body.getPayload();
      for (int i = 0; i < keyNumber; i++) {
        String keyInfo = payload.substring(i * 40, (i + 1) * 40);
        String keyId = keyInfo.substring(0, 8);
        String key = keyInfo.substring(8);
        LZKey lzKey = new LZKey();
        lzKey.setId(keyId);
        lzKey.setKey(key);
        System.out.println("lzKey = " + lzKey);
        results.add(lzKey);
      }
      return results;
    } catch (Exception e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  public BleGetLockInfoResponse getLockInfo(BlePkg pkg) {
    if (pkg == null || pkg.getBody() == null
        || pkg.getBody().getPayload() == null) return new BleGetLockInfoResponse();

    String payload = pkg.getBody().getPayload();
    System.out.println("payload = " + payload);
    BleGetLockInfoResponse response = new BleGetLockInfoResponse();
    response.setRspCode(payload.substring(0, 2));
    response.setDeviceId(BleHexUtil.hexStr2Str(payload.substring(2, 34).toUpperCase()).trim());
    response.setVersion(BleHexUtil.hexStr2Str(payload.substring(34, 50).toUpperCase()).trim());
    response.setModel(BleHexUtil.hexStr2Str(payload.substring(50, 66).toUpperCase()).trim());
    response.setType(BleHexUtil.hexStr2Str(payload.substring(66, 70).toUpperCase()).trim());
    response.setName(BleHexUtil.hexStr2Str(payload.substring(70, 110).toUpperCase()).trim());
    return response;
  }

  public BleAddLockPwdResponse addLockPwd(BlePkg pkg) {
    if (pkg == null || pkg.getBody() == null
        || pkg.getBody().getPayload() == null) return new BleAddLockPwdResponse();

    String payload = pkg.getBody().getPayload();
    if (TextUtils.isEmpty(payload) || payload.length() < 2) new BleAddLockPwdResponse();
    BleAddLockPwdResponse response = new BleAddLockPwdResponse();
    response.setCode(payload.substring(0, 2));
    String pwdId = payload.substring(2);
    int pwdIdInt;
    try {
      pwdIdInt = Integer.parseInt(pwdId, 16);
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return response;
    }
    response.setPwdId(String.valueOf(pwdIdInt));
    return response;
  }

  public BleAddLockFpResponse addLockFp(BlePkg pkg) {
    if (pkg == null || pkg.getBody() == null
        || pkg.getBody().getPayload() == null) return new BleAddLockFpResponse();

    String payload = pkg.getBody().getPayload();
    if (TextUtils.isEmpty(payload) || payload.length() < 2) return new BleAddLockFpResponse();
    BleAddLockFpResponse response = new BleAddLockFpResponse();
    response.setCode(payload.substring(0, 2));
    String fpId = payload.substring(2);
    int fpIdInt;
    try {
      fpIdInt = Integer.parseInt(fpId, 16);
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return response;
    }
    response.setFpId(String.valueOf(fpIdInt));
    return response;
  }

  public BleAddLockFpResponse addLockDoorCard(BlePkg pkg) {
    return addLockFp(pkg);
  }

  public BleGetRemoteLockStatusResponse getRemoteLockStatus(BlePkg pkg) {
    BleGetRemoteLockStatusResponse statusResponse = new BleGetRemoteLockStatusResponse();
    statusResponse.setCode("01");
    if (pkg == null || pkg.getBody() == null
        || pkg.getBody().getPayload() == null) return statusResponse;

    String payload = pkg.getBody().getPayload();
    if (payload.length() != 10) return statusResponse;

    BleGetRemoteLockStatusResponse response = new BleGetRemoteLockStatusResponse();
    response.setCode(payload.substring(0, 2));
    response.setLock("01".equals(payload.substring(2, 4)));
    response.setBle("01".equals(payload.substring(4, 6)));
    response.setZigBee("01".equals(payload.substring(6, 8)));
    response.setBattery("01".equals(payload.substring(8)));
    return response;
  }

  public BleCheckLockAdminPwdResponse checkLockAdminPwd(BlePkg pkg) {
    if (pkg == null || pkg.getBody() == null
        || TextUtils.isEmpty(pkg.getBody().getPayload())) return null;

    if (pkg.getBody().getPayload().length() < 4) return null;

    String payload = pkg.getBody().getPayload();
    BleCheckLockAdminPwdResponse response = new BleCheckLockAdminPwdResponse();
    response.setRspCode(payload.substring(0, 2));
    response.setClearFactoryResetFlag(payload.substring(2, 4));
    return response;
  }

  public String payload(BlePkg pkg) {
    if (pkg == null || pkg.getBody() == null
        || TextUtils.isEmpty(pkg.getBody().getPayload())) return null;
    String payload = pkg.getBody().getPayload();
    return payload.length() < 2 ? null : payload;
  }

  public String unbindUser(BlePkg pkg) {
    return payload(pkg);
  }

  //00 08050801020200030602000800050405040304050402050601030204010706000101070404040403090106050406d5b2c52b7264a1ddd4b45a1f8a3b
  public String[] getTempPwds(String payload) {
    String tempPwds = payload.substring(2);
    String[] result = new String[10];
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      String tempPwd = tempPwds.substring(i * 12, (i + 1) * 12);
      sb.delete(0, sb.length());
      byte[] bytes = BleHexUtil.hexStringToBytes(tempPwd);
      for (byte aByte : bytes) {
        sb.append(aByte);
      }
      result[i] = sb.toString();
    }

//    System.out.println("result = " + Arrays.toString(result));

    return result;

  }

  public String pkgToReqString(BlePkg pkg, String keyRepoId) {
    List<SubBlePkg> subBlePkgs = blePkgToSubPkgs(pkg, keyRepoId);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < subBlePkgs.size(); i++) {
      SubBlePkg subPkg = subBlePkgs.get(i);
      byte[] bytes = subPkg.toBytes();
      String hex = BleHexUtil.encodeHexStr(bytes);
      sb.append(hex);
      if (i != subBlePkgs.size() - 1) sb.append(",");
    }
    return sb.toString();

  }

  public void setReuseKey(LZKey reuseKey) {
    mReuseKey = reuseKey;
  }

  public void setKeyByteOfLockPin(byte[] keyByteOfLockPin) {
    mKeyByteOfLockPin = keyByteOfLockPin;
  }

  public static void setEncryptionType(String encryptiontype) {
    BleMapper.mEncryptionType = encryptiontype;
  }
}
