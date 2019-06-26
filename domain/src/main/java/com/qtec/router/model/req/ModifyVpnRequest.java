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
public class ModifyVpnRequest {
  private String description;
  private String ifname;
  private String mode;
  private String server_ip;
  private String username;
  private String password;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getIfname() {
    return ifname;
  }

  public void setIfname(String ifname) {
    this.ifname = ifname;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public String getServer_ip() {
    return server_ip;
  }

  public void setServer_ip(String server_ip) {
    this.server_ip = server_ip;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "ModifyVpnRequest{" +
        "description='" + description + '\'' +
        ", ifname='" + ifname + '\'' +
        ", mode='" + mode + '\'' +
        ", server_ip='" + server_ip + '\'' +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        '}';
  }
}
