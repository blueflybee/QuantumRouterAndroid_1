package com.qtec.mapp.model.rsp;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   : 问题列表
 *     version: 1.0
 * </pre>
 */
public class GetQuestionListResponse implements Serializable{

    private String title;
    private String faqUniqueKey;

    public String getFaqUniqueKey() {
        return faqUniqueKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFaqUniqueKey(String faqUniqueKey) {
        this.faqUniqueKey = faqUniqueKey;
    }

    @Override
    public String toString() {
        return "GetQuestionListResponse{" +
                "title='" + title + '\'' +
                ", faqUniqueKey='" + faqUniqueKey + '\'' +
                '}';
    }
}
