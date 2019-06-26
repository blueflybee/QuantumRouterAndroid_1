package com.qtec.mapp.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetfeedBackResponse {
    private String replyContent;
    private String feedbackUniqueKey;

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getFeedbackUniqueKey() {
        return feedbackUniqueKey;
    }

    public void setFeedbackUniqueKey(String feedbackUniqueKey) {
        this.feedbackUniqueKey = feedbackUniqueKey;
    }

    @Override
    public String toString() {
        return "GetfeedBackResponse{" +
                "replyContent='" + replyContent + '\'' +
                ", feedbackUniqueKey='" + feedbackUniqueKey + '\'' +
                '}';
    }
}
