package com.baihe.lib_framework.base

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.baihe.lib_framework.R
import com.baihe.lib_framework.loading.CenterLoadingView
import com.baihe.lib_framework.toast.TipsToast


/**
 * Activity 基类
 */
abstract class BaseActivity : AppCompatActivity(){
    protected var TAG: String? = this::class.java.simpleName
    var loadingDialog:Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout()
        initView(savedInstanceState)
        initData()
    }

    /**
     * 设置布局
     */
    open fun setContentLayout() {
        setContentView(getLayoutResId())
    }

    /**
     * 初始化视图
     * @return Int 布局id
     * 仅用于不继承BaseDataBindActivity类的传递布局文件
     */
    abstract fun getLayoutResId(): Int

    /**
     * 初始化布局
     * @param savedInstanceState Bundle?
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     */
    open fun initData() {

    }



    /**
     * Toast
     * @param msg Toast内容
     */
    fun showToast(msg: String) {
        TipsToast.showTips(msg)
    }

    /**
     * Toast
     * @param resId 字符串id
     */
    fun showToast(@StringRes resId: Int) {
        TipsToast.showTips(resId)
    }

    fun showDefLoadingDialog(){
        if (loadingDialog == null){
            loadingDialog = CenterLoadingView(this, R.style.loading_dialog)
        }
        if (loadingDialog?.isShowing == true) {
            loadingDialog?.dismiss()
        }
        loadingDialog?.setTitle("加载中...")
        if (isFinishing) {
            return
        }
        loadingDialog?.show()
    }

    fun dismissDefLoadingDialog(){
        if (isFinishing) {
            return
        }
        loadingDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }


}