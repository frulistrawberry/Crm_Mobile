package com.baihe.lib_common.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.databinding.DialogBottomSelectBinding
import com.baihe.lib_common.ui.dialog.adapter.SingleSelectAdapter
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_common.viewmodel.CommonViewModel
import com.baihe.lib_framework.base.BaseActivity
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.ext.RecyclerViewExt.divider
import com.baihe.lib_framework.ext.ViewExt.click

class SelectRecordUserDialog {
    class Builder(context: Context,val channelId:String):BaseDialog.Builder<Builder>(context){
        private val mBinding by lazy {
            DialogBottomSelectBinding.inflate(LayoutInflater.from(context))
        }
        private val adapter by lazy{
            SingleSelectAdapter()
        }
        private val option by lazy {
            mutableListOf<KeyValueEntity>()
        }
        private var defaultValue: String? = null
        private val activity = context as BaseActivity
        private val mViewModel by lazy {
            activity.let {
                ViewModelProvider(activity).get(CommonViewModel::class.java)
            }
        }

        init {
            setContentView(mBinding.root)
            setAnimStyle(BaseDialog.AnimStyle.BOTTOM)
            setWidth(WindowManager.LayoutParams.MATCH_PARENT)
            setHeight(WindowManager.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
            mBinding.bottomSelectListRv.layoutManager = LinearLayoutManager(context)
            mBinding.bottomSelectListRv.divider(includeLast = false)
            mBinding.bottomSelectListRv.adapter = adapter
            mBinding.bottomSelectCancelIv.click {
                dismiss()
            }
            mBinding.bottomSelectTitleTv.text = "请选择提供人"
            mViewModel.loadingDialogLiveData.observe(activity){
                if (it)
                    activity.showLoadingDialog()
                else
                    activity.dismissLoadingDialog()
            }
            mViewModel.recordUserListLiveData.observe(activity){userList->
                option.clear()
                userList.forEach { user->
                    option.add(KeyValueEntity().apply {
                        name = user.name
                        value = user.id
                    })
                }
                setData(option)
            }
        }
        private fun setData(data:List<KeyValueEntity>?) {
            var defParenPosition = -1
            data?.forEach{parent ->
                if (parent.value == defaultValue){
                    defParenPosition = data.indexOf(parent)
                    return@forEach
                }
            }
            adapter.selectPosition = defParenPosition
            adapter.setData(data)
        }

        fun setDefaultValue(defaultValue:String?): Builder {
            this.defaultValue = defaultValue
            return this
        }

        fun setOnConfirmClickListener(onConfirm:(dialog: Dialog?, value:String,name:String)->Unit): Builder {
            mBinding.bottomSelectConfirmTv.click {
                dismiss()
                if (adapter.selectPosition == -1)
                    return@click
                var value:String
                var name:String
                option.let {
                    value = option[adapter.selectPosition].value
                    name = option[adapter.selectPosition].name
                }
                onConfirm.invoke(dialog, value,name)
            }
            return this
        }

        override fun create(): BaseDialog {
            mViewModel.getRecordUser(channelId)
            return super.create()

        }


    }
}