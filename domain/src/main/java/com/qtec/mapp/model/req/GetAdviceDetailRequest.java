package com.qtec.mapp.model.req;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 我的反馈详情 网络请求
 *      version: 1.0
 * </pre>
 */

public class GetAdviceDetailRequest {
    private String feedbackUniqueKey;

    public String getFeedbackUniqueKey() {
        return feedbackUniqueKey;
    }

    public void setFeedbackUniqueKey(String feedbackUniqueKey) {
        this.feedbackUniqueKey = feedbackUniqueKey;
    }

    @Override
    public String toString() {
        return "GetAdviceDetailRequest{" +
                "feedbackUniqueKey='" + feedbackUniqueKey + '\'' +
                '}';
    }
}
