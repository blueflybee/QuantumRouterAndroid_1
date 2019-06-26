package com.qtec.router.model.rsp;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetWaitingAuthDeviceListResponse implements Serializable{

    private String dev_name;
    private int dev_type;
    private String dev_ip;
    private String dev_mac;

    public String getDev_name() {
        return dev_name;
    }

    public void setDev_name(String dev_name) {
        this.dev_name = dev_name;
    }

    public int getDev_type() {
        return dev_type;
    }

    public void setDev_type(int dev_type) {
        this.dev_type = dev_type;
    }

    public String getDev_ip() {
        return dev_ip;
    }

    public void setDev_ip(String dev_ip) {
        this.dev_ip = dev_ip;
    }

    public String getDev_mac() {
        return dev_mac;
    }

    public void setDev_mac(String dev_mac) {
        this.dev_mac = dev_mac;
    }

    @Override
    public String toString() {
        return "GetWaitingAuthDeviceListResponse{" +
            "dev_name='" + dev_name + '\'' +
            ", dev_type='" + dev_type + '\'' +
            ", dev_ip='" + dev_ip + '\'' +
            ", dev_mac='" + dev_mac + '\'' +
            '}';
    }
}
