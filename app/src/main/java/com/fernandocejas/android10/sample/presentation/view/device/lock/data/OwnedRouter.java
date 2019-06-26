package com.fernandocejas.android10.sample.presentation.view.device.lock.data;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class OwnedRouter implements Serializable{
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
