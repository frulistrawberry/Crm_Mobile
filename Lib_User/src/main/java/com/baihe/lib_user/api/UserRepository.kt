package com.baihe.lib_user.api

import androidx.lifecycle.LifecycleOwner
import com.baihe.http.EasyHttp
import com.baihe.http.model.ResponseClass
import com.baihe.lib_common.http.BaseRepository
import com.baihe.lib_common.http.api.CommonApi
import com.baihe.lib_common.http.api.JsonParam
import com.baihe.lib_common.http.response.BaseResponse
import com.baihe.lib_user.BossSeaEntity
import com.baihe.lib_user.constant.UrlConstant

class UserRepository(lifecycleOwner: LifecycleOwner?):BaseRepository(lifecycleOwner) {
    suspend fun getBossSea():List<BossSeaEntity>?{
        return requestResponse {
            EasyHttp.get(lifecycleOwner)
                .api(CommonApi(UrlConstant.GET_BOSS_SEA,null))
                .execute(object : ResponseClass<BaseResponse<List<BossSeaEntity>>>() {})
        }
    }
}