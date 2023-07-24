package com.baihe.lib_contract.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.baihe.lib_common.constant.KeyConstant
import com.baihe.lib_common.constant.KeyConstant.KEY_CONTRACT_DATA
import com.baihe.lib_common.constant.KeyConstant.KEY_CONTRACT_ID
import com.baihe.lib_common.constant.KeyConstant.KEY_ORDER_ID
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_contract.ContractDetailEntity
import com.baihe.lib_contract.ContractViewModel
import com.baihe.lib_contract.databinding.ContractActivityContractDetailBinding
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.gone
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class PreviewContractActivity:
    BaseMvvmActivity<ContractActivityContractDetailBinding, ContractViewModel>() {

    val contractInfo: ContractDetailEntity by lazy {
        val json = intent.getStringExtra(KEY_CONTRACT_DATA)
        Gson().fromJson(json,ContractDetailEntity::class.java)
    }

    val params:LinkedHashMap<String,Any?>  by lazy {
        val type: Type = object : TypeToken<LinkedHashMap<String, Any?>>() {}.type
        val json = intent.getStringExtra(KEY_CONTRACT_DATA)
        Gson().fromJson(json,type)
    }
    val orderId by lazy {
        intent.getStringExtra(KEY_ORDER_ID)
    }
    val contractId by lazy {
        intent.getStringExtra(KEY_CONTRACT_ID)
    }

    companion object{
        fun start(context: Context,orderId:String, contractInfo:String,contractId:String?=null){
            context.startActivity(Intent(context,PreviewContractActivity::class.java).apply {
                putExtra(KEY_CONTRACT_DATA,contractInfo)
                putExtra(KEY_ORDER_ID,orderId)
                putExtra(KEY_CONTRACT_ID,contractId)
            })
        }
    }



    override fun initViewModel() {
        super.initViewModel()
        mViewModel.loadingDialogLiveData.observe(this){
            if (it){
                showLoadingDialog()
            }else{
                dismissLoadingDialog()
            }
        }

        mViewModel.resultLiveData.observe(this){
            if (it){
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "合同预览"
        }
        mBinding.btnCommit.text = "确认提交"
        mBinding.srlRoot.setEnableLoadMore(false)
        mBinding.srlRoot.setEnableRefresh(false)
        mBinding.llBasic.gone()
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnCommit.click {
            var attachmentList = mutableListOf<String>()
            if (!contractInfo.contract_pic.isNullOrEmpty()){
                attachmentList = contractInfo.contract_pic!!.split(",").toMutableList()
            }
            mViewModel.addOrUpdateContract(params,orderId,contractId,attachmentList)

        }
    }

    override fun initData() {
        super.initData()
        mBinding.kvlContract.setData(contractInfo.showArray())
        mBinding.kvlInfoAdditional.setData(contractInfo.additionalShowArray())
    }



}