package com.baihe.lib_customer.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.ui.adapter.ReqListAdapter
import com.baihe.lib_customer.CustomerListItemEntity
import com.baihe.lib_customer.R
import com.baihe.lib_customer.databinding.CustomerItemCustomerListBinding
import com.baihe.lib_customer.databinding.CustomerItemSelectCustomerBinding
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.ext.ViewExt.click

class CustomerSelectAdapter(val context: Context):
    BaseRecyclerViewAdapter<CustomerListItemEntity, CustomerItemSelectCustomerBinding>() {

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<CustomerItemSelectCustomerBinding>,
        item: CustomerListItemEntity?,
        position: Int
    ) {
        holder.binding.tvTitle.text = "${item?.name}  |  ${item?.phone}"
        if (item?.isCheck == true){
            holder.binding.cbCustomer.setImageResource(R.mipmap.ic_check)
        }else{
            holder.binding.cbCustomer.setImageResource(R.mipmap.ic_uncheck)

        }

        holder.binding.kvlCustomer.setData(item?.basicShowArray())
//        holder.binding.cbCustomer.setOnCheckedChangeListener { _, isCheck ->
//            for (datum in getData()) {
//                datum.isCheck = false
//            }
//            item?.isCheck = isCheck
//
//        }

    }

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CustomerItemSelectCustomerBinding {
        return CustomerItemSelectCustomerBinding.inflate(layoutInflater,parent,false)
    }


}