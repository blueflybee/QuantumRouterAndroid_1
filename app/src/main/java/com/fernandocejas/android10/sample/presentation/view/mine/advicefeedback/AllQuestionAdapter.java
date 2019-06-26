package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import android.content.Context;
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

public class AllQuestionAdapter extends CommAdapter<GetQuestionListResponse> {

    public AllQuestionAdapter(Context context, List<GetQuestionListResponse> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, GetQuestionListResponse bean) {
        TextView title = baseViewHolder.getView(R.id.tv_questionListTitle);
        title.setText(bean.getTitle());
    }
}
