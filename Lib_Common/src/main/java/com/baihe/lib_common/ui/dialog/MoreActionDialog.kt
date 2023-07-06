package com.baihe.lib_common.ui.dialog

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.R
import com.baihe.lib_common.databinding.CommonDialogMoreActionBinding
import com.baihe.lib_common.databinding.DialogBottomSelectBinding
import com.baihe.lib_common.entity.ButtonTypeEntity
import com.baihe.lib_common.ui.dialog.adapter.MoreActionsAdapter
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.ext.RecyclerViewExt.divider
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.manager.AppManager

class MoreActionDialog {
    class Builder(context: Context):BaseDialog.Builder<Builder>(context){
        private val mBinding by lazy {
            CommonDialogMoreActionBinding.inflate(LayoutInflater.from(context))
        }

        private var onButtonsClick:((type:Int)->Unit)? = null

        val adapter = MoreActionsAdapter()

        init {
            setContentView(mBinding.root)
            setAnimStyle(BaseDialog.AnimStyle.BOTTOM)
            setWidth((AppManager.getScreenWidthPx()*0.37).toInt())
            setHeight(WindowManager.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM.or(Gravity.START))
            setThemeStyle(R.style.MoreActionDialogTheme)
            setAnimStyle(R.style.AlphaAnimStyle)
            mBinding.rvActions.layoutManager = LinearLayoutManager(context)
            mBinding.rvActions.adapter = adapter.apply {
                onItemClickListener = {_,position->
                    val item = getData()[position]
                    onButtonsClick?.invoke(item.type)
                    dismiss()
                }
            }
        }

        fun setData(buttons:List<ButtonTypeEntity>):Builder{
            adapter.setData(buttons)
            return this
        }

        fun setOnButtonsClickListener(block:((type:Int)->Unit)?):Builder{
            onButtonsClick = block
            return this
        }
    }
}