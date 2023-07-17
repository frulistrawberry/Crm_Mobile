package com.baihe.lib_common.entity



class ButtonTypeEntity {
    companion object{
        const val ACTION_CALL = 1
        const val ACTION_DISPATCH_ORDER = 2
        const val ACTION_FOLLOW = 3
        const val ACTION_EDIT_OPPO = 4
        const val ACTION_TRANSFER_OPPO = 5
        const val ACTION_DELETE_OPPO = 6
        const val ACTION_SIGN = 7
        const val ACTION_CONFIRM_ARRIVAL = 8
        const val ACTION_SET_PEOPLE = 9
        const val ACTION_TRANSFER_ORDER = 10
        const val ACTION_EDIT_ORDER = 11
        const val ACTION_CHARGE_ORDER = 12
        const val ACTION_EDIT_CONTRACT = 13
    }
    var type = -1
    var name: String? = null
}