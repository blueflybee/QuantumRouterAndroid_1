package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.utils.DateUtil;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.qtec.mapp.model.rsp.GetMsgOtherListResponse;

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

public class MessageListAdapter extends CommAdapter1<GetMsgOtherListResponse> {
    protected boolean isEdit = false;
    private List<Integer> mPositions;

    public Map<Integer, Boolean> checkedMap; // 保存checkbox是否被选中的状态
    public Map<Integer, Integer> visibleMap; // 保存checkbox是否显示的状态

    public MessageListAdapter(Context context,List<GetMsgOtherListResponse> data, int layoutId) {
        super(context, data, layoutId);
        checkedMap = new HashMap<Integer, Boolean>();
        visibleMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < data.size(); i++) {
            checkedMap.put(i, false);//checked未被选中
            visibleMap.put(i, CheckBox.GONE); //checked不显示
        }

        //寻找需要插入title的位置
        //getPostionsToInsertTitle(data);
    }

    @Override
    public void convert(final BaseViewHolder baseViewHolder, final GetMsgOtherListResponse getMsgListResponse,int position) {
        TextView name = baseViewHolder.getView(R.id.tv_deviceName);
        TextView time = baseViewHolder.getView(R.id.tv_deviceTime);
        TextView msg = baseViewHolder.getView(R.id.tv_deviceMsg);

        name.setText(getMsgListResponse.getMessageTitle());
        msg.setText(getMsgListResponse.getMessageContent());

        //格式化时间
        String oridaryDate = getMsgListResponse.getCreateDate();
        String displayDate = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        try {
            Date ckDate = sdf.parse(oridaryDate);
            displayDate = DateUtil.convertDateToDiaplayTime(ckDate);
            System.out.println("displayDate = " + displayDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

      /*  if (mPositions.contains(position)) {
            time.setVisibility(View.VISIBLE);
            time.setText(displayDate);
        } else {
            time.setVisibility(View.GONE);
        }*/

        time.setText(displayDate);

        CheckBox mCB = (CheckBox) baseViewHolder.getView(R.id.cb_otherMsg);

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

    public void setEdit(boolean edit){
        this.isEdit = edit;
        notifyDataSetChanged();
    }

    /**
     * 获取需要添加title的位置
     *
     * @param
     * @return
     */
    public void getPostionsToInsertTitle(List<GetMsgOtherListResponse> data){
        mPositions = new ArrayList<>();
        String currentTime = "";
        for (int i = 0; i < data.size(); i++) {
            //格式化时间
            String oridaryDate = data.get(i).getCreateDate();
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
