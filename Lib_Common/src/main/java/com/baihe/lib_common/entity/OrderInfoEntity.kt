package com.baihe.lib_common.entity

import com.google.gson.annotations.SerializedName

data class OrderInfoEntity(
    @SerializedName("orderstatus")
    val orderStatus:String?,val name:String?) {
}