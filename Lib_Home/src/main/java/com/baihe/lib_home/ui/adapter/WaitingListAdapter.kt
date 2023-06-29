package com.baihe.lib_home.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.helper.AppHelper
import com.baihe.lib_home.R
import com.baihe.lib_home.WaitingEntity
import com.baihe.lib_home.databinding.HomeItemWaitingListBinding

class WaitingListAdapter(private val context:Context): BaseRecyclerViewAdapter<WaitingEntity, HomeItemWaitingListBinding>() {
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<HomeItemWaitingListBinding>,
        item: WaitingEntity?,
        position: Int) {
        holder.binding.tvTitle.text = item?.name
        if (item?.type == 4)
            holder.binding.tvOverdue.visible()
        else
            holder.binding.tvOverdue.gone()
        val statusText = item?.toStatusText()
        if (statusText!=null){
            val labelBg = statusText.bgColor
            if (!labelBg.isNullOrEmpty() && labelBg.startsWith("#")){
                val labelDrawable = AppHelper.getApplication().resources.getDrawable(R.drawable.bg_round_label_solid) as GradientDrawable
                labelDrawable.setColor(Color.parseColor(labelBg))
                holder.binding.tvTag.background = labelDrawable
            }else{
                holder.binding.tvTag.background = null
            }
            holder.binding.tvTag.text = statusText.text
            holder.binding.tvTag.setTextColor(Color.parseColor(statusText.textColor))
            holder.binding.tvTag.visible()
        }else{
            holder.binding.tvTag.gone()
        }
        if (item?.tag == "1")
            holder.binding.tvTip.visible()
        else
            holder.binding.tvTip.gone()
        holder.binding.kvlWaiting.setData(item?.showArray())






    }

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): HomeItemWaitingListBinding {
        return HomeItemWaitingListBinding.inflate(layoutInflater,parent,false)
    }
}