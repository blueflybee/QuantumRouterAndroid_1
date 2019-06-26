package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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

public class FeedBackAdviceAdapter extends BaseAdapter {
  private Context context;

  public static interface IMsgViewType {
    int IMVT_COM_MSG = 0;// 收到对方的消息
    int IMVT_TO_MSG = 1;// 自己发送出去的消息
  }

  private static final int ITEMCOUNT = 2;// 消息类型的总数
  private List<ChatMsgEntity> coll;// 消息对象数组
  private LayoutInflater mInflater;

  public FeedBackAdviceAdapter(Context context, List<ChatMsgEntity> coll) {
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

    if(position == 2){
      //设置我的反馈可点击
      TextView view = viewHolder.tvContent;
      String str = coll.get(2).getMessage();
      String cliclTxt = "我的反馈";
      SpannableStringBuilder spannable = new SpannableStringBuilder(str);
      int startIndex = str.indexOf(cliclTxt);
      int endIndex = startIndex + cliclTxt.length();
      //文字点击
      if (startIndex < 0) startIndex = 0;
      spannable.setSpan(new TextClick(),startIndex,endIndex
          , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      //一定要记得设置，不然点击不生效
      view.setMovementMethod(LinkMovementMethod.getInstance());
      view.setText(spannable);
      view.setHighlightColor(Color.TRANSPARENT);//设置点击后的颜色为透明
    }

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

  public class TextClick extends ClickableSpan {

    @Override
    public void onClick(View widget) {
      //在此处理点击事件
      Intent intent = new Intent(context,MyFeedBackActivity.class);
      context.startActivity(intent);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
      ds.setColor(context.getResources().getColor(R.color.blue_1194f6));
      ds.setUnderlineText(true);
    }
  }

}
