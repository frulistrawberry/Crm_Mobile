package com.baihe.http.processor;

/**
 * Author：xubo
 * Time：2020-04-07
 * Description：结果解密处理器
 */
public interface ResultDecryptProcessor {
    /**
     * 数据解密
     *
     * @param response
     * @return
     */
    String decrypt(String response);
}
