package com.cainiao.funcommonlibrary.okhttp.requestbuilder;

import com.cainiao.funcommonlibrary.okhttp.callback.OkProgressCallback;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PutRequestBuilder extends RequestBuilder {

    @Override
    public PutRequestBuilder url(String url) {
        super.url(url);
        return this;
    }


    @Override
    public PutRequestBuilder params(Map<String, String> params) {
        super.params(params);
        return this;
    }

    @Override
    public PutRequestBuilder addParam(String key, String value) {
        super.addParam(key, value);
        return this;
    }

    @Override
    public PutRequestBuilder headers(Map<String, String> headers) {
        super.headers(headers);
        return this;
    }

    @Override
    public PutRequestBuilder addHeader(String key, String values) {
        super.addHeader(key, values);
        return this;
    }


    public PutRequestBuilder setBodyString(String body) {
        return setBodyString("text/plain", body);
    }


    public PutRequestBuilder setBodyString(String mediaType, String body) {
        requestBody = RequestBody.create(MediaType.parse(mediaType), body);
        return this;
    }


    public PutRequestBuilder addFormDataPart(String key, String value) {
        if (multipartBodyBuilder == null) {
            multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        }
        multipartBodyBuilder.addFormDataPart(key, value);
        return this;
    }


    public PutRequestBuilder addFormDataPart(String key, String filename, String mediaType, String file) {
        if (multipartBodyBuilder == null) {
            multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        }
        multipartBodyBuilder.addFormDataPart(key, filename, RequestBody.create(MediaType.parse(mediaType), new File(file)));
        return this;
    }


    public PutRequestBuilder setBodyBytes(final byte[] content) {
        return setBodyBytes(null, content);
    }


    public PutRequestBuilder setBodyBytes(final String mediaType, final byte[] content) {
        MediaType media = null;
        if (mediaType == null)
            media = null;
        else
            media = MediaType.parse(mediaType);
        requestBody = RequestBody.create(media, content);
        return this;
    }


    public PutRequestBuilder setBodyFile(final File file) {
        return setBodyFile(null, file);
    }


    public PutRequestBuilder setBodyFile(final String mediaType, final File file) {
        MediaType media = null;
        if (mediaType == null)
            media = null;
        else
            media = MediaType.parse(mediaType);

        requestBody = RequestBody.create(media, file);
        return this;
    }

    public PutRequestBuilder addReqProgressCallback(OkProgressCallback reqProgressCallback) {
        this.reqProgressCallback = reqProgressCallback;
        return this;
    }


    public PutRequestBuilder setWriteTimeOut(int writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }


    public PutRequestBuilder setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }


    @Override
    public PutRequestBuilder tag(Object tag) {
        super.tag(tag);
        return this;
    }


    @Override
    public Call enqueue(Callback callback) {
        return enqueue("PUT", callback);
    }

    @Override
    public Response execute() throws IOException {
        return execute("PUT");
    }

}
