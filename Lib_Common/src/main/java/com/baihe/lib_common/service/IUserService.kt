package com.baihe.lib_common.service

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider
import com.baihe.lib_common.entity.UserEntity
import com.baihe.lib_framework.base.BaseDialog

interface IUserService :IProvider{
    /**
     * 保存用户信息
     * @param user 用户信息
     */
    fun saveUserInfo(user:UserEntity?)

    /**
     * 获取用户信息
     */
    fun getUserInfo():UserEntity?

    /**
     * 获取用户ID
     */
    fun getUserId():String?

    /**
     * 获取用户手机号
     */
    fun getPhoneNum():String?

    /**
     * 获取用户公司id
     */
    fun getCompanyId():String?

    /**
     * 获取用户公司名称
     */
    fun getCompanyName():String?

    /**
     * 获取公海选择弹窗
     */
    fun getBossSeaDialog(context: Context,block:(id:String,name:String)->Unit):BaseDialog

    /**
     * 清除与用户信息
     */
    fun clearUserInfo()

    /**
     * 用户协议
     */
    fun readAgreement(context: Context)

    /**
     * 隐私政策
     */
    fun readPolicy(context: Context)


}