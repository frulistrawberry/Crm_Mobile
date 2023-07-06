package com.baihe.lib_user.dialog

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.manager.AppManager
import com.baihe.lib_user.databinding.UserDialogSettingTipBinding


class BaseUserTipDialog {
    class Builder(context: Context) : BaseDialog.Builder<Builder>(context) {
        private val mBinding = UserDialogSettingTipBinding.inflate(
            LayoutInflater.from(context)
        )
        private var confirmClick: OnConfirmClick? = null

        init {
            setContentView(mBinding.root)
            setGravity(Gravity.CENTER)
            setWidth((AppManager.getScreenWidthPx() * 0.73).toInt())
            setAnimStyle(BaseDialog.AnimStyle.DEFAULT)
            mBinding.tvCancel.click {
                dismiss()
            }
            mBinding.tvClose.click {
                dismiss()
            }
            mBinding.tvConfirm.click {
                confirmClick?.onClick()
                dismiss()
            }
        }

        fun addOnConfirmClick(confirmClick: OnConfirmClick): Builder {
            this.confirmClick = confirmClick
            return this
        }
    }

    interface OnConfirmClick {
        fun onClick()
    }
}