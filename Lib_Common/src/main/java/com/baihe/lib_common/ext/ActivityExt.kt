package com.baihe.lib_common.ext

import com.baihe.lib_common.dialog.LoadingDialog
import com.baihe.lib_framework.base.BaseActivity

/**
 * 显示通用加载弹窗
 */
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