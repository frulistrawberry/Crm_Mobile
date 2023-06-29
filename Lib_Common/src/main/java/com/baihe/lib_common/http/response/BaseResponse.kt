package com.baihe.lib_common.http.response

import com.google.gson.annotations.SerializedName


data class BaseResponse<T>  (
    val code:Int,
    val msg:String,
    val data: Data<T>?
    ){
    fun isSuccess():Boolean{
        return code == 200
    }

}

data class Data<T>(
    val result:T?,
    val other: Int,
    @SerializedName("apver")
    val appVersion:String
)

data class ListData<T>(
    val total:Int,
    val page:Int,
    val pageSize:Int,
    val rows:MutableList<T>?
)