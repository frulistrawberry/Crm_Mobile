package com.baihe.http.callback;

/**
 * Author：xubo
 * Time：2018-12-28
 * Description：http请求下载回调
 */
public abstract class DownloadCallBack {

    /**
     * 下载准备(UI线程)
     */
    public void downloadReady() {

    }

    /**
     * 下载进度(UI线程)
     * @param downloadSize 当前下载大小
     * @param totalSize 下载总大小
     * @param progress 当前下载进度
     */
    public abstract void downloadProgress(long downloadSize, long totalSize, double progress);

    /**
     * 下载成功(UI线程)
     */
    public abstract void downloadSuccess();

    /**
     * 下载失败(UI线程)
     */
    public abstract void downloadFail();

    /**
     * 下载取消(UI线程)
     */
    public void downloadCancel() {

    }

}
