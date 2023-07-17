package com.baihe.lib_home.ui.dialog

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.baihe.lib_common.provider.CustomerServiceProvider
import com.baihe.lib_common.provider.OpportunityServiceProvider
import com.baihe.lib_common.ui.dialog.AlertDialog
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.manager.AppManager
import com.baihe.lib_common.R
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
                OpportunityServiceProvider.createOrUpdateOpportunity(context)
                dialog?.dismiss()
            }
            mBinding.btnCreateOrder.click {
                dialog?.dismiss()
                AlertDialog.Builder(context)
                    .setText(R.id.button1,"已有，去选择")
                    .setText(R.id.button2,"暂无，去新增")
                    .setOnClickListener(R.id.button1,object :BaseDialog.OnClickListener{
                        override fun onClick(dialog: BaseDialog?, view: View) {
                            dialog?.dismiss()
                            // TODO: 选择机会
                        }

                    })
                    .setOnClickListener(R.id.button2,object :BaseDialog.OnClickListener{
                        override fun onClick(dialog: BaseDialog?, view: View) {
                            dialog?.dismiss()
                            // TODO: 新增机会
                        }

                    })
            }
            mBinding.btnCancel.click {
                dialog?.dismiss()
            }
        }
    }
}