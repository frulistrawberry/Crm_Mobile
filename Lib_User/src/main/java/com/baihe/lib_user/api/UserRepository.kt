package com.baihe.lib_user.api

import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import com.baihe.http.EasyHttp
import com.baihe.http.model.ResponseClass
import com.baihe.lib_common.http.BaseRepository
import com.baihe.lib_common.http.api.CommonApi
import com.baihe.lib_common.http.api.JsonParam
import com.baihe.lib_common.http.response.BaseResponse
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_user.BossSeaEntity
import com.baihe.lib_user.VersionEntity
import com.baihe.lib_user.constant.UrlConstant

class UserRepository(lifecycleOwner: LifecycleOwner) : BaseRepository(lifecycleOwner) {
    suspend fun getBossSea(): List<BossSeaEntity>? {
        return requestResponse {
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.GET_BOSS_SEA, null))
                .execute(object : ResponseClass<BaseResponse<List<BossSeaEntity>>>() {})
        }
    }

    suspend fun submitPassword(
        userId: String?,
        oldPas: String,
        newPas: String,
        confirmPas: String
    ): Int? {
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("userId", userId)
                .putParamValue("oldPassWord", oldPas)
                .putParamValue("newPassWord", newPas)
                .putParamValue("repeatPassWord", confirmPas)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.SUBMIT_PASSWORD, params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<Int>>() {})
        }
    }

    suspend fun checkAppVersion(currentVersion: String): VersionEntity? {
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("channel", currentVersion)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.CHECK_VERSION, params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<VersionEntity>>() {})
        }
    }

    suspend fun setPushStatus(pushSwitch: Int): Int? {
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("status", pushSwitch)
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.SET_PUSH, params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<Int>>() {})
        }
    }

    suspend fun deleteAccount(): List<String>? {
        return requestResponse {
            val account = UserServiceProvider.getPhoneNum()
            var params: JsonParam? = null
            if (account?.isNotEmpty() == true) {
                params = JsonParam.newInstance()
                    .putParamValue("account", account)
            }
            EasyHttp.post(lifecycleOwner)
                .api(CommonApi(UrlConstant.DELETE_ACCOUNT, params?.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<List<String>>>() {})
        }
    }
}