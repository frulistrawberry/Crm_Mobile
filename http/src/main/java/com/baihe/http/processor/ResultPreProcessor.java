package com.baihe.http.processor;

/**
 * Author：xubo
 * Time：2018-12-29
 * Description：响应数据前置处理器（用于响应数据统一预处理,比如所需数据从特定数据节点获取）
 */
public abstract class ResultPreProcessor {

    /**
     * 数据处理
     *
     * @param response
     * @param isToastError
     * @return
     */
    public abstract String process(String response, boolean isToastError);

    /**
     * 是否中断页面里的数据处理(比如数据返回不是200，处理后的数据只需要Toast即，直接中断页面对数据的处理)
     * 分析预处理完的数据，是否中断页面里的数据处理
     * 默认不中断
     *
     * @param processData
     * @return
     */
    public boolean interrupt(String processData) {
        return false;
    }

}
