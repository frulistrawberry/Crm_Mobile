package com.baihe.lib_order.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.constant.KeyConstant
import com.baihe.lib_common.constant.KeyConstant.KEY_OPPO_PHASE
import com.baihe.lib_common.constant.KeyConstant.KEY_ORDER_TYPE
import com.baihe.lib_common.constant.StatusConstant
import com.baihe.lib_common.databinding.CommonSrlListBinding
import com.baihe.lib_common.entity.ButtonTypeEntity
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_CALL
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_DISPATCH_ORDER
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_FOLLOW
import com.baihe.lib_common.ext.FragmentExt.dismissLoadingDialog
import com.baihe.lib_common.ext.FragmentExt.showLoadingDialog
import com.baihe.lib_common.provider.ContractServiceProvider
import com.baihe.lib_common.provider.OpportunityServiceProvider
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_common.ui.activity.AddFollowActivity
import com.baihe.lib_common.viewmodel.CommonViewModel
import com.baihe.lib_framework.base.BaseMvvmFragment
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_order.ui.OrderViewModel
import com.baihe.lib_order.ui.activity.*
import com.baihe.lib_order.ui.adapter.OrderListAdapter
import com.dylanc.loadingstateview.ViewType

class OrderListFragment: BaseMvvmFragment<CommonSrlListBinding, OrderViewModel>() {
    private val type by lazy {
        arguments?.getString(KEY_ORDER_TYPE,"0")
    }
    private val isCompanyNeedContract by lazy {
        UserServiceProvider.isCompanyNeedContract()
    }
    private val adapter by lazy {
        OrderListAdapter().apply {
            onButtonActionListener = {item, action ->
                when(action){
                    ButtonTypeEntity.ACTION_SIGN->{
                        if (item.order_phase == StatusConstant.ORDER_PHASE_STORE_TO_BE_ENTERED){
                            PreSignActivity.start(requireActivity(), item.req_id,item.order_id)
                        }else if (isCompanyNeedContract){
                            ContractServiceProvider.toAddOrUpdateContract(requireActivity(),item.order_id)
                        }else{
                            SignActivity.start(requireActivity(),item.order_id)
                        }
                    }
                    ButtonTypeEntity.ACTION_CONFIRM_ARRIVAL->{
                        ConfirmIndoorActivity.start(this@OrderListFragment, item.req_id,item.order_id)
                    }
                    ButtonTypeEntity.ACTION_SET_PEOPLE->{
                        AddPeopleActivity.start(this@OrderListFragment,item.order_id)
                    }
                    ButtonTypeEntity.ACTION_TRANSFER_ORDER->{
                        OrderTransferActivity.start(this@OrderListFragment,item.order_id)
                    }
                    ButtonTypeEntity.ACTION_EDIT_ORDER->{
                        OpportunityServiceProvider.toEditOppo(requireContext(), item.req_id, item.customer_id,"编辑订单")
                    }
                    ButtonTypeEntity.ACTION_CHARGE_ORDER->{
                        OrderChargebackActivity.start(this@OrderListFragment,item.order_id)
                    }
                    ButtonTypeEntity.ACTION_EDIT_CONTRACT->{
                    }
                    ACTION_FOLLOW ->{
                        com.baihe.lib_order.ui.activity.AddFollowActivity.start(requireActivity(),
                            item.req_id, item.customer_id,item.order_id)
                    }
                    ACTION_CALL->{
                        commonViewModel.call(item.customer_id)
                    }
                }


            }
            onItemClickListener = { _, position ->
                val item = getItem(position)
                OrderDetailActivity.start(requireContext(),item?.order_id!!)
            }
        }
    }
    private val commonViewModel by lazy {
        ViewModelProvider(this).get(CommonViewModel::class.java)
    }
    var page = 1

    companion object {
        @JvmStatic
        fun newFragment(type:String): OrderListFragment {
            return OrderListFragment().apply {
                val arguments = Bundle()
                arguments.putString(KeyConstant.KEY_ORDER_TYPE,type)
                setArguments(arguments)
            }
        }
    }

    fun refresh(keywords:String = "",params:LinkedHashMap<String,Any?>?=null,order:Int = -1){
        page = 1
        mViewModel.getOrderList(page,type!!,keywords)
    }



    override fun initViewModel() {
        super.initViewModel()
        commonViewModel.loadingDialogLiveData.observe(this){
            if (it){
                showLoadingDialog()
            }else
                dismissLoadingDialog()
        }

        mViewModel.loadingStateLiveData.observe(this){
            when(it){
                ViewType.LOADING ->{
                    if (page == 1 && !mBinding?.srlRoot!!.isRefreshing)
                        showLoadingView()
                }
                ViewType.CONTENT -> {
                    showContentView()
                    mBinding?.srlRoot!!.finishRefresh()
                    mBinding?.srlRoot!!.finishLoadMore()
                }
                ViewType.EMPTY -> {
                    if (page == 1){
                        showEmptyView()
                        mBinding?.srlRoot!!.finishRefresh()
                    }else{
                        mBinding?.srlRoot!!.finishLoadMore()
                    }

                }
                ViewType.ERROR -> {
                    if (page == 1){
                        showErrorView()
                        mBinding?.srlRoot!!.finishRefresh()
                    }else{
                        mBinding?.srlRoot!!.finishLoadMore()
                    }

                }
                else -> LogUtil.d(it.name)
            }
        }
        mViewModel.listLiveData.observe(this){
            if (it.size<10)
                mBinding?.srlRoot!!.setEnableLoadMore(false)
            else
                mBinding?.srlRoot!!.setEnableLoadMore(true)
            if (page == 1){
                adapter.setData(it)
            }else
                adapter.addAll(it)
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mBinding?.srlRoot?.setEnableRefresh(true)
        mBinding?.srlRoot?.setEnableLoadMore(true)
        mBinding?.rvList?.layoutManager = LinearLayoutManager(requireContext())
        mBinding?.rvList?.adapter = adapter
    }
    override fun initListener() {
        super.initListener()
        mBinding?.srlRoot!!.setOnRefreshListener{
            page = 1
            mViewModel.getOrderList(page,type!!)
        }
        mBinding?.srlRoot!!.setOnLoadMoreListener {
            page++
            mViewModel.getOrderList(page,type!!)
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getOrderList(page,type!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK ){
            page = 1
            mViewModel.getOrderList(page,type!!)
        }
    }

}