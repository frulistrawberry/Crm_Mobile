package com.baihe.lib_framework.utils;

import static android.content.Context.INPUT_METHOD_SERVICE;
import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 键盘工具类
 *
 * 本文作者：谷哥的小弟
 * 博客地址：http://blog.csdn.net/lfdfhl
 */

public class KeyboardUtils {
    /**
     * 显示软键盘
     */
    public static void showInputMethod(View view) {
        if (view == null) {
            return;
        }
        if (view instanceof EditText) {
            view.requestFocus();
        }
        InputMethodManager inputmethodManager = (InputMethodManager) view.getContext()
                .getSystemService(INPUT_METHOD_SERVICE);
        if (inputmethodManager != null) {
            inputmethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 特定时间后显示软键盘
     */
    public static void showInputMethod(final View view, long delayMillis) {
        if (view != null) {
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showInputMethod(view);
                }
            }, delayMillis);
        }
    }

    /**
     * 显示软键盘
     */
    public static void showInputMethodForce(TextView textView) {
        if (textView == null) {
            return;
        }
        textView.setCursorVisible(true);
        InputMethodManager inputmethodManager = (InputMethodManager) textView.getContext().getSystemService(INPUT_METHOD_SERVICE);
        inputmethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideInputMethod(View view) {
        try {
            InputMethodManager inputmethodManager = (InputMethodManager) view.getContext()
                    .getSystemService(INPUT_METHOD_SERVICE);
            if (inputmethodManager != null) {
                inputmethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 强制隐藏软键盘
     * @param context 当前Activity
     */
    public static void hideInputMethod(Context context) {
        if (context instanceof Activity) {
            hideInputMethod(((Activity)context));
        }
    }

    /**
     * 强制隐藏软键盘
     * @param activity 当前Activity
     */
    public static void hideInputMethod(Activity activity) {
        InputMethodManager inputmethodManager = (InputMethodManager) activity
                .getSystemService(INPUT_METHOD_SERVICE);
        if (inputmethodManager != null && inputmethodManager.isActive()) {
            IBinder binder = activity.getWindow().getDecorView().getWindowToken();
            inputmethodManager.hideSoftInputFromWindow(binder, 0);
        }
    }


}

