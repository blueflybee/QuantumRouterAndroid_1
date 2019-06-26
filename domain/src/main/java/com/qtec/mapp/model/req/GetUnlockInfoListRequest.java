package com.qtec.mapp.model.req;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      version: 1.0
 * </pre>
 */

public class GetUnlockInfoListRequest {
    private String deviceSerialNo;
    private String type;
    private String start;
    private String pageSize;
    private String recordUniqueKey;

    public String getDeviceSerialNo() {
        return deviceSerialNo;
    }

    public void setDeviceSerialNo(String deviceSerialNo) {
        this.deviceSerialNo = deviceSerialNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getRecordUniqueKey() {
        return recordUniqueKey;
    }

    public void setRecordUniqueKey(String recordUniqueKey) {
        this.recordUniqueKey = recordUniqueKey;
    }

    @Override
    public String toString() {
        return "GetUnlockInfoListRequest{" +
                "deviceSerialNo='" + deviceSerialNo + '\'' +
                ", type='" + type + '\'' +
                ", start='" + start + '\'' +
                ", pageSize='" + pageSize + '\'' +
                '}';
    }
}
