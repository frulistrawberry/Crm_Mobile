package com.baihe.lib_order.ui.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_common.R
import com.baihe.lib_common.entity.OpportunityListItemEntity
import com.baihe.lib_common.utils.FormatUtils
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.utils.ResUtils
import com.baihe.lib_order.databinding.OrderOppoSelectItemBinding

class OppoSelectAdapter :
    BaseRecyclerViewAdapter<OpportunityListItemEntity, OrderOppoSelectItemBinding>() {
    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): OrderOppoSelectItemBinding {
        return OrderOppoSelectItemBinding.inflate(layoutInflater,parent,false)
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<OrderOppoSelectItemBinding>,
        item: OpportunityListItemEntity?,
        position: Int
    ) {
        holder.binding.tvTitle.text = item?.title
        if (item?.isCheck == true){
            holder.binding.cbOppo.setImageResource(R.mipmap.ic_check)
        }else{
            holder.binding.cbOppo.setImageResource(R.mipmap.ic_uncheck)

        }
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
        holder.binding.kvlCustomer.setData(item?.toShowArray())


    }
}