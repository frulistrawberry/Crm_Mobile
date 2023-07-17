package com.baihe.lib_common.constant

object StatusConstant {
    //===================机会状态=====================//
    /**
     * 待邀约
     */
    const val OPPO_TO_BE_INVITED = "200"
    /**
     * 客户无效
     */
    const val OPPO_INVALID_CUSTOMER = "210"
    /**
     * 客户待定
     */
    const val OPPO_CUSTOMER_TBD = "220"
    /**
     * 客户有效
     */
    const val OPPO_CUSTOMER_EFFECTIVE = "230"
    /**
     * 邀约成功
     */
    const val OPPO_INVITATION_SUCCESSFUL = "240"
    /**
     * 已进店
     */
    const val OPPO_ENTERED_STORE = "250"
    /**
     * 已删除
     */
    const val OPPO_DELETED = "260"
    //===================订单状态=====================//
    /**
     * 未生成订单
     */
    const val ORDER_NONE = "0"
    /**
     * 待签约
     */
    const val ORDER_TO_BE_SIGNED = "1"
    /**
     * 已签约
     */
    const val ORDER_SIGNED = "2"
    /**
     * 已退单
     */
    const val ORDER_CHARGED = "3"
    /**
     * 已取消
     */
    const val ORDER_CANCELED = "4"
    /**
     * 已完成
     */
    const val ORDER_COMPLETED = "6"
    //===================订单跟进状态=====================//
    /**
     * 待邀约&待回访
     */
    const val ORDER_PHASE_TO_BE_INVITED = "300"
    /**
     * 客户待定
     */
    const val ORDER_PHASE_CUSTOMER_TBD = "310"
    /**
     * 客户有效
     */
    const val ORDER_PHASE_CUSTOMER_EFFECTIVE = "330"
    /**
     * 待进店&邀约成功
     */
    const val ORDER_PHASE_STORE_TO_BE_ENTERED = "340"
    /**
     * 已进店
     */
    const val ORDER_PHASE_ENTERED_STORE = "350"
    /**
     * 客户无效
     */
    const val ORDER_PHASE_INVALID_CUSTOMER = "360"
    /**
     * 有效到店
     */
    const val ORDER_PHASE_EFFECTIVE_ARRIVAL = "370"
    /**
     * 无效到店
     */
    const val ORDER_PHASE_INVALID_STORE_ARRIVAL = "380"
}