package com.fernandocejas.android10.sample.presentation.view.device.router.loadkey;

import com.fernandocejas.android10.sample.presentation.data.LZKeyInfo;
import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.FirstGetKeyResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/07/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface MatchPinView extends LoadDataView{
  void showPinEmp();

  void showFirstGetKeySuccess();

  void showPinVerifyFail();

  void goLoadKey(LZKeyInfo<LZKeyInfo.KeyBean> data);

  void showPinVerifySuccess();

}
