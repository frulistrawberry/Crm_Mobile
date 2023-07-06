package com.baihe.lib_message.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_common.entity.MessageEntity
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_message.databinding.MessageItemLayoutBinding

/**
 * @author xukankan
 * @date 2023/7/5 15:08
 * @email：xukankan@jiayuan.com
 * @description：消息适配器
 */
class MessageListAdapter : BaseRecyclerViewAdapter<MessageEntity, MessageItemLayoutBinding>() {
    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): MessageItemLayoutBinding {
        return MessageItemLayoutBinding.inflate(layoutInflater, parent, false)
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<MessageItemLayoutBinding>,
        item: MessageEntity?,
        position: Int
    ) {
        holder.binding.tvTitle.text = item?.title
        holder.binding.tvContent.text = item?.text
        holder.binding.tvCheck.text = item?.btn
        holder.binding.tvTime.text = item?.createTime
        holder.itemView.setOnClickListener {

        }
    }

}