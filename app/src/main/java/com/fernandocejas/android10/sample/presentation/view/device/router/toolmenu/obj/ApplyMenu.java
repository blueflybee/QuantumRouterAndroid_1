package com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.obj;

import java.io.Serializable;

/**
 * 应用菜单项
 */
public class ApplyMenu implements Serializable {
  private String name;//显示的文字，应用的名称
  private String id;
  private String type;
  private String url;//应用入口地址
  private boolean fixed;//点击是否可以进行增删
  private int index;//控件的初始位置，可以用于判断长按是否可以拖拽
  private int imgRes;//显示的图片
  private int state;//是否处于头部的状态,0：在头部，1：不在头部

  public ApplyMenu() {

  }

  public ApplyMenu(String name, String id, String type, String url,
                   int index, boolean fixed, int imgRes, int state) {
    this.name = name;
    this.id = id;
    this.type = type;
    this.url = url;
    this.fixed = fixed;
    this.index = index;
    this.imgRes = imgRes;
    this.state = state;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public boolean getFixed() {
    return fixed;
  }

  public void setFixed(boolean fixed) {
    this.fixed = fixed;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public int getImgRes() {
    return imgRes;
  }

  public void setImgRes(int imgRes) {
    this.imgRes = imgRes;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return "ApplyMenu{" +
        "name='" + name + '\'' +
        ", id='" + id + '\'' +
        ", type='" + type + '\'' +
        ", state=" + state +
        '}';
  }

  @Override
  public ApplyMenu clone() {
    ApplyMenu applyMenu = new ApplyMenu();
    applyMenu.setId(getId());
    applyMenu.setFixed(getFixed());
    applyMenu.setImgRes(getImgRes());
    applyMenu.setIndex(getIndex());
    applyMenu.setName(getName());
    applyMenu.setState(getState());
    applyMenu.setType(getType());
    applyMenu.setUrl(getUrl());
    return applyMenu;
  }
}
