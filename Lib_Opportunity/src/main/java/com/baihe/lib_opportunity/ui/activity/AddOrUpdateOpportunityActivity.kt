package com.baihe.lib_opportunity.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.baihe.lib_common.constant.RequestCode
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
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

    companion object{
        @JvmStatic
        fun start(context: Context, oppoId:String?=null){
            val intent = Intent(context,AddOrUpdateOpportunityActivity::class.java).apply {
                putExtra("oppoId",oppoId)
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
        mViewModel.loadingDialogLiveData.observe(this){
            if (it)
                showLoadingDialog()
            else
                dismissLoadingDialog()
        }
        mViewModel.oppoTempleLiveData.observe(this){
            mBinding.kvlOpportunity.setData(it)
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
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnCommit.click {
            val params = mBinding.kvlOpportunity.commit()
            if (params!=null)
                mViewModel.addOrUpdateOpportunity(params,oppoId)
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getOppoTemple(oppoId)
    }
}