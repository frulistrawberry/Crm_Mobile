package com.baihe.lib_order.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.invisible
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_order.OrderStatus
import com.baihe.lib_order.R
import com.baihe.lib_order.databinding.OrderProgressItemBinding

class OrderStatusAdapter: BaseRecyclerViewAdapter<OrderStatus, OrderProgressItemBinding>() {
    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): OrderProgressItemBinding {
        return OrderProgressItemBinding.inflate(layoutInflater,parent,false)
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<OrderProgressItemBinding>,
        item: OrderStatus?,
        position: Int
    ) {
        item?.let {
            holder.binding.tvOrderStatus.text = item.statusTxt
            if (item.status == 1)
                holder.binding.imageView1.visible()
            else
                holder.binding.imageView1.invisible()
            if (item.status == 2 || item.status == 1){
                holder.binding.imageView2.setImageResource(R.drawable.order_progress_completed)
            }else{
                holder.binding.imageView2.setImageResource(R.drawable.order_progress_un_completed)
            }
            holder.binding.tvTime.text=item.date?:""
        }

    }
}