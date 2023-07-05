package com.baihe.lib_common.utils

import com.baihe.lib_common.entity.StatusText

object FormatUtils {
    fun formatOppoLabel(phase:String?):StatusText?{
        val statusText = StatusText()
        when(phase){
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

    fun formatOrderLabelWithDispatch(phase:String?,orderStatus:String?):StatusText?{
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
        }else if (!phase.isNullOrEmpty()&&(phase == "230" || phase == "240")){
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

    fun formatOrderLabel(orderStatus: String?):StatusText?{
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
            return statusText
        }
        return null
    }
}