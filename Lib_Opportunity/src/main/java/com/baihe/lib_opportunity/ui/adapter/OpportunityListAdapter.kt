package com.baihe.lib_opportunity.ui.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_common.R
import com.baihe.lib_common.entity.OpportunityListItemEntity
import com.baihe.lib_common.utils.FormatUtils
import com.baihe.lib_common.viewmodel.CommonViewModel
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.utils.ResUtils
import com.baihe.lib_opportunity.databinding.OppoItemOpportunityListBinding

class OpportunityListAdapter:
    BaseRecyclerViewAdapter<OpportunityListItemEntity, OppoItemOpportunityListBinding>() {
    var onButtonActionListener:((oppoId:String,customerId:String,orderStatus:String?,type:Int)->Unit)? =null
    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): OppoItemOpportunityListBinding {
        return OppoItemOpportunityListBinding.inflate(layoutInflater,parent,false)
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<OppoItemOpportunityListBinding>,
        item: OpportunityListItemEntity?,
        position: Int
    ) {
        holder.binding.tvTitle.text = item?.title
        val oppoLabel = FormatUtils.formatOppoLabel(item?.reqPhase)
        if (oppoLabel != null) {
            holder.binding.tvReqPhase.text = oppoLabel.text
            if (!oppoLabel.bgColor.isNullOrEmpty() && oppoLabel.bgColor.startsWith("#")){
                val labelDrawable = ResUtils.getImageFromResource(R.drawable.bg_round_label_solid) as GradientDrawable
                labelDrawable.setColor(Color.parseColor(oppoLabel.bgColor))
                holder.binding.tvReqPhase.background = labelDrawable
            }else{
                holder.binding.tvReqPhase.background = null
            }
            if (!oppoLabel.textColor.isNullOrEmpty() && oppoLabel.textColor.startsWith("#")){
                holder.binding.tvReqPhase.setTextColor(Color.parseColor(oppoLabel.textColor))
            }else{
                holder.binding.tvReqPhase.setTextColor(ResUtils.getColorFromResource(R.color.COLOR_4A4C5C))
            }
            holder.binding.tvReqPhase.visible()
        }else{
            holder.binding.tvReqPhase.gone()
        }
        val orderLabel = FormatUtils.formatOrderLabelWithDispatch(item?.reqPhase, item?.orderStatus)
        if (orderLabel!=null){
            holder.binding.tvOrderStatus.text = orderLabel.text
            if (!orderLabel.bgColor.isNullOrEmpty() && orderLabel.bgColor.startsWith("#")){
                val labelDrawable = ResUtils.getImageFromResource(R.drawable.bg_round_label_top_half_solid) as GradientDrawable
                labelDrawable.setColor(Color.parseColor(orderLabel.bgColor))
                holder.binding.tvOrderStatus.background = labelDrawable
            }else{
                holder.binding.tvOrderStatus.background = null
            }
            if (!orderLabel.textColor.isNullOrEmpty() && orderLabel.textColor.startsWith("#")){
                holder.binding.tvOrderStatus.setTextColor(Color.parseColor(orderLabel.textColor))
            }else{
                holder.binding.tvOrderStatus.setTextColor(ResUtils.getColorFromResource(R.color.COLOR_4A4C5C))
            }
            holder.binding.tvOrderStatus.visible()
        }else{
            holder.binding.tvOrderStatus.gone()
        }
        holder.binding.kvlOpportunity.setData(item?.toShowArray())
        val buttons = item?.genBottomButtons()
        holder.binding.btnCall.text = buttons?.get(0)?.name
        holder.binding.btnRight.text = buttons?.get(1)?.name
        holder.binding.btnRight.click {
            onButtonActionListener?.invoke(item?.id!!,
                item.customerId ?:"", item.orderStatus,buttons?.get(1)?.type?:-1)
        }
        holder.binding.btnCall.click {
            onButtonActionListener?.invoke(item?.customerId.toString(),item?.customerId?:"",item?.orderStatus,buttons?.get(0)?.type?:-1)
        }

    }

}