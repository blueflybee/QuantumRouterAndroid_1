package com.qtec.mapp.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetUnlockInfoListResponse {

    private String recordUniqueKey;
    private String occurTime;
    private String userNickName;
    private String userHeadImage;
    private String message;
    private String messageCode;

    public String getRecordUniqueKey() {
        return recordUniqueKey;
    }

    public void setRecordUniqueKey(String recordUniqueKey) {
        this.recordUniqueKey = recordUniqueKey;
    }

    public String getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserHeadImage() {
        return userHeadImage;
    }

    public void setUserHeadImage(String userHeadImage) {
        this.userHeadImage = userHeadImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    @Override
    public String toString() {
        return "GetUnlockInfoListResponse{" +
            "recordUniqueKey='" + recordUniqueKey + '\'' +
            ", occurTime='" + occurTime + '\'' +
            ", userNickName='" + userNickName + '\'' +
            ", userHeadImage='" + userHeadImage + '\'' +
            ", message='" + message + '\'' +
            ", messageCode='" + messageCode + '\'' +
            '}';
    }
}
