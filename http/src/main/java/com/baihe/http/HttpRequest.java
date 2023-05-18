package com.baihe.http;

import android.text.TextUtils;

import com.baihe.common.log.LogUtils;
import com.baihe.http.interceptor.ResultInterceptor;
import com.orhanobut.logger.Logger;

import com.baihe.http.callback.CallBack;
import com.baihe.http.callback.DownloadCallBack;
import com.baihe.http.callback.HeadCallBack;
import com.baihe.http.callback.UploadCallBack;
import com.baihe.http.processor.ResultDecryptProcessor;
import com.baihe.http.processor.ResultPreProcessor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Author：xubo
 * Time：2018-12-28
 * Description：
 */
public class HttpRequest<Result> {
    private HttpUrl baseUrl;
    private OkHttpClient okHttpClient;
    private Map<String, String> params;
    private Platform platform;
    private Object tag;
    private ResultInterceptor partResultInterceptor;
    private Request.Builder bulder;
    private int repeatRequestCount;
    private String requestAlias;
    private boolean addCommonHeader;
    private boolean addCommonParms;

    private HttpRequest(HttpUrl baseUrl, OkHttpClient okHttpClient) {
        this.baseUrl = baseUrl;
        this.okHttpClient = okHttpClient;
        this.params = new HashMap<>();
        this.platform = Platform.get();
        this.tag = UUID.randomUUID().toString();
        this.bulder = new Request.Builder();
        this.repeatRequestCount = Http.getInstance().getRepeatRequestCount();
        this.requestAlias = "";
        this.addCommonHeader = true;
        this.addCommonParms = true;
    }

    /**
     * 设置tag标记
     *
     * @param tag
     * @return
     */
    public HttpRequest tag(Object tag) {
        this.tag = tag;
        return this;
    }

    /**
     * 设置请求失败时重复请求次数
     *
     * @param count
     * @return
     */
    public HttpRequest setRepeatRequestCount(int count) {
        this.repeatRequestCount = count;
        return this;
    }

    /**
     * 设置请求别名，为打印用
     *
     * @param requestAlias
     * @return
     */
    public HttpRequest requestAlias(String requestAlias) {
        this.requestAlias = requestAlias;
        return this;
    }

    /**
     * 不添加公共请求头
     *
     * @return
     */
    public HttpRequest noAddCommonHeader() {
        addCommonHeader = false;
        return this;
    }

    /**
     * 不添加公共参数
     *
     * @return
     */
    public HttpRequest noAddCommonParms() {
        addCommonParms = false;
        return this;
    }

    /**
     * 设置局部结果拦截器
     *
     * @param partResultInterceptor
     * @return
     */
    public HttpRequest setPartResultInterceptor(ResultInterceptor partResultInterceptor) {
        this.partResultInterceptor = partResultInterceptor;
        return this;
    }

    /**
     * 设置连接超时时间
     *
     * @param timeout 超时时间（ms）
     * @return
     */
    public HttpRequest connectTimeout(long timeout) {
        okHttpClient = okHttpClient.newBuilder().connectTimeout(timeout, TimeUnit.MILLISECONDS).build();
        return this;
    }

    /**
     * 设置读数据超时时间（对应响应或下载）
     *
     * @param timeout 超时时间（ms）
     * @return
     */
    public HttpRequest readTimeout(long timeout) {
        okHttpClient = okHttpClient.newBuilder().readTimeout(timeout, TimeUnit.MILLISECONDS).build();
        return this;
    }

    /**
     * 写数据超时时间（对应请求或上传）
     *
     * @param timeout 超时时间（ms）
     * @return
     */
    public HttpRequest writeTimeout(long timeout) {
        okHttpClient = okHttpClient.newBuilder().writeTimeout(timeout, TimeUnit.MILLISECONDS).build();
        return this;
    }

    /**
     * 添加请求头
     *
     * @param key
     * @param value
     * @return
     */
    public HttpRequest addHeader(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            bulder.addHeader(key, value);
        }
        return this;
    }

    /**
     * 添加请求参数
     *
     * @param key
     * @param value
     * @return
     */
    public HttpRequest putParam(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            this.params.put(key, value);
        }
        return this;
    }

    /**
     * 添加请求参数
     *
     * @param jsonParam
     * @return
     */
    public HttpRequest putParam(JsonParam jsonParam) {
        if (jsonParam != null) {
            this.params.put(jsonParam.getParamKey(), jsonParam.getParamValue());
        }
        return this;
    }

    /**
     * 添加请求参数
     *
     * @param params
     * @return
     */
    public HttpRequest putParams(Map<String, String> params) {
        if (params != null) {
            this.params.putAll(params);
        }
        return this;
    }

    /**
     * 获取tag标记
     *
     * @return
     */
    public Object getTag() {
        return tag;
    }


    /**
     * head请求（直接抛出请求，不关心结果）
     *
     * @return
     */
    public HttpRequest head() {
        return head(null);
    }

    /**
     * head请求
     *
     * @param callBack
     */
    public HttpRequest head(HeadCallBack callBack) {
        bulder.tag(tag);
        if (addCommonHeader && Http.getInstance().getHeadersInterceptor() != null) {
            okHttpClient = okHttpClient.newBuilder().addInterceptor(Http.getInstance().getHeadersInterceptor()).build();
        }
        if (addCommonParms && Http.getInstance().getParamsInterceptor() != null) {
            okHttpClient = okHttpClient.newBuilder().addInterceptor(Http.getInstance().getParamsInterceptor()).build();
        }
        if (params.size() > 0) {
            HttpUrl.Builder urlBuilder = baseUrl.newBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();
                urlBuilder.addQueryParameter(name, value);
            }
            baseUrl = urlBuilder.build();
        }
        Logger.i(LogUtils.buildLogInfo(null, requestAlias, "HEAD", URLDecoder.decode(baseUrl.toString()), null, null));
        Request request = bulder.url(baseUrl).head().build();
        headRequest(request, callBack, 0);
        return this;
    }

    /**
     * get请求（直接抛出请求，不关心结果）
     *
     * @return
     */
    public HttpRequest get() {
        return get(null);
    }

    /**
     * get请求
     *
     * @param callBack
     */
    public HttpRequest get(CallBack callBack) {
        bulder.tag(tag);
        if (addCommonHeader && Http.getInstance().getHeadersInterceptor() != null) {
            okHttpClient = okHttpClient.newBuilder().addInterceptor(Http.getInstance().getHeadersInterceptor()).build();
        }
        if (addCommonParms && Http.getInstance().getParamsInterceptor() != null) {
            okHttpClient = okHttpClient.newBuilder().addInterceptor(Http.getInstance().getParamsInterceptor()).build();
        }
        if (params.size() > 0) {
            HttpUrl.Builder urlBuilder = baseUrl.newBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();
                urlBuilder.addQueryParameter(name, value);
            }
            baseUrl = urlBuilder.build();
        }
        Request request = bulder.url(baseUrl).build();
        Logger.i(LogUtils.buildLogInfo(null, requestAlias, "GET", URLDecoder.decode(baseUrl.toString()), null, null));
        request(request, callBack, 0);
        return this;
    }

    /**
     * post请求（直接抛出请求，不关心结果）
     */
    public HttpRequest post() {
        return post(null);
    }

    /**
     * post请求
     *
     * @param callBack
     */
    public HttpRequest post(CallBack callBack) {
        bulder.tag(tag);
        if (addCommonHeader && Http.getInstance().getHeadersInterceptor() != null) {
            okHttpClient = okHttpClient.newBuilder().addInterceptor(Http.getInstance().getHeadersInterceptor()).build();
        }
        if (addCommonParms && Http.getInstance().getParamsInterceptor() != null) {
            okHttpClient = okHttpClient.newBuilder().addInterceptor(Http.getInstance().getParamsInterceptor()).build();
        }
        bulder.url(baseUrl);
        if (params.size() > 0) {
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            RequestBody body = builder.build();
            bulder.post(body);
        }
        Logger.i(LogUtils.buildLogInfo(null, requestAlias, "POST", URLDecoder.decode(baseUrl.toString()), null, null));
        LogUtils.printCollections(params);
        Request request = bulder.build();
        request(request, callBack, 0);
        return this;
    }

    /**
     * 上传
     *
     * @param uploadFileWrap
     * @param callBack
     */
    public HttpRequest upload(UploadFileWrap uploadFileWrap, UploadCallBack callBack) {
        if (uploadFileWrap == null) {
            throw new IllegalStateException("请指定上传文件");
        }
        bulder.tag(tag);
        if (addCommonHeader && Http.getInstance().getHeadersInterceptor() != null) {
            okHttpClient = okHttpClient.newBuilder().addInterceptor(Http.getInstance().getHeadersInterceptor()).build();
        }
        if (addCommonParms && Http.getInstance().getParamsInterceptor() != null) {
            okHttpClient = okHttpClient.newBuilder().addInterceptor(Http.getInstance().getParamsInterceptor()).build();
        }
        bulder.url(baseUrl);
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        multipartBuilder.setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            multipartBuilder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        MediaType mediaType = MediaType.parse(uploadFileWrap.getContentTpye());
        RequestBody body = creatUploadRequestBody(mediaType, uploadFileWrap.getUploadFile(), callBack);
        multipartBuilder.addFormDataPart(uploadFileWrap.getUploadFileParam(), uploadFileWrap.getUploadFile().getName(), body);
        bulder.post(multipartBuilder.build());
        Request request = bulder.build();
        //写数据超时时间小于默认上传超时时间，则设置成默认上传超时时间
        if (okHttpClient.writeTimeoutMillis() < Http.DEFAULT_UPLOAD_TIMEOUT) {
            writeTimeout(Http.DEFAULT_UPLOAD_TIMEOUT);
        }
        request(request, callBack, 0);
        return this;
    }

    /**
     * 下载
     *
     * @param savePath
     * @param callBack
     */
    public HttpRequest download(String savePath, DownloadCallBack callBack) {
        return download(new File(savePath), callBack);
    }

    /**
     * 下载
     *
     * @param saveFile
     * @param callBack
     */
    public HttpRequest download(File saveFile, DownloadCallBack callBack) {
        if (saveFile.exists()) {
            if (saveFile.isDirectory()) {
                throw new IllegalStateException("下载文件不可以是一个文件夹");
            }
            saveFile.delete();
        }
        try {
            saveFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("下载文件创建失败: " + e.toString());
        }

        bulder.tag(tag);
        if (addCommonHeader && Http.getInstance().getHeadersInterceptor() != null) {
            okHttpClient = okHttpClient.newBuilder().addInterceptor(Http.getInstance().getHeadersInterceptor()).build();
        }
        if (addCommonParms && Http.getInstance().getParamsInterceptor() != null) {
            okHttpClient = okHttpClient.newBuilder().addInterceptor(Http.getInstance().getParamsInterceptor()).build();
        }
        if (params.size() > 0) {
            HttpUrl.Builder urlBuilder = baseUrl.newBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();
                urlBuilder.addQueryParameter(name, value);
            }
            baseUrl = urlBuilder.build();
        }
        Request request = bulder.url(baseUrl).build();
        //读数据超时时间小于默认下载超时时间，则设置成默认下载超时时间
        if (okHttpClient.readTimeoutMillis() < Http.DEFAULT_DOWNLOAD_TIMEOUT) {
            readTimeout(Http.DEFAULT_DOWNLOAD_TIMEOUT);
        }
        downloadRequest(saveFile, request, callBack, 0);
        return this;
    }

    /**
     * 取消请求
     */
    public void cancel() {
        for (Call call : okHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : okHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 创建上传RequestBody
     *
     * @param mediaType
     * @param uploadFile
     * @param uploadCallBack
     * @return
     */
    private RequestBody creatUploadRequestBody(final MediaType mediaType, final File uploadFile, final UploadCallBack uploadCallBack) {
        RequestBody requestBody = new RequestBody() {

            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength() {
                return uploadFile.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(uploadFile);
                    Buffer buf = new Buffer();
                    long totalSize = contentLength();
                    long uploadSize = 0;
                    long length = 0;
                    while ((length = source.read(buf, 2048)) != -1) {
                        sink.write(buf, length);
                        uploadSize += length;
                        double progress = (double) uploadSize / totalSize;
                        if (uploadCallBack != null) {
                            uploadCallBack.uploadProgress(uploadSize, totalSize, progress);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.i(LogUtils.buildLogInfo("上传读写异常", null, null, baseUrl.toString(), null, e.toString()));
                } finally {
                    if (source != null) {
                        source.close();
                    }
                }
            }
        };
        return requestBody;
    }

    /**
     * head请求
     *
     * @param request
     * @param callBack
     * @param requestFailCount
     */
    private void headRequest(final Request request, final HeadCallBack callBack, final int requestFailCount) {
        Call call = okHttpClient.newCall(request);
        if (requestFailCount == 0 && callBack != null) {
            callBack.before();
        }
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                if (call.isCanceled()) {
                    Logger.i(LogUtils.buildLogInfo("请求取消", requestAlias, null, null, null, null));
                    if (callBack != null) {
                        platform.execute(new Runnable() {
                            @Override
                            public void run() {
                                callBack.after();
                            }
                        });
                    }
                } else {
                    if (requestFailCount < repeatRequestCount) {
                        platform.execute(new Runnable() {
                            @Override
                            public void run() {
                                int count = requestFailCount + 1;
                                Logger.i(LogUtils.buildLogInfo("重复请求第" + count + "次", requestAlias, null, null, null, null));
                                headRequest(request, callBack, count);
                            }
                        });
                    } else {
                        Logger.i(LogUtils.buildLogInfo("网络异常", requestAlias, null, null, null, e.toString()));
                        if (callBack != null) {
                            platform.execute(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.error();
                                    callBack.after();
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onResponse(final Call call, Response response) {
                if (call.isCanceled()) {
                    Logger.i(LogUtils.buildLogInfo("请求取消", requestAlias, null, null, null, null));
                    if (callBack != null) {
                        platform.execute(new Runnable() {
                            @Override
                            public void run() {
                                callBack.after();
                            }
                        });
                    }
                } else {
                    if (response.isSuccessful()) {
                        if (callBack != null) {
                            platform.execute(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.success();
                                    callBack.after();
                                }
                            });
                        }
                    } else {
                        Logger.i(LogUtils.buildLogInfo("服务器异常", requestAlias, null, null, response.code() + "", null));
                        if (callBack != null) {
                            platform.execute(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.fail();
                                    callBack.after();
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    /**
     * http请求
     *
     * @param request
     * @param callBack
     * @param requestFailCount
     */
    private void request(final Request request, final CallBack<Result> callBack, final int requestFailCount) {
        Call call = okHttpClient.newCall(request);
        if (requestFailCount == 0 && callBack != null) {
            callBack.before();
        }
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (call.isCanceled()) {
                    if (callBack != null && callBack instanceof UploadCallBack) {
                        Logger.i(LogUtils.buildLogInfo("上传取消", requestAlias, null, null, null, null));
                        final UploadCallBack uploadCallBack = (UploadCallBack) callBack;
                        platform.execute(new Runnable() {
                            @Override
                            public void run() {
                                uploadCallBack.uploadCancel();
                            }
                        });
                    } else {
                        Logger.i(LogUtils.buildLogInfo("请求取消", requestAlias, null, null, null, null));
                    }
                    if (callBack != null) {
                        platform.execute(new Runnable() {
                            @Override
                            public void run() {
                                callBack.after(null);
                            }
                        });
                    }
                } else {
                    if (requestFailCount < repeatRequestCount) {
                        platform.execute(new Runnable() {
                            @Override
                            public void run() {
                                int count = requestFailCount + 1;
                                Logger.i(LogUtils.buildLogInfo("重复请求第" + count + "次", requestAlias, null, null, null, null));
                                request(request, callBack, count);
                            }
                        });
                    } else {
                        if (callBack != null && callBack instanceof UploadCallBack) {
                            Logger.i(LogUtils.buildLogInfo("网络异常,上传失败", requestAlias, null, null, null, e.toString()));
                        } else {
                            Logger.i(LogUtils.buildLogInfo("网络异常", requestAlias, null, null, null, e.toString()));
                        }
                        if (callBack != null) {
                            platform.execute(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.error();
                                    callBack.after(null);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (call.isCanceled()) {
                    if (callBack != null && callBack instanceof UploadCallBack) {
                        Logger.i(LogUtils.buildLogInfo("上传取消", requestAlias, null, null, null, null));
                        final UploadCallBack uploadCallBack = (UploadCallBack) callBack;
                        platform.execute(new Runnable() {
                            @Override
                            public void run() {
                                uploadCallBack.uploadCancel();
                            }
                        });
                    } else {
                        Logger.i(LogUtils.buildLogInfo("请求取消", requestAlias, null, null, null, null));
                    }
                    if (callBack != null) {
                        platform.execute(new Runnable() {
                            @Override
                            public void run() {
                                callBack.after(null);
                            }
                        });
                    }
                } else {
                    if (response.isSuccessful()) {
                        try {
                            String responseString = response.body().string();
                            ResultDecryptProcessor resultDecryptProcessor = Http.getInstance().getResultDecryptProcessor();
                            if (resultDecryptProcessor != null) {
                                responseString = resultDecryptProcessor.decrypt(responseString);
                            }
                            LogUtils.printJson(responseString);
                            final ResultInterceptor resultInterceptor = Http.getInstance().getResultInterceptor();
                            if (partResultInterceptor != null && partResultInterceptor.isIntercept(responseString)) {
                                partResultInterceptor.interceptAction(responseString);
                                if (callBack != null) {
                                    platform.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            callBack.after(null);
                                        }
                                    });
                                }
                            } else if (resultInterceptor != null && resultInterceptor.isIntercept(responseString)) {
                                resultInterceptor.interceptAction(responseString);
                                if (callBack != null) {
                                    platform.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            callBack.after(null);
                                        }
                                    });
                                }
                            } else {
                                ResultPreProcessor resultPreProcessor = Http.getInstance().getResultPreProcessor();
                                if (resultPreProcessor != null) {
                                    responseString = resultPreProcessor.process(responseString, callBack != null ? callBack.isToastError() : false);
                                    if (resultPreProcessor.interrupt(responseString)) {
                                        if (callBack != null) {
                                            platform.execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    callBack.after(null);
                                                }
                                            });
                                        }
                                        return;
                                    }
                                }
                                final Result result = callBack.doInBackground(responseString);
                                if (callBack != null) {
                                    platform.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (result != null) {
                                                callBack.success(result);
                                            } else {
                                                callBack.fail();
                                            }
                                            callBack.after(result);
                                        }
                                    });
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Logger.i(LogUtils.buildLogInfo("请求数据处理异常", requestAlias, null, null, null, e.toString()));
                            if (callBack != null) {
                                platform.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callBack.fail();
                                        callBack.after(null);
                                    }
                                });
                            }
                        }
                    } else {
                        Logger.i(LogUtils.buildLogInfo("服务器异常", requestAlias, null, null, response.code() + "", null));
                        if (callBack != null) {
                            platform.execute(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.fail();
                                    callBack.after(null);
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    /**
     * 下载请求
     *
     * @param saveFile
     * @param request
     * @param callBack
     * @param requestFailCount
     */
    private void downloadRequest(final File saveFile, final Request request, final DownloadCallBack callBack, final int requestFailCount) {
        Call call = okHttpClient.newCall(request);
        if (requestFailCount == 0 && callBack != null) {
            callBack.downloadReady();
        }
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (call.isCanceled()) {
                    Logger.i(LogUtils.buildLogInfo("下载取消", requestAlias, null, null, null, null));
                    if (callBack != null) {
                        platform.execute(new Runnable() {
                            @Override
                            public void run() {
                                callBack.downloadCancel();
                            }
                        });
                    }
                } else {
                    if (requestFailCount < repeatRequestCount) {
                        platform.execute(new Runnable() {
                            @Override
                            public void run() {
                                int count = requestFailCount + 1;
                                Logger.i(LogUtils.buildLogInfo("重复下载第" + count + "次", requestAlias, null, null, null, null));
                                downloadRequest(saveFile, request, callBack, count);
                            }
                        });
                    } else {
                        Logger.i(LogUtils.buildLogInfo("网络异常,下载失败", requestAlias, null, null, null, e.toString()));
                        if (callBack != null) {
                            platform.execute(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.downloadFail();
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onResponse(final Call call, Response response) {
                if (call.isCanceled()) {
                    Logger.i(LogUtils.buildLogInfo("下载取消", requestAlias, null, null, null, null));
                    if (callBack != null) {
                        platform.execute(new Runnable() {
                            @Override
                            public void run() {
                                callBack.downloadCancel();
                            }
                        });
                    }
                } else {
                    if (response.isSuccessful()) {
                        InputStream is = null;
                        FileOutputStream fos = null;
                        try {
                            ResponseBody responseBody = response.body();
                            is = responseBody.byteStream();
                            fos = new FileOutputStream(saveFile);

                            long downloadSize = 0;
                            final long totalSize = responseBody.contentLength();
                            byte[] buf = new byte[2048];
                            int length = 0;
                            while ((length = is.read(buf)) != -1) {
                                downloadSize += length;
                                fos.write(buf, 0, length);
                                final double progress = (double) downloadSize / totalSize;
                                final long finalDownloadSize = downloadSize;
                                if (callBack != null) {
                                    platform.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            callBack.downloadProgress(finalDownloadSize, totalSize, progress);
                                        }
                                    });
                                }
                            }
                            fos.flush();
                            Logger.i(LogUtils.buildLogInfo("下载完成", requestAlias, null, null, null, null));
                            if (callBack != null) {
                                platform.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callBack.downloadSuccess();
                                    }
                                });
                            }
                        } catch (final Exception e) {
                            e.printStackTrace();
                            if (call.isCanceled()) {
                                Logger.i(LogUtils.buildLogInfo("下载取消", requestAlias, null, null, null, null));
                                if (callBack != null) {
                                    platform.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            callBack.downloadCancel();
                                        }
                                    });
                                }
                            } else {
                                Logger.i(LogUtils.buildLogInfo("读写异常,下载失败", requestAlias, null, null, null, e.toString()));
                                if (callBack != null) {
                                    platform.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            callBack.downloadFail();
                                        }
                                    });
                                }
                            }
                        } finally {
                            try {
                                if (is != null) {
                                    is.close();
                                }
                                if (fos != null) {
                                    fos.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        Logger.i(LogUtils.buildLogInfo("服务器异常,下载失败", requestAlias, null, null, response.code() + "", null));
                        if (callBack != null) {
                            platform.execute(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.downloadFail();
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    /**
     * 创建HttpRequest
     *
     * @param baseUrl
     * @return
     */
    public static HttpRequest create(String baseUrl) {
        return newBuilder().baseUrl(baseUrl).builder();
    }

    /**
     * 创建HttpRequest
     *
     * @param baseUrl
     * @return
     */
    public static HttpRequest create(HttpUrl baseUrl) {
        return newBuilder().baseUrl(baseUrl).builder();
    }

    /**
     * 创建HttpRequest
     *
     * @param baseUrl
     * @param okHttpClient
     * @return
     */
    public static HttpRequest create(String baseUrl, OkHttpClient okHttpClient) {
        return newBuilder().baseUrl(baseUrl).client(okHttpClient).builder();
    }

    /**
     * 创建HttpRequest
     *
     * @param baseUrl
     * @param okHttpClient
     * @return
     */
    public static HttpRequest create(HttpUrl baseUrl, OkHttpClient okHttpClient) {
        return newBuilder().baseUrl(baseUrl).client(okHttpClient).builder();
    }

    private static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private OkHttpClient okHttpClient;
        private HttpUrl baseUrl;

        public Builder client(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public Builder baseUrl(String url) {
            if (url == null || "".equals(url.trim())) {
                throw new IllegalArgumentException("baseUrl不能为空");
            }
            HttpUrl httpUrl = HttpUrl.parse(url);
            return baseUrl(httpUrl);
        }

        public Builder baseUrl(HttpUrl url) {
            if (url == null) {
                throw new IllegalArgumentException("baseUrl不能为空");
            }
            this.baseUrl = url;
            return this;
        }

        public HttpRequest builder() {
            if (baseUrl == null) {
                throw new IllegalArgumentException("baseUrl为空,HttpRequest对象创建失败");
            }
            if (okHttpClient == null) {
                //使用默认即可
                //未设置okHttpClient,则用默认的OkHttpClientBuiler创建okHttpClient
                okHttpClient = Http.getInstance().getOkHttpClientBuiler().build();
            }
            return new HttpRequest(baseUrl, okHttpClient);
        }
    }
}
