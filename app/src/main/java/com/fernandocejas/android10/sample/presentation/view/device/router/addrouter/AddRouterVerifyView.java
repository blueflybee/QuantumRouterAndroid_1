package com.fernandocejas.android10.sample.presentation.view.device.router.addrouter;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.CommitAddRouterInfoResponse;
import com.qtec.router.model.rsp.AddRouterVerifyResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface AddRouterVerifyView extends LoadDataView{
    void showAdminPwdEmp();

    void onAddSuccess(AddRouterVerifyResponse response);

    void onCommitSuccess(CommitAddRouterInfoResponse response);

}
