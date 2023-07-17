package com.baihe.lib_order.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.baihe.lib_common.constant.KeyConstant
import com.baihe.lib_common.databinding.CommonActivityFollowDetailBinding
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_order.ui.OrderViewModel
import com.dylanc.loadingstateview.ViewType

class OrderSubDetailActivity:
    BaseMvvmActivity<CommonActivityFollowDetailBinding, OrderViewModel>() {
    private val orderId: String by lazy {
        intent.getStringExtra(KeyConstant.KEY_ORDER_ID)
    }
    companion object{
        fun start(context: Context, orderId:String){
            context.startActivity(Intent(context,OrderSubDetailActivity::class.java).apply {
                putExtra(KeyConstant.KEY_ORDER_ID,orderId)
            })
        }
    }



    override fun initViewModel() {
        super.initViewModel()
        mViewModel.loadingStateLiveData.observe(this){
            when(it){
                ViewType.LOADING ->{
                    showLoadingView()
                }
                ViewType.CONTENT -> {
                    showContentView()
                }
                ViewType.EMPTY -> {
                    showEmptyView()
                }
                ViewType.ERROR -> {
                    showErrorView()
                }
                else -> LogUtil.d(it.name)
            }
        }
        mViewModel.detailLiveData.observe(this){ detailEntity ->
            detailEntity?.let {
                mBinding.kvlFollow.setData(it.orderAllShowArray())
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "订单详情"
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getOrderDetail(orderId)
    }
}