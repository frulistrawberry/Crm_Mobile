package com.baihe.lib_customer.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.constant.KeyConstant
import com.baihe.lib_common.constant.RequestCode
import com.baihe.lib_common.databinding.CommonSrlListBinding
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.provider.CustomerServiceProvider
import com.baihe.lib_common.provider.OpportunityServiceProvider
import com.baihe.lib_common.ui.widget.state.ToolbarConfigExt.showSearch
import com.baihe.lib_common.viewmodel.CommonViewModel
import com.baihe.lib_customer.CustomerViewModel
import com.baihe.lib_customer.R
import com.baihe.lib_customer.databinding.CustomerActivitySelectBinding
import com.baihe.lib_customer.ui.adapter.CustomerListAdapter
import com.baihe.lib_customer.ui.adapter.CustomerSelectAdapter
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.log.LogUtil
import com.dylanc.loadingstateview.ViewType

@SuppressLint("NotifyDataSetChanged")
class SelectCustomerActivity: BaseMvvmActivity<CustomerActivitySelectBinding, CustomerViewModel>() {
    private var page = 1
    private var keywords = ""
    val commonViewModel by lazy {
        ViewModelProvider(this).get(CommonViewModel::class.java)
    }
    private  val adapter by lazy {
        CustomerSelectAdapter(this).apply {
            onItemClickListener = { _, position->
                val item = getData()[position]
                for (datum in getData()) {
                    datum.isCheck = false
                }
                item.isCheck = true
                notifyDataSetChanged()
            }

        }
    }

    companion object{
        @JvmStatic
        fun start(context: Context){
            if (context is Activity)
                context.startActivityForResult(Intent(context,SelectCustomerActivity::class.java),RequestCode.REQUEST_SELECT_CUSTOMER)
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        commonViewModel.loadingDialogLiveData.observe(this){
            if (it){
                showLoadingDialog()
            }else
                dismissLoadingDialog()
        }
        mViewModel.loadingStateLiveData.observe(this){
            when(it){
                ViewType.LOADING ->{
                    if (page == 1 && !mBinding.srlRoot.isRefreshing)
                        showLoadingView()
                }
                ViewType.CONTENT -> {
                    showContentView()
                    mBinding.srlRoot.finishRefresh()
                    mBinding.srlRoot.finishLoadMore()
                }
                ViewType.EMPTY -> {
                    if (page == 1){
                        showEmptyView()
                        mBinding.srlRoot.finishRefresh()
                    }else{
                        mBinding.srlRoot.finishLoadMore()
                    }

                }
                ViewType.ERROR -> {
                    if (page == 1){
                        showErrorView()
                        mBinding.srlRoot.finishRefresh()
                    }else{
                        mBinding.srlRoot.finishLoadMore()
                    }

                }
                else -> LogUtil.d(it.name)
            }
        }
        mViewModel.customerListLiveData.observe(this){
            if (it.size<10)
                mBinding.srlRoot.setEnableLoadMore(false)
            else
                mBinding.srlRoot.setEnableLoadMore(true)
            if (page == 1){
                adapter.setData(it)
            }else
                adapter.addAll(it)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "选择客户"
            showSearch {
                page = 1
                keywords = it
                mViewModel.getCustomerList(page,it)
            }

        }
        mBinding.srlRoot.setEnableRefresh(true)
        mBinding.srlRoot.setEnableLoadMore(true)
        mBinding.rvList.layoutManager = LinearLayoutManager(this)
        mBinding.rvList.adapter = adapter
    }

    override fun initListener() {
        super.initListener()
        mBinding.srlRoot.setOnRefreshListener{
            page = 1
            mViewModel.getCustomerList(page,keywords)
        }
        mBinding.srlRoot.setOnLoadMoreListener {
            page++
            mViewModel.getCustomerList(page,keywords)
        }
        mBinding.button1.click {
            CustomerServiceProvider.createOrUpdateCustomer(this)
        }

        mBinding.button2.click {
            for (datum in adapter.getData()) {
                if (datum.isCheck){
                    val data = Intent().apply {
                        putExtras(Bundle().apply {
                            putString(KeyConstant.KEY_CUSTOMER_ID,datum.id.toString())
                            putString(KeyConstant.KEY_CUSTOMER_NAME,datum.name)
                            putString(KeyConstant.KEY_CUSTOMER_PHONE_CIPHER_TXT,datum.phone)
                            putString(KeyConstant.KEY_CUSTOMER_PHONE_PLAIN_TXT,datum.see_phone)
                            putString(KeyConstant.KEY_CUSTOMER_WECHAT,datum.wechat)
                            putString(KeyConstant.KEY_CUSTOMER_IDENTITY,datum.identity.toString())
                            putString(KeyConstant.KEY_CUSTOMER_IDENTITY_TXT,datum.identity_txt)
                        })
                    }
                    setResult(RESULT_OK,data)
                    finish()
                }
            }
        }


    }

    override fun initData() {
        super.initData()
        mViewModel.getCustomerList(page,keywords)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.REQUEST_ADD_CUSTOMER && RESULT_OK == resultCode)
            mViewModel.getCustomerList(page,"")
    }


}