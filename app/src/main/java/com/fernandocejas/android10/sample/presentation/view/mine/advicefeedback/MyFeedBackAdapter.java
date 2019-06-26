package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.utils.Base64Util;
import com.fernandocejas.android10.sample.presentation.utils.DateUtil;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.fernandocejas.android10.sample.presentation.view.mine.deviceshare.ShareMemListAdapter;
import com.qtec.mapp.model.rsp.GetMyAdviceResponse;
import com.qtec.mapp.model.rsp.GetUnlockInfoListResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class MyFeedBackAdapter extends CommAdapter1<GetMyAdviceResponse> {
    private List<Integer> mPositions;
    private int index = 0;
    OnAdviceDetailClickListener mOnAdviceDetailClickListener;

    public MyFeedBackAdapter(Context context, List<GetMyAdviceResponse> data, int layoutId) {
        super(context, data, layoutId);
        //寻找需要插入title的位置
        getPostionsToInsertTitle(data);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, GetMyAdviceResponse getMyAdviceResponse, int mPostition) {
        // 含有表情的富文本发送之前Utf-8编码过，所以要解码
        index = mPostition;
        String tempContent = "";
        try {
            tempContent = Base64Util.decodeBase64(getMyAdviceResponse.getFeedbackContent());
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView date = baseViewHolder.getView(R.id.tv_time);
        TextView content = baseViewHolder.getView(R.id.tv_questionTitle);
        RelativeLayout rlItem = baseViewHolder.getView(R.id.rl_item_question);
        //TextView title = baseViewHolder.getView(R.id.tv_questionTitle);

        //格式化时间
        String oridaryDate = getMyAdviceResponse.getCreateTime();
        String displayDate = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date ckDate = sdf.parse(oridaryDate);
            displayDate = DateUtil.convertDateToDiaplayTime(ckDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (mPositions.contains(mPostition)) {
            date.setVisibility(View.VISIBLE);
            date.setText(displayDate);
        } else {
            date.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(tempContent)){
            content.setText(tempContent);
        }

        rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnAdviceDetailClickListener != null){
                    mOnAdviceDetailClickListener.onAdviceDetailClick(mPostition);
                }
            }
        });

    }

    public interface OnAdviceDetailClickListener {
        void onAdviceDetailClick(int position);
    }

    /**
     * 更多 回调接口
     *
     * @param
     * @return
     */
    public void setOnAdviceDetailClickListener(OnAdviceDetailClickListener mOnAdviceDetailClickListener) {
        this.mOnAdviceDetailClickListener = mOnAdviceDetailClickListener;
    }

    public int getItemPostion(){
        return index;
    }


    /**
     * 获取需要添加title的位置
     *
     * @param
     * @return
     */
    public void getPostionsToInsertTitle(List<GetMyAdviceResponse> data){
        mPositions = new ArrayList<>();
        String currentTime = "";
        for (int i = 0; i < data.size(); i++) {
            //格式化时间
            String oridaryDate = data.get(i).getCreateTime();
            String displayDate = "";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            try {
                Date ckDate = sdf.parse(oridaryDate);
                displayDate = DateUtil.convertDateToDiaplayTime(ckDate);
            } catch (Exception e) {
            }
            if (!currentTime.equals(displayDate)) {
                mPositions.add(i);
                currentTime = displayDate;
            }
        }
        System.out.println("positions = " + mPositions);
    }
}
