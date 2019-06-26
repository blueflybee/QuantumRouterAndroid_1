package com.qtec.mapp.model.req;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 设备数量网络请求
 *      version: 1.0
 * </pre>
 */

public class GetDeviceCountRequest {
    private String userUniqueKey;

    public void setUserUniqueKey(String userUniqueKey) {
        this.userUniqueKey = userUniqueKey;
    }

    public String getUserUniqueKey() {
        return userUniqueKey;
    }

    @Override
    public String toString() {
        return "GetDeviceCountRequest{" +
                "userUniqueKey='" + userUniqueKey + '\'' +
                '}';
    }
}
