package com.fernandocejas.android10.sample.presentation.view.device.router.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.TimeUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ItemRouterStatusClientBinding;
import com.fernandocejas.android10.sample.presentation.utils.DateUtil;
import com.qtec.router.model.rsp.RouterStatusResponse;
import com.qtec.router.model.rsp.RouterStatusResponse.Status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/22
 *     desc   : 网关状态界面下属客户端列表adpter
 *     version: 1.0
 * </pre>
 */
public class RouterStatusClientAdapter extends RecyclerView.Adapter<RouterStatusClientAdapter.ViewHolder> {

    private Context mContext;
    private List<Status> mStatuses = new ArrayList<>();

    public RouterStatusClientAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        ItemRouterStatusClientBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_router_status_client, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mStatuses.get(position));
        if(mStatuses.get(position).getStastatus() == 0){
            //offline
            holder.binding.ivConnectStatus.setVisibility(View.GONE);
            holder.binding.tvClientStatus.setText("已离线");
            holder.binding.tvClientTime.setText("");

        }else {
          /*  if(mStatuses.get(position).getRx() < 1024){
                //mBinding.tvRouterDownspeed.setText(String.format("%.2f",Float.parseFloat((response.getRouterrx()*8f/1024)+"")) + " kb/s");
                holder.binding.tvClientStatus.setText(String.format("%.2f",Float.parseFloat((mStatuses.get(position).getRx()*8f/1024)+"")) + " kb/s");

            }else if(mStatuses.get(position).getRx() < 1024*1024){
                holder.binding.tvClientStatus.setText(String.format("%.2f",Float.parseFloat((mStatuses.get(position).getRx()*8f/1024/1024)+"")) + " Mb/s");
            }else{
                holder.binding.tvClientStatus.setText((mStatuses.get(position).getRx()/1024/1024)+"G/s");
            }*/

            holder.binding.tvClientStatus.setText(String.format("%.2f",Float.parseFloat((mStatuses.get(position).getRx()*1f/1024)+"")) + " KB/s");

            long onLineTime = TimeUtils.getNowMills() - (long)mStatuses.get(position).getStastatus()*1000;//上线时间,返回的是秒

            holder.binding.tvClientTime.setText(DateUtil.convertDateToDiaplayTimeForOnLineTime(TimeUtils.millis2Date(onLineTime)));

        }

        switch (mStatuses.get(position).getDevicetype()) {
            case 0:
                //computer
                holder.binding.ivClientIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_mac_blue));
                break;

            case 1:
                //android
                holder.binding.ivClientIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_phone));
                break;

            case 2:
                // iphone
                holder.binding.ivClientIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_phone));
                break;

            case 3:
                // iphone
                holder.binding.ivClientIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_phone));
                break;

            default:
                break;
        }

        if(TextUtils.isEmpty(holder.binding.tvClientName.getText())){
            holder.binding.tvClientName.setText("unknow");
            if(mStatuses.get(position).getStastatus() == 0){
                //离线
                holder.binding.ivClientIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_unknow_black));
            }else {
                holder.binding.ivClientIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_unknow_blue));
            }

        }

        if("unknow".equals(mStatuses.get(position).getStaname())){
            //未知设备
            if(mStatuses.get(position).getStastatus() == 0){
                //离线
                holder.binding.ivClientIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_unknow_black));
            }else {
                holder.binding.ivClientIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_unknow_blue));
            }
        }

        try {
            if((!TextUtils.isEmpty(AppConstant.getMacAddress(mContext))) && (!(TextUtils.isEmpty(mStatuses.get(position).getMacaddr())))){
                if(AppConstant.getMacAddress(mContext).equalsIgnoreCase(mStatuses.get(position).getMacaddr())){
                    holder.binding.tvClientLocal.setVisibility(View.VISIBLE);
                }else{
                    holder.binding.tvClientLocal.setVisibility(View.GONE);
                }
            }else {
                holder.binding.tvClientLocal.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mStatuses.size();
    }

    public void update(List<Status> statuses) {
        if (statuses == null || statuses.isEmpty()) return;
        mStatuses.clear();
        mStatuses.addAll(statuses);

        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private final ItemRouterStatusClientBinding binding;

        public ViewHolder(ItemRouterStatusClientBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Status status) {
            binding.setStatus(status);
            binding.executePendingBindings();
        }
    }

    public final static String getSystemDate() {
        // 完整显示日期时间
        String str = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date());
        return str;
    }
}
