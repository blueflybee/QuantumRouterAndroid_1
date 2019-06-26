package com.fernandocejas.android10.sample.presentation.view.login.register;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetIdCodeResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   : 获取验证码view
 *     version: 1.0
 * </pre>
 */
public interface GetIdCodeView extends LoadDataView{
    void showUserPhoneEmp();

    void openRegister(GetIdCodeResponse response);

    void showGetIdCodeSuccess();

}
