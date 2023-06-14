package com.baihe.lib_framework.log

import com.google.gson.GsonBuilder
import com.safframework.log.converter.Converter
import java.lang.reflect.Type

class LogConverter:Converter {
    private val gson by lazy {
        GsonBuilder().setLenient()// json宽松
            .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
            .serializeNulls() //智能null
            .setPrettyPrinting()// 调教格式
            .disableHtmlEscaping() //默认是GSON把HTML 转义的
            .create()
    }

    override fun <T> fromJson(json: String, type: Type): T {
        return gson.fromJson(json,type)
    }

    override fun toJson(data: Any): String {
        return gson.toJson(data)
    }
}