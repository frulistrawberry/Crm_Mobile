package com.baihe.lib_common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_common.databinding.CommonItemFollowBinding
import com.baihe.lib_common.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible

class FollowListAdapter: BaseRecyclerViewAdapter<List<KeyValueEntity>, CommonItemFollowBinding>() {
    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<CommonItemFollowBinding>,
        item: List<KeyValueEntity>?,
        position: Int
    ) {
        holder.binding.kvlFollow.setData(item)
        if (position == itemCount-1)
            holder.binding.vDivider.gone()
        else
            holder.binding.vDivider.visible()
    }

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonItemFollowBinding {
        return CommonItemFollowBinding.inflate(layoutInflater,parent,false)
    }
}