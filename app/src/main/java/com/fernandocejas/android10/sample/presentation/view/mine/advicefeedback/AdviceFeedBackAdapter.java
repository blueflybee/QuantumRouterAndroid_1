package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter;
import com.qtec.mapp.model.rsp.GetQuestionListResponse;

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

public class AdviceFeedBackAdapter extends CommAdapter<GetQuestionListResponse> {

    public AdviceFeedBackAdapter(Context context, List<GetQuestionListResponse> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, GetQuestionListResponse getQuestionListResponse) {
        TextView questionTitle = baseViewHolder.getView(R.id.tv_questionListTitle);
        if(TextUtils.isEmpty(getQuestionListResponse.getTitle())){
            questionTitle.setText("未知问题");
        }else{
            questionTitle.setText(getQuestionListResponse.getTitle());
        }

    }
}
