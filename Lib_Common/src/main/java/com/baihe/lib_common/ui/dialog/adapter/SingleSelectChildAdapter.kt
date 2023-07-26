package com.baihe.lib_common.ui.dialog.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_common.R
import com.baihe.lib_common.databinding.DialogItemSelectChildBinding
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.ext.SpanExt.colorSpan

class SingleSelectChildAdapter(val context: Context): BaseRecyclerViewAdapter<KeyValueEntity, DialogItemSelectChildBinding>() {

    var selectPosition:Int = -1
    var parentPosition:Int = -1
    var keywords:String = ""
    var onChildSelectListener:((position:Int,parentPosition:Int)->Unit)? = null
    init {
        onItemClickListener = { _, position ->
            val tempPosition = selectPosition
            selectPosition = position
            notifyItemChanged(position)
            notifyItemChanged(tempPosition)
            onChildSelectListener?.invoke(position,parentPosition)
        }
    }

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): DialogItemSelectChildBinding {
        return DialogItemSelectChildBinding.inflate(layoutInflater,parent,false)
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<DialogItemSelectChildBinding>,
        item: KeyValueEntity?,
        position: Int
    ) {
        holder.binding.bottomSelectItemNameTv.text = item?.label
        if (keywords.isNotEmpty()){
            item?.label?.let {label->
                if (label.contains(keywords)){
                    holder.binding.bottomSelectItemNameTv.colorSpan(
                        label, label.indexOf(keywords).rangeTo(keywords.length) ,
                        Color.parseColor("#FF6C8EFF"))
                }
            }
        }
        if (selectPosition == position){
            holder.binding.bottomSelectItemCheckedCb.setImageResource(R.mipmap.ic_check)
        }else{
            holder.binding.bottomSelectItemCheckedCb.setImageResource(R.mipmap.ic_uncheck)
        }
    }
}