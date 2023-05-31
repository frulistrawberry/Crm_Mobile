package com.baihe.lib_common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baihe.lib_common.http.callback.IApiErrorCallback
import com.baihe.lib_common.http.exception.ApiException
import com.baihe.lib_common.http.exception.ERROR
import com.baihe.lib_common.http.exception.ExceptionHandler
import com.baihe.lib_common.http.response.BaseResponse
import com.baihe.lib_framework.log.LogUtil

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

/**
 *viewModel基类
 */
open class BaseViewModel : ViewModel() {
    /**
     * 运行在主线程中，可直接调用
     * @param errorBlock 错误回调
     * @param responseBlock 请求函数
     */
    fun launchUI(errorBlock: (Int?, String?) -> Unit, responseBlock: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            safeApiCall(errorBlock = errorBlock, responseBlock)
        }
    }

    /**
     * 需要运行在协程作用域中
     * @param errorBlock 错误回调
     * @param responseBlock 请求函数
     */
    private suspend fun <T> safeApiCall(
        errorBlock: suspend (Int?, String?) -> Unit,
        responseBlock: suspend () -> T?
    ): T? {
        try {
            return responseBlock()
        } catch (e: ApiException) {
            errorBlock(e.errCode, e.errMsg)
        }
        return null
    }

    /**
     * 不依赖BaseRepository，运行在主线程中，可直接调用
     * @param errorCall 错误回调
     * @param responseBlock 请求函数
     * @param successBlock 请求回调
     */
    fun <T> launchUIWithResult(
        responseBlock: suspend () -> BaseResponse<T>?,
        errorCall: IApiErrorCallback?,
        successBlock: (T?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            val result = safeApiCallWithResult(errorCall = errorCall, responseBlock)
            successBlock(result)
        }
    }

    /**
     * 不依赖BaseRepository，需要在作用域中运行
     * @param errorCall 错误回调
     * @param responseBlock 请求函数
     */
    private suspend fun <T> safeApiCallWithResult(
        errorCall: IApiErrorCallback?,
        responseBlock: suspend () -> BaseResponse<T>?
    ): T? {
        try {
            val response = withContext(Dispatchers.IO) {
                withTimeout(10 * 1000) {
                    responseBlock()
                }
            } ?: return null
            return response.data?.result
        } catch (e: ApiException) {
            errorCall?.onError(e.errCode, e.errMsg)
        }
        return null
    }


}