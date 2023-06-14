package com.baihe.lib_common.http.log.server

import com.baihe.http.config.IRequestServer

class ApiServer : IRequestServer{
    override fun getHost(): String {
        return "http://gcrmapi.hunli.baihe.com"
    }
}