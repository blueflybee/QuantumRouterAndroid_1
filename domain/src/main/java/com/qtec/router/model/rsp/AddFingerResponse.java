package com.qtec.router.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AddFingerResponse {
    private String fpid;

    public String getFpid() {
        return fpid;
    }

    public void setFpid(String fpid) {
        this.fpid = fpid;
    }

    @Override
    public String toString() {
        return "AddFingerResponse{" +
            "fpid='" + fpid + '\'' +
            '}';
    }
}
