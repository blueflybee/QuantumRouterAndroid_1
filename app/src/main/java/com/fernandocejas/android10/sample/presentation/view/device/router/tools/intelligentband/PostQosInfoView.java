package com.fernandocejas.android10.sample.presentation.view.device.router.tools.intelligentband;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.GetQosInfoResponse;
import com.qtec.router.model.rsp.PostQosInfoResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface PostQosInfoView extends LoadDataView {
  void postQosInfo(PostQosInfoResponse response);
}
