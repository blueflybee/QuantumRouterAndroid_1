package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetLockListResponse;

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

public class ConnectLockListAdapter extends CommAdapter1<GetLockListResponse> {
    private Context mContext;
    OnConnectLockClickListener mOnConnectLockClickListener;

    public ConnectLockListAdapter(Context context, List<GetLockListResponse> data, int layoutId) {
        super(context, data, layoutId);
        mContext = context;
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, GetLockListResponse response, int mPostition) {
        TextView name = baseViewHolder.getView(R.id.tv_device_name);
        TextView nick = baseViewHolder.getView(R.id.tv_device_nick);
        TextView connect = baseViewHolder.getView(R.id.tv_device_connect);

        name.setText(response.getDeviceNickName());

        nick.setText(response.getDeviceModel());

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("catEye: 猫眼和门锁配对中… adapter");
                if(mOnConnectLockClickListener != null){
                    mOnConnectLockClickListener.onConnectLockClick(mPostition);
                }
            }
        });
    }

    public interface OnConnectLockClickListener {
        void onConnectLockClick(int position);
    }

    /**
     * 设备详情回调函数
     *
     * @param
     * @return
     */
    public void setOnConnectLockClickListener(OnConnectLockClickListener connectLockClickListener) {
        mOnConnectLockClickListener = connectLockClickListener;
    }

}
