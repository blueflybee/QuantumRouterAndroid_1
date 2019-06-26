package com.fernandocejas.android10.sample.presentation.view.login.login;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.LoginResponse;

/**
 * @author shaojun
 * @name LoginView
 * @package com.fernandocejas.android10.sample.presentation.view
 * @date 15-9-10
 */
public interface LoginView extends LoadDataView {
  void openMain(LoginResponse response);

  void showUsernameEmp();

  void showPasswordEmp();

  void showPasswordErrorThreeTimes(Throwable e);

  void showPasswordErrorMoreTimes(Throwable e);
}
