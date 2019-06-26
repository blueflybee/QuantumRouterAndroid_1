package com.fernandocejas.android10.sample.presentation.view.mine.deviceshare;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.utils.GlideUtil;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.qtec.mapp.model.rsp.GetShareMemListResponse;

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

public class ShareMemListAdapter extends CommAdapter1<GetShareMemListResponse> {
    OnDetailClickListener mOnDetailClickListener;
    private int mPosition = 0;
    private Context mContext;

    public ShareMemListAdapter(Context context, List<GetShareMemListResponse> data, int layoutId) {
        super(context, data, layoutId);
        this.mContext = context;
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, GetShareMemListResponse getShareMemListResponse,int mPosition) {
        this.mPosition = mPosition;
        TextView account = baseViewHolder.getView(R.id.tv_account);
        TextView date = baseViewHolder.getView(R.id.tv_date);
        RelativeLayout rlMoreDetail = baseViewHolder.getView(R.id.rl_more_detail);
        ImageView imgHead = baseViewHolder.getView(R.id.img_head_share_mem);

        String headImgUrl = getShareMemListResponse.getSharedUserHeadImage() + GlideUtil.IMG_OSS_HANDLE_POSTFIX;
        GlideUtil.loadCircleHeadImage(mContext, headImgUrl, imgHead, R.drawable.default_avatar);

        String inviteState = "";
        switch (getShareMemListResponse.getHandleType()){
            case "0":
                inviteState = "拒绝";
                break;
            case "1":
                inviteState = "接受";
                break;
            case "2":
                inviteState = "未处理";
                break;
            case "3":
                inviteState = "已失效";
                break;
            default:
                break;
        }
        account.setText(getShareMemListResponse.getSharedUserName() + "(" + getShareMemListResponse.getSharedUserPhone() + ")");
        date.setText(getShareMemListResponse.getShareDate() + " "+inviteState);

       /* rlMoreDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnDetailClickListener != null){
                    mOnDetailClickListener.onDetailClick();
                }
            }
        });*/

    }

    public interface OnDetailClickListener {
        void onDetailClick();
    }

    /**
     * 更多 回调接口
     *
     * @param
     * @return
     */
    public void setOnDetailClickListener(OnDetailClickListener mOnDetailClickListener) {
        this.mOnDetailClickListener = mOnDetailClickListener;
    }

    public int getItemPostion(){
        return mPosition;
    }

}
