package com.fernandocejas.android10.sample.presentation.view.login.register;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.RegisterResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface RegisterView extends LoadDataView{
    void showUserPhoneEmp();

    void onGetIdCodeSuccess();

    void showIdCodeEmp();

    void showPasswordEmp();

    void showRegisterSuccess(RegisterResponse response);


}
