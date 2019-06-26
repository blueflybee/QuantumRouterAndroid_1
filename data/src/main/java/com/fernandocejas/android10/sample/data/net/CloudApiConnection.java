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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.qtec.mapp.model.req.TransmitRequest;
import com.qtec.model.core.QtecEncryptInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Api Connection class used to retrieve data from the cloud.
 * Implements {@link Callable} so when executed asynchronously can
 * return a value.
 */
public class CloudApiConnection implements IPostConnection {

  public static final String KEY_ENCRYPT_INFO = "encryptInfo";
  public static final String KEY_DECRYPT_INFO = "decryptInfo";
  public static final String JSON_KEY_DATA = "data";
  public static final String KEY_TOKEN = "token";
  private static OkHttpClient okHttpClient;
  private URL url;
  private String response;
  private String sessionId = "";


  private static Gson sGson = new GsonBuilder()
      .registerTypeAdapter(
          new TypeToken<Map<String, Object>>(){}.getType(),
          new JsonDeserializer<Map<String, Object>>() {
            @Override
            public Map<String, Object> deserialize(
                JsonElement json, Type typeOfT,
                JsonDeserializationContext context) throws JsonParseException {

              Map<String, Object> map = new HashMap<>();
              JsonObject jsonObject = json.getAsJsonObject();
              Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
              for (Map.Entry<String, JsonElement> entry : entrySet) {
                Object ot = entry.getValue();
                if(ot instanceof JsonPrimitive){
                  map.put(entry.getKey(), ((JsonPrimitive) ot).getAsString());
                }else{
                  map.put(entry.getKey(), ot);
                }
              }
              return map;
            }
          }).create();

  protected CloudApiConnection(String url) {
    try {
      this.url = new URL(url);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getSessionId() {
    return sessionId;
  }

  @Override
  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  /**
   * Do a request to an api synchronously.
   * It should not be executed in the main thread of the application.
   *
   * @return A string response
   */
  @Override
  @Nullable
  public String requestSyncCall(String requestMsg, String bizCode, QtecEncryptInfo encryptInfo) throws IOException {
    connectToApi(requestMsg, bizCode, encryptInfo);
    return response;
  }

  private void connectToApi(String requestMsg, String bizCode, QtecEncryptInfo encryptInfo) throws IOException {

    try {
      OkHttpClient okHttpClient = this.createClient();


      if (!CloudUrlPath.PATH_UPLOAD_LOGCAT.equals(bizCode)) {
        Logger.t("cloud-url").d(url.toString());
      }

      String requestUrl = this.url + encryptInfo.getRequestUrl();
      String method = encryptInfo.getMethod();

      System.out.println("request class url = " + requestUrl);

      checkDataClass(encryptInfo);

      Request.Builder builder = new Request.Builder();
      String token = encryptInfo.getToken();
      if (!TextUtils.isEmpty(token)) {
        builder.header(KEY_TOKEN, token);
      }

      Request request;
      if (CloudUrlPath.METHOD_POST.equals(method)) {
        request = builder
            .url(requestUrl)
            .post(createPostRequestBody(encryptInfo, requestMsg))
            .build();
      } else {
        String getRequestStr = createGetRequestStr(encryptInfo);
        System.out.println("getRequestStr = " + getRequestStr);
        request = builder
            .url(requestUrl + getRequestStr)
            .build();
      }

      Response response = okHttpClient.newCall(request).execute();
      if (response.isSuccessful()) {
        checkDataClassSuccess(encryptInfo);
        this.response = response.body().string();
      } else {
        throw new IOException("无法连接到服务器，请检查网络连接");
        //throw new IOException("Unexpected code " + response);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IOException("网络有点卡...");
    }
  }

  private void checkDataClassSuccess(QtecEncryptInfo encryptInfo) {
    Object data = encryptInfo.getData();
    if (data != null) {
      System.out.println("request class success is " + data.getClass().getName());
    } else {
      System.out.println("request class success is null++" + encryptInfo.getRequestUrl());
    }
    System.out.println("request class---------------------------------------------------------");
  }

  private void checkDataClass(QtecEncryptInfo encryptInfo) {
    Object data = encryptInfo.getData();
    if (data != null) {
      System.out.println("request class is " + data.getClass().getName());
    } else {
      System.out.println("request class is null++" + encryptInfo.getRequestUrl());
    }
  }


  private String createGetRequestStr(QtecEncryptInfo encryptInfo) {
    Object data = encryptInfo.getData();
    if (data == null) return "";
    String json = new Gson().toJson(data);
    if (TextUtils.isEmpty(json)) return "";
    StringBuilder result = new StringBuilder();
    result.append("?");
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    Gson gson = new Gson();
    Map<String, Object> dataMap = gson.fromJson(json, type);
    Set<String> keys = dataMap.keySet();
    for (String key : keys) {
      Object value = dataMap.get(key);
      String getStr;
      if (value instanceof String) {
        getStr = value.toString();
      } else {
        getStr = gson.toJson(value);
      }
      result.append(key).append("=").append(getStr).append("&");
    }
    return result.toString();
  }

  /**
   * 创建请求的参数body
   *
   * @param encryptInfo
   * @param requestMsg
   */
  @NonNull
  private RequestBody createPostRequestBody(QtecEncryptInfo encryptInfo, String requestMsg) throws JSONException {
    Object data = encryptInfo.getData();
    if (data == null) return new FormBody.Builder().build();

//    Gson gson = new Gson();
    String json = data instanceof TransmitRequest ? new JSONObject(requestMsg).getString(JSON_KEY_DATA) : sGson.toJson(data);
    System.out.println("json = " + json);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    Map<String, Object> dataMap = sGson.fromJson(json, type);
    System.out.println("dataMap = " + dataMap);
    FormBody.Builder builder = new FormBody.Builder();
    Set<String> keys = dataMap.keySet();
    for (String key : keys) {
      Object value = dataMap.get(key);
      if (value instanceof String) {
        builder.add(key, value.toString());
      } else {
        builder.add(key, sGson.toJson(value));
      }
    }
    return builder.build();
  }

  private OkHttpClient createClient() {
    if (okHttpClient == null) {
      OkHttpClient.Builder builder = new OkHttpClient.Builder();
      okHttpClient = builder
          .readTimeout(20000, TimeUnit.MILLISECONDS)
          .writeTimeout(20000, TimeUnit.MILLISECONDS)
          .connectTimeout(10000, TimeUnit.MILLISECONDS)
          .retryOnConnectionFailure(true)
          .sslSocketFactory(getSSLContext().getSocketFactory())
          .hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
              return true;
            }
          }).build();
    }
    return okHttpClient;
  }

  private SSLContext getSSLContext() {
    try {
      SSLContext sc = SSLContext.getInstance("TLSv1.2");
      sc.init(null, new TrustManager[]{new X509TrustManager() {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
          return new X509Certificate[0];
        }

      }}, new SecureRandom());

      return sc;

    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    } catch (KeyManagementException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public String getUrl() {
    if (url == null) {
      return "";
    }
    return url.toString();
  }

}
