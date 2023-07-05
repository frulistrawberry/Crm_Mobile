package com.baihe.lib_common.ui.dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_common.databinding.DialogItemMoreActionBinding
import com.baihe.lib_common.entity.ButtonTypeEntity
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter

class MoreActionsAdapter: BaseRecyclerViewAdapter<ButtonTypeEntity, DialogItemMoreActionBinding>() {
    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): DialogItemMoreActionBinding {
        return DialogItemMoreActionBinding.inflate(layoutInflater,parent,false)
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<DialogItemMoreActionBinding>,
        item: ButtonTypeEntity?,
        position: Int
    ) {
        holder.binding.button.text = item?.name
    }
}