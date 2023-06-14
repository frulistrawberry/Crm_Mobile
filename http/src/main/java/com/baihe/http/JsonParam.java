package com.baihe.http;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Author：xubo
 * Time：2018-12-28
 * Description：json参数
 */
public class  JsonParam {
    private String paramKey;
    private JSONObject paramValueJSONObject;

    public static JsonParam newInstance(String paramKey) {
        return new JsonParam(paramKey);
    }

    public JsonParam(String paramKey) {
        if (TextUtils.isEmpty(paramKey)) {
            throw new IllegalStateException("json参数的key不能为空");
        }
        this.paramKey = paramKey;
        this.paramValueJSONObject = new JSONObject();
    }

    /**
     * json参数的添加具体值
     *
     * @param key
     * @param value
     * @return
     */
    public JsonParam putParamValue(String key, Object value) {
        if (!TextUtils.isEmpty(key) && value != null) {
            paramValueJSONObject.put(key, value);
        }
        return this;
    }

    /**
     * json参数的添加具体值
     *
     * @param paramvalues
     * @return
     */
    public JsonParam putParamValue(Map<String, Object> paramvalues) {
        if (paramvalues != null) {
            paramValueJSONObject.putAll(paramvalues);
        }
        return this;
    }

    /**
     * 获取json参数的key
     *
     * @return
     */
    public String getParamKey() {
        return paramKey;
    }

    /**
     * 获取json参数的value
     *
     * @return
     */
    public String getParamValue() {
        return paramValueJSONObject.toJSONString();
    }
}
