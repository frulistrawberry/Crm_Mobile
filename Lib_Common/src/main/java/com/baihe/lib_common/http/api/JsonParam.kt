package com.baihe.lib_common.http.api

import android.text.TextUtils
import com.google.gson.GsonBuilder

class JsonParam()  {
    private val params: MutableMap<String, Any?> = mutableMapOf()
    private val gson by lazy {
        GsonBuilder().setLenient()// json宽松
            .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
            .serializeNulls() //智能null
            .setPrettyPrinting()// 调教格式
            .disableHtmlEscaping() //默认是GSON把HTML 转义的
            .create()
    }


    companion object {
        fun newInstance():JsonParam{
            return JsonParam()
        }
    }


    fun putParamValue(key: String?, value: Any?): JsonParam {
        if(!key.isNullOrEmpty() && value!=null){
            params[key]=value
        }
        return this
    }

    fun putParamValue(paramValues: LinkedHashMap<String, Any?>?): JsonParam {
        if (!paramValues.isNullOrEmpty()) {
            params.putAll(paramValues)
        }
        return this
    }
    fun getParamValue(): String? {
        return gson.toJson(params)
    }
}