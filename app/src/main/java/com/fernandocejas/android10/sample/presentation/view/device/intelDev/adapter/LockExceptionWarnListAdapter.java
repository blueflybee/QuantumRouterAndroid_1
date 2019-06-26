package com.fernandocejas.android10.sample.presentation.view.device.intelDev.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.utils.DateUtil;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.qtec.mapp.model.rsp.GetUnlockInfoListResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/22
 *     desc   : 门锁异常提醒
 *     version: 1.0
 * </pre>
 */
public class LockExceptionWarnListAdapter extends CommAdapter1<GetUnlockInfoListResponse> {

    public LockExceptionWarnListAdapter(Context context, List<GetUnlockInfoListResponse> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, GetUnlockInfoListResponse unlockInfoListResponse, int mPostition) {
        TextView date = baseViewHolder.getView(R.id.tv_lock_exception_date);
        TextView time = baseViewHolder.getView(R.id.tv_lock_exception_time);
       // TextView user = baseViewHolder.getView(R.id.tv_lock_exception_name);
        TextView content = baseViewHolder.getView(R.id.tv_lock_exception_content);

        //user.setText(unlockInfoListResponse.getUserNickName());

        //格式化时间
        //格式化时间
        String oridaryDate = unlockInfoListResponse.getOccurTime();
        String displayDate = "";
        String displayTime = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
            Locale.CHINA);
        try {
            Date ckDate = sdf.parse(oridaryDate);
            displayDate = DateUtil.convertDateToDiaplayTime(ckDate);
            displayTime = (new SimpleDateFormat("HH:mm")).format(ckDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        date.setText(displayDate);
        time.setText(displayTime);

        /*String statusMsg = "";
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
                    break;
            }
        }*/

        content.setText(unlockInfoListResponse.getMessage());
    }
}
