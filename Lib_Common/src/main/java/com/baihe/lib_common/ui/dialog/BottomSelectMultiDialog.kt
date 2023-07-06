package com.baihe.lib_common.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.databinding.DialogBottomSelectBinding
import com.baihe.lib_common.ui.dialog.adapter.SelectDataAdapter
import com.baihe.lib_common.ui.dialog.adapter.SingleSelectAdapter
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.ext.RecyclerViewExt.divider
import com.baihe.lib_framework.ext.ViewExt.click

class BottomSelectMultiDialog {
    class Builder(context: Context) :BaseDialog.Builder<Builder>(context){
        private val mBinding by lazy {
            DialogBottomSelectBinding.inflate(LayoutInflater.from(context))
        }
        private lateinit var singleSelectAdapter: SingleSelectAdapter

        init {
            setContentView(mBinding.root)
            setAnimStyle(BaseDialog.AnimStyle.BOTTOM)
            setWidth(WindowManager.LayoutParams.MATCH_PARENT)
            setHeight(WindowManager.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
            mBinding.bottomSelectListRv.layoutManager = LinearLayoutManager(context)
            mBinding.bottomSelectCancelIv.click {
                dismiss()
            }

        }

        fun setSelectDataAdapter(selectDataAdapter: SelectDataAdapter): Builder {
            singleSelectAdapter = SingleSelectAdapter(selectDataAdapter)
            mBinding.bottomSelectListRv.adapter = singleSelectAdapter
            return this
        }

        fun setTitle(title:String): Builder {
            mBinding.bottomSelectTitleTv.text = title
            return this
        }

        fun setOnConfirmClickListener(onConfirm:(dialog: Dialog?, position:Int)->Unit): Builder {
            mBinding.bottomSelectConfirmTv.click {
                onConfirm.invoke(dialog, singleSelectAdapter.selectPosition)
            }
            return this
        }
    }
}