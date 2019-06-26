package com.qtec.router.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class QueryFingerInfoRequest {
    private String devid;
    private String usrid;

    public String getDevid() {
        return devid;
    }

    public void setDevid(String devid) {
        this.devid = devid;
    }

    public String getUsrid() {
        return usrid;
    }

    public void setUsrid(String usrid) {
        this.usrid = usrid;
    }

    @Override
    public String toString() {
        return "QueryFingerInfoRequest{" +
            "devid='" + devid + '\'' +
            ", usrid='" + usrid + '\'' +
            '}';
    }
}
