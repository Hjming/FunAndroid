package com.cainiao.funcommonlibrary.okhttp.exception;

/**
 * time: 2019-12-12
 * Project_Name: FunAndroid
 * Package_Name:
 * Description: 通用的网络异常
 * Created by hjm.
 */
public class HttpException extends Exception {

    private int statusCode;


    public HttpException() {
    }

    public HttpException(int code) {
        this.statusCode = code;
    }

    public HttpException(int code, String detailMessage) {
        super(detailMessage);
        this.statusCode = code;
    }

    /**
     * 获取 状态码
     *
     * @return 状态码
     */
    public int getStatusCode() {
        return statusCode;
    }
}
