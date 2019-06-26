package com.qtec.router.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   : 修改名称
 *     version: 1.0
 * </pre>
 */
public class ModifyFingerNameRequest {
    private String usrid;
    private String devid;
    private String fingerprintid;
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ModifyFingerNameRequest{" +
            "usrid='" + usrid + '\'' +
            ", devid='" + devid + '\'' +
            ", fingerprintid='" + fingerprintid + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}
