package com.baihe.lib_common.entity

import com.baihe.lib_common.constant.StatusConstant.OPPO_CUSTOMER_EFFECTIVE
import com.baihe.lib_common.constant.StatusConstant.OPPO_INVITATION_SUCCESSFUL
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_CALL
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_DISPATCH_ORDER
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_FOLLOW
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.google.gson.annotations.SerializedName

data class OpportunityListItemEntity(
    @SerializedName("req_id")
    val id:String,
    @SerializedName("customer_id")
    val customerId:String?,
    val title:String?,
    @SerializedName("hide_phone")
    val phone:String?,
    @SerializedName("follow_content")
    val followContent:String?,
    @SerializedName("follow_user_name")
    val followUserName:String?,
    @SerializedName("next_contact_time")
    val nextContactTime:String?,
    @SerializedName("arrival_time")
    val arrivalTime:String?,
    @SerializedName("reserve_time")
    val reserveTime:String?,
    @SerializedName("req_phase")
    val reqPhase:String,
    var isCheck:Boolean,
    @SerializedName("orderstatus")
    val orderStatus:String?


){
    fun toShowArray():List<KeyValueEntity>{
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.apply {
            add(KeyValueEntity().apply {
                key = "联系电话"
                `val` = this@OpportunityListItemEntity.phone
            })
            when(reqPhase){
                "220"->{
                    if (!nextContactTime.isNullOrEmpty()){
                        add(KeyValueEntity().apply {
                            key = "下次回访时间"
                            `val` = nextContactTime
                        })
                    }
                }
                "230"->{
                    if (!nextContactTime.isNullOrEmpty()){
                        add(KeyValueEntity().apply {
                            key = "下次回访时间"
                            `val` = nextContactTime
                        })
                    }
                }
                "240"->{
                    if (!reserveTime.isNullOrEmpty()){
                        add(KeyValueEntity().apply {
                            key = "计划进店时间"
                            `val` = reserveTime
                        })
                    }
                }
                "210"->{
                    if (!orderStatus.isNullOrEmpty()&&orderStatus=="3"){
                        if (!arrivalTime.isNullOrEmpty()){
                            add(KeyValueEntity().apply {
                                key = "进店时间"
                                `val` = arrivalTime
                            })
                        }
                    }
                }
                "250"->{
                    if (!arrivalTime.isNullOrEmpty()){
                        add(KeyValueEntity().apply {
                            key = "进店时间"
                            `val` = arrivalTime
                        })
                    }
                }

            }
            add(KeyValueEntity().apply {
                key = "最新沟通记录"
                `val` = followContent?:"无最新沟通记录"
            })
            add(KeyValueEntity().apply {
                key = "跟进人"
                `val` = followUserName
            })

            return kvList
        }
    }

    fun genBottomButtons():List<ButtonTypeEntity>{
        val buttons = mutableListOf<ButtonTypeEntity>()
        buttons.add(ButtonTypeEntity().apply {
            name = "打电话"
            type = ACTION_CALL
        })
        buttons.add(ButtonTypeEntity().apply {
            if ((reqPhase == OPPO_CUSTOMER_EFFECTIVE || reqPhase == OPPO_INVITATION_SUCCESSFUL)&&(orderStatus.isNullOrEmpty()||orderStatus == "0")){
                name = "下发订单"
                type = ACTION_DISPATCH_ORDER
            }else{
                name = "写跟进"
                type = ACTION_FOLLOW
            }
        })
        return buttons
    }

}