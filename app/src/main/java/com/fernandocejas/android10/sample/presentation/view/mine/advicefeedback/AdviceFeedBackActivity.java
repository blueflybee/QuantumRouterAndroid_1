package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAdviceFeedbackBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMineComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MineModule;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.mapp.model.req.GetQuestionDetailRequest;
import com.qtec.mapp.model.req.GetQuestionListRequest;
import com.qtec.mapp.model.rsp.GetQuestionDetailResponse;
import com.qtec.mapp.model.rsp.GetQuestionListResponse;
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
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class AdviceFeedBackActivity extends BaseActivity implements IGetQuestionListView, IGetQuestionDetailView,View.OnClickListener, AdapterView.OnItemClickListener {
  ActivityAdviceFeedbackBinding adviceBinding;
  private PtrClassicFrameLayout mPtrFrame;
  private int mPageNum = 0;//统计上拉加载的次数

  @Inject
  QuestionDetailPresenter mQuestionDetailPresenter;
  @Inject
  GetQuestionListPresenter mGetQuestionListPresenter;

  private AdviceFeedBackAdapter mAdviceAdapter;
  private List<GetQuestionListResponse> mQuestions;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    adviceBinding = DataBindingUtil.setContentView(this, R.layout.activity_advice_feedback);
    initTitleBar("用户反馈");
    initView();
    initializeInjector();
    initPresenter();
    //常见问题不刷新 全部问题进行刷新
    queryQuestionList("0", "10");// 每次请求10条记录
   // dealWithRefresh();

    mTitleBar.setRightAs("我的反馈", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mNavigator.navigateTo(AdviceFeedBackActivity.this, MyFeedBackActivity.class, null);
      }
    });
  }

  private void initView() {
    mQuestions = new ArrayList<>();
    mPtrFrame = adviceBinding.ptrLayout;
    adviceBinding.llAllQuestion.setOnClickListener(this);
    adviceBinding.llFeedBackAdvice.setOnClickListener(this);
    adviceBinding.lvQuestionsList.setOnItemClickListener(this);
  }

  private void queryQuestionList(String start, String pageSize) {

    GetQuestionListRequest request = new GetQuestionListRequest();
    request.setStart(start);
    request.setPageSize(pageSize);
    request.setFaqUniqueKey("-1");
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);
    mGetQuestionListPresenter.getQuestionList(encryptInfo);
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
    mGetQuestionListPresenter.setView(this);
    mQuestionDetailPresenter.setView(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.ll_allQuestion:
        mNavigator.navigateTo(this, AllQuestionActivity.class, null);
        break;

      // 我要反馈
      case R.id.ll_FeedBackAdvice:
        mNavigator.navigateTo(this, FeedBackAdviceActivity.class, null);
        break;

      default:
        break;
    }
  }

  /**
   * 问题详情
   *
   * @param
   * @return
   */
  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    GetQuestionDetailRequest request = new GetQuestionDetailRequest();
    request.setFaqUniqueKey(mQuestions.get(position).getFaqUniqueKey());
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);
    mQuestionDetailPresenter.getQuestionDetail(encryptInfo);
  }

  /**
   * 问题详情
   *
   * @param
   * @return
   */
  @Override
  public void openQuestionDetail(GetQuestionDetailResponse response) {
    //有内容返回再进去
    Intent intent = new Intent();
    intent.putExtra(mNavigator.EXTR_QUESTION_DETAIL_CONTENT, (Serializable) response);
    mNavigator.navigateTo(this, AdviceDetailActivity.class, intent);
  }

  /**
   * 问题列表
   *
   * @param
   * @return
   */
  @Override
  public void openQuestionList(List<GetQuestionListResponse> response) {
    mQuestions.addAll(response);
    if(mQuestions.size() == 0){
      //空视图
      adviceBinding.tvEmpty.setVisibility(View.VISIBLE);
      adviceBinding.lvQuestionsList.setVisibility(View.GONE);
      return;
    }

    adviceBinding.tvEmpty.setVisibility(View.GONE);
    adviceBinding.lvQuestionsList.setVisibility(View.VISIBLE);
    mAdviceAdapter = new AdviceFeedBackAdapter(this, mQuestions, R.layout.item_question_list);
    adviceBinding.lvQuestionsList.setAdapter(mAdviceAdapter);


  }
}
