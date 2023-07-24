package com.baihe.lib_home.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.databinding.CommonSrlListBinding
import com.baihe.lib_framework.base.BaseMvvmFragment
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_home.HomeViewModel
import com.baihe.lib_home.ui.adapter.WaitingListAdapter
import com.dylanc.loadingstateview.ViewType

class WaitingListFragment private constructor(): BaseMvvmFragment<CommonSrlListBinding, HomeViewModel>() {
    private var page = 1
    private  val adapter by lazy {
        WaitingListAdapter(requireContext())
    }
    private val type by lazy {
        arguments?.getString("type")
    }

    companion object {
        @JvmStatic
        fun newFragment(type:String): WaitingListFragment {
            return WaitingListFragment().apply {
                val arguments = Bundle()
                arguments.putString("type",type)
                setArguments(arguments)
            }
        }
    }
    override fun initViewModel() {
        super.initViewModel()
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
        mViewModel.waitingListLiveData.observe(this){
            if (it.size<10)
                mBinding?.srlRoot?.setEnableLoadMore(false)
            else
                mBinding?.srlRoot?.setEnableLoadMore(true)
            if (page == 1){
                adapter.setData(it)
            }else
                adapter.addAll(it)
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mBinding?.srlRoot?.setEnableRefresh(true)
        mBinding?.srlRoot?.setEnableLoadMore(true)
        mBinding?.rvList!!.layoutManager = LinearLayoutManager(requireContext())
        mBinding?.rvList!!.adapter = adapter
    }

    override fun initListener() {
        super.initListener()
        mBinding?.srlRoot!!.setOnRefreshListener{
            page = 1
            mViewModel.getWaitingList(page,type!!)
        }
        mBinding?.srlRoot!!.setOnLoadMoreListener {
            page++
            mViewModel.getWaitingList(page,type!!)
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getWaitingList(page,type!!)
    }
}