package com.baihe.lib_framework.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baihe.lib_framework.log.LogUtil;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Author：xubo
 * Time：2018-01-09
 * Description：json工具类
 */

public class JsonUtils {

    /**
     * 解析接口返回结果
     *
     * @param response
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T parse(String response, Type type) throws JSONException {
        T t = JSONObject.parseObject(response, type);
        return t;
    }

    /**
     * 解析接口返回结果
     *
     * @param response
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> parseList(String response, Class<T> clazz) throws JSONException{
        List<T> list = JSONObject.parseArray(response, clazz);
        return list;
    }
}
