package com.fernandocejas.android10.sample.presentation.view.device.router.tools.directsafeinspection;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.PostInspectionResponse;
import com.qtec.router.model.rsp.SetVpnResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface PostInspectionView extends LoadDataView{
    void safeInspection(PostInspectionResponse response);
}
