package com.qtec.mapp.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetExtraNetPortRequest {

    private String deviceSerialNo;
    private String userUniqueKey;

    public String getDeviceSerialNo() {
        return deviceSerialNo;
    }

    public void setDeviceSerialNo(String deviceSerialNo) {
        this.deviceSerialNo = deviceSerialNo;
    }

    public String getUserUniqueKey() {
        return userUniqueKey;
    }

    public void setUserUniqueKey(String userUniqueKey) {
        this.userUniqueKey = userUniqueKey;
    }

    @Override
    public String toString() {
        return "GetExtraNetPortRequest{" +
            "deviceSerialNo='" + deviceSerialNo + '\'' +
            ", userUniqueKey='" + userUniqueKey + '\'' +
            '}';
    }
}
