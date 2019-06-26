package com.fernandocejas.android10.sample.presentation.view.device.router.tools.data;

import java.io.Serializable;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/08/21
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class WaitAuthBean implements Serializable{
  private String type;
  private String name;
  private String state;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return "ChildCareBean{" +
        "type='" + type + '\'' +
        ", name='" + name + '\'' +
        ", state='" + state + '\'' +
        '}';
  }
}
