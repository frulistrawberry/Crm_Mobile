package com.baihe.lib_common.http.log;

import com.baihe.http.config.IHttpLogStrategy;
import com.baihe.lib_framework.log.LogUtil;

public class HttpLogStrategy implements IHttpLogStrategy {
    @Override
    public void printLog(String tag, String log) {
        LogUtil.d(tag,log);
    }

    @Override
    public void printJson(String tag, String json) {
        LogUtil.json(tag,json);
    }

    @Override
    public void printKeyValue(String tag, String key, String value) {
        LogUtil.d(tag,key+"="+value);
    }

    @Override
    public void printThrowable(String tag, Throwable throwable) {
        LogUtil.e(tag,throwable);
    }

    @Override
    public void printStackTrace(String tag, StackTraceElement[] stackTrace) {
        for (StackTraceElement element : stackTrace) {
            // 获取代码行数
            int lineNumber = element.getLineNumber();
            // 获取类的全路径
            String className = element.getClassName();
            if (lineNumber <= 0 || className.startsWith("com.hjq.http")) {
                continue;
            }

            printLog(tag, "RequestCode = (" + element.getFileName() + ":" + lineNumber + ") ");
            break;
        }
    }
}
