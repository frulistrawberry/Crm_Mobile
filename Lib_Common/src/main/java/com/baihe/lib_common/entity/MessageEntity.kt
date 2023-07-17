package com.baihe.lib_common.entity

import com.google.gson.annotations.SerializedName

/**
 * @author xukankan
 * @date 2023/7/5 15:10
 * @email：xukankan@jiayuan.com
 * @description：消息实体类
 */
data class MessageEntity(
    val title: String,
    val text: String,
    @SerializedName("time")
    val createTime: String,
    @SerializedName("unread")
    val unRead: Boolean,
    val btn: String,
    val msgId: String,
    val type: Int,
    @SerializedName("data_id")
    val dataId: String
)