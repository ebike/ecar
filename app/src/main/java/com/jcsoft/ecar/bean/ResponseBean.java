package com.jcsoft.ecar.bean;

import java.io.Serializable;

/**
 * 响应状态信息
 * Created by jimmy on 2015/07/12.
 */
public class ResponseBean<T> implements Serializable {

    private Integer code;
    private String errmsg;
    private T data;

    public ResponseBean(){

    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
