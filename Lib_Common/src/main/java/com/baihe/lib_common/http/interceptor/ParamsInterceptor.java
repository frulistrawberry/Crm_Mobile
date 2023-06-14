package com.baihe.lib_common.http.interceptor;


import com.baihe.lib_common.http.api.JsonParam;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author：xubo
 * Time：2020-04-06
 * Description：公共参数拦截器
 */
public class ParamsInterceptor implements Interceptor {
    private static final String METHOD_HEAD = "HEAD";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    private Map<String, String> params = new HashMap<>();

    public void setParam(String key, String value) {
        params.put(key, value);
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        //添加公共参数
        if (METHOD_HEAD.equals(request.method()) || METHOD_GET.equals(request.method())) {
            HttpUrl.Builder urlBuilder = request.url().newBuilder();
            if (params.size() > 0) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }
            }
            HttpUrl httpUrl = urlBuilder.build();
            requestBuilder.url(httpUrl);
        } else if (METHOD_POST.equals(request.method())) {
            RequestBody requestBody = request.body();
            if (params.size() > 0) {
                if (requestBody instanceof FormBody) {
                    FormBody oldFormBody = (FormBody) requestBody;
                    FormBody.Builder builder = new FormBody.Builder();
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        builder.add(entry.getKey(), entry.getValue());
                    }
                    for (int i = 0; i < oldFormBody.size(); i++) {
                        builder.add(oldFormBody.encodedName(i), oldFormBody.encodedValue(i));
                    }
                    RequestBody body = builder.build();
                    requestBuilder.post(body);
                } else if (requestBody instanceof MultipartBody) {
                    MultipartBody oldMultipartBody = (MultipartBody) requestBody;
                    MultipartBody.Builder builder = new MultipartBody.Builder();
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        builder.addFormDataPart(entry.getKey(), entry.getValue());
                    }
                    for (int i = 0; i < oldMultipartBody.size(); i++) {
                        builder.addPart(oldMultipartBody.part(i));
                    }
                    builder.setType(oldMultipartBody.type());
                    RequestBody body = builder.build();
                    requestBuilder.post(body);
                }
            } else {
                requestBuilder.post(requestBody);
            }
        }
        request = requestBuilder.build();
        return chain.proceed(request);
    }

    public static class Builder {
        private ParamsInterceptor interceptor;

        public Builder() {
            interceptor = new ParamsInterceptor();
        }


        public Builder addParam(String key, String value) {
            interceptor.params.put(key, value);
            return this;
        }

        public Builder addParams(Map<String, String> params) {
            interceptor.params.putAll(params);
            return this;
        }

        public ParamsInterceptor build() {
            return interceptor;
        }

    }
}
