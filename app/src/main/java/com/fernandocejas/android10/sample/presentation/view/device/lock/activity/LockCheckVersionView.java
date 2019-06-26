package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/12/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface LockCheckVersionView extends LoadDataView{
  void showFirmwareVersionCheckFail();

  void showHasNewVersion(String currentVersion, String newVersion);

  void showIsLastedVersion(String currentVersion);

  void startUpdateFirmware(String filePath);
}
