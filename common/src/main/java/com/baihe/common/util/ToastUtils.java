package com.baihe.common.util;

import android.widget.Toast;

import com.baihe.common.base.BaseApplication;

/**
 * Author：xubo
 * Time：2018-08-04
 * Description：toast工具类
 */

public class ToastUtils {

    /**
     * 纯文本toast
     *
     * @param text
     * @param duration
     */
    public static void toast(String text, int duration) {
        BaseApplication.MyToast.builder.display(text, duration);
    }

    /**
     * 纯文本toast
     *
     * @param text
     */
    public static void toast(String text) {
        BaseApplication.MyToast.builder.display(text, Toast.LENGTH_SHORT);
    }

    /**
     * 网络异常提示
     */
    public static void toastNetError() {
        BaseApplication.MyToast.builder.display("网络异常,请重试", Toast.LENGTH_LONG);
    }

    /**
     * 服务器异常提示
     */
    public static void toastNetWorkFail() {
        BaseApplication.MyToast.builder.display("服务器异常,请重试", Toast.LENGTH_LONG);
    }
}
