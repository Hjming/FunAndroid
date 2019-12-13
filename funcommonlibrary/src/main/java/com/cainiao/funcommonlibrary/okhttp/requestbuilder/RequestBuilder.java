package com.cainiao.funcommonlibrary.okhttp.requestbuilder;

import android.text.TextUtils;

import com.cainiao.funcommonlibrary.okhttp.body.BodyWrapper;
import com.cainiao.funcommonlibrary.okhttp.callback.OkCallback;
import com.cainiao.funcommonlibrary.okhttp.callback.OkProgressCallback;

import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class RequestBuilder {

    protected MultipartBody.Builder multipartBodyBuilder;

    protected RequestBody requestBody;

    protected OkProgressCallback reqProgressCallback;

    protected OkProgressCallback rspProgressCallback;

    protected int readTimeOut;

    protected int writeTimeOut;

    protected String url;
    protected Map<String, String> params;
    protected Map<String, String> headers;
    protected Object tag;


    abstract Call enqueue(Callback callback);

    abstract Response execute() throws IOException;


    public RequestBuilder url(String url) {
        this.url = url;
        return this;
    }


    public RequestBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public RequestBuilder addParam(String key, String value) {
        if (params == null) {
            params = new IdentityHashMap<>();
        }
        params.put(key, value);
        return this;
    }

    public RequestBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public RequestBuilder addHeader(String key, String values) {
        if (headers == null) {
            headers = new IdentityHashMap<>();
        }
        headers.put(key, values);
        return this;
    }

    public RequestBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    protected void appendHeaders(Request.Builder builder, Map<String, String> headers) {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;

        for (String key : headers.keySet()) {
            String value = headers.get(key);
            if(value == null){
                value = "";
            }
            headerBuilder.add(key, value);
        }
        builder.headers(headerBuilder.build());
    }

    protected void appendParams(FormBody.Builder builder, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                String value = params.get(key);
                if(value == null){
                    value = "";
                }
                builder.add(key, value);
            }
        }
    }

    protected String appendParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if(url.contains("?")){
            sb.append(url + "&");
        }else{
            sb.append(url + "?");
        }
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key)
                        .append("=")
                        .append(params.get(key) == null ? "" : params.get(key))
                        .append("&");
            }
        }

        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    protected void makeBody(String method, Request.Builder builder) {
        if (reqProgressCallback != null) {
            if (multipartBodyBuilder != null) {
                builder.method(method, BodyWrapper.addProgressRequestListener(multipartBodyBuilder.build(), reqProgressCallback));
            } else if (requestBody != null) {
                builder.method(method, BodyWrapper.addProgressRequestListener(requestBody, reqProgressCallback));
            } else {
                FormBody.Builder encodingBuilder = new FormBody.Builder();
                appendParams(encodingBuilder, params);
                builder.method(method, BodyWrapper.addProgressRequestListener(encodingBuilder.build(), reqProgressCallback));
            }
        } else {
            if (multipartBodyBuilder != null) {
                builder.method(method, multipartBodyBuilder.build());
            } else if (requestBody != null) {
                builder.method(method, requestBody);
            } else {
                FormBody.Builder encodingBuilder = new FormBody.Builder();
                appendParams(encodingBuilder, params);
                builder.method(method, encodingBuilder.build());
            }
        }
    }


    protected OkHttpClient cloneClient() {

        OkHttpClient okHttpClient = null;

        if (writeTimeOut * readTimeOut > 0) {
            OkHttpClient.Builder okHttpClientBuilder = HttpUtil.getInstance().newBuilder();
            if (writeTimeOut > 0) {
                okHttpClientBuilder.writeTimeout(writeTimeOut, TimeUnit.SECONDS);
            }
            if (readTimeOut > 0) {
                okHttpClientBuilder.readTimeout(readTimeOut, TimeUnit.SECONDS);
            }
            okHttpClient = okHttpClientBuilder.build();
        }

        if (okHttpClient == null) {
            okHttpClient = HttpUtil.getInstance();
        }

        if (rspProgressCallback != null) {
            okHttpClient = BodyWrapper.addProgressResponseListener(okHttpClient, rspProgressCallback);
        }

        return okHttpClient;
    }


    protected Call enqueue(String method, Callback callback) {

        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url can not be null !");
        }

        Request.Builder builder = new Request.Builder().url(url);
        if (tag != null) {
            builder.tag(tag);
        }
        appendHeaders(builder, headers);

        makeBody(method, builder);

        Request request = builder.build();

        Call call = cloneClient().newCall(request);
        call.enqueue(callback);
        if (callback instanceof OkCallback) {
            ((OkCallback) callback).onStart();
        }
        return call;
    }

    protected Response execute(String method) throws IOException {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url can not be null !");
        }

        Request.Builder builder = new Request.Builder().url(url);
        if (tag != null) {
            builder.tag(tag);
        }
        appendHeaders(builder, headers);

        makeBody(method, builder);

        Request request = builder.build();

        Call call = cloneClient().newCall(request);
        return call.execute();
    }
}
