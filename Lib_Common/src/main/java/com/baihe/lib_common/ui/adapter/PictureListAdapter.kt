package com.baihe.lib_common.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.imageloader.ImageLoaderUtils
import com.baihe.lib_common.R
import com.baihe.lib_common.databinding.ItemPhotoListBinding
import com.baihe.lib_common.entity.LocalPhotoEntity
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.manager.AppManager
import com.baihe.lib_framework.utils.DpToPx

@SuppressLint("NotifyDataSetChanged")
class PictureListAdapter: BaseRecyclerViewAdapter<LocalPhotoEntity, ItemPhotoListBinding>() {

    val selectPhotos = mutableListOf<LocalPhotoEntity>()

    var maxSize:Int = 9

    init {
        onItemClickListener = { _, position ->
            val selectItem = getItem(position)
            selectItem?.let {
                if (selectPhotos.contains(selectItem)){
                    selectPhotos.remove(selectItem)
                    notifyItemChanged(position)
                }else{
                    if (selectPhotos.size<maxSize){
                        selectPhotos.add(selectItem)
                        notifyItemChanged(position)
                    }else{
                        notifyDataSetChanged()
                    }


                }
            }


        }

    }



    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): ItemPhotoListBinding {
        return ItemPhotoListBinding.inflate(layoutInflater,parent,false)
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<ItemPhotoListBinding>,
        item: LocalPhotoEntity?,
        position: Int
    ) {
        holder.binding.ivPhoto.layoutParams?.let {
            it.width = (AppManager.getScreenWidthPx()-DpToPx.dpToPx(16))/4
            it.height = (AppManager.getScreenWidthPx()-DpToPx.dpToPx(16))/4
        }
        holder.binding.vMask.layoutParams?.let {
            it.width = (AppManager.getScreenWidthPx()-DpToPx.dpToPx(16))/4
            it.height = (AppManager.getScreenWidthPx()-DpToPx.dpToPx(16))/4
        }
        item?.let {
            val path = if (item.photoThumbnailPath.isNullOrEmpty())
                item.photoPath
            else
                item.photoThumbnailPath
            ImageLoaderUtils.getInstance()
                .loadImage(holder.binding.ivPhoto.context,
                    holder.binding.ivPhoto,path)
            if(selectPhotos.contains(item))
                holder.binding.ivCheck.setImageResource(R.mipmap.photo_list_item_select_s)
            else
                holder.binding.ivCheck.setImageResource(R.mipmap.photo_list_item_select_n)
            if (selectPhotos.size<maxSize)
                holder.binding.vMask.gone()
            else
                holder.binding.vMask.visible()



        }
    }
}