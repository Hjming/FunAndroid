package com.cainiao.funcommonlibrary.okhttp.parser;

import com.alibaba.fastjson.JSON;
import com.cainiao.funcommonlibrary.util.LogUtil;

import okhttp3.Response;

public class ModelParser <T> extends BaseParser<T> {
    protected Class<T> clazz;

    public ModelParser(Class<T> clazz) {
        this.clazz = clazz;
    }


    @Override
    protected T parse(Response response) throws Exception {
        String resp = response.body().string();
        LogUtil.d("ModelParser","=====url:"+response.request().url().toString());
        LogUtil.d("ModelParser","=====resp:"+resp);
        if (response.isSuccessful()) {
            T object = JSON.parseObject(resp, clazz);
            return object;
        }
        return null;
    }
}
