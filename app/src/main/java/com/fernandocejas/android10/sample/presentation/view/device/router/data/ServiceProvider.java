package com.fernandocejas.android10.sample.presentation.view.device.router.data;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/03
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ServiceProvider {

  private String name;
  private String tel;

  public ServiceProvider(String name, String tel) {
    this.name = name;
    this.tel = tel;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }
}
