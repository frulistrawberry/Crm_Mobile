package com.baihe.lib_common.ui.dialog.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_common.R
import com.baihe.lib_common.databinding.DialogItemSelectBinding
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter

@SuppressLint("NotifyDataSetChanged")
class SingleSelectAdapter: BaseRecyclerViewAdapter<KeyValueEntity, DialogItemSelectBinding>() {
    var selectPosition:Int = -1
    init {
        onItemClickListener = { _, position ->
            val tempPosition = selectPosition
            selectPosition = position
            notifyItemChanged(position)
            notifyItemChanged(tempPosition)
        }
    }

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): DialogItemSelectBinding {
        return DialogItemSelectBinding.inflate(layoutInflater,parent,false)
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<DialogItemSelectBinding>,
        item: KeyValueEntity?,
        position: Int
    ) {
        holder.binding.bottomSelectItemNameTv.text = item?.name
        if (selectPosition == position){
            holder.binding.bottomSelectItemCheckedCb.setImageResource(R.mipmap.ic_check)
        }else{
            holder.binding.bottomSelectItemCheckedCb.setImageResource(R.mipmap.ic_uncheck)
        }
    }
}