package com.baihe.lib_common.ui.dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_common.databinding.DialogItemMultiSelectBinding
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter

class MultiSelectAdapter: BaseRecyclerViewAdapter<KeyValueEntity, DialogItemMultiSelectBinding>() {
    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): DialogItemMultiSelectBinding {
        return DialogItemMultiSelectBinding.inflate(layoutInflater,parent,false)
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<DialogItemMultiSelectBinding>,
        item: KeyValueEntity?,
        position: Int
    ) {
//        holder.binding.bottomSelectItemNameTv.text = item?.key
//        holder.binding.bottomSelectItemCheckedCb.isChecked = selectPosition == position
    }
}