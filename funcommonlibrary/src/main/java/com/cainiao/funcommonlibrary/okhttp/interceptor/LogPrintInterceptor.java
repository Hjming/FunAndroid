package com.cainiao.funcommonlibrary.okhttp.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.cainiao.funcommonlibrary.BuildConfig;
import com.cainiao.funcommonlibrary.util.LogUtil;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LogPrintInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        LogUtil.e("OKHttp","==="+request.url());
        Response response = chain.proceed(request);
        LogUtil.e("OKHttp","==="+response.code()+":"+response.message());
        LogUtil.e("OKHttp","==="+response.peekBody(1024).string());
        /**获取请求返回打印到日志中去*/
        if(BuildConfig.DEBUG) {
            JSONObject obj = new JSONObject();
            obj.put("url", response.request().url().toString() + "");
            if(request != null) {
                obj.put("webType", request.method() + "");
                if ("POST".equals(request.method())) {//Post请求获取参数
                    StringBuilder sb = new StringBuilder();
                    sb.append("{");
                    if (request.body() instanceof FormBody) {
                        FormBody body = (FormBody) request.body();
                        for (int i = 0; i < body.size(); i++) {
                            sb.append("\"" + body.encodedName(i) + "\":\"" + body.encodedValue(i) + "\",");
                        }
                        sb.delete(sb.length() - 1, sb.length());
                        sb.append("}");
                        obj.put("params", sb.toString());
                    }
                }
            }

            obj.put("code",response.code() + "");
            obj.put("resp", response.peekBody(1024 * 1024).string() + "");
            String url = obj.get("url").toString();
            if(url.substring(url.length()-5,url.length()).equals(".webp")){
                LogUtil.d("webp",url);
            }
            if(!url.substring(url.length()-4,url.length()).equals(".jpg")
                    && !url.substring(url.length()-4,url.length()).equals(".png")
                    && !url.substring(url.length()-4,url.length()).equals(".JPG")
                    && !url.substring(url.length()-4,url.length()).equals(".PNG")
                    && !url.substring(url.length()-5,url.length()).equals(".jpeg")
                    && !url.substring(url.length()-5,url.length()).equals(".webp")) {
                LogUtil.writeWebLog(obj);
            }
        }
        return response;
    }
}