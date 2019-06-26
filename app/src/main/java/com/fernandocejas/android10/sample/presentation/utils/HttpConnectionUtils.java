package com.fernandocejas.android10.sample.presentation.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2018/08/14
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class HttpConnectionUtils {
  //设置请求方式
  public static String json_url_is_online="http://xwthird.cloudsee.net/ThirdService/device/deviceOnlineN?companyName=jzlz&deviceGuidArr=C1035023,C200558849&sig=fab0fd5b2a5f4cbbc481733851bbfbde&type=6543210";
  public static String json_url_version="http://xwthird.cloudsee.net/custom/ThirdService/device/deviceVersion?companyName=jzlz&deviceType=JZLZ-C3H&sig=6ed4a3e58a6e95e8fbd3381954d46721";
  //判断网络是否连接
  public static boolean Netisavilable(Context context){
    //获得网络管理
    ConnectivityManager cManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    //获得网络详情
    NetworkInfo networkInfo=cManager.getActiveNetworkInfo();
    if(networkInfo==null||!networkInfo.isAvailable()){
      return false;
    }
    return true;

  }

  public static String check_j(String url){
    String str="";
    StringBuffer sb=new StringBuffer(url);
    try {
      //创建url
      URL myurl=new URL(sb.toString());
      HttpURLConnection urlConnection=(HttpURLConnection) myurl.openConnection();
      urlConnection.setConnectTimeout(5000);
      urlConnection.setReadTimeout(5000);
      if(urlConnection.getResponseCode()==200){
        BufferedReader br=new BufferedReader
            (new InputStreamReader
                (urlConnection.getInputStream(),"utf-8"));


        str=br.readLine();
      }

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return str;
  }
}
