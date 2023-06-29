package com.baihe.lib_customer

import com.baihe.lib_common.entity.FollowEntity
import com.baihe.lib_common.entity.ReqInfoEntity
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.google.gson.annotations.SerializedName

data class CustomerListItemEntity(
    val id:Int,
    val name:String?,
    val phone:String?,
    @SerializedName("req_cout")
    val reqCount:Int,
    @SerializedName("order_cout")
    val orderCount:Int,
    @SerializedName("finsh_order_cout")
    val finishOrderCount:Int,
    val reqInfo:List<ReqInfoEntity>?

)

data class CustomerDetailEntity(
    val id:Int,
    val name:String?,
    val wechat:String?,
    val phone:String?,
    @SerializedName("identity_txt")
    val identity:String?,
    @SerializedName("identity")
    val identityId:String?,
    @SerializedName("record_user_id_txt")
    val recordUser:String?,
    @SerializedName("record_user_id")
    val recordUserId:String?,
    @SerializedName("create_by_txt")
    val createBy:String?,
    @SerializedName("req_cout")
    val reqCount:Int,
    @SerializedName("order_cout")
    val orderCount:Int,
    @SerializedName("create_time")
    val createTime:String?,
    @SerializedName("channel_txt")
    val sourceChannel:String?,
    @SerializedName("channel")
    val sourceChannelId:String?,
    @SerializedName("jindian_txt")
    val entryChannel:String?,
    @SerializedName("daodian_txt")
    val effectiveChannel:String?,
    @SerializedName("is_update")
    val isUpdate:Boolean,
    val reqInfo:List<ReqInfoEntity>?,
    val follow:List<FollowEntity>?
){
    fun basicShowArray():List<KeyValueEntity>{
        val showArray = mutableListOf<KeyValueEntity>()
        showArray.apply {
            add(KeyValueEntity().apply {
                key = "首次录入来源渠道"
                `val` = sourceChannel
            })
            add(KeyValueEntity().apply {
                key = "电话"
                `val` = this@CustomerDetailEntity.phone
            })
            add(KeyValueEntity().apply {
                key = "微信"
                `val` = this@CustomerDetailEntity.wechat
            })
            add(KeyValueEntity().apply {
                key = "客户身份"
                `val` = identity
            })
            add(KeyValueEntity().apply {
                key = "首次有效渠道"
                `val` = effectiveChannel
            })
            add(KeyValueEntity().apply {
                key = "首次到店渠道"
                `val` = entryChannel
            })
            add(KeyValueEntity().apply {
                key = "录入人"
                `val` = createBy
            })
            add(KeyValueEntity().apply {
                key = "创建时间"
                `val` = createTime
            })
            add(KeyValueEntity().apply {
                key = "首次提供人"
                `val` = recordUser
            })

        }

        return showArray
    }

    fun followShowArray():List<List<KeyValueEntity>>{
        val  showArray = mutableListOf<List<KeyValueEntity>>()
        follow?.forEach {
            showArray.add(it.showArray())
        }
        return showArray
    }
}
