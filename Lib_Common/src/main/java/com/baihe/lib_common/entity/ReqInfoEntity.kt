package com.baihe.lib_common.entity

import com.baihe.lib_common.entity.StatusText.Mode
import com.google.gson.annotations.SerializedName

data class ReqInfoEntity(
    val title:String?,
    @SerializedName("req_owner")
    val reqOwner:String?,
    @SerializedName("order_owner")
    val orderOwner:String?,
    val id:String,
    val order_phase:String?,
    val phase:String?
)