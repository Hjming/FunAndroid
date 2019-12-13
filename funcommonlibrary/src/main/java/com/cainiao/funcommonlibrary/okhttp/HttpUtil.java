package com.cainiao.funcommonlibrary.okhttp;

import com.cainiao.funcommonlibrary.okhttp.interceptor.LogInterceptor;
import com.cainiao.funcommonlibrary.okhttp.interceptor.LogPrintInterceptor;
import com.cainiao.funcommonlibrary.okhttp.interceptor.MockInterceptor;
import com.cainiao.funcommonlibrary.okhttp.requestbuilder.DeleteRequestBuilder;
import com.cainiao.funcommonlibrary.okhttp.requestbuilder.GetRequestBuilder;
import com.cainiao.funcommonlibrary.okhttp.requestbuilder.PostRequestBuilder;
import com.cainiao.funcommonlibrary.okhttp.requestbuilder.PutRequestBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

/**
 * time: 2019-12-13
 * Project_Name: FunAndroid
 * Package_Name: com.cainiao.funandroid.net.requestonline
 * Description:
 * Created by hjm.
 */
public class HttpUtil {

    private static OkHttpClient httpClient;
    private static OkHttpClient.Builder httpClientBuilder;


    public static OkHttpClient init() {
        synchronized (HttpUtil.class) {
            if (httpClient == null) {
                if (httpClientBuilder == null) {
                    httpClientBuilder = new OkHttpClient.Builder()
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS);
                }

                httpClient = httpClientBuilder.build();
            }
        }

        return httpClient;
    }


    public static OkHttpClient getInstance() {
        return httpClient == null ? init() : httpClient;
    }

    public static void setInstance(OkHttpClient okHttpClient) {
        HttpUtil.httpClient = okHttpClient;
    }

    public static OkHttpClient.Builder getInstanceBuilder() {
        if (httpClientBuilder == null) {
            httpClientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS);
        }
        return httpClientBuilder;
    }

    /** 封装Request请求 */

    // 封装get请求构造器
    public static GetRequestBuilder get(String url) {
        return new GetRequestBuilder().url(url);
    }

    // 封装post请求构造器
    public static PostRequestBuilder post(String url) {
        return new PostRequestBuilder().url(url);
    }

    // 封装put请求构造器
    public static PutRequestBuilder put(String url) {
        return new PutRequestBuilder().url(url);
    }

    // 封装delete请求构造器
    public static DeleteRequestBuilder delete(String url) {
        return new DeleteRequestBuilder().url(url);
    }


    public static void cancel(Object tag) {
        Dispatcher dispatcher = getInstance().dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public static void isLog(boolean isLog) {
        if (isLog) {
            getInstanceBuilder().addInterceptor(new LogInterceptor());
        }
    }

    public static void isPrintLog(boolean isPrint) {
        if (isPrint) {
            getInstanceBuilder().addInterceptor(new LogPrintInterceptor());
        }
    }


    public static void isMock(boolean isMock, File mockFileDir) {
        if (isMock && mockFileDir != null) {
            try {
                if (!mockFileDir.exists() && mockFileDir.mkdirs()) {

                }
                getInstanceBuilder().addInterceptor(new MockInterceptor(mockFileDir));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
