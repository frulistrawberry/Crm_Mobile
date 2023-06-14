package com.baihe.lib_common.http.exception

import android.net.ParseException
import com.baihe.http.exception.HttpException
import com.baihe.lib_common.http.cookie.CookieJarImpl
import com.baihe.lib_common.provider.LoginServiceProvider
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_common.push.PushHelper
import com.baihe.lib_framework.helper.AppHelper
import com.baihe.lib_framework.toast.TipsToast
import java.net.ConnectException

/**
 * 统一错误处理工具类
 */
object ExceptionHandler {

    fun handleException(e: Throwable): ApiException {
        val ex: ApiException
        when (e) {
            is ApiException -> {
                ex = ApiException(e.errCode, e.errMsg, e)
                if (ex.errCode == ERROR.UNLOGIN.code){
                    //退出登录
                    UserServiceProvider.clearUserInfo()
                    PushHelper.logoutAccount()
                    CookieJarImpl.getInstance().cookieStore.removeAll()
                    LoginServiceProvider.login(AppHelper.getApplication())
                }
            }
            is NoNetWorkException -> {
                ex = ApiException(ERROR.NETWORD_ERROR, e)
            }
            is HttpException -> {
                ex = when (e.code()) {
                    ERROR.UNAUTHORIZED.code -> ApiException(ERROR.UNAUTHORIZED, e)
                    ERROR.FORBIDDEN.code -> ApiException(ERROR.FORBIDDEN, e)
                    ERROR.NOT_FOUND.code -> ApiException(ERROR.NOT_FOUND, e)
                    ERROR.REQUEST_TIMEOUT.code -> ApiException(ERROR.REQUEST_TIMEOUT, e)
                    ERROR.GATEWAY_TIMEOUT.code -> ApiException(ERROR.GATEWAY_TIMEOUT, e)
                    ERROR.INTERNAL_SERVER_ERROR.code -> ApiException(ERROR.INTERNAL_SERVER_ERROR, e)
                    ERROR.BAD_GATEWAY.code -> ApiException(ERROR.BAD_GATEWAY, e)
                    ERROR.SERVICE_UNAVAILABLE.code -> ApiException(ERROR.SERVICE_UNAVAILABLE, e)
                    else -> ApiException(e.code(), e.message(), e)
                }
            }
            is ParseException -> {
                ex = ApiException(ERROR.PARSE_ERROR, e)
            }
            is ConnectException -> {
                ex = ApiException(ERROR.NETWORD_ERROR, e)
            }
            is javax.net.ssl.SSLException -> {
                ex = ApiException(ERROR.SSL_ERROR, e)
            }
            is java.net.SocketException -> {
                ex = ApiException(ERROR.TIMEOUT_ERROR, e)
            }
            is java.net.SocketTimeoutException -> {
                ex = ApiException(ERROR.TIMEOUT_ERROR, e)
            }
            is java.net.UnknownHostException -> {
                ex = ApiException(ERROR.UNKNOW_HOST, e)
            }
            else -> {
                ex = if (!e.message.isNullOrEmpty()) ApiException(1000, e.message!!, e)
                else ApiException(ERROR.UNKNOWN, e)
            }
        }
        TipsToast.showTips(ex.errMsg)
        return ex
    }
}
