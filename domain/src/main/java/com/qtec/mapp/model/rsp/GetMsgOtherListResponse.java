package com.qtec.mapp.model.rsp;

import java.io.Serializable;
import java.util.Arrays;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   : 其他消息列表
 *     version: 1.0
 * </pre>
 */
public class GetMsgOtherListResponse implements Serializable{
    private String messageTitle;
    private String handleType;
    private String messageContent;
    private String userUniqueKey;
    private String createDate;
    private String messageUniqueKey;
    private String isRead;

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getHandleType() {
        return handleType;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getUserUniqueKey() {
        return userUniqueKey;
    }

    public void setUserUniqueKey(String userUniqueKey) {
        this.userUniqueKey = userUniqueKey;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getMessageUniqueKey() {
        return messageUniqueKey;
    }

    public void setMessageUniqueKey(String messageUniqueKey) {
        this.messageUniqueKey = messageUniqueKey;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "GetMsgOtherListResponse{" +
                "messageTitle='" + messageTitle + '\'' +
                ", handleType='" + handleType + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", userUniqueKey='" + userUniqueKey + '\'' +
                ", createDate='" + createDate + '\'' +
                ", messageUniqueKey='" + messageUniqueKey + '\'' +
                ", isRead='" + isRead + '\'' +
                '}';
    }
}
