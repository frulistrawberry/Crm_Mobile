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



    companion object{
        @JvmStatic
        fun start(act: Fragment, orderId:String,contractId:String?=null){
            val intent = Intent(act.requireContext(),AddOrUpdateContractActivity::class.java).apply {
                putExtra(KeyConstant.KEY_ORDER_ID,orderId)
                putExtra(KeyConstant.KEY_CONTRACT_ID,contractId)
            }
            act.startActivityForResult(intent, RequestCode.REQUEST_ADD_CONTRACT)
        }

        @JvmStatic
        fun start(act: Activity, orderId:String,contractId:String?=null){
            val intent = Intent(act,AddOrUpdateContractActivity::class.java).apply {
                putExtra(KeyConstant.KEY_ORDER_ID,orderId)
                putExtra(KeyConstant.KEY_CONTRACT_ID,contractId)
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
            val paramsAdditional = mBinding.kvlInfoAdditional.commit()
            if (paramsAdditional != null) {
                params?.putAll(paramsAdditional as Map<String,Any>)
            }
            PreviewContractActivity.start(this,orderId!!, Gson().toJson(params),contractId)
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
                val imageList = data.getStringArrayListExtra(KeyConstant.KEY_PHOTOS)
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