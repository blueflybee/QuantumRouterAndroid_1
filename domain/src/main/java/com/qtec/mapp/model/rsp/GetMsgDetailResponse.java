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
public class GetMsgDetailResponse {

    private String imgUrl;
    private String shareUserName;
    private String messageTitle;
    private String createDate;
    private String messageContent;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getShareUserName() {
        return shareUserName;
    }

    public void setShareUserName(String shareUserName) {
        this.shareUserName = shareUserName;
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

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    @Override
    public String toString() {
        return "GetMsgDetailResponse{" +
                "imgUrl='" + imgUrl + '\'' +
                ", shareUserName='" + shareUserName + '\'' +
                ", messageTitle='" + messageTitle + '\'' +
                ", createDate='" + createDate + '\'' +
                ", messageContent='" + messageContent + '\'' +
                '}';
    }
}
