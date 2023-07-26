package com.baihe.lib_common.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.databinding.DialogBottomSelectBinding
import com.baihe.lib_common.ui.dialog.adapter.MultiSelectAdapter
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.ext.RecyclerViewExt.divider
import com.baihe.lib_framework.ext.ViewExt.click

class BottomSelectMultiDialog {
    class Builder(context: Context) :BaseDialog.Builder<Builder>(context){
        private val mBinding by lazy {
            DialogBottomSelectBinding.inflate(LayoutInflater.from(context))
        }

        private val adapter:MultiSelectAdapter by lazy {
            MultiSelectAdapter(context)
        }

        init {
            setContentView(mBinding.root)
            setAnimStyle(BaseDialog.AnimStyle.BOTTOM)
            setWidth(WindowManager.LayoutParams.MATCH_PARENT)
            setHeight(WindowManager.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
            mBinding.bottomSelectListRv.layoutManager = LinearLayoutManager(context)
            mBinding.bottomSelectListRv.adapter = adapter
            mBinding.bottomSelectListRv.divider(includeLast = false)
            mBinding.bottomSelectCancelIv.click {
                dismiss()
            }

        }


        fun setTitle(title:String): Builder {
            mBinding.bottomSelectTitleTv.text = title
            return this
        }

        fun setData(data:List<KeyValueEntity>?,defaultValue:String?,defaultSubValue:String?):Builder{
            val defParenPosition = mutableListOf<Int>()
            val defChildPosition = mutableListOf<Int>()
            val defaultValues:MutableList<String> = if (defaultValue?.contains(",") == true){
                defaultValue.split(",").toMutableList()
            }else if (!defaultValue.isNullOrEmpty()){
                mutableListOf<String>().also {
                    it.add(defaultValue)
                }
            }else{
                mutableListOf()
            }
            val subDefaultValues = if (defaultSubValue?.contains(",") == true){
                defaultSubValue.split(",").toMutableList()
            }else if (!defaultSubValue.isNullOrEmpty()){
                mutableListOf<String>().also {
                    it.add(defaultSubValue)
                }
            }else{
                mutableListOf()
            }
            data?.forEach{parent ->
                if (defaultValues.contains(parent.value) ){
                    defParenPosition.add(data.indexOf(parent))
                }
                if (!parent.children.isNullOrEmpty()){
                    parent.children.forEach { child ->
                        if (subDefaultValues.contains(child.value)){
                            defChildPosition.add(parent.children.indexOf(child))
                        }
                    }
                }
            }
            adapter.selectPosition = defParenPosition
            adapter.childSelectPosition = defChildPosition
            adapter.setData(data)
            return this
        }
        fun setOnConfirmClickListener(onConfirm:(dialog: Dialog?, value:String,label:String,subValue:String?)->Unit): Builder {
            mBinding.bottomSelectConfirmTv.click {
                dismiss()
                if (adapter.selectPosition.isEmpty())
                    return@click
                val data = adapter.getData()
                var value:String
                var label:String
                var subValue:String? = null
                data.let {
                    val valueSb = StringBuilder()
                    val labelSb = StringBuilder()
                    adapter.selectPosition.forEach { parentSelect ->
                        valueSb.append(data[parentSelect].value)
                        labelSb.append(data[parentSelect].label)
                        if (adapter.selectPosition.indexOf(parentSelect)<adapter.selectPosition.size-1){
                            valueSb.append(",")
                            labelSb.append(",")
                        }
                        if (adapter.childSelectPosition.isNotEmpty()){
                            val subValueSb = StringBuilder()
                            if (!data[parentSelect].children.isNullOrEmpty()){
                                adapter.childSelectPosition.forEach {
                                    subValueSb.append(data[parentSelect].children[it].value)
                                    if (adapter.childSelectPosition.indexOf(it)<adapter.childSelectPosition.size-1){
                                        subValueSb.append(",")
                                    }
                                }
                            }

                            subValue  = subValueSb.toString()
                        }
                    }
                    value = valueSb.toString()
                    label = labelSb.toString()
                }

                onConfirm.invoke(dialog, value,label,subValue)
            }
            return this
        }
    }
}