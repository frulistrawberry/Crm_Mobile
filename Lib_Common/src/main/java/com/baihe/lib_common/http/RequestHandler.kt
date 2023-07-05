package com.baihe.lib_common.http

import com.baihe.http.EasyLog
import com.baihe.http.config.IRequestHandler
import com.baihe.http.exception.HttpException
import com.baihe.http.request.HttpRequest
import com.baihe.lib_common.http.exception.ApiException
import com.baihe.lib_common.http.exception.ERROR
import com.baihe.lib_common.http.exception.ExceptionHandler
import com.baihe.lib_common.http.response.BaseResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import okhttp3.Response
import java.lang.reflect.Type

class RequestHandler :IRequestHandler {
    private val gson by lazy {
        GsonBuilder().setLenient()// json宽松
            .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
            .serializeNulls() //智能null
            .setPrettyPrinting()// 调教格式
            .disableHtmlEscaping() //默认是GSON把HTML 转义的
            .create()
    }

    override fun requestSuccess(httpRequest: HttpRequest<*>, response: Response, type: Type): Any {

        if (Response::class.java == type){
            return response
        }
        if (response.isSuccessful){
            val body = response.body
                ?: throw ApiException(ERROR.UNKNOWN)
            val text = body.string()
            EasyLog.printKeyValue(httpRequest, "RequestUrl", response.request.url.toString())
            EasyLog.printKeyValue(httpRequest, "RequestMethod", httpRequest.requestMethod)
            EasyLog.printLine(httpRequest)
            EasyLog.printJson(httpRequest,text)
            EasyLog.printLine(httpRequest)
            val result:Any =
                try {
                    val responseJSONObject = JsonParser.parseString(text).asJsonObject
                    val code = responseJSONObject.get("code").asInt
                    val message = responseJSONObject.get("msg").asString
                    if (code != 200){
                        BaseResponse<String>(code,message,null)
                    }else{
                        gson.fromJson(text,type)
                    }
                }catch (e: Exception){
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