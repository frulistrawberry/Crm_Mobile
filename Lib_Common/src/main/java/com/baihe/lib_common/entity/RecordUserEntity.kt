package com.baihe.lib_common.entity

import com.google.gson.annotations.SerializedName

data class RecordUserEntity(
    val id:String,
    val name:String
    )
data class RecordUserDataEntity(
    @SerializedName("into_user_list")
    val recordUserList:List<RecordUserEntity>
    )