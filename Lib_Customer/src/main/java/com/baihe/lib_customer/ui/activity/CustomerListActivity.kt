package com.baihe.lib_customer.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.databinding.CommonSrlListBinding
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.viewmodel.CommonViewModel
import com.baihe.lib_common.ui.widget.state.ToolbarConfigExt.showSearch
import com.baihe.lib_customer.CustomerViewModel
import com.baihe.lib_customer.R
import com.baihe.lib_customer.ui.adapter.CustomerListAdapter
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.log.LogUtil
import com.dylanc.loadingstateview.ViewType

class CustomerListActivity: BaseMvvmActivity<CommonSrlListBinding, CustomerViewModel>() {
    private var page = 1
    private var keywords = ""
    val commonViewModel by lazy {
        ViewModelProvider(this).get(CommonViewModel::class.java)
    }
    private  val adapter by lazy {
        CustomerListAdapter(this).apply {
            onCallListener = {
                commonViewModel.call(it)
            }
        }
    }

    companion object{
        @JvmStatic
        fun start(context: Context){
            context.startActivity(Intent(context,CustomerListActivity::class.java))
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
            title = "客户列表"
            rightIcon(R.mipmap.ic_create_black){
                AddOrUpdateCustomerActivity.start(this@CustomerListActivity)
            }
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
        adapter.onItemClickListener = {_,position->
            val item = adapter.getData()[position]
            CustomerDetailActivity.start(this,item.id.toString())

        }


    }

    override fun initData() {
        super.initData()
        mViewModel.getCustomerList(page,keywords)
    }



}