package com.qtec.mapp.model.rsp;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   : 设备共享成员
 *     version: 1.0
 * </pre>
 */
public class GetShareMemListResponse implements Serializable{

    private String historyUniqueKey;
    private String sharedUserName;
    private String sharedUserPhone;
    private String shareDate;
    private String handleType;
    private String sharedUserHeadImage;

    public String getHistoryUniqueKey() {
        return historyUniqueKey;
    }

    public void setHistoryUniqueKey(String historyUniqueKey) {
        this.historyUniqueKey = historyUniqueKey;
    }

    public String getSharedUserName() {
        return sharedUserName;
    }

    public void setSharedUserName(String sharedUserName) {
        this.sharedUserName = sharedUserName;
    }

    public String getSharedUserPhone() {
        return sharedUserPhone;
    }

    public void setSharedUserPhone(String sharedUserPhone) {
        this.sharedUserPhone = sharedUserPhone;
    }

    public String getShareDate() {
        return shareDate;
    }

    public void setShareDate(String shareDate) {
        this.shareDate = shareDate;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    public String getHandleType() {
        return handleType;
    }

    public void setSharedUserHeadImage(String sharedUserHeadImage) {
        this.sharedUserHeadImage = sharedUserHeadImage;
    }

    public String getSharedUserHeadImage() {
        return sharedUserHeadImage;
    }

    @Override
    public String toString() {
        return "GetShareMemListResponse{" +
            "historyUniqueKey='" + historyUniqueKey + '\'' +
            ", sharedUserName='" + sharedUserName + '\'' +
            ", sharedUserPhone='" + sharedUserPhone + '\'' +
            ", shareDate='" + shareDate + '\'' +
            ", handleType='" + handleType + '\'' +
            ", sharedUserHeadImage='" + sharedUserHeadImage + '\'' +
            '}';
    }
}
