package com.qtec.mapp.model.rsp;

import java.io.Serializable;
import java.util.Arrays;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   : 消息列表
 *     version: 1.0
 * </pre>
 */
public class GetMsgListResponse<T> implements Serializable{
    private String messageUniqueKey;
    private String messageTitle;
    private String createDate;
    private T messageContent;
    private String isRead;

    public String getMessageUniqueKey() {
        return messageUniqueKey;
    }

    public void setMessageUniqueKey(String messageUniqueKey) {
        this.messageUniqueKey = messageUniqueKey;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public void setMessageContent(T messageContent) {
        this.messageContent = messageContent;
    }

    public T getMessageContent() {
        return messageContent;
    }

    @Override
    public String toString() {
        return "GetMsgListResponse{" +
                "messageUniqueKey='" + messageUniqueKey + '\'' +
                ", messageTitle='" + messageTitle + '\'' +
                ", createDate='" + createDate + '\'' +
                ", messageContent=" + messageContent +
                ", isRead='" + isRead + '\'' +
                '}';
    }

    public static class messageContent<T> implements Serializable{

        private String deviceName;
        private String userUniqueKey;
        private String historyUniqueKey;
        private String handleType;
        private String[] deviceInfo;

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getUserUniqueKey() {
            return userUniqueKey;
        }

        public void setUserUniqueKey(String userUniqueKey) {
            this.userUniqueKey = userUniqueKey;
        }

        public String getHistoryUniqueKey() {
            return historyUniqueKey;
        }

        public void setHistoryUniqueKey(String historyUniqueKey) {
            this.historyUniqueKey = historyUniqueKey;
        }

        public String getHandleType() {
            return handleType;
        }

        public void setHandleType(String handleType) {
            this.handleType = handleType;
        }

        public String[] getDeviceInfo() {
            return deviceInfo;
        }

        public void setDeviceInfo(String[] deviceInfo) {
            this.deviceInfo = deviceInfo;
        }

        @Override
        public String toString() {
            return "messageContent{" +
                    "deviceName='" + deviceName + '\'' +
                    ", userUniqueKey='" + userUniqueKey + '\'' +
                    ", historyUniqueKey='" + historyUniqueKey + '\'' +
                    ", handleType='" + handleType + '\'' +
                    ", deviceInfo=" + Arrays.toString(deviceInfo) +
                    '}';
        }
    }
}
