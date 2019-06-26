/*
 * Copyright 2018 Google Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.google.android.sambadocumentsprovider.mount;

import java.io.File;
import java.io.IOException;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/08/17
 *      desc: 本地文件上传工具包
 *      version: 1.0
 * </pre>
 */

public class UploadUtils {
  /**
   * 支持在线预览的本地路径
   *
   * @param
   * @return
   */
  public static final String genLocalPathForPreview(String smbFilePath) {
    //File folder = new File(String.valueOf(Environment.getExternalStorageDirectory()) + File.separator + "smbsamba/");
    File folder = new File(AppConstant.SAMBA_DOWNLOAD_CACHE_PATH);
    if (!folder.exists()) {
      folder.mkdirs();
    }
    String name = getFileName(smbFilePath);
    if (name == null) {
      name = String.valueOf(System.currentTimeMillis());
    }
    StringBuffer buffer = new StringBuffer();
    buffer.append(folder.getAbsolutePath()).append("/").append(name).toString();
    File f = new File(buffer.toString());
    try {
      f.createNewFile();

    } catch (IOException e) {
      e.printStackTrace();
    }
    return f.getAbsolutePath();
  }

  public final static String getFileName(String path) {
    if (path == null) {
      return path;
    }
    if (!path.contains("/")) {
      return path;
    }
    int index = path.lastIndexOf("/");
    if (index < 0 || index + 1 >= path.length()) {
      return path;
    }
    return path.substring(index + 1);
  }


}
