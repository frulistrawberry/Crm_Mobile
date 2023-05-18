package com.baihe.http;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * Author：xubo
 * Time：2018-12-28
 * Description：
 */
public class Platform {
    private static final Platform PLATFORM = findPlatform();

    public static Platform get() {
        return PLATFORM;
    }

    private static Platform findPlatform() {
        return new Platform();
    }

    public void execute(Runnable runnable) {
        defaultCallbackExecutor().execute(runnable);
    }

    private Executor defaultCallbackExecutor() {
        return new MainThreadExecutor();
    }

    static class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable runnable) {
            handler.post(runnable);
        }
    }
}
