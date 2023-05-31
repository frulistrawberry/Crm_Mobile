package com.baihe.lib_common.http.response

import com.alibaba.fastjson.annotation.JSONField

data class BaseResponse<out T> (
    val code:Int,
    val msg:String,
    val data: Data<T>?
    ){
    fun isSuccess():Boolean{
        return code == 200
    }

}

data class Data<out T>(
    val other:Int,
    @JSONField(name = "apver")
    val appVersion:String,
    val result:T?
    )