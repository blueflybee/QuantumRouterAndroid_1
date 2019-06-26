package com.qtec.mapp.model.rsp;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   : 设备共享
 *     version: 1.0
 * </pre>
 */
public class GetDevicesListResponse implements Serializable{
    private String router_name;
    private String lock_name;

    public String getRouter_name() {
        return router_name;
    }

    public void setRouter_name(String router_name) {
        this.router_name = router_name;
    }

    public String getLock_name() {
        return lock_name;
    }

    public void setLock_name(String lock_name) {
        this.lock_name = lock_name;
    }

    @Override
    public String toString() {
        return "GetShareMemListResponse{" +
                "router_name='" + router_name + '\'' +
                ", lock_name='" + lock_name + '\'' +
                '}';
    }
}
