package com.baihe.common.util;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.UUID;

/**
 * Author：xubo
 * Time：2020-02-28
 * Description：
 */
public class DeviceUtils {
    private static final int UNIQUE_FLAG_INSERT_INTERVAL = 1;
    private static final int UNIQUE_FLAG_RANDOM_LENGTH = 4;

    /**
     * 获取AndroidId
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        String deviceId = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);
        //'9774d56d682e549c'是错误android id，2.2手机普遍会固定成这个id
        if (TextUtils.isEmpty(deviceId) || "9774d56d682e549c".equals(deviceId)) {
            return "";
        }
        return deviceId;
    }

    /**
     * 获取AndroidId和唯一id集合
     *
     * @param context
     * @return
     */
    public static String[] getAndroidAndUniqueId(Context context) {
        String androidId = getAndroidId(context);
        if (TextUtils.isEmpty(androidId)) {
            androidId = UUID.randomUUID().toString().replace("-", "");
        }
        String md5 = Md5Utils.md5(androidId, "utf-8", false);
        String uniqueId = StringUtils.insert(md5, UNIQUE_FLAG_INSERT_INTERVAL, UNIQUE_FLAG_RANDOM_LENGTH);
        String[] result = new String[]{androidId, uniqueId};
        return result;
    }

}
