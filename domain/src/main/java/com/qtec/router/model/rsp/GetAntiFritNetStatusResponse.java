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
public class GetAntiFritNetStatusResponse implements Serializable {
    private int enable;
    private int router_access;
    private int lan_dev_access;

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getRouter_access() {
        return router_access;
    }

    public void setRouter_access(int router_access) {
        this.router_access = router_access;
    }

    public int getLan_dev_access() {
        return lan_dev_access;
    }

    public void setLan_dev_access(int lan_dev_access) {
        this.lan_dev_access = lan_dev_access;
    }

    @Override
    public String toString() {
        return "GetAntiFritNetStatusResponse{" +
            "enable=" + enable +
            ", router_access=" + router_access +
            ", lan_dev_access=" + lan_dev_access +
            '}';
    }
}
