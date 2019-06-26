package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;


import com.fernandocejas.android10.sample.domain.interactor.cloud.AddTempPwd;
import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.AddTempPwdResponse;
import com.qtec.mapp.model.rsp.GetTempPwdResponse;
import com.qtec.mapp.model.rsp.QueryTempPwdResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 18-9-11
 *     desc   : TempPwdView
 *     version: 1.0
 * </pre>
 */
public interface TempPwdView extends LoadDataView {

  void onAddTempPwdSuccess(AddTempPwdResponse response);

  void onQueryTempPwd(QueryTempPwdResponse response);

  void onGetTempPwdFail();

  void onGetTempPwdSuccess(String tempPassword);
}