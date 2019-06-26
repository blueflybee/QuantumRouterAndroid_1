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
public class GetAgreementResponse {

    private String statuteContent;

    public String getStatuteContent() {
        return statuteContent;
    }

    public void setStatuteContent(String statuteContent) {
        this.statuteContent = statuteContent;
    }

    @Override
    public String toString() {
        return "GetAgreementResponse{" +
                "statuteContent='" + statuteContent + '\'' +
                '}';
    }
}
