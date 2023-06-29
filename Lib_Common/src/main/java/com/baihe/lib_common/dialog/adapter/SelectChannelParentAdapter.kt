package com.baihe.lib_common.dialog.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baihe.lib_common.R
import com.baihe.lib_common.databinding.DialogItemChannelFirstLevelBinding
import com.baihe.lib_common.entity.ChannelEntity
import com.baihe.lib_common.widget.font.FontStyle
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.utils.ResUtils

@SuppressLint("NotifyDataSetChanged")
class SelectChannelParentAdapter:
    BaseRecyclerViewAdapter<ChannelEntity, DialogItemChannelFirstLevelBinding>() {
    var selectPosition = 0
    private var lastPosition = -1
    private var nextPosition = 1
    var onItemSelectListener:((position:Int)->Unit)? = null

    init {
        lastPosition = selectPosition-1
        nextPosition = selectPosition+1
        onItemClickListener = { _, position ->
            lastPosition = position-1
            nextPosition = position+1
            selectPosition = position
            notifyDataSetChanged()
            onItemSelectListener?.invoke(position)
        }
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<DialogItemChannelFirstLevelBinding>,
        item: ChannelEntity?,
        position: Int
    ) {
        holder.binding.tvLabel.text = item?.label
        if (position == selectPosition){
            holder.binding.llParent.background = ResUtils.getColorDrawable(R.color.white)
            holder.binding.tvLabel.fontStyle = FontStyle.HALF_BOLD
        }else{
            holder.binding.tvLabel.fontStyle = FontStyle.LIGHT
            when (position) {
                lastPosition -> {
                    holder.binding.llParent.background = ResUtils.getImageFromResource(R.drawable.common_bottom_right_round_f6f7fc)
                }
                nextPosition -> {
                    holder.binding.llParent.background = ResUtils.getImageFromResource(R.drawable.common_top_right_round_f6f7fc)
                }
                else -> {
                    holder.binding.llParent.background = ResUtils.getColorDrawable(R.color.COLOR_FFF6F7FC)
                }
            }

        }
    }

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): DialogItemChannelFirstLevelBinding {
        return DialogItemChannelFirstLevelBinding.inflate(layoutInflater,parent,false)
    }
}