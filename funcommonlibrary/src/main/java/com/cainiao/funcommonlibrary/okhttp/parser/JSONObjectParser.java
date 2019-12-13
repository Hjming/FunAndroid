package com.cainiao.funcommonlibrary.okhttp.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import okhttp3.Response;

/**
 * time: 2019-12-12
 * Project_Name: FunAndroid
 * Package_Name:
 * Description: 简单jsonObject自动解析
 * Created by hjm.
 */
public class JSONObjectParser extends BaseParser<JSONObject> {

    @Override
    protected JSONObject parse(Response response) throws Exception {
        if (response.isSuccessful()) {
            JSONObject jsonObject = JSON.parseObject(response.body().string());
            return jsonObject;
        }
        return null;
    }
}
