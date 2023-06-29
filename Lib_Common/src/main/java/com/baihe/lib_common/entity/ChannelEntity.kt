package com.baihe.lib_common.entity

data class ChannelEntity(
    val value: Int,
    val label:String,
    val pid:Int,
    var children:List<ChannelEntity>?,
    var isSelected:Boolean = false
)