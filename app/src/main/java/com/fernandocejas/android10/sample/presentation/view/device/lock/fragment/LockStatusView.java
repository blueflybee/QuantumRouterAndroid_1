package com.fernandocejas.android10.sample.presentation.view.device.lock.fragment;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.GetLockStatusResponse;
import com.qtec.router.model.rsp.RouterStatusResponse;
import com.qtec.router.model.rsp.RouterStatusResponse.Status;

import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface LockStatusView extends LoadDataView{
    void showLockStatus(GetLockStatusResponse response);
}
