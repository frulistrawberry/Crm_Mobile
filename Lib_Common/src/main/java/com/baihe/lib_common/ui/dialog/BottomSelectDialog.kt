package com.baihe.lib_common.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.databinding.DialogBottomSelectBinding
import com.baihe.lib_common.ui.dialog.adapter.SingleSelectAdapter
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.ext.RecyclerViewExt.divider
import com.baihe.lib_framework.ext.ViewExt.click

class BottomSelectDialog {
    class Builder(context: Context):BaseDialog.Builder<Builder>(context){


        private val mBinding by lazy {
            DialogBottomSelectBinding.inflate(LayoutInflater.from(context))
        }
        private val adapter by lazy{
            SingleSelectAdapter()
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

        }


        fun setTitle(title:String): Builder {
            mBinding.bottomSelectTitleTv.text = title
            return this
        }

        fun setData(data:List<KeyValueEntity>?, defaultValue:String?):Builder {
            var defParenPosition = -1
            data?.forEach{parent ->
                if (parent.value == defaultValue){
                    defParenPosition = data.indexOf(parent)
                    return@forEach
                }
            }
            adapter.selectPosition = defParenPosition
            adapter.setData(data)
            return this
        }

        fun setOnConfirmClickListener(onConfirm:(dialog: Dialog?,value:String,name:String)->Unit): Builder {
            mBinding.bottomSelectConfirmTv.click {
                dismiss()
                if (adapter.selectPosition == -1)
                    return@click
                val data = adapter.getData()
                var value:String
                var name:String
                data.let {
                    value = data[adapter.selectPosition].value
                    name = data[adapter.selectPosition].name
                }
                onConfirm.invoke(dialog, value,name)
            }
            return this
        }


    }




}