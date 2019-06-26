package com.fernandocejas.android10.sample.presentation.view.device.router.tools.specialattention;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.GetSpecialAttentionResponse;
import com.qtec.router.model.rsp.PostSpecialAttentionResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface GetSpecialAttentionView extends LoadDataView {
  void getSpecialAttention(GetSpecialAttentionResponse response);
}
