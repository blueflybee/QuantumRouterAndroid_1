package com.qtec.model.core;


import com.fernandocejas.android10.sample.domain.params.IRequest;

/**
 * Created by l2h on 17-6-5.
 * Desc: 公共返回结果
 */
public class QtecResult<T> implements IRequest{

    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "QtecResult{" +
            "code=" + code +
            ", msg='" + msg + '\'' +
            ", data=" + data +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QtecResult<?> that = (QtecResult<?>) o;

        if (code != that.code) return false;
        if (!msg.equals(that.msg)) return false;
        return data.equals(that.data);

    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + msg.hashCode();
        result = 31 * result + data.hashCode();
        return result;
    }


}
