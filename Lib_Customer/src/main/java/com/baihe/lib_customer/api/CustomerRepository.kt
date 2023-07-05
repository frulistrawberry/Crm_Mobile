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
                        key = "来源渠道"
                        optional = "1"
                        showStatus = "1"
                        editable = "2"
                        event = KeyValEventEntity().apply {
                            action = "channe"
                            paramKey = "channel"
                        }

                    })
                    add(KeyValueEntity().apply {
                        key = "客户姓名"
                        optional = "1"
                        showStatus = "1"
                        editable = "2"
                        event = KeyValEventEntity().apply {
                            action = "input"
                            paramKey = "name"
                        }

                    })
                    add(KeyValueEntity().apply {
                        key = "联系方式"
                        optional = "1"
                        showStatus = "1"
                        editable = "2"
                        event = KeyValEventEntity().apply {
                            action = "contact"
                            paramKey = "phone,wechat"
                        }

                    })
                    add(KeyValueEntity().apply {
                        key = "首次提供人"
                        optional = "1"
                        showStatus = "1"
                        editable = "2"
                        event = KeyValEventEntity().apply {
                            action = "recordUser"
                            paramKey = "record_user_id"
                        }

                    })
                    add(KeyValueEntity().apply {
                        key = "客户身份"
                        optional = "1"
                        showStatus = "1"
                        editable = "2"
                        event = KeyValEventEntity().apply {
                            action = "select"
                            paramKey = "identity"
                            options = mutableListOf<KeyValueEntity?>().apply {
                                add(KeyValueEntity().apply {
                                    key = "新娘"
                                    `val` = "1"
                                })
                                add(KeyValueEntity().apply {
                                    key = "新郎"
                                    `val` = "2"
                                })
                                add(KeyValueEntity().apply {
                                    key = "新人亲属"
                                    `val` = "5"
                                })
                                add(KeyValueEntity().apply {
                                    key = "其他"
                                    `val` = "7"
                                })
                            }
                        }

                    })
                }
            }else{
                val customerInfo = customerDetail(customerId)
                kvList.apply {
                    add(KeyValueEntity().apply {
                        key = "来源渠道"
                        `val` = customerInfo?.sourceChannel
                        optional = "1"
                        showStatus = "1"
                        editable = "2"
                        defaultVal = customerInfo?.sourceChannelId
                        event = KeyValEventEntity().apply {
                            action = "channe"
                            paramKey = "channel"
                        }

                    })
                    add(KeyValueEntity().apply {
                        key = "客户姓名"
                        optional = "1"
                        `val` = customerInfo?.name
                        defaultVal = customerInfo?.name
                        showStatus = "1"
                        editable = "2"
                        event = KeyValEventEntity().apply {
                            action = "input"
                            paramKey = "name"
                        }

                    })
                    add(KeyValueEntity().apply {
                        key = "联系方式"
                        optional = "1"
                        showStatus = "1"
                        editable = "2"
                        `val` = "${customerInfo?.phone},${customerInfo?.wechat}"
                        defaultVal = "${customerInfo?.phone},${customerInfo?.wechat}"
                        event = KeyValEventEntity().apply {
                            action = "contact"
                            paramKey = "phone,wechat"
                        }

                    })
                    add(KeyValueEntity().apply {
                        key = "首次提供人"
                        optional = "1"
                        showStatus = "1"
                        editable = "2"
                        `val` = customerInfo?.recordUser
                        defaultVal = customerInfo?.recordUserId
                        event = KeyValEventEntity().apply {
                            action = "recordUser"
                            paramKey = "record_user_id"
                        }

                    })
                    add(KeyValueEntity().apply {
                        key = "客户身份"
                        optional = "1"
                        showStatus = "1"
                        editable = "2"
                        `val` = customerInfo?.identity
                        defaultVal = customerInfo?.identityId
                        event = KeyValEventEntity().apply {
                            action = "select"
                            paramKey = "identity"
                            options = mutableListOf<KeyValueEntity?>().apply {
                                add(KeyValueEntity().apply {
                                    key = "新娘"
                                    `val` = "1"
                                })
                                add(KeyValueEntity().apply {
                                    key = "新郎"
                                    `val` = "2"
                                })
                                add(KeyValueEntity().apply {
                                    key = "新人亲属"
                                    `val` = "5"
                                })
                                add(KeyValueEntity().apply {
                                    key = "其他"
                                    `val` = "7"
                                })
                            }
                        }

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