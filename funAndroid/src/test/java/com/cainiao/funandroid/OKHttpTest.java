package com.cainiao.funandroid;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class OKHttpTest {

    @Test
    public void testGet() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Request request = new Request.Builder()
                .url("http://httpbin.org/get?id=id666")
                .build();

        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();

            // Response : okhttp3.internal.http.RealResponseBody@6bdf28bb
            System.out.println("Response : " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Test
    public void testPost() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();


        // post 需要Request 有请求体 指定请求体的数据格式
        // （1）请求体的数据格式
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        // （2）请求体
        String json = "{\"name\":\"funAndroid\"}";


        RequestBody requestBody = RequestBody.create(mediaType, json);


        // 构建Request
        Request request = new Request.Builder()
                .url("http://httpbin.org/post") // http协议的 请求行
                //.header() 请求头
                .post(requestBody)  // 请求体
                .build();


        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();

            // Response : okhttp3.internal.http.RealResponseBody@6bdf28bb
            System.out.println("Response : " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 测试拦截器
     */
    @Test
    public void testInterceptor() {
        // 一、定义拦截器
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                long start = System.currentTimeMillis();
                // 截获请求
                Request request = chain.request();
                // 然后再继续请求 获取到 响应
                Response response = chain.proceed(request);
                long end = System.currentTimeMillis();

                System.out.println("时间耗时 ：" + (end - start));
                return response;
            }
        };

        // 二、执行请求
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();


        // post 需要Request 有请求体 指定请求体的数据格式
        // （1）请求体的数据格式
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        // （2）请求体
        String json = "{\"name\":\"funAndroid\"}";


        RequestBody requestBody = RequestBody.create(mediaType, json);


        // 构建Request
        Request request = new Request.Builder()
                .url("http://httpbin.org/post") // http协议的 请求行
                //.header() 请求头
                .post(requestBody)  // 请求体
                .build();


        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();

            // Response : okhttp3.internal.http.RealResponseBody@6bdf28bb
            System.out.println("Response : " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
