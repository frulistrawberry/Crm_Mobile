package com.baihe.common.util;

import com.alibaba.fastjson.JSONObject;

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
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parse(String response, Class<T> clazz) {
        try {
            T t = JSONObject.parseObject(response, clazz);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析接口返回结果
     *
     * @param response
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> parseList(String response, Class<T> clazz) {
        try {
            List<T> list = JSONObject.parseArray(response, clazz);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
