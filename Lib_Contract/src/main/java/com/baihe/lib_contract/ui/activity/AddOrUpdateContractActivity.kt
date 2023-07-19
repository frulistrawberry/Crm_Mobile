package com.baihe.lib_contract.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.baihe.lib_common.constant.KeyConstant
import com.baihe.lib_common.constant.RequestCode
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_contract.ContractViewModel
import com.baihe.lib_contract.databinding.ContractActivityAddOrUpdateContractBinding
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.log.LogUtil
import com.dylanc.loadingstateview.ViewType

class AddOrUpdateContractActivity:
    BaseMvvmActivity<ContractActivityAddOrUpdateContractBinding, ContractViewModel>() {
    //    private val orderId:String? by lazy {
//        intent.getStringExtra(KeyConstant.KEY_ORDER_ID)
//    }
//
//    private val contractId:String? by lazy {
//        intent.getStringExtra(KeyConstant.KEY_CONTRACT_ID)
//    }
//
//    private var customerId:String? = null
//
//
//    companion object{
//        @JvmStatic
//        fun start(context: Context, orderId:String?=null,){
//            val intent = Intent(context,AddOrUpdateContractActivity::class.java).apply {
//                putExtra(KeyConstant.KEY_ORDER_ID,orderId)
//            }
//            if (context is Activity)
//                context.startActivityForResult(intent, RequestCode.REQUEST_ADD_CONTRACT)
//            else
//                context.startActivity(intent)
//        }
//    }
//    override fun initViewModel() {
//        super.initViewModel()
//        mViewModel.loadingStateLiveData.observe(this){
//            when(it){
//                ViewType.LOADING ->{
//                    if (!mBinding.srlRoot.isRefreshing)
//                        showLoadingView()
//                }
//                ViewType.CONTENT -> {
//                    showContentView()
//                    mBinding.srlRoot.finishRefresh()
//                }
//                ViewType.EMPTY -> {
//                    showEmptyView()
//                    mBinding.srlRoot.finishRefresh()
//                }
//                ViewType.ERROR -> {
//                    showErrorView()
//                    mBinding.srlRoot.finishRefresh()
//                }
//                else -> LogUtil.d(it.name)
//            }
//        }
//        mViewModel.loadingDialogLiveData.observe(this){
//            if (it)
//                showLoadingDialog()
//            else
//                dismissLoadingDialog()
//        }
//        mViewModel.tempLiveData.observe(this){
//            mBinding.kvlOpportunity.data = it
//
//
//        }
//
//
//        mViewModel.oppoAddOrUpdateLiveData.observe(this){
//            if (it){
//                setResult(RESULT_OK)
//                finish()
//            }
//        }
//    }
//
//
//    override fun initView(savedInstanceState: Bundle?) {
//        setToolbar {
//            title = if (oppoId.isNullOrEmpty())
//                "新增销售机会"
//            else
//                "编辑销售机会"
//        }
//        mBinding.srlRoot.setEnableLoadMore(false)
//    }
//
//    override fun initListener() {
//        super.initListener()
//        mBinding.btnCommit.click {
//            val params = mBinding.kvlOpportunity.commit()
//            if (params!=null)
//                mViewModel.addOrUpdateOpportunity(params,oppoId,customerId)
//        }
//        mBinding.srlRoot.setOnRefreshListener(){
//            mViewModel.getOppoTemple(oppoId,customerId)
//        }
//    }
//
//    override fun initData() {
//        super.initData()
//        customerId = intent.getStringExtra(KeyConstant.KEY_CUSTOMER_ID)
//        mViewModel.getContractList(oppoId,customerId)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RequestCode.REQUEST_SELECT_CUSTOMER && resultCode == RESULT_OK){
//            customerId = data?.let {
//                data.getStringExtra(KeyConstant.KEY_CUSTOMER_ID)
//            }
//            customerId?.let {
//                mViewModel.getCustomerInfo(it)
//            }
//
//
//        }
//    }
    override fun initView(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }
}