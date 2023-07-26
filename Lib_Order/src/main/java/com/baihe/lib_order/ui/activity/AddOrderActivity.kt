package com.baihe.lib_order.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.constant.KeyConstant
import com.baihe.lib_common.constant.RequestCode
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.ext.FragmentExt.dismissLoadingDialog
import com.baihe.lib_common.ext.FragmentExt.showLoadingDialog
import com.baihe.lib_common.provider.ContractServiceProvider
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_common.ui.dialog.BottomSelectDialog
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.RecyclerViewExt.divider
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_order.databinding.OrderActivityAddOrderBinding
import com.baihe.lib_order.ui.OrderViewModel
import com.baihe.lib_order.ui.adapter.OppoSelectAdapter
import com.dylanc.loadingstateview.OnReloadListener
import com.dylanc.loadingstateview.ViewType
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class AddOrderActivity: BaseMvvmActivity<OrderActivityAddOrderBinding, OrderViewModel>() {


    companion object{
        const val MODE_OPPO_SELECT = 1
        const val MODE_OPPO_ADD = 2
        const val ACTION_ORDER_SAVE = 1
        const val ACTION_ORDER_NEXT = 2

        fun start(act:Activity,mode:Int){
            act.startActivityForResult(Intent(act,AddOrderActivity::class.java).apply {
                putExtra(KeyConstant.KEY_ORDER_MODE,mode)
            },RequestCode.REQUEST_ADD_ORDER)
        }

        fun start(act:Fragment,mode:Int){
            act.startActivityForResult(Intent(act.requireContext(),AddOrderActivity::class.java).apply {
                putExtra(KeyConstant.KEY_ORDER_MODE,mode)
            },RequestCode.REQUEST_ADD_ORDER)
        }
    }

    private val isCompanyNeedContract by lazy {
        UserServiceProvider.isCompanyNeedContract()
    }
    private var customerId:String? = null
    private var page = 1
    private var action = ACTION_ORDER_SAVE
    private var mode:Int = MODE_OPPO_SELECT
    private val adapter by lazy {
        OppoSelectAdapter().apply {
            onItemClickListener = {_,position ->
                val item = getData()[position]
                for (datum in getData()) {
                    datum.isCheck = false
                }
                item.isCheck = true
                notifyDataSetChanged()
            }
        }
    }

    private val selectDialog by lazy {
        val items = mutableListOf<KeyValueEntity>()
        items.add(KeyValueEntity().apply {
            name = "已有，去选择"
            value = MODE_OPPO_SELECT.toString()
        })
        items.add(KeyValueEntity().apply {
            name = "没有，需新增"
            value = MODE_OPPO_ADD.toString()
        })
        BottomSelectDialog.Builder(this)
            .setData(items,mode.toString())
            .setOnConfirmClickListener { _, value, name,_ ->
                mBinding.tvMode.text = name
                mode = value.toInt()
                toggleMode()
            }
            .create()
    }

    override fun initViewModel() {
        super.initViewModel()

        mViewModel.loadingDialogLiveData.observe(this){
            if (it){
                showLoadingDialog()
            }else
                dismissLoadingDialog()
        }

        mViewModel.loadingStateLiveData.observe(this){
            when(it){
                ViewType.LOADING ->{
                    if (page == 1 && !mBinding.srlSelectMode!!.isRefreshing)
                        showLoadingView()
                }
                ViewType.CONTENT -> {
                    showContentView()
                    mBinding.srlSelectMode.finishRefresh()
                    mBinding.srlSelectMode.finishLoadMore()
                }
                ViewType.EMPTY -> {
                    if (page == 1){
                        showEmptyView()
                        mBinding.srlSelectMode.finishRefresh()
                    }else{
                        mBinding.srlSelectMode.finishLoadMore()
                    }

                }
                ViewType.ERROR -> {
                    if (page == 1){
                        showErrorView()
                        mBinding.srlSelectMode.finishRefresh()
                    }else{
                        mBinding.srlSelectMode.finishLoadMore()
                    }

                }
                else -> LogUtil.d(it.name)
            }

        }


        mViewModel.createOrderLiveData.observe(this){
            when(action){
                ACTION_ORDER_NEXT ->{
                    //  创建合同
                    ContractServiceProvider.toAddOrUpdateContract(this@AddOrderActivity,it.order_id!!)
                }
                ACTION_ORDER_SAVE ->{
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }

        mViewModel.oppoListLiveData.observe(this){
            if (page == 1)
                adapter.setData(it)
            else
                adapter.addAll(it)
        }
        mViewModel.oppoTempleLiveData.observe(this){
            mBinding.kvlOpportunity.data = it
            val kvItem = mBinding.kvlOpportunity.findEntityByParamKey("followUserId")
            if (kvItem!=null){
                if (kvItem.value.isNullOrEmpty()){
                    kvItem.value = UserServiceProvider.getUserId()
                }
                if (kvItem.defaultValue.isNullOrEmpty()){
                    kvItem.defaultValue = UserServiceProvider.getUser()?.name
                }
                mBinding.kvlOpportunity.refreshItem(kvItem)
            }


        }

        mViewModel.customerLiveData.observe(this){
            it?.let {customer->
                mBinding.kvlOpportunity.data?.let {
                    it.forEach {keyValueEntity ->
                        when(keyValueEntity.name){
                            "来源渠道"-> {
                                customer.let { customerDetailEntity ->
                                    keyValueEntity.is_channge = "1"
                                    keyValueEntity.value = customerDetailEntity.sourceChannelId
                                    keyValueEntity.defaultValue= customerDetailEntity.sourceChannel
                                }
                            }
                            "客户姓名" ->{
                                customer.let { customerDetailEntity ->
                                    keyValueEntity.is_channge = "1"
                                    keyValueEntity.value = customerDetailEntity.name
                                    keyValueEntity.defaultValue= customerDetailEntity.name
                                }
                            }
                            "联系方式" ->{
                                customer.let { customerDetailEntity ->
                                    keyValueEntity.is_channge = "1"
                                    keyValueEntity.value = "${customerDetailEntity.see_phone?:""},${customerDetailEntity.wechat?:""}"
                                    keyValueEntity.defaultValue= "${customerDetailEntity.phone?:""},${customerDetailEntity.wechat?:""}"
                                }
                            }
                            "提供人" ->{
                                customer.let { customerDetailEntity ->
                                    keyValueEntity.is_channge = "2"
                                    keyValueEntity.channelId = customerDetailEntity.sourceChannelId
                                    keyValueEntity.value = customerDetailEntity.recordUserId
                                    keyValueEntity.defaultValue= customerDetailEntity.recordUser
                                }
                            }
                            "客户身份"->{
                                customer.let { customerDetailEntity ->
                                    keyValueEntity.is_channge = "2"
                                    keyValueEntity.value = customerDetailEntity.identityId
                                    keyValueEntity.defaultValue= customerDetailEntity.identity
                                }
                            }

                        }
                    }
                }
                mBinding.kvlOpportunity.refresh()
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "销售订单"
        }
        mBinding.rvList.apply {
            layoutManager = LinearLayoutManager(this@AddOrderActivity)
            adapter = this@AddOrderActivity.adapter
            divider(includeLast = false)
        }
        if (isCompanyNeedContract){
            mBinding.btnMode.visible()
            mBinding.button1.visible()
            mBinding.button2.visible()
            toggleMode()
        }else{
            mBinding.btnMode.gone()
            mBinding.button2.gone()
            mBinding.button1.visible()
            mode = MODE_OPPO_ADD
            toggleMode()
        }

    }

    override fun initListener() {
        super.initListener()
        mBinding.srlSelectMode.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener{
            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 1
                mViewModel.getOppoList(page)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page++
                mViewModel.getOppoList(page)
            }

        })
        mBinding.btnMode.click {
            selectDialog.show()
        }
        mBinding.button1.click {
            action = ACTION_ORDER_SAVE
            createOrder()

        }
        mBinding.button2.click {
            action = ACTION_ORDER_NEXT
            createOrder()
        }
    }


    override fun initData() {
        super.initData()
        mode = intent.getIntExtra(KeyConstant.KEY_ORDER_MODE, MODE_OPPO_ADD)
        toggleMode()
    }

    private fun createOrder() {
        when(mode){
            MODE_OPPO_ADD ->{
                val params = mBinding.kvlOpportunity.commit()
                params?.let {
                    mViewModel.createOrderWithoutOppo(params)
                }
            }
            MODE_OPPO_SELECT ->{
                val data = adapter.getData()
                data.forEach {datum->
                    if (datum.isCheck){
                        mViewModel.createOrderWithOppo(datum.id,datum.customerId!!)
                    }
                }
            }
        }

    }



    private fun toggleMode(){
        when(mode){
            MODE_OPPO_ADD ->{
                mViewModel.getOppoTemple()
                mBinding.tvMode.text = "暂无，需新增"
                mBinding.srlSelectMode.gone()
                mBinding.llAddMode.visible()
                if (isCompanyNeedContract)
                    mBinding.button2.visible()
                mBinding.button1.visible()
            }
            MODE_OPPO_SELECT ->{
                page = 1
                mViewModel.getOppoList(page)
                mBinding.tvMode.text = "已有，去选择"
                mBinding.llAddMode.gone()
                mBinding.srlSelectMode.visible()
                mBinding.button2.visible()
                mBinding.button1.gone()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.REQUEST_SELECT_CUSTOMER && resultCode == RESULT_OK){
            customerId = data?.let {
                data.getStringExtra(KeyConstant.KEY_CUSTOMER_ID)
            }
            customerId?.let {
                mViewModel.getCustomerInfo(it)
            }
        }
        if (requestCode == RequestCode.REQUEST_ADD_CONTRACT && resultCode == RESULT_OK){
            setResult(RESULT_OK)
            finish()
        }
    }
}