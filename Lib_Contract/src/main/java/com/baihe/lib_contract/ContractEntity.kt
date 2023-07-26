package com.baihe.lib_contract

import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity

class ContractEntity {
}
data class ContractListItemEntity(
    val system_no:String?,
    val contract_alias:String?,
    val name:String?,
    val phone:String?,
    val sign_amount:String?,
    val contract_type:String?,
    val follow_user_id_txt:String?,
    val create_time:String?,
    val id:String?,
    ){
    fun showArray():List<KeyValueEntity>{
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.add(KeyValueEntity().apply {
            key = "结算方式"
            `val` = contract_type
        })
        kvList.add(KeyValueEntity().apply {
            key = "客户姓名"
            `val` = this@ContractListItemEntity.name
        })
        kvList.add(KeyValueEntity().apply {
            key = "客户手机号"
            `val` = this@ContractListItemEntity.phone
        })
        kvList.add(KeyValueEntity().apply {
            key = "签单人"
            `val` = this@ContractListItemEntity.follow_user_id_txt
        })
        kvList.add(KeyValueEntity().apply {
            key = "执行日期"
            `val` = this@ContractListItemEntity.create_time
        })
        return kvList
    }
}

data class ContractDetailEntity(
    val bride: String?,
    val bride_mobile: String?,
    val bride_mobile_txt: String?,
    val category: String?,
    val category_txt: String?,
    val cd_price: String?,
    val contact: String?,
    val contact_identity: Int,
    val contact_identity_txt: String?,
    val contact_mobile: String,
    val contact_mobile_txt: String,
    val contract_alias: String,
    val contract_id: Int,
    val contract_pic: String?,
    val contract_type: String,
    val create_by: Int,
    val create_by_txt: String,
    val create_time: String,
    val customer_id: Int,
    val dq_beginTime: Any,
    val dq_endTime: String?,
    val expo_num: String,
    val final_category: String,
    val final_category_txt: String,
    val first_plan_amount: String,
    val follow_user_id: Int,
    val follow_user_id_txt: String,
    val groom: String,
    val groom_mobile: String,
    val groom_mobile_txt: String,
    val hotel: String,
    val hotel_hall: String,
    val hotel_hall_txt: String,
    val hotel_tables: String?,
    val name: String,
    val order_id: String,
    val other_price: String,
    val owner_id: String,
    val owner_id_txt: String,
    val per_budget: String?,
    val phone: String,
    val plan_receivables_date1: String,
    val plan_receivables_date2: String,
    val plan_receivables_date3: String,
    val plan_type: Int,
    val plan_type_txt: String,
    val remark: String?,
    val req_id: Int,
    val schedule: String?,
    val schedule_txt: String,
    val second_plan_amount: String,
    val see_phone: String,
    val servicer_ate: String,
    val sign_amount: String,
    val sign_date: String,
    val system_no: String,
    val third_plan_amount: String,
    val type: String,
    val wedding_date: String,
    val yb_table: String?
){
    fun showArray():List<KeyValueEntity>{
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.add(KeyValueEntity().apply {
            key = "合同编号"
            `val` = system_no
        })
        kvList.add(KeyValueEntity().apply {
            key = "合同标题"
            `val` = contract_alias
        })
        kvList.add(KeyValueEntity().apply {
            key = "结算方式"
            `val` = contract_type
        })
        kvList.add(KeyValueEntity().apply {
            key = "客户姓名"
            `val` = this@ContractDetailEntity.name
        })
        kvList.add(KeyValueEntity().apply {
            key = "合同金额"
            `val` = this@ContractDetailEntity.sign_amount
        })
        kvList.add(KeyValueEntity().apply {
            key = "婚博会单号"
            `val` = this@ContractDetailEntity.expo_num
        })
        kvList.add(KeyValueEntity().apply {
            key = "签单人"
            `val` = this@ContractDetailEntity.owner_id_txt
        })
        kvList.add(KeyValueEntity().apply {
            key = "执行时间"
            `val` = wedding_date
        })
        kvList.add(KeyValueEntity().apply {
            key = "选择回款计划"
            `val` = plan_type_txt
        })
        kvList.add(KeyValueEntity().apply {
            key = "首期款"
            `val` = first_plan_amount
        })
        kvList.add(KeyValueEntity().apply {
            key = "回款时间"
            `val` = plan_receivables_date1
        })
        kvList.add(KeyValueEntity().apply {
            key = "中期款"
            `val` = second_plan_amount
        })
        kvList.add(KeyValueEntity().apply {
            key = "回款时间"
            `val` = plan_receivables_date2
        })
        kvList.add(KeyValueEntity().apply {
            key = "尾期款"
            `val` = third_plan_amount
        })
        kvList.add(KeyValueEntity().apply {
            key = "回款时间"
            `val` = plan_receivables_date3
        })
        kvList.add(KeyValueEntity().apply {
            key = "场地名称"
            `val` = hotel
        })
        kvList.add(KeyValueEntity().apply {
            key = "宴会厅"
            `val` = hotel_hall_txt
        })
        kvList.add(KeyValueEntity().apply {
            key = "档期日期"
            `val` = wedding_date
        })
        kvList.add(KeyValueEntity().apply {
            key = "档期时段"
            `val` = schedule_txt
        })
        kvList.add(KeyValueEntity().apply {
            key = "婚礼桌数"
            `val` = hotel_tables
        })
        kvList.add(KeyValueEntity().apply {
            key = "备桌"
            `val` = yb_table
        })
        kvList.add(KeyValueEntity().apply {
            key = "每桌价格"
            `val` = per_budget
        })
        kvList.add(KeyValueEntity().apply {
            key = "合同照片"
            `val` = ""
            attach = mutableListOf()
            if (contract_pic?.contains(",") == true){
                val urls = contract_pic.split(",")
                attach.addAll(urls)
            }else if (!contract_pic.isNullOrEmpty()){
                attach.add(contract_pic)

            }
        })
        kvList.add(KeyValueEntity().apply {
            key = "合同备注"
            `val` = remark
        })


        return kvList
    }

    fun additionalShowArray():List<KeyValueEntity>{
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.add(KeyValueEntity().apply {
            key = "新娘姓名"
            `val` = bride
        })
        kvList.add(KeyValueEntity().apply {
            key = "新郎手机号"
            `val` = groom_mobile
        })
        kvList.add(KeyValueEntity().apply {
            key = "联系人姓名"
            `val` = contact
        })
        kvList.add(KeyValueEntity().apply {
            key = "联系人电话"
            `val` = contact_mobile


        })
        kvList.add(KeyValueEntity().apply {
            key = "联系人身份"
            `val` = contact_identity_txt
        })
        kvList.add(KeyValueEntity().apply {
            key = "场地费用"
            `val` = cd_price
        })
        kvList.add(KeyValueEntity().apply {
            key = "其他价格"
            `val` = other_price
        })
        kvList.add(KeyValueEntity().apply {
            key = "服务费用"
            `val` = servicer_ate
        })
        return kvList
    }

    fun basicShowArray():List<KeyValueEntity>{
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.add(KeyValueEntity().apply {
            key = "合同创建时间"
            `val` = sign_date
        })
        kvList.add(KeyValueEntity().apply {
            key = "合同创建人"
            `val` = create_by_txt
        })
        return kvList
    }
}

data class TempleEntity(val row:ContractTemple)

data class ContractTemple(val contractInfo:List<KeyValueEntity>,val contract:List<KeyValueEntity>)