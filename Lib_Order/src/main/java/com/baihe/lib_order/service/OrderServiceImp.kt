package com.baihe.lib_order.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.baihe.lib_common.constant.RoutePath
import com.baihe.lib_common.service.IOrderService
import com.baihe.lib_order.ui.activity.OrderDetailActivity
import com.baihe.lib_order.ui.activity.OrderListActivity

@Route(path = RoutePath.ORDER_SERVICE_ORDER)
class OrderServiceImp:IOrderService {


    override fun toOrderList(context: Context) {
        OrderListActivity.start(context)
    }

    override fun toOrderDetail(context: Context, orderId: String) {
        OrderDetailActivity.start(context,orderId)
    }
}