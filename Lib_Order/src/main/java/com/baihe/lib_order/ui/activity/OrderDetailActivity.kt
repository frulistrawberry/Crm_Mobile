package com.baihe.lib_order.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.R
import com.baihe.lib_common.constant.KeyConstant.KEY_ORDER_ID
import com.baihe.lib_common.constant.StatusConstant
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_CHARGE_ORDER
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_CONFIRM_ARRIVAL
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_EDIT_CONTRACT
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_EDIT_ORDER
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_FOLLOW
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_SET_PEOPLE
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_SIGN
import com.baihe.lib_common.entity.ButtonTypeEntity.Companion.ACTION_TRANSFER_ORDER
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.provider.ContractServiceProvider
import com.baihe.lib_common.provider.OpportunityServiceProvider
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_common.ui.adapter.FollowListAdapter
import com.baihe.lib_common.ui.dialog.MoreActionDialog
import com.baihe.lib_common.viewmodel.CommonViewModel
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_order.databinding.OrderActivityOrderDetailBinding
import com.baihe.lib_order.ui.OrderViewModel
import com.baihe.lib_order.ui.adapter.OrderStatusAdapter
import com.dylanc.loadingstateview.ViewType

class OrderDetailActivity: BaseMvvmActivity<OrderActivityOrderDetailBinding, OrderViewModel>() {

    private val commonViewModel by lazy {
        ViewModelProvider(this).get(CommonViewModel::class.java)
    }

    private val statusAdapter by lazy {
        OrderStatusAdapter()
    }

    private val followAdapter by lazy {
        FollowListAdapter()
    }

    private val orderId by lazy {
        intent.getStringExtra(KEY_ORDER_ID)
    }

    private val isCompanyNeedContract by lazy {
        UserServiceProvider.isCompanyNeedContract()
    }
    private var reqId:String? = null;
    private var customerId:String? = null;
    private var contractId:String? = null;
    private var order_phase:String? = null;

    companion object{
        fun start(context:Context,orderId:String){
            context.startActivity(Intent(context,OrderDetailActivity::class.java).apply {
                putExtra(KEY_ORDER_ID,orderId)
            })
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        commonViewModel.loadingDialogLiveData.observe(this){
            if (it){
                showLoadingDialog()
            }else{
                dismissLoadingDialog()
            }
        }
        mViewModel.loadingStateLiveData.observe(this){
            when(it){
                ViewType.LOADING ->{
                    if (!mBinding.srlRoot.isRefreshing)
                        showLoadingView()
                }
                ViewType.CONTENT -> {
                    showContentView()
                    mBinding.srlRoot.finishRefresh()
                }
                ViewType.EMPTY -> {
                    showEmptyView()
                    mBinding.srlRoot.finishRefresh()
                }
                ViewType.ERROR -> {
                    showErrorView()
                    mBinding.srlRoot.finishRefresh()
                }
                else -> LogUtil.d(it.name)
            }
        }
        mViewModel.detailLiveData.observe(this){ detailEntity ->
            detailEntity.opportunityInfo?.let {opportunityInfo->
                reqId = opportunityInfo.req_id
                customerId = opportunityInfo.customer_id
                order_phase = opportunityInfo.order_phase
                mBinding.tvTitle.text = opportunityInfo.title
                if (opportunityInfo.orderstatus == StatusConstant.ORDER_TO_BE_SIGNED){
                    mBinding.tvPhase.text = opportunityInfo.order_phase_status
                }else{
                    mBinding.tvPhase.text = opportunityInfo.orderstatus_txt
                }
            }
            mBinding.kvlBasic.setData(detailEntity.basicShowArray())
            detailEntity.authority?.let {
                if (it.viewoppotion)
                    mBinding.btnOppo.visible()
                if (it.viewReaser)
                    mBinding.btnReqMore.visible()
                if (it.vieworder)
                    mBinding.btnOrder.visible()
                if (it.viewoppoLog)
                    mBinding.llFollow.visible()
            }
            statusAdapter.setData(detailEntity.genOrderStatus())
            mBinding.kvlOrder.setData(detailEntity.orderShowArray())
            if (!detailEntity.reqShowArray().isNullOrEmpty()){
                mBinding.llReq.visible()
                mBinding.kvlReq.setData(detailEntity.reqShowArray())
                mBinding.button.gone()
                detailEntity.genReqButton()?.let {buttonType->
                    mBinding.button.text = buttonType.name
                    mBinding.button.click {
                        buttonClick(buttonType.type)
                    }
                    mBinding.button.visible()
                }
            }else{
                mBinding.llReq.gone()
            }

            if (!detailEntity.contractShowArray().isNullOrEmpty()){
                mBinding.llContract.visible()
                contractId = detailEntity.contractInfo?.id
                mBinding.kvlContract.setData(detailEntity.contractShowArray())
            }else{
                mBinding.llContract.gone()
            }
            if (!detailEntity.peopleInfo.isNullOrEmpty()){
                mBinding.llPeople.visible()
                mBinding.kvlPeople.setData(detailEntity.peopleInfo)
            }else{
                mBinding.llPeople.gone()
            }
            if (!detailEntity.followData?.follow.isNullOrEmpty()){
                mBinding.llFollow.visible()
                followAdapter.setData(detailEntity.followData?.follow)
            }else{
                mBinding.llFollow.gone()
            }

            val bottomButtons = detailEntity.genBottomButtons()
            when (bottomButtons.size) {
                1 -> {
                    mBinding.button1.gone()
                    mBinding.button2.gone()
                    mBinding.button3.visible()
                    mBinding.button3.text = bottomButtons[0].name
                    mBinding.button3.click {
                        buttonClick(bottomButtons[0].type)
                    }
                }
                2 -> {
                    mBinding.button1.gone()
                    mBinding.button2.visible()
                    mBinding.button3.visible()
                    mBinding.button2.text = bottomButtons[0].name
                    mBinding.button3.text = bottomButtons[1].name
                    mBinding.button2.click {
                        buttonClick(bottomButtons[0].type)
                    }
                    mBinding.button3.click {
                        buttonClick(bottomButtons[1].type)
                    }
                }
                3 -> {
                    mBinding.button1.visible()
                    mBinding.button2.visible()
                    mBinding.button3.visible()
                    mBinding.button1.text = bottomButtons[0].name
                    mBinding.button2.text = bottomButtons[1].name
                    mBinding.button3.text = bottomButtons[2].name
                    mBinding.button1.click {
                        buttonClick(bottomButtons[0].type)
                    }
                    mBinding.button2.click {
                        buttonClick(bottomButtons[1].type)
                    }
                    mBinding.button3.click {
                        buttonClick(bottomButtons[2].type)
                    }
                }
                else -> {
                    mBinding.llButtons.gone()
                }
            }
            val moreButtons = detailEntity.genMoreButtons()
            mBinding.btnMore.click {
                MoreActionDialog.Builder(this)
                    .setData(moreButtons)
                    .setOnButtonsClickListener {type->
                        buttonClick(type)
                    }
                    .create().show()
            }
            mBinding.btnOppo.click {
                OpportunityServiceProvider.toOpportunityDetail(this,detailEntity.opportunityInfo?.req_id!!)
            }




        }

    }


    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "订单详情"
        }
        mBinding.rvStatus.layoutManager = GridLayoutManager(this,4)
        mBinding.rvStatus.adapter = statusAdapter
        mBinding.srlRoot.setEnableLoadMore(false)
        mBinding.rvFollow.layoutManager = LinearLayoutManager(this)
        mBinding.rvFollow.adapter = followAdapter
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnReqMore.click { view->
            ReserveDetailActivity.start(this, orderId)
        }
        mBinding.btnOrder.click {
            OrderSubDetailActivity.start(this,orderId)
        }

        mBinding.btnContractMore.click {
            // TODO: 合同详情
            ContractServiceProvider.toContractDetail(this,contractId!!)
        }
        mBinding.btnPeopleMore.click {
            PeopleDetailActivity.start(this,orderId)
        }

        mBinding.srlRoot.setOnRefreshListener {
            mViewModel.getOrderDetail(orderId)
        }



    }

    override fun initData() {
        super.initData()
        mViewModel.getOrderDetail(orderId)
    }

    private fun buttonClick(type: Int) {
        when(type){
            ACTION_SIGN->{

                if (order_phase == StatusConstant.ORDER_PHASE_STORE_TO_BE_ENTERED || order_phase == StatusConstant.ORDER_PHASE_CUSTOMER_EFFECTIVE){
                    PreSignActivity.start(this,reqId!!,orderId)
                }else if (isCompanyNeedContract){
                    ContractServiceProvider.toAddOrUpdateContract(this,orderId)
                }else{
                    SignActivity.start(this,orderId)
                }
            }
            ACTION_CONFIRM_ARRIVAL->{
                ConfirmIndoorActivity.start(this,reqId!!,orderId)
            }
            ACTION_SET_PEOPLE->{
                AddPeopleActivity.start(this,orderId)
            }
            ACTION_TRANSFER_ORDER->{
                OrderTransferActivity.start(this,orderId)
            }
            ACTION_EDIT_ORDER->{
                OpportunityServiceProvider.toEditOppo(this,reqId!!,customerId!!,"编辑订单")
            }
            ACTION_CHARGE_ORDER->{
                OrderChargebackActivity.start(this,orderId)
            }
            ACTION_EDIT_CONTRACT->{
                ContractServiceProvider.toAddOrUpdateContract(this,orderId,contractId)
            }
            ACTION_FOLLOW ->{
                AddFollowActivity.start(this,reqId!!,customerId!!,orderId)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK)
            mViewModel.getOrderDetail(orderId)
    }

}