package com.baihe.lib_common.ext

import com.baihe.lib_common.dialog.LoadingDialog
import com.baihe.lib_framework.base.BaseActivity
import com.baihe.lib_framework.base.BaseFragment

/**
 * 显示通用加载弹窗
 */
fun BaseFragment.showLoadingDialog(){
    if (loadingDialog == null)
        loadingDialog = LoadingDialog.Builder(requireContext()).create()
    if (loadingDialog?.isShowing == true) {
        loadingDialog?.dismiss()
    }
    loadingDialog?.show()
}

fun BaseFragment.dismissLoadingDialog(){
    loadingDialog?.let {
        if (it.isShowing) {
            it.dismiss()
        }
    }
}