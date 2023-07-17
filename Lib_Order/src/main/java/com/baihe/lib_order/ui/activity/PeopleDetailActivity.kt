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

class PeopleDetailActivity: BaseMvvmActivity<CommonActivityFollowDetailBinding, OrderViewModel>() {
    private val orderId: String by lazy {
        intent.getStringExtra(KeyConstant.KEY_ORDER_ID)
    }
    companion object{
        fun start(context: Context, orderId:String){
            context.startActivity(Intent(context,PeopleDetailActivity::class.java).apply {
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
        mViewModel.peopleLiveData.observe(this){
            mBinding.kvlFollow.setData(it)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "人员信息"
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getOrderPeople(orderId)
    }
}