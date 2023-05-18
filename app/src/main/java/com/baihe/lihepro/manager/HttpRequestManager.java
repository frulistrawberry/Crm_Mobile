package com.baihe.lihepro.manager;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author：xubo
 * Time：2020-08-28
 * Description：
 */
public class HttpRequestManager implements Observer {
    public enum HttpStatus {
        //未开始
        UN_START,
        //成功
        SUCCESS,
        //网络服务器错误
        NET_FAIL,
        //网络错误
        NET_ERROR,
        //重新开始
        RE_START;
    }

    private static final int THREAD_MAX_NUMBER = 5;

    private Queue<HttpTask> taskQueue = new LinkedList<>();
    private Map<String, TaskCallBack> taskCallBackMap = new HashMap<>();
    private CallBack callBack;
    private HttpStatus httpStatus;
    private boolean isQue;

    private ExecutorService executorService;
    private Handler handler;

    private HttpRequestManager() {
        this.httpStatus = HttpStatus.UN_START;
        executorService = Executors.newFixedThreadPool(THREAD_MAX_NUMBER);
        handler = new Handler(Looper.getMainLooper());
    }

    public static HttpRequestManager newInstance() {
        return new HttpRequestManager();
    }

    public HttpRequestManager get(HttpTask task) {
        return this.get(task, null);
    }

    public HttpRequestManager get(HttpTask task, TaskCallBack taskCallBack) {
        task.setMethod(HttpTask.HttpMethod.GET);
        task.setToastError(taskCallBack.isToastError());
        task.addObserver(this);
        taskQueue.offer(task);
        if (taskCallBack != null) {
            taskCallBackMap.put(task.getTaskId(), taskCallBack);
        }
        return this;
    }

    public HttpRequestManager post(HttpTask task) {
        return this.post(task, null);
    }

    public HttpRequestManager post(HttpTask task, TaskCallBack taskCallBack) {
        task.setMethod(HttpTask.HttpMethod.POST);
        task.setToastError(taskCallBack.isToastError());
        task.addObserver(this);
        taskQueue.offer(task);
        if (taskCallBack != null) {
            taskCallBackMap.put(task.getTaskId(), taskCallBack);
        }
        return this;
    }

    public void execute() {
        this.execute(null);
    }

    public void execute(CallBack callBack) {
        this.callBack = callBack;
        for (HttpTask task : taskQueue) {
            executorService.execute(task);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof HttpTask) {
            HttpTask httpTask = (HttpTask) o;
            if ((httpStatus == HttpStatus.UN_START || httpStatus == HttpStatus.RE_START) && httpTask.getRequestStatus() == HttpTask.RequestStatus.REQUEST_ING) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.before();
                        }
                    }
                });
            } else if (checkRequestEnd()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            callBack.after();
                        }
                    }
                });
            }

            //已经有错误页，不能再更新状态
            if (this.httpStatus == HttpStatus.NET_ERROR || this.httpStatus == HttpStatus.NET_FAIL) {
                return;
            }
            if (arg != null && arg instanceof String && httpTask.getHttpStatus() == HttpTask.HttpStatus.SUCCESS) {
                if (taskCallBackMap.containsKey(httpTask.getTaskId())) {
                    String response = (String) arg;
                    TaskCallBack taskCallBack = taskCallBackMap.get(httpTask.getTaskId());
                    boolean isSuccess = taskCallBack.ex(response);
                    if (isSuccess) {
                        if (checkHttpsSuccess()) {
                            setHttpStatus(HttpStatus.SUCCESS);
                        }
                    } else {
                        httpTask.setHttpStatus(HttpTask.HttpStatus.NET_FAIL);
                        setHttpStatus(HttpStatus.NET_FAIL);
                    }
                } else {
                    if (checkHttpsSuccess()) {
                        setHttpStatus(HttpStatus.SUCCESS);
                    }
                }
            } else if (httpTask.getHttpStatus() == HttpTask.HttpStatus.NET_FAIL) {
                setHttpStatus(HttpStatus.NET_FAIL);
            } else if (httpTask.getHttpStatus() == HttpTask.HttpStatus.NET_ERROR) {
                setHttpStatus(HttpStatus.NET_ERROR);
            }

        }
    }

    public void setHttpStatus(final HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (httpStatus == HttpStatus.NET_ERROR) {
                    if (callBack != null) {
                        callBack.netError();
                    }
                } else if (httpStatus == HttpStatus.NET_FAIL) {
                    if (callBack != null) {
                        callBack.netFail();
                    }
                } else if (httpStatus == HttpStatus.SUCCESS) {
                    if (callBack != null) {
                        callBack.success();
                    }
                }
            }
        });
    }

    /**
     * 检查所有请求是否结束
     *
     * @return
     */
    private boolean checkRequestEnd() {
        for (HttpTask task : taskQueue) {
            if (!task.getRequestStatus().equals(HttpTask.RequestStatus.REQUEST_END)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查所有请求是否处理成功
     *
     * @return
     */
    private boolean checkHttpsSuccess() {
        for (HttpTask task : taskQueue) {
            if (!task.getHttpStatus().equals(HttpTask.HttpStatus.SUCCESS)) {
                return false;
            }
        }
        return true;
    }

    public static abstract class TaskCallBack<Result> {
        public abstract Result doInBackground(String response);

        public abstract void success(Result result);

        public boolean isToastError() {
            return true;
        }

        public final boolean ex(String response) {
            Result result = null;
            try {
                result = doInBackground(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result != null) {
                success(result);
                return true;
            } else {
                return false;
            }
        }
    }

    public interface CallBack {
        void before();

        void success();

        void netError();

        void netFail();

        void after();
    }
}
