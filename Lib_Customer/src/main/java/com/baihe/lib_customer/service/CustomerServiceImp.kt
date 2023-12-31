package com.baihe.lib_customer.service

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.baihe.lib_common.constant.RoutePath.CUSTOMER_SERVICE_CUSTOMER
import com.baihe.lib_common.entity.CustomerDetailEntity
import com.baihe.lib_common.service.ICustomerService
import com.baihe.lib_customer.api.CustomerRepository
import com.baihe.lib_customer.ui.activity.AddOrUpdateCustomerActivity
import com.baihe.lib_customer.ui.activity.CustomerDetailActivity
import com.baihe.lib_customer.ui.activity.CustomerListActivity
import com.baihe.lib_customer.ui.activity.SelectCustomerActivity

@Route(path = CUSTOMER_SERVICE_CUSTOMER)
class CustomerServiceImp:ICustomerService {

    override fun toCustomerList(context: Context) {
        CustomerListActivity.start(context)
    }

    override fun toCustomerDetail(context: Context, id: String) {
        CustomerDetailActivity.start(context,id)
    }

    override fun createOrUpdateCustomer(context: Context,customerId:String?) {
        AddOrUpdateCustomerActivity.start(context,customerId)
    }

    override fun chooseCustomer(context: Context) {
        SelectCustomerActivity.start(context)
    }

    override suspend fun getCustomerInfo(
        customerId: String,
        lifecycleOwner: LifecycleOwner
    ): CustomerDetailEntity? {
        val repository = CustomerRepository(lifecycleOwner)
        return repository.customerDetail(customerId)
    }


}