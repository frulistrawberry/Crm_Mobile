package com.baihe.lib_home.home

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import com.baihe.lib_common.R
import com.baihe.lib_common.provider.CustomerServiceProvider
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.manager.AppManager
import com.baihe.lib_home.databinding.HomeDialogQuickCreateBinding

class QuickCreateDialog {
    class Builder(context: Context):BaseDialog.Builder<Builder>(context){
        private val mBinding: HomeDialogQuickCreateBinding = HomeDialogQuickCreateBinding.inflate(
            LayoutInflater.from(context)
        )
        init {
            setContentView(mBinding.root)
            setWidth((AppManager.getScreenWidthPx()*0.37).toInt())
            setGravity(Gravity.END.or(Gravity.TOP))
            mBinding.btnCreateCustomer.click {
                CustomerServiceProvider.createOrUpdateCustomer(context)
                dialog?.dismiss()
            }
            mBinding.btnCreateOpportunity.click {
                dialog?.dismiss()
            }
            mBinding.btnCreateOrder.click {
                dialog?.dismiss()
            }
            mBinding.btnCancel.click {
                dialog?.dismiss()
            }
        }
    }
}