package com.qtec.mapp.model.req;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 新增意见反馈 网络请求
 *      version: 1.0
 * </pre>
 */

public class PostFeedBackRequest {
    private String userUniqueKey;
    private String userContact;
    private String feedbackContent;

    public String getUserUniqueKey() {
        return userUniqueKey;
    }

    public void setUserUniqueKey(String userUniqueKey) {
        this.userUniqueKey = userUniqueKey;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    @Override
    public String toString() {
        return "PostFeedBackRequest{" +
                "userUniqueKey='" + userUniqueKey + '\'' +
                ", userContact='" + userContact + '\'' +
                ", feedbackContent='" + feedbackContent + '\'' +
                '}';
    }
}
