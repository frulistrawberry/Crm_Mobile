package com.baihe.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author：xubo
 * Time：2019-06-03
 * Description：时间工具类
 */
public class TimeUtils {
    public static SimpleDateFormat SIMPLE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 传入时间是否和当前时间在同一天
     *
     * @param time
     */
    public static boolean isSameDay(long time) {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(new Date(time)).equals(format.format(new Date(currentTime)));
    }

    /**
     * 格式化时间（yyyy-MM-dd）
     *
     * @param time
     * @return
     */
    public static String formatSimpleTime(long time) {
        return SIMPLE_TIME_FORMAT.format(new Date(time));
    }

    /**
     * 格式化时间（yyyy-MM-dd HH:mm:ss）
     *
     * @param time
     * @return
     */
    public static String formatTime(long time) {
        return TIME_FORMAT.format(new Date(time));
    }
}
