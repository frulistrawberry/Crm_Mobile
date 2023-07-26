package com.baihe.lib_contract.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.baihe.lib_common.constant.KeyConstant
import com.baihe.lib_common.constant.RequestCode
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_common.ui.activity.PhotoPickActivity
import com.baihe.lib_common.ui.dialog.AlertDialog
import com.baihe.lib_common.ui.widget.keyvalue.KeyValueEditLayout
import com.baihe.lib_common.ui.widget.keyvalue.KeyValueLayout.OnItemActionListener
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_contract.ContractViewModel
import com.baihe.lib_contract.databinding.ContractActivityAddOrUpdateContractBinding
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.log.LogUtil
import com.dylanc.loadingstateview.ViewType
import com.google.gson.Gson

class AddOrUpdateContractActivity:
    BaseMvvmActivity<ContractActivityAddOrUpdateContractBinding, ContractViewModel>() {
    private val orderId:String? by lazy {
        intent.getStringExtra(KeyConstant.KEY_ORDER_ID)
    }

    private val contractId:String? by lazy {
        intent.getStringExtra(KeyConstant.KEY_CONTRACT_ID)
    }

    private val needPreview by lazy {
        intent.getBooleanExtra(KeyConstant.KEY_CONTRACT_NEED_PREVIEW,true)
    }



    companion object{
        @JvmStatic
        fun start(act: Fragment, orderId:String,contractId:String?=null,needPreview:Boolean = true){
            val intent = Intent(act.requireContext(),AddOrUpdateContractActivity::class.java).apply {
                putExtra(KeyConstant.KEY_ORDER_ID,orderId)
                putExtra(KeyConstant.KEY_CONTRACT_ID,contractId)
                putExtra(KeyConstant.KEY_CONTRACT_NEED_PREVIEW,needPreview)
            }
            act.startActivityForResult(intent, RequestCode.REQUEST_ADD_CONTRACT)
        }

        @JvmStatic
        fun start(act: Activity, orderId:String,contractId:String?=null,needPreview:Boolean = true){
            val intent = Intent(act,AddOrUpdateContractActivity::class.java).apply {
                putExtra(KeyConstant.KEY_ORDER_ID,orderId)
                putExtra(KeyConstant.KEY_CONTRACT_ID,contractId)
                putExtra(KeyConstant.KEY_CONTRACT_NEED_PREVIEW,needPreview)
            }
            act.startActivityForResult(intent, RequestCode.REQUEST_ADD_CONTRACT)
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
        mViewModel.loadingDialogLiveData.observe(this){
            if (it)
                showLoadingDialog()
            else
                dismissLoadingDialog()
        }
        mViewModel.tempLiveData.observe(this){
            mBinding.kvlContract.data = it.contract
            mBinding.kvlInfoAdditional.data = it.contractInfo
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
            title = if (contractId.isNullOrEmpty())
                "完成签约"
            else
                "修改合同"
        }
        mBinding.srlRoot.setEnableLoadMore(false)
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnCommit.click {
            val params = mBinding.kvlContract.commit()
            params?.let {
                val paramsAdditional = mBinding.kvlInfoAdditional.commit()
                paramsAdditional?.let {
                   params.putAll(paramsAdditional as Map<String,Any>)
               }
                val amount = params["sign_amount"] as String
                val firstAmount = params["first_plan_amount"] as String
                val secondAmount = params["second_plan_amount"] as String
                val thirdAmount = params["third_plan_amount"] as String
                if (amount.toDouble() != firstAmount.toDouble()+secondAmount.toDouble()+thirdAmount.toDouble()){
                    showToast("设置的分期待回款金额合计金额需等于合同金额")
                    return@click
                }
                if (needPreview)
                    PreviewContractActivity.start(this,Gson().toJson(mBinding.kvlContract.data), Gson().toJson(mBinding.kvlInfoAdditional.data),Gson().toJson(params),orderId!!,contractId)
                else{
                    AlertDialog.Builder(this)
                        .setContent("确定已完成签约？")
                        .setOnConfirmListener {
                            var attachmentList = mutableListOf<String>()
                            if (!(params["contract_pic"] as String?).isNullOrEmpty()){
                                attachmentList = (params["contract_pic"] as String).split(",").toMutableList()
                            }
                            mViewModel.addOrUpdateContract(params,orderId,contractId,attachmentList)
                        }.create().show()

                }

            }

        }
        mBinding.srlRoot.setOnRefreshListener {
            mViewModel.getContractTemp(orderId,contractId)
        }

        mBinding.kvlContract.setOnItemActionListener(object :
            KeyValueEditLayout.OnItemActionListener() {
            override fun onEvent(
                keyValueEntity: KeyValueEntity?,
                itemType: KeyValueEditLayout.ItemType?
            ) {
                if (keyValueEntity?.paramKey == "contract_pic"){
                    PhotoPickActivity.start(this@AddOrUpdateContractActivity)
                }
                if (keyValueEntity?.paramKey == "plan_type"){
                    val signAmountKv = mBinding.kvlContract.findEntityByParamKey("sign_amount")
                    val firstPlanAmountKv = mBinding.kvlContract.findEntityByParamKey("first_plan_amount")
                    val secondPlanAmountKv = mBinding.kvlContract.findEntityByParamKey("second_plan_amount")
                    val thirdPlanAmountKv = mBinding.kvlContract.findEntityByParamKey("third_plan_amount")
                    if (!keyValueEntity.value.isNullOrEmpty()){
                        if (keyValueEntity.defaultValue=="自定义"){
                            firstPlanAmountKv?.apply {
                                type = "amount"
                            }
                            secondPlanAmountKv?.apply {
                                type = "amount"
                            }
                            thirdPlanAmountKv?.apply {
                                type = "amount"
                            }
                        }else{
                            firstPlanAmountKv?.apply {
                                type = "readonly"
                                value = ((keyValueEntity.value.toInt()/100%10)*10*signAmountKv.value.toDouble()/100).toString()
                                defaultValue = ((keyValueEntity.value.toInt()/100%10)*10*signAmountKv.value.toDouble()/100).toString()
                            }
                            secondPlanAmountKv?.apply {
                                type = "readonly"
                                value = ((keyValueEntity.value.toInt()/10%10)*10*signAmountKv.value.toDouble()/100).toString()
                                defaultValue = ((keyValueEntity.value.toInt()/10%10)*10*signAmountKv.value.toDouble()/100).toString()
                            }
                            thirdPlanAmountKv?.apply {
                                type = "readonly"
                                value = ((keyValueEntity.value.toInt()%10)*10*signAmountKv.value.toDouble()/100).toString()
                                defaultValue = ((keyValueEntity.value.toInt()%10)*10*signAmountKv.value.toDouble()/100).toString()
                            }

                        }
                        mBinding.kvlContract.data = mBinding.kvlContract.data


                    }
                }
                if (keyValueEntity?.paramKey == "sign_amount"){
                    val planTypeKv = mBinding.kvlContract.findEntityByParamKey("plan_type")
                    val firstPlanAmountKv = mBinding.kvlContract.findEntityByParamKey("first_plan_amount")
                    val secondPlanAmountKv = mBinding.kvlContract.findEntityByParamKey("second_plan_amount")
                    val thirdPlanAmountKv = mBinding.kvlContract.findEntityByParamKey("third_plan_amount")
                    planTypeKv?.apply {
                        value = ""
                        defaultValue = ""
                    }
                    firstPlanAmountKv?.apply {
                        value = ""
                        defaultValue = ""
                    }
                    secondPlanAmountKv?.apply {
                        value = ""
                        defaultValue = ""
                    }
                    thirdPlanAmountKv?.apply {
                        value = ""
                        defaultValue = ""
                    }
                    mBinding.kvlContract.refresh()
                }
            }

        })
        mBinding.kvlContract.setOnItemCheckListener(object :
            KeyValueEditLayout.OnItemCheckListener(){
            override fun onEvent(
                keyValueEntity: KeyValueEntity?,
                itemType: KeyValueEditLayout.ItemType?
            ):Boolean {
                if (keyValueEntity?.paramKey == "plan_type"){
                    val signAmountKv = mBinding.kvlContract.findEntityByParamKey("sign_amount")
                    if (signAmountKv?.value.isNullOrEmpty()){
                        showToast("请输入合同金额")
                        return false
                    }
                }

                return true
            }

        })
    }

    override fun initData() {
        super.initData()
        mViewModel.getContractTemp(orderId,contractId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.REQUEST_ADD_CONTRACT && resultCode == RESULT_OK){
            setResult(RESULT_OK)
            finish()
        }
        if (requestCode == RequestCode.REQUEST_PHOTO && resultCode == RESULT_OK){
            data?.let {
                val newImageList = data.getStringArrayListExtra(KeyConstant.KEY_PHOTOS)
                val imageList = mutableListOf<String>()
                 mBinding.kvlContract.findEntityByParamKey("contract_pic")?.value?.split(",")?.forEach {
                     imageList.add(it)
                 }
                imageList.addAll(newImageList)
                val imagePaths = StringBuilder()
                imageList.forEach {
                    imagePaths.append(it)
                    if (imageList.indexOf(it)<imageList.size-1)
                        imagePaths.append(",")
                }
                val keyValueEntity = mBinding.kvlContract.findEntityByParamKey("contract_pic")
                keyValueEntity?.let {
                    keyValueEntity.value = imagePaths.toString()
                    keyValueEntity.attach = imageList
                    mBinding.kvlContract.refreshItem(keyValueEntity)
                }
            }
        }
    }

}