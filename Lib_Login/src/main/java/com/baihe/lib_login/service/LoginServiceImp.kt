package com.baihe.lib_login.service

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.baihe.lib_common.constant.LOGIN_SERVICE_LOGIN
import com.baihe.lib_common.http.cookie.CookieJarImpl
import com.baihe.lib_common.http.exception.ApiException
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_common.push.PushHelper
import com.baihe.lib_common.service.ILoginService
import com.baihe.lib_framework.helper.AppHelper
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_login.api.LoginRepository
import com.baihe.lib_login.login.activity.LoginActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Route(path = LOGIN_SERVICE_LOGIN)
class LoginServiceImp:ILoginService {

    override fun login(context: Context) {
        LoginActivity.start(context)
    }

    override fun logout(
        context: Context,
        lifecycleOwner: LifecycleOwner?) {
        val scope = lifecycleOwner?.lifecycleScope ?: GlobalScope
        val loginRepository = LoginRepository(lifecycleOwner)
        scope.launch {
            try {
                loginRepository.logout()
                PushHelper.logoutAccount()
                UserServiceProvider.clearUserInfo()

                CookieJarImpl.getInstance().cookieStore.removeAll()
                login(AppHelper.getApplication())

            }catch (ex:ApiException){
                LogUtil.e(ex)
            }
        }
    }
}