package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import java.io.File;
import java.io.Serializable;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/08/09
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class LoadingBean implements Serializable{
  private float downloadProgress;
  private float uploaded;
  private long totalSize;
  private File file;

  public LoadingBean() {
  }

  public LoadingBean(float downloadProgress, float uploaded, long totalSize, File file) {
    this.downloadProgress = downloadProgress;
    this.uploaded = uploaded;
    this.totalSize = totalSize;
    this.file = file;
  }

  public float getDownloadProgress() {
    return downloadProgress;
  }

  public void setDownloadProgress(float downloadProgress) {
    this.downloadProgress = downloadProgress;
  }

  public float getUploaded() {
    return uploaded;
  }

  public void setUploaded(float uploaded) {
    this.uploaded = uploaded;
  }

  public long getTotalSize() {
    return totalSize;
  }

  public void setTotalSize(long totalSize) {
    this.totalSize = totalSize;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  @Override
  public String toString() {
    return "LoadingBean{" +
        "downloadProgress=" + downloadProgress +
        ", uploaded=" + uploaded +
        ", totalSize=" + totalSize +
        ", fileName='" + file + '\'' +
        '}';
  }
}
