package com.baihe.lib_home.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.ui.dialog.DateDialogUtils
import com.baihe.lib_common.ext.FragmentExt.dismissLoadingDialog
import com.baihe.lib_common.ext.FragmentExt.showLoadingDialog
import com.baihe.lib_common.provider.*
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.base.BaseMvvmFragment
import com.baihe.lib_framework.ext.TimeExt.formattedDate
import com.baihe.lib_framework.ext.TimeExt.toMillis
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.invisible
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_framework.toast.TipsToast
import com.baihe.lib_home.HomeViewModel
import com.baihe.lib_home.databinding.HomeFragmentHomeBinding
import com.baihe.lib_home.ui.dialog.QuickCreateDialog
import com.baihe.lib_home.ui.activity.WaitingListActivity
import com.baihe.lib_home.ui.adapter.WaitingListAdapter
import com.baihe.lib_home.ui.widget.HomeDecorViewDelegate
import com.dylanc.loadingstateview.ViewType
import java.util.*


class HomeFragment: BaseMvvmFragment<HomeFragmentHomeBinding, HomeViewModel>() {
    private val decorDelegate = HomeDecorViewDelegate()
    private val bossSeaDialog by lazy {
        UserServiceProvider.getBossSeaDialog(requireContext()){
                _, name ->
            decorDelegate.mBinding.tvBoss.text = name
            if (startDate!=null && endDate!=null){
                mViewModel.getHomeData(startDate!!,endDate!!)
            }else
                mViewModel.getHomeData()

        }
    }
    lateinit var waitingListAdapter: WaitingListAdapter
     private var startDate:String? = null
     private var endDate:String? = null
    private val startTimePicker by lazy {
        DateDialogUtils.createDatePickerView(requireContext(),"选择起始日期") { date, _ ->
            startDate = date!!.time.formattedDate()
            if (startDate!!.toMillis()<=endDate!!.toMillis()){
                mBinding?.tvStartDate?.text = startDate
                mViewModel.getDataView(startDate!!,endDate!!)
            }else{
                TipsToast.showTips("起始日期需早于结束日期")
            }

        }
    }
    private val endTimePicker by lazy {
        DateDialogUtils.createDatePickerView(requireContext(),"选择结束日期") { date, _ ->
            endDate = date!!.time.formattedDate()
            if (startDate!!.toMillis()<=endDate!!.toMillis()){
                mBinding?.tvEndDate?.text = endDate
                mViewModel.getDataView(startDate!!,endDate!!)
            }else{
                TipsToast.showTips("起始日期需早于结束日期")
            }

        }
    }

    @SuppressLint("SetTextI18n")
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
            mBinding?.tvSignCustomerCount?.text = it.signCustomerCount.toString()
            mBinding?.tvSignSum?.text = "￥${it.signSum}"
            if (it.isOppoDataNotEmpty()){
                val data = it.generateOpportunityData()
                mBinding?.tvChartOppo?.text = it.oppoCount.toString()
                mBinding?.pcOpportunity?.data = data
                mBinding?.llOppoDataView?.visible()
            }else{
                mBinding?.llOppoDataView?.gone()
            }
            if (it.isOrderDataNotEmpty()){
                val data = it.generateOrderData()
                mBinding?.tvChartOrder?.text = it.orderCount.toString()
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
        decorDelegate.mBinding.tvBoss.text = UserServiceProvider.getCompanyName()
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
        mBinding?.btnStartDate?.click {
            startTimePicker.show()
        }
        mBinding?.btnEndDate?.click {
            endTimePicker.show()
        }


    }

    override fun initData() {
        super.initData()
        startDate = System.currentTimeMillis().formattedDate()
        endDate = System.currentTimeMillis().formattedDate()
        mViewModel.getHomeData()
    }

    private fun initChartViews() {
        mBinding?.tvStartDate?.text = System.currentTimeMillis().formattedDate()
        mBinding?.tvEndDate?.text = System.currentTimeMillis().formattedDate()
        mBinding?.pcOpportunity?.apply {
            isDrawHoleEnabled = true
            holeRadius = 60f
            isRotationEnabled = false
            description.isEnabled = false
            isHighlightPerTapEnabled = false
            setUsePercentValues(true)
            legend.isEnabled = false
            setExtraOffsets(10f, 20f, 10f, 20f)

        }
        mBinding?.pcOrder?.apply {
            isDrawHoleEnabled = true
            holeRadius = 60f
            isRotationEnabled = false
            description.isEnabled = false
            isHighlightPerTapEnabled = false
            setUsePercentValues(true)
            legend.isEnabled = false
            setExtraOffsets(10f, 20f, 10f, 20f)

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
            OpportunityServiceProvider.toOpportunityList(requireContext())
        }
        decorDelegate.mBinding.btnCustomer.click {
            CustomerServiceProvider.toCustomerList(requireContext())
        }
        decorDelegate.mBinding.btnOrder.click {
            OrderServiceProvider.toOrderList(requireContext())
        }
        decorDelegate.mBinding.btnContract.click {
            ContractServiceProvider.toContractList(requireContext())
        }
        decorDelegate.mBinding.btnBossSea.click {
            bossSeaDialog.show()
        }
        decorDelegate.mBinding.btnCreate.click {
            QuickCreateDialog.Builder(requireContext())
                .addOnDismissListener(object :BaseDialog.OnDismissListener {
                    override fun onDismiss(dialog: BaseDialog?) {
                        decorDelegate.mBinding.btnCreate.visible()
                    }
                })
                .addOnCancelListener(object :BaseDialog.OnCancelListener {
                    override fun onCancel(dialog: BaseDialog?) {
                        decorDelegate.mBinding.btnCreate.visible()
                    }
                })
                .addOnShowListener(object :BaseDialog.OnShowListener {
                    override fun onShow(dialog: BaseDialog?) {
                        decorDelegate.mBinding.btnCreate.invisible()
                    }
                })
                .create().show()
        }
        decorDelegate.mBinding.srlRoot.setOnRefreshListener {
            if (startDate!=null && endDate!=null){
                mViewModel.getHomeData(startDate!!,endDate!!)
            }else
                mViewModel.getHomeData()
        }


    }


}


