package com.fernandocejas.android10.sample.presentation.view.safe;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.PostInspectionResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface PostSafeTestView extends LoadDataView{
    void safeInspection(PostInspectionResponse response);
}
