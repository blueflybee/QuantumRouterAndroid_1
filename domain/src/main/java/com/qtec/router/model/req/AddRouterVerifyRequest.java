package com.qtec.router.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AddRouterVerifyRequest {
//    "password": "xxx",
//            "stamac": "xxx",
//            "staname": "xxx",
//            "stasysinfo": "xxx",
//            "username": "xxx"
    private String password;
    private String stamac;
    private String staname;
    private String stasysinfo;
    private String username;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStamac() {
        return stamac;
    }

    public void setStamac(String stamac) {
        this.stamac = stamac;
    }

    public String getStaname() {
        return staname;
    }

    public void setStaname(String staname) {
        this.staname = staname;
    }

    public String getStasysinfo() {
        return stasysinfo;
    }

    public void setStasysinfo(String stasysinfo) {
        this.stasysinfo = stasysinfo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
