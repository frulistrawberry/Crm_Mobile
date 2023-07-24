package com.baihe.lib_opportunity

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
import com.baihe.lib_common.constant.StatusConstant.ORDER_NONE
import com.baihe.lib_common.constant.StatusConstant.ORDER_SIGNED
import com.baihe.lib_common.constant.StatusConstant.ORDER_TO_BE_SIGNED
import com.baihe.lib_common.entity.*
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_DELETE_OPPO
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_DISPATCH_ORDER
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_EDIT_OPPO
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_FOLLOW
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_TRANSFER_OPPO
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.google.gson.annotations.SerializedName

class OpportunityEntity {
}




data class OpportunityDetailEntity(
    val phase:String?,
    val title:String,
    val phone: String?,
    @SerializedName("category_txt")
    val category:String?,
    @SerializedName("follow_user_id_txt")
    val followUser:String?,
    val name:String?,
    @SerializedName("live_city_code")
    val cityInfo:CityEntity?,
    @SerializedName("identity_txt")
    val identity: String?,
    @SerializedName("owner_id_txt")
    val owner:String?,
    val remark:String?,
    val reserve:ReserveInfoEntity?,
    val order:OrderInfoEntity?,
    val customer_id:String?,
    val channel_txt:String?,
    val sub_category:String?,
    val user_id_txt:String?,
    val wedding_date_from:String?,
    val wedding_date_end:String?,
    val create_time:String?,
    val update_time:String?,
    val follow_create:String?,
    val viewcustomer:Boolean,
    val vieworder:Boolean,
    val viewReaser:Boolean,
    val viewoppotion:Boolean,
    val follow:List<FollowEntity>?
){
    fun toBasicShowArray():List<KeyValueEntity>{
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.apply {
            add(KeyValueEntity().apply {
                key = "客户姓名"
                `val` = this@OpportunityDetailEntity.name
                if (viewcustomer){
                    text = "查看客户"
                    action = "jump"
                }


            })
            add(KeyValueEntity().apply {
                key = "联系方式"
                `val` = this@OpportunityDetailEntity.phone
                action = "call"
                icon = "ic_call"
            })
            add(KeyValueEntity().apply {
                key = "业务品类"
                `val` = category
            })
            add(KeyValueEntity().apply {
                key = "跟进人"
                `val` = followUser
            })
            add(KeyValueEntity().apply {
                key = "意向区域"
                `val` = cityInfo?.full
            })
            add(KeyValueEntity().apply {
                key = "客户身份"
                `val` = identity
            })
            add(KeyValueEntity().apply {
                key = "提供人"
                `val` = owner
            })
            add(KeyValueEntity().apply {
                key = "备注"
                `val` = remark
            })

        }
        return kvList
    }

    fun toAllShowArray():List<KeyValueEntity>{
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.apply {
            add(KeyValueEntity().apply {
                key = "客户编码"
                `val` = customer_id
            })
            add(KeyValueEntity().apply {
                key = "渠道"
                `val` = channel_txt
            })
            add(KeyValueEntity().apply {
                key = "客户姓名"
                `val` = this@OpportunityDetailEntity.name
            })
            add(KeyValueEntity().apply {
                key = "联系方式"
                `val` = this@OpportunityDetailEntity.phone
                action = "call"
                icon = "ic_call"
            })
            add(KeyValueEntity().apply {
                key = "业务品类"
                `val` = category
            })
            add(KeyValueEntity().apply {
                key = "业务子品类"
                `val` = sub_category
            })
            add(KeyValueEntity().apply {
                key = "跟进人"
                `val` = followUser
            })
            add(KeyValueEntity().apply {
                key = "提供人"
                `val` = owner
            })
            add(KeyValueEntity().apply {
                key = "创建人"
                `val` = user_id_txt
            })
            add(KeyValueEntity().apply {
                key = "意向区域"
                `val` = cityInfo?.full
            })
            add(KeyValueEntity().apply {
                key = "客户身份"
                `val` = identity
            })
            add(KeyValueEntity().apply {
                key = "预计婚期"
                `val` = "${wedding_date_from}-${wedding_date_end}"
            })
            add(KeyValueEntity().apply {
                key = "机会创建时间"
                `val` = create_time
            })
            add(KeyValueEntity().apply {
                key = "最新编辑时间"
                `val` = update_time
            })
            add(KeyValueEntity().apply {
                key = "备注"
                `val` = remark
            })

        }
        return kvList
    }

    fun toReserveShowArray():List<KeyValueEntity>{
        val kvList = mutableListOf<KeyValueEntity>()
        if (reserve == null || reserve.arrivalTime.isNullOrEmpty()){
            return kvList
        }
        kvList.apply {
            add(KeyValueEntity().apply {
                key = "计划到店时间"
                `val` = reserve?.arrivalTime
            })
            add(KeyValueEntity().apply {
                key = "沟通内容"
                `val` = reserve?.comment
            })

        }
        return kvList
    }

    fun toOrderShowArray():List<KeyValueEntity>{
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.apply {
            add(KeyValueEntity().apply {
                key = "订单状态"
                `val` = when(order?.orderStatus){
                    ORDER_NONE->{
                        ""
                    }
                    ORDER_TO_BE_SIGNED->{
                        "待签约"
                    }
                    ORDER_SIGNED->{
                        "已签约"
                    }
                    ORDER_CHARGED->{
                        "已退单"
                    }
                    ORDER_CANCELED->{
                        "已取消"
                    }
                    ORDER_COMPLETED->{
                        "已完成"
                    }
                    else->{
                        ""
                    }
                }
            })
            add(KeyValueEntity().apply {
                key = "跟进人"
                `val` = order?.name
            })

        }
        return kvList
    }
    fun getOppoLabel():StatusText?{
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


    fun genBottomButtons():List<ButtonTypeEntity>{
        val buttons = mutableListOf<ButtonTypeEntity>()
        if ((phase == "230" || phase == "240")&&(order == null || order.orderStatus.isNullOrEmpty()|| order.orderStatus == "0")){
            buttons.add(ButtonTypeEntity().apply {
                name = "下发订单"
                type = ACTION_DISPATCH_ORDER
            })
        }

        buttons.add(ButtonTypeEntity().apply {
            name = "写跟进"
            type = ACTION_FOLLOW
        })
        return buttons
    }

    fun genMoreButtons():List<ButtonTypeEntity>{
        val buttons = mutableListOf<ButtonTypeEntity>()
        when(phase){
            OPPO_TO_BE_INVITED->{
                buttons.apply {
                    add(ButtonTypeEntity().apply {
                        name = "归档机会"
                        type = ACTION_DELETE_OPPO
                    })
                    add(ButtonTypeEntity().apply {
                        name = "编辑机会"
                        type = ACTION_EDIT_OPPO
                    })
                    add(ButtonTypeEntity().apply {
                        name = "转移"
                        type = ACTION_TRANSFER_OPPO
                    })
                }
            }
            OPPO_CUSTOMER_TBD,OPPO_CUSTOMER_EFFECTIVE,OPPO_INVITATION_SUCCESSFUL->{
                buttons.apply {
                    add(ButtonTypeEntity().apply {
                        name = "编辑机会"
                        type = ACTION_EDIT_OPPO
                    })
                    add(ButtonTypeEntity().apply {
                        name = "转移"
                        type = ACTION_TRANSFER_OPPO
                    })
                }
            }
            OPPO_INVALID_CUSTOMER->{
                buttons.apply {
                    add(ButtonTypeEntity().apply {
                        name = "归档机会"
                        type = ACTION_DELETE_OPPO
                    })
                    add(ButtonTypeEntity().apply {
                        name = "编辑机会"
                        type = ACTION_EDIT_OPPO
                    })
                }
            }
            OPPO_ENTERED_STORE->{
                buttons.apply {
                    add(ButtonTypeEntity().apply {
                        name = "转移"
                        type = ACTION_TRANSFER_OPPO
                    })
                }
            }
            OPPO_DELETED->{
                buttons.apply {
                    add(ButtonTypeEntity().apply {
                        name = "编辑机会"
                        type = ACTION_EDIT_OPPO
                    })
                }
            }
        }
        return buttons
    }

}