package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAdviceDetailBinding;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.mapp.model.rsp.GetQuestionDetailResponse;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class AdviceDetailActivity extends BaseActivity implements View.OnClickListener {
    private ActivityAdviceDetailBinding mAdviceDetailBinding;
    GetQuestionDetailResponse mQuestionDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdviceDetailBinding = DataBindingUtil.setContentView(this,R.layout.activity_advice_detail);
       // mAdviceDetailBinding = DataBindingUtil.setContentView(this, R.file_paths.activity_advice_detail);
        initTitleBar("问题详情");

        // 获取问题详情
        mQuestionDetail = (GetQuestionDetailResponse) getIntent().getSerializableExtra(mNavigator.EXTR_QUESTION_DETAIL_CONTENT);
        mAdviceDetailBinding.setQuestionDetail(mQuestionDetail);
        mAdviceDetailBinding.btnQuestionSolved.setOnClickListener(this);
        mAdviceDetailBinding.btnNotSolved.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_questionSolved:
                this.finish();
                break;

            case R.id.btn_notSolved:
                mNavigator.navigateTo(this,FeedBackAdviceActivity.class,null);
                break;
            default:
                break;
        }
    }
}
