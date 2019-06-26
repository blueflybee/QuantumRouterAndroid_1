package com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.SpecialAttentionBean;

import java.util.List;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 已关注
 *      version: 1.0
 * </pre>
 */

public class AttentionedListAdapter extends CommAdapter1<SpecialAttentionBean> {
    Boolean isOnLine = true,isOffLine = true;
    OnRemoveClickListener mOnRemoveClickListener;
    OnOnLineClickListener mOnlineClickListener;
    OnOffLineClickListener mOnOfflineClickListener;

    private Context mContext;

    public AttentionedListAdapter(Context context, List<SpecialAttentionBean> data, int layoutId) {
        super(context, data, layoutId);
        mContext = context;
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, SpecialAttentionBean bean,int position) {
        TextView name = (TextView) baseViewHolder.getView(R.id.tv_attentioned_name);
        //cb_special_attention
        ImageView head = (ImageView) baseViewHolder.getView(R.id.img_attentioned_head);
        ImageView offLine = (ImageView) baseViewHolder.getView(R.id.img_attentioned_offline);
        ImageView onLine = (ImageView) baseViewHolder.getView(R.id.img_attentioned_online);
        Button removeBtn = (Button) baseViewHolder.getView(R.id.btn_remove_atttention);

        if(!TextUtils.isEmpty(bean.getName())){
            name.setText(bean.getName());
        }else{
            name.setText("unknow");
        }

        switch (bean.getType()) {
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

            default:
                head.setBackground(mContext.getResources().getDrawable(R.drawable.ic_phone));
                break;
        }

        if("0".equals(bean.getMode())){
            offLine.setBackgroundResource(R.drawable.ic_off);
            onLine.setBackgroundResource(R.drawable.ic_off);
        }else if("1".equals(bean.getMode())){
            offLine.setBackgroundResource(R.drawable.ic_off);
            onLine.setBackgroundResource(R.drawable.ic_open);
        }else if("2".equals(bean.getMode())){
            offLine.setBackgroundResource(R.drawable.ic_open);
            onLine.setBackgroundResource(R.drawable.ic_off);
        }else if("3".equals(bean.getMode())){
            offLine.setBackgroundResource(R.drawable.ic_open);
            onLine.setBackgroundResource(R.drawable.ic_open);
        }

        offLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOffLine){
                    isOffLine = false;
                    offLine.setBackgroundResource(R.drawable.ic_off);
                }else {
                    isOffLine = true;
                    offLine.setBackgroundResource(R.drawable.ic_open);
                }
                if(mOnOfflineClickListener != null){
                    mOnOfflineClickListener.onOffLineClick(position,isOffLine);
                }
            }
        });

        onLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnLine){
                    isOnLine = false;
                    onLine.setBackgroundResource(R.drawable.ic_off);
                }else {
                    isOnLine = true;
                    onLine.setBackgroundResource(R.drawable.ic_open);
                }
                if(mOnlineClickListener != null){
                    mOnlineClickListener.onOnlineClick(position,isOnLine);
                }

            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnRemoveClickListener != null){
                    mOnRemoveClickListener.onRemoveClick(position);
                }
            }
        });

    }

    public void setOnDetailClickListener(OnRemoveClickListener mOnRemoveClickListener) {
        this.mOnRemoveClickListener = mOnRemoveClickListener;
    }

    public void setOnLineClickListener(OnOnLineClickListener mOnlineClickListener) {
        this.mOnlineClickListener = mOnlineClickListener;
    }

    public void setOnOffLineClickListener(OnOffLineClickListener mOnOfflineClickListener) {
        this.mOnOfflineClickListener = mOnOfflineClickListener;
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(int position);
    }

    public interface OnOnLineClickListener {
        void onOnlineClick(int position,Boolean flag);
    }

    public interface OnOffLineClickListener {
        void onOffLineClick(int position,Boolean flag);
    }

}
