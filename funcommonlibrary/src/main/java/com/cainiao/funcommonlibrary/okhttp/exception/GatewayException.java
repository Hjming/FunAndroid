package com.cainiao.funcommonlibrary.okhttp.exception;

/**
 * time: 2019-12-12
 * Project_Name: FunAndroid
 * Package_Name:
 * Description: 网关异常
 * Created by hjm.
 */
public class GatewayException extends Exception {

    private String code;

    public GatewayException(String code) {
        this.code = code;
    }

    public GatewayException(String code, String detailMessage) {
        super(detailMessage);
        this.code = code;
    }


    public String getCode() {
        return code;
    }
}
