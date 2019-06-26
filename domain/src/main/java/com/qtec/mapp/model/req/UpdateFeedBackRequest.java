package com.qtec.mapp.model.req;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 更新意见反馈 网络请求
 *      version: 1.0
 * </pre>
 */

public class UpdateFeedBackRequest {
    private String feedbackUniqueKey;
    private String feedbackContent;

    public String getFeedbackUniqueKey() {
        return feedbackUniqueKey;
    }

    public void setFeedbackUniqueKey(String feedbackUniqueKey) {
        this.feedbackUniqueKey = feedbackUniqueKey;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    @Override
    public String toString() {
        return "UpdateFeedBackRequest{" +
                "feedbackUniqueKey='" + feedbackUniqueKey + '\'' +
                ", feedbackContent='" + feedbackContent + '\'' +
                '}';
    }
}
