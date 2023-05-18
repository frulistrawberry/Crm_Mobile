package com.baihe.common.manager;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author：xubo
 * Time：2020-05-07
 * Description：
 */
public class TaskManager {
    private static final int THREAD_MAX_NUMBER = 10;

    private static class Holder {
        private static final TaskManager INSTANCE = new TaskManager();
    }

    public static TaskManager newInstance() {
        return Holder.INSTANCE;
    }

    private ExecutorService executorService;
    private Handler handler;

    private TaskManager() {
        executorService = Executors.newFixedThreadPool(THREAD_MAX_NUMBER);
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * 执行主线程任务
     *
     * @param runnable
     */
    public void runOnUi(Runnable runnable) {
        handler.post(runnable);
    }

    /**
     * 延迟执行主线程任务
     *
     * @param runnable
     * @param delayMillis
     */
    public void runOnUi(Runnable runnable, long delayMillis) {
        handler.postDelayed(runnable, delayMillis);
    }

    /**
     * 执行后台线程任务
     *
     * @param task
     */
    public void runOnBackground(Task task) {
        executorService.execute(task);
    }

    /**
     * 延迟执行后台线程任务
     *
     * @param task
     * @param delayMillis
     */
    public void runOnBackground(final Task task, long delayMillis) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                executorService.execute(task);
            }
        };
        task.setCancelTag(runnable);
        handler.postDelayed(runnable, delayMillis);
    }

    /**
     * 取消未执行的延迟任务
     *
     * @param runnable
     */
    public void cancelTask(Runnable runnable) {
        if (runnable instanceof Task) {
            Task task = (Task) runnable;
            Runnable tag = task.getCancelTag();
            if (tag != null) {
                handler.removeCallbacks(tag);
            } else {
                handler.removeCallbacks(runnable);
            }
        } else {
            handler.removeCallbacks(runnable);
        }
    }

}
