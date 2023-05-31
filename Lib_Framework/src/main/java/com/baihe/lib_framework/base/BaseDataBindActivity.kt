package com.baihe.lib_framework.base

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.baihe.lib_framework.ext.saveAs
import com.baihe.lib_framework.ext.saveAsUnChecked
import java.lang.reflect.ParameterizedType

/**
 * dataBinding Activity基类
 */
abstract class BaseDataBindActivity<DB : ViewBinding> : BaseActivity() {
    lateinit var mBinding: DB

    override fun setContentLayout() {
        val type = javaClass.genericSuperclass
        val vbClass: Class<DB> = type!!.saveAs<ParameterizedType>().actualTypeArguments[0].saveAs()
        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        mBinding = method.invoke(this, layoutInflater)!!.saveAsUnChecked()
        setContentView(mBinding.root)
    }
}