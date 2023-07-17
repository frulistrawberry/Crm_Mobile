package com.baihe.lib_contract.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.databinding.CommonSrlListBinding
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.provider.OpportunityServiceProvider
import com.baihe.lib_common.ui.widget.state.ToolbarConfigExt.showSearch
import com.baihe.lib_common.viewmodel.CommonViewModel
import com.baihe.lib_contract.ContractViewModel
import com.baihe.lib_contract.ui.adapter.ContractListAdapter
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.log.LogUtil
import com.dylanc.loadingstateview.ViewType

class ContractListActivity: BaseMvvmActivity<CommonSrlListBinding, ContractViewModel>() {
    private var page = 1
    private var keywords = ""
    private  val adapter by lazy {
        ContractListAdapter()
    }

    companion object{
        @JvmStatic
        fun start(context: Context){
            context.startActivity(Intent(context,ContractListActivity::class.java))
        }
    }

    override fun initViewModel() {
        super.initViewModel()
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
        mViewModel.listLiveData.observe(this){
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
            title = "合同列表"
            showSearch {
                page = 1
                keywords = it
                mViewModel.getContractList(page,it)
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
            mViewModel.getContractList(page,keywords)
        }
        mBinding.srlRoot.setOnLoadMoreListener {
            page++
            mViewModel.getContractList(page,keywords)
        }
        adapter.onItemClickListener = {_,position->
            val item = adapter.getData()[position]
            ContractDetailActivity.start(this,item.id.toString())

        }


    }

    override fun initData() {
        super.initData()
        mViewModel.getContractList(page,keywords)
    }



}