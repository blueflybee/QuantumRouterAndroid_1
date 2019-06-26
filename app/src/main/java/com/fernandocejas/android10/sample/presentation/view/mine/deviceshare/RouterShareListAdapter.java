package com.fernandocejas.android10.sample.presentation.view.mine.deviceshare;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;

import java.util.List;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class RouterShareListAdapter extends CommAdapter1<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> {
    private Context mContext;

    public RouterShareListAdapter(Context context, List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> data, int layoutId) {
        super(context, data, layoutId);
        mContext = context;
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> getDevTreeResponse, int mPostition) {
        TextView routerName = baseViewHolder.getView(R.id.tv_routerName);
        TextView memNum = baseViewHolder.getView(R.id.tv_memNum);

        if(!TextUtils.isEmpty(getDevTreeResponse.getDeviceName())){
            routerName.setText(getDevTreeResponse.getDeviceName());
        }
        if(TextUtils.isEmpty(getDevTreeResponse.getDeviceShareNum())){
            memNum.setText("正在共享给0位成员");
        }else{
            memNum.setText("正在共享给"+getDevTreeResponse.getDeviceShareNum()+"位成员");
        }
    }

}
