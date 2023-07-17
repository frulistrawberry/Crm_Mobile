package com.baihe.lib_common.provider

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.baihe.lib_common.constant.RoutePath
import com.baihe.lib_common.service.IOrderService
import com.baihe.lib_common.service.IUserService

object OrderServiceProvider {
    @Autowired(name = RoutePath.ORDER_SERVICE_ORDER)
    lateinit var service: IOrderService

    init {
        ARouter.getInstance().inject(this)
    }

    /**
     * 订单列表
     */
    @JvmStatic
    fun toOrderList(context: Context) = service.toOrderList(context)

    /**
     * 订单详情
     * @param orderId 订单id
     */
    @JvmStatic
    fun toOrderDetail(context: Context,orderId:String) = service.toOrderDetail(context,orderId)
}