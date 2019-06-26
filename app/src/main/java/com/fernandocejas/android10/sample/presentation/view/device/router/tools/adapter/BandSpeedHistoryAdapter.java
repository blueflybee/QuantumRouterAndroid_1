package com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.utils.DateUtil;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.bandspeedmeasure.BandSpeedHisBean;
import com.qtec.mapp.model.rsp.GetUnlockInfoListResponse;

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

public class BandSpeedHistoryAdapter extends CommAdapter1<BandSpeedHisBean> {
    private List<Integer> mPositions;
    public BandSpeedHistoryAdapter(Context context, List<BandSpeedHisBean> data, int layoutId) {
        super(context, data, layoutId);
        getPostionsToInsertTitle(data);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, BandSpeedHisBean bean, int mPostition) {
        TextView date = baseViewHolder.getView(R.id.tv_speed_history_date);
        TextView speed = baseViewHolder.getView(R.id.tv_speed);
        View view = baseViewHolder.getView(R.id.view_speed);

        if(TextUtils.isEmpty(bean.getSpeed())){
            speed.setText("网速 "+0+" MB/s");
        }else{
            speed.setText("网速 "+bean.getSpeed()+" MB/s");
        }


        if (mPositions.contains(mPostition)) {
            date.setVisibility(View.VISIBLE);
            date.setText(bean.getDate());
        } else {
            date.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 获取需要添加title的位置
     *
     * @param
     * @return
     */
    public void getPostionsToInsertTitle(List<BandSpeedHisBean> data){
        mPositions = new ArrayList<>();
        String currentTime = "";
        for (int i = 0; i < data.size(); i++) {
            if (!currentTime.equals(data.get(i).getDate())) {
                mPositions.add(i);
                currentTime = data.get(i).getDate();
            }
        }
        System.out.println("positions = " + mPositions);
    }


}
