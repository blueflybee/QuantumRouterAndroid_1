package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.utils.DateUtil;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.qtec.mapp.model.rsp.GetMsgListResponse;

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

public class MessageShareListAdapter extends CommAdapter1<GetMsgListResponse<GetMsgListResponse.messageContent>> {
    private List<GetMsgListResponse> mDeleteMsgList = new ArrayList<>();
    OnAcceptDetailClickListener mOnAcceptDetailClickListener;
    private Button mBtnAccept;
    public Map<Integer, Boolean> checkedMap; // 保存checkbox是否被选中的状态
    public Map<Integer, Integer> visibleMap; // 保存checkbox是否显示的状态
    private Context mContext;

    public MessageShareListAdapter(Context context, List<GetMsgListResponse<GetMsgListResponse.messageContent>> data, int layoutId) {
        super(context, data, layoutId);
        this.mContext = context;
        checkedMap = new HashMap<Integer, Boolean>();
        visibleMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < data.size(); i++) {
            checkedMap.put(i, false);//checked未被选中
            visibleMap.put(i, CheckBox.GONE); //checked不显示
        }

    }

    @Override
    public void convert(final BaseViewHolder baseViewHolder, final GetMsgListResponse<GetMsgListResponse.messageContent> getMsgListResponse,int position) {

        TextView title = baseViewHolder.getView(R.id.tv_title);
        TextView deviceTree = baseViewHolder.getView(R.id.tv_deviceTree);
        TextView date = baseViewHolder.getView(R.id.tv_date);
        title.setText(getMsgListResponse.getMessageTitle());

        //格式化时间
        String oridaryDate = getMsgListResponse.getCreateDate();
        String displayDate = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        try {
            Date ckDate = sdf.parse(oridaryDate);
            System.out.println("消息 oridaryDate = " + oridaryDate);
            System.out.println("消息 ckDate = " + ckDate);
            displayDate = DateUtil.convertDateToDiaplayTime(ckDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date.setText(displayDate);

        StringBuffer buffer = new StringBuffer();

        buffer.append(getMsgListResponse.getMessageContent().getDeviceName());
        buffer.append("\n");
        for(int i=0;i<getMsgListResponse.getMessageContent().getDeviceInfo().length;i++){
            buffer.append(getMsgListResponse.getMessageContent().getDeviceInfo()[i]);
            buffer.append("\n");
        }
        deviceTree.setText(buffer);

        switch (getMsgListResponse.getMessageContent().getHandleType()){
            case "0":
                baseViewHolder.getView(R.id.btn_accept).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.tv_state).setVisibility(View.VISIBLE);
                ((TextView)baseViewHolder.getView(R.id.tv_state)).setText("拒绝");
                ((TextView)baseViewHolder.getView(R.id.tv_state)).setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                ((TextView)baseViewHolder.getView(R.id.tv_state)).setTextColor(mContext.getResources().getColor(R.color.white));
                break;

            case "1":
                // 0 拒绝 1 已接受 2 接受 3 已失效
                baseViewHolder.getView(R.id.btn_accept).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.tv_state).setVisibility(View.VISIBLE);
                ((TextView)baseViewHolder.getView(R.id.tv_state)).setText("已接受");
                ((TextView)baseViewHolder.getView(R.id.tv_state)).setBackground(mContext.getResources().getDrawable(R.drawable.frame_accept));
                ((TextView)baseViewHolder.getView(R.id.tv_state)).setTextColor(mContext.getResources().getColor(R.color.blue_1976d2));
                break;

            case "2":
                // 0 拒绝 1 接受 2 未处理 3 已失效
                baseViewHolder.getView(R.id.btn_accept).setVisibility(View.VISIBLE);
                baseViewHolder.getView(R.id.tv_state).setVisibility(View.GONE);
                ((Button)baseViewHolder.getView(R.id.btn_accept)).setText("接受邀请");
                break;

            case "3":
                baseViewHolder.getView(R.id.btn_accept).setVisibility(View.GONE);
                baseViewHolder.getView(R.id.tv_state).setVisibility(View.VISIBLE);
                ((TextView)baseViewHolder.getView(R.id.tv_state)).setText("已失效");
                ((TextView)baseViewHolder.getView(R.id.tv_state)).setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                ((TextView)baseViewHolder.getView(R.id.tv_state)).setTextColor(mContext.getResources().getColor(R.color.white));
                break;

            default:
                break;
        }

        // 处理接受邀请
        mBtnAccept = baseViewHolder.getView(R.id.btn_accept);
        baseViewHolder.getView(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入设备详情
                if(mOnAcceptDetailClickListener != null){
                    mOnAcceptDetailClickListener.onAcceptDetailClick(position);
                }
            }
        });

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

    public interface OnAcceptDetailClickListener {
        void onAcceptDetailClick(int position);
    }

    /**
    * 设备详情回调函数
    *
    * @param
    * @return
    */
    public void setOnAcceptDetailClickListener(OnAcceptDetailClickListener acceptDetailClickListener) {
        mOnAcceptDetailClickListener = acceptDetailClickListener;
    }

    /**
    * 获取待删除的消息
    *
    * @param
    * @return
    */
    public List<GetMsgListResponse> getmDeleteMsgShreList(){
        return mDeleteMsgList;
    }

    public Button getBtnAccept(){
        return mBtnAccept;
    }

    public void setIsAccept(Boolean isAccept){
        notifyDataSetChanged();
    }

}
