package com.qtec.mapp.model.req;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 共享成员网络请求
 *      version: 1.0
 * </pre>
 */

public class GetSharedMemListRequest {
    private String userUniqueKey;
    private String routerSerialNo;

    public String getUserUniqueKey() {
        return userUniqueKey;
    }

    public void setUserUniqueKey(String userUniqueKey) {
        this.userUniqueKey = userUniqueKey;
    }

    public String getRouterSerialNo() {
        return routerSerialNo;
    }

    public void setRouterSerialNo(String routerSerialNo) {
        this.routerSerialNo = routerSerialNo;
    }

    @Override
    public String toString() {
        return "GetSharedMemListRequest{" +
                "userUniqueKey='" + userUniqueKey + '\'' +
                ", routerSerialNo='" + routerSerialNo + '\'' +
                '}';
    }
}
