package com.fernandocejas.android10.sample.presentation.utils;

import android.text.TextUtils;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.qtec.mapp.model.req.TransmitRequest;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class EncryptInfoFactory {

  public static QtecMultiEncryptInfo createMultiEncryptInfo(Object routerData, String path, String method) {
    QtecEncryptInfo routerEncryptInfo = new QtecEncryptInfo<>();
    routerEncryptInfo.setRequestUrl(path);
    routerEncryptInfo.setMethod(method);
    routerEncryptInfo.setData(routerData);

    //build transmit
    TransmitRequest<QtecEncryptInfo> transmit = new TransmitRequest<>();
    transmit.setRouterSerialNo(GlobleConstant.getgDeviceId());
    transmit.setEncryptInfo(routerEncryptInfo);

    //build cloud EncryptInfo
    QtecEncryptInfo<TransmitRequest> cloudEncryptInfo = new QtecEncryptInfo<>();
    cloudEncryptInfo.setData(transmit);

    QtecMultiEncryptInfo multiEncryptInfo = new QtecMultiEncryptInfo();
    multiEncryptInfo.setCloudEncryptInfo(cloudEncryptInfo);
    multiEncryptInfo.setRouterEncryptInfo(routerEncryptInfo);

    //当wifi id和router Id相等则直连，其他情况非直连
    try{
//      System.out.println("唯一ID mul = " + PrefConstant.ROUTER_ID_CONNECTED_DIRECT);
//      System.out.println("唯一ID mul Glo =" + GlobleConstant.getgDeviceId());
      if(TextUtils.isEmpty(GlobleConstant.getgDeviceId())){
        multiEncryptInfo.setRouterDirectConnect(false);
        System.out.println("链路测试：中转网络");
      }else {
        if((PrefConstant.ROUTER_ID_CONNECTED_DIRECT).equals(GlobleConstant.getgDeviceId())){

       /*   if(PrefConstant.IS_ANTI_SWITCH && PrefConstant.IS_ANTI_LIMIT_NET){
            if(PrefConstant.IS_AUTI_DEVICE_AUTHED){
              //认证过
              System.out.println("链路测试：直连网络");
              multiEncryptInfo.setRouterDirectConnect(true);
            }else {
              //未认证过
              System.out.println("链路测试：中转网络");
              multiEncryptInfo.setRouterDirectConnect(false);
            }
          }else {
            System.out.println("链路测试：直连网络");
            multiEncryptInfo.setRouterDirectConnect(true);
          }
*/
          System.out.println("链路测试：直连网络");
          multiEncryptInfo.setRouterDirectConnect(true);
        }else {
          System.out.println("链路测试：中转网络");
          multiEncryptInfo.setRouterDirectConnect(false);
        }
      }

    }catch (Exception e){
      e.printStackTrace();
    }

    return multiEncryptInfo;
  }

  public static QtecMultiEncryptInfo createMultiEncryptInfo(String routerId, Object routerData, String path, String method) {
    QtecEncryptInfo routerEncryptInfo = new QtecEncryptInfo<>();
    routerEncryptInfo.setRequestUrl(path);
    routerEncryptInfo.setMethod(method);
    routerEncryptInfo.setData(routerData);

    //build transmit
    TransmitRequest<QtecEncryptInfo> transmit = new TransmitRequest<>();
    transmit.setRouterSerialNo(routerId);
    transmit.setEncryptInfo(routerEncryptInfo);

    //build cloud EncryptInfo
    QtecEncryptInfo<TransmitRequest> cloudEncryptInfo = new QtecEncryptInfo<>();
    cloudEncryptInfo.setData(transmit);

    QtecMultiEncryptInfo multiEncryptInfo = new QtecMultiEncryptInfo();
    multiEncryptInfo.setCloudEncryptInfo(cloudEncryptInfo);
    multiEncryptInfo.setRouterEncryptInfo(routerEncryptInfo);

    //当wifi id和router Id相等则直连，其他情况非直连
    try{
//      System.out.println("唯一ID mul = " + PrefConstant.ROUTER_ID_CONNECTED_DIRECT);
//      System.out.println("唯一ID mul Glo =" + GlobleConstant.getgDeviceId());
      if(TextUtils.isEmpty(GlobleConstant.getgDeviceId())){
        multiEncryptInfo.setRouterDirectConnect(false);
        System.out.println("链路测试：中转网络");
      }else {
        if((PrefConstant.ROUTER_ID_CONNECTED_DIRECT).equals(GlobleConstant.getgDeviceId())){

        /*  if(PrefConstant.IS_LOCAL_DEVICE_NOT_AUTHED && PrefConstant.IS_ANTI_ROUTER_ACCESS_ENABLED){
            System.out.println("链路测试：中转网络");
            multiEncryptInfo.setRouterDirectConnect(false);
          }else {
            System.out.println("链路测试：直连网络");
            multiEncryptInfo.setRouterDirectConnect(true);
          }*/

       /*   if(PrefConstant.IS_ANTI_SWITCH && PrefConstant.IS_ANTI_LIMIT_NET){
            if(PrefConstant.IS_AUTI_DEVICE_AUTHED){
              //认证过
              System.out.println("链路测试：直连网络");
              multiEncryptInfo.setRouterDirectConnect(true);
            }else {
              //未认证过
              System.out.println("链路测试：中转网络");
              multiEncryptInfo.setRouterDirectConnect(false);
            }
          }else {
            System.out.println("链路测试：直连网络");
            multiEncryptInfo.setRouterDirectConnect(true);
          }*/

          System.out.println("链路测试：直连网络");
          multiEncryptInfo.setRouterDirectConnect(true);

        }else {
          multiEncryptInfo.setRouterDirectConnect(false);
          System.out.println("链路测试：中转网络");
        }
      }
    }catch (Exception e){
      e.printStackTrace();
    }

    return multiEncryptInfo;
  }

  public static QtecEncryptInfo createEncryptInfo(Object request) {
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo<>();
    encryptInfo.setData(request);
    return encryptInfo;
  }

}
