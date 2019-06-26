package com.fernandocejas.android10.sample.presentation.view.device.router.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.qtec.router.model.rsp.GetVpnListResponse;

import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/22
 *     desc   : 网关基本信息
 *     version: 1.0
 * </pre>
 */
public class VPNAdapter extends CommAdapter1<GetVpnListResponse.VpnBean> {
  OnVpnDetailClickListener mOnVpnDetailClickListener;
  OnVpnConnectClickListener mOnVpnConnectClickListener;
  private int mPosition = 0;
  private Context mContext;
  private Boolean isConnecting = false;
  private String selectedIfName = "";

  public VPNAdapter(Context context, List<GetVpnListResponse.VpnBean> data, int layoutId,String selectedIfName,Boolean isConnecting) {
    super(context, data, layoutId);
    mContext = context;
    this.selectedIfName = selectedIfName;
    this.isConnecting = isConnecting;
  }

  @Override
  public void convert(BaseViewHolder baseViewHolder, GetVpnListResponse.VpnBean response, int postition) {
    TextView status = (TextView) baseViewHolder.getView(R.id.tv_status);

    TextView name = (TextView) baseViewHolder.getView(R.id.tv_vpn_name);
    LinearLayout detail = (LinearLayout) baseViewHolder.getView(R.id.ll_vpn_detail);
    ImageView img = (ImageView) baseViewHolder.getView(R.id.img_vpn_item);
    RelativeLayout rlVpnDetail = (RelativeLayout) baseViewHolder.getView(R.id.rl_vpn_detail);
    /*LinearLayout llVpnItem = (LinearLayout) baseViewHolder.getView(R.id.ll_vpn_item);*/

    name.setText(response.getDescription());

    if("down".equals(response.getStatus())){
      if(isConnecting){
        status.setText("连接中");

        name.setTextColor(mContext.getResources().getColor(R.color.black_424242));
        if(selectedIfName.equals(response.getIfname())) {
          status.setVisibility(View.VISIBLE);
          img.setVisibility(View.VISIBLE);
        }else {
          status.setVisibility(View.GONE);
          img.setVisibility(View.GONE);
        }
      }else {
        status.setText("未连接");
        name.setTextColor(mContext.getResources().getColor(R.color.black_424242));
        status.setVisibility(View.GONE);
      }

    }else if("up".equals(response.getStatus())){
      status.setText("已连接");
      img.setVisibility(View.VISIBLE);
      status.setVisibility(View.VISIBLE);
      name.setTextColor(mContext.getResources().getColor(R.color.blue_2196f3));
    }

    if(!isConnecting){
      if(response.getEnable() == 1){
        status.setVisibility(View.VISIBLE);
        img.setVisibility(View.VISIBLE);
        name.setTextColor(mContext.getResources().getColor(R.color.blue_2196f3));
      }else {
        img.setVisibility(View.GONE);
        status.setVisibility(View.GONE);
      }
    }

    detail.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(mOnVpnConnectClickListener != null){
          mOnVpnConnectClickListener.onVpnConnectClick(v,postition);
        }
      }
    });

    rlVpnDetail.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //进入设备详情
        if(mOnVpnDetailClickListener != null){
          mOnVpnDetailClickListener.onVpnDetailClick(postition);
        }
      }
    });

  }

  public interface OnVpnDetailClickListener {
    void onVpnDetailClick(int position);
  }

  public interface OnVpnConnectClickListener {
    void onVpnConnectClick(View v,int position);
  }

  /**
   * 设备详情回调函数
   *
   * @param
   * @return
   */
  public void setOnVpnDetailClickListener(OnVpnDetailClickListener vpnDetailClickListener) {
    mOnVpnDetailClickListener = vpnDetailClickListener;
  }

  public void setOnVpnConnectClickListener(OnVpnConnectClickListener vpnConnectClickListener) {
    mOnVpnConnectClickListener = vpnConnectClickListener;
  }

  public void updateSelectedItem(int positon){
    mPosition = positon;
    notifyDataSetChanged();
  }
}
