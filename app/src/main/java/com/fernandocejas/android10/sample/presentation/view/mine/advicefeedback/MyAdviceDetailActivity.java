package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityMyAdviceDetailBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMineComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MineModule;
import com.fernandocejas.android10.sample.presentation.utils.Base64Util;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.qtec.mapp.model.rsp.FeedBackResponse;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.mapp.model.req.UpdateFeedBackRequest;
import com.qtec.mapp.model.rsp.GetUpdateFeedBackResponse;

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
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class MyAdviceDetailActivity extends BaseActivity implements View.OnClickListener,IUpdateFeedBackView {
    private ActivityMyAdviceDetailBinding mMyAdviceDetailBinding;
    FeedBackResponse<List<FeedBackResponse.ReplyContent>> mAdviceDetails;
    private String mUniqueKey;
    private List<ChatMsgEntity> mDetailList;
    private MyAdviceDetailAdapter mAdapter;

    @Inject
    UpdateFeedBackPresenter updateFeedBackPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyAdviceDetailBinding = DataBindingUtil.setContentView(this,R.layout.activity_my_advice_detail);
        initTitleBar("反馈详情");
        initView();
        initializeInjector();
        initPresenter();
        initData();
    }

    private void initView() {
        mDetailList = new ArrayList<>();
        //mMyAdviceDetailBinding.etSendmessage1.addTextChangedListener(textWatcher);
        mMyAdviceDetailBinding.btnSend1.setOnClickListener(this);

        InputWatcher watcher = new InputWatcher();
        watcher.addEt(mMyAdviceDetailBinding.etSendmessage1);
        watcher.setInputListener(isEmpty -> {
            mMyAdviceDetailBinding.btnSend1.setClickable(!isEmpty);
            if (isEmpty) {
                mMyAdviceDetailBinding.btnSend1.setBackgroundColor(getResources().getColor(R.color.gray));
            } else {
                mMyAdviceDetailBinding.btnSend1.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
            }
        });
    }
    private void initData() {
        mDetailList.clear();

        // 获取反馈详情
        mAdviceDetails = (FeedBackResponse<List<FeedBackResponse.ReplyContent>>) getIntent().getSerializableExtra(mNavigator.EXTR_ADVICE_DETAIL);
        mUniqueKey = (String) getIntent().getSerializableExtra(mNavigator.EXTR_ADVICE_POTISION);



        for (int i = 0; i < mAdviceDetails.getReplyContent().size(); i++) {
            ChatMsgEntity entity = new ChatMsgEntity();
            if("0".equals(mAdviceDetails.getReplyContent().get(i).getType())){
                entity.setMsgType(true); //收到的消息 left
            }else{
                entity.setMsgType(false); //提问的消息 right
            }

            entity.setDate(mAdviceDetails.getReplyContent().get(i).getTime());

            // 含有表情的富文本发送之前Utf-8编码过，所以要解码
            String tempContent = "";
            try {

                tempContent =Base64Util.decodeBase64(mAdviceDetails.getReplyContent().get(i).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }

            entity.setMessage(tempContent);
            entity.setUniqueKey("");
            mDetailList.add(entity);
        }

        mAdapter = new MyAdviceDetailAdapter(this, mDetailList);
        mMyAdviceDetailBinding.lvMyAdvice.setAdapter(mAdapter);
        //显示键盘
        //showSoftKeyBoard();
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
        updateFeedBackPresenter.setView(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send1:
                hideSoftKeyBoard();
                // 输入表情之后EditText做了自动识别，发送到服务端之前先编码
                String sendUpdateContent = "";
                try {
                    sendUpdateContent = Base64Util.encodeBase64(getText(mMyAdviceDetailBinding.etSendmessage1));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                send();// 展示提问

                UpdateFeedBackRequest request = new UpdateFeedBackRequest();
                request.setFeedbackUniqueKey(mUniqueKey);
                request.setFeedbackContent(sendUpdateContent);
                QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
                encryptInfo.setData(request);
                updateFeedBackPresenter.updateFeedBack(encryptInfo);

                break;

            default:
                break;
        }
    }

    @Override
    public void updateFeedback(GetUpdateFeedBackResponse response) {
        ChatMsgEntity entity = new ChatMsgEntity();

        entity.setMessage(Base64Util.decodeBase64(response.getReplyContent()));
        entity.setUniqueKey("");
        entity.setDate(getDate());
        entity.setMsgType(true);//接收消息
        mDetailList.add(entity);
        mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变

        mMyAdviceDetailBinding.lvMyAdvice.setSelection(mMyAdviceDetailBinding.lvMyAdvice.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项

    }


    /**
     * 发送消息
     */
    private void send() {
        if (!TextUtils.isEmpty(getText(mMyAdviceDetailBinding.etSendmessage1))) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(getDate());
            entity.setMessage(getText(mMyAdviceDetailBinding.etSendmessage1));
            entity.setMsgType(false);//发送消息
            mDetailList.add(entity);
            mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变

            mMyAdviceDetailBinding.etSendmessage1.setText("");

            mMyAdviceDetailBinding.lvMyAdvice.setSelection(mMyAdviceDetailBinding.lvMyAdvice.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
        }
    }

    private String getDate() {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(currentTime);
        return formatter.format(date);
    }
}
