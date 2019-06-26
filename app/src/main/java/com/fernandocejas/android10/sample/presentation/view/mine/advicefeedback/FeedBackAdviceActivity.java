package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityFeedBackAdviceBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMineComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MineModule;
import com.fernandocejas.android10.sample.presentation.utils.Base64Util;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.mine.component.FeedBackPopupWindow;
import com.qtec.mapp.model.req.PostFeedBackRequest;
import com.qtec.mapp.model.req.UpdateFeedBackRequest;
import com.qtec.mapp.model.rsp.GetUpdateFeedBackResponse;
import com.qtec.mapp.model.rsp.GetfeedBackResponse;
import com.qtec.model.core.QtecEncryptInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc:　我要反馈
 *      version: 1.0
 * </pre>
 */

public class FeedBackAdviceActivity extends BaseActivity implements View.OnClickListener, IPostFeedBackView, IUpdateFeedBackView {
  private ActivityFeedBackAdviceBinding mFeedBackAdviceBinding;
  private int count = 0;  //统计点击次数，1代表首次，2以上代表更新反馈
  private String mFeedbackUniqueKey;
  private List<ChatMsgEntity> mAdviceList;
  private FeedBackAdviceAdapter mAdapter;
  private String phoneNum = "";

  @Inject
  PostFeedBackPresenter postFeedBackPresenter;
  @Inject
  UpdateFeedBackPresenter updateFeedBackPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mFeedBackAdviceBinding = DataBindingUtil.setContentView(this, R.layout.activity_feed_back_advice);
    initTitleBar("意见反馈");
    initView();
    initializeInjector();
    initPresenter();

    initData();
  }

  private void initView() {
    mAdviceList = new ArrayList<>();
//    mFeedBackAdviceBinding.etSendmessage.addTextChangedListener(textWatcher);
    mFeedBackAdviceBinding.btnSend.setOnClickListener(this);

    //控制发送按钮是否可点击
    InputWatcher watcher = new InputWatcher();
    watcher.addEt(mFeedBackAdviceBinding.etSendmessage);
    watcher.setInputListener(isEmpty -> {
      mFeedBackAdviceBinding.btnSend.setClickable(!isEmpty);
      if (isEmpty) {
        mFeedBackAdviceBinding.btnSend.setBackgroundColor(getResources().getColor(R.color.gray));
      } else {
        mFeedBackAdviceBinding.btnSend.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });
  }

  private void initializeInjector() {
    DaggerMineComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .mineModule(new MineModule())
        .build()
        .inject(this);

  }

  private void initPresenter() {
    postFeedBackPresenter.setView(this);
    updateFeedBackPresenter.setView(this);
  }

  private void initData() {
    mAdviceList.clear();
    //初始化数据
    ChatMsgEntity entity = new ChatMsgEntity();
    entity.setMessage("您好，请简要描述您的问题或建议");
    entity.setUniqueKey("");
    entity.setDate(getDate());
    entity.setMsgType(true);//接收消息
    mAdviceList.add(entity);
    mAdapter = new FeedBackAdviceAdapter(this, mAdviceList);
    mFeedBackAdviceBinding.lvFeedBackAdvice.setAdapter(mAdapter);
    //显示键盘
    //showSoftKeyBoard();
  }

  /**
   * 获得焦点并弹出软键盘
   *
   */
  private void showSoftKeyBoard() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        KeyboardUtils.showSoftInput(mFeedBackAdviceBinding.etSendmessage);
      }
    }, 500);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_send:
        final FeedBackPopupWindow mFeedBackPopWin;

        if (count == 0) {
          mFeedBackPopWin = new FeedBackPopupWindow(FeedBackAdviceActivity.this);
          mFeedBackPopWin.setFocusable(true);
          mFeedBackPopWin.showAtLocation(mFeedBackPopWin.getOuterLayout(), Gravity.NO_GRAVITY, 0, 0);

          mFeedBackPopWin.setOnPositiveClickListener(new FeedBackPopupWindow.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
              hideSoftKeyBoard();

              phoneNum = getText(mFeedBackPopWin.getPhoneNumEdit());

              SPUtils spUtils = new SPUtils(PrefConstant.SP_NAME);
              String userUniqueKey = spUtils.getString(PrefConstant.SP_USER_UNIQUE_KEY);
              // 输入表情之后EditText做了自动识别，发送到服务端之前先编码
              String sendAdviceContent = "";
              try {

                sendAdviceContent = Base64Util.encodeBase64(getText(mFeedBackAdviceBinding.etSendmessage));
              } catch (Exception e) {
                e.printStackTrace();
              }

              send(); //展示提问
              //请求反馈
              count++;
              PostFeedBackRequest request = new PostFeedBackRequest();
              // request.setFeedbackContent(adviceContent);
              request.setFeedbackContent(sendAdviceContent);
              request.setUserContact(Base64Util.encodeBase64(phoneNum));
              request.setUserUniqueKey(userUniqueKey);
              QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
              encryptInfo.setData(request);
              postFeedBackPresenter.getFeedBack(encryptInfo);


            }
          });
        } else if (count >= 1) {
          //更新反馈

          // 输入表情之后EditText做了自动识别，发送到服务端之前先编码
          String sendUpdateContent = "";
          try {
            sendUpdateContent = Base64Util.encodeBase64(getText(mFeedBackAdviceBinding.etSendmessage));
          } catch (Exception e) {
            e.printStackTrace();
          }

          send();//展示反馈
          //请求更新
          UpdateFeedBackRequest request = new UpdateFeedBackRequest();
          request.setFeedbackUniqueKey(mFeedbackUniqueKey);
          request.setFeedbackContent(sendUpdateContent);
          QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
          encryptInfo.setData(request);
          updateFeedBackPresenter.updateFeedBack(encryptInfo);
        }

        //KeyboardUtils.hideSoftInput(this);//隐藏键盘
        hideSoftKeyBoard();

        break;

      default:
        break;
    }
  }


  /**
   * 提交反饋 成功
   *
   * @param
   * @return
   */
  @Override
  public void openFeedback(GetfeedBackResponse response) {
    if ((TextUtils.isEmpty(response.getFeedbackUniqueKey())) && (TextUtils.isEmpty(response.getReplyContent()))) {
      return;
    }

    mFeedbackUniqueKey = response.getFeedbackUniqueKey();

    //第一次提交返回第一条消息
    ChatMsgEntity entity1 = new ChatMsgEntity();

    entity1.setMessage(Base64Util.decodeBase64(response.getReplyContent()));
    entity1.setUniqueKey(response.getFeedbackUniqueKey());
    entity1.setDate(getDate());
    entity1.setMsgType(true);//接收消息
    mAdviceList.add(entity1);
    //第一次提交返回第二条消息
    ChatMsgEntity entity2 = new ChatMsgEntity();
    entity2.setMessage("您的联系方式为：" + phoneNum);
    entity2.setUniqueKey("");
    entity2.setDate(getDate());
    entity2.setMsgType(true);//接收消息
    mAdviceList.add(entity2);

    mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变

   /* showSoftKeyBoard();
    KeyboardUtils.hideSoftInput(this);*/


  }

  private String getDate() {
    long currentTime = System.currentTimeMillis();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date date = new Date(currentTime);
    return formatter.format(date);
  }

  /**
   * 更新反馈 成功
   *
   * @param
   * @return
   */
  @Override
  public void updateFeedback(GetUpdateFeedBackResponse response) {
    if (TextUtils.isEmpty(response.getReplyContent())) {
      return;
    }

    ChatMsgEntity entity = new ChatMsgEntity();
    entity.setMessage(Base64Util.decodeBase64(response.getReplyContent()));
    entity.setUniqueKey("");
    entity.setDate(getDate());
    entity.setMsgType(true);//接收消息
    mAdviceList.add(entity);
    mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变

    mFeedBackAdviceBinding.lvFeedBackAdvice.setSelection(mFeedBackAdviceBinding.lvFeedBackAdvice.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项

  }

  /**
   * 发送消息
   */
  private void send() {
    if (!TextUtils.isEmpty(getText(mFeedBackAdviceBinding.etSendmessage))) {
      ChatMsgEntity entity = new ChatMsgEntity();
      entity.setDate(getDate());
      entity.setMessage(getText(mFeedBackAdviceBinding.etSendmessage));
      entity.setMsgType(false);//发送消息
      mAdviceList.add(entity);
      mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变

      mFeedBackAdviceBinding.etSendmessage.setText("");

      mFeedBackAdviceBinding.lvFeedBackAdvice.setSelection(mFeedBackAdviceBinding.lvFeedBackAdvice.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
    }
  }


}
