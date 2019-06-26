package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAcceptInvitationBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMessageComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.ActivityModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MessageModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.login.login.LoginActivity;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.fernandocejas.android10.sample.presentation.view.message.data.GetAcceptContent;
import com.google.gson.Gson;
import com.qtec.mapp.model.req.DealInvitationRequest;
import com.qtec.mapp.model.rsp.DealInvitationResponse;
import com.qtec.model.core.QtecEncryptInfo;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/30
 *      desc: 首页接受邀请弹窗
 *      version: 1.0
 * </PRE>
 */

public class AcceptInvitationActivity extends Activity implements IDealInvitationView {
    private ActivityAcceptInvitationBinding mAcceptBinding;
    private CPushMessage pushMsg;
    private GetAcceptContent mAcceptContent;
    private  YunOsMessageReceiver.OnRefreshMsgListener mOnRefresgMsgListener;

    @Inject
    PostMsgInvitationPresenter mPostMsgInvitationPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAcceptBinding = DataBindingUtil.setContentView(this, R.layout.activity_accept_invitation);
        getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

        setFinishOnTouchOutside(false);

        initializeInjector();
        initPresenter();
        pushMsg = (CPushMessage) getIntent().getParcelableExtra("ACCEPT_INVITATION_FLAG");
        initData();
    }

    private void initData() {

        String flag = pushMsg.getTitle().substring(0,2);
        String title = pushMsg.getTitle().substring(2);
        // 01 分享  00 多端登录  02 消息刷新
        if(flag.equals("01")){

            try{
                //此处解析Json字符串 Content
                if(!TextUtils.isEmpty(pushMsg.getContent())){
                    Gson gson = new Gson();
                    mAcceptContent = gson.fromJson(pushMsg.getContent(), GetAcceptContent.class);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            findViewById(R.id.ll_inviteBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_loginOut).setVisibility(View.GONE);
            findViewById(R.id.rl_close).setVisibility(View.VISIBLE);

            try{
                if(!TextUtils.isEmpty(title)){
                    mAcceptBinding.tvTitle.setText(title);
                }

                if(!TextUtils.isEmpty(mAcceptContent.getBodyContent())){
                    mAcceptBinding.tvContent.setText(mAcceptContent.getBodyContent());
                }
            }catch(Exception e){
                e.printStackTrace();
            }


            findViewById(R.id.btn_ingore).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AcceptInvitationActivity.this.finish();
                }
            });

            findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //处理邀请
                    DealInvitationRequest request = new DealInvitationRequest();
                    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
                    request.setHistoryUniqueKey(mAcceptContent.getHistoryUniqueKey());
                    request.setHandleType("1");  //处理
                    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
                    encryptInfo.setData(request);
                    mPostMsgInvitationPresenter.dealInvitation(encryptInfo);
                }
            });

            findViewById(R.id.rl_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else{
            try{
                if(!TextUtils.isEmpty(title)){
                    mAcceptBinding.tvTitle.setText(title);
                }

                if(!TextUtils.isEmpty(pushMsg.getContent())){
                    mAcceptBinding.tvContent.setText(pushMsg.getContent());
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            findViewById(R.id.ll_inviteBtn).setVisibility(View.GONE);
            findViewById(R.id.rl_close).setVisibility(View.GONE);
            findViewById(R.id.btn_loginOut).setVisibility(View.VISIBLE);

            findViewById(R.id.btn_loginOut).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*startActivity(new Intent(AcceptInvitationActivity.this, LoginActivity.class));
                    AcceptInvitationActivity.this.finish();*/
                    //重复登出操作
                    PrefConstant.putAppToken("");
                    ((AndroidApplication) getApplication()).unbindBleService();
                    new Navigator().navigateNewAndClearTask(AcceptInvitationActivity.this, LoginActivity.class);
                    /*AcceptInvitationActivity.this.finish();*/
                }
            });
        }

        /*findViewById(R.id.rl_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptInvitationActivity.this.finish();
            }
        });*/

    }

    private void initializeInjector() {
        DaggerMessageComponent.builder()
                .applicationComponent(((AndroidApplication) getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .messageModule(new MessageModule())
                .build()
                .inject(this);
    }

    private void initPresenter() {
        mPostMsgInvitationPresenter.setView(this);
    }

    @Override
    public void dealInvitation(DealInvitationResponse response) {
        //Toast.makeText(this, "接受了邀请", Toast.LENGTH_SHORT).show();
        //mOnRefresgMsgListener.refreshMsg();//刷新消息白点
       // new Navigator().navigateExistAndClearTop(getContext(), MainActivity.class);
        /*this.finish();*/
        startActivity(new Intent(getContext(),MainActivity.class));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(String message) {
        this.finish();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showLoginInvalid() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPostMsgInvitationPresenter.destroy();
    }

    @Override
    public void onBackPressed() {}
}
