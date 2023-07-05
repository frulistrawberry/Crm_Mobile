package com.baihe.lib_user.api

import androidx.lifecycle.LifecycleOwner
import com.baihe.http.EasyHttp
import com.baihe.http.model.ResponseClass
import com.baihe.lib_common.http.BaseRepository
import com.baihe.lib_common.http.api.CommonApi
import com.baihe.lib_common.http.api.JsonParam
import com.baihe.lib_common.http.response.BaseResponse
import com.baihe.lib_common.http.response.Data
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValEventEntity
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_user.BossSeaEntity
import com.baihe.lib_user.constant.UrlConstant

class UserRepository(lifecycleOwner: LifecycleOwner?) : BaseRepository(lifecycleOwner) {
    suspend fun getBossSea(): List<BossSeaEntity>? {
        return requestResponse {
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.GET_BOSS_SEA, null))
                .execute(object : ResponseClass<BaseResponse<List<BossSeaEntity>>>() {})
        }
    }

    suspend fun submitPassword(oldPas: String, newPas: String, confirmPas: String): Int? {
        return requestResponse {
            val params = JsonParam.newInstance()
                .putParamValue("oldPas", oldPas)
                .putParamValue("newPas", newPas)
                .putParamValue("confirmPas", confirmPas)
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.SUBMIT_PASSWORD, params.getParamValue()))
                .execute(object : ResponseClass<BaseResponse<Int>>() {})
        }
    }
}