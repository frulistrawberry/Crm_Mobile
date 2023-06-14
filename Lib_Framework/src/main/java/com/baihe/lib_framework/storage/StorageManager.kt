package com.baihe.lib_framework.storage

import android.content.Context

object StorageManager {
    private var mStrategy:StorageStrategy? = null

    @JvmStatic
    fun init(context: Context,name:String? = null,isDebug:Boolean,strategy: StorageStrategy = MMKVStrategy()){
        mStrategy = strategy
        mStrategy?.init(context,name,isDebug)
    }

    fun put(key:String,value:Any?){
        mStrategy?.put(key,value)
    }

    fun get(key: String,value: Any?):Any?{
        return mStrategy?.get(key,value)
    }

    fun putObject(key: String,value: Any?){
        mStrategy?.putObject(key,value)
    }

    fun <T>getObject(key: String,clazz: Class<T>):T?{
        return mStrategy?.getObject(key,clazz)
    }

    fun remove(key: String){
        mStrategy?.remove(key)
    }

    fun getAll(){
        mStrategy?.getAll()
    }

    fun clear(){
        mStrategy?.clear()
    }
}