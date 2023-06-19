package com.baihe.lib_home.home

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.ext.FragmentExt.dismissLoadingDialog
import com.baihe.lib_common.ext.FragmentExt.showLoadingDialog
import com.baihe.lib_framework.base.BaseMvvmFragment
import com.baihe.lib_framework.ext.ResourcesExt.color
import com.baihe.lib_framework.ext.TimeExt.formattedDate
import com.baihe.lib_framework.ext.ViewExt
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_home.databinding.HomeFragmentHomeBinding
import com.baihe.lib_home.HomeViewModel
import com.baihe.lib_home.R
import com.baihe.lib_home.waiting.WaitingListActivity
import com.baihe.lib_home.waiting.WaitingListAdapter
import com.baihe.lib_home.widget.HomeDecorViewDelegate
import com.dylanc.loadingstateview.ViewType
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.model.GradientColor

class HomeFragment: BaseMvvmFragment<HomeFragmentHomeBinding, HomeViewModel>() {
    private val decorDelegate = HomeDecorViewDelegate()
    lateinit var waitingListAdapter: WaitingListAdapter

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.loadingStateLiveData.observe(this){
            when(it){
                ViewType.LOADING ->{
                    if (!decorDelegate.mBinding.srlRoot.isRefreshing)
                        showLoadingView()
                }
                ViewType.CONTENT -> {
                    showContentView()
                    decorDelegate.mBinding.srlRoot.finishRefresh()
                }
                ViewType.EMPTY -> {
                    showEmptyView()
                    decorDelegate.mBinding.srlRoot.finishRefresh()
                }
                ViewType.ERROR -> {
                    showErrorView()
                    decorDelegate.mBinding.srlRoot.finishRefresh()
                }
                else -> LogUtil.d(it.name)
            }
        }

        mViewModel.dataViewLiveData.observe(this){
            LogUtil.json(TAG!!,it)
            mBinding?.tvCustomerCount?.text = it.customerCount.toString()
            mBinding?.tvOppoCount?.text = it.oppoCount.toString()
            mBinding?.tvOrderCount?.text = it.orderCount.toString()
            mBinding?.tvOrderCount?.text = it.customerCount.toString()
            if (it.isOppoDataNotEmpty()){
                val data = it.generateOpportunityData()
                mBinding?.pcOpportunity?.data = data
                mBinding?.llOppoDataView?.visible()
            }else{
                mBinding?.llOppoDataView?.gone()
            }
            if (it.isOrderDataNotEmpty()){
                val data = it.generateOpportunityData()
                mBinding?.pcOrder?.data = data
                mBinding?.llOrderDataView?.visible()
            }else{
                mBinding?.llOrderDataView?.gone()
            }
        }

        mViewModel.waitingListLiveData.observe(this){
            LogUtil.json(TAG!!,it)
            if (it.isNullOrEmpty()){
                mBinding?.llWaitingList?.gone()
            }else{
                waitingListAdapter.setData(it)
                mBinding?.llWaitingList?.visible()
            }
        }

        mViewModel.loadingDialogLiveData.observe(this){
            if (it)
                showLoadingDialog()
            else
                dismissLoadingDialog()
        }

    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        setDecorView(decorDelegate)
        initRefresh()
        initWaitingList()
        initChartViews()
    }

    private fun initRefresh() {
        decorDelegate.mBinding.srlRoot.setEnableLoadMore(false)
    }

    override fun initListener() {
        super.initListener()
        initHeaderListener()
        mBinding?.btnAll?.click {
            WaitingListActivity.start(requireContext())
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getHomeData()
    }

    private fun initChartViews() {
        mBinding?.tvStartDate?.text = System.currentTimeMillis().formattedDate()
        mBinding?.tvEndDate?.text = System.currentTimeMillis().formattedDate()
        mBinding?.pcOpportunity?.apply {
            isDrawHoleEnabled = true
            holeRadius = 60f
            description.isEnabled = false

        }
    }

    private fun initWaitingList() {
        waitingListAdapter = WaitingListAdapter(requireContext())
        mBinding?.rvWaitingList?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = waitingListAdapter
        }

    }



    private fun initHeaderListener() {
        decorDelegate.mBinding.btnOpportunity.click {

        }
        decorDelegate.mBinding.btnCustomer.click {

        }
        decorDelegate.mBinding.btnOrder.click {

        }
        decorDelegate.mBinding.btnContract.click {

        }
        decorDelegate.mBinding.btnBossSea.click {

        }
        decorDelegate.mBinding.btnCreate.click {

        }
        decorDelegate.mBinding.srlRoot.setOnRefreshListener {
            mViewModel.getHomeData()
        }


    }


}


