package com.baihe.lib_contract.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.baihe.lib_contract.ContractListItemEntity
import com.baihe.lib_contract.databinding.ContractListItemBinding
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter

class ContractListAdapter:
    BaseRecyclerViewAdapter<ContractListItemEntity, ContractListItemBinding>() {
    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): ContractListItemBinding {
        return ContractListItemBinding.inflate(layoutInflater,parent,false)
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<ContractListItemBinding>,
        item: ContractListItemEntity?,
        position: Int
    ) {
        item?.let {
            holder.binding.tvAmount.text = item.sign_amount
            holder.binding.tvTitle.text = item.contract_alias
            holder.binding.tvNum.text = "合同编号：${item.system_no}"
            holder.binding.kvlContract.setData(item.showArray())
        }

    }
}