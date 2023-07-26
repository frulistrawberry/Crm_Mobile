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
class MultiSelectAdapter(val context:Context): BaseRecyclerViewAdapter<KeyValueEntity, DialogItemSelectBinding>() {

    var selectPosition:MutableList<Int> = mutableListOf()
    var childSelectPosition:MutableList<Int> = mutableListOf()
    var keywords:String = ""
    init {
        onItemClickListener = { _, position ->
            if (selectPosition.contains(position))
                selectPosition.remove(position)
            else
                selectPosition.add(position)
            notifyDataSetChanged()
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
            if (selectPosition.contains(position) ){
                holder.binding.bottomSelectItemCheckedCb.setImageResource(R.mipmap.ic_check)
            }else{
                holder.binding.bottomSelectItemCheckedCb.setImageResource(R.mipmap.ic_uncheck)
            }
            if (item.children?.isEmpty() == false && (selectPosition.contains(position) || (keywords.isNotEmpty()&&item.label.contains(keywords)))){
                holder.binding.bottomSelectItemChildRv.layoutManager = LinearLayoutManager(context)
                holder.binding.bottomSelectItemChildRv.adapter = MultiSelectChildAdapter(context).apply {
                    parentPosition = position
                    childSelectPosition.forEach {
                        selectPosition.add(it)
                    }
                    setData(item.children)
                    keywords = this@MultiSelectAdapter.keywords
                    onChildSelectListener = { childPosition, parentPosition ->
                        if (childSelectPosition.contains(childPosition))
                            childSelectPosition.remove(childPosition)
                        else
                            childSelectPosition.add(childPosition)
                        if (!this@MultiSelectAdapter.selectPosition.contains(parentPosition)){
                            this@MultiSelectAdapter.selectPosition.add(parentPosition)
                            this@MultiSelectAdapter.notifyDataSetChanged()
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