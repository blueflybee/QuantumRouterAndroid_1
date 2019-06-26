package com.qtec.mapp.model.req;

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
public class GetLockListRequest implements Serializable{

    private String userUniqueKey;

    public String getUserUniqueKey() {
        return userUniqueKey;
    }

    public void setUserUniqueKey(String userUniqueKey) {
        this.userUniqueKey = userUniqueKey;
    }

    @Override
    public String toString() {
        return "GetLockListRequest{" +
            "userUniqueKey='" + userUniqueKey + '\'' +
            '}';
    }
}
