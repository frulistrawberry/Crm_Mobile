package com.baihe.lib_login.api

import androidx.lifecycle.LifecycleOwner
import com.baihe.http.EasyHttp
import com.baihe.http.model.ResponseClass
import com.baihe.lib_common.entity.MessageEntity
import com.baihe.lib_common.entity.MessageInfoEntity
import com.baihe.lib_common.http.BaseRepository
import com.baihe.lib_common.http.api.CommonApi
import com.baihe.lib_common.http.api.JsonParam
import com.baihe.lib_common.http.response.BaseResponse
import com.baihe.lib_common.http.response.ListData
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_message.constant.UrlConstant

class MessageRepository(lifecycleOwner: LifecycleOwner?) : BaseRepository(lifecycleOwner) {

    /**
     *
     * 获取消息列表
     * @param page 一次请求多少
     */
    public suspend fun getMessages(page: Int): MessageInfoEntity? {
        return requestResponse {
            val userId = UserServiceProvider.getUserId()
            val params = JsonParam.newInstance()
                .putParamValue("push_id", userId)
                .putParamValue("page", page)
                .putParamValue("pageSize", 10)
                .putParamValue("unread", 3)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.MESSAGE, params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<MessageInfoEntity>>() {})
        }
    }


    /**
     * 设置已读
     * @param type 0 全部 / 1 特定消息
     * @param noticeId 消息Id
     */
    public suspend fun setMessageRead(type: Int, noticeId: String?): String? {
        return requestResponse {
            val params = JsonParam.newInstance()
            if (type == 1) {
                params.putParamValue("noticeId", noticeId)
            }
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.SET_MESSAGE_READ, params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<String>>() {})
        }
    }

}