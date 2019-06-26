package com.qtec.mapp.model.req;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 问题详情网络请求
 *      version: 1.0
 * </pre>
 */

public class GetQuestionDetailRequest {
    private String faqUniqueKey;

    public String getFaqUniqueKey() {
        return faqUniqueKey;
    }

    public void setFaqUniqueKey(String faqUniqueKey) {
        this.faqUniqueKey = faqUniqueKey;
    }

    @Override
    public String toString() {
        return "GetQuestionDetailRequest{" +
                "faqUniqueKey='" + faqUniqueKey + '\'' +
                '}';
    }
}
