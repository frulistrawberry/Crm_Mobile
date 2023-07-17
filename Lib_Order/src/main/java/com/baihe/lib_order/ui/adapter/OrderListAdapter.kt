package com.baihe.lib_order.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.baihe.lib_common.constant.StatusConstant
import com.baihe.lib_common.constant.StatusConstant.ORDER_TO_BE_SIGNED
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_order.OrderListItemEntity
import com.baihe.lib_order.databinding.OrderListItemBinding

class OrderListAdapter: BaseRecyclerViewAdapter<OrderListItemEntity, OrderListItemBinding>() {

    var onButtonActionListener:((orderId:String,oppoId:String,customerId:String,type:Int)->Unit)? =null
    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): OrderListItemBinding {
        return OrderListItemBinding.inflate(layoutInflater,parent,false)
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<OrderListItemBinding>,
        item: OrderListItemEntity?,
        position: Int
    ) {
        item?.let {
            when(item.orderstatus){
                ORDER_TO_BE_SIGNED -> {
                    holder.binding.tvPhase.text=item.order_phase_txt
                }
                else ->{
                    holder.binding.tvPhase.text=item.orderstatus_txt
                }
            }
            holder.binding.tvTitle.text=item.title
            holder.binding.kvlOrder.setData(item.showArray())
            val buttons = item.genBottomButtons()
            if (buttons.isEmpty()){
                holder.binding.llButtons.gone()
            }else{
                for(index in 0 until  holder.binding.llButtons.childCount){
                    holder.binding.llButtons.getChildAt(index).gone()
                }
                buttons.forEach{buttonEntity ->
                    val index = buttons.indexOf(buttonEntity)

                    val button = if (buttons.size == 3)
                        holder.binding.llButtons.getChildAt(index) as TextView
                    else
                        holder.binding.llButtons.getChildAt(index+1) as TextView
                    button.text = buttonEntity.name
                    button.click {
                        onButtonActionListener?.invoke(item.order_id,item.req_id,item.customer_id,buttonEntity.type)
                    }
                    button.visible()

                }
                holder.binding.llButtons.visible()
            }
        }
    }
}