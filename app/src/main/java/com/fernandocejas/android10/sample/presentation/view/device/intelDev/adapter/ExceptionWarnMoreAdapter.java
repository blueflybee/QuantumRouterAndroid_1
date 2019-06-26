package com.fernandocejas.android10.sample.presentation.view.device.intelDev.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.utils.DateUtil;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
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

public class ExceptionWarnMoreAdapter extends CommAdapter1<GetUnlockInfoListResponse> {
    private List<Integer> mPositions;

    public ExceptionWarnMoreAdapter(Context context, List<GetUnlockInfoListResponse> data, int layoutId) {
        super(context, data, layoutId);

        //寻找需要插入title的位置
        getPostionsToInsertTitle(data);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, GetUnlockInfoListResponse unlockInfoListResponse, int mPostition) {
        TextView date = baseViewHolder.getView(R.id.tv_lock_exception_warn_more_date);
        TextView time = baseViewHolder.getView(R.id.tv_exception_warn_more_time);
        TextView content = baseViewHolder.getView(R.id.tv_exception_warn_more_content);

        //格式化时间
        String oridaryDate = unlockInfoListResponse.getOccurTime();
        String displayDate = "";
        String displayTime = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        try {
            Date ckDate = sdf.parse(oridaryDate);
            displayDate = DateUtil.convertDateToDiaplayTime(ckDate);
            System.out.println("displayDate = " + displayDate);
            displayTime = ckDate.getHours()+":"+ckDate.getMinutes();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (mPositions.contains(mPostition)) {
            date.setVisibility(View.VISIBLE);
            date.setText(displayDate);
        } else {
            date.setVisibility(View.GONE);
        }
        time.setText(displayTime);

        content.setText(unlockInfoListResponse.getMessage());

       /* String statusMsg = "";
        if(!TextUtils.isEmpty(unlockInfoListResponse.getMessageCode())){
            switch (unlockInfoListResponse.getMessageCode()){
                case "1":
                    statusMsg = "网络异常";
                    break;

                case "2":
                    statusMsg = "多次输入错误密码";
                    break;

                case "3":
                    statusMsg = "门锁电量不足";
                    break;

                case "4":
                    statusMsg = "防撬报警";
                    break;

                case "5":
                    statusMsg = "假锁报警";
                    break;
                case "6":
                    statusMsg = "未自动上锁报警";
                    break;
                case "7":
                    statusMsg = "胁迫开锁报警，其他待扩展";
                    break;

                default:
                    statusMsg = "";
                    break;
            }
            if(!TextUtils.isEmpty(statusMsg)){
                content.setText(statusMsg);
            }
        }*/
    }

    /**
    * 获取需要添加title的位置
    *
    * @param
    * @return
    */
    public void getPostionsToInsertTitle(List<GetUnlockInfoListResponse> data){
        mPositions = new ArrayList<>();
        String currentTime = "";
        for (int i = 0; i < data.size(); i++) {
            //格式化时间
            String oridaryDate = data.get(i).getOccurTime();
            String displayDate = "";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
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
