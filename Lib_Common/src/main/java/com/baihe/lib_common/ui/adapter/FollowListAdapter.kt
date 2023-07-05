package com.baihe.lib_common.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_common.databinding.CommonItemFollowBinding
import com.baihe.lib_common.entity.FollowEntity
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible

class FollowListAdapter: BaseRecyclerViewAdapter<FollowEntity, CommonItemFollowBinding>() {

    var onFollowItemClickListener:((followId:String)->Unit)? = null


    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<CommonItemFollowBinding>,
        item: FollowEntity?,
        position: Int
    ) {

        if (item?.title.isNullOrEmpty()||item?.time.isNullOrEmpty()){
            holder.binding.llFollowHeader.gone()
        }else{
            holder.binding.tvTitle.text = item?.title
            holder.binding.tvFollowTime.text = item?.time
            holder.binding.llFollowHeader.click {
                if (onFollowItemClickListener != null) {
                    onFollowItemClickListener?.invoke(item?.id?:"")
                }
            }
            holder.binding.llFollowHeader.visible()
        }
        if (item?.content!=null)
            holder.binding.kvlFollow.setData(item.content)
        else
            holder.binding.kvlFollow.setData(item?.showArray())
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