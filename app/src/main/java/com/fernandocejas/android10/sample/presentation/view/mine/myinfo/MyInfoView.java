package com.fernandocejas.android10.sample.presentation.view.mine.myinfo;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.LogoutResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface MyInfoView extends LoadDataView {
  void openLogin(LogoutResponse response);

  void finishView();

  void showModifySuccess();

  void uploadHeadImageSuccess(String imageUrl);

  void uploadHeadImageFail();



//    void showUserPhoneEmp();

//    void showUserHeadImgEmp();
//
//    void showUserNickNameEmp();

}
