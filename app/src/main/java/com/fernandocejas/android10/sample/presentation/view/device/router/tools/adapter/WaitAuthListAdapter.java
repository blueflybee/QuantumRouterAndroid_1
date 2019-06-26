package com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.ChildCareBean;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.WaitAuthBean;
import com.qtec.router.model.rsp.GetWaitingAuthDeviceListResponse;

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

public class WaitAuthListAdapter extends CommAdapter1<GetWaitingAuthDeviceListResponse> {
    private Context mContext;
    public WaitAuthListAdapter(Context context, List<GetWaitingAuthDeviceListResponse> data, int layoutId) {
        super(context, data, layoutId);
        this.mContext = context;
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, GetWaitingAuthDeviceListResponse bean,int position) {
        TextView name = (TextView) baseViewHolder.getView(R.id.tv_wait_auth_name);
        TextView state = (TextView) baseViewHolder.getView(R.id.tv_state);
        ImageView head = (ImageView) baseViewHolder.getView(R.id.img_wait_auth_head);

        if(TextUtils.isEmpty(bean.getDev_name())){
            name.setText("unknow");
        }else{
            name.setText(bean.getDev_name());
        }

        state.setText("手动认证");

        //判断一下本机是否已经认证
        try {
            if((!TextUtils.isEmpty(AppConstant.getMacAddress(mContext))) && (!(TextUtils.isEmpty(bean.getDev_mac())))){
                if(AppConstant.getMacAddress(mContext).equalsIgnoreCase(bean.getDev_mac())){
                    PrefConstant.IS_AUTI_DEVICE_AUTHED = false;
                    System.out.println("防蹭网：WaitAuthListAdapter IS_AUTI_DEVICE_AUTHED "+PrefConstant.IS_AUTI_DEVICE_AUTHED);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        switch (bean.getDev_type()){
            case 0:
                //computer
                head.setBackground(mContext.getResources().getDrawable(R.drawable.ic_mac_blue));
                break;

            case 1:
                //android
                head.setBackground(mContext.getResources().getDrawable(R.drawable.ic_phone));
                break;

            case 2:
                // iphone
                head.setBackground(mContext.getResources().getDrawable(R.drawable.ic_phone));
                break;

            case 3:
                // iphone
                head.setBackground(mContext.getResources().getDrawable(R.drawable.ic_phone));
                break;

            default:
                break;
        }

    }
}
