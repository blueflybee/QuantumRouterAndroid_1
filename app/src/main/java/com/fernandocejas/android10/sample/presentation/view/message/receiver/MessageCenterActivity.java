package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityMessageCenterBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMessageComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MessageModule;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.mapp.model.rsp.GetMsgCountResponse;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 通知中心
 *      version: 1.0
 * </pre>
 */

public class MessageCenterActivity extends BaseActivity implements View.OnClickListener,IGetMsgCountView{
    private ActivityMessageCenterBinding mMessageCenterBinding;
    private int REQUEST_CODE_MSG = 11;
    private int REQUEST_CODE_NOTICE = 12;

    @Inject
    GetMsgCountPresenter mMsgCountPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessageCenterBinding = DataBindingUtil.setContentView(this, R.layout.activity_message_center);
        initTitleBar("通知中心");

        initializeInjector();
        initPresenter();

        initView();

        quaryMsgCount();


    }

    private void quaryMsgCount() {
        mMsgCountPresenter.getMsgCount();
    }

    private void initView() {
        mMessageCenterBinding.rlDeviceShare.setOnClickListener(this);
        mMessageCenterBinding.rlOtherNotification.setOnClickListener(this);
    }

    private void initializeInjector() {
        DaggerMessageComponent.builder()
            .applicationComponent(getApplicationComponent())
            .activityModule(getActivityModule())
            .messageModule(new MessageModule())
            .build()
            .inject(this);
    }

    private void initPresenter() {
        mMsgCountPresenter.setView(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent0 = new Intent();
        intent0.putExtra("Extra",12);
        setResult(12,intent0);

        switch (v.getId()) {
            case R.id.rl_deviceShare:
                Intent intent = new Intent(this,ShareMessageListActivity.class);
                startActivityForResult(intent,REQUEST_CODE_MSG);
                break;

            case R.id.rl_otherNotification:
                Intent intent1 = new Intent(this,OtherMessageListActivity.class);
                startActivityForResult(intent1,REQUEST_CODE_NOTICE);
                break;

            default:
                break;
        }
    }

    /**
    * 统计新消息数量
    *
    * @param
    * @return
    */
    @Override
    public void getMsgCount(GetMsgCountResponse response) {
        if("0".equals(response.getUnHandleNum())){
            mMessageCenterBinding.tvMsgCount.setVisibility(View.GONE);
        }else{
            mMessageCenterBinding.tvMsgCount.setVisibility(View.VISIBLE);
        }

        if("0".equals(response.getUnReadMsg())){
            mMessageCenterBinding.tvNoticeCount.setVisibility(View.GONE);
        }else {
            mMessageCenterBinding.tvNoticeCount.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /* if(requestCode == REQUEST_CODE_MSG){
            mMessageCenterBinding.tvMsgCount.setVisibility(View.GONE);
        }else if(requestCode == REQUEST_CODE_NOTICE){
            mMessageCenterBinding.tvNoticeCount.setVisibility(View.GONE);
        }*/
        quaryMsgCount();
    }
}

