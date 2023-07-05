package com.baihe.lib_common.entity

import com.google.gson.annotations.SerializedName

data class ReserveInfoEntity(
    @SerializedName("arrival_time")
    val arrivalTime:String?,
    val comment:String?,
    val id:String?,
    )
