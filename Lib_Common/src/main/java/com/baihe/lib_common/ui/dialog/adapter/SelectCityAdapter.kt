package com.baihe.lib_common.ui.dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_common.R
import com.baihe.lib_common.databinding.DialogItemCitySelectBinding
import com.baihe.lib_common.databinding.DialogItemSelectBinding
import com.baihe.lib_common.entity.CityEntity
import com.baihe.lib_common.utils.ChineseCharacterUtil
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.invisible
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.utils.ResUtils

class SelectCityAdapter: BaseRecyclerViewAdapter<CityEntity, DialogItemCitySelectBinding>() {
    var char:String? = null
    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): DialogItemCitySelectBinding {
        return DialogItemCitySelectBinding.inflate(layoutInflater,parent,false)
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<DialogItemCitySelectBinding>,
        item: CityEntity?,
        position: Int
    ) {
        holder.binding.bottomSelectItemNameTv.text = item?.name
        if (item?.select==true)
            holder.binding.bottomSelectItemCheckedIv.setImageResource(R.mipmap.ic_check)
        else
            holder.binding.bottomSelectItemCheckedIv.setImageResource(R.mipmap.ic_uncheck)
        val currChart = ChineseCharacterUtil.getUpperCaseFirst(item?.name,false)
        holder.binding.tvChar.text = currChart
        if (char == null || !char.equals(currChart)){
            char = currChart
            holder.binding.tvChar.visible()
        }else{
            holder.binding.tvChar.invisible()
        }
    }

}