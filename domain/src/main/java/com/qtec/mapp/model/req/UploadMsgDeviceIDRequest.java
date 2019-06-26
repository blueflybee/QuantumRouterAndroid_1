package com.qtec.mapp.model.req;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 上传设备中心消息ID
 *      version: 1.0
 * </pre>
 */

public class UploadMsgDeviceIDRequest {
    private String deviceSerialNo;
    private String userPhone;
    private String platform;

    public String getDeviceSerialNo() {
        return deviceSerialNo;
    }

    public void setDeviceSerialNo(String deviceSerialNo) {
        this.deviceSerialNo = deviceSerialNo;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "UploadMsgDeviceIDRequest{" +
            "deviceSerialNo='" + deviceSerialNo + '\'' +
            ", userPhone='" + userPhone + '\'' +
            ", platform='" + platform + '\'' +
            '}';
    }
}
