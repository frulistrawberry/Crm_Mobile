package com.baihe.lihepro.manager;

import com.baihe.http.HttpRequest;
import com.baihe.http.callback.CallBack;

import java.util.Observable;
import java.util.UUID;

/**
 * Author：xubo
 * Time：2020-08-28
 * Description：
 */
public class HttpTask<Result> extends Observable implements Runnable {
    public enum HttpMethod {
        GET,
        POST;
    }

    public enum HttpStatus {
        //未开始
        UN_START,
        //成功
        SUCCESS,
        //服务器错误
        NET_FAIL,
        //网络错误
        NET_ERROR,
    }

    public enum RequestStatus {
        REQUEST_READY,
        REQUEST_ING,
        REQUEST_END;
    }

    private HttpRequest httpRequest;
    private String taskId;
    private HttpMethod method;
    private RequestStatus requestStatus;
    private HttpStatus httpStatus;
    private boolean isToastError;
    private CallBack<String> callBack = new CallBack<String>() {

        @Override
        public String doInBackground(String response) {
            return response;
        }

        @Override
        public void success(String response) {
            HttpTask.this.httpStatus = HttpStatus.SUCCESS;
        }

        @Override
        public void error() {
            HttpTask.this.httpStatus = HttpStatus.NET_ERROR;
        }

        @Override
        public void fail() {
            HttpTask.this.httpStatus = HttpStatus.NET_FAIL;
        }

        @Override
        public void before() {
            super.before();
            setRequestStatus(RequestStatus.REQUEST_ING, null);
        }

        @Override
        public void after(String response) {
            super.after(response);
            setRequestStatus(RequestStatus.REQUEST_END, response);
        }

        @Override
        public boolean isToastError() {
            return isToastError;
        }
    };

    public static HttpTask create(HttpRequest httpRequest){
        return new HttpTask(httpRequest);
    }

    public HttpTask(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;

        this.taskId = UUID.randomUUID().toString();
        this.requestStatus = RequestStatus.REQUEST_READY;
        this.httpStatus = HttpStatus.UN_START;
        this.isToastError = true;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getTaskId() {
        return taskId;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setToastError(boolean toastError) {
        isToastError = toastError;
    }

    public void setRequestStatus(RequestStatus requestStatus, String response) {
        this.requestStatus = requestStatus;
        setChanged();
        notifyObservers(response);
    }

    @Override
    public void run() {
        if (method == HttpMethod.POST) {
            httpRequest.post(callBack);
        } else {
            httpRequest.get(callBack);
        }
    }
}
