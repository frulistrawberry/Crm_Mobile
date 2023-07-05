package com.baihe.lib_common.ui.activity

import android.os.Bundle
import com.baihe.lib_common.R
import com.baihe.lib_common.databinding.CommonActivityFollowDetailBinding
import com.baihe.lib_common.viewmodel.CommonViewModel
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.log.LogUtil
import com.dylanc.loadingstateview.ViewType

class FollowDetailActivity: BaseMvvmActivity<CommonActivityFollowDetailBinding, CommonViewModel>() {


    private val followId: String by lazy {
        intent.getStringExtra("followId")
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
        mViewModel.followDetailLiveData.observe(this){
            mBinding.kvlFollow.setData(it.content)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "跟进详情"
            navIcon = R.mipmap.navigation_icon
        }

    }

    override fun initData() {
        super.initData()
        mViewModel.getFollowDetail(followId)
    }
}