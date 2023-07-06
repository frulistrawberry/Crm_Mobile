package com.baihe.lib_common.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.baihe.lib_common.databinding.CommonDialogLoadingBinding
import com.baihe.lib_common.databinding.DialogAlertBinding
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.manager.AppManager

class AlertDialog {

    class Builder(context: Context): BaseDialog.Builder<Builder>(context) {
        private val mBinding: DialogAlertBinding = DialogAlertBinding.inflate(
            LayoutInflater.from(context)
        )
        var onConfirmListener:((dialog:Dialog?)->Unit)? = null

        init {
            setContentView(mBinding.root)
            setAnimStyle(BaseDialog.AnimStyle.DEFAULT)
            mBinding.btnCancel.click {
                dismiss()
            }
            mBinding.btnConfirm.click {
                if (onConfirmListener != null) {
                    onConfirmListener!!.invoke(dialog)
                    dismiss()
                }
            }
        }

        fun setContent(content:String):Builder{
            mBinding.tvContent.text = content
            return this
        }

        fun setOnConfirmListener(block:((dialog:Dialog?)->Unit)?):Builder{
            onConfirmListener = block
            return this
        }
    }
}