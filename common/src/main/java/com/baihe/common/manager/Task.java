package com.baihe.common.manager;

import android.os.Process;

import androidx.annotation.WorkerThread;

/**
 * Author：xubo
 * Time：2020-06-18
 * Description：
 */
public abstract class Task implements Runnable {
    private int priority = Process.THREAD_PRIORITY_BACKGROUND;
    private Runnable cancelTag;

    public Task() {

    }

    public Task(int priority) {
        this.priority = priority;
    }

    @Override
    public void run() {
        Process.setThreadPriority(priority);
        onRun();
    }

    public Runnable getCancelTag() {
        return cancelTag;
    }

    public void setCancelTag(Runnable cancelTag) {
        this.cancelTag = cancelTag;
    }

    @WorkerThread
    public abstract void onRun();
}
