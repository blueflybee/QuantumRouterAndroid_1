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

import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.qtec.model.core.QtecEncryptInfo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Api Connection class used to retrieve data from the cloud.
 * Implements {@link Callable} so when executed asynchronously can
 * return a value.
 */
public class RouterApiConnection implements IPostConnection {

  public static final String KEY_ENCRYPT_INFO = "encryptInfo";
  public static final String KEY_DECRYPT_INFO = "decryptInfo";
  public static final String ROUTER_SERIAL_NUMBER = "router_serial_number";
  private static OkHttpClient okHttpClient;
  private URL url;
  private String response;
  private String sessionId = "";

  protected RouterApiConnection(String url) {
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
    connectToApi(requestMsg, bizCode);
    return response;
  }

  @Override
  public String getUrl() {
    if (url == null) return "";
    return url.toString();
  }

  private void connectToApi(String requestMsg, String bizCode) throws IOException {

    try {
      OkHttpClient okHttpClient = this.createClient();

      Logger.t("request").d(url.toString());

      /**
       * 创建请求的参数body
       */
      RequestBody body = FormBody.create(MediaType.parse("application/json"), requestMsg);

      Request request = new Request.Builder()
          .header(ROUTER_SERIAL_NUMBER, "12345678")
          .url(url)
          .post(body)
          .build();

      Response response = okHttpClient.newCall(request).execute();
      if (response.isSuccessful()) {
        this.response = response.body().string();
      } else {
        throw new IOException("无法连接到服务器，请检查网络连接");
        //throw new IOException("Unexpected code " + response);
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new IOException("网络有点卡...");
    }
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
}
