package com.qtec.mapp.model.rsp;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetMyAdviceResponse implements Serializable{
    private String feedbackContent;
    private String feedbackUniqueKey;
    private String createTime;

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public String getFeedbackUniqueKey() {
        return feedbackUniqueKey;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public void setFeedbackUniqueKey(String feedbackUniqueKey) {
        this.feedbackUniqueKey = feedbackUniqueKey;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    @Override
    public String toString() {
        return "GetMyAdviceResponse{" +
                "feedbackContent='" + feedbackContent + '\'' +
                ", feedbackUniqueKey='" + feedbackUniqueKey + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
