package com.baihe.lib_login.api

import androidx.lifecycle.LifecycleOwner
import com.baihe.http.EasyHttp
import com.baihe.http.model.ResponseClass
import com.baihe.lib_common.entity.UserEntity
import com.baihe.lib_common.http.BaseRepository
import com.baihe.lib_common.http.api.CommonApi
import com.baihe.lib_common.http.api.JsonParam
import com.baihe.lib_common.http.response.BaseResponse
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_framework.storage.StorageManager
import com.baihe.lib_login.constant.UrlConstant
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow

class LoginRepository(lifecycleOwner: LifecycleOwner):BaseRepository(lifecycleOwner) {

    /**
     * 登录
     * @param userName 用户名
     * @param passWord 密码
     * @param verifyCode 验证码
     */
    private suspend fun login(userName:String,passWord:String?=null,verifyCode:String?=null):UserEntity?{
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("userName",userName)
                .putParamValue("passWord",passWord)
                .putParamValue("verifyCode",verifyCode)

            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.LOGIN,params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<UserEntity>>() {})
        }
    }

    suspend fun passWordLogin(userName: String,passWord: String):UserEntity?{
        var result:UserEntity? = null
        flow {
            emit(login(userName = userName,passWord = passWord))
        }.flatMapConcat {
            flow {
                UserServiceProvider.saveUser(it)
                val needContract = UserServiceProvider.getCompanyConfig(lifecycleOwner)?.is_contract
                UserServiceProvider.saveCompanyContractConfig("1"==needContract)
                emit(it)
            }
        }.collect {
            result = it
        }


        return result
    }

    suspend fun verifyCodeLogin(userName: String,verifyCode: String):UserEntity?{
        var result:UserEntity? = null
        flow {
            emit(login(userName = userName,verifyCode = verifyCode))
        }.flatMapConcat {
            flow {
                UserServiceProvider.saveUser(it)
                val needContract = UserServiceProvider.getCompanyConfig(lifecycleOwner)?.is_contract
                UserServiceProvider.saveCompanyContractConfig("1"==needContract)
                emit(it)
            }
        }.collect {
            result = it
        }
        return result

    }


    /**
     * 获取验证码
     * @param mobile 手机号
     * @param verifyType 验证码类型 1：短信验证码 2：语音换证吗
     */
    suspend fun sendVerifyCode(mobile:String,verifyType:Int? = 1):Int?{
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("mobile",mobile)
                .putParamValue("verifyType",verifyType)
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.SEND_VERIFY_CODE,params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<Int>>(){})
        }
    }

    /**
     * 退出登录
     */
    suspend fun logout():String?{
        return requestResponse {
            EasyHttp.post(lifecycleOwner).api(CommonApi(UrlConstant.LOGOUT))
                .execute(object : ResponseClass<BaseResponse<String>>(){})
        }
    }

}