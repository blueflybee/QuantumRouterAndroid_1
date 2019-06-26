package com.fernandocejas.android10.sample.presentation.view.mine.deviceshare;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.fernandocejas.android10.sample.presentation.view.message.receiver.MessageShareListAdapter;
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

public class RouterListAdapter extends CommAdapter1<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> {
    private Context mContext;
    OnMoreClickListener mOnMoreClickListener;
    private int mPosition = 0;

    public RouterListAdapter(Context context, List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> data, int layoutId) {
        super(context, data, layoutId);
        mContext = context;
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> getDevTreeResponse, int mPostition) {
        this.mPosition = mPostition;
        TextView routerName = baseViewHolder.getView(R.id.tv_routerName);
        TextView memNum = baseViewHolder.getView(R.id.tv_memNum);
        ImageView imgMore = baseViewHolder.getView(R.id.img_more);
        if(!TextUtils.isEmpty(getDevTreeResponse.getDeviceName())){
            routerName.setText(getDevTreeResponse.getDeviceName());
        }
        if(TextUtils.isEmpty(getDevTreeResponse.getDeviceShareNum())){
            memNum.setText("正在共享给0位成员");
        }else{
            memNum.setText("正在共享给"+getDevTreeResponse.getDeviceShareNum()+"位成员");
        }

        //门锁
        TextView lockName = baseViewHolder.getView(R.id.tv_lockName);
        StringBuffer buffer = new StringBuffer();
        if(getDevTreeResponse.getDeviceList().size() == 0||(getDevTreeResponse.getDeviceList() != null)){
            lockName.setText("暂无设备连接！");
            return;
        }

        for(int i = 0; i< getDevTreeResponse.getDeviceList().size(); i++){
            buffer.append(getDevTreeResponse.getDeviceList().get(i).getDeviceName());
            buffer.append("\n");
        }
        lockName.setText(buffer.toString());

        /*rlMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnMoreClickListener != null){
                    mOnMoreClickListener.onMoreClick();
                }
            }
        });*/

    }

    public interface OnMoreClickListener {
        void onMoreClick();
    }

    /**
     * 更多 回调接口
     *
     * @param
     * @return
     */
    public void setOnMoreClickListener(OnMoreClickListener mOnMoreClickListener) {
        this.mOnMoreClickListener = mOnMoreClickListener;
    }

    public int getItemPostion(){
        return mPosition;
    }
}
