package com.baihe.lib_login.api

import androidx.lifecycle.LifecycleOwner
import com.baihe.http.EasyHttp
import com.baihe.http.model.ResponseClass
import com.baihe.lib_common.entity.MessageEntity
import com.baihe.lib_common.http.BaseRepository
import com.baihe.lib_common.http.api.CommonApi
import com.baihe.lib_common.http.api.JsonParam
import com.baihe.lib_common.http.response.BaseResponse
import com.baihe.lib_common.http.response.ListData
import com.baihe.lib_message.constant.UrlConstant

class MessageRepository(lifecycleOwner: LifecycleOwner?) : BaseRepository(lifecycleOwner) {

    /**
     * 获取消息列表
     */
    public suspend fun getMessages(): ListData<MessageEntity>? {
        return requestResponse {
            val params = JsonParam.newInstance()
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.MESSAGE, params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<ListData<MessageEntity>>>() {})
        }
    }

}