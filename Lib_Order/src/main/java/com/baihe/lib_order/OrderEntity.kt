package com.baihe.lib_order

import com.baihe.lib_common.constant.StatusConstant
import com.baihe.lib_common.constant.StatusConstant.OPPO_ENTERED_STORE
import com.baihe.lib_common.constant.StatusConstant.OPPO_INVITATION_SUCCESSFUL
import com.baihe.lib_common.constant.StatusConstant.ORDER_CANCELED
import com.baihe.lib_common.constant.StatusConstant.ORDER_CHARGED
import com.baihe.lib_common.constant.StatusConstant.ORDER_COMPLETED
import com.baihe.lib_common.constant.StatusConstant.ORDER_PHASE_CUSTOMER_EFFECTIVE
import com.baihe.lib_common.constant.StatusConstant.ORDER_PHASE_CUSTOMER_TBD
import com.baihe.lib_common.constant.StatusConstant.ORDER_PHASE_EFFECTIVE_ARRIVAL
import com.baihe.lib_common.constant.StatusConstant.ORDER_PHASE_ENTERED_STORE
import com.baihe.lib_common.constant.StatusConstant.ORDER_PHASE_INVALID_CUSTOMER
import com.baihe.lib_common.constant.StatusConstant.ORDER_PHASE_INVALID_STORE_ARRIVAL
import com.baihe.lib_common.constant.StatusConstant.ORDER_PHASE_STORE_TO_BE_ENTERED
import com.baihe.lib_common.constant.StatusConstant.ORDER_PHASE_TO_BE_INVITED
import com.baihe.lib_common.constant.StatusConstant.ORDER_SIGNED
import com.baihe.lib_common.constant.StatusConstant.ORDER_TO_BE_SIGNED
import com.baihe.lib_common.entity.ButtonTypeEntity
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_CHARGE_ORDER
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_CONFIRM_ARRIVAL
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_EDIT_CONTRACT
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_EDIT_ORDER
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_FOLLOW
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_SET_PEOPLE
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_SIGN
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_TRANSFER_ORDER
import com.baihe.lib_common.entity.CityEntity
import com.baihe.lib_common.entity.FollowEntity
import com.baihe.lib_common.entity.OpportunityListItemEntity
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_common.utils.FormatUtils


data class OrderTempleEntity(
    val oppoTemple:List<KeyValueEntity>? = null,
    val oppoList:List<OpportunityListItemEntity>? = null
    )
data class OrderListItemEntity(
    val active_commission: String,
    val arrival_time: String,
    val banquet_commission: String,
    val channel: String,
    val channel_txt: String,
    val city_code: String,
    val city_info: CityEntity?,
    val company_id: String,
    val create_time: String,
    val customer_id: String,
    val customer_name: String,
    val detailed_source: String,
    val est_arrival_time: String,
    val final_category_txt: String,
    val follow_content: String,
    val follow_time: String,
    val follow_user_id: Int,
    val hide_phone: String,
    val invalid_reason: String,
    val main_category: String,
    val next_contact_time: String,
    val one_stop_commission: String,
    val order_id: String,
    val order_phase: String,
    val order_phase_txt: String,
    val orderstatus: String,
    val orderstatus_txt: String,
    val outhotel: String,
    val outhotel_txt: String,
    val owner_id: String,
    val owner_id_name: String,
    val phone: String,
    val remark: String,
    val req_id: String,
    val req_phase: String,
    val req_phase_txt: String,
    val status_datetime: String,
    val sub_category: String,
    val title: String,
    val top_category_txt: String,
    val wedding_commission: String,
    val wedding_date: String,
    val wedding_date_end: String,
    val wedding_date_from: String
){
    fun showArray():List<KeyValueEntity>{
        val showArray = mutableListOf<KeyValueEntity>()
        showArray.add(KeyValueEntity().apply {
            key = "联系电话"
            `val` = hide_phone
        })
        if (ORDER_TO_BE_SIGNED==orderstatus){
            when(order_phase){
                ORDER_PHASE_INVALID_STORE_ARRIVAL,
                ORDER_PHASE_CUSTOMER_TBD,
                ORDER_PHASE_CUSTOMER_EFFECTIVE,
                ORDER_PHASE_EFFECTIVE_ARRIVAL, ->{
                    showArray.add(KeyValueEntity().apply {
                        key = "下次回访时间"
                        `val` = status_datetime
                    })
                }
                ORDER_PHASE_STORE_TO_BE_ENTERED ->{
                    showArray.add(KeyValueEntity().apply {
                        key = "计划进店时间"
                        `val` = status_datetime
                    })
                }
                ORDER_PHASE_ENTERED_STORE ->{
                    showArray.add(KeyValueEntity().apply {
                        key = "到店时间"
                        `val` = status_datetime
                    })
                }

            }
        }else{
            when(orderstatus){
                ORDER_SIGNED->{
                    showArray.add(KeyValueEntity().apply {
                        key = "档期"
                        `val` = wedding_date
                    })
                    showArray.add(KeyValueEntity().apply {
                        key = "意向区域"
                        `val` = city_info?.full
                    })
                }
                ORDER_CANCELED->{
                    showArray.add(KeyValueEntity().apply {
                        key = "取消时间"
                        `val` = status_datetime
                    })
                }
                ORDER_CHARGED->{
                    showArray.add(KeyValueEntity().apply {
                        key = "退单时间"
                        `val` = status_datetime
                    })
                }
            }
        }
        showArray.add(KeyValueEntity().apply {
            key = "最新沟通记录"
            `val` = follow_content
        })
        showArray.add(KeyValueEntity().apply {
            key = "跟进人"
            `val` = owner_id_name
        })
        return showArray
    }

    fun genBottomButtons():List<ButtonTypeEntity>{
        val buttons = mutableListOf<ButtonTypeEntity>()
        if (ORDER_TO_BE_SIGNED==orderstatus){
            buttons.add(ButtonTypeEntity().apply {
                name = "打电话"
                type = ButtonTypeEntity.ACTION_CALL
            })
            when(order_phase){
                ORDER_PHASE_TO_BE_INVITED,
                ORDER_PHASE_INVALID_STORE_ARRIVAL,
                ORDER_PHASE_CUSTOMER_TBD,
                ORDER_PHASE_EFFECTIVE_ARRIVAL,
                ORDER_PHASE_INVALID_CUSTOMER, ->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "写跟进"
                        type = ACTION_FOLLOW
                    })
                }
                ORDER_PHASE_CUSTOMER_EFFECTIVE,
                ORDER_PHASE_STORE_TO_BE_ENTERED,
                ORDER_PHASE_ENTERED_STORE,
                ->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "签约"
                        type = ACTION_SIGN
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "确认到店"
                        type = ACTION_CONFIRM_ARRIVAL
                    })
                }

            }
        }else{
            when(orderstatus){
                ORDER_SIGNED->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "打电话"
                        type = ButtonTypeEntity.ACTION_CALL
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "设置人员"
                        type = ACTION_SET_PEOPLE
                    })
                }
                ORDER_CANCELED,
                ORDER_CHARGED->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "打电话"
                        type = ButtonTypeEntity.ACTION_CALL
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "写跟进"
                        type = ACTION_FOLLOW
                    })
                }
            }
        }
        return buttons
    }

}

data class OrderDetailEntity(
    val authority: Authority?,
    val contractInfo: ContractInfo?,
    val followData: FollowEntity?,
    val foundationInfo: FoundationInfo?,
    val lamp: Lamp?,
    val opportunityInfo: OpportunityInfo?,
    val peopleInfo: List<KeyValueEntity>?,
    val yuyueData: ReserveData?
){
    fun basicShowArray():List<KeyValueEntity>{
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.apply {
            add(KeyValueEntity().apply {
                key = "客户姓名"
                `val` = opportunityInfo?.customer_name
                if (authority?.viewcustomer == true){
                    text = "查看客户"
                    action = "jump"
                }


            })
            add(KeyValueEntity().apply {
                key = "联系方式"
                `val` = opportunityInfo?.phone
                action = "call"
                icon = "ic_call"
            })
            add(KeyValueEntity().apply {
                key = "业务品类"
                `val` = opportunityInfo?.top_category_txt
            })
            add(KeyValueEntity().apply {
                key = "来源渠道"
                `val` = opportunityInfo?.channel_txt
            })
            add(KeyValueEntity().apply {
                key = "跟进人"
                `val` = foundationInfo?.req_follow_user
            })
            add(KeyValueEntity().apply {
                key = "意向区域"
                `val` = opportunityInfo?.city_code?.full
            })
            add(KeyValueEntity().apply {
                key = "客户身份"
                `val` = opportunityInfo?.identity
            })
            add(KeyValueEntity().apply {
                key = "备注"
                `val` = opportunityInfo?.remark
            })

        }
        return kvList
    }
    fun orderAllShowArray():List<KeyValueEntity>{
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.add(KeyValueEntity().apply {
            key = "下发人员"
            `val` = foundationInfo?.req_follow_user
        })
        kvList.add(KeyValueEntity().apply {
            key = "当前跟进人"
            `val` = foundationInfo?.order_follow_user
        })
        kvList.add(KeyValueEntity().apply {
            key = "当前状态"
            `val` = foundationInfo?.order_status
        })
        kvList.add(KeyValueEntity().apply {
            key = "当前跟进状态"
            `val` = opportunityInfo?.order_phase_status
        })
        kvList.add(KeyValueEntity().apply {
            key = "进店时间"
            `val` = lamp?.daodian_time
        })
        kvList.add(KeyValueEntity().apply {
            key = "创建时间"
            `val` = opportunityInfo?.order_create_time
        })
        kvList.add(KeyValueEntity().apply {
            key = "最新跟进时间"
            `val` = yuyueData?.create_time
        })
        return kvList
    }
    fun orderShowArray():List<KeyValueEntity>{
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.add(KeyValueEntity().apply {
            key = "跟进人"
            `val` = foundationInfo?.order_follow_user
        })
        return kvList
    }
    fun genOrderStatus():List<OrderStatus>{
        val statusList = mutableListOf<OrderStatus>()
        statusList.add(OrderStatus().apply {
            statusTxt = "邀约成功"
            date = lamp?.yaoyue_time
            opportunityInfo?.let {
                status = when(opportunityInfo.req_phase){
                    OPPO_INVITATION_SUCCESSFUL->{
                        1
                    }
                    OPPO_ENTERED_STORE->{
                        2
                    }
                    else ->{
                        -1
                    }
                }
            }
        })
        statusList.add(OrderStatus().apply {
            statusTxt = "已进店"
            date = lamp?.daodian_time
            opportunityInfo?.let {
                status = when(opportunityInfo.req_phase){
                    OPPO_ENTERED_STORE->{
                        if (opportunityInfo.orderstatus== ORDER_TO_BE_SIGNED){
                            1
                        }else{
                            2
                        }
                    }
                    else ->{
                        -1
                    }
                }
            }
        })
        statusList.add(OrderStatus().apply {
            statusTxt = "已签约"
            date = lamp?.qianyue_time
            opportunityInfo?.let {
                status = when(opportunityInfo.orderstatus){
                    ORDER_SIGNED->{
                        2
                    }
                    ORDER_COMPLETED->{
                        1
                    }
                    else->{
                        -1
                    }
                }
            }
        })
        statusList.add(OrderStatus().apply {
            statusTxt = "已完成"
            date = lamp?.wancheng_time
            opportunityInfo?.let {
                status = when(opportunityInfo.orderstatus){
                    ORDER_COMPLETED->{
                        2
                    }
                    else->{
                        -1
                    }
                }
            }
        })
        return statusList
    }
    fun reqShowArray():List<KeyValueEntity>?{
        if (yuyueData==null||(yuyueData.arrival_time.isNullOrEmpty()&&yuyueData.next_contact_time.isNullOrEmpty()))
            return null
        val kvList = mutableListOf<KeyValueEntity>()
        yuyueData.arrival_time?.let {
            kvList.add(KeyValueEntity().apply {
                key = "客户计划进店时间"
                `val` = yuyueData.arrival_time
            })
        }
        yuyueData.next_contact_time?.let {
            kvList.add(KeyValueEntity().apply {
                key = "计划回访时间"
                `val` = yuyueData.next_contact_time
            })
        }
        kvList.add(KeyValueEntity().apply {
            key = "沟通内容"
            `val` = yuyueData.comment
        })
        return kvList

    }
    fun genReqButton(): ButtonTypeEntity? {
        return if (!yuyueData?.arrival_time.isNullOrEmpty()){
            ButtonTypeEntity().apply {
                type = ACTION_CONFIRM_ARRIVAL
                name = "确认到店"
            }
        }else if (!yuyueData?.next_contact_time.isNullOrEmpty()){
            ButtonTypeEntity().apply {
                type = ACTION_FOLLOW
                name = "录跟进"
            }
        }else{
            null
        }
    }
    fun contractShowArray():List<KeyValueEntity>?{
        contractInfo?.let {
            if (contractInfo.id.isNullOrEmpty())
                return null
            val kvList = mutableListOf<KeyValueEntity>()
            kvList.add(KeyValueEntity().apply {
                key = "合同编号"
                `val` = contractInfo.system_no
            })
            kvList.add(KeyValueEntity().apply {
                key = "合同金额"
                `val` = contractInfo.sign_amount
            })
            return kvList
        }
        return null
    }
    fun genBottomButtons():List<ButtonTypeEntity>{
        val buttons = mutableListOf<ButtonTypeEntity>()
        if (ORDER_TO_BE_SIGNED==opportunityInfo?.orderstatus){
            when(opportunityInfo.order_phase){
                ORDER_PHASE_CUSTOMER_EFFECTIVE->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "设置人员"
                        type = ACTION_SET_PEOPLE
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "签约"
                        type = ACTION_SIGN
                    })
//                    buttons.add(ButtonTypeEntity().apply {
//                        name = "写跟进"
//                        type = ACTION_FOLLOW
//                    })
                }
                ORDER_PHASE_EFFECTIVE_ARRIVAL->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "写跟进"
                        type = ACTION_FOLLOW
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "签约"
                        type = ACTION_SIGN
                    })
                }
                ORDER_PHASE_TO_BE_INVITED->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "写跟进"
                        type = ACTION_FOLLOW
                    })
                }
                ORDER_PHASE_INVALID_STORE_ARRIVAL->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "设置人员"
                        type = ACTION_SET_PEOPLE
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "写跟进"
                        type = ACTION_FOLLOW
                    })
                }
                ORDER_PHASE_STORE_TO_BE_ENTERED->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "设置人员"
                        type = ACTION_SET_PEOPLE
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "签约"
                        type = ACTION_SIGN
                    })
//                    buttons.add(ButtonTypeEntity().apply {
//                        name = "写跟进"
//                        type = ACTION_FOLLOW
//                    })
                }
                ORDER_PHASE_CUSTOMER_TBD->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "写跟进"
                        type = ACTION_FOLLOW
                    })
                }
                ORDER_PHASE_INVALID_CUSTOMER->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "写跟进"
                        type = ACTION_FOLLOW
                    })
                }
                ORDER_PHASE_ENTERED_STORE->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "设置人员"
                        type = ACTION_SET_PEOPLE
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "签约"
                        type = ACTION_SIGN
                    })
//                    buttons.add(ButtonTypeEntity().apply {
//                        name = "写跟进"
//                        type = ACTION_FOLLOW
//                    })
                }




            }
        }else{
            when(opportunityInfo?.orderstatus){
                ORDER_SIGNED->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "设置人员"
                        type = ACTION_SET_PEOPLE
                    })
                }
                ORDER_CANCELED,
                ORDER_CHARGED->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "写跟进"
                        type = ACTION_FOLLOW
                    })
                }
            }
        }
        return buttons
    }

    fun genMoreButtons():List<ButtonTypeEntity>{
        val buttons = mutableListOf<ButtonTypeEntity>()
        if (ORDER_TO_BE_SIGNED==opportunityInfo?.orderstatus){
            when(opportunityInfo.order_phase){
                ORDER_PHASE_CUSTOMER_EFFECTIVE->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "转移给他人"
                        type = ACTION_TRANSFER_ORDER
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "修改订单"
                        type = ACTION_EDIT_ORDER
                    })
                }
                ORDER_PHASE_EFFECTIVE_ARRIVAL->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "设置人员"
                        type = ACTION_SET_PEOPLE
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "转移给他人"
                        type = ACTION_TRANSFER_ORDER
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "修改订单"
                        type = ACTION_EDIT_ORDER
                    })
                }
                ORDER_PHASE_TO_BE_INVITED->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "转移给他人"
                        type = ACTION_TRANSFER_ORDER
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "修改订单"
                        type = ACTION_EDIT_ORDER
                    })
                }
                ORDER_PHASE_INVALID_STORE_ARRIVAL->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "转移给他人"
                        type = ACTION_TRANSFER_ORDER
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "修改订单"
                        type = ACTION_EDIT_ORDER
                    })
                }
                ORDER_PHASE_STORE_TO_BE_ENTERED->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "转移给他人"
                        type = ACTION_TRANSFER_ORDER
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "修改订单"
                        type = ACTION_EDIT_ORDER
                    })
                }
                ORDER_PHASE_CUSTOMER_TBD->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "转移给他人"
                        type = ACTION_TRANSFER_ORDER
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "修改订单"
                        type = ACTION_EDIT_ORDER
                    })
                }
                ORDER_PHASE_INVALID_CUSTOMER->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "修改订单"
                        type = ACTION_EDIT_ORDER
                    })
                }
                ORDER_PHASE_ENTERED_STORE->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "转移给他人"
                        type = ACTION_TRANSFER_ORDER
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "修改订单"
                        type = ACTION_EDIT_ORDER
                    })
                }




            }
        } else{
            when(opportunityInfo?.orderstatus){
                ORDER_SIGNED->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "申请退单"
                        type = ACTION_CHARGE_ORDER
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "编辑合同"
                        type = ACTION_EDIT_CONTRACT
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "转移给他人"
                        type = ACTION_TRANSFER_ORDER
                    })
                    buttons.add(ButtonTypeEntity().apply {
                        name = "修改订单"
                        type = ACTION_EDIT_ORDER
                    })
                }
                ORDER_CANCELED,
                ORDER_CHARGED->{
                    buttons.add(ButtonTypeEntity().apply {
                        name = "编辑订单"
                        type = ACTION_EDIT_ORDER
                    })
                }
            }
        }
        return buttons
    }

}


data class ContractInfo(
    val id:String?,
    val system_no:String?,
    val sign_amount:String?,
)

data class Authority(
    val viewReaser: Boolean,
    val viewcustomer: Boolean,
    val viewoppoLog: Boolean,
    val viewoppotion: Boolean,
    val vieworder: Boolean
)

data class ReserveData(
    val contact_way_txt:String?,
    val phase_txt:String?,
    val reserve_txt:String?,
    val arrival_time:String?,
    val next_contact_time:String?,
    val comment:String?,
    val attachment:String?,
    val create_by_txt:String?,
    val create_time:String?,
){
     fun showArray():List<KeyValueEntity>{
         val kvList = mutableListOf<KeyValueEntity>()
         kvList.add(KeyValueEntity().apply {
             key = "跟进方式"
             `val` = contact_way_txt
         })
         kvList.add(KeyValueEntity().apply {
             key = "跟进结果"
             `val` = phase_txt
         })
         kvList.add(KeyValueEntity().apply {
             key = "预约进店"
             `val` = reserve_txt
         })
         kvList.add(KeyValueEntity().apply {
             key = "预约进店时间"
             `val` = arrival_time
         })
         kvList.add(KeyValueEntity().apply {
             key = "下次回访时间"
             `val` = next_contact_time
         })
         kvList.add(KeyValueEntity().apply {
             key = "沟通内容"
             `val` = comment
         })
         kvList.add(KeyValueEntity().apply {
             key = "附件"
             `val` = ""
             attach = mutableListOf()
             if (attachment?.contains(",") == true){
                 val urls = attachment.split(",")
                 attach.addAll(urls)
             }else if (!attachment.isNullOrEmpty()){
                 attach.add(attachment)

             }
         })
         kvList.add(KeyValueEntity().apply {
             key = "跟进人"
             `val` = create_by_txt
         })
         kvList.add(KeyValueEntity().apply {
             key = "跟进时间"
             `val` = create_time
         })
         return kvList
     }
}

data class FoundationInfo(
    val order_follow_user: String,
    val order_id: Int,
    val order_status: String,
    val req_follow_user: String,
    val title: String
)

data class Lamp(
    val daodian_time: String?,
    val qianyue_time: String?,
    val wancheng_time: String?,
    val yaoyue_time: String?
)

data class OpportunityInfo(
    val active_commission: String,
    val arrival_time: Any,
    val banquet_commission: String,
    val channel: String,
    val channel_txt: String,
    val city_code: CityEntity,
    val contract_type: String,
    val create_user_id: Int,
    val create_user_name: String,
    val customer_id: String,
    val customer_name: String,
    val final_category_txt: String,
    val follow_user_id: Int,
    val follow_user_name: String?,
    val hide_phone: String,
    val identity: String,
    val main_category: String,
    val one_stop_commission: String,
    val order_create_time: String,
    val order_follow_time: Any,
    val order_id: String?,
    val order_num: String,
    val order_owner_id: Int,
    val order_phase: String,
    val order_phase_status: String,
    val orderstatus: String,
    val orderstatus_txt: String,
    val owner_id: Int,
    val owner_name: String,
    val phone: String,
    val remark: String,
    val req_create_time: String,
    val req_id: String,
    val req_phase: String,
    val req_status: String,
    val req_type: String,
    val sign_date: Any,
    val sub_category: String,
    val title: String,
    val top_category_txt: String,
    val wedding_commission: String,
    val wedding_date: String,
    val wedding_date_end: String,
    val wedding_date_from: String

)
class  OrderStatus{
    var date:String? = null
    var status:Int = -1
    var statusTxt:String? = null
}
