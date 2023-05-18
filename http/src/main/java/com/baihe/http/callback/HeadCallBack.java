package com.baihe.http.callback;

/**
 * Author：xubo
 * Time：2019-10-29
 * Description：
 */
public abstract class HeadCallBack {
    /**
     * 请求前准备(UI线程)
     */
    public void before() {

    }

    /**
     * 请求成功(UI线程)
     */
    public abstract void success();

    /**
     * 网络异常(UI线程)
     */
    public abstract void error();

    /**
     * 服务器异常(UI线程)
     */
    public abstract void fail();

    /**
     * 请求操作结束(UI线程)
     */
    public void after() {

    }
}
