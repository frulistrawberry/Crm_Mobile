package com.baihe.lib_common.provider

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.baihe.lib_common.constant.RoutePath.CUSTOMER_SERVICE_CUSTOMER
import com.baihe.lib_common.service.ICustomerService

object CustomerServiceProvider {
    @Autowired(name = CUSTOMER_SERVICE_CUSTOMER)
    lateinit var customerService:ICustomerService

    init {
        ARouter.getInstance().inject(this)
    }


    /**
     * 客户列表
     */
    @JvmStatic
    fun toCustomerList(context: Context) = customerService.toCustomerList(context)

    /**
     * 客户详情
     * @param id 客户Id
     */
    @JvmStatic
    fun toCustomerDetail(context: Context,id:String) = customerService.toCustomerDetail(context,id)

    /**
     * 创建客户
     */
    @JvmStatic
    fun createOrUpdateCustomer(context: Context,customerId:String?=null) = customerService.createOrUpdateCustomer(context,customerId)

    @JvmStatic
    fun chooseCustomer(context: Context) = customerService.chooseCustomer(context)

    @JvmStatic
    suspend fun getCustomerInfo(customerId: String,lifecycleOwner: LifecycleOwner) = customerService.getCustomerInfo(customerId,lifecycleOwner)
}