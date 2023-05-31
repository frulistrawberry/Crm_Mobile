package com.baihe.lib_common.http.server

import com.baihe.http.config.IRequestServer

class ApiServer : IRequestServer{
    override fun getHost(): String {
        return "https://gcrmapi.hunli.baihe.com"
    }
}