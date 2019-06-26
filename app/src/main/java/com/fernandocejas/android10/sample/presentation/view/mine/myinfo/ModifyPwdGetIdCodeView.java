package com.fernandocejas.android10.sample.presentation.view.mine.myinfo;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.ModifyPwdGetIdCodeResponse;
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
public interface ModifyPwdGetIdCodeView extends LoadDataView{
    void showUserPhoneEmp();

    void showGetIdCodeSuccess();

    void openModifyPwd(ModifyPwdGetIdCodeResponse response);

}
