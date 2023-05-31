package com.baihe.lib_common.http

import androidx.lifecycle.LifecycleOwner
import com.baihe.lib_common.http.response.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

/**
 * @author mingyan.su
 * @date   2023/2/23 23:31
 * @desc   基础仓库
 */
open class BaseRepository(val lifecycleOwner: LifecycleOwner) {

    /**
     * IO中处理请求
     */
    suspend fun <T> requestResponse(requestCall: suspend () -> BaseResponse<T>?): T? {
        val response = withContext(Dispatchers.IO) {
            withTimeout(10 * 1000) {
                requestCall()
            }
        } ?:return null
        return response.data?.result
    }
}