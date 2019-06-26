package com.fernandocejas.android10.sample.presentation.view.mine.myinfo;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.CheckAppVersionResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface VersionInfoView extends LoadDataView{
  void showVersionInfo(CheckAppVersionResponse response);
}
