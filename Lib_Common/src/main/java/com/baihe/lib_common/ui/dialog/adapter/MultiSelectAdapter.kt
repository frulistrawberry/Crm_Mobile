package com.baihe.lib_common.ui.dialog.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.R
import com.baihe.lib_common.databinding.DialogItemSelectBinding
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.ext.RecyclerViewExt.divider
import com.baihe.lib_framework.ext.SpanExt.colorSpan
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible

class MultiSelectAdapter(val context:Context): BaseRecyclerViewAdapter<KeyValueEntity, DialogItemSelectBinding>() {

    var selectPosition:Int = -1
    var childSelectPosition:Int = -1
    var keywords:String = ""
    init {
        onItemClickListener = { _, position ->
            val tempPosition = selectPosition
            selectPosition = position
            childSelectPosition = -1
            notifyItemChanged(position)
            if (tempPosition!=-1)
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

        item?.let {
            holder.binding.bottomSelectItemNameTv.text = item.label
            if (keywords.isNotEmpty()){
                item.label?.let { label->
                    if (label.contains(keywords)){
                        holder.binding.bottomSelectItemNameTv.colorSpan(
                            label, label.indexOf(keywords).rangeTo(keywords.length) ,
                            Color.parseColor("#FF6C8EFF"))
                    }
                }
            }
            if (selectPosition == position ){
                holder.binding.bottomSelectItemCheckedCb.setImageResource(R.mipmap.ic_check)
            }else{
                holder.binding.bottomSelectItemCheckedCb.setImageResource(R.mipmap.ic_uncheck)
            }
            if (item.children?.isEmpty() == false && (selectPosition == position || (keywords.isNotEmpty()&&item.label.contains(keywords)))){
                holder.binding.bottomSelectItemChildRv.layoutManager = LinearLayoutManager(context)
                holder.binding.bottomSelectItemChildRv.adapter = MultiSelectChildAdapter(context).apply {
                    selectPosition = childSelectPosition
                    parentPosition = position
                    setData(item.children)
                    keywords = this@MultiSelectAdapter.keywords
                    onChildSelectListener = {childPosition,parentPosition ->
                        childSelectPosition = childPosition
                        if (this@MultiSelectAdapter.selectPosition!=parentPosition){
                            this@MultiSelectAdapter.selectPosition = parentPosition
                            val tempPosition = this@MultiSelectAdapter.selectPosition
                            this@MultiSelectAdapter.selectPosition = parentPosition
                            this@MultiSelectAdapter.notifyItemChanged(parentPosition)
                            if (tempPosition!=-1)
                                this@MultiSelectAdapter.notifyItemChanged(tempPosition)
                        }


                    }
                }
                holder.binding.bottomSelectItemChildRv.divider(includeLast = false)
                holder.binding.bottomSelectItemChildRv.visible()
            }else{
                holder.binding.bottomSelectItemChildRv.gone()
            }
        }

    }
}