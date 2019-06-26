package com.fernandocejas.android10.sample.data.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.blankj.utilcode.util.DeviceUtils;
import com.blueflybee.keystore.KeystoreRepertory;
import com.blueflybee.keystore.data.LZKey;
import com.blueflybee.sm2sm3sm4.library.SM4Utils;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.domain.mapper.JsonMapper;
import com.google.gson.reflect.TypeToken;
import com.qtec.mapp.model.req.TransmitRequest;
import com.qtec.mapp.model.rsp.TransmitResponse;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecResult;

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
public class CloudConverter implements IConverter {

  private SM4Utils mSm4Utils;
  private JsonMapper mJsonMapper;

  private LZKey mEncryptKey = new LZKey();
  private LZKey mDecryptKey = new LZKey();

  private LZKey mTestKey = new LZKey();

  public CloudConverter() {
    mSm4Utils = new SM4Utils();
    mSm4Utils.setHexString(true);
    mJsonMapper = new JsonMapper();

    mTestKey.setId("1000000000000000");
    mTestKey.setKey("96e79218965eb72c92a549dd5a330112");
  }

  @Override
  public String convertTo(@NonNull String reqJson, int encryption) {
    try {
      Type type = new TypeToken<QtecEncryptInfo<TransmitRequest>>() {
      }.getType();
      QtecEncryptInfo<TransmitRequest<Object>> encryptInfo = (QtecEncryptInfo<TransmitRequest<Object>>) mJsonMapper.fromJson(reqJson, type);
      TransmitRequest transmitRequest = encryptInfo.getData();
      Object routerData = transmitRequest.getEncryptInfo();
      String routerDataJson = mJsonMapper.toJson(routerData);
      boolean keyTooLow = KeystoreRepertory.getInstance().isKeyTooLow(GlobleConstant.getgKeyRepoId());
      transmitRequest.setReuse(keyTooLow ? 1 : 0);
      transmitRequest.setEncryptInfo(encrypt(routerDataJson, encryption));
      transmitRequest.setKeyId(mEncryptKey.getId());
      transmitRequest.setEncryption(String.valueOf(encryption));
      transmitRequest.setDeviceId(DeviceUtils.getAndroidID());
//      transmitRequest.setUserId("zxcasdqwe123");
      transmitRequest.setUserId(PrefConstant.getUserUniqueKey("0"));
      encryptInfo.setData(transmitRequest);
      return mJsonMapper.toJson(encryptInfo);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  @Override
  public String convertToForFake(@NonNull String rspJson, int encryption) {
    try {
      Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
      }.getType();
      QtecResult<TransmitResponse<String>> encryptInfo = (QtecResult<TransmitResponse<String>>) mJsonMapper.fromJson(rspJson, type);
      TransmitResponse<String> transmitResponse = encryptInfo.getData();
      String routerData = transmitResponse.getEncryptInfo();
      transmitResponse.setEncryptInfo(encrypt(routerData, encryption));
//      transmitResponse.setKeyInvalid(1);
      transmitResponse.setKeyId(mEncryptKey.getId());
      transmitResponse.setEncryption(String.valueOf(encryption));
      transmitResponse.setDeviceId(DeviceUtils.getAndroidID());
      transmitResponse.setUserId(PrefConstant.getUserUniqueKey("0"));
      encryptInfo.setData(transmitResponse);
      return mJsonMapper.toJson(encryptInfo);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  @Override
  public String convertFrom(String rspJson, String requestUrl) {
    try {
      Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
      }.getType();
      QtecResult<TransmitResponse<String>> qtecInfo = (QtecResult<TransmitResponse<String>>) mJsonMapper.fromJson(rspJson, type);
      TransmitResponse<String> data = qtecInfo.getData();
      if (data == null) return "";
      int keyinvalid = data.getKeyInvalid();
      if (keyinvalid == 1) {
        return EXP_KEY_INVALID;
      }
      return decrypt(qtecInfo);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }

  }

  @Override
  public String createEncryptResponse(String resultJson, int encryption) {
    try {
      Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
      }.getType();
      QtecResult<TransmitResponse<String>> encryptInfo = (QtecResult<TransmitResponse<String>>) mJsonMapper.fromJson(resultJson, type);
      TransmitResponse<String> transmitResponse = encryptInfo.getData();
      String routerData = transmitResponse.getEncryptInfo();
      transmitResponse.setEncryptInfo(encryptForTestResponse(routerData, encryption));
//      transmitResponse.setKeyInvalid(1);
      transmitResponse.setKeyId(mTestKey.getId());
      transmitResponse.setEncryption(String.valueOf(encryption));
      transmitResponse.setDeviceId(DeviceUtils.getAndroidID());
      transmitResponse.setUserId(PrefConstant.getUserUniqueKey("0"));
      encryptInfo.setData(transmitResponse);
      return mJsonMapper.toJson(encryptInfo);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  @Override
  public String convertFromForTestResponse(String encryptResult, String path) {
    try {
      Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
      }.getType();
      QtecResult<TransmitResponse<String>> qtecInfo = (QtecResult<TransmitResponse<String>>) mJsonMapper.fromJson(encryptResult, type);
      int keyinvalid = qtecInfo.getData().getKeyInvalid();
      if (keyinvalid == 1) {
        return EXP_KEY_INVALID;
      }
      return decryptForTestResponse(qtecInfo);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }

  }

  private String decryptForTestResponse(QtecResult<TransmitResponse<String>> qtecEncryptInfo) {
    if (qtecEncryptInfo == null || qtecEncryptInfo.getData() == null) return mJsonMapper.toJson(qtecEncryptInfo);
    String encryption = qtecEncryptInfo.getData().getEncryption();
    if ("0".equals(encryption) || TextUtils.isEmpty(encryption)) return mJsonMapper.toJson(qtecEncryptInfo);
    System.out.println("CloudConverter: mTestKey ++= " + mTestKey);
    mSm4Utils.setSecretKey(mTestKey.getKey());
    String routerData = qtecEncryptInfo.getData().getEncryptInfo();
    String decryptRouterData = mSm4Utils.decryptData_ECB(routerData);
    decryptRouterData = AppStringUtil.cutTail(decryptRouterData);
    qtecEncryptInfo.getData().setEncryptInfo(decryptRouterData);
    return mJsonMapper.toJson(qtecEncryptInfo);
  }

  private String encryptForTestResponse(String initTxt, int encryption) {
    if (encryption == 0) return initTxt;
    System.out.println("CloudConverter: mTestKey ++= " + mTestKey.getKey());
    mSm4Utils.setSecretKey(mTestKey.getKey());
    return mSm4Utils.encryptData_ECB(initTxt);
  }

  private String encrypt(String initTxt, int encryption) {
    if (encryption == 0) return initTxt;
    String keyRepoId = GlobleConstant.getgKeyRepoId();
    System.out.println("keyRepoId = " + keyRepoId);
    mEncryptKey = KeystoreRepertory.getInstance().key(keyRepoId);
    System.out.println("CloudConverter: mEncryptKey ++= " + mEncryptKey);
    mSm4Utils.setSecretKey(mEncryptKey.getKey());
    return mSm4Utils.encryptData_ECB(initTxt);
  }

  private String decrypt(QtecResult<TransmitResponse<String>> qtecEncryptInfo) {
    if (qtecEncryptInfo == null || qtecEncryptInfo.getData() == null) return mJsonMapper.toJson(qtecEncryptInfo);
    String encryption = qtecEncryptInfo.getData().getEncryption();
    if ("0".equals(encryption) || TextUtils.isEmpty(encryption)) return mJsonMapper.toJson(qtecEncryptInfo);
    mDecryptKey = KeystoreRepertory.getInstance().key(GlobleConstant.getgKeyRepoId(), qtecEncryptInfo.getData().getKeyId());
    System.out.println("CloudConverter: mDecryptKey ++= " + mDecryptKey);
    mSm4Utils.setSecretKey(mDecryptKey.getKey());
    String routerData = qtecEncryptInfo.getData().getEncryptInfo();
    String decryptRouterData = mSm4Utils.decryptData_ECB(routerData);
    decryptRouterData = AppStringUtil.cutTail(decryptRouterData);
    qtecEncryptInfo.getData().setEncryptInfo(decryptRouterData);
    return mJsonMapper.toJson(qtecEncryptInfo);
  }


}
