package com.baihe.common.log;

import android.text.TextUtils;
import android.util.Log;

import com.baihe.common.BuildConfig;
import com.orhanobut.logger.Logger;

/**
 * Author：xubo
 * Time：2020-02-14
 * Description：
 */
public class LogUtils {
    public static final String LOG_TAG = "LiheCrm";

    /**
     * 打印Verbose类型日志
     *
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, msg);
        }
    }

    /**
     * 打印Verbose类型日志
     *
     * @param tag
     * @param format
     * @param args
     */
    public static void v(String tag, String format, Object... args) {
        if (BuildConfig.DEBUG) {
            String msg = String.format(format, args);
            Log.v(tag, msg);
        }
    }

    /**
     * 打印Debug类型日志
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    /**
     * 打印Debug类型日志
     *
     * @param tag
     * @param format
     * @param args
     */
    public static void d(String tag, String format, Object... args) {
        if (BuildConfig.DEBUG) {
            String msg = String.format(format, args);
            Log.d(tag, msg);
        }
    }

    /**
     * 打印Info类型日志
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg);
        }
    }

    /**
     * 打印Info类型日志
     *
     * @param tag
     * @param format
     * @param args
     */
    public static void i(String tag, String format, Object... args) {
        if (BuildConfig.DEBUG) {
            String msg = String.format(format, args);
            Log.i(tag, msg);
        }
    }

    /**
     * 打印Warn类型日志
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg);
        }
    }

    /**
     * 打印Warn类型日志
     *
     * @param tag
     * @param format
     * @param args
     */
    public static void w(String tag, String format, Object... args) {
        if (BuildConfig.DEBUG) {
            String msg = String.format(format, args);
            Log.w(tag, msg);
        }
    }

    /**
     * 打印Error类型日志
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }

    /**
     * 打印Error类型日志
     *
     * @param tag
     * @param format
     * @param args
     */
    public static void e(String tag, String format, Object... args) {
        if (BuildConfig.DEBUG) {
            String msg = String.format(format, args);
            Log.e(tag, msg);
        }
    }

    /**
     * 打印json信息
     *
     * @param json
     */
    public static void printJson(String json) {
        Logger.json(json);
    }

    /**
     * 打印xml信息
     *
     * @param xml
     */
    public static void printXml(String xml) {
        Logger.xml(xml);
    }

    /**
     * 打印集合信息(map, list, array, set等)
     *
     * @param collections
     */
    public static void printCollections(Object collections) {
        Logger.d(collections);
    }

    /**
     * 日志信息组装
     *
     * @param msg
     * @param exception
     * @return
     */
    public static String buildLogInfo(String msg, String exception) {
        return buildLogInfo(msg, null, null, null, null, exception);
    }

    /**
     * 日志信息组装
     *
     * @param msg
     * @param method
     * @param url
     * @param responseCode
     * @param exception
     * @return
     */
    public static String buildLogInfo(String msg, String requestAlias, String method, String url, String responseCode, String exception) {
        StringBuffer buffer = new StringBuffer();
        if (!TextUtils.isEmpty(msg)) {
            buffer.append("Msg: " + msg);
            buffer.append("\n\n");
        }
        if (!TextUtils.isEmpty(requestAlias)) {
            buffer.append("RequestAlias: " + requestAlias);
            buffer.append("\n\n");
        }
        if (!TextUtils.isEmpty(method)) {
            buffer.append("Method: " + method);
            buffer.append("\n\n");
        }
        if (!TextUtils.isEmpty(url)) {
            buffer.append("URL: " + url);
            buffer.append("\n\n");
        }
        if (!TextUtils.isEmpty(responseCode)) {
            buffer.append("Response Code: " + responseCode);
            buffer.append("\n\n");
        }
        if (!TextUtils.isEmpty(exception)) {
            buffer.append("Exception: " + exception);
            buffer.append("\n\n");
        }
        return buffer.toString();
    }

}
