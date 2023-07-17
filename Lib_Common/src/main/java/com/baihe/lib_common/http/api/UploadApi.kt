package com.baihe.lib_common.http.api

import com.baihe.http.config.IRequestApi
import com.baihe.http.config.IRequestType
import com.baihe.http.model.BodyType
import com.baihe.lib_common.constant.UrlConstant
import java.io.File

class UploadApi(val file:File):IRequestApi, IRequestType {

    override fun getApi(): String {
        return UrlConstant.UPLOAD
    }

    override fun getBodyType(): BodyType {
        return BodyType.FORM
    }



}