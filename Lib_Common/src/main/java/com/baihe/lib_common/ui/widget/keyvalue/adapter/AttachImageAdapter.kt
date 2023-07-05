package com.baihe.lib_common.ui.widget.keyvalue.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.imageloader.ImageLoaderUtils
import com.baihe.lib_common.databinding.LayoutKeyvalueItemAttachBinding
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter

class AttachImageAdapter: BaseRecyclerViewAdapter<String, LayoutKeyvalueItemAttachBinding>() {
    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): LayoutKeyvalueItemAttachBinding {
        return LayoutKeyvalueItemAttachBinding.inflate(layoutInflater,parent,false)
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<LayoutKeyvalueItemAttachBinding>,
        item: String?,
        position: Int
    ) {
        ImageLoaderUtils.getInstance().loadImage(holder.binding.imageview.context,holder.binding.imageview,item)
    }
}