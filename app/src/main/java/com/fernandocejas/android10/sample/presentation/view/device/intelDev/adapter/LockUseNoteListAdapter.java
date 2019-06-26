package com.fernandocejas.android10.sample.presentation.view.device.intelDev.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.utils.DateUtil;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.qtec.mapp.model.rsp.GetMsgListResponse;
import com.qtec.mapp.model.rsp.GetUnlockInfoListResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class LockUseNoteListAdapter extends CommAdapter1<GetUnlockInfoListResponse> {

    public LockUseNoteListAdapter(Context context, List<GetUnlockInfoListResponse> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, GetUnlockInfoListResponse unlockInfoListResponse, int mPostition) {
        TextView date = baseViewHolder.getView(R.id.tv_lock_use_date);
        TextView time = baseViewHolder.getView(R.id.tv_lock_use_time);
        TextView user = baseViewHolder.getView(R.id.tv_lock_user);
        TextView method = baseViewHolder.getView(R.id.tv_lock_use_method);

        //格式化时间
        String oridaryDate = unlockInfoListResponse.getOccurTime();
        String displayDate = "";
        String displayTime = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        try {
            Date ckDate = sdf.parse(oridaryDate);
            displayDate = DateUtil.convertDateToDiaplayTime(ckDate);
            displayTime = (new SimpleDateFormat("HH:mm")).format(ckDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        date.setText(displayDate);
        time.setText(displayTime);
        user.setText(unlockInfoListResponse.getUserNickName());

        method.setText(unlockInfoListResponse.getMessage());

       /* String openMethod = "";
        if(!TextUtils.isEmpty(unlockInfoListResponse.getMessageCode())){
            switch (unlockInfoListResponse.getMessageCode()){
                case "0":
                    openMethod = "量子开锁-近场开门";
                    break;

                case "1":
                    openMethod = "量子开锁-远场开门";
                    break;

                case "2":
                    openMethod = "钥匙开锁";
                    break;

                case "3":
                    openMethod = "蓝牙开锁";
                    break;

                case "4":
                    openMethod = "密码开锁";
                    break;

                case "5":
                    openMethod = "蓝牙反锁，其他待扩展";
                    break;

                default:
                    openMethod = "";
                    break;
            }

            if(!TextUtils.isEmpty(openMethod)){
                method.setText(openMethod);
            }
        }*/


    }
}
