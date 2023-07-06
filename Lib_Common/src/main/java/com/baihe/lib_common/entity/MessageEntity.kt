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
    @SerializedName("create_time")
    val createTime: String,
    val status: Int,
    val btn: String,
    val noticeId: String
)