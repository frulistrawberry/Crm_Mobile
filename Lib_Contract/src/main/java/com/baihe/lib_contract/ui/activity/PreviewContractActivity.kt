package com.baihe.lib_contract.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.baihe.lib_common.constant.KeyConstant.KEY_ADDITIONAL_DATA
import com.baihe.lib_common.constant.KeyConstant.KEY_CONTRACT_DATA
import com.baihe.lib_common.constant.KeyConstant.KEY_CONTRACT_ID
import com.baihe.lib_common.constant.KeyConstant.KEY_ORDER_ID
import com.baihe.lib_common.constant.KeyConstant.KEY_PARAMS
import com.baihe.lib_common.constant.RequestCode
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_contract.ContractViewModel
import com.baihe.lib_contract.databinding.ContractActivityContractDetailBinding
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.safframework.log.extension.json


class PreviewContractActivity:
    BaseMvvmActivity<ContractActivityContractDetailBinding, ContractViewModel>() {

    private val contractInfo:List<KeyValueEntity> by lazy {
        val json = intent.getStringExtra(KEY_CONTRACT_DATA)
        val type = object : TypeToken<List<KeyValueEntity>>() {}.type
        Gson().fromJson<List<KeyValueEntity>?>(json,type).onEach {
            if (it.paramKey == "contract_pic"){
                it.attach = mutableListOf()
                if (it.value?.contains(",") == true){
                    val urls = it.value.split(",")
                    it.attach.addAll(urls)
                }else if (!it.value.isNullOrEmpty()){
                    it.attach.add(it.value)
                }else{
                    it.attach.clear()
                }
            }

        }
    }
    private val additionalInfo:List<KeyValueEntity> by lazy {
        val json = intent.getStringExtra(KEY_ADDITIONAL_DATA)
        val type = object : TypeToken<List<KeyValueEntity>>() {}.type
        Gson().fromJson(json,type)
    }
    private val params:LinkedHashMap<String,Any?> by lazy {
        val json =  intent.getStringExtra(KEY_PARAMS)
        val type = object : TypeToken<LinkedHashMap<String,Any?>>() {}.type
        Gson().fromJson(json,type)
    }
    private val orderId: String? by lazy {
        intent.getStringExtra(KEY_ORDER_ID)
    }
    private val contractId: String? by lazy {
        intent.getStringExtra(KEY_CONTRACT_ID)
    }

    companion object{
        fun start(context: Activity,contractInfo:String,additionalInfo:String,params:String,orderId:String, contractId:String?=null){
            context.startActivityForResult(Intent(context,PreviewContractActivity::class.java).apply {
                putExtra(KEY_CONTRACT_DATA,contractInfo)
                putExtra(KEY_ADDITIONAL_DATA,additionalInfo)
                putExtra(KEY_PARAMS,params)
                putExtra(KEY_ORDER_ID,orderId)
                putExtra(KEY_CONTRACT_ID,contractId)
            },RequestCode.REQUEST_ADD_CONTRACT)
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
        mBinding.tvTip.visible()
        mBinding.llBasic.gone()
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnCommit.click {
            var attachmentList = mutableListOf<String>()
            if (!(params["contract_pic"] as String?).isNullOrEmpty()){
                attachmentList = (params["contract_pic"] as String).split(",").toMutableList()
            }
            mViewModel.addOrUpdateContract(params,orderId,contractId,attachmentList)
        }
    }

    override fun initData() {
        super.initData()
        mBinding.kvlContract.setData(contractInfo)
        mBinding.kvlInfoAdditional.setData(additionalInfo)
    }



}