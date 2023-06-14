package com.baihe.lib_home.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.ext.dismissLoadingDialog
import com.baihe.lib_common.ext.showLoadingDialog
import com.baihe.lib_framework.base.BaseMvvmFragment
import com.baihe.lib_framework.ext.click
import com.baihe.lib_framework.ext.formattedDate
import com.baihe.lib_framework.ext.gone
import com.baihe.lib_framework.ext.visible
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_home.databinding.HomeFragmentHomeBinding
import com.baihe.lib_home.HomeViewModel
import com.baihe.lib_home.widget.HomeDecorViewDelegate
import com.dylanc.loadingstateview.ViewType

class HomeFragment: BaseMvvmFragment<HomeFragmentHomeBinding, HomeViewModel>() {
    private val decorDelegate = HomeDecorViewDelegate()

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
            if (it.isOppoDataEmpty()){
                mBinding?.llOppoDataView?.visible()
            }else{
                mBinding?.llOppoDataView?.gone()
            }
            if (it.isOrderDataEmpty()){
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
    }

    override fun initData() {
        super.initData()
        mViewModel.getHomeData()
    }

    private fun initChartViews() {
        mBinding?.tvStartDate?.text = System.currentTimeMillis().formattedDate()
        mBinding?.tvEndDate?.text = System.currentTimeMillis().formattedDate()
    }

    private fun initWaitingList() {
        mBinding?.rvWaitingList?.apply {
            layoutManager = LinearLayoutManager(requireContext())
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


