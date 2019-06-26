package com.fernandocejas.android10.sample.presentation.data;

/**
 * Created by wusj on 17/1/4.
 */

public class User {
  private String username;
  private String password;
  private String sessionId;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String toString() {
    return "Data{" +
        "password='" + password + '\'' +
        ", username='" + username + '\'' +
        ", sessionId='" + sessionId + '\'' +
        '}';
  }
}
