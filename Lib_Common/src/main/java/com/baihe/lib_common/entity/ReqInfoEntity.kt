package com.baihe.lib_common.entity

import com.baihe.lib_common.entity.StatusText.Mode
import com.google.gson.annotations.SerializedName

data class ReqInfoEntity(
    val title:String?,
    @SerializedName("req_owner")
    val reqOwner:String?,
    @SerializedName("order_owner")
    val orderOwner:String?,
    val phase:Int?
){
    fun getPhaseLabel():StatusText?{
        val statusText = StatusText()
        when(phase){
            200->{
                statusText.apply {
                    text = "待邀约"
                    textColor = "#FFFFFF"
                    bgColor = "#7AFF8B00"
                    mode = Mode.FILL
                }
            }
            220->{
                statusText.apply {
                    text = "客户待定"
                    textColor = "#FFFFFF"
                    bgColor = "#66FFB600"
                    mode = Mode.FILL
                }
            }
            230->{
                statusText.apply {
                    text = "客户有效"
                    textColor = "#FFFFFF"
                    bgColor = "#7A6C8EFF"
                    mode = Mode.FILL
                }
            }
            240->{
                statusText.apply {
                    text = "邀约成功"
                    textColor = "#FFFFFF"
                    bgColor = "#666CBF09"
                    mode = Mode.FILL
                }
            }
            210->{
                statusText.apply {
                    text = "客户无效"
                    textColor = "#FFFFFF"
                    bgColor = "#52FF2C2C"
                    mode = Mode.FILL
                }
            }
            250->{
                statusText.apply {
                    text = "已进店"
                    textColor = "#FFFFFF"
                    bgColor = "#666CBF09"
                    mode = Mode.FILL
                }
            }
            260->{
                statusText.apply {
                    text = "已删除"
                    textColor = "#FFFFFF"
                    bgColor = "#667D7D7D"
                    mode = Mode.FILL
                }
            }
            1 ->{
                statusText.apply {
                    text = "待签约"
                    textColor = "#FFE08B01"
                    bgColor = "#FFE08B01"
                    mode = Mode.STROKE
                }
            }
            2 ->{
                statusText.apply {
                    text = "已签约"
                    textColor = "#FF5BA433"
                    bgColor = "#FF5BA433"
                    mode = Mode.STROKE
                }
            }
            3 ->{
                statusText.apply {
                    text = "已退单"
                    textColor = "#FF898A8D"
                    bgColor = "#FF898A8D"
                    mode = Mode.STROKE
                }
            }
            4 ->{
                statusText.apply {
                    text = "已取消"
                    textColor = "#FF898A8D"
                    bgColor = "#FF898A8D"
                    mode = Mode.STROKE
                }
            }
            6 ->{
                statusText.apply {
                    text = "已完成"
                    textColor = "#FF5BA433"
                    bgColor = "#FF5BA433"
                    mode = Mode.STROKE
                }
            }
            else ->{
                return null
            }
        }
        return statusText
    }
}