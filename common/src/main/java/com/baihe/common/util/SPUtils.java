package com.baihe.common.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author：xubo
 * Time：2020-07-22
 * Description：
 */
public class SPUtils {
    /**
     * 用户配置存储
     */
    private static final String SP_USER_CONFIG = "SP_USER_";
    /**
     * 手机配置存储
     */
    private static final String SP_PHONE_CONFIG = "SP_PHONE";

    /**
     * 获取用户配置SharedPreferences
     *
     * @param context 上下文
     * @param userId  用户id
     * @return
     */
    public static SharedPreferences getUserSP(Context context, String userId) {
        return context.getSharedPreferences(SP_USER_CONFIG + userId, Context.MODE_PRIVATE);
    }

    /**
     * 获取手机配置SharedPreferences
     *
     * @param context 上文
     * @return
     */
    public static SharedPreferences getPhoneSP(Context context) {
        return context.getSharedPreferences(SP_PHONE_CONFIG, Context.MODE_PRIVATE);
    }
}
