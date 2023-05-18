package com.baihe.http.callback;

/**
 * Author：xubo
 * Time：2018-12-28
 * Description：http请求上传回调
 */
public abstract class UploadCallBack<Result> extends CallBack<Result> {

    /**
     * 上传进度(UI线程)
     * @param uploadSize 当前上传大小
     * @param totalSize 上传总大小
     * @param progress 当前上传进度
     */
    public abstract void uploadProgress(long uploadSize, long totalSize, double progress);

    /**
     * 上传取消(UI线程)
     */
    public void uploadCancel() {

    }

}
