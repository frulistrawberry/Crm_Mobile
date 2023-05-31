package com.baihe.lib_common.http

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import com.baihe.http.config.IRequestHandler
import com.baihe.http.exception.HttpException
import com.baihe.http.request.HttpRequest
import com.baihe.lib_common.http.exception.ApiException
import com.baihe.lib_common.http.exception.ExceptionHandler
import com.baihe.lib_common.http.response.BaseResponse
import okhttp3.Response
import java.lang.reflect.Type

class RequestHandler :IRequestHandler {
    override fun requestSuccess(httpRequest: HttpRequest<*>, response: Response, type: Type): Any {

        if (Response::class.java == type){
            return response
        }
        if (response.isSuccessful){
            val body = response.body
                ?: throw NullPointerException("Response from " + httpRequest.generateLogTag() +
                        " was null but response body type was declared as non-null")
            val text = body.toString()

            val result:Any =
                try {
                    JSON.parseObject(text,type) as Any
                }catch (e:JSONException){
                    throw e
                }

            if (result is BaseResponse<*>){
                if (!result.isSuccess()){
                    throw ApiException(result.code,result.msg)
                }
            }
            return result
        }else{
            throw HttpException(response)
        }


    }

    override fun requestFail(httpRequest: HttpRequest<*>, e: Exception): Exception {

        return ExceptionHandler.handleException(e)
    }
}