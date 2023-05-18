package com.baihe.http.interceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author：xubo
 * Time：2020-04-06
 * Description：公共请求头拦截器
 */
public class HeadersInterceptor implements Interceptor {
    private Map<String, String> headers = new HashMap<>();

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        //添加公共header
        if (headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        request = requestBuilder.build();
        return chain.proceed(request);
    }

    public static class Builder {
        private HeadersInterceptor interceptor;

        public Builder() {
            interceptor = new HeadersInterceptor();
        }

        public Builder addHeader(String key, String value) {
            interceptor.headers.put(key, value);
            return this;
        }

        public Builder addHeaders(Map<String, String> headers) {
            interceptor.headers.putAll(headers);
            return this;
        }

        public HeadersInterceptor build() {
            return interceptor;
        }

    }
}
