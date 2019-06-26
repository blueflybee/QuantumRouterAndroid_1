package com.fernandocejas.android10.sample.presentation.view.login.forgetpwd;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetIdCodeResponse;
import com.qtec.mapp.model.rsp.ResetPwdGetIdCodeResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface ResetPwdGetIdCodeView extends LoadDataView{
    void showUserPhoneEmp();


    void openResetPwd(ResetPwdGetIdCodeResponse response);

    void showGetIdCodeSuccess();
}
