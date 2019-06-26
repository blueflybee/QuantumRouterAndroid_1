package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.QueryDiskStateResponse;

/**
 * <pre>
 *     author :
 *     time   : 2017/06/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface IFormatDiskView extends LoadDataView{
    void formatDisk(QueryDiskStateResponse response);
}
