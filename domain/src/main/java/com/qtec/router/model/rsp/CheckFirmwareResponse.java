package com.qtec.router.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class CheckFirmwareResponse {

  private String updateversionNo;
  private String localversionNo;
  //0,1
  private int effectivity;


  public String getUpdateversionNo() {
    return updateversionNo;
  }

  public void setUpdateversionNo(String updateversionNo) {
    this.updateversionNo = updateversionNo;
  }

  public int getEffectivity() {
    return effectivity;
  }

  public void setEffectivity(int effectivity) {
    this.effectivity = effectivity;
  }

  public String getLocalversionNo() {
    return localversionNo;
  }

  public void setLocalversionNo(String localversionNo) {
    this.localversionNo = localversionNo;
  }
}
