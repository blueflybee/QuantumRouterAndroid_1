package com.fernandocejas.android10.sample.presentation.view.device.intelDev.fragment;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetUnlockInfoListResponse;

import java.util.List;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc:
 *      version: 1.0
 * </pre>
 */

public interface IGetUnlockInfoListView extends LoadDataView {
    void openLockUseNoteList(List<GetUnlockInfoListResponse> response);
    void openExceptionWarningList(List<GetUnlockInfoListResponse> response);
}
