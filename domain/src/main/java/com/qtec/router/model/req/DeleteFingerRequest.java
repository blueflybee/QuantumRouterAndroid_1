package com.qtec.router.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   : 删除指纹
 *     version: 1.0
 * </pre>
 */
public class DeleteFingerRequest {
    private String usrid;
    private String devid;
    private String fingerprintid;

    public String getUsrid() {
        return usrid;
    }

    public void setUsrid(String usrid) {
        this.usrid = usrid;
    }

    public String getDevid() {
        return devid;
    }

    public void setDevid(String devid) {
        this.devid = devid;
    }

    public String getFingerprintid() {
        return fingerprintid;
    }

    public void setFingerprintid(String fingerprintid) {
        this.fingerprintid = fingerprintid;
    }

    @Override
    public String toString() {
        return "DeleteFingerResponse{" +
            "usrid='" + usrid + '\'' +
            ", devid='" + devid + '\'' +
            ", fingerprintid='" + fingerprintid + '\'' +
            '}';
    }
}
