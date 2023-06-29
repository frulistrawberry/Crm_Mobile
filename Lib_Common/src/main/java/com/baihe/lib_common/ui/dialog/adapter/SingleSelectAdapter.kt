package com.baihe.lib_common.ui.dialog.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_common.databinding.DialogItemSelectBinding
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
@SuppressLint("NotifyDataSetChanged")
class SingleSelectAdapter(val selectDataAdapter: SelectDataAdapter): BaseRecyclerViewAdapter<String, DialogItemSelectBinding>() {
    var selectPosition = selectDataAdapter.initSelectDataPosition()
    init {
        setData(MutableList(selectDataAdapter.getCount()) { "" })
        onItemClickListener = { _, position->
            selectPosition = position
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
        item: String?,
        position: Int
    ) {
        holder.binding.bottomSelectItemNameTv.text = selectDataAdapter.getText(position)
        holder.binding.bottomSelectItemCheckedCb.isChecked = selectPosition == position
    }
}