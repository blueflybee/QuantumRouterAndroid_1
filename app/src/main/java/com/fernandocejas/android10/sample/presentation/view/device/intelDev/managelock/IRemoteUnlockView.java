package com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.RemoteLockOperationResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface IRemoteUnlockView extends LoadDataView{
    void remoteUnlock(RemoteLockOperationResponse response);
}
