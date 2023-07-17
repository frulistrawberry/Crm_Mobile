package com.baihe.lib_common.provider

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.baihe.lib_common.constant.RoutePath.CONTRACT_SERVICE_CONTRACT
import com.baihe.lib_common.constant.RoutePath.CUSTOMER_SERVICE_CUSTOMER
import com.baihe.lib_common.service.IContractService
import com.baihe.lib_common.service.ICustomerService

object ContractServiceProvider {
    @Autowired(name = CONTRACT_SERVICE_CONTRACT)
    lateinit var customerService:IContractService

    init {
        ARouter.getInstance().inject(this)
    }


    /**
     * 合同列表页
     */
    @JvmStatic
    fun toCustomerList(context: Context) = customerService.toContractList(context)

    /**
     * 合同详情页
     * @param contractId 合同Id
     */
    @JvmStatic
    fun toCustomerDetail(context: Context,contractId:String) = customerService.toContractDetail(context,contractId)

}