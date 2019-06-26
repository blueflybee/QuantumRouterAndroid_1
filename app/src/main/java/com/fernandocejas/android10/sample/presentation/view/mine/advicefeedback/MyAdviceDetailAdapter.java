package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.utils.GlideUtil;

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

public class MyAdviceDetailAdapter extends BaseAdapter {
  private Context context;
  public static interface IMsgViewType {
    int IMVT_COM_MSG = 0;// 收到对方的消息
    int IMVT_TO_MSG = 1;// 自己发送出去的消息
  }

  private static final int ITEMCOUNT = 2;// 消息类型的总数
  private List<ChatMsgEntity> coll;// 消息对象数组
  private LayoutInflater mInflater;

  public MyAdviceDetailAdapter(Context context, List<ChatMsgEntity> coll) {
    this.coll = coll;
    mInflater = LayoutInflater.from(context);
    this.context = context;
  }

  public int getCount() {
    return coll.size();
  }

  public Object getItem(int position) {
    return coll.get(position);
  }

  public long getItemId(int position) {
    return position;
  }

  /**
   * 得到Item的类型，是对方发过来的消息，还是自己发送出去的
   */
  public int getItemViewType(int position) {
    ChatMsgEntity entity = coll.get(position);

    if (entity.getMsgType()) {//收到的消息
      return IMsgViewType.IMVT_COM_MSG;
    } else {//自己发送的消息
      return IMsgViewType.IMVT_TO_MSG;
    }
  }

  /**
   * Item类型的总数
   */
  public int getViewTypeCount() {
    return ITEMCOUNT;
  }

  public View getView(int position, View convertView, ViewGroup parent) {

    ChatMsgEntity entity = coll.get(position);
    boolean isComMsg = entity.getMsgType();

    ViewHolder viewHolder = null;
    if (convertView == null) {
      if (isComMsg) {
        convertView = mInflater.inflate(
            R.layout.item_feedback_left, null);
      } else {
        convertView = mInflater.inflate(
            R.layout.item_feedback_right, null);
      }

      viewHolder = new ViewHolder();
           viewHolder.tvSendTime = (TextView) convertView
                .findViewById(R.id.tv_sendtime);
      viewHolder.headImg = (ImageView) convertView
          .findViewById(R.id.img_head_dialog);
            /*viewHolder.tvUserName = (TextView) convertView
                .findViewById(R.id.tv_username);*/
      viewHolder.tvContent = (TextView) convertView
          .findViewById(R.id.tv_chatcontent);
      viewHolder.isComMsg = isComMsg;

      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    /*    viewHolder.tvSendTime.setText(entity.getDate());
        viewHolder.tvUserName.setText(entity.getName());*/
    viewHolder.tvSendTime.setText(entity.getDate());
    viewHolder.tvContent.setText(entity.getMessage());

    if(!coll.get(position).getMsgType()){
      //set right head img
      GlideUtil.loadCircleHeadImage(context, PrefConstant.getUserHeadImg(),viewHolder.headImg);
    }

    return convertView;
  }

  static class ViewHolder {
    public TextView tvSendTime;
    /*public TextView tvUserName;*/
    public ImageView headImg;
    public TextView tvContent;
    public boolean isComMsg = true;
  }

}