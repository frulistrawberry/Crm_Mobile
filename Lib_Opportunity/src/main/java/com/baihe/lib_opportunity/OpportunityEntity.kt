package com.baihe.lib_opportunity

import com.baihe.lib_common.entity.ButtonAction
import com.baihe.lib_common.entity.ReqInfoEntity
import com.baihe.lib_common.entity.StatusText
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.google.gson.annotations.SerializedName

class OpportunityEntity {
}

data class OpportunityListItemEntity(
    @SerializedName("req_id")
    val id:Int,
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

    fun getOppoLabel():StatusText?{
        val statusText = StatusText()
        when(reqPhase){
            "200"->{
                statusText.apply {
                    text = "待邀约"
                    textColor = "#FFFFFF"
                    bgColor = "#7AFF8B00"
                    mode = StatusText.Mode.FILL
                }
            }
            "220"->{
                statusText.apply {
                    text = "客户待定"
                    textColor = "#FFFFFF"
                    bgColor = "#66FFB600"
                    mode = StatusText.Mode.FILL
                }
            }
            "230"->{
                statusText.apply {
                    text = "客户有效"
                    textColor = "#FFFFFF"
                    bgColor = "#7A6C8EFF"
                    mode = StatusText.Mode.FILL
                }
            }
            "240"->{
                statusText.apply {
                    text = "邀约成功"
                    textColor = "#FFFFFF"
                    bgColor = "#666CBF09"
                    mode = StatusText.Mode.FILL
                }
            }
            "210"->{
                statusText.apply {
                    text = "客户无效"
                    textColor = "#FFFFFF"
                    bgColor = "#52FF2C2C"
                    mode = StatusText.Mode.FILL
                }
            }
            "250"->{
                statusText.apply {
                    text = "已进店"
                    textColor = "#FFFFFF"
                    bgColor = "#666CBF09"
                    mode = StatusText.Mode.FILL
                }
            }
            "260"->{
                statusText.apply {
                    text = "已删除"
                    textColor = "#FFFFFF"
                    bgColor = "#667D7D7D"
                    mode = StatusText.Mode.FILL
                }
            }else ->{
            return null
        }
        }
        return statusText
    }

    fun getOrderLabel():StatusText?{
        val statusText = StatusText()
        if (orderStatus!=null && orderStatus.isNotEmpty() && orderStatus!="0"){
            when(orderStatus){
                "1" ->{
                    statusText.apply {
                        text = "待签约"
                        textColor = "#FFE08B01"
                        bgColor = "#33FFB600"
                        mode = StatusText.Mode.TOP_HALF_FILL
                    }
                }
                "2" ->{
                    statusText.apply {
                        text = "已签约"
                        textColor = "#FF5BA433"
                        bgColor = "#336CBF09"
                        mode = StatusText.Mode.TOP_HALF_FILL
                    }
                }
                "3" ->{
                    statusText.apply {
                        text = "已退单"
                        textColor = "#FF898A8D"
                        bgColor = "#14000000"
                        mode = StatusText.Mode.TOP_HALF_FILL
                    }
                }
                "4" ->{
                    statusText.apply {
                        text = "已取消"
                        textColor = "#FF898A8D"
                        bgColor = "#14000000"
                        mode = StatusText.Mode.TOP_HALF_FILL
                    }
                }
                "6" ->{
                    statusText.apply {
                        text = "已完成"
                        textColor = "#FF6C8EFF"
                        bgColor = "#FFEDF1FF"
                        mode = StatusText.Mode.TOP_HALF_FILL
                    }
                }
            }
        }else if (reqPhase == "230" || reqPhase == "240"){
            statusText.apply {
                text = "未下发订单"
                textColor = "#FFF11E1E"
                bgColor = "#1FFF2C2C"
                mode = StatusText.Mode.TOP_HALF_FILL
            }
        }else{
            return null
        }
        return statusText
    }

    fun getButtonAction():ButtonAction{
        return if ((reqPhase == "230" || reqPhase == "240")&&(orderStatus.isNullOrEmpty()||orderStatus == "0")){
            ButtonAction.DISPATCH_ORDER
        }else{
            ButtonAction.FOLLOW
        }
    }

    fun getButtonText():String{
        return if ((reqPhase == "230" || reqPhase == "240")&&(orderStatus.isNullOrEmpty()||orderStatus == "0")){
            "下发订单"
        }else{
            "写跟进"
        }
    }

}