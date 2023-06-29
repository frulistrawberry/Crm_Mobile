package com.baihe.lib_customer.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.adapter.ReqListAdapter
import com.baihe.lib_customer.CustomerListItemEntity
import com.baihe.lib_customer.databinding.CustomerItemCustomerListBinding
import com.baihe.lib_framework.adapter.BaseBindViewHolder
import com.baihe.lib_framework.adapter.BaseRecyclerViewAdapter
import com.baihe.lib_framework.ext.ViewExt.click

class CustomerListAdapter(val context: Context):
    BaseRecyclerViewAdapter<CustomerListItemEntity, CustomerItemCustomerListBinding>() {
    var onCallListener:((customerId:String)->Unit)? = null

    @SuppressLint("SetTextI18n")
    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<CustomerItemCustomerListBinding>,
        item: CustomerListItemEntity?,
        position: Int
    ) {
        holder.binding.tvTitle.text = "${item?.name}  |  ${item?.phone}"
        holder.binding.tvFlowOppo.text = item?.reqCount.toString()
        holder.binding.tvFlowOrder.text = item?.orderCount.toString()
        holder.binding.tvFinishOrder.text = item?.finishOrderCount.toString()
        holder.binding.rvReq.apply {
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter = ReqListAdapter().apply {
                if (!item?.reqInfo.isNullOrEmpty()){
                    if (item!!.reqInfo!!.size>2){
                        setData(item.reqInfo!!.subList(0,2))
                    }else{
                        setData(item.reqInfo)
                    }
                }else{
                    setData(null)
                }
            }


        }
        holder.binding.btnCall.click {
            onCallListener?.invoke(item?.id.toString())
        }
    }

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): CustomerItemCustomerListBinding {
        return CustomerItemCustomerListBinding.inflate(layoutInflater,parent,false)
    }
}