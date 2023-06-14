package com.baihe.lib_framework.storage

import android.content.Context
import android.util.Log
import com.baihe.lib_framework.log.LogUtil
import com.google.gson.GsonBuilder
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel

class MMKVStrategy : StorageStrategy{
    private lateinit var mmkv:MMKV
    private val gson by lazy {
        GsonBuilder().setLenient()// json宽松
            .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
            .serializeNulls() //智能null
            .setPrettyPrinting()// 调教格式
            .disableHtmlEscaping() //默认是GSON把HTML 转义的
            .create()
    }
    override fun init(context: Context, name: String?,isDebug:Boolean) {
        MMKV.initialize(context)
        if (isDebug){
            MMKV.setLogLevel(MMKVLogLevel.LevelDebug)
        }else{
            MMKV.setLogLevel(MMKVLogLevel.LevelError)

        }
        mmkv = if (name.isNullOrEmpty()){
            MMKV.defaultMMKV()
        } else{
            MMKV.mmkvWithID(name)
        }
    }

    override fun put(key: String, value: Any?) {
        when(value){
            is Int ->{
                mmkv.encode(key, value).let {
                    LogUtil.d("MMKV","MMKV-encode:$value")
                }
                return
            }
            is Float -> {
                mmkv.encode(key, value).let {
                    LogUtil.d("MMKV","MMKV-encode:$value")
                }
                return
            }
            is Boolean ->{
                mmkv.encode(key, value).let {
                    LogUtil.d("MMKV","MMKV-encode:$value")
                }
                return
            }
            is Long ->{
                mmkv.encode(key, value).let {
                    LogUtil.d("MMKV","MMKV-encode:$value")
                }
                return
            }
            is ByteArray ->{
                mmkv.encode(key, value).let {
                    LogUtil.d("MMKV","MMKV-encode:$value")
                }
                return
            }
            is Double ->{
                mmkv.encode(key, value).let {
                    LogUtil.d("MMKV","MMKV-encode:$value")
                }
                return
            }
            is String ->{
                mmkv.encode(key, value).let {
                    LogUtil.d("MMKV","MMKV-encode:$value")
                }
                return
            }

        }
    }

    override fun get(key: String, defValue: Any?):Any? {
        when(defValue){
            is Int ->{
                return mmkv.decodeInt(key,defValue).also {
                    LogUtil.d("MMKV","MMKV-decode:$it")
                }
            }
            is Float -> {
                return mmkv.decodeFloat(key,defValue).also {
                    LogUtil.d("MMKV","MMKV-decode:$it")
                }
            }
            is Boolean ->{
                return mmkv.decodeBool(key,defValue).also {
                    LogUtil.d("MMKV","MMKV-decode:$it")
                }
            }
            is Long ->{
                return mmkv.decodeLong(key,defValue).also {
                    LogUtil.d("MMKV","MMKV-decode:$it")
                }
            }
            is ByteArray ->{
                return mmkv.decodeBytes(key,defValue).also {
                    LogUtil.d("MMKV","MMKV-decode:$it")
                }
            }
            is Double ->{
                return mmkv.decodeDouble(key,defValue).also {
                    LogUtil.d("MMKV","MMKV-decode:$it")
                }
            }
            is String ->{
                return mmkv.decodeString(key,defValue).also {
                    LogUtil.d("MMKV","MMKV-decode:$it")
                }
            }

        }
        return null
    }

    override fun <T> putObject(key: String, value: T) {
        val json = gson.toJson(value)
        mmkv.encode(key,json)
        LogUtil.d("MMKV",json)

    }

    override fun <T> getObject(key: String, clazz: Class<T>): T? {
        val json = mmkv.decodeString(key)
        return if (json.isNullOrEmpty())
            null
        else{
            gson.fromJson(json,clazz).also {
                LogUtil.json("MMKV",it)
            }
        }
    }

    override fun remove(key: String) {
        mmkv.remove(key)
    }

    override fun getAll(): Map<String, *> {

        return mmkv.all
    }

    override fun clear() {
        mmkv.clearAll()
    }

}