package com.baihe.lib_framework.base

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.baihe.lib_framework.R
import com.baihe.lib_framework.loading.CenterLoadingView
import com.baihe.lib_framework.toast.TipsToast
import com.baihe.lib_framework.utils.StatusBarSettingHelper
import com.baihe.lib_framework.widget.state.ktx.Decorative
import com.baihe.lib_framework.widget.state.ktx.LoadingState
import com.baihe.lib_framework.widget.state.ktx.LoadingStateDelegate
import com.dylanc.loadingstateview.OnReloadListener


/**
 * Activity 基类
 */
abstract class BaseActivity : AppCompatActivity(), LoadingState by LoadingStateDelegate(),
    OnReloadListener, Decorative {
    protected var TAG: String? = this::class.java.simpleName
    var loadingDialog:Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentLayout()
        initView(savedInstanceState)
        initListener()
        initData()
        StatusBarSettingHelper.setStatusBarTranslucent(this)
        StatusBarSettingHelper.statusBarLightMode(this)
    }

    /**
     * 设置布局
     */
    open fun setContentLayout() {
        setContentView(getLayoutResId())
        decorateContentView(this, this)
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

    open fun initListener(){

    }

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

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog?.dismiss()
    }


}