package com.baihe.lib_common.utils

import com.baihe.lib_common.constant.StatusConstant
import com.baihe.lib_common.constant.StatusConstant.OPPO_CUSTOMER_EFFECTIVE
import com.baihe.lib_common.constant.StatusConstant.OPPO_CUSTOMER_TBD
import com.baihe.lib_common.constant.StatusConstant.OPPO_DELETED
import com.baihe.lib_common.constant.StatusConstant.OPPO_ENTERED_STORE
import com.baihe.lib_common.constant.StatusConstant.OPPO_INVALID_CUSTOMER
import com.baihe.lib_common.constant.StatusConstant.OPPO_INVITATION_SUCCESSFUL
import com.baihe.lib_common.constant.StatusConstant.OPPO_TO_BE_INVITED
import com.baihe.lib_common.constant.StatusConstant.ORDER_CANCELED
import com.baihe.lib_common.constant.StatusConstant.ORDER_CHARGED
import com.baihe.lib_common.constant.StatusConstant.ORDER_COMPLETED
import com.baihe.lib_common.constant.StatusConstant.ORDER_SIGNED
import com.baihe.lib_common.constant.StatusConstant.ORDER_TO_BE_SIGNED
import com.baihe.lib_common.entity.StatusText

object FormatUtils {
    fun formatOppoLabel(phase:String?):StatusText?{
        val statusText = StatusText()
        when(phase){
            OPPO_TO_BE_INVITED->{
                statusText.apply {
                    text = "待邀约"
                    textColor = "#FFFFFF"
                    bgColor = "#7AFF8B00"
                    mode = StatusText.Mode.FILL
                }
            }
            OPPO_CUSTOMER_TBD->{
                statusText.apply {
                    text = "客户待定"
                    textColor = "#FFFFFF"
                    bgColor = "#66FFB600"
                    mode = StatusText.Mode.FILL
                }
            }
            OPPO_CUSTOMER_EFFECTIVE->{
                statusText.apply {
                    text = "客户有效"
                    textColor = "#FFFFFF"
                    bgColor = "#7A6C8EFF"
                    mode = StatusText.Mode.FILL
                }
            }
            OPPO_INVITATION_SUCCESSFUL->{
                statusText.apply {
                    text = "邀约成功"
                    textColor = "#FFFFFF"
                    bgColor = "#666CBF09"
                    mode = StatusText.Mode.FILL
                }
            }
            OPPO_INVALID_CUSTOMER->{
                statusText.apply {
                    text = "客户无效"
                    textColor = "#FFFFFF"
                    bgColor = "#52FF2C2C"
                    mode = StatusText.Mode.FILL
                }
            }
            OPPO_ENTERED_STORE->{
                statusText.apply {
                    text = "已进店"
                    textColor = "#FFFFFF"
                    bgColor = "#666CBF09"
                    mode = StatusText.Mode.FILL
                }
            }
            OPPO_DELETED->{
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

    fun formatOrderLabelWithDispatch(phase:String?,orderStatus:String?):StatusText?{
        val statusText = StatusText()
        if (orderStatus!=null && orderStatus.isNotEmpty() && orderStatus!="0"){
            when(orderStatus){
                ORDER_TO_BE_SIGNED ->{
                    statusText.apply {
                        text = "待签约"
                        textColor = "#FFE08B01"
                        bgColor = "#33FFB600"
                        mode = StatusText.Mode.TOP_HALF_FILL
                    }
                }
                ORDER_SIGNED ->{
                    statusText.apply {
                        text = "已签约"
                        textColor = "#FF5BA433"
                        bgColor = "#336CBF09"
                        mode = StatusText.Mode.TOP_HALF_FILL
                    }
                }
                ORDER_CHARGED ->{
                    statusText.apply {
                        text = "已退单"
                        textColor = "#FF898A8D"
                        bgColor = "#14000000"
                        mode = StatusText.Mode.TOP_HALF_FILL
                    }
                }
                ORDER_CANCELED ->{
                    statusText.apply {
                        text = "已取消"
                        textColor = "#FF898A8D"
                        bgColor = "#14000000"
                        mode = StatusText.Mode.TOP_HALF_FILL
                    }
                }
                ORDER_COMPLETED ->{
                    statusText.apply {
                        text = "已完成"
                        textColor = "#FF6C8EFF"
                        bgColor = "#FFEDF1FF"
                        mode = StatusText.Mode.TOP_HALF_FILL
                    }
                }
            }
        }else if (!phase.isNullOrEmpty()&&(phase == OPPO_CUSTOMER_EFFECTIVE || phase == OPPO_INVITATION_SUCCESSFUL)){
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

    fun formatOrderLabel(orderStatus: String):StatusText{
        val statusText = StatusText()
        when(orderStatus){
            ORDER_TO_BE_SIGNED->{
                statusText.apply {
                    text = "待签约"
                    textColor = "#FFE08B01"
                    bgColor = "#FFE08B01"
                    mode = StatusText.Mode.STROKE
                }
            }
            ORDER_SIGNED->{
                statusText.apply {
                    text = "已签约"
                    textColor = "#FF5BA433"
                    bgColor = "#FF5BA433"
                    mode = StatusText.Mode.STROKE
                }
            }
            ORDER_CHARGED->{
                statusText.apply {
                    text = "已退单"
                    textColor = "#FF898A8D"
                    bgColor = "#FF898A8D"
                    mode = StatusText.Mode.STROKE
                }
            }
            ORDER_CANCELED->{
                statusText.apply {
                    text = "已取消"
                    textColor = "#FF898A8D"
                    bgColor = "#FF898A8D"
                    mode = StatusText.Mode.STROKE
                }
            }
            ORDER_COMPLETED->{
                statusText.apply {
                    text = "已完成"
                    textColor = "#FF5BA433"
                    bgColor = "#FF5BA433"
                    mode = StatusText.Mode.STROKE
                }
            }
        }
        return statusText
    }


}