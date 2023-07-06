package com.baihe.lib_customer.api

import androidx.lifecycle.LifecycleOwner
import com.baihe.http.EasyHttp
import com.baihe.http.model.ResponseClass
import com.baihe.lib_common.http.BaseRepository
import com.baihe.lib_common.http.api.CommonApi
import com.baihe.lib_common.http.api.JsonParam
import com.baihe.lib_common.http.response.BaseResponse
import com.baihe.lib_common.http.response.Data
import com.baihe.lib_common.http.response.ListData
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValEventEntity
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_customer.CustomerDetailEntity
import com.baihe.lib_customer.CustomerListItemEntity
import com.baihe.lib_customer.constant.UrlConstant

class CustomerRepository(lifecycle: LifecycleOwner): BaseRepository(lifecycle) {
    suspend fun customerList(page:Int,name:String?,pageSize:Int = 10): ListData<CustomerListItemEntity>? {
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("page",page)
                .putParamValue("name",name)
                .putParamValue("pageSize",pageSize)
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.CUSTOMER_LIST,params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<ListData<CustomerListItemEntity>>>() {})
        }
    }

    suspend fun customerDetail(customerId:String):CustomerDetailEntity? {
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("customerId",customerId)
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.CUSTOMER_DETAIL,params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<CustomerDetailEntity>>() {})
        }
    }

    suspend fun getCustomerTemple(customerId: String?):List<KeyValueEntity>?{
        return requestResponse {
            val kvList = mutableListOf<KeyValueEntity>()

            if (customerId.isNullOrEmpty()){
                kvList.apply {
                    add(KeyValueEntity().apply {
                        name = "来源渠道"
                        is_true = "1"
                        is_open = "1"
                        is_channge = "2"
                        type = "channe"
                        paramKey = "channel"

                    })
                    add(KeyValueEntity().apply {
                        name = "客户姓名"
                        is_true = "1"
                        is_open = "1"
                        is_channge = "2"
                        type = "input"
                        paramKey = "name"

                    })
                    add(KeyValueEntity().apply {
                        name = "联系方式"
                        is_true = "1"
                        is_open = "1"
                        is_channge = "2"
                        type = "contact"
                        paramKey = "phone,wechat"

                    })
                    add(KeyValueEntity().apply {
                        name = "首次提供人"
                        is_true = "1"
                        is_open = "1"
                        is_channge = "2"
                        type = "recordUser"
                        paramKey = "record_user_id"

                    })
                    add(KeyValueEntity().apply {
                        name = "客户身份"
                        is_true = "1"
                        is_open = "1"
                        is_channge = "2"
                        type = "select"
                        paramKey = "identity"
                        option = mutableListOf<KeyValueEntity?>().apply {
                            add(KeyValueEntity().apply {
                                name = "新娘"
                                value = "1"
                            })
                            add(KeyValueEntity().apply {
                                name = "新郎"
                                value = "2"
                            })
                            add(KeyValueEntity().apply {
                                name = "新人亲属"
                                value = "5"
                            })
                            add(KeyValueEntity().apply {
                                name = "其他"
                                value = "7"
                            })
                        }

                    })
                }
            }else{
                val customerInfo = customerDetail(customerId)
                kvList.apply {
                    add(KeyValueEntity().apply {
                        name = "来源渠道"
                        is_true = "1"
                        is_open = "1"
                        is_channge = "2"
                        type = "channe"
                        paramKey = "channel"
                        value= customerInfo?.sourceChannelId
                        defaultValue = customerInfo?.sourceChannel
                    })
                    add(KeyValueEntity().apply {
                        name = "客户姓名"
                        is_true = "1"
                        is_open = "1"
                        is_channge = "2"
                        type = "input"
                        paramKey = "name"
                        value= customerInfo?.name
                        defaultValue = customerInfo?.name

                    })
                    add(KeyValueEntity().apply {
                        name = "联系方式"
                        is_true = "1"
                        is_open = "1"
                        is_channge = "2"
                        type = "contact"
                        paramKey = "phone,wechat"
                        value = "${customerInfo?.phone},${customerInfo?.wechat}"
                        defaultValue = "${customerInfo?.phone},${customerInfo?.wechat}"

                    })
                    add(KeyValueEntity().apply {
                        name = "首次提供人"
                        is_true = "1"
                        is_open = "1"
                        is_channge = "2"
                        type = "recordUser"
                        paramKey = "record_user_id"
                        value= customerInfo?.recordUserId
                        defaultValue = customerInfo?.recordUser

                    })
                    add(KeyValueEntity().apply {
                        name = "客户身份"
                        is_true = "1"
                        is_open = "1"
                        is_channge = "2"
                        type = "select"
                        paramKey = "identity"
                        option = mutableListOf<KeyValueEntity?>().apply {
                            add(KeyValueEntity().apply {
                                name = "新娘"
                                value = "1"
                            })
                            add(KeyValueEntity().apply {
                                name = "新郎"
                                value = "2"
                            })
                            add(KeyValueEntity().apply {
                                name = "新人亲属"
                                value = "5"
                            })
                            add(KeyValueEntity().apply {
                                name = "其他"
                                value = "7"
                            })
                        }
                        value= customerInfo?.identityId
                        defaultValue = customerInfo?.identity
                    })
                }
            }

            val data  = Data(kvList,0,"3.5.0")
            BaseResponse(200,"请求成功",data)

        }
    }



    suspend fun addCustomer(params:LinkedHashMap<String,Any?>):Any? {
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
                .putParamValue(params)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.ADD_CUSTOMER,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<CustomerDetailEntity>>() {})
        }
    }

    suspend fun updateCustomer(params:LinkedHashMap<String,Any?>):Any? {
        return requestResponse {
            val jsonParam = JsonParam.newInstance()
                .putParamValue(params)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.UPDATE_CUSTOMER,jsonParam.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<CustomerDetailEntity>>() {})
        }
    }
}