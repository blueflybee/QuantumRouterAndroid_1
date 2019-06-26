package com.fernandocejas.android10.sample.presentation.view.mine.myinfo;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface ModifyPwdView extends LoadDataView{

    void showUserPhoneEmp();

    void onGetIdCodeSuccess();

    void showIdCodeEmp();

    void showPasswordEmp();

    void showModifyPwdSuccess();

}
