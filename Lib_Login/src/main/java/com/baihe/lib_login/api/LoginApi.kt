package com.baihe.lib_login.api

import com.baihe.http.annotation.HttpIgnore
import com.baihe.lib_common.http.api.BaseApi
import com.baihe.lib_login.constant.UrlConstant

/**
 * 登录接口
 * @param userName 手机号
 * @param password 密码
 * @param verifyCode 验证码
 */
data class LoginApi(
    @HttpIgnore
    val userName:String,
    @HttpIgnore
    val password:String?,
    @HttpIgnore
    val verifyCode:String?

):BaseApi() {
    override fun getApi(): String {
        return UrlConstant.LOGIN
    }
}

/**
 * 获取验证码接口
 * @param mobile 手机号
 * @param verifyType 验证码类型:1-短信验证码,2-语音验证码
 */
data class SendVerifyCodeApi(
    @HttpIgnore
    val mobile:String,
    @HttpIgnore
    val verifyType:Int? = 1,

):BaseApi() {
    override fun getApi(): String {
        return UrlConstant.SEND_VERIFY_CODE
    }
}

/**
 * 退出登录接口
 */
class LogoutApi:BaseApi() {
    override fun getApi(): String {
        return UrlConstant.SEND_VERIFY_CODE
    }
}
