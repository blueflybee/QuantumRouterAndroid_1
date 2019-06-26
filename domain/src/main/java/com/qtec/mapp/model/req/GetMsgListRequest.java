package com.qtec.mapp.model.req;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 消息列表 网络请求
 *      version: 1.0
 * </pre>
 */

public class GetMsgListRequest {
    private String userUniqueKey;
    private String msgType;
    private String start;
    private String pageSize;
    private String messageUniqueKey;

    public String getUserUniqueKey() {
        return userUniqueKey;
    }

    public void setUserUniqueKey(String userUniqueKey) {
        this.userUniqueKey = userUniqueKey;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getMessageUniqueKey() {
        return messageUniqueKey;
    }

    public void setMessageUniqueKey(String messageUniqueKey) {
        this.messageUniqueKey = messageUniqueKey;
    }

    @Override
    public String toString() {
        return "GetMsgListRequest{" +
                "userUniqueKey='" + userUniqueKey + '\'' +
                ", msgType='" + msgType + '\'' +
                ", start='" + start + '\'' +
                ", pageSize='" + pageSize + '\'' +
                '}';
    }
}
