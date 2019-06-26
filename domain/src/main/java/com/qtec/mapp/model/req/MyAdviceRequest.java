package com.qtec.mapp.model.req;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 我的反馈
 *      version: 1.0
 * </pre>
 */

public class MyAdviceRequest {
    private String userUniqueKey;
    private String feedbackUniqueKey;
    private String pageSize;

    public String getUserUniqueKey() {
        return userUniqueKey;
    }

    public void setUserUniqueKey(String userUniqueKey) {
        this.userUniqueKey = userUniqueKey;
    }

    public String getFeedbackUniqueKey() {
        return feedbackUniqueKey;
    }

    public void setFeedbackUniqueKey(String feedbackUniqueKey) {
        this.feedbackUniqueKey = feedbackUniqueKey;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "MyAdviceRequest{" +
            "userUniqueKey='" + userUniqueKey + '\'' +
            ", feedbackUniqueKey='" + feedbackUniqueKey + '\'' +
            ", pageSize='" + pageSize + '\'' +
            '}';
    }
}
