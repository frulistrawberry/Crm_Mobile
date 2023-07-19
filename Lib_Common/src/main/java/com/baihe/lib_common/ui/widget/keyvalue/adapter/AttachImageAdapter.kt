package com.baihe.lib_common.ui.widget.keyvalue.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baihe.imageloader.ImageLoaderUtils
import com.baihe.lib_common.databinding.LayoutKeyvalueItemAttachBinding
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.manager.AppManager
import com.baihe.lib_framework.utils.DpToPx
import com.baihe.lib_framework.utils.ViewUtils

class AttachImageAdapter(val mode:Int = 1): BaseRecyclerViewAdapter<String, LayoutKeyvalueItemAttachBinding>() {

    companion object{
        const val MODE_EDIT = 1
        const val MODE_VIEW = 2
    }

    var spanCount = 4


    init {

    }
    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): LayoutKeyvalueItemAttachBinding {
        return LayoutKeyvalueItemAttachBinding.inflate(layoutInflater,parent,false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<LayoutKeyvalueItemAttachBinding>,
        item: String?,
        position: Int
    ) {


        holder.binding.imageview.layoutParams.height = holder.binding.imageview.layoutParams.width

        if (mode == MODE_VIEW){
            holder.binding.btnDel.gone()
        }else{
            holder.binding.btnDel.visible()
        }
        holder.binding.btnDel.click {
            getData().removeAt(position)
            notifyDataSetChanged()
        }
        ImageLoaderUtils.getInstance().loadCornerImage(holder.binding.imageview.context,holder.binding.imageview,item,DpToPx.dpToPx(14))
        ViewUtils.setClipViewCornerRadius(holder.binding.imageview,DpToPx.dpToPx(14))

    }

}