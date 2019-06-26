package com.qtec.model.core;

import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.qtec.mapp.model.req.TransmitRequest;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/07/03
 *     desc   : 多端并发请求，封装多个request
 *     version: 1.0
 * </pre>
 */
public class QtecMultiEncryptInfo implements IRequest{

    private QtecEncryptInfo routerEncryptInfo;
    private QtecEncryptInfo<TransmitRequest> cloudEncryptInfo;
    private Boolean isRouterDirectConnect = false;

    public QtecEncryptInfo getRouterEncryptInfo() {
        return routerEncryptInfo;
    }

    public void setRouterEncryptInfo(QtecEncryptInfo routerEncryptInfo) {
        this.routerEncryptInfo = routerEncryptInfo;
    }

    public QtecEncryptInfo<TransmitRequest> getCloudEncryptInfo() {
        return cloudEncryptInfo;
    }

    public void setCloudEncryptInfo(QtecEncryptInfo<TransmitRequest> cloudEncryptInfo) {
        this.cloudEncryptInfo = cloudEncryptInfo;
    }

    public Boolean getRouterDirectConnect() {
        return isRouterDirectConnect;
    }

    public void setRouterDirectConnect(Boolean routerDirectConnect) {
        isRouterDirectConnect = routerDirectConnect;
    }
}
