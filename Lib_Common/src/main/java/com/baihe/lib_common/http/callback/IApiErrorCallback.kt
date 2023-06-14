package com.baihe.lib_common.http.callback

import com.baihe.lib_framework.toast.TipsToast


/**
 * 接口请求错误回调
 */
interface IApiErrorCallback {
    /**
     * 错误回调处理
     */
    fun onError(code: Int?, error: String?) {
        TipsToast.showTips(error)
    }

}