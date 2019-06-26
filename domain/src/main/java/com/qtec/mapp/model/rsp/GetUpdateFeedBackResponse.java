package com.qtec.mapp.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     version: 1.0
 * </pre>
 */
public class GetUpdateFeedBackResponse {
    private String replyContent;

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyContent() {
        return replyContent;
    }

    @Override
    public String toString() {
        return "GetUpdateFeedBackResponse{" +
                "replyContent='" + replyContent + '\'' +
                '}';
    }
}
