package com.fernandocejas.android10.sample.data.utils;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blueflybee.keystore.KeystoreRepertory;
import com.blueflybee.keystore.data.LZKey;
import com.blueflybee.sm2sm3sm4.library.SM4Utils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.mapper.JsonMapper;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.qtec.model.core.QtecInfo;
import com.qtec.model.core.QtecResult;
import com.qtec.router.model.rsp.FirstGetKeyResponse;
import com.qtec.router.model.rsp.SearchIntelDevNotifyResponse;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/07/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class RouterConverter implements IConverter {

  public static final String SP_NAME = "sp_router";
  public static final String SP_USER_UNIQUE_KEY = "sp_user_unique_key";

  private SM4Utils mSm4Utils;
  private JsonMapper mJsonMapper;


  private LZKey mEncryptKey = new LZKey();
  private LZKey mDecryptKey = new LZKey();


  private LZKey mTestKey = new LZKey();
  private SPUtils mSpUtils = new SPUtils(SP_NAME);

  public RouterConverter() {
    mSm4Utils = new SM4Utils();
    mSm4Utils.setHexString(true);
    mJsonMapper = new JsonMapper();

    mTestKey.setId("1000000000000000");
    mTestKey.setKey("96e79218965eb72c92a549dd5a330112");
  }

  public String convertTo(@NonNull String reqJson, int encryption) {
    try {
      QtecInfo qtecInfo = new QtecInfo();
      qtecInfo.setEncryption(encryption);

      if (encryption == 0) {
        qtecInfo.setReuse(0);
      } else {
        boolean keyTooLow = KeystoreRepertory.getInstance().isKeyTooLow(GlobleConstant.getgKeyRepoId());
        qtecInfo.setReuse(keyTooLow ? 1 : 0);
      }
      qtecInfo.setEncryptinfo(encrypt(reqJson, encryption));

      qtecInfo.setKeyid(encryption == 0 ? "" : mEncryptKey.getId());
      qtecInfo.setUserid(mSpUtils.getString(SP_USER_UNIQUE_KEY));
      qtecInfo.setDeviceid(DeviceUtils.getAndroidID());
      return mJsonMapper.toJson(qtecInfo);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  @Override
  public String convertFrom(String rspJson, String requestUrl) {
    try {
      Type type = new TypeToken<QtecInfo>() {
      }.getType();
      QtecInfo qtecInfo = (QtecInfo) mJsonMapper.fromJson(rspJson, type);

      String connectSerialNumber = qtecInfo.getSerialnumber();
      String accessSerialNumber = GlobleConstant.getgDeviceId();
      String deviceType = GlobleConstant.getgDeviceType();
      System.out.println("deviceType = " + deviceType);
      System.out.println("accessSerialNumber = " + accessSerialNumber);
      System.out.println("connectSerialNumber = " + connectSerialNumber);
      System.out.println("requestUrl = " + requestUrl);
      if ("0".equals(deviceType)
          && !RouterUrlPath.PATH_SEARCH_ROUTER.equals(requestUrl)
          && !accessSerialNumber.equals(GlobleConstant.NO_ID)
          && !accessSerialNumber.equals(connectSerialNumber)) {
        return EXP_ACCESS_DEVICE_NOT_CONNECTED;
      }
      int keyinvalid = qtecInfo.getKeyinvalid();
      if (keyinvalid == 1) {
        return EXP_KEY_INVALID + ":" + qtecInfo.getSerialnumber();
      }
      if (RouterUrlPath.PATH_FIRST_GET_KEY.equals(requestUrl)) {
        String encryptinfo = qtecInfo.getEncryptinfo();
        try {
          mJsonMapper.fromJson(encryptinfo, new TypeToken<QtecResult>() {
          }.getType());
          return decrypt(qtecInfo);
        } catch (JsonSyntaxException e) {
          FirstGetKeyResponse firstGetKeyResponse = new FirstGetKeyResponse();
          firstGetKeyResponse.setEncryptinfo(encryptinfo);
          QtecResult<FirstGetKeyResponse> qtecResult = new QtecResult<>();
          qtecResult.setCode(0);
          qtecResult.setMsg("ok");
          qtecResult.setData(firstGetKeyResponse);
          return mJsonMapper.toJson(qtecResult);
        }

      } else {
        return decrypt(qtecInfo);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }

  }

  @Override
  public String convertToForFake(@NonNull String rspJson, int encryption) {
    try {
      QtecInfo qtecInfo = new QtecInfo();
      qtecInfo.setEncryption(encryption);
      qtecInfo.setEncryptinfo(encryptForFirstGetKey(rspJson, encryption));
      qtecInfo.setKeyid("");
      qtecInfo.setSerialnumber("qtec0000_0");
      qtecInfo.setUserid(mSpUtils.getString(SP_USER_UNIQUE_KEY));
      qtecInfo.setDeviceid(DeviceUtils.getAndroidID());
      return mJsonMapper.toJson(qtecInfo);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  @Override
  public String createEncryptResponse(String resultJson, int encryption) {
    try {
      QtecInfo qtecInfo = new QtecInfo();
      qtecInfo.setEncryption(encryption);
      qtecInfo.setSerialnumber("qtec0000_0");
      qtecInfo.setEncryptinfo(encryptForTestResponse(resultJson, encryption));
      qtecInfo.setKeyid(encryption == 0 ? "" : mTestKey.getId());
//      qtecInfo.setKeyinvalid(encryption == 0 ? 0 : 1);
      qtecInfo.setUserid(mSpUtils.getString(SP_USER_UNIQUE_KEY));
      qtecInfo.setDeviceid(DeviceUtils.getAndroidID());
      return mJsonMapper.toJson(qtecInfo);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  private String encryptForTestResponse(String initTxt, int encryption) {
    if (encryption == 0) return initTxt;
    System.out.println("RouterConverter_encryptForTestResponse: mEncryptKey ++= " + mTestKey.getKey());
    mSm4Utils.setSecretKey(mTestKey.getKey());
    return mSm4Utils.encryptData_ECB(initTxt);
  }

  private String decryptForTestResponse(QtecInfo qtecInfo) {
    if (qtecInfo == null) return "";
    if (qtecInfo.getEncryption() == 0) return qtecInfo.getEncryptinfo();
    System.out.println("RouterConverter: mTestKey ++++= " + mTestKey);
    mSm4Utils.setSecretKey(mTestKey.getKey());
    String decryptJson = mSm4Utils.decryptData_ECB(qtecInfo.getEncryptinfo());
    return AppStringUtil.cutTail(decryptJson);
  }

  @Override
  public String convertFromForTestResponse(String encryptResult, String path) {
    try {
      Type type = new TypeToken<QtecInfo>() {
      }.getType();
      QtecInfo qtecInfo = (QtecInfo) mJsonMapper.fromJson(encryptResult, type);
      String connectSerialNumber = qtecInfo.getSerialnumber();
      String accessSerialNumber = GlobleConstant.getgDeviceId();
      String deviceType = GlobleConstant.getgDeviceType();

      if ("0".equals(deviceType)
          && !RouterUrlPath.PATH_SEARCH_ROUTER.equals(path)
          && !accessSerialNumber.equals(GlobleConstant.NO_ID)
          && !accessSerialNumber.equals(connectSerialNumber)) {
        return EXP_ACCESS_DEVICE_NOT_CONNECTED;
      }

      int keyinvalid = qtecInfo.getKeyinvalid();
      if (keyinvalid == 1) {
        return EXP_KEY_INVALID;
      }
      if (RouterUrlPath.PATH_FIRST_GET_KEY.equals(path)) {
        FirstGetKeyResponse firstGetKeyResponse = new FirstGetKeyResponse();
        firstGetKeyResponse.setEncryptinfo(qtecInfo.getEncryptinfo());
        QtecResult<FirstGetKeyResponse> qtecResult = new QtecResult<>();
        qtecResult.setCode(0);
        qtecResult.setMsg("ok");
        qtecResult.setData(firstGetKeyResponse);
        return mJsonMapper.toJson(qtecResult);
      } else {
        return decryptForTestResponse(qtecInfo);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  private String encrypt(String initTxt, int encryption) {
    if (encryption == 0) return initTxt;
    String keyRepoId = GlobleConstant.getgKeyRepoId();
    System.out.println("keyRepoId = " + keyRepoId);
    mEncryptKey = KeystoreRepertory.getInstance().key(keyRepoId);
    System.out.println("RouterConverter: mEncryptKey ++= " + mEncryptKey);
    mSm4Utils.setSecretKey(mEncryptKey.getKey());
    return mSm4Utils.encryptData_ECB(initTxt);
  }

  private String encryptForFirstGetKey(String initTxt, int encryption) {
    if (encryption == 0) return initTxt;
    System.out.println("RouterConverter_encryptForFirstGetKey: mEncryptKey ++= " + mTestKey.getKey());
    mSm4Utils.setSecretKey(mTestKey.getKey());
    return mSm4Utils.encryptData_ECB(initTxt);
  }


  private String decrypt(QtecInfo qtecInfo) {
    if (qtecInfo == null) return "";
    if (qtecInfo.getEncryption() == 0) return qtecInfo.getEncryptinfo();
    mDecryptKey = KeystoreRepertory.getInstance().key(GlobleConstant.getgKeyRepoId(), qtecInfo.getKeyid());
    System.out.println("RouterConverter: mDecryptKey ++= " + mDecryptKey);
    mSm4Utils.setSecretKey(mDecryptKey.getKey());


    String decryptJson = mSm4Utils.decryptData_ECB(qtecInfo.getEncryptinfo());
    System.out.println("decryptJson = " + decryptJson);

    return AppStringUtil.cutTail(decryptJson);
  }


}
