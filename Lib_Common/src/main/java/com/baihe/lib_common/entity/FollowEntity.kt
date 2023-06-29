package com.baihe.lib_common.entity

import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.google.gson.annotations.SerializedName

data class FollowEntity(
    @SerializedName("create_time")
    val createTime:String,
    @SerializedName("next_contact_time")
    val nextContactTime:String,
    @SerializedName("channel_txt")
    val channel:String,
    val name:String,
    @SerializedName("create_by_txt")
    val createByTxt:String,
    val action:Int,
    val result:String,
    @SerializedName("time_txt")
    val timeText:String,

){
    fun showArray():List<KeyValueEntity>{
        val showArray = mutableListOf<KeyValueEntity>()
        showArray.apply {
            add(KeyValueEntity().apply {
                key = createByTxt
                `val` = name
            })
            add(KeyValueEntity().apply {
                key = "跟进结果"
                `val` = result
            })
            add(KeyValueEntity().apply {
                key = "渠道"
                `val` = channel
            })
            add(KeyValueEntity().apply {
                key = timeText
                `val` = when(action){
                    5,6,7,8,9->{
                        createTime
                    }else ->{
                        nextContactTime
                    }
                }

            })
        }
        return showArray

    }
}