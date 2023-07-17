package com.baihe.lib_common.ui.dialog

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import com.baihe.lib_common.databinding.DialogFilterBinding
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.manager.AppManager

class FilterDialog {
    class Builder(context: Context):BaseDialog.Builder<Builder>(context){
        private val mBinding by lazy {
            DialogFilterBinding.inflate(LayoutInflater.from(context))
        }

        private var onCommitListener:((params:LinkedHashMap<String,Any?>)->Unit?)? = null

        init {
            setContentView(mBinding.root)
            setAnimStyle(BaseDialog.AnimStyle.RIGHT)
            setWidth((AppManager.getScreenWidthPx()*0.8F).toInt())
            setHeight(WindowManager.LayoutParams.MATCH_PARENT)
            setGravity(Gravity.END)
            mBinding.btnCancel.click {
                dismiss()
            }

            mBinding.btnReset.click {
                mBinding.kvlFilter.data?.let {data->
                    data.forEach {
                        it.value = ""
                        it.defaultValue = ""
                    }
                    mBinding.kvlFilter.refresh()
                }
            }

            mBinding.btnCommit.click {
                val params = mBinding.kvlFilter.commit()
                params?.let {
                    onCommitListener?.let {
                        onCommitListener!!.invoke(params)
                        dismiss()
                    }
                }
            }
        }

        fun setTitle(title:String):Builder{
            mBinding.tvTitle.text = title
            return this
        }

        fun setData(kvList:List<KeyValueEntity>):Builder{
            mBinding.kvlFilter.data = kvList
            return this
        }

        fun setOnCommitListener(block:((params:LinkedHashMap<String,Any?>)->Unit?)?):Builder{
            onCommitListener = block
            return this
        }

    }
}