package com.baihe.lib_common.ext

import com.baihe.lib_common.ui.dialog.LoadingDialog
import com.baihe.lib_framework.base.BaseActivity
import com.baihe.lib_framework.base.BaseFragment
import com.baihe.lib_framework.log.LogUtil

/**
 * 显示通用加载弹窗
 */
object ActivityExt{
    fun BaseActivity.showLoadingDialog(){
        if (loadingDialog == null)
            loadingDialog = LoadingDialog.Builder(this).create()
        if (loadingDialog?.isShowing == true) {
            loadingDialog?.dismiss()
        }
        if (isFinishing) {
            return
        }
        loadingDialog?.show()
    }

    fun BaseActivity.dismissLoadingDialog(){
        if (isFinishing) {
            return
        }
        loadingDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    fun BaseActivity.printBackStack(){
        val backStackCount = supportFragmentManager.backStackEntryCount
        for (i in 0 until backStackCount) {
            val backStackEntry = supportFragmentManager.getBackStackEntryAt(i)
            val fragmentName = backStackEntry.name
            LogUtil.d("BackStackInfo", "Fragment $i: $fragmentName")
        }

    }

}
