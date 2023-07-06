package com.baihe.lib_opportunity

import android.telephony.CellIdentity
import com.baihe.lib_common.entity.*
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

    fun genBottomButtons():List<ButtonTypeEntity>{
        val buttons = mutableListOf<ButtonTypeEntity>()
        buttons.add(ButtonTypeEntity().apply {
            name = "打电话"
            type = 1
        })
        buttons.add(ButtonTypeEntity().apply {
            if ((reqPhase == "230" || reqPhase == "240")&&(orderStatus.isNullOrEmpty()||orderStatus == "0")){
                name = "下发订单"
                type = 2
            }else{
                name = "写跟进"
                type = 3
            }
        })
        return buttons
    }

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
    val follow_create:String?,
    val follow:List<FollowEntity>?
){
    fun toBasicShowArray():List<KeyValueEntity>{
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.apply {
            add(KeyValueEntity().apply {
                key = "客户姓名"
                `val` = this@OpportunityDetailEntity.name
                text = "查看客户"
                action = "jump"

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
                `val` = name
            })
            add(KeyValueEntity().apply {
                key = "联系方式"
                `val` = this@OpportunityDetailEntity.phone
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
                `val` = follow_create
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
                    "0"->{
                        ""
                    }
                    "1"->{
                        "待签约"
                    }
                    "2"->{
                        "已签约"
                    }
                    "3"->{
                        "已退单"
                    }
                    "4"->{
                        "已取消"
                    }
                    "6"->{
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
        if ((phase == "230" || phase == "240")&&(order?.orderStatus.isNullOrEmpty()||order?.orderStatus == "0")){
            buttons.add(ButtonTypeEntity().apply {
                name = "下发订单"
                type = 2
            })
        }

        buttons.add(ButtonTypeEntity().apply {
            name = "写跟进"
            type = 1
        })
        return buttons
    }

    fun genMoreButtons():List<ButtonTypeEntity>{
        val buttons = mutableListOf<ButtonTypeEntity>()
        when(phase){
            "200"->{
                buttons.apply {
                    add(ButtonTypeEntity().apply {
                        name = "归档机会"
                        type = 5
                    })
                    add(ButtonTypeEntity().apply {
                        name = "编辑机会"
                        type = 3
                    })
                    add(ButtonTypeEntity().apply {
                        name = "转移"
                        type = 4
                    })
                }
            }
            "220"->{
                buttons.apply {
                    add(ButtonTypeEntity().apply {
                        name = "编辑机会"
                        type = 3
                    })
                    add(ButtonTypeEntity().apply {
                        name = "转移"
                        type = 4
                    })
                }
            }
            "230"->{
                buttons.apply {
                    add(ButtonTypeEntity().apply {
                        name = "编辑机会"
                        type = 3
                    })
                    add(ButtonTypeEntity().apply {
                        name = "转移"
                        type = 4
                    })
                }
            }
            "240"->{
                buttons.apply {
                    if (order?.orderStatus!=null||order?.orderStatus.toString()=="0"){
                        add(ButtonTypeEntity().apply {
                            name = "编辑机会"
                            type = 3
                        })
                    }
                    add(ButtonTypeEntity().apply {
                        name = "转移"
                        type = 4
                    })
                }
            }
            "210"->{
                buttons.apply {
                    add(ButtonTypeEntity().apply {
                        name = "归档机会"
                        type = 5
                    })
                    add(ButtonTypeEntity().apply {
                        name = "编辑机会"
                        type = 3
                    })
                }
            }
            "250"->{
                buttons.apply {
                    add(ButtonTypeEntity().apply {
                        name = "转移"
                        type = 4
                    })
                }
            }
            "260"->{
                buttons.apply {
                    add(ButtonTypeEntity().apply {
                        name = "编辑机会"
                        type = 3
                    })
                }
            }
        }
        return buttons
    }

}