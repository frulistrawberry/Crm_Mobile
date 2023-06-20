package com.baihe.lib_user.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.baihe.http.EasyConfig
import com.baihe.lib_common.constant.RoutePath.USER_SERVICE_USER
import com.baihe.lib_common.entity.UserEntity
import com.baihe.lib_common.service.IUserService
import com.baihe.lib_framework.storage.StorageManager
import com.baihe.lib_user.constant.CacheConstant

@Route(path = USER_SERVICE_USER)
class UserServiceImp:IUserService {
    override fun saveUserInfo(user: UserEntity?) {
        StorageManager.putObject(CacheConstant.USER_INFO_DATA,user)
        StorageManager.put(CacheConstant.USER_PHONE_NUMBER, user?.account)
        StorageManager.put(CacheConstant.USER_ID, user?.id.toString())
        StorageManager.put(CacheConstant.USER_COMPANY_ID, user?.company_id.toString())
        EasyConfig.getInstance().addParam("userId",user?.id.toString())
        EasyConfig.getInstance().addParam("companyId",user?.company_id.toString())
    }

    override fun getUserInfo(): UserEntity? {
        return StorageManager.getObject(CacheConstant.USER_INFO_DATA,UserEntity::class.java)
    }

    override fun getUserId(): String {
        return StorageManager.get(CacheConstant.USER_ID,"") as String
    }

    override fun getPhoneNum(): String? {
        return StorageManager.get(CacheConstant.USER_PHONE_NUMBER,"") as String?
    }

    override fun getCompanyId(): String {
        return  StorageManager.get(CacheConstant.USER_COMPANY_ID,"") as String
    }

    override fun clearUserInfo() {
        StorageManager.remove(CacheConstant.USER_INFO_DATA)
        StorageManager.remove(CacheConstant.USER_ID)
        StorageManager.remove(CacheConstant.USER_COMPANY_ID)
        EasyConfig.getInstance().removeParam("userId")
        EasyConfig.getInstance().removeParam("companyId")
    }

    override fun init(context: Context?) {

    }
}