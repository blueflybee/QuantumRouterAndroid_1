package com.fernandocejas.android10.sample.presentation.view.device.lock.fragment;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetDoorCardsResponse;
import com.qtec.mapp.model.rsp.GetFingerprintsResponse;
import com.qtec.mapp.model.rsp.GetPasswordsResponse;

import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface SecurityManageView extends LoadDataView{
  void showFingerprints(List<GetFingerprintsResponse> fingerprints);

  void showPasswords(List<GetPasswordsResponse> passwords);

  void showNoFingerprints();

  void showNoPasswords();

  void showNoDoorCards();

  void showDoorCards(List<GetDoorCardsResponse> responses);
}
