package com.baihe.lib_customer.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.baihe.lib_common.constant.RequestCode
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_customer.CustomerViewModel
import com.baihe.lib_customer.R
import com.baihe.lib_customer.databinding.CustomerActivityAddOrUpdateCustomerBinding
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.log.LogUtil
import com.dylanc.loadingstateview.ViewType

class AddOrUpdateCustomerActivity :BaseMvvmActivity<CustomerActivityAddOrUpdateCustomerBinding,CustomerViewModel>(){
    private val customerId:String? by lazy {
        intent.getStringExtra("customerId")
    }

    companion object{
        @JvmStatic
        fun start(context:Context,customerId:String?=null){
            val intent = Intent(context,AddOrUpdateCustomerActivity::class.java).apply {
                putExtra("customerId",customerId)
            }
            if (context is Activity)
                context.startActivityForResult(intent,RequestCode.REQUEST_ADD_CUSTOMER)
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
        mViewModel.customerTempleLiveData.observe(this){
            mBinding.kvlCustomer.setData(it)
        }

        mViewModel.customerAddOrUpdateLiveData.observe(this){
            if (it){
                setResult(RESULT_OK)
                finish()
            }
        }
    }


    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "新增客户"
            navIcon = R.mipmap.navigation_icon
        }
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnCommit.click {
            val params = mBinding.kvlCustomer.commit()
            if (params!=null)
                mViewModel.addOrUpdateCustomer(params,customerId)
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getCustomerTemple(customerId)
    }
}