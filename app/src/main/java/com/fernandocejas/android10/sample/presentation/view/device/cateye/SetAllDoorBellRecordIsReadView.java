package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.cateye.model.response.GetDoorBellRecordDetailResponse;
import com.qtec.cateye.model.response.SetAllDoorBellRecordReadResponse;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc:
 *      version: 1.0
 * </pre>
 */

public interface SetAllDoorBellRecordIsReadView extends LoadDataView {
    void setAllDoorBeelRecordRead(SetAllDoorBellRecordReadResponse response);
}
