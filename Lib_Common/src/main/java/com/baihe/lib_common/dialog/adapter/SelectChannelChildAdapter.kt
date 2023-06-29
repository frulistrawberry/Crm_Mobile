package com.baihe.lib_common.dialog.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baihe.lib_common.databinding.DialogItemChannelThridLevelBinding
import com.baihe.lib_common.entity.ChannelEntity
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.ext.SpanExt.colorSpan

class SelectChannelChildAdapter(var selectPosition:Int = -1):
    BaseRecyclerViewAdapter<ChannelEntity, DialogItemChannelThridLevelBinding>() {
    var keywords:String = ""
    var onItemSelectListener:((view: View, position:Int)->Unit)? = null

    init {
        onItemClickListener = {view, position ->
            selectPosition = position
            notifyDataSetChanged()
            onItemSelectListener?.invoke(view,position)
        }
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<DialogItemChannelThridLevelBinding>,
        item: ChannelEntity?,
        position: Int
    ) {
        holder.binding.bottomSelectItemCheckedCb.isChecked = selectPosition == position
        holder.binding.bottomSelectItemNameTv.colorSpan(item?.label?:"",
            item?.label?.indexOf(keywords)?.rangeTo(keywords.length) ?: 0..0,
            Color.parseColor("#FF6C8EFF")
        )
    }

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): DialogItemChannelThridLevelBinding {
        return DialogItemChannelThridLevelBinding.inflate(layoutInflater,parent,false)
    }
}