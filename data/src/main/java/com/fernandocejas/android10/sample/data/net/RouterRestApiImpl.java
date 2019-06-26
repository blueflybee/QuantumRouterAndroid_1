/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.android10.sample.data.net;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.fernandocejas.android10.sample.data.constant.ExceptionConstant;
import com.fernandocejas.android10.sample.data.exception.NetworkConnectionException;
import com.fernandocejas.android10.sample.data.utils.IConverter;
import com.fernandocejas.android10.sample.data.utils.RouterConverter;
import com.fernandocejas.android10.sample.domain.mapper.JsonMapper;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.qtec.mapp.model.rsp.UnbindIntelDevResponse;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecResult;
import com.qtec.router.model.req.SetDHCPRequest;
import com.qtec.router.model.req.SetPPPOERequest;
import com.qtec.router.model.req.SetStaticIPRequest;
import com.qtec.router.model.rsp.AddFingerResponse;
import com.qtec.router.model.rsp.AddIntelDevVerifyResponse;
import com.qtec.router.model.rsp.AddRouterVerifyResponse;
import com.qtec.router.model.rsp.AddVpnResponse;
import com.qtec.router.model.rsp.AllowAuthDeviceResponse;
import com.qtec.router.model.rsp.BindRouterToLockResponse;
import com.qtec.router.model.rsp.CheckAdminPwdResponse;
import com.qtec.router.model.rsp.CheckFirmwareResponse;
import com.qtec.router.model.rsp.ChildCareListResponse;
import com.qtec.router.model.rsp.ConfigWifiResponse;
import com.qtec.router.model.rsp.DeleteFingerResponse;
import com.qtec.router.model.rsp.DeleteVpnResponse;
import com.qtec.router.model.rsp.DeleteWifiSwitchResponse;
import com.qtec.router.model.rsp.EnableAntiFritNetResponse;
import com.qtec.router.model.rsp.FactoryResetResponse;
import com.qtec.router.model.rsp.FirstConfigResponse;
import com.qtec.router.model.rsp.FirstGetKeyResponse;
import com.qtec.router.model.rsp.GetAntiFritNetStatusResponse;
import com.qtec.router.model.rsp.GetAntiFritQuestionResponse;
import com.qtec.router.model.rsp.GetAntiFritSwitchResponse;
import com.qtec.router.model.rsp.GetBandSpeedResponse;
import com.qtec.router.model.rsp.GetBlackListResponse;
import com.qtec.router.model.rsp.GetConnectedWifiResponse;
import com.qtec.router.model.rsp.GetFirmwareUpdateStatusResponse;
import com.qtec.router.model.rsp.GetGuestWifiSwitchResponse;
import com.qtec.router.model.rsp.GetKeyResponse;
import com.qtec.router.model.rsp.GetKeyResponse.KeyBean;
import com.qtec.router.model.rsp.GetLockStatusResponse;
import com.qtec.router.model.rsp.GetNetModeResponse;
import com.qtec.router.model.rsp.GetQosInfoResponse;
import com.qtec.router.model.rsp.GetRouterFirstConfigResponse;
import com.qtec.router.model.rsp.GetRouterInfoResponse;
import com.qtec.router.model.rsp.GetSambaAccountResponse;
import com.qtec.router.model.rsp.GetSignalRegulationResponse;
import com.qtec.router.model.rsp.GetSpecialAttentionResponse;
import com.qtec.router.model.rsp.GetTimerTaskResponse;
import com.qtec.router.model.rsp.GetVpnListResponse;
import com.qtec.router.model.rsp.GetWaitingAuthDeviceListResponse;
import com.qtec.router.model.rsp.GetWifiConfigResponse;
import com.qtec.router.model.rsp.GetWifiDetailResponse;
import com.qtec.router.model.rsp.GetWifiTimeConfigResponse;
import com.qtec.router.model.rsp.GetWirelessListResponse;
import com.qtec.router.model.rsp.ModifyFingerNameResponse;
import com.qtec.router.model.rsp.ModifyVpnResponse;
import com.qtec.router.model.rsp.OpenBandSpeedResponse;
import com.qtec.router.model.rsp.PostChildCareDetailResponse;
import com.qtec.router.model.rsp.PostConnectWirelessResponse;
import com.qtec.router.model.rsp.PostInspectionResponse;
import com.qtec.router.model.rsp.PostQosInfoResponse;
import com.qtec.router.model.rsp.PostSignalRegulationResponse;
import com.qtec.router.model.rsp.PostSpecialAttentionResponse;
import com.qtec.router.model.rsp.QueryBindRouterToLockResponse;
import com.qtec.router.model.rsp.QueryDiskStateResponse;
import com.qtec.router.model.rsp.QueryFingerInfoResponse;
import com.qtec.router.model.rsp.RemoveBlackMemResponse;
import com.qtec.router.model.rsp.RestartRouterResponse;
import com.qtec.router.model.rsp.RouterStatusResponse;
import com.qtec.router.model.rsp.RouterStatusResponse.Status;
import com.qtec.router.model.rsp.SearchIntelDevNotifyResponse;
import com.qtec.router.model.rsp.SearchRouterResponse;
import com.qtec.router.model.rsp.SetAdminPwdResponse;
import com.qtec.router.model.rsp.SetAntiNetQuestionResponse;
import com.qtec.router.model.rsp.SetDHCPResponse;
import com.qtec.router.model.rsp.SetGuestWifiSwitchResponse;
import com.qtec.router.model.rsp.SetPPPOEResponse;
import com.qtec.router.model.rsp.SetRouterTimerResponse;
import com.qtec.router.model.rsp.SetStaticIPResponse;
import com.qtec.router.model.rsp.SetVpnResponse;
import com.qtec.router.model.rsp.SetWifiAllSwitchResponse;
import com.qtec.router.model.rsp.SetWifiDataResponse;
import com.qtec.router.model.rsp.SetWifiSwitchResponse;
import com.qtec.router.model.rsp.UnbindRouterToLockResponse;
import com.qtec.router.model.rsp.UpdateFirmwareResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

/**
 * {@link CloudRestApi} implementation for retrieving data from the network.
 */
@Singleton
public class RouterRestApiImpl implements RouterRestApi {

  public static final String KEY_INVALID_ROUTER_ID = "key_invalid_router_id";
  private static IPostConnection apiPostConnection;
  private final Context context;

  private IConverter mRouterConverter;

  /**
   * Constructor of the class
   *
   * @param context {@link Context}.
   */
  @Inject
  public RouterRestApiImpl(Context context) {

    if (context == null) {
      throw new IllegalArgumentException("The constructor parameters cannot be null!!!");
    }
    this.mRouterConverter = new RouterConverter();
    this.context = context;

  }

  public static IPostConnection getApiPostConnection() {
    return apiPostConnection;
  }

  public static void setApiPostConnection(IPostConnection apiPostConnection) {
    RouterRestApiImpl.apiPostConnection = apiPostConnection;
  }


  private Object requestService(IRequest request, Type type, Subscriber<?> subscriber, int encryption) {

//    ((QtecEncryptInfo) request).setToken(CloudUrlPath.getToken());
    Object data = null;
    if (apiPostConnection.getClass() != MockApiPostConnection.class) {
      checkNetworkConnection(subscriber);
    }

    JsonMapper jsonMapper = new JsonMapper();

    String requestMsg = null;

    try {

      requestMsg = jsonMapper.toJson(request);

    } catch (JsonIOException e) {
      e.printStackTrace();
      subscriber.onError(new JsonIOException("请求数据不完整"));
    }

    Logger.t("router-none-request").json(requestMsg);

    String encryptReq = mRouterConverter.convertTo(requestMsg, encryption);
    Logger.t("router-sm4-request").json(encryptReq);


    String responseJson = null;
    try {
      responseJson = apiPostConnection.requestSyncCall(encryptReq, "", (QtecEncryptInfo) request);
      System.out.println("raw responseJson = " + responseJson);
    } catch (IOException e) {
      e.printStackTrace();
      subscriber.onError(e);
    }

    Logger.t("router-sm4-response").json(responseJson);

    String decryptResult = mRouterConverter.convertFrom(responseJson, ((QtecEncryptInfo) request).getRequestUrl());
    if (IConverter.EXP_ACCESS_DEVICE_NOT_CONNECTED.equals(decryptResult)) {
      subscriber.onError(new IOException("未链接当前网关的wifi或当前网关服务异常，尝试转发"));
    }

    if (!TextUtils.isEmpty(decryptResult) && decryptResult.contains(IConverter.EXP_KEY_INVALID)) {
      Intent intent = new Intent();
      if (decryptResult.contains(":")) {
        String[] result = decryptResult.split(":");
        intent.putExtra(KEY_INVALID_ROUTER_ID, result[1]);
      }
      intent.setAction(CloudRestApiImpl.ACTION_ROUTER_KEY_INVALID);
      context.sendBroadcast(intent);
    }

    Logger.t("router-none-response").json(decryptResult);

    QtecResult qtecResult = null;
    try {
      qtecResult = (QtecResult) jsonMapper.fromJson(decryptResult, type);
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
      Logger.t("router-response").d(decryptResult);
      subscriber.onError(new IOException("返回非法数据格式"));
    }

    if (qtecResult == null) {
      subscriber.onError(new IOException("数据异常"));
    }

    if (qtecResult.getCode() != 0) {
      String msg = TextUtils.isEmpty(qtecResult.getMsg()) ? "业务异常" : ExceptionConstant.convertCodeToMsg(qtecResult.getCode(), qtecResult.getMsg());
      subscriber.onError(new IOException(msg));
    }

    data = qtecResult.getData();
    if (data == null) {
      subscriber.onError(new IOException("data数据不完整"));
    }

    return data;
  }



  private void checkNetworkConnection(Subscriber<?> subscriber) {
    if (!isThereInternetConnection()) {
      subscriber.onError(new NetworkConnectionException());
    }
  }

  /**
   * Checks if the device has any active internet connection.
   *
   * @return true device with internet connection, otherwise false.
   */
  private boolean isThereInternetConnection() {
    boolean isConnected;

    ConnectivityManager connectivityManager =
        (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

    return isConnected;
  }

  @Override
  public Observable<RouterStatusResponse<List<Status>>> getRouterStatus(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<RouterStatusResponse<List<Status>>>() {
      @Override
      public void call(Subscriber<? super RouterStatusResponse<List<Status>>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_ROUTER_STATUS);
        Type type = new TypeToken<QtecResult<RouterStatusResponse<List<Status>>>>() {
        }.getType();
        RouterStatusResponse<List<Status>> response = (RouterStatusResponse<List<Status>>) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<GetLockStatusResponse> getLockStatus(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetLockStatusResponse>() {
      @Override
      public void call(Subscriber<? super GetLockStatusResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_LOCK_STATUS);
        Type type = new TypeToken<QtecResult<GetLockStatusResponse>>() {
        }.getType();
        GetLockStatusResponse response = (GetLockStatusResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<GetAntiFritNetStatusResponse> getAntiFritNetStatus(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetAntiFritNetStatusResponse>() {
      @Override
      public void call(Subscriber<? super GetAntiFritNetStatusResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_ANTI_FRIT_NET);
        Type type = new TypeToken<QtecResult<GetAntiFritNetStatusResponse>>() {
        }.getType();
        GetAntiFritNetStatusResponse response = (GetAntiFritNetStatusResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<EnableAntiFritNetResponse> enableAntiFritNet(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<EnableAntiFritNetResponse>() {
      @Override
      public void call(Subscriber<? super EnableAntiFritNetResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_ENABLE_ANTI_FRIT_NET);
        Type type = new TypeToken<QtecResult<EnableAntiFritNetResponse>>() {
        }.getType();
        EnableAntiFritNetResponse response = (EnableAntiFritNetResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<SetAntiNetQuestionResponse> setAntiQuestion(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<SetAntiNetQuestionResponse>() {
      @Override
      public void call(Subscriber<? super SetAntiNetQuestionResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_SET_ANTI_QUESTION);
        Type type = new TypeToken<QtecResult<SetAntiNetQuestionResponse>>() {
        }.getType();
        SetAntiNetQuestionResponse response = (SetAntiNetQuestionResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<AllowAuthDeviceResponse> allowAuthDevice(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<AllowAuthDeviceResponse>() {
      @Override
      public void call(Subscriber<? super AllowAuthDeviceResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_ALLOW_AUTH_DEVICE);
        Type type = new TypeToken<QtecResult<AllowAuthDeviceResponse>>() {
        }.getType();
        AllowAuthDeviceResponse response = (AllowAuthDeviceResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<GetAntiFritSwitchResponse> getAntiFritSwitch(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetAntiFritSwitchResponse>() {
      @Override
      public void call(Subscriber<? super GetAntiFritSwitchResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_ANTI_FRIT_SWITCH);
        Type type = new TypeToken<QtecResult<GetAntiFritSwitchResponse>>() {
        }.getType();
        GetAntiFritSwitchResponse response = (GetAntiFritSwitchResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<PostChildCareDetailResponse> postChildCareDetail(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<PostChildCareDetailResponse>() {
      @Override
      public void call(Subscriber<? super PostChildCareDetailResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_CHILD_CARE_LIST);
        Type type = new TypeToken<QtecResult<PostChildCareDetailResponse>>() {
        }.getType();
        PostChildCareDetailResponse response = (PostChildCareDetailResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<OpenBandSpeedResponse> openBandSpeedMeasure(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<OpenBandSpeedResponse>() {
      @Override
      public void call(Subscriber<? super OpenBandSpeedResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_BAND_SPEED);
        Type type = new TypeToken<QtecResult<OpenBandSpeedResponse>>() {
        }.getType();
        OpenBandSpeedResponse response = (OpenBandSpeedResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<GetGuestWifiSwitchResponse> getGuestSwitch(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetGuestWifiSwitchResponse>() {
      @Override
      public void call(Subscriber<? super GetGuestWifiSwitchResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_GUEST_SWITCH);
        Type type = new TypeToken<QtecResult<GetGuestWifiSwitchResponse>>() {
        }.getType();
        GetGuestWifiSwitchResponse response = (GetGuestWifiSwitchResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<SetGuestWifiSwitchResponse> setGuestSwitch(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<SetGuestWifiSwitchResponse>() {
      @Override
      public void call(Subscriber<? super SetGuestWifiSwitchResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_SET_GUEST_SWITCH);
        Type type = new TypeToken<QtecResult<SetGuestWifiSwitchResponse>>() {
        }.getType();
        SetGuestWifiSwitchResponse response = (SetGuestWifiSwitchResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<SetWifiAllSwitchResponse> setWifiAllSwitch(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<SetWifiAllSwitchResponse>() {
      @Override
      public void call(Subscriber<? super SetWifiAllSwitchResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_SET_WIFI_ALL_SWITCH);
        Type type = new TypeToken<QtecResult<SetWifiAllSwitchResponse>>() {
        }.getType();
        SetWifiAllSwitchResponse response = (SetWifiAllSwitchResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<DeleteWifiSwitchResponse> deleteWifiSwitch(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<DeleteWifiSwitchResponse>() {
      @Override
      public void call(Subscriber<? super DeleteWifiSwitchResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_DELETE_WIFI_SWITCH);
        Type type = new TypeToken<QtecResult<DeleteWifiSwitchResponse>>() {
        }.getType();
        DeleteWifiSwitchResponse response = (DeleteWifiSwitchResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<SetWifiDataResponse> setWiFiData(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<SetWifiDataResponse>() {
      @Override
      public void call(Subscriber<? super SetWifiDataResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_SET_WIFI_DATA);
        Type type = new TypeToken<QtecResult<SetWifiDataResponse>>() {
        }.getType();
        SetWifiDataResponse response = (SetWifiDataResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<AddVpnResponse> addVpn(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<AddVpnResponse>() {
      @Override
      public void call(Subscriber<? super AddVpnResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_ADD_VPN);
        Type type = new TypeToken<QtecResult<AddVpnResponse>>() {
        }.getType();
        AddVpnResponse response = (AddVpnResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<DeleteVpnResponse> deleteVpn(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<DeleteVpnResponse>() {
      @Override
      public void call(Subscriber<? super DeleteVpnResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_DELETE_VPN);
        Type type = new TypeToken<QtecResult<DeleteVpnResponse>>() {
        }.getType();
        DeleteVpnResponse response = (DeleteVpnResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<ModifyVpnResponse> modifyVpn(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<ModifyVpnResponse>() {
      @Override
      public void call(Subscriber<? super ModifyVpnResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_MODIFY_VPN);
        Type type = new TypeToken<QtecResult<ModifyVpnResponse>>() {
        }.getType();
        ModifyVpnResponse response = (ModifyVpnResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<SetVpnResponse> setVpn(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<SetVpnResponse>() {
      @Override
      public void call(Subscriber<? super SetVpnResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_SET_VPN);
        Type type = new TypeToken<QtecResult<SetVpnResponse>>() {
        }.getType();
        SetVpnResponse response = (SetVpnResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>> getVpnList(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>>() {
      @Override
      public void call(Subscriber<? super GetVpnListResponse<List<GetVpnListResponse.VpnBean>>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_VPN_LIST);
        Type type = new TypeToken<QtecResult<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>>>() {
        }.getType();
        GetVpnListResponse<List<GetVpnListResponse.VpnBean>> response = (GetVpnListResponse<List<GetVpnListResponse.VpnBean>>) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>> getWirelssList(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>>() {
      @Override
      public void call(Subscriber<? super GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_WIRELESS_LIST);
        Type type = new TypeToken<QtecResult<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>>>() {
        }.getType();
        GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>> response = (GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<GetConnectedWifiResponse> getConnectedWifi(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetConnectedWifiResponse>() {
      @Override
      public void call(Subscriber<? super GetConnectedWifiResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_CONNECTED_WIFI);
        Type type = new TypeToken<QtecResult<GetConnectedWifiResponse>>() {
        }.getType();
        GetConnectedWifiResponse response = (GetConnectedWifiResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<SetWifiSwitchResponse> setWifiSwitch(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<SetWifiSwitchResponse>() {
      @Override
      public void call(Subscriber<? super SetWifiSwitchResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_SET_WIFI_SWITCH);
        Type type = new TypeToken<QtecResult<SetWifiSwitchResponse>>() {
        }.getType();
        SetWifiSwitchResponse response = (SetWifiSwitchResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<PostInspectionResponse> safeInspection(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<PostInspectionResponse>() {
      @Override
      public void call(Subscriber<? super PostInspectionResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_POST_SAFE_INSPECTION);
        Type type = new TypeToken<QtecResult<PostInspectionResponse>>() {
        }.getType();
        PostInspectionResponse response = (PostInspectionResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<List<GetBlackListResponse>> getBlackList(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<GetBlackListResponse>>() {
      @Override
      public void call(Subscriber<? super List<GetBlackListResponse>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_BLACK_LIST);
        Type type = new TypeToken<QtecResult<List<GetBlackListResponse>>>() {
        }.getType();
        List<GetBlackListResponse> response = (List<GetBlackListResponse>) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<RemoveBlackMemResponse> removeBlackMem(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<RemoveBlackMemResponse>() {
      @Override
      public void call(Subscriber<? super RemoveBlackMemResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_REMOVE_BLACK_MEM);
        Type type = new TypeToken<QtecResult<RemoveBlackMemResponse>>() {
        }.getType();
        RemoveBlackMemResponse response = (RemoveBlackMemResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<GetAntiFritQuestionResponse> getAntiFritQuestion(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetAntiFritQuestionResponse>() {
      @Override
      public void call(Subscriber<? super GetAntiFritQuestionResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_ANTI_FRIT_QUESTION);
        Type type = new TypeToken<QtecResult<GetAntiFritQuestionResponse>>() {
        }.getType();
        GetAntiFritQuestionResponse response = (GetAntiFritQuestionResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<PostConnectWirelessResponse> connectWireless(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<PostConnectWirelessResponse>() {
      @Override
      public void call(Subscriber<? super PostConnectWirelessResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_POST_CONNECT_WIRELESS);
        Type type = new TypeToken<QtecResult<PostConnectWirelessResponse>>() {
        }.getType();
        PostConnectWirelessResponse response = (PostConnectWirelessResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<GetWifiDetailResponse> getWifiDetail(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetWifiDetailResponse>() {
      @Override
      public void call(Subscriber<? super GetWifiDetailResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_WIFI_DETAIL);
        Type type = new TypeToken<QtecResult<GetWifiDetailResponse>>() {
        }.getType();
        GetWifiDetailResponse response = (GetWifiDetailResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<PostSpecialAttentionResponse> postSpecialAttention(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<PostSpecialAttentionResponse>() {
      @Override
      public void call(Subscriber<? super PostSpecialAttentionResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_POST_SPECIAL_ATTENTION);
        Type type = new TypeToken<QtecResult<PostSpecialAttentionResponse>>() {
        }.getType();
        PostSpecialAttentionResponse response = (PostSpecialAttentionResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<GetSpecialAttentionResponse> getSpecialAttention(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetSpecialAttentionResponse>() {
      @Override
      public void call(Subscriber<? super GetSpecialAttentionResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_SPECIAL_ATTENTION);
        Type type = new TypeToken<QtecResult<GetSpecialAttentionResponse>>() {
        }.getType();
        GetSpecialAttentionResponse response = (GetSpecialAttentionResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<GetQosInfoResponse> getQosInfo(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetQosInfoResponse>() {
      @Override
      public void call(Subscriber<? super GetQosInfoResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_QOS_INFO);
        Type type = new TypeToken<QtecResult<GetQosInfoResponse>>() {
        }.getType();
        GetQosInfoResponse response = (GetQosInfoResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<PostQosInfoResponse> postQosInfo(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<PostQosInfoResponse>() {
      @Override
      public void call(Subscriber<? super PostQosInfoResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_QOS_INFO);
        Type type = new TypeToken<QtecResult<PostQosInfoResponse>>() {
        }.getType();
        PostQosInfoResponse response = (PostQosInfoResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>> getWifiTimeConfig(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>>() {
      @Override
      public void call(Subscriber<? super GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_WIFI_TIME_CONFIG);
        Type type = new TypeToken<QtecResult<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>>>() {
        }.getType();
        GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>> response = (GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<GetSignalRegulationResponse> getSignalMode(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetSignalRegulationResponse>() {
      @Override
      public void call(Subscriber<? super GetSignalRegulationResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_SIGNAL_MODE);
        Type type = new TypeToken<QtecResult<GetSignalRegulationResponse>>() {
        }.getType();
        GetSignalRegulationResponse response = (GetSignalRegulationResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<List<GetWaitingAuthDeviceListResponse>> getWaitingAuthList(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<GetWaitingAuthDeviceListResponse>>() {
      @Override
      public void call(Subscriber<? super List<GetWaitingAuthDeviceListResponse>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_WAITING_LIST);
        Type type = new TypeToken<QtecResult<List<GetWaitingAuthDeviceListResponse>>>() {
        }.getType();
        List<GetWaitingAuthDeviceListResponse> response = (List<GetWaitingAuthDeviceListResponse>) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<List<ChildCareListResponse>> getChildCareList(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<ChildCareListResponse>>() {
      @Override
      public void call(Subscriber<? super List<ChildCareListResponse>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_CHILD_CARE_LIST);
        Type type = new TypeToken<QtecResult<List<ChildCareListResponse>>>() {
        }.getType();
        List<ChildCareListResponse> response = (List<ChildCareListResponse>) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<AddFingerResponse> addFingerInfo(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<AddFingerResponse>() {
      @Override
      public void call(Subscriber<? super AddFingerResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_ADD_FINGER);
        Type type = new TypeToken<QtecResult<AddFingerResponse>>() {
        }.getType();
        AddFingerResponse response = (AddFingerResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<ModifyFingerNameResponse> modifyFingerName(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<ModifyFingerNameResponse>() {
      @Override
      public void call(Subscriber<? super ModifyFingerNameResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_MODIFY_FINGER_NAME);
        Type type = new TypeToken<QtecResult<ModifyFingerNameResponse>>() {
        }.getType();
        ModifyFingerNameResponse response = (ModifyFingerNameResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<DeleteFingerResponse> deleteFingerInfo(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<DeleteFingerResponse>() {
      @Override
      public void call(Subscriber<? super DeleteFingerResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_DELETE_FINGER);
        Type type = new TypeToken<QtecResult<DeleteFingerResponse>>() {
        }.getType();
        DeleteFingerResponse response = (DeleteFingerResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> queryFingerInfo(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>>() {
      @Override
      public void call(Subscriber<? super QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_QUERY_FINGER_INFO);
        Type type = new TypeToken<QtecResult<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>>>() {
        }.getType();
        QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>> response =
            (QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }

      }
    });
  }

  @Override
  public Observable<SearchRouterResponse> searchRouter(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<SearchRouterResponse>() {
      @Override
      public void call(Subscriber<? super SearchRouterResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_SEARCH_ROUTER);
        Type type = new TypeToken<QtecResult<SearchRouterResponse>>() {
        }.getType();
        SearchRouterResponse response = (SearchRouterResponse) requestService(encryptInfo, type, subscriber, 0);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<AddRouterVerifyResponse> addRouterVerify(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<AddRouterVerifyResponse>() {
      @Override
      public void call(Subscriber<? super AddRouterVerifyResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_ADD_ROUTER_VERIFY);
        Type type = new TypeToken<QtecResult<AddRouterVerifyResponse>>() {
        }.getType();
        AddRouterVerifyResponse response = (AddRouterVerifyResponse) requestService(encryptInfo, type, subscriber, 0);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<AddIntelDevVerifyResponse> addIntelDevVerify(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<AddIntelDevVerifyResponse>() {
      @Override
      public void call(Subscriber<? super AddIntelDevVerifyResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_ADD_INTEL_DEV_VERIFY);
        Type type = new TypeToken<QtecResult<AddIntelDevVerifyResponse>>() {
        }.getType();
        AddIntelDevVerifyResponse response = (AddIntelDevVerifyResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<UnbindIntelDevResponse> unbind(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<UnbindIntelDevResponse>() {
      @Override
      public void call(Subscriber<? super UnbindIntelDevResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_UNBIND_INTEL_DEV);
        Type type = new TypeToken<QtecResult<UnbindIntelDevResponse>>() {
        }.getType();
        UnbindIntelDevResponse response = (UnbindIntelDevResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<FirstGetKeyResponse> firstGetKey(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<FirstGetKeyResponse>() {
      @Override
      public void call(Subscriber<? super FirstGetKeyResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_FIRST_GET_KEY);
        Type type = new TypeToken<QtecResult<FirstGetKeyResponse>>() {
        }.getType();
        FirstGetKeyResponse response = (FirstGetKeyResponse) requestService(encryptInfo, type, subscriber, 0);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<SearchIntelDevNotifyResponse> searchIntelDevNotify(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<SearchIntelDevNotifyResponse>() {
      @Override
      public void call(Subscriber<? super SearchIntelDevNotifyResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_SEARCH_INTEL_DEV_NOTIFY);
        Type type = new TypeToken<QtecResult<SearchIntelDevNotifyResponse>>() {
        }.getType();
        SearchIntelDevNotifyResponse response = (SearchIntelDevNotifyResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<GetRouterInfoResponse> getRouterInfo(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetRouterInfoResponse>() {
      @Override
      public void call(Subscriber<? super GetRouterInfoResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_ROUTER_INFO);
        Type type = new TypeToken<QtecResult<GetRouterInfoResponse>>() {
        }.getType();
        GetRouterInfoResponse response = (GetRouterInfoResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<SetDHCPResponse> setDHCP(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<SetDHCPResponse>() {
      @Override
      public void call(Subscriber<? super SetDHCPResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        int configed = ((SetDHCPRequest) encryptInfo.getData()).getConfiged();
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_SET_DHCP);
        Type type = new TypeToken<QtecResult<SetDHCPResponse>>() {
        }.getType();
        SetDHCPResponse response = (SetDHCPResponse) requestService(encryptInfo, type, subscriber, configed);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<SetPPPOEResponse> setPPPOE(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<SetPPPOEResponse>() {
      @Override
      public void call(Subscriber<? super SetPPPOEResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        int configed = ((SetPPPOERequest) encryptInfo.getData()).getConfiged();
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_SET_PPPOE);
        Type type = new TypeToken<QtecResult<SetPPPOEResponse>>() {
        }.getType();
        SetPPPOEResponse response = (SetPPPOEResponse) requestService(encryptInfo, type, subscriber, configed);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<SetStaticIPResponse> setStaticIP(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<SetStaticIPResponse>() {
      @Override
      public void call(Subscriber<? super SetStaticIPResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        int configed = ((SetStaticIPRequest) encryptInfo.getData()).getConfiged();
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_SET_STATIC_IP);
        Type type = new TypeToken<QtecResult<SetStaticIPResponse>>() {
        }.getType();
        SetStaticIPResponse response = (SetStaticIPResponse) requestService(encryptInfo, type, subscriber, configed);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<GetNetModeResponse> getNetMode(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetNetModeResponse>() {
      @Override
      public void call(Subscriber<? super GetNetModeResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_NET_MODE);
        Type type = new TypeToken<QtecResult<GetNetModeResponse>>() {
        }.getType();
        GetNetModeResponse response = (GetNetModeResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<RestartRouterResponse> restartRouter(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<RestartRouterResponse>() {
      @Override
      public void call(Subscriber<? super RestartRouterResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_RESTART_ROUTER);
        Type type = new TypeToken<QtecResult<RestartRouterResponse>>() {
        }.getType();
        RestartRouterResponse response = (RestartRouterResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<FactoryResetResponse> factoryReset(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<FactoryResetResponse>() {
      @Override
      public void call(Subscriber<? super FactoryResetResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_FACTORY_RESET);
        Type type = new TypeToken<QtecResult<FactoryResetResponse>>() {
        }.getType();
        FactoryResetResponse response = (FactoryResetResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<UpdateFirmwareResponse> updateFirmware(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<UpdateFirmwareResponse>() {
      @Override
      public void call(Subscriber<? super UpdateFirmwareResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_UPDATE_FIRMWARE);
        Type type = new TypeToken<QtecResult<UpdateFirmwareResponse>>() {
        }.getType();
        UpdateFirmwareResponse response = (UpdateFirmwareResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<GetWifiConfigResponse> getWifiConfig(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetWifiConfigResponse>() {
      @Override
      public void call(Subscriber<? super GetWifiConfigResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_WIFI_CONFIG);
        Type type = new TypeToken<QtecResult<GetWifiConfigResponse>>() {
        }.getType();
        GetWifiConfigResponse response = (GetWifiConfigResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<ConfigWifiResponse> configWifi(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<ConfigWifiResponse>() {
      @Override
      public void call(Subscriber<? super ConfigWifiResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_CONFIG_WIFI);
        Type type = new TypeToken<QtecResult<ConfigWifiResponse>>() {
        }.getType();
        ConfigWifiResponse response = (ConfigWifiResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<GetTimerTaskResponse> getTimerTask(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetTimerTaskResponse>() {
      @Override
      public void call(Subscriber<? super GetTimerTaskResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_TIMER_TASK);
        Type type = new TypeToken<QtecResult<GetTimerTaskResponse>>() {
        }.getType();
        GetTimerTaskResponse response = (GetTimerTaskResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<SetRouterTimerResponse> setRouterTimer(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<SetRouterTimerResponse>() {
      @Override
      public void call(Subscriber<? super SetRouterTimerResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_SET_ROUTER_TIMER);
        Type type = new TypeToken<QtecResult<SetRouterTimerResponse>>() {
        }.getType();
        SetRouterTimerResponse response = (SetRouterTimerResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<CheckAdminPwdResponse> checkAdminPwd(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<CheckAdminPwdResponse>() {
      @Override
      public void call(Subscriber<? super CheckAdminPwdResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_CHECK_ADMIN_PWD);
        Type type = new TypeToken<QtecResult<CheckAdminPwdResponse>>() {
        }.getType();
        CheckAdminPwdResponse response = (CheckAdminPwdResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<SetAdminPwdResponse> setAdminPwd(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<SetAdminPwdResponse>() {
      @Override
      public void call(Subscriber<? super SetAdminPwdResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_SET_ADMIN_PWD);
        Type type = new TypeToken<QtecResult<SetAdminPwdResponse>>() {
        }.getType();
        SetAdminPwdResponse response = (SetAdminPwdResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<GetSambaAccountResponse> getSambaAccount(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetSambaAccountResponse>() {
      @Override
      public void call(Subscriber<? super GetSambaAccountResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_SAM_PWD);
        Type type = new TypeToken<QtecResult<GetSambaAccountResponse>>() {
        }.getType();
        GetSambaAccountResponse response = (GetSambaAccountResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<QueryDiskStateResponse> queryDiskState(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<QueryDiskStateResponse>() {
      @Override
      public void call(Subscriber<? super QueryDiskStateResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_QUERY_DISK_STATE);
        Type type = new TypeToken<QtecResult<QueryDiskStateResponse>>() {
        }.getType();
        QueryDiskStateResponse response = (QueryDiskStateResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<QueryDiskStateResponse> formatDisk(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<QueryDiskStateResponse>() {
      @Override
      public void call(Subscriber<? super QueryDiskStateResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_QUERY_DISK_STATE);
        Type type = new TypeToken<QtecResult<QueryDiskStateResponse>>() {
        }.getType();
        QueryDiskStateResponse response = (QueryDiskStateResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<PostSignalRegulationResponse> setSignalRegulationInfo(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<PostSignalRegulationResponse>() {
      @Override
      public void call(Subscriber<? super PostSignalRegulationResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_SET_SIGNAL_REGULATION);
        Type type = new TypeToken<QtecResult<PostSignalRegulationResponse>>() {
        }.getType();
        PostSignalRegulationResponse response = (PostSignalRegulationResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<GetBandSpeedResponse> getBandSpeed(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetBandSpeedResponse>() {
      @Override
      public void call(Subscriber<? super GetBandSpeedResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_BAND_SPEED);
        Type type = new TypeToken<QtecResult<GetBandSpeedResponse>>() {
        }.getType();
        GetBandSpeedResponse response = (GetBandSpeedResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<GetKeyResponse<List<KeyBean>>> getKey(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetKeyResponse<List<KeyBean>>>() {
      @Override
      public void call(Subscriber<? super GetKeyResponse<List<KeyBean>>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_KEY);
        Type type = new TypeToken<QtecResult<GetKeyResponse<List<KeyBean>>>>() {
        }.getType();
        GetKeyResponse<List<KeyBean>> response = (GetKeyResponse<List<KeyBean>>) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });
  }

  @Override
  public Observable<CheckFirmwareResponse> checkFirmware(final IRequest request) {

    return Observable.create(new Observable.OnSubscribe<CheckFirmwareResponse>() {
      @Override
      public void call(Subscriber<? super CheckFirmwareResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_CHECK_FIRMWARE);
        Type type = new TypeToken<QtecResult<CheckFirmwareResponse>>() {
        }.getType();
        CheckFirmwareResponse response = (CheckFirmwareResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });

  }

  @Override
  public Observable<GetFirmwareUpdateStatusResponse> getFirmwareUpdateStatus(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetFirmwareUpdateStatusResponse>() {
      @Override
      public void call(Subscriber<? super GetFirmwareUpdateStatusResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_FIRMWARE_UPDATE_STATUS);
        Type type = new TypeToken<QtecResult<GetFirmwareUpdateStatusResponse>>() {
        }.getType();
        GetFirmwareUpdateStatusResponse response = (GetFirmwareUpdateStatusResponse) requestService(encryptInfo, type, subscriber, 1);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });

  }

  @Override
  public Observable<FirstConfigResponse> firstConfig(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<FirstConfigResponse>() {
      @Override
      public void call(Subscriber<? super FirstConfigResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_FIRST_CONFIG);
        Type type = new TypeToken<QtecResult<FirstConfigResponse>>() {
        }.getType();
        FirstConfigResponse response = (FirstConfigResponse) requestService(encryptInfo, type, subscriber, 0);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });

  }

  @Override
  public Observable<BindRouterToLockResponse> bindRouterToLock(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<BindRouterToLockResponse>() {
      @Override
      public void call(Subscriber<? super BindRouterToLockResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_BIND_ROUTER_TO_LOCK);
        Type type = new TypeToken<QtecResult<BindRouterToLockResponse>>() {
        }.getType();
        BindRouterToLockResponse response = (BindRouterToLockResponse) requestService(encryptInfo, type, subscriber, 0);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });

  }

  @Override
  public Observable<QueryBindRouterToLockResponse> queryBindRouterToLock(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<QueryBindRouterToLockResponse>() {
      @Override
      public void call(Subscriber<? super QueryBindRouterToLockResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_QUERY_BIND_ROUTER_TO_LOCK);
        Type type = new TypeToken<QtecResult<QueryBindRouterToLockResponse>>() {
        }.getType();
        QueryBindRouterToLockResponse response = (QueryBindRouterToLockResponse) requestService(encryptInfo, type, subscriber, 0);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });

  }

  @Override
  public Observable<UnbindRouterToLockResponse> unbindRouterToLock(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<UnbindRouterToLockResponse>() {
      @Override
      public void call(Subscriber<? super UnbindRouterToLockResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_UNBIND_ROUTER_TO_LOCK);
        Type type = new TypeToken<QtecResult<UnbindRouterToLockResponse>>() {
        }.getType();
        UnbindRouterToLockResponse response = (UnbindRouterToLockResponse) requestService(encryptInfo, type, subscriber, 0);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });

  }

  @Override
  public Observable<GetRouterFirstConfigResponse> getFirstConfig(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetRouterFirstConfigResponse>() {
      @Override
      public void call(Subscriber<? super GetRouterFirstConfigResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setRequestUrl(RouterUrlPath.PATH_GET_ROUTER_FIRST_CONFIG);
        Type type = new TypeToken<QtecResult<GetRouterFirstConfigResponse>>() {
        }.getType();
        GetRouterFirstConfigResponse response = (GetRouterFirstConfigResponse) requestService(encryptInfo, type, subscriber, 0);
        if (response != null) {
          subscriber.onNext(response);
        }
      }
    });

  }
}
