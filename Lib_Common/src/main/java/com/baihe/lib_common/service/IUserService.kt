package com.baihe.lib_common.service

import com.alibaba.android.arouter.facade.template.IProvider
import com.baihe.lib_common.entity.UserEntity

interface IUserService :IProvider{
    /**
     * 保存用户信息
     * @param user 用户信息
     */
    fun saveUserInfo(user:UserEntity)

    /**
     * 获取用户ID
     */
    fun getUserId():String

    /**
     * 获取用户手机号
     */
    fun getPhoneNum():String

    /**
     * 清除与用户信息
     */
    fun clearUserInfo()
}