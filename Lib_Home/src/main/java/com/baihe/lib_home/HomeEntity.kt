package com.baihe.lib_home

import com.google.gson.annotations.SerializedName

data class DataEntity(
    val cancelOrder: Int,
    val customerCount: Int,
    @SerializedName("customeUnValid")
    val customerUnValid: Int,
    @SerializedName("customeValid")
    val customerValid: Int,
    @SerializedName("customeWait")
    val customerWait: Int,
    val exitOrder: Int,
    @SerializedName("finshOrder")
    val finishOrder: Int,
    val openInvitation: Int,
    val oppoCount: Int,
    val order: Int,
    val orderCount: Int,
    val orderWait: Int,
    @SerializedName("signCustomeCount")
    val signCustomerCount: Int,
    val signSum: Int,
    val successInvitation: Int,
    val waitInvitation: Int
){
    fun isOppoDataEmpty():Boolean{
        return waitInvitation+successInvitation+
                openInvitation+customerValid+customerUnValid+
                customerWait>0
    }

    fun isOrderDataEmpty():Boolean{
        return orderWait+order+cancelOrder+exitOrder+finishOrder>0
    }
}

data class WaitingEntity(
    @SerializedName("arrival_time")
    val arrivalTime: String,
    val category: String,
    @SerializedName("category_txt")
    val categoryTxt: String,
    @SerializedName("customer_id")
    val customerId: Int,
    @SerializedName("follow_txt")
    val followTxt: String,
    val id: Int,
    val name: String,
    @SerializedName("nextContactTime")
    val next_contact_time: String,
    val phone: String,
    val type: Int,
    @SerializedName("type_txt")
    val typeTxt: String
)

data class HomeEntity(
    val waitingEntity:List<WaitingEntity>?,
    val dataEntity: DataEntity?
    )