package com.qtec.mapp.model.req;

import java.util.Arrays;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 消息删除 网络请求
 *      version: 1.0
 * </pre>
 */

public class DeleteMsgRequest {
    private String[] messageUniqueKeys;

    public void setMessageUniqueKeys(String[] messageUniqueKeys) {
        this.messageUniqueKeys = messageUniqueKeys;
    }

    public String[] getMessageUniqueKeys() {
        return messageUniqueKeys;
    }

    @Override
    public String toString() {
        return "DeleteMsgRequest{" +
                "messageUniqueKeys=" + Arrays.toString(messageUniqueKeys) +
                '}';
    }
}
