package com.fernandocejas.android10.sample.presentation.view.device.cateye.test;

import android.util.Log;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 播放设置相关基本方法，云视通协议和流媒体协议均需要使用
 * Created by juyang on 17/6/23.
 */

public class CommonUtil {


    /**
     * 获取手机IP地址
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("IpAddress", ex.toString());
        }
        return "";
    }

    /**
     * 获取设备的云视通组
     *
     * @param deviceNum
     */
    public static String getGroup(String deviceNum) {

        StringBuffer groupSB = new StringBuffer();
        if (!"".equalsIgnoreCase(deviceNum)) {
            for (int i = 0; i < deviceNum.length(); i++) {
                if (Character.isLetter(deviceNum.charAt(i))) { // 用char包装类中的判断字母的方法判断每一个字符
                    groupSB = groupSB.append(deviceNum.charAt(i));
                }
            }
        }
        return groupSB.toString();
    }

    /**
     * 获取设备的云视通组和号码
     *
     * @param deviceNum
     */
    public static int getYST(String deviceNum) {
        int yst = 0;

        StringBuffer ystSB = new StringBuffer();
        if (!"".equalsIgnoreCase(deviceNum)) {
            for (int i = 0; i < deviceNum.length(); i++) {
                if (Character.isDigit(deviceNum.charAt(i))) {
                    ystSB = ystSB.append(deviceNum.charAt(i));
                }
            }
        }

        if ("".equalsIgnoreCase(ystSB.toString())) {
            yst = 0;
        } else {
            yst = Integer.parseInt(ystSB.toString());
        }
        return yst;
    }

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(AppConsts.FORMATTER_DATE_AND_TIME);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String time = formatter.format(curDate);
        return time;
    }


    /**
     * 递归创建文件目录
     *
     * @param filePath 要创建的目录路径
     * @author
     */
    public static void createDirectory(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return;
        }
        File parentFile = file.getParentFile();

        if (null != file && parentFile.exists()) {
            if (parentFile.isDirectory()) {
            } else {
                parentFile.delete();
                boolean res = parentFile.mkdir();
                if (!res) {
                    parentFile.delete();
                }
            }

            boolean res = file.mkdir();
            if (!res) {
                file.delete();
            }

        } else {
            createDirectory(file.getParentFile().getAbsolutePath());
            boolean res = file.mkdir();
            if (!res) {
                file.delete();
            }
        }
    }

    /**
     * 根据key 获取 String value
     *
     * @param map
     * @param key
     * @return
     */
    public static String genStringValueByKey(HashMap<String, String> map, String key) {
        String value = "";
        if (null == map || "".equalsIgnoreCase(key)) {
            return null;
        }
        if (null != map.get(key) && !"".equalsIgnoreCase(map.get(key))) {
            value = map.get(key);
        }
        return value;
    }

    /**
     * 根据key 获取 Int value
     *
     * @param map
     * @param key
     * @return
     */
    public static int genIntValueByKey(HashMap<String, String> map, String key) {
        int value = -1;
        if (null == map || "".equalsIgnoreCase(key)) {
            return -1;
        }

        if (!map.containsKey(key)) {
            return -1;
        }

        if (null != map.get(key) && !"".equalsIgnoreCase(map.get(key))) {
            value = Integer.parseInt(map.get(key));
        }
        return value;
    }


    /**
     * 特定 json 转 HashMap
     *
     * @param msg
     * @return
     */
    public static HashMap<String, String> genMsgMap(String msg) {
        HashMap<String, String> map = new HashMap<String, String>();

        if (null == msg || "".equalsIgnoreCase(msg)) {
            return null;
        }
        Matcher matcher = Pattern.compile("([^=;]+)=([^=;]+)").matcher(msg);
        while (matcher.find()) {
            map.put(matcher.group(1), matcher.group(2));
        }
        return map;
    }

    /**
     * 特定 json 转 HashMap 不会覆盖
     *
     * @param msg
     * @return
     */
    public static HashMap<String, String> genMsgMap1(String msg) {
        HashMap<String, String> map = new HashMap<String, String>();

        if (null == msg || "".equalsIgnoreCase(msg)) {
            return null;
        }
        Matcher matcher = Pattern.compile("([^=;]+)=([^=;]+)").matcher(msg);
        while (matcher.find()) {
            if (null != map.get(matcher.group(1))
                    && !"".equalsIgnoreCase(matcher.group(1))) {

            } else {
                map.put(matcher.group(1), matcher.group(2));
            }

        }
        return map;
    }
}
