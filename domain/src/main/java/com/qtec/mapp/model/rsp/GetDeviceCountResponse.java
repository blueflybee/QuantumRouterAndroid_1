package com.qtec.mapp.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetDeviceCountResponse {

    private String routerNum;

    public String getRouterNum() {
        return routerNum;
    }

    public void setRouterNum(String routerNum) {
        this.routerNum = routerNum;
    }

    @Override
    public String toString() {
        return "GetDeviceCountResponse{" +
                "routerNum='" + routerNum + '\'' +
                '}';
    }
}
