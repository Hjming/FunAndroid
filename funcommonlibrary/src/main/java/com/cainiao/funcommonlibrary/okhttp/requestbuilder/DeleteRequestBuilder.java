package com.cainiao.funcommonlibrary.okhttp.requestbuilder;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeleteRequestBuilder extends RequestBuilder {

    protected RequestBody requestBody;

    @Override
    public DeleteRequestBuilder url(String url) {
        super.url(url);
        return this;
    }


    @Override
    public DeleteRequestBuilder params(Map<String, String> params) {
        super.params(params);
        return this;
    }

    @Override
    public DeleteRequestBuilder addParam(String key, String value) {
        super.addParam(key, value);
        return this;
    }

    @Override
    public DeleteRequestBuilder headers(Map<String, String> headers) {
        super.headers(headers);
        return this;
    }

    @Override
    public DeleteRequestBuilder addHeader(String key, String values) {
        super.addHeader(key, values);
        return this;
    }

    public DeleteRequestBuilder setBodyString(String body) {
        return setBodyString("text/plain", body);
    }


    public DeleteRequestBuilder setBodyString(String mediaType, String body) {
        requestBody = RequestBody.create(MediaType.parse(mediaType), body);
        return this;
    }

    @Override
    public DeleteRequestBuilder tag(Object tag) {
        super.tag(tag);
        return this;
    }

    @Override
    public Call enqueue(Callback callback) {
        return enqueue("DELETE", callback);
    }

    @Override
    public Response execute() throws IOException {
        return execute("DELETE");
    }
}
