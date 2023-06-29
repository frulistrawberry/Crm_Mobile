package com.baihe.lib_user.dialog

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_common.widget.font.FontStyle
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_user.BossSeaEntity
import com.baihe.lib_user.databinding.UserBossSeaItemBinding

class BossSeaListAdapter: BaseRecyclerViewAdapter<BossSeaEntity, UserBossSeaItemBinding>() {
    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): UserBossSeaItemBinding {
        return UserBossSeaItemBinding.inflate(layoutInflater,parent,false)
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<UserBossSeaItemBinding>,
        item: BossSeaEntity?,
        position: Int
    ) {
        if (UserServiceProvider.getCompanyId() == item?.id){
            holder.binding.tvCompanyName.fontStyle = FontStyle.HALF_BOLD
            holder.binding.tvCompanyName.setTextColor(Color.parseColor("#687FFF"))
        }else{
            holder.binding.tvCompanyName.fontStyle = FontStyle.NORMAL
            holder.binding.tvCompanyName.setTextColor(Color.parseColor("#4A4C5C"))

        }
        holder.binding.tvCompanyName.text = item?.name?:"未知"
    }
}