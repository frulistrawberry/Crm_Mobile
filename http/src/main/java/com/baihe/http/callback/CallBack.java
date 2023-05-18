package com.baihe.http.callback;

/**
 * Author：xubo
 * Time：2018-12-28
 * Description：http请求回调
 */
public abstract class CallBack<Result> {

    /**
     * http请求操作前(UI线程)，顺序1
     */
    public void before() {

    }

    /**
     * http请求成功后数据处理(后台线程)，顺序2
     *
     * @param response http请求成功的数据
     * @return 处理成功的数据对象
     */
    public abstract Result doInBackground(String response);

    /**
     * 数据对象处理成功(UI线程)，顺序3
     *
     * @param result 处理成功的数据对象
     */
    public abstract void success(Result result);

    /**
     * 网络异常(UI线程)，顺序3
     */
    public abstract void error();

    /**
     * 服务器异常(UI线程)，顺序3
     */
    public abstract void fail();

    /**
     * http请求操作结束(UI线程)，顺序4
     * 通过参数可以判断请求操作结束后是否请求成功数据并处理
     *
     * @param result 处理成功的数据对象，如果没有成功则返回null
     */
    public void after(Result result) {
        after();
    }

    /**
     * http请求操作结束(UI线程)，顺序4
     */
    public void after() {

    }

    /**
     * 结果预处理时是否自动toast错误
     * 设置结果预处理时需要实现
     *
     * @return
     */
    public boolean isToastError() {
        return true;
    }

}
