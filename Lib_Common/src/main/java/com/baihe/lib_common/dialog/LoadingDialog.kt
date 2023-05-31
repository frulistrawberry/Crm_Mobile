package com.baihe.lib_common.dialog

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import com.baihe.lib_common.R
import com.baihe.lib_common.databinding.CommonDialogLoadingBinding
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.base.BaseDialog.AnimStyle.TOAST
import com.baihe.lib_framework.manager.AppManager

class LoadingDialog {
    class Builder(context: Context):BaseDialog.Builder<Builder>(context){
        private val mBinding:CommonDialogLoadingBinding = CommonDialogLoadingBinding.inflate(
            LayoutInflater.from(context)
        )
        init {
            setContentView(mBinding.root)
            setWidth(AppManager.getScreenWidthPx()/2)
            setHeight(AppManager.getScreenWidthPx()/2)
            setAnimStyle(TOAST)
            setThemeStyle(R.style.loading_dialog)
            (mBinding.loadingIv.drawable as AnimationDrawable).start()
        }
    }
}