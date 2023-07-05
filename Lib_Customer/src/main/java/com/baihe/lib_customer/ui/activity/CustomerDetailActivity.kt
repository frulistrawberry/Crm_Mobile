package com.baihe.lib_customer.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.ui.adapter.FollowListAdapter
import com.baihe.lib_common.ui.widget.AxisItemDecoration
import com.baihe.lib_customer.CustomerViewModel
import com.baihe.lib_customer.R
import com.baihe.lib_customer.databinding.CustomerActivityCustomerDetailBinding
import com.baihe.lib_common.ui.adapter.ReqListAdapter
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.log.LogUtil
import com.dylanc.loadingstateview.ViewType

class CustomerDetailActivity:
    BaseMvvmActivity<CustomerActivityCustomerDetailBinding, CustomerViewModel>() {

    private val customerId by lazy {
        intent.getStringExtra("customerId")
    }

    private val reqAdapter by lazy {
        ReqListAdapter()
    }

    private val followAdapter by lazy {
        FollowListAdapter()
    }

    companion object{
        fun start(context: Context,customerId:String){
            val intent = Intent(context,CustomerDetailActivity::class.java)
            intent.apply {
                putExtra("customerId",customerId)
            }
            context.startActivity(intent)
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.loadingStateLiveData.observe(this){
            when(it){
                ViewType.LOADING ->{
                    if (!mBinding.srlRoot.isRefreshing)
                        showLoadingView()
                }
                ViewType.CONTENT -> {
                    showContentView()
                    mBinding.srlRoot.finishRefresh()
                }
                ViewType.EMPTY -> {
                    showEmptyView()
                    mBinding.srlRoot.finishRefresh()
                }
                ViewType.ERROR -> {
                    showErrorView()
                    mBinding.srlRoot.finishRefresh()
                }
                else -> LogUtil.d(it.name)
            }
        }
        mViewModel.customerDetailLiveData.observe(this){
            mBinding.tvName.text = it.name
            if (it.isUpdate)
                mBinding.btnEdit.visible()
            else
                mBinding.btnEdit.gone()
            mBinding.kvlCustomer.setData(it.basicShowArray())
            mBinding.tvFlowOppo.text = it.reqCount.toString()
            mBinding.tvFlowOrder.text = it.orderCount.toString()
            reqAdapter.setData(it.reqInfo)
            if(it.follow.isNullOrEmpty()){
                mBinding.llFollow.gone()
            }else{
                mBinding.llFollow.visible()
                followAdapter.setData(it.follow)
            }


        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "客户详情页"
            navIcon = R.mipmap.navigation_icon
        }

        mBinding.srlRoot.setEnableLoadMore(false)
        mBinding.rvReq.layoutManager = LinearLayoutManager(this)
        mBinding.rvReq.isNestedScrollingEnabled = false
        mBinding.rvReq.adapter = reqAdapter
        mBinding.rvFollow.layoutManager = LinearLayoutManager(this)
        mBinding.rvFollow.addItemDecoration(AxisItemDecoration(this))
        mBinding.rvFollow.isNestedScrollingEnabled = false
        mBinding.rvFollow.adapter = followAdapter

    }

    override fun initListener() {
        super.initListener()
        mBinding.srlRoot.setOnRefreshListener {
            mViewModel.getCustomerDetail(customerId)
        }
        mBinding.btnEdit.click {
            AddOrUpdateCustomerActivity.start(this,customerId)
        }
        reqAdapter.onItemClickListener = { _, position->
            val item = reqAdapter.getData()[position]
        }


    }

    override fun initData() {
        super.initData()
        mViewModel.getCustomerDetail(customerId)
    }
}