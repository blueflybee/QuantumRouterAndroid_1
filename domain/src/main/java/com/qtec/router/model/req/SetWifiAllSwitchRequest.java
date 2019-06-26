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
public class SetWifiAllSwitchRequest<T> {
  private int enable;
  private T rules;

  public int getEnable() {
    return enable;
  }

  public void setEnable(int enable) {
    this.enable = enable;
  }

  public T getRules() {
    return rules;
  }

  public void setRules(T rules) {
    this.rules = rules;
  }


  public static class WifiSwitchConfig{
    private int id;
    private int rule_enable;

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public int getRule_enable() {
      return rule_enable;
    }

    public void setRule_enable(int rule_enable) {
      this.rule_enable = rule_enable;
    }

    @Override
    public String toString() {
      return "WifiSwitchConfig{" +
          "id=" + id +
          ", rule_enable=" + rule_enable +
          '}';
    }
  }

}
