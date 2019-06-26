package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.widget.ImageView;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/09/07
 *      desc: 单个文件操作
 *      version: 1.0
 * </pre>
 */

public class SingalFileOperateBean {
  private String title;
  private int img;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getImg() {
    return img;
  }

  public void setImg(int img) {
    this.img = img;
  }

  @Override
  public String toString() {
    return "SingalFileOperateBean{" +
        "title='" + title + '\'' +
        ", img=" + img +
        '}';
  }
}
