package com.baihe.lib_opportunity.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.baihe.lib_common.constant.KeyConstant
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
        intent.getStringExtra("oppoId")
    }

    private var customerId:String? = null


    companion object{
        @JvmStatic
        fun start(context: Context, oppoId:String?=null
                  ,customerId:String?=null){
            val intent = Intent(context,AddOrUpdateOpportunityActivity::class.java).apply {
                putExtra("oppoId",oppoId)
                putExtra("customerId",customerId)
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
            mBinding.kvlOpportunity.setData(it)
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

        mViewModel.oppoAddOrUpdateLiveData.observe(this){
            if (it){
                setResult(RESULT_OK)
                finish()
            }
        }
    }


    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "新增销售机会"
            navIcon = R.mipmap.navigation_icon
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
        customerId = intent.getStringExtra("customerId")
        mViewModel.getOppoTemple(oppoId,customerId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.REQUEST_SELECT_CUSTOMER && resultCode == RESULT_OK){
            if (data != null) {
                val bundle = data.extras
                if (bundle != null) {
                    customerId = bundle.getString(KeyConstant.KEY_CUSTOMER_ID)
                    val name = bundle.getString(KeyConstant.KEY_CUSTOMER_NAME)
                    val cipherPhone = bundle.getString(KeyConstant.KEY_CUSTOMER_PHONE_CIPHER_TXT)
                    val plainPhone = bundle.getString(KeyConstant.KEY_CUSTOMER_PHONE_PLAIN_TXT)
                    val wechat = bundle.getString(KeyConstant.KEY_CUSTOMER_WECHAT)
                    val identity = bundle.getString(KeyConstant.KEY_CUSTOMER_IDENTITY)
                    val identityTxt = bundle.getString(KeyConstant.KEY_CUSTOMER_IDENTITY_TXT)
                    val nameKV =  mBinding.kvlOpportunity.findEntityByParamKey("name")
                    val contactKV =  mBinding.kvlOpportunity.findEntityByName("联系方式")
                    val identityKV =  mBinding.kvlOpportunity.findEntityByParamKey("identity")
                    if (nameKV!=null){
                        nameKV.is_channge = "1"
                        nameKV.value = name
                        nameKV.defaultValue = name
                    }
                    if (contactKV!=null){
                        contactKV.is_channge = "1"
                        contactKV.value = "${plainPhone},${wechat}"
                        contactKV.defaultValue = "${cipherPhone},${wechat}"
                    }
                    if (identityKV!=null){
                        identityKV.is_channge = "1"
                        identityKV.value = identity
                        identityKV.defaultValue = identityTxt
                    }
                    mBinding.kvlOpportunity.refresh()


                }

            }
        }
    }
}