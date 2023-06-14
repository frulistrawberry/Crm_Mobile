package com.baihe.lib_framework.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.baihe.lib_framework.ext.saveAs
import com.baihe.lib_framework.ext.saveAsUnChecked
import java.lang.reflect.ParameterizedType

/**
 * dataBinding Fragment 基类
 */
abstract class BaseViewBindFragment<VB : ViewBinding>() : BaseFragment(){
    var mBinding: VB? = null


    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        val type = javaClass.genericSuperclass
        val vbClass: Class<VB> = type!!.saveAs<ParameterizedType>().actualTypeArguments[0].saveAs()
        val method = vbClass.getDeclaredMethod("inflate",LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        mBinding = method.invoke(this, inflater,container,false)!!.saveAsUnChecked()
        return mBinding!!.root.decorate(this, this)
    }

    override fun getLayoutResId(): Int = -1

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }




}