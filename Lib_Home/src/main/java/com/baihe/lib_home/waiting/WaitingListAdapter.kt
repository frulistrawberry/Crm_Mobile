package com.baihe.lib_home.waiting

import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_home.WaitingEntity
import com.baihe.lib_home.databinding.HomeItemWaitingListBinding

class WaitingListAdapter: BaseRecyclerViewAdapter<WaitingEntity, HomeItemWaitingListBinding>() {
    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<HomeItemWaitingListBinding>,
        item: WaitingEntity?,
        position: Int) {

    }

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): HomeItemWaitingListBinding {
        return HomeItemWaitingListBinding.inflate(layoutInflater,parent,false)
    }
}