package com.baihe.lib_common.entity

/**
 * @author xukankan
 * @date 2023/7/5 15:10
 * @email：xukankan@jiayuan.com
 * @description：消息实体类
 */
data class MessageEntity(
    val title: String,
    val content: String,
    val time: String,
    val isRead: Boolean,
    val jump:String
)