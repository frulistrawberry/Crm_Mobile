package com.baihe.lib_common.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_common.R
import com.baihe.lib_common.databinding.CommonItemCustomerListReqBinding
import com.baihe.lib_common.entity.ReqInfoEntity
import com.baihe.lib_common.entity.StatusText.Mode
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.ext.ViewExt.invisible
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.utils.DpToPx
import com.baihe.lib_framework.utils.ResUtils

class ReqListAdapter:
    BaseRecyclerViewAdapter<ReqInfoEntity, CommonItemCustomerListReqBinding>() {
    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<CommonItemCustomerListReqBinding>,
        item: ReqInfoEntity?,
        position: Int
    ) {
        holder.binding.tvTitle.text = item?.title?:""
        val stringBuilder = StringBuilder().apply {
            if (!item?.reqOwner.isNullOrEmpty()&&!item?.orderOwner.isNullOrEmpty()){
                append("""
                    邀约-${item?.reqOwner}
                    销售-${item?.orderOwner}
                """.trimIndent())
            }else if (!item?.orderOwner.isNullOrEmpty()){
                append("销售-${item?.orderOwner}")
            }else if (!item?.reqOwner.isNullOrEmpty()){
                append("邀约-${item?.reqOwner}")
            }

        }
        holder.binding.tvOwner.text = stringBuilder
        val statusText = item?.getPhaseLabel()
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
            // TODO: 邀约详情
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


