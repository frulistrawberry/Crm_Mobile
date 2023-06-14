package com.baihe.lib_framework.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * @author mingyan.su
 * @date   2023/2/27 12:31
 * @desc   DataBinding和ViewModel基类
 */
abstract class BaseMvvmFragment<VB : ViewBinding, VM : ViewModel> : BaseViewBindFragment<VB>() {
    lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }



    open fun initViewModel() {
        val argument = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        mViewModel = ViewModelProvider(this).get(argument[1] as Class<VM>)
    }
}