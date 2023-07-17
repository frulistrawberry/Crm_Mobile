package com.baihe.lib_common.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_common.R
import com.baihe.lib_common.databinding.CommonItemCustomerListReqBinding
import com.baihe.lib_common.entity.ReqInfoEntity
import com.baihe.lib_common.entity.StatusText.Mode
import com.baihe.lib_common.provider.OpportunityServiceProvider
import com.baihe.lib_common.utils.FormatUtils
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.invisible
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.utils.DpToPx
import com.baihe.lib_framework.utils.ResUtils

class ReqListAdapter(val context: Context):
    BaseRecyclerViewAdapter<ReqInfoEntity, CommonItemCustomerListReqBinding>() {
    @SuppressLint("SetTextI18n")
    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<CommonItemCustomerListReqBinding>,
        item: ReqInfoEntity?,
        position: Int
    ) {
        holder.binding.tvTitle.text = item?.title?:""

        if (item?.reqOwner.isNullOrEmpty()){
            holder.binding.tvReq.gone()
        }else{
            holder.binding.tvReq.text = "邀约-${item?.reqOwner}"
            holder.binding.tvReq.visible()
        }
        if (item?.orderOwner.isNullOrEmpty()){
            holder.binding.tvOrder.gone()
        }else{
            holder.binding.tvOrder.text = "销售-${item?.orderOwner}"
            holder.binding.tvOrder.visible()
        }
        val statusText = FormatUtils.formatOppoLabel(item?.phase)
        if (statusText!=null){
            val labelBg = statusText.bgColor
            if (!labelBg.isNullOrEmpty() && labelBg.startsWith("#")){

                if (statusText.mode == Mode.FILL){
                    val labelDrawable = ResUtils.getImageFromResource(R.drawable.bg_round_label_solid) as GradientDrawable
                    labelDrawable.setColor(Color.parseColor(labelBg))
                    holder.binding.tvPhase.background = labelDrawable
                }

                else{
                    val labelDrawable = ResUtils.getImageFromResource(R.drawable.bg_round_label_stroke) as GradientDrawable
                    labelDrawable.setStroke(DpToPx.dpToPx(0.5f).toInt(),Color.parseColor(labelBg))
                    holder.binding.tvPhase.background = labelDrawable
                }


            }else{
                holder.binding.tvPhase.background = null
            }
            holder.binding.tvPhase.text = statusText.text
            holder.binding.tvPhase.setTextColor(Color.parseColor(statusText.textColor))
            holder.binding.tvPhase.visible()
        }else{
            holder.binding.tvPhase.invisible()
        }
        onItemClickListener = {_,_ ->
            item?.let {
                OpportunityServiceProvider.toOpportunityDetail(context,item.id)
            }

        }
    }

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CommonItemCustomerListReqBinding {
        return CommonItemCustomerListReqBinding.inflate(layoutInflater,parent,false)
    }
}


