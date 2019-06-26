package com.fernandocejas.android10.sample.data.net;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ConnectTest {

    public static final String KEY_ENCRYPT_INFO = "encryptInfo";
    public static final String KEY_DECRYPT_INFO = "decryptInfo";
    private static OkHttpClient okHttpClient;
    private URL url;
    private String response;
    private String sessionId = "";

    public static void main(String args[]) throws IOException {
        System.out.println("hello world   +++++++++++++111");
        try {
            OkHttpClient okHttpClient = createClient();


            /**
             * 创建请求的参数body
             */
            FormBody.Builder builder = new FormBody.Builder();
//            builder.add(KEY_ENCRYPT_INFO, requestMsg);
            builder.add(KEY_DECRYPT_INFO, "dd");
            RequestBody body = builder.build();

            Request request = new Request.Builder()
//                    .url(CloudRestApi.Liu_Li_Hua)
                    .post(body)
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String rsp = response.body().string();
            } else {
                throw new IOException("无法连接到服务器，请检查网络连接");
                //throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("网络有点卡...");
        }


    }

    private static OkHttpClient createClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            okHttpClient = builder
                    .readTimeout(20000, TimeUnit.MILLISECONDS)
                    .writeTimeout(20000, TimeUnit.MILLISECONDS)
                    .connectTimeout(15000, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(true)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    }).build();
        }
        return okHttpClient;
    }
}
