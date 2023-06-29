package com.baihe.lib_common.provider

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.baihe.lib_common.constant.RoutePath.USER_SERVICE_USER
import com.baihe.lib_common.entity.UserEntity
import com.baihe.lib_common.service.IUserService

object UserServiceProvider {
    @Autowired(name = USER_SERVICE_USER)
    lateinit var userService: IUserService

    init {
        ARouter.getInstance().inject(this)
    }

    @JvmStatic
    fun getUserId() = userService.getUserId()

    @JvmStatic
    fun getCompanyId() = userService.getCompanyId()

    @JvmStatic
    fun getCompanyName() = userService.getCompanyName()

    @JvmStatic
    fun getPhoneNum() = userService.getPhoneNum()

    @JvmStatic
    fun getBossSeaDialog(context:Context,block:(id:String,name:String)->Unit) = userService.getBossSeaDialog(context,block)


    @JvmStatic
    fun saveUser(user:UserEntity?) = userService.saveUserInfo(user)

    @JvmStatic
    fun getUser() = userService.getUserInfo()


    @JvmStatic
    fun clearUserInfo(){
        userService.clearUserInfo()
    }
}