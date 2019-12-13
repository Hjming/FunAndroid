package com.cainiao.funcommonlibrary.okhttp.parser;

import okhttp3.Response;

public abstract class BaseParser<T> {

    protected int code;
    protected String msg;

    protected abstract T parse(Response response) throws Exception;

    public T parseResponse(Response response) throws Exception {
        code = response.code();
        msg = response.message();
        return parse(response);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return msg;
    }

}