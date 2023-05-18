package com.baihe.http.interceptor;

/**
 * Author：xubo
 * Time：2018-12-28
 * Description：结果拦截器（用于特殊数据统一拦截处理）
 */
public interface ResultInterceptor {

    /**
     * 是否拦截，注入拦截规则（后台线程）
     * @param response http请求成功的数据
     * @return 是否拦截
     */
    boolean isIntercept(String response);

    /**
     * 拦截操作（UI线程）
     * @param response http请求成功的数据
     */
    void interceptAction(String response);

}
