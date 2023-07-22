package com.baihe.lib_common.entity

import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.google.gson.annotations.SerializedName

data class FollowEntity(
    val id:String,
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
    val title:String,
    val time:String,
    val contact_way_txt:String,
    val phase_txt:String,
    val reserve_txt:String,
    val reserve_time:String,
    val comment:String,
    val attachment:String?,
    val daodian_comment:String?,
    val follow_create:String?,
    val daodian_time:String?,
    @SerializedName("row")
    val content:List<KeyValueEntity>?,
    val follow:List<FollowEntity>?

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
                `val` = when(this@FollowEntity.action){
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

    fun followDetail():List<KeyValueEntity>{
        val showArray = mutableListOf<KeyValueEntity>()
        showArray.apply {
            add(KeyValueEntity().apply {
                key = "跟进方式"
                `val` = contact_way_txt
            })
            add(KeyValueEntity().apply {
                key = "跟进结果"
                `val` = phase_txt
            })
            add(KeyValueEntity().apply {
                key = "预约进店"
                `val` = reserve_txt
            })
            add(KeyValueEntity().apply {
                key = "预约时间"
                `val` = reserve_time
            })
            add(KeyValueEntity().apply {
                key = "下次回访时间"
                `val` = nextContactTime
            })
            add(KeyValueEntity().apply {
                key = "沟通内容"
                `val` = comment
            })
            add(KeyValueEntity().apply {
                key = "附件"
                `val` = ""
                attach = mutableListOf()

                if (attachment?.contains(",") == true){
                    val urls = attachment.split(",")
                    attach.addAll(urls)
                }else if (!attachment.isNullOrEmpty()){
                    attach.add(attachment)

                }
            })
            add(KeyValueEntity().apply {
                key = "跟进人"
                `val` = createByTxt
            })
            add(KeyValueEntity().apply {
                key = "跟进时间"
                `val` = createTime
            })
        }
        return showArray

    }
}