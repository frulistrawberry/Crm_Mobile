package com.baihe.lib_common.ui.dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_common.databinding.DialogItemCitySelectBinding
import com.baihe.lib_common.databinding.DialogItemSelectBinding
import com.baihe.lib_common.entity.CityEntity
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter

class SelectCityAdapter: BaseRecyclerViewAdapter<CityEntity, DialogItemCitySelectBinding>() {
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
        holder.binding.bottomSelectItemCheckedCb.isChecked = item?.select?:false
    }

}