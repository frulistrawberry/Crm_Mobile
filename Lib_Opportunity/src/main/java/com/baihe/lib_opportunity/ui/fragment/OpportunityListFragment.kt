package com.baihe.lib_opportunity.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.databinding.CommonSrlListBinding
import com.baihe.lib_common.ext.FragmentExt.dismissLoadingDialog
import com.baihe.lib_common.ext.FragmentExt.showLoadingDialog
import com.baihe.lib_common.viewmodel.CommonViewModel
import com.baihe.lib_framework.base.BaseMvvmFragment
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_opportunity.OpportunityViewModel
import com.baihe.lib_opportunity.ui.activity.OpportunityDetailActivity
import com.baihe.lib_opportunity.ui.adapter.OpportunityListAdapter
import com.dylanc.loadingstateview.ViewType

class OpportunityListFragment:BaseMvvmFragment<CommonSrlListBinding,OpportunityViewModel>() {

    private val isHistorical by lazy {
        arguments?.getString("isHistorical","0")
    }
    private val adapter by lazy {
        OpportunityListAdapter().apply {
            onButtonActionListener = {id, action ->
                when(action){
                    1->{
                        //打电话
                        commonViewModel.call(id)
                    }
                    2->{
                        // TODO: 写跟进
                    }
                    else ->{

                    }
                }

            }
        }
    }
    private val commonViewModel by lazy {
        ViewModelProvider(this).get(CommonViewModel::class.java)
    }
    var page = 1

    companion object {
        @JvmStatic
        fun newFragment(isHistorical:String): OpportunityListFragment {
            return OpportunityListFragment().apply {
                val arguments = Bundle()
                arguments.putString("isHistorical",isHistorical)
                setArguments(arguments)
            }
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
                    if (page == 1 && !mBinding?.srlRoot!!.isRefreshing)
                        showLoadingView()
                }
                ViewType.CONTENT -> {
                    showContentView()
                    mBinding?.srlRoot!!.finishRefresh()
                    mBinding?.srlRoot!!.finishLoadMore()
                }
                ViewType.EMPTY -> {
                    if (page == 1){
                        showEmptyView()
                        mBinding?.srlRoot!!.finishRefresh()
                    }else{
                        mBinding?.srlRoot!!.finishLoadMore()
                    }

                }
                ViewType.ERROR -> {
                    if (page == 1){
                        showErrorView()
                        mBinding?.srlRoot!!.finishRefresh()
                    }else{
                        mBinding?.srlRoot!!.finishLoadMore()
                    }

                }
                else -> LogUtil.d(it.name)
            }
        }
        mViewModel.oppoListLiveData.observe(this){
            if (it.size<10)
                mBinding?.srlRoot!!.setEnableLoadMore(false)
            else
                mBinding?.srlRoot!!.setEnableLoadMore(true)
            if (page == 1){
                adapter.setData(it)
            }else
                adapter.addAll(it)
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mBinding?.srlRoot?.setEnableRefresh(true)
        mBinding?.srlRoot?.setEnableLoadMore(true)
        mBinding?.rvList?.layoutManager = LinearLayoutManager(requireContext())
        mBinding?.rvList?.adapter = adapter
    }
    override fun initListener() {
        super.initListener()
        mBinding?.srlRoot!!.setOnRefreshListener{
            page = 1
            mViewModel.getOppoList(page,isHistorical!!)
        }
        mBinding?.srlRoot!!.setOnLoadMoreListener {
            page++
            mViewModel.getOppoList(page,isHistorical!!)
        }
        adapter.onItemClickListener = {_,position->
            val item = adapter.getData()[position]
            OpportunityDetailActivity.start(requireContext(),item.id.toString())
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getOppoList(page,isHistorical!!)
    }
}