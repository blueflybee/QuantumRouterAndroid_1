package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

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
 *      desc: 所有问题
 *      version: 1.0
 * </pre>
 */

public class AllQuestionActivity extends BaseActivity implements AdapterView.OnItemClickListener, IGetQuestionListView,IGetQuestionDetailView {
  ActivityAdviceFeedbackBinding adviceBinding;
  private AllQuestionAdapter mAdviceAdapter;
  private PtrClassicFrameLayout mPtrFrame;
  private int mPageNum = 0;//统计上拉加载的次数
  private List<GetQuestionListResponse> mQuestions;
  private Boolean isHasNoMoreData = false;

  @Inject
  GetQuestionListPresenter mGetQuestionListPresenter;
  @Inject
  QuestionDetailPresenter mQuestionDetailPresenter;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    adviceBinding = DataBindingUtil.setContentView(this, R.layout.activity_advice_feedback);
    initTitleBar("全部问题");
    initializeInjector();
    initPresenter();
    initView();
    dealWithRefresh();
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

  private void initView() {
    mQuestions = new ArrayList<>();
    mPtrFrame = adviceBinding.ptrLayout;
    adviceBinding.rlBottom.setVisibility(View.GONE);
    adviceBinding.tvQuestionList.setText("全部问题");
    adviceBinding.lvQuestionsList.setOnItemClickListener(this);
  }

  private void queryQuestionList(String start, String pageSize) {
    GetQuestionListRequest request = new GetQuestionListRequest();
    if("0".equals(start)){
      request.setFaqUniqueKey("-1");
    }else {
      if(mQuestions != null && mQuestions.size() > 1){
        request.setFaqUniqueKey(mQuestions.get(mQuestions.size()-1).getFaqUniqueKey());
      }
    }
    request.setStart(start);
    request.setPageSize(pageSize);
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);
    mGetQuestionListPresenter.getQuestionList(encryptInfo);
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
              Toast.makeText(AllQuestionActivity.this, "已加载所有数据，请稍候再试", Toast.LENGTH_SHORT).show();
              mPtrFrame.refreshComplete();
              return;
            }else {
              queryQuestionList("" + mQuestions.size(), "14");// 每次请求14条记录
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

            if (mQuestions.size() == 0) {
              mPageNum = 0;
              queryQuestionList("0", "14");
            } else {
              mAdviceAdapter.notifyDataSetChanged();
              mPtrFrame.refreshComplete();
            }

          }
        }, 100);
      }

      @Override
      public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
        return super.checkCanDoLoadMore(frame, adviceBinding.lvQuestionsList, footer);
      }

      @Override
      public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return super.checkCanDoRefresh(frame, adviceBinding.lvQuestionsList, header);
      }
    });
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    /*mNavigator.navigateTo(this, AdviceDetailActivity.class, null);*/
    GetQuestionDetailRequest request = new GetQuestionDetailRequest();
    request.setFaqUniqueKey(mQuestions.get(position).getFaqUniqueKey());
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);
    mQuestionDetailPresenter.getQuestionDetail(encryptInfo);
  }

  @Override
  public void openQuestionList(List<GetQuestionListResponse> response) {
    if(response.size() == 0){
      //判断是否为空，为空说明刷新完成，没有更多数据了
      isHasNoMoreData = true;
    }else {
      isHasNoMoreData = false;
    }

    mQuestions.addAll(response);
    if (mQuestions.size() == 0) {
      //空视图
      adviceBinding.tvEmpty.setVisibility(View.VISIBLE);
      adviceBinding.lvQuestionsList.setVisibility(View.GONE);
      mPtrFrame.refreshComplete();
      return;
    }

    adviceBinding.tvEmpty.setVisibility(View.GONE);
    adviceBinding.lvQuestionsList.setVisibility(View.VISIBLE);
    if (mPageNum == 0) {
      mPageNum = 1;
      mAdviceAdapter = new AllQuestionAdapter(this, mQuestions, R.layout.item_question_list);
      adviceBinding.lvQuestionsList.setAdapter(mAdviceAdapter);
      mPtrFrame.refreshComplete();
    } else {
      mPageNum++;
      mAdviceAdapter.notifyDataSetChanged();
      mPtrFrame.refreshComplete();
    }

  }

  @Override
  public void openQuestionDetail(GetQuestionDetailResponse response) {
    //有内容返回再进去
    Intent intent = new Intent();
    intent.putExtra(mNavigator.EXTR_QUESTION_DETAIL_CONTENT, (Serializable) response);
    mNavigator.navigateTo(this, AdviceDetailActivity.class, intent);
  }
}
