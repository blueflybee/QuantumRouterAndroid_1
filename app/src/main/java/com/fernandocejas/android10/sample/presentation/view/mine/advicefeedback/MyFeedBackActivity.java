package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityMyFeedbackBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMineComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MineModule;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.addrouter.AddRouterActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.deviceshare.RouterListActivity;
import com.qtec.mapp.model.req.GetAdviceDetailRequest;
import com.qtec.mapp.model.req.MyAdviceRequest;
import com.qtec.mapp.model.rsp.FeedBackResponse;
import com.qtec.mapp.model.rsp.GetMyAdviceResponse;
import com.qtec.model.core.QtecEncryptInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 我的反馈
 *      version: 1.0
 * </pre>
 */

public class MyFeedBackActivity extends BaseActivity implements IGetAdviceDetailView,View.OnClickListener,IMyAdviceView{
    ActivityMyFeedbackBinding mAdviceBinding;
    private MyFeedBackAdapter mFeedBackAdapter;
    private List<GetMyAdviceResponse> mAdvices;
    private int mPosition;
    private PtrClassicFrameLayout mPtrFrame;
    private Boolean isHasNoMoreData = false;
    private int mPageNum = 0;//统计上拉加载的次数

    @Inject
    AdviceDetailPresenter mAdviceDetailPresenter;
    @Inject
    GetMyAdvicePresenter mMyAdvicePresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdviceBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_feedback);
        initTitleBar("我的反馈");
        initView();
        initializeInjector();
        initPresenter();
        dealWithRefresh();
    }

    private void dealWithRefresh() {
        //下拉刷新支持时间
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setDurationToClose(1000);
        mPtrFrame.disableWhenHorizontalMove(true);//解决横向滑动冲突
        //进入Activity自动下拉刷新
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                //queryQuestionList("0", "14");
                mPtrFrame.autoRefresh();
            }
        }, 300);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            // 上拉加载的回调方法
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //mPtrFrame.refreshComplete();
                        // queryQuestionList("" + (mPageNum * 14), "14");// 每次请求14条记录
                        if(isHasNoMoreData){
                            Toast.makeText(MyFeedBackActivity.this, "已加载所有数据，请稍候再试", Toast.LENGTH_SHORT).show();
                            mPtrFrame.refreshComplete();
                            return;
                        }else {
                            requestMyAdviceList(mAdvices.get(mAdvices.size()-1).getFeedbackUniqueKey(),"14");
                        }
                    }
                }, 1000);
            }

            // 下拉刷新的回调方法
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // mPtrFrame.autoLoadMore();
                        //mPtrFrame.refreshComplete();

                        if (mAdvices.size() == 0) {
                            mPageNum = 0;
                            requestMyAdviceList("-1","14");
                        } else {
                            mFeedBackAdapter.notifyDataSetChanged();
                            mPtrFrame.refreshComplete();
                        }

                    }
                }, 100);
            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                return super.checkCanDoLoadMore(frame, mAdviceBinding.lvQuestionsList, footer);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, mAdviceBinding.lvQuestionsList, header);
            }
        });
    }

    private void initView() {
        mPtrFrame = mAdviceBinding.ptrLayout;
        mAdvices = new ArrayList<>();
    }

    private void requestMyAdviceList(String feedbackUniqueKey,String pageSize ) {
        MyAdviceRequest request = new MyAdviceRequest();
        request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
        request.setFeedbackUniqueKey(feedbackUniqueKey);
        request.setPageSize(pageSize);
        QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
        encryptInfo.setData(request);
        mMyAdvicePresenter.getMyAdviceList(encryptInfo);
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
        mAdviceDetailPresenter.setView(this);
        mMyAdvicePresenter.setView(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.left_layout:
                this.finish();
                break;

            default:
                break;
        }
    }
/*
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPosition = position;
        GetAdviceDetailRequest request = new GetAdviceDetailRequest();
        request.setFeedbackUniqueKey( mAdvices.get(position).getFeedbackUniqueKey());
        QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
        encryptInfo.setData(request);
        mAdviceDetailPresenter.getAdviceDetail(encryptInfo);

    }*/

    @Override
    public Context getContext() {
        return null;
    }

    /**
     * 反馈详情
     *
     * @param
     * @return
     */
    @Override
    public void openAdviceDetail(FeedBackResponse<List<FeedBackResponse.ReplyContent>> response) {
        //有内容返回再进去
        Intent intent = new Intent();
        intent.putExtra(mNavigator.EXTR_ADVICE_DETAIL,(Serializable) response);
        intent.putExtra(mNavigator.EXTR_ADVICE_POTISION,mAdvices.get(mPosition).getFeedbackUniqueKey());

        mNavigator.navigateTo(this, MyAdviceDetailActivity.class, intent);
        //mNavigator.navigateTo(this, FeedBackAdviceActivity.class, intent);
    }

    /**
    * 反馈列表 返回
    *
    * @param
    * @return
    */
    @Override
    public void openMyAdvice(List<GetMyAdviceResponse> responses) {

        if(responses.size() == 0){
            //判断是否为空，为空说明刷新完成，没有更多数据了
            isHasNoMoreData = true;
        }else {
            isHasNoMoreData = false;
        }

        mAdvices.addAll(responses);
        if (mAdvices.size() == 0) {
            mPtrFrame.refreshComplete();
            //空视图
            mAdviceBinding.lvQuestionsList.setEmptyView(findViewById(R.id.include_my_advice_empty_view));
            findViewById(R.id.include_my_advice_empty_view).findViewById(R.id.btn_empty_feedBack).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNavigator.navigateTo(MyFeedBackActivity.this, FeedBackAdviceActivity.class,null);
                }
            });
            return;
        }

        if (mPageNum == 0) {
            mPageNum = 1;
            mFeedBackAdapter = new MyFeedBackAdapter(this,mAdvices, R.layout.item_my_question_list);
            mAdviceBinding.lvQuestionsList.setAdapter(mFeedBackAdapter);
            mPtrFrame.refreshComplete();
        } else {
            mPageNum++;
            mFeedBackAdapter.notifyDataSetChanged();
            mPtrFrame.refreshComplete();
        }


        mFeedBackAdapter.setOnAdviceDetailClickListener(new MyFeedBackAdapter.OnAdviceDetailClickListener() {
            @Override
            public void onAdviceDetailClick(int position) {
                mPosition = position;
                GetAdviceDetailRequest request = new GetAdviceDetailRequest();
                request.setFeedbackUniqueKey( mAdvices.get(position).getFeedbackUniqueKey());
                QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
                encryptInfo.setData(request);
                mAdviceDetailPresenter.getAdviceDetail(encryptInfo);
            }
        });

    }
}
