package com.cainiao.funcommonlibrary.okhttp.parser;

import okhttp3.Response;

/**
 * time: 2019-12-12
 * Project_Name: FunAndroid
 * Package_Name:
 * Description: 字符串自动解析
 * Created by hjm.
 */
public class StringParser extends BaseParser<String> {

    @Override
    protected String parse(Response response) throws Exception {
        if (response.isSuccessful()) {
            return response.body().string();
        }
        return null;
    }
}
