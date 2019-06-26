package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.GetSambaAccountResponse;
import com.qtec.router.model.rsp.ModifyFingerNameResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   : 获取samba账号 密码
 *     version: 1.0
 * </pre>
 */
public interface IGetSambaAccountView extends LoadDataView{
    void getSambaAccount(GetSambaAccountResponse response);
}
