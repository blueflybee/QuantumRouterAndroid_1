package com.qtec.mapp.model.req;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 分享 网络请求
 *      version: 1.0
 * </pre>
 */

public class PostInvitationRequest {
    private String deviceSerialNo;
    private String sharePhone;
    private String sharedPhone;

    public String getDeviceSerialNo() {
        return deviceSerialNo;
    }

    public void setDeviceSerialNo(String deviceSerialNo) {
        this.deviceSerialNo = deviceSerialNo;
    }

    public String getSharePhone() {
        return sharePhone;
    }

    public void setSharePhone(String sharePhone) {
        this.sharePhone = sharePhone;
    }

    public String getSharedPhone() {
        return sharedPhone;
    }

    public void setSharedPhone(String sharedPhone) {
        this.sharedPhone = sharedPhone;
    }

    @Override
    public String toString() {
        return "PostInvitationRequest{" +
                "deviceSerialNo='" + deviceSerialNo + '\'' +
                ", sharePhone='" + sharePhone + '\'' +
                ", sharedPhone='" + sharedPhone + '\'' +
                '}';
    }
}
