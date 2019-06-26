package com.fernandocejas.android10.sample.presentation.view.device.intelDev.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.utils.DateUtil;
import com.fernandocejas.android10.sample.presentation.utils.GlideUtil;
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

public class LockUseNoteMoreAdapter extends CommAdapter1<GetUnlockInfoListResponse> {
    private List<Integer> mPositions;
    private Context context;

    public LockUseNoteMoreAdapter(Context context, List<GetUnlockInfoListResponse> data, int layoutId) {
        super(context, data, layoutId);
        this.context = context;
        //寻找需要插入title的位置
        getPostionsToInsertTitle(data);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, GetUnlockInfoListResponse unlockInfoListResponse, int mPostition) {
        TextView time = baseViewHolder.getView(R.id.tv_lock_use_more_time);
        TextView date = baseViewHolder.getView(R.id.tv_lock_use_more_date);
        TextView name = baseViewHolder.getView(R.id.tv_lock_use_more_name);
        ImageView headImg = baseViewHolder.getView(R.id.img_lock_use_more_head);
        TextView method = baseViewHolder.getView(R.id.tv_lock_use_more_method);

        try{
            String headImgUrl = unlockInfoListResponse.getUserHeadImage() + GlideUtil.IMG_OSS_HANDLE_POSTFIX;
            GlideUtil.loadCircleHeadImage(context, headImgUrl, headImg, R.drawable.default_avatar);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(!TextUtils.isEmpty(unlockInfoListResponse.getUserNickName())){
            name.setText(unlockInfoListResponse.getUserNickName());
        }

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

        if (mPositions.contains(mPostition)) {
            date.setVisibility(View.VISIBLE);
            date.setText(displayDate);
        } else {
            date.setVisibility(View.GONE);
        }
        time.setText(displayTime);

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
