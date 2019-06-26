package com.fernandocejas.android10.sample.presentation.view.message.data;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/30
 *      desc: 阿里消息推送的content json串解析
 *      version: 1.0
 * </pre>
 */

public class GetAcceptContent {
    private String bodyContent;
    private String historyUniqueKey;

    public String getBodyContent() {
        return bodyContent;
    }

    public void setBodyContent(String bodyContent) {
        this.bodyContent = bodyContent;
    }

    public String getHistoryUniqueKey() {
        return historyUniqueKey;
    }

    public void setHistoryUniqueKey(String historyUniqueKey) {
        this.historyUniqueKey = historyUniqueKey;
    }

    @Override
    public String toString() {
        return "GetAcceptContent{" +
                "bodyContent='" + bodyContent + '\'' +
                ", historyUniqueKey='" + historyUniqueKey + '\'' +
                '}';
    }
}
