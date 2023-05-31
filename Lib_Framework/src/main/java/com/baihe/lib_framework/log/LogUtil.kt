package com.baihe.lib_framework.log

import android.app.Application
import android.nfc.Tag
import android.util.Log
import com.safframework.log.L
import com.safframework.log.LogLevel
import com.safframework.log.configL
import com.safframework.log.converter.fastjson.FastJSONConverter
import com.safframework.log.printer.Printer

/**
 *  API is the same to {@link android.util.Log}
 */
object LogUtil {
    private const val TAG = "LogUtil"
    private var isDebug = false



    @JvmStatic
    @JvmOverloads
    fun init( isDebug: Boolean = false,globalTag: String = TAG,header:String? = null) {
        configL {
            tag = globalTag
            this.header = header
            converter = FastJSONConverter()
        }
    }

    @JvmStatic
    fun e(throwable: Throwable?){
        if (!isDebug)
            return
        L.e(throwable)
    }

    @JvmStatic
    fun e(tag: String?, throwable: Throwable?){
        if (!isDebug)
            return
        L.e(tag,throwable)
    }

    @JvmStatic
    fun e(msg: String?){
        if (!isDebug)
            return
        L.e(msg)
    }

    @JvmStatic
    fun e(tag: String?, msg: String?){
        if (!isDebug)
            return
        L.e(tag,msg)
    }

    @JvmStatic
    fun w(msg: String?){
        if (!isDebug)
            return
        L.w(msg)
    }

    @JvmStatic
    fun w(tag: String?, msg: String?){
        if (!isDebug)
            return
        L.w(tag,msg)
    }

    @JvmStatic
    fun i(msg: String?){
        if (!isDebug)
            return
        L.i(msg)
    }

    @JvmStatic
    fun i(tag: String?, msg: String?){
        if (!isDebug)
            return
        L.i(tag,msg)
    }

    @JvmStatic
    fun d(msg: String?) {
        if (!isDebug)
            return
        L.d(msg)
    }

    @JvmStatic
    fun d(tag: String?, msg: String?){
        if (!isDebug)
            return
        L.d(tag,msg)
    }

    @JvmStatic
    fun print(logLevel: LogLevel, tag: String?, msg: String?,  printer: Printer){
        if (!isDebug)
            return
        L.print(logLevel,tag,msg,printer)
    }










}
