package com.baihe.lib_common.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.R
import com.baihe.lib_common.databinding.DialogBottomSelectBinding
import com.baihe.lib_common.databinding.DialogCitySelectBinding
import com.baihe.lib_common.entity.CityEntity
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.ui.dialog.adapter.SelectCityAdapter
import com.baihe.lib_common.viewmodel.CommonViewModel
import com.baihe.lib_framework.base.BaseActivity
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.ext.RecyclerViewExt.divider
import com.baihe.lib_framework.ext.ResourcesExt.drawable
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.toast.TipsToast
import com.baihe.lib_framework.utils.ResUtils

class SelectCityDialog {
    class Builder(context: Context):BaseDialog.Builder<Builder>(context){
        private val mBinding by lazy {
            DialogCitySelectBinding.inflate(LayoutInflater.from(context))
        }
        private val parentAdapter by lazy {
            SelectCityAdapter()
        }
        private val childAdapter by lazy {
            SelectCityAdapter()
        }
        private val mViewModel by lazy {
            ViewModelProvider(activity).get(CommonViewModel::class.java)
        }
        private val activity = context as BaseActivity

        private var onConfirmListener:((city:CityEntity)->Unit)? = null

        var selectCity:CityEntity? = null

        init {
            setContentView(mBinding.root)
            setAnimStyle(BaseDialog.AnimStyle.BOTTOM)
            setWidth(WindowManager.LayoutParams.MATCH_PARENT)
            setHeight(WindowManager.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
            mBinding.bottomSelectTitleTv.text="请选择意向区域"
            mBinding.bottomSelectListRv.layoutManager = LinearLayoutManager(context)
            mBinding.bottomSelectListRv.adapter = parentAdapter
            mBinding.bottomSelectCancelIv.click {
                dismiss()
            }
            mBinding.tvTagParent.text = "请选择"
            mBinding.tvTagChild.gone()
            mViewModel.loadingDialogLiveData.observe(activity){
                if (it){
                    activity.showLoadingDialog()
                }else{
                    activity.dismissLoadingDialog()
                }
            }
            mViewModel.cityListLiveData.observe(activity){
                parentAdapter.setData(it)
            }
            parentAdapter.onItemClickListener = { _, position->
                val item = parentAdapter.getItem(position)
                var lastSelectItem:CityEntity? = null
                val data = parentAdapter.getData()
                data.forEach {
                    if (it.select)
                        lastSelectItem = it
                }
                if (lastSelectItem!=null){
                    lastSelectItem!!.select = false
                    parentAdapter.notifyItemChanged(data.indexOf(lastSelectItem))
                }
                item?.select=true
                parentAdapter.notifyItemChanged(position)
                val children = item?.children?: emptyList()
                mBinding.tvTagParent.text = item?.name
                mBinding.tvTagParent.setTextColor(Color.parseColor("#FF6C8EFF"))
                mBinding.tvTagParent.background = context.drawable(R.drawable.bg_city_select_label_light)
                mBinding.tvTagChild.setTextColor(Color.parseColor("#8F4A4C5C"))
                mBinding.tvTagChild.background = context.drawable(R.drawable.bg_city_select_label_gray)
                mBinding.tvTagChild.text = "请选择"
                mBinding.bottomSelectListRv.adapter = childAdapter
                childAdapter.setData(children)
                selectCity = null
                mBinding.tvTagChild.visible()
            }
            childAdapter.onItemClickListener = { _, position->
                val item = childAdapter.getItem(position)
                var lastSelectItem:CityEntity? = null
                val data = childAdapter.getData()
                data.forEach {
                    if (it.select)
                        lastSelectItem = it
                }
                if (lastSelectItem!=null){
                    lastSelectItem!!.select = false
                    childAdapter.notifyItemChanged(data.indexOf(lastSelectItem))
                }
                item?.select=true
                childAdapter.notifyItemChanged(position)
                mBinding.tvTagChild.setTextColor(Color.parseColor("#FF6C8EFF"))
                mBinding.tvTagChild.background = context.drawable(R.drawable.bg_city_select_label_light)
                mBinding.tvTagChild.text = item?.name
                selectCity = item
            }
            mBinding.tvTagParent.click {
                mBinding.tvTagChild.setTextColor(Color.parseColor("#8F4A4C5C"))
                mBinding.tvTagChild.background = context.drawable(R.drawable.bg_city_select_label_gray)
                mBinding.tvTagChild.text = "请选择"
                mBinding.bottomSelectListRv.adapter = parentAdapter
                selectCity = null
            }

            mBinding.bottomSelectConfirmTv.click {
                if (selectCity!=null){
                    if (onConfirmListener!=null){
                        onConfirmListener?.invoke(selectCity!!)
                    }
                    dismiss()
                }else{
                    TipsToast.showTips("请选择意向区域")
                }
            }

            mViewModel.getCityList()

        }

        fun setOnConfirmListener(block:(city:CityEntity)->Unit):Builder{
            onConfirmListener = block
            return this
        }
    }
}