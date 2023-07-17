package com.baihe.lib_contract.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.baihe.lib_common.constant.KeyConstant
import com.baihe.lib_contract.ContractViewModel
import com.baihe.lib_contract.databinding.ContractActivityContractDetailBinding
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.log.LogUtil
import com.dylanc.loadingstateview.ViewType

class ContractDetailActivity:
    BaseMvvmActivity<ContractActivityContractDetailBinding, ContractViewModel>() {
    private val contractId: String by lazy {
        intent.getStringExtra(KeyConstant.KEY_CONTRACT_ID)
    }
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
                    showLoadingView()
                }
                ViewType.CONTENT -> {
                    showContentView()
                }
                ViewType.EMPTY -> {
                    showEmptyView()
                }
                ViewType.ERROR -> {
                    showErrorView()
                }
                else -> LogUtil.d(it.name)
            }
        }
        mViewModel.detailLiveData.observe(this){ detailEntity ->
            detailEntity?.let {
                mBinding.kvlContract.setData(it.showArray())
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "合同详情"
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getContractDetail(contractId)
    }
}