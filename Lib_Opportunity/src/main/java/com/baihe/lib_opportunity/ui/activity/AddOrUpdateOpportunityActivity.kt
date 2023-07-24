package com.baihe.lib_opportunity.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.baihe.lib_common.constant.KeyConstant
import com.baihe.lib_common.constant.KeyConstant.KEY_CUSTOMER_ID
import com.baihe.lib_common.constant.KeyConstant.KEY_OPPO_ID
import com.baihe.lib_common.constant.KeyConstant.KEY_TITLE
import com.baihe.lib_common.constant.RequestCode
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_opportunity.OpportunityViewModel
import com.baihe.lib_opportunity.R
import com.baihe.lib_opportunity.databinding.OppoActivityAddOrUpdateOpportunityBinding
import com.dylanc.loadingstateview.ViewType

class AddOrUpdateOpportunityActivity:
    BaseMvvmActivity<OppoActivityAddOrUpdateOpportunityBinding, OpportunityViewModel>() {
    private val oppoId:String? by lazy {
        intent.getStringExtra(KEY_OPPO_ID)
    }

    private val title:String? by lazy {
        intent.getStringExtra(KEY_TITLE)
    }

    private var customerId:String? = null


    companion object{
        @JvmStatic
        fun start(context: Context, oppoId:String?=null
                  ,customerId:String?=null,title:String?=null){
            val intent = Intent(context,AddOrUpdateOpportunityActivity::class.java).apply {
                putExtra(KEY_OPPO_ID,oppoId)
                putExtra(KEY_CUSTOMER_ID,customerId)
                putExtra(KEY_TITLE,title)
            }
            if (context is Activity)
                context.startActivityForResult(intent, RequestCode.REQUEST_ADD_CUSTOMER)
            else
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
        mViewModel.loadingDialogLiveData.observe(this){
            if (it)
                showLoadingDialog()
            else
                dismissLoadingDialog()
        }
        mViewModel.oppoTempleLiveData.observe(this){
            mBinding.kvlOpportunity.data = it
            val kvItem = mBinding.kvlOpportunity.findEntityByParamKey("followUserId")
            if (kvItem!=null){
                if (kvItem.value.isNullOrEmpty()){
                    kvItem.value = UserServiceProvider.getUserId()
                }
                if (kvItem.defaultValue.isNullOrEmpty()){
                    kvItem.defaultValue = UserServiceProvider.getUser()?.name
                }
                mBinding.kvlOpportunity.refreshItem(kvItem)
            }


        }

        mViewModel.customerLiveData.observe(this){
            it?.let {customer->
                mBinding.kvlOpportunity.data?.let {
                    it.forEach {keyValueEntity ->
                        when(keyValueEntity.name){
                            "来源渠道"-> {
                                customer.let { customerDetailEntity ->
                                    keyValueEntity.is_channge = "1"
                                    keyValueEntity.value = customerDetailEntity.sourceChannelId
                                    keyValueEntity.defaultValue= customerDetailEntity.sourceChannel
                                }
                            }
                            "客户姓名" ->{
                                customer.let { customerDetailEntity ->
                                    keyValueEntity.is_channge = "1"
                                    keyValueEntity.value = customerDetailEntity.name
                                    keyValueEntity.defaultValue= customerDetailEntity.name
                                }
                            }
                            "联系方式" ->{
                                customer.let { customerDetailEntity ->
                                    keyValueEntity.is_channge = "1"
                                    keyValueEntity.value = "${customerDetailEntity.see_phone?:""},${customerDetailEntity.wechat?:""}"
                                    keyValueEntity.defaultValue= "${customerDetailEntity.phone?:""},${customerDetailEntity.wechat?:""}"
                                }
                            }
                            "提供人" ->{
                                customer.let { customerDetailEntity ->
                                    keyValueEntity.is_channge = "2"
                                    keyValueEntity.channelId = customerDetailEntity.sourceChannelId
                                    keyValueEntity.value = customerDetailEntity.recordUserId
                                    keyValueEntity.defaultValue= customerDetailEntity.recordUser
                                }
                            }
                            "客户身份"->{
                                customer.let { customerDetailEntity ->
                                    keyValueEntity.is_channge = "2"
                                    keyValueEntity.value = customerDetailEntity.identityId
                                    keyValueEntity.defaultValue= customerDetailEntity.identity
                                }
                            }

                        }
                    }
                }
                mBinding.kvlOpportunity.refresh()
            }
        }

        mViewModel.oppoAddOrUpdateLiveData.observe(this){
            if (it){
                setResult(RESULT_OK)
                finish()
            }
        }
    }


    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = if (oppoId.isNullOrEmpty())
                "新增销售机会"
            else
                "编辑销售机会"
            if (!title.isNullOrEmpty())
                title
        }
        mBinding.srlRoot.setEnableLoadMore(false)
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnCommit.click {
            val params = mBinding.kvlOpportunity.commit()
            if (params!=null)
                mViewModel.addOrUpdateOpportunity(params,oppoId,customerId)
        }
        mBinding.srlRoot.setOnRefreshListener(){
            mViewModel.getOppoTemple(oppoId,customerId)
        }
    }

    override fun initData() {
        super.initData()
        customerId = intent.getStringExtra(KEY_CUSTOMER_ID)
        mViewModel.getOppoTemple(oppoId,customerId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.REQUEST_SELECT_CUSTOMER && resultCode == RESULT_OK){
            customerId = data?.let {
                    data.getStringExtra(KEY_CUSTOMER_ID)
            }
            customerId?.let {
                mViewModel.getCustomerInfo(it)
            }


        }
    }
}