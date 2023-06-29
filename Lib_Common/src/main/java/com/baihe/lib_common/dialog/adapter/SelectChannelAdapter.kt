package com.baihe.lib_common.dialog.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.databinding.DialogItemChannelSecondLevelBinding
import com.baihe.lib_common.entity.ChannelEntity
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.adapter.BaseViewHolder
import com.baihe.lib_framework.ext.RecyclerViewExt.divider
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible

class SelectChannelAdapter:
    BaseRecyclerViewAdapter<ChannelEntity, DialogItemChannelSecondLevelBinding>() {
    var selectPosition:Int = -1
    var lastSelectPosition:Int = -1
    var childSelectPosition:Int = -1
    var keywords:String? = null
    var onItemSelectListener:((view: View, position:Int,childPosition:Int)->Unit)? = null
    var needAllExpand = false

    init {
        onItemClickListener = {view, position ->
            lastSelectPosition = selectPosition
            selectPosition = position
            notifyItemChanged(position)
            if (lastSelectPosition!=-1)
                notifyItemChanged(lastSelectPosition)
            onItemSelectListener?.invoke(view,position,childSelectPosition)

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<DialogItemChannelSecondLevelBinding>,
        item: ChannelEntity?,
        position: Int
    ) {
        holder.binding.bottomSelectItemNameTv.text = item?.label
        holder.binding.bottomSelectItemCheckedCb.isChecked = selectPosition == position
        if (position == selectPosition || (keywords?.isEmpty() == false) && selectPosition == -1){
            val childChannelList = item?.children
            if (childChannelList?.isEmpty() == true)
                holder.binding.rvChild.gone()
            else{
                holder.binding.rvChild.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = SelectChannelChildAdapter(childSelectPosition).apply {
                        keywords = this@SelectChannelAdapter.keywords?:""
                        setData(childChannelList)
                        onItemSelectListener = {_,childPosition ->
                            needAllExpand = false
                            this@SelectChannelAdapter.selectPosition = position
                            childSelectPosition = childPosition
                            this@SelectChannelAdapter.notifyDataSetChanged()

                        }
                    }
                    divider(Color.parseColor("#FFE6E6EC"))

                }
                holder.binding.rvChild.visible()
            }
        }else{
            holder.binding.rvChild.gone()
        }
    }





    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): DialogItemChannelSecondLevelBinding {
        return DialogItemChannelSecondLevelBinding.inflate(layoutInflater,parent,false)
    }
}