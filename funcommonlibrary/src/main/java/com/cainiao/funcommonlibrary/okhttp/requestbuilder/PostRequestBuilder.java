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

public class PostRequestBuilder extends RequestBuilder {


    /**
     * 设置post的url
     *
     * @param url
     * @return PostRequestBuilder
     */
    @Override
    public PostRequestBuilder url(String url) {
        super.url(url);
        return this;
    }


    /**
     * 覆盖设置form参数
     *
     * @param params
     * @return PostRequestBuilder
     */
    @Override
    public PostRequestBuilder params(Map<String, String> params) {
        super.params(params);
        return this;
    }

    /**
     * 添加一个form参数
     *
     * @param key
     * @param value
     * @return PostRequestBuilder
     */
    @Override
    public PostRequestBuilder addParam(String key, String value) {
        super.addParam(key, value);
        return this;
    }

    /**
     * 覆盖设置header
     *
     * @param headers headers
     * @return PostRequestBuilder
     */
    @Override
    public PostRequestBuilder headers(Map<String, String> headers) {
        super.headers(headers);
        return this;
    }

    /**
     * 添加一个header字段
     *
     * @param key
     * @param values
     * @return PostRequestBuilder
     */
    @Override
    public PostRequestBuilder addHeader(String key, String values) {
        super.addHeader(key, values);
        return this;
    }

    /**
     * 设置请求的tag，以便cancel使用
     *
     * @param tag
     * @return PostRequestBuilder
     */
    @Override
    public PostRequestBuilder tag(Object tag) {
        super.tag(tag);
        return this;
    }


    /**
     * 添加multipart
     *
     * @param key
     * @param value
     * @return PostRequestBuilder
     */
    public PostRequestBuilder addFormDataPart(String key, String value) {
        if (multipartBodyBuilder == null) {
            multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        }
        multipartBodyBuilder.addFormDataPart(key, value);
        return this;
    }

    /**
     * 添加multipart
     *
     * @param key
     * @param filename
     * @param mediaType
     * @param file
     * @return PostRequestBuilder
     */
    public PostRequestBuilder addFormDataPart(String key, String filename, String mediaType, String file) {
        if (multipartBodyBuilder == null) {
            multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        }
        multipartBodyBuilder.addFormDataPart(key, filename, RequestBody.create(MediaType.parse(mediaType), new File(file)));
        return this;
    }

    /**
     * 设置post的字符串,默认mediatype为text/plain
     *
     * @param body
     * @return PostRequestBuilder
     */
    public PostRequestBuilder setBodyString(String body) {
        return setBodyString("text/plain", body);
    }

    /**
     * 设置post的字符串
     *
     * @param mediaType
     * @param body
     * @return PostRequestBuilder
     */
    public PostRequestBuilder setBodyString(String mediaType, String body) {
        requestBody = RequestBody.create(MediaType.parse(mediaType), body);
        return this;
    }

//    private PostRequestBuilder setPostStream(final String mediaType, final InputStream body) {
//
//        requestBody = new RequestBody() {
//            @Override
//            public MediaType contentType() {
//                if(mediaType == null)
//                    return null;
//                return MediaType.parse(mediaType);
//            }
//
//            @Override
//            public void writeTo(BufferedSink sink) throws IOException {
//                Source source = null;
//                try {
//                    source = Okio.source(body);
//                    sink.writeAll(source);
//                } finally {
//                    Util.closeQuietly(source);
//                    body.close();
//                }
//            }
//        };
//        return this;
//    }

    /**
     * 设置post的字节数组
     *
     * @param content
     * @return PostRequestBuilder
     */
    public PostRequestBuilder setBodyBytes(final byte[] content) {
        return setBodyBytes(null, content);
    }

    /**
     * 设置post的字节数组
     *
     * @param mediaType
     * @param content
     * @return PostRequestBuilder
     */
    public PostRequestBuilder setBodyBytes(final String mediaType, final byte[] content) {
        MediaType media = null;
        if (mediaType == null)
            media = null;
        else
            media = MediaType.parse(mediaType);
        requestBody = RequestBody.create(media, content);
        return this;
    }

    /**
     * 设置post的文件
     *
     * @param file
     * @return PostRequestBuilder
     */
    public PostRequestBuilder setBodyFile(final File file) {
        return setBodyFile(null, file);
    }

    /**
     * 设置post的文件
     *
     * @param mediaType
     * @param file
     * @return PostRequestBuilder
     */
    public PostRequestBuilder setBodyFile(final String mediaType, final File file) {
        MediaType media = null;
        if (mediaType == null)
            media = null;
        else
            media = MediaType.parse(mediaType);

        requestBody = RequestBody.create(media, file);
        return this;
    }

    /**
     * 设置写超时时间
     *
     * @param writeTimeOut unit is second
     * @return PostRequestBuilder
     */
    public PostRequestBuilder setWriteTimeOut(int writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    /**
     * 设置读超时时间
     *
     * @param readTimeOut unit is second
     * @return PostRequestBuilder
     */
    public PostRequestBuilder setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    /**
     * 设置进度回调
     *
     * @param reqProgressCallback
     * @return PostRequestBuilder
     */
    public PostRequestBuilder addReqProgressCallback(OkProgressCallback reqProgressCallback) {
        this.reqProgressCallback = reqProgressCallback;
        return this;
    }


    /**
     * 异步post：支持OkCallback（主线程）和Callback（工作线程）
     *
     * @param callback
     * @return Call
     */
    @Override
    public Call enqueue(Callback callback) {
        return enqueue("POST", callback);
    }

    /**
     * 同步post
     *
     * @return Response
     * @throws IOException
     */
    @Override
    public Response execute() throws IOException {
        return execute("POST");
    }


}

