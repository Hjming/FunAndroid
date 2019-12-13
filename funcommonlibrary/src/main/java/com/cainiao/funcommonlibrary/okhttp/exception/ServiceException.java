package com.cainiao.funcommonlibrary.okhttp.exception;

import com.alibaba.fastjson.JSONObject;

/**
 * time: 2019-12-12
 * Project_Name: FunAndroid
 * Package_Name:
 * Description: 业务逻辑异常
 * Created by hjm.
 */
public class ServiceException extends Exception {

    private int code;

    private String codeStr;

    private JSONObject data;

    public ServiceException(int code) {
        this.code = code;
    }

    public ServiceException(int code, String detailMessage) {
        super(detailMessage);
        this.code = code;
    }

    public ServiceException(int code, String detailMessage, JSONObject data) {
        super(detailMessage);
        this.code = code;
        this.data = data;
    }

    public ServiceException(String code) {
        this.codeStr = code;
    }

    public ServiceException(String code, String detailMessage) {
        super(detailMessage);
        this.codeStr = code;
    }

    public ServiceException(String code, String detailMessage, JSONObject data) {
        super(detailMessage);
        this.codeStr = code;
        this.data = data;
    }


    public int getCode() {
        return code;
    }

    public String getStrCode() {
        if (codeStr == null) {
            return code + "";
        } else {
            return codeStr;
        }
    }

    public JSONObject getData() {
        return data;
    }
}
