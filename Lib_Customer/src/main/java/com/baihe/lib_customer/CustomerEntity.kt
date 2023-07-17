package com.baihe.lib_customer

import com.baihe.lib_common.entity.FollowEntity
import com.baihe.lib_common.entity.ReqInfoEntity
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.google.gson.annotations.SerializedName

data class CustomerListItemEntity(
    val id:Int,
    val name:String?,
    val phone:String?,
    val see_phone:String?,
    val wechat:String?,
    val allot_time:String?,
    @SerializedName("req_cout")
    val reqCount:Int,
    @SerializedName("order_cout")
    val orderCount:Int,
    @SerializedName("finsh_order_cout")
    val identity:Int,
    val identity_txt:String?,
    val finishOrderCount:Int,
    val record_user_id_txt:String,
    var isCheck:Boolean = false,
    val reqInfo:List<ReqInfoEntity>?


){
    fun basicShowArray():List<KeyValueEntity>{
        val showArray = mutableListOf<KeyValueEntity>()
        showArray.apply {
            add(KeyValueEntity().apply {
                key = "创建时间"
                `val` = allot_time
            })
            add(KeyValueEntity().apply {
                key = "跟进人"
                `val` = record_user_id_txt
            })

        }

        return showArray
    }
}


