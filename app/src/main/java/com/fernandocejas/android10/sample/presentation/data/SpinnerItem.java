package com.fernandocejas.android10.sample.presentation.data;

/**
 * @author shaojun
 * @name SpinnerItem
 * @package com.fernandocejas.android10.sample.presentation.data
 * @date 15-12-9
 */
public class SpinnerItem implements KVPair{

  private String key;
  private String value;

  public SpinnerItem() {
  }

  @Override public String getKey() {
    return key;
  }

  @Override public void setKey(String key) {
    this.key = key;
  }

  @Override public String getValue() {
    return value;
  }

  @Override public void setValue(String value) {
    this.value = value;
  }
}
