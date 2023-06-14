package com.baihe.lib_framework.storage

import android.content.Context

interface StorageStrategy {
    fun init(context: Context,name: String? = null,isDebug:Boolean)

    fun put(key:String,value: Any?)

//    fun putString(key: String,value:String?)
//
//    fun getString(key: String,defValue: String?)

    fun get(key: String,defValue: Any?):Any?

    fun <T>putObject(key: String,value: T)

    fun <T>getObject(key: String,value: Class<T>):T?

    fun remove(key: String)

    fun getAll():Map<String,*>

    fun clear()


}