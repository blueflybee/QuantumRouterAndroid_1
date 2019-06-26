package com.qtec.router.model.rsp;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class QueryFingerInfoResponse<T> {

    private T fingerprintlist;

    public T getFingerprintlist() {
        return fingerprintlist;
    }

    @Override
    public String toString() {
        return "QueryFingerInfoResponse{" +
            "fingerprintlist=" + fingerprintlist +
            '}';
    }

    public void setFingerprintlist(T fingerprintlist) {
        this.fingerprintlist = fingerprintlist;
    }

    public static class FingerInfo implements Serializable {
        private String fpid;
        private String fpname;

        public String getFpid() {
            return fpid;
        }

        public void setFpid(String fpid) {
            this.fpid = fpid;
        }

        public String getFpname() {
            return fpname;
        }

        public void setFpname(String fpname) {
            this.fpname = fpname;
        }

        @Override
        public String toString() {
            return "FingerInfo{" +
                "fpid='" + fpid + '\'' +
                ", fpname='" + fpname + '\'' +
                '}';
        }
    }

}
