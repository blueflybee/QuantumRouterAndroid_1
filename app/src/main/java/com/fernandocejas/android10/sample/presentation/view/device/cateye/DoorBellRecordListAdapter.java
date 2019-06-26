package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.utils.DateUtil;
import com.fernandocejas.android10.sample.presentation.utils.GlideUtil;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.qtec.cateye.model.response.GetDoorBellRecordListResponse;
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

public class DoorBellRecordListAdapter extends CommAdapter1<GetDoorBellRecordListResponse.MsgList> {
    private List<Integer> mPositions;
    private Context context;
    protected boolean isEdit = false;

    public Map<Integer, Boolean> checkedMap; // 保存checkbox是否被选中的状态
    public Map<Integer, Integer> visibleMap; // 保存checkbox是否显示的状态

    public DoorBellRecordListAdapter(Context context, List<GetDoorBellRecordListResponse.MsgList> data, int layoutId) {
        super(context, data, layoutId);
        this.context = context;

        checkedMap = new HashMap<Integer, Boolean>();
        visibleMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < data.size(); i++) {
            checkedMap.put(i, false);//checked未被选中
            visibleMap.put(i, CheckBox.GONE); //checked不显示
        }

        //寻找需要插入title的位置
        getPostionsToInsertTitle(data);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, GetDoorBellRecordListResponse.MsgList response, int position) {
        TextView time = baseViewHolder.getView(R.id.tv_record_time);
        TextView date = baseViewHolder.getView(R.id.tv_record_date);
        TextView name = baseViewHolder.getView(R.id.tv_record_name);
        ImageView headImg = baseViewHolder.getView(R.id.img_record_head);
        ImageView imgRedImg = baseViewHolder.getView(R.id.img_msg_red);

        try{
            String headImgUrl = response.getPicturePath() + GlideUtil.IMG_OSS_HANDLE_POSTFIX;
            GlideUtil.loadCircleHeadImage(context, headImgUrl, headImg, R.drawable.bg_bell);
        }catch (Exception e){
            e.printStackTrace();
        }

        //门铃类型
        name.setText(convertMsgCode(response.getMessageCode()));

        if("0".equals(response.getIsRead())){
            //未读
            imgRedImg.setVisibility(View.VISIBLE);
        }else if("1".equals(response.getIsRead())){
            //已读
            imgRedImg.setVisibility(View.GONE);
        }

        //格式化时间
        String oridaryDate = response.getOccurTime();
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

        if (mPositions.contains(position)) {
            date.setVisibility(View.VISIBLE);
            date.setText(displayDate);
        } else {
            date.setVisibility(View.GONE);
        }
        time.setText(displayTime);


        CheckBox mCB = (CheckBox) baseViewHolder.getView(R.id.cb_doorRecord);

        try{
            mCB.setTag(position);
            mCB.setVisibility(visibleMap.get(position));
            mCB.setChecked(checkedMap.get(position));
        }catch (Exception e){
            e.printStackTrace();
        }

        mCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedMap.put((Integer) buttonView.getTag(),isChecked);
            }
        });

    }

    private String convertMsgCode(String msgCode) {
        String beerType;
        switch (msgCode){
            case "201":
                beerType = "门铃告警";
                break;
            case "202":
                beerType = "红外感应告警";
                break;
            case "203":
                beerType = "低电量告警";
                break;
            case "":
                beerType = "其他告警";
                break;
            default:
                beerType = "其他告警";
                break;
        }
        return beerType;
    }

    /**
     * 获取需要添加title的位置
     *
     * @param
     * @return
     */
    public void getPostionsToInsertTitle(List<GetDoorBellRecordListResponse.MsgList> data){
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
