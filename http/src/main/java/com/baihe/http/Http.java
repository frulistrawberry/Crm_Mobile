package com.baihe.http;

import androidx.annotation.Size;

import com.baihe.common.log.LogUtils;
import com.baihe.http.interceptor.HeadersInterceptor;
import com.baihe.http.interceptor.ResultInterceptor;
import com.baihe.http.cookie.CookieStore;
import com.baihe.http.cookie.MyCookieJar;
import com.baihe.http.interceptor.ParamsInterceptor;
import com.baihe.http.processor.ResultDecryptProcessor;
import com.baihe.http.processor.ResultPreProcessor;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Author：xubo
 * Time：2018-12-28
 * Description：
 */
public class Http {
    /**
     * 默认连接超时时间
     */
    public final static int DEFAULT_CONNECT_TIMEOUT = 15000;

    /**
     * 默认读数据超时时间
     */
    public final static int DEFAULT_READ_TIMEOUT = 15000;

    /**
     * 默认写数据超时时间
     */
    public final static int DEFAULT_WRITE_TIMEOUT = 15000;

    /**
     * 默认下载超时时间
     */
    public final static int DEFAULT_DOWNLOAD_TIMEOUT = 60000;

    /**
     * 默认上传超时时间
     */
    public final static int DEFAULT_UPLOAD_TIMEOUT = 60000;

    /**
     * 默认请求失败重新请求次数
     */
    private final static int DEFAULT_REPEAT_REQUEST_COUNT = 3;

    /**
     * 默认OkHttpClientBuiler
     */
    private OkHttpClient.Builder okHttpClientBuiler;

    /**
     * cookie存储
     */
    private CookieStore cookieStore;

    /**
     * 全局请求结果拦截器
     */
    private ResultInterceptor resultInterceptor;

    /**
     * 公共请求头拦截器
     */
    private HeadersInterceptor headersInterceptor;

    /**
     * 公共参数拦截器
     */
    private ParamsInterceptor paramsInterceptor;

    /**
     * 结果解密处理器
     */
    private ResultDecryptProcessor resultDecryptProcessor;

    /**
     * 结果前置处理器
     */
    private ResultPreProcessor resultPreProcessor;

    /**
     * 重复请求次数（请求失败时）
     */
    private int repeatRequestCount;

    private Http() {
        this.okHttpClientBuiler = new OkHttpClient.Builder();
        this.okHttpClientBuiler.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        this.okHttpClientBuiler.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS);
        this.okHttpClientBuiler.writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
        this.repeatRequestCount = DEFAULT_REPEAT_REQUEST_COUNT;




    }

    private static class HttpHolder {
        private static final Http http = new Http();
    }

    public static Http getInstance() {
        return HttpHolder.http;
    }

    /**
     * 获取默认OkHttpClientBuiler对象
     *
     * @return
     */
    public OkHttpClient.Builder getOkHttpClientBuiler() {
        return okHttpClientBuiler;
    }

    /**
     * 设置全局连接超时时间
     *
     * @param connectTimeout 超时时间（ms）
     * @return
     */
    public Http setConnectTimeout(long connectTimeout) {
        okHttpClientBuiler.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    /**
     * 设置全局读取超时时间
     *
     * @param readTimeout 超时时间（ms）
     * @return
     */
    public Http setReadTimeout(long readTimeout) {
        okHttpClientBuiler.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    /**
     * 设置全局写入超时时间
     *
     * @param writeTimeout 超时时间（ms）
     * @return
     */
    public Http setWriteTimeout(long writeTimeout) {
        okHttpClientBuiler.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    /**
     * 开启cookie存储
     *
     * @param cookieStore cookie存储
     * @return
     */
    public Http cookie(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
        okHttpClientBuiler.cookieJar(new MyCookieJar(cookieStore));
        return this;
    }

    /**
     * 获取cookie存储对象
     *
     * @return
     */
    public CookieStore getCookieStore() {
        return cookieStore;
    }

    /**
     * 设置全局结果拦截器（所有请求结果都会过滤拦截）
     *
     * @param resultInterceptor
     * @return
     */
    public Http setResultInterceptor(ResultInterceptor resultInterceptor) {
        this.resultInterceptor = resultInterceptor;
        return this;
    }

    /**
     * 获取全局结果拦截器
     *
     * @return
     */
    public ResultInterceptor getResultInterceptor() {
        return resultInterceptor;
    }

    /**
     * 获取结果解密处理器
     *
     * @return
     */
    public ResultDecryptProcessor getResultDecryptProcessor() {
        return resultDecryptProcessor;
    }

    /**
     * 设置结果解密处理器
     *
     * @param resultDecryptProcessor
     */
    public void setResultDecryptProcessor(ResultDecryptProcessor resultDecryptProcessor) {
        this.resultDecryptProcessor = resultDecryptProcessor;
    }

    /**
     * 获取结果前置处理器
     *
     * @return
     */
    public ResultPreProcessor getResultPreProcessor() {
        return resultPreProcessor;
    }

    /**
     * 设置结果前置处理器（所有请求结果都会前置处理）
     *
     * @param resultPreProcessor
     * @return
     */
    public Http setResultPreProcessor(ResultPreProcessor resultPreProcessor) {
        this.resultPreProcessor = resultPreProcessor;
        return this;
    }

    /**
     * 获取公共请求头拦截器
     *
     * @return
     */
    public HeadersInterceptor getHeadersInterceptor() {
        return headersInterceptor;
    }

    /**
     * 设置请求头拦截器
     *
     * @param headersInterceptor
     */
    public void setHeadersInterceptor(HeadersInterceptor headersInterceptor) {
        this.headersInterceptor = headersInterceptor;
    }

    /**
     * 获取公共参数拦截器
     *
     * @return
     */
    public ParamsInterceptor getParamsInterceptor() {
        return paramsInterceptor;
    }

    /**
     * 设置公共参数拦截器
     *
     * @param paramsInterceptor
     */
    public void setParamsInterceptor(ParamsInterceptor paramsInterceptor) {
        this.paramsInterceptor = paramsInterceptor;
    }

    public void setLogInterceptor(){
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.e("HttpLog",message);
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        this.okHttpClientBuiler.addInterceptor(logInterceptor);
    }

    /**
     * 设置请求失败时重复请求的次数
     *
     * @param repeatRequestCount repeatRequestCount,最小值为0
     * @return
     */
    public Http setRepeatRequestCount(@Size(min = 0) int repeatRequestCount) {
        this.repeatRequestCount = repeatRequestCount;
        return this;
    }

    /**
     * 获取请求失败时重复请求的次数
     *
     * @return
     */
    public int getRepeatRequestCount() {
        return repeatRequestCount;
    }

    /**
     * 取消tag请求
     *
     * @param tag
     */
    public void cancelTag(Object tag) {
        Dispatcher dispatcher = okHttpClientBuiler.build().dispatcher();
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

    /**
     * 取消所有请求
     */
    public void cancelAll() {
        Dispatcher dispatcher = okHttpClientBuiler.build().dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            call.cancel();
        }
        for (Call call : dispatcher.runningCalls()) {
            call.cancel();
        }
    }

    /**
     * 指定okHttpClient取消请求
     *
     * @param okHttpClient
     * @param tag
     */
    public void cancelTag(OkHttpClient okHttpClient, Object tag) {
        Dispatcher dispatcher = okHttpClient.dispatcher();
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

    /**
     * 指定okHttpClient取消所有请求
     *
     * @param okHttpClient
     */
    public void cancelAll(OkHttpClient okHttpClient) {
        Dispatcher dispatcher = okHttpClient.dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            call.cancel();
        }
        for (Call call : dispatcher.runningCalls()) {
            call.cancel();
        }
    }
}
