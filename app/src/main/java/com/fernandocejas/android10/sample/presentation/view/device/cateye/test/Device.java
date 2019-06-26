package com.fernandocejas.android10.sample.presentation.view.device.cateye.test;

import java.util.ArrayList;

/**
 * 云视通协议：简单的设备集合类
 *
 * @author juyang
 */
public class Device {

    //通道列表
    private ArrayList<Channel> channelList;

    private String ip;
    private int port;

    private String gid;//A
    private int no;//361
    private String fullNo;//A361

    private String user;
    private String pwd;

    //小助手是否起作用
    private boolean isHelperEnabled;

    //网络库返回的类型
//    public static final int DEVICE_TYPE_UNKOWN = -1;
//    public static final int DEVICE_TYPE_DVR = 0x01;
//    public static final int DEVICE_TYPE_950 = 0x02;
//    public static final int DEVICE_TYPE_951 = 0x03;
//    public static final int DEVICE_TYPE_IPC = 0x04;
//    public static final int DEVICE_TYPE_NVR = 0x05;
    private int deviceType;

    //是否带帧头
    private boolean isJFH;

    /**
     * 创建指定通道个数的设备
     *
     * @param ip
     * @param port
     * @param gid
     * @param no
     * @param user
     * @param pwd
     * @param isHomeProduct
     * @param channelCount
     */
    public Device(String ip, int port, String gid, int no, String user,
                  String pwd, boolean isHomeProduct, int channelCount) {
        this.ip = ip;
        this.port = port;
        this.gid = gid;
        this.no = no;
        this.fullNo = gid + no;
        this.user = user;
        this.pwd = pwd;

        isHelperEnabled = false;
        channelList = new ArrayList<Channel>();

        for (int i = 0; i < channelCount; i++) {
            Channel channel = new Channel(this, i, i + 1, false, false, this.fullNo + "_" + (i + 1));
            channelList.add(channel);
        }
    }

    public ArrayList<Channel> getChannelList() {
        return channelList;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getGid() {
        return gid;
    }

    public int getNo() {
        return no;
    }

    public String getFullNo() {
        return fullNo;
    }

    public String getUser() {
        return user;
    }

    public String getPwd() {
        return pwd;
    }

    public boolean isHelperEnabled() {
        return isHelperEnabled;
    }

    public void setHelperEnabled(boolean isHelperEnabled) {
        this.isHelperEnabled = isHelperEnabled;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }


    public boolean isJFH() {
        return isJFH;
    }

    public void setJFH(boolean JFH) {
        isJFH = JFH;
    }

    @Override
    public String toString() {
        StringBuilder sBuilder = new StringBuilder(1024);
        sBuilder.append(fullNo).append("(").append(ip).append(":").append(port)
                .append("): ").append("user = ").append(user)
                .append(", pwd = ").append(pwd).append(", enabled = ")
                .append(isHelperEnabled);
        if (null != channelList) {
            int size = channelList.size();
            for (int i = 0; i < size; i++) {
                sBuilder.append("\n").append(channelList.get(i).toString());
            }
        }
        return sBuilder.toString();
    }
}
