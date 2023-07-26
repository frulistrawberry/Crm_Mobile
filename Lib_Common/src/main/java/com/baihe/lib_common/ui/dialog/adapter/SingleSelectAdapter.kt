package com.baihe.lib_common.ui.dialog.adapter

import android.annotation.SuppressLint
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

@SuppressLint("NotifyDataSetChanged")
class SingleSelectAdapter(val context:Context): BaseRecyclerViewAdapter<KeyValueEntity, DialogItemSelectBinding>() {
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
            if (!item.label.isNullOrEmpty()){
                holder.binding.bottomSelectItemNameTv.text = item.label

            }else if (!item.name.isNullOrEmpty()){
                holder.binding.bottomSelectItemNameTv.text = item.name
            }
            if (keywords.isNotEmpty()){
                if (!item.label.isNullOrEmpty()){
                    item.label?.let { label->
                        if (label.contains(keywords)){
                            holder.binding.bottomSelectItemNameTv.colorSpan(
                                label, label.indexOf(keywords).rangeTo(keywords.length) ,
                                Color.parseColor("#FF6C8EFF"))
                        }
                    }
                }else if (!item.name.isNullOrEmpty()){
                    item.name?.let { label->
                        if (label.contains(keywords)){
                            holder.binding.bottomSelectItemNameTv.colorSpan(
                                label, label.indexOf(keywords).rangeTo(keywords.length) ,
                                Color.parseColor("#FF6C8EFF"))
                        }
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
                holder.binding.bottomSelectItemChildRv.adapter = SingleSelectChildAdapter(context).apply {
                    selectPosition = childSelectPosition
                    parentPosition = position
                    setData(item.children)
                    keywords = this@SingleSelectAdapter.keywords
                    onChildSelectListener = {childPosition,parentPosition ->
                        childSelectPosition = childPosition
                        if (this@SingleSelectAdapter.selectPosition!=parentPosition){
                            this@SingleSelectAdapter.selectPosition = parentPosition
                            val tempPosition = this@SingleSelectAdapter.selectPosition
                            this@SingleSelectAdapter.selectPosition = parentPosition
                            this@SingleSelectAdapter.notifyItemChanged(parentPosition)
                            if (tempPosition!=-1)
                                this@SingleSelectAdapter.notifyItemChanged(tempPosition)
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