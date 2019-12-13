package com.cainiao.funcommonlibrary.okhttp.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import okhttp3.Response;

public class JSONArrayParser extends BaseParser<JSONArray> {

    @Override
    protected JSONArray parse(Response response) throws Exception {
        if (response.isSuccessful()) {
            JSONArray jsonArray = JSON.parseArray(response.body().string());
            return jsonArray;
        }
        return null;
    }

}
