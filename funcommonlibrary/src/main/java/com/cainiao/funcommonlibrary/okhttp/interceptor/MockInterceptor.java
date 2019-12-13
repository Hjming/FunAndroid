package com.cainiao.funcommonlibrary.okhttp.interceptor;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;

public class MockInterceptor implements Interceptor {

    private File mockFileDir;

    public MockInterceptor(File file) {
        this.mockFileDir = file;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        Response response = null;

        try {
            if (mockFileDir != null && mockFileDir.exists() && mockFileDir.isDirectory()) {
                HttpUrl url = request.url();
                String path = url.encodedPath().substring(1);
                path = path.replace("/", "#");

                if (TextUtils.isEmpty(path)) {
                    path = "blank";
                }

                final String finalPath = path;
                File[] files = mockFileDir.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        return filename.equals(finalPath + ".json");
                    }
                });

                if (files != null && files.length > 0) {
                    File file = files[0];

                    BufferedSource bufferedSource = Okio.buffer(Okio.source(file));
                    String mockStr = bufferedSource.readUtf8();

                    JSONArray mockRespJSONArray = JSON.parseArray(mockStr);
                    int mockStatusCode = 200;
                    String mockRespStr = "";
                    MediaType mediaType = null;

                    if (mockRespJSONArray != null && mockRespJSONArray.size() > 0) {
                        for (Object jsonObject : mockRespJSONArray) {
                            JSONObject mockRespJSONObject = (JSONObject) jsonObject;

                            if (mockRespJSONObject.containsKey("params")) {
                                // 设置了参数过滤
                                boolean flag = true;
                                JSONObject paramsJSONObject = mockRespJSONObject.getJSONObject("params");
                                Set<String> paramNames = paramsJSONObject.keySet();
                                for (String name : paramNames) {
                                    String paramValue = url.queryParameter(name);
                                    if (paramValue == null || !paramValue.equals(paramsJSONObject.getString(name))) {
                                        flag = false;
                                        break;
                                    }
                                }

                                if (flag) {
                                    // 参数匹配
                                    mockStatusCode = mockRespJSONObject.containsKey("code") ? mockRespJSONObject.getInteger("code") : 200;
                                    mockRespStr = mockRespJSONObject.containsKey("resp") ? mockRespJSONObject.getString("resp") : "";
                                    mediaType = MediaType.parse(mockRespJSONObject.containsKey("mediaType") ? mockRespJSONObject.getString("mediaType") : "text/plain");
                                    response = new Response.Builder()
                                            .code(mockStatusCode)
                                            .body(ResponseBody.create(mediaType, mockRespStr))
                                            .message("the response is from Mock-Data!")
                                            .request(chain.request())
                                            .protocol(Protocol.HTTP_1_1)
                                            .build();
                                    break;
                                } else {
                                    // 参数未匹配
                                    continue;
                                }
                            } else {
                                // 未设置参数过滤
                                mockStatusCode = mockRespJSONObject.containsKey("code") ? mockRespJSONObject.getInteger("code") : 200;
                                mockRespStr = mockRespJSONObject.containsKey("resp") ? mockRespJSONObject.getString("resp") : "";
                                mediaType = MediaType.parse(mockRespJSONObject.containsKey("mediaType") ? mockRespJSONObject.getString("mediaType") : "text/plain");
                                response = new Response.Builder()
                                        .code(mockStatusCode)
                                        .body(ResponseBody.create(mediaType, mockRespStr))
                                        .message("the response is from Mock-Data!")
                                        .request(chain.request())
                                        .protocol(Protocol.HTTP_1_1)
                                        .build();
                            }


                        }
                    }


                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response == null) {
            // 未匹配到模拟请求
            response = chain.proceed(request);
        }


        return response;
    }
}