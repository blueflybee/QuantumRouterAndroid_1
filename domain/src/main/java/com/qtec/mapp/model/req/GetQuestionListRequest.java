package com.qtec.mapp.model.req;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 用户协议网络请求
 *      version: 1.0
 * </pre>
 */

public class GetQuestionListRequest {
    //当start==0时，传-1，当start!=0时，该值取服务端上次返回的列表中最后一个记录的faqUniqueKey"
    private String start;
    private String pageSize;
    private String faqUniqueKey;

    public String getPageSize() {
        return pageSize;
    }

    public String getStart() {
        return start;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setFaqUniqueKey(String faqUniqueKey) {
        this.faqUniqueKey = faqUniqueKey;
    }

    public String getFaqUniqueKey() {
        return faqUniqueKey;
    }
}
