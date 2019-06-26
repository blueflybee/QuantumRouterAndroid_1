package com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.qtec.router.model.rsp.GetWifiTimeConfigResponse;

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

public class WifiTimeIntervalListAdapter extends CommAdapter1<GetWifiTimeConfigResponse.WifiTimeConfig> {

    OnDetailClickListener mOnDetailClickListener;
    OnSetSwitchClickListener setSwitchClickListener;

    public WifiTimeIntervalListAdapter(Context context, List<GetWifiTimeConfigResponse.WifiTimeConfig> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, GetWifiTimeConfigResponse.WifiTimeConfig bean,int position) {
        String startHour = "",startMin = "",endHour = "",endMin = "";

        TextView time = (TextView) baseViewHolder.getView(R.id.tv_time);
        TextView weeks = (TextView) baseViewHolder.getView(R.id.tv_weeks);
        ImageView intervalSwitch = (ImageView) baseViewHolder.getView(R.id.img_interval_switch);
        TextView name = (TextView) baseViewHolder.getView(R.id.tv_time_interval_name);
        RelativeLayout timeIntervalLayout = (RelativeLayout) baseViewHolder.getView(R.id.rl_time_interval1);

        name.setText(bean.getName());
        // <10拼接一个0
        if(bean.getStart_hour() < 10){
            startHour = "0"+bean.getStart_hour();
        }else {
            startHour = ""+bean.getStart_hour();
        }
        if(bean.getStart_min() < 10){
            startMin = "0"+bean.getStart_min();
        }else {
            startMin = ""+bean.getStart_min();
        }
        if(bean.getStop_hour() < 10){
            endHour = "0"+bean.getStop_hour();
        }else {
            endHour = ""+bean.getStop_hour();
        }
        if(bean.getStop_min() < 10){
            endMin = "0"+bean.getStop_min();
        }else {
            endMin = ""+bean.getStop_min();
        }

        time.setText(""+startHour+":"+startMin+"-"+endHour+":"+endMin);


        String[] weekDays = bean.getWeek_day().split(",");
        StringBuffer buffer = new StringBuffer();
        //0表示星期日，1-6表示星期一至六

        for (int i = 0; i < weekDays.length; i++) {
            for (int j = 0; j < 7; j++) {
                if((""+j).equals(weekDays[i])){
                    buffer.append(convertDateToString(j)).append(" ");
                    break;
                }
            }
        }

        //规则转换
        if("1,2,3,4,5".equals(bean.getWeek_day())){
            //工作日：12345
            weeks.setText("工作日");
        }else if("6,0".equals(bean.getWeek_day())){
            //周末：67
            weeks.setText("周末");
        }else if("1,2,3,4,5,6,0".equals(bean.getWeek_day())){
            //每天
            weeks.setText("每天");
        }else{
            weeks.setText(buffer);
        }

        /*weeks.setText(buffer);*/

        if(bean.getRule_enable() == 1){
            //生效
            intervalSwitch.setBackgroundResource(R.drawable.ic_open);
        }else{
            intervalSwitch.setBackgroundResource(R.drawable.ic_off);
        }

        intervalSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isIntervalOn = true;

                if(bean.getRule_enable() == 1){
                    isIntervalOn = false;
                    /*intervalSwitch.setBackgroundResource(R.drawable.ic_off);*/
                }else {
                    isIntervalOn = true;
                    /*intervalSwitch.setBackgroundResource(R.drawable.ic_open);*/
                }

                if(setSwitchClickListener != null){
                    setSwitchClickListener.onSetSwitchClick(position,isIntervalOn);
                }
            }
        });

        timeIntervalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnDetailClickListener != null){
                    mOnDetailClickListener.onDetailClick(position);
                }
            }
        });

    }

    private String convertDateToString(int i) {
        String str = "";
        switch (i){
            case 0:
                str = "周日";
                break;
            case 1:
                str = "周一";
                break;
            case 2:
                str = "周二";
                break;
            case 3:
                str = "周三";
                break;
            case 4:
                str = "周四";
                break;
            case 5:
                str = "周五";
                break;
            case 6:
                str = "周六";
                break;
            default:
                break;
        }
        return str;
    }

    public void setOnDetailClickListener(OnDetailClickListener mOnDetailClickListener) {
        this.mOnDetailClickListener = mOnDetailClickListener;
    }

    public interface OnDetailClickListener {
        void onDetailClick(int position);
    }
    public interface OnSetSwitchClickListener {
        void onSetSwitchClick(int position,Boolean flag);
    }


    public void setSwitchClickListener(OnSetSwitchClickListener setSwitchClickListener) {
        this.setSwitchClickListener = setSwitchClickListener;
    }


}
