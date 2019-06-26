package com.qtec.lock.model.core;

import com.fernandocejas.android10.sample.domain.utils.BleHexUtil;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/30
 *     desc   : 蓝牙通信数据包分包模式一个子包
 *     version: 1.0
 * </pre>
 */
public class SubBlePkg implements Comparable{

  private String head;
  private String offset;
  private int index;
  private String subPayload;

  public String getHead() {
    return head;
  }

  public void setHead(String head) {
    this.head = head;
  }

  public String getOffset() {
    return offset;
  }

  public void setOffset(String offset) {
    this.offset = offset;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public String getSubPayload() {
    return subPayload;
  }

  public void setSubPayload(String subPayload) {
    this.subPayload = subPayload;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SubBlePkg subBlePkg = (SubBlePkg) o;

    if (index != subBlePkg.index) return false;
    if (head != null ? !head.equals(subBlePkg.head) : subBlePkg.head != null) return false;
    if (offset != null ? !offset.equals(subBlePkg.offset) : subBlePkg.offset != null) return false;
    return subPayload != null ? subPayload.equals(subBlePkg.subPayload) : subBlePkg.subPayload == null;

  }

  @Override
  public int hashCode() {
    int result = head != null ? head.hashCode() : 0;
    result = 31 * result + (offset != null ? offset.hashCode() : 0);
    result = 31 * result + index;
    result = 31 * result + (subPayload != null ? subPayload.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "SubBlePkg{" +
        "head='" + head + '\'' +
        ", offset='" + offset + '\'' +
        ", index=" + index +
        ", subPayload='" + subPayload + '\'' +
        '}';
  }

  @Override
  public int compareTo(Object o) {
    return getIndex() - ((SubBlePkg) o).getIndex();
  }

  public byte[] toBytes() {
    return BleHexUtil.hexStringToBytes(getHead() + getOffset() + getSubPayload());
  }
}
