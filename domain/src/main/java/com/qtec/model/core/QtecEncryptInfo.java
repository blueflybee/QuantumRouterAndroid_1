package com.qtec.model.core;

import com.fernandocejas.android10.sample.domain.params.IRequest;

/**
 * Created by l2h on 17-6-6.
 */
public class QtecEncryptInfo<T> implements IRequest {

    private String token;//请求token
    private String method;//请求方法
    private T data;//请求参数
    private String requestUrl;//请求路径

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
