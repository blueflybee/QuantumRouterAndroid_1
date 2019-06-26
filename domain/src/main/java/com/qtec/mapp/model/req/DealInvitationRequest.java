package com.qtec.mapp.model.req;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      version: 1.0
 * </pre>
 */

public class DealInvitationRequest {
    private String userUniqueKey;
    private String historyUniqueKey;
    private String handleType;

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

    @Override
    public String toString() {
        return "DealInvitationRequest{" +
                "userUniqueKey='" + userUniqueKey + '\'' +
                ", historyUniqueKey='" + historyUniqueKey + '\'' +
                ", handleType='" + handleType + '\'' +
                '}';
    }
}
