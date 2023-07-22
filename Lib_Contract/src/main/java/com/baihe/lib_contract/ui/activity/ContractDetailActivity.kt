package com.baihe.lib_contract.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.baihe.lib_common.constant.KeyConstant
import com.baihe.lib_contract.ContractViewModel
import com.baihe.lib_contract.databinding.ContractActivityContractDetailBinding
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.log.LogUtil
import com.dylanc.loadingstateview.ViewType
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

class ContractDetailActivity:
    BaseMvvmActivity<ContractActivityContractDetailBinding, ContractViewModel>() {
    private val contractId: String by lazy {
        intent.getStringExtra(KeyConstant.KEY_CONTRACT_ID)
    }
    private var orderId:String?=null
    companion object{
        fun start(context: Context, orderId:String){
            context.startActivity(Intent(context,ContractDetailActivity::class.java).apply {
                putExtra(KeyConstant.KEY_CONTRACT_ID,orderId)
            })
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
                    if (mBinding.srlRoot.isRefreshing)
                        mBinding.srlRoot.finishRefresh()
                    showContentView()
                }
                ViewType.EMPTY -> {
                    if (mBinding.srlRoot.isRefreshing)
                        mBinding.srlRoot.finishRefresh()
                    showEmptyView()
                }
                ViewType.ERROR -> {
                    if (mBinding.srlRoot.isRefreshing)
                        mBinding.srlRoot.finishRefresh()
                    showErrorView()
                }
                else -> LogUtil.d(it.name)
            }
        }
        mViewModel.detailLiveData.observe(this){ detailEntity ->
            detailEntity?.let {
                orderId = detailEntity.order_id
                mBinding.kvlContract.setData(it.showArray())
                mBinding.kvlInfoAdditional.setData(it.additionalShowArray())
                mBinding.kvlInfoBasic.setData(it.basicShowArray())
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "合同详情"
        }
        mBinding.srlRoot.setEnableLoadMore(false)
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnCommit.click {
            AddOrUpdateContractActivity.start(this,orderId!!,contractId)
        }
        mBinding.srlRoot.setOnRefreshListener(OnRefreshListener {
            mViewModel.getContractDetail(contractId)
        })
    }

    override fun initData() {
        super.initData()
        mViewModel.getContractDetail(contractId)
    }
}