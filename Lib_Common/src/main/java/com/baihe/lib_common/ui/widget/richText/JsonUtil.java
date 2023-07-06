package com.baihe.lib_common.ui.widget.richText;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author xukankan
 * @date 2023/7/6 11:21
 * @email：xukankan@jiayuan.com
 * @description：
 */
public class JsonUtil {
    public JsonUtil() {
    }

    public static boolean isJsonObject(@NonNull String src) {
        boolean re = true;

        try {
            new JSONObject(src);
            return re;
        } catch (JSONException var6) {
            re = false;
            return re;
        } finally {
            ;
        }
    }

    public static boolean isJsonArray(@NonNull JSONObject obj, @NonNull String name) {
        boolean re = true;

        try {
            obj.getJSONArray(name);
            return re;
        } catch (JSONException var7) {
            re = false;
            return re;
        } finally {
            ;
        }
    }

    public static boolean isJsonArray(@NonNull String src) {
        boolean re = true;

        try {
            new JSONArray(src);
            return re;
        } catch (JSONException var6) {
            re = false;
            return re;
        } finally {
            ;
        }
    }

    private static boolean isKeyValid(@NonNull String key, @NonNull JSONObject jsonObject) {
        return jsonObject.has(key);
    }

    public static JSONObject getJsonObject(@NonNull JSONObject obj, @NonNull String key) {
        if (obj == null) {
            return new JSONObject();
        } else if (!obj.has(key)) {
            return new JSONObject();
        } else {
            return isJsonObject(obj.optString(key)) ? obj.optJSONObject(key) : new JSONObject();
        }
    }

    public static JSONArray getJsonArray(@NonNull JSONObject obj, @NonNull String key) {
        if (obj == null) {
            return new JSONArray();
        } else if (!obj.has(key)) {
            return new JSONArray();
        } else {
            return isJsonArray(obj.optString(key)) ? obj.optJSONArray(key) : new JSONArray();
        }
    }

    public static String getString(@NonNull String key, @NonNull JSONObject jsonObject) {
        return getString(key, jsonObject, "");
    }

    public static String getString(@NonNull String key, @NonNull JSONObject jsonObject, @NonNull String defaultValue) {
        if (!isKeyValid(key, jsonObject)) {
            return defaultValue;
        } else {
            String result = jsonObject.optString(key, defaultValue);
            if (TextUtils.isEmpty(result)) {
                result = "";
            }

            return result;
        }
    }

    public static int getInt(@NonNull String key, @NonNull JSONObject jsonObject) {
        return getInt(key, jsonObject, 0);
    }

    public static int getInt(@NonNull String key, @NonNull JSONObject jsonObject, int defaultValue) {
        return !isKeyValid(key, jsonObject) ? defaultValue : jsonObject.optInt(key, defaultValue);
    }

    public static long getLong(@NonNull String key, @NonNull JSONObject jsonObject) {
        return getLong(key, jsonObject, 0L);
    }

    public static long getLong(@NonNull String key, @NonNull JSONObject jsonObject, long defaultValue) {
        return !isKeyValid(key, jsonObject) ? defaultValue : jsonObject.optLong(key, defaultValue);
    }

    public static boolean getBoolean(@NonNull String key, @NonNull JSONObject jsonObject) {
        return getBoolean(key, jsonObject, false);
    }

    public static boolean getBoolean(@NonNull String key, @NonNull JSONObject jsonObject, boolean defaultValue) {
        return !isKeyValid(key, jsonObject) ? defaultValue : jsonObject.optBoolean(key, defaultValue);
    }

    public static String[] getJsonStrArray(String key, JSONObject obj) {
        if (obj.has(key) && isJsonArray(obj, key)) {
            JSONArray res = null;
            String[] list = null;

            try {
                res = obj.getJSONArray(key);
                int length = res.length();
                list = new String[length];

                for(int i = 0; i < length; ++i) {
                    list[i] = res.getString(i);
                }
            } catch (JSONException var6) {

            }

            if (list == null) {
                list = new String[0];
            }

            return list;
        } else {
            return new String[0];
        }
    }
}
