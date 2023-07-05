package com.baihe.lib_opportunity.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.R
import com.baihe.lib_common.ui.activity.FollowDetailActivity
import com.baihe.lib_common.ui.adapter.FollowListAdapter
import com.baihe.lib_common.ui.dialog.MoreActionDialog
import com.baihe.lib_common.ui.widget.AxisItemDecoration
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_framework.utils.ResUtils
import com.baihe.lib_opportunity.OpportunityViewModel
import com.baihe.lib_opportunity.databinding.OppoActivityOpportunityDetailBinding
import com.dylanc.loadingstateview.ViewType
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

class OpportunityDetailActivity:BaseMvvmActivity<OppoActivityOpportunityDetailBinding,OpportunityViewModel>() {
    private val oppoId by lazy {
        intent.getStringExtra("oppoId")
    }

    private val followAdapter by lazy {
        FollowListAdapter()
    }

    companion object{
        fun start(context: Context, oppoId:String){
            val intent = Intent(context,OpportunityDetailActivity::class.java)
            intent.apply {
                putExtra("oppoId",oppoId)
            }
            context.startActivity(intent)
        }
    }

    override fun initViewModel() {
        super.initViewModel()
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
        mViewModel.oppoDetailLiveData.observe(this){
            mBinding.tvTitle.text = it.title
            val oppoLabel = it?.getOppoLabel()
            if (oppoLabel != null) {
                mBinding.tvPhase.text = oppoLabel.text
                if (!oppoLabel.bgColor.isNullOrEmpty() && oppoLabel.bgColor.startsWith("#")){
                    val labelDrawable = ResUtils.getImageFromResource(R.drawable.bg_round_label_solid) as GradientDrawable
                    labelDrawable.setColor(Color.parseColor(oppoLabel.bgColor))
                    mBinding.tvPhase.background = labelDrawable
                }else{
                    mBinding.tvPhase.background = null
                }
                if (!oppoLabel.textColor.isNullOrEmpty() && oppoLabel.textColor.startsWith("#")){
                    mBinding.tvPhase.setTextColor(Color.parseColor(oppoLabel.textColor))
                }else{
                    mBinding.tvPhase.setTextColor(ResUtils.getColorFromResource(R.color.COLOR_4A4C5C))
                }
                mBinding.tvPhase.visible()
            }else{
                mBinding.tvPhase.gone()
            }
            mBinding.kvlBasic.setData(it.toBasicShowArray())
            if (it.reserve != null) {
                mBinding.kvlReq.setData(it.toReserveShowArray())
                mBinding.llReq.visible()
            }else{
                mBinding.llReq.gone()
            }
            if (it.order != null) {
                mBinding.kvlOrder.setData(it.toReserveShowArray())
                mBinding.llOrder.visible()
            }else{
                mBinding.llOrder.gone()
            }
            if(it.follow.isNullOrEmpty()){
                mBinding.llFollow.gone()
            }else{
                mBinding.llFollow.visible()
                followAdapter.setData(it.follow)
            }
            val bottomButtons = it.genBottomButtons()
            if (bottomButtons.size == 1){
                mBinding.button1.gone()
                mBinding.button2.visible()
                mBinding.button2.text = bottomButtons[0].name
                mBinding.button2.click {
                    buttonClick(bottomButtons[0].type)
                }
            }else if (bottomButtons.size == 2){
                mBinding.button1.visible()
                mBinding.button2.visible()
                mBinding.button1.text = bottomButtons[0].name
                mBinding.button2.text = bottomButtons[1].name
                mBinding.button1.click {
                    buttonClick(bottomButtons[0].type)
                }
                mBinding.button2.click {
                    buttonClick(bottomButtons[1].type)
                }
            }
            val moreButtons = it.genMoreButtons()
            mBinding.btnMore.click {
                MoreActionDialog.Builder(this)
                    .setData(moreButtons)
                    .setOnButtonsClickListener {type->
                        buttonClick(type)
                    }
                    .create().show()
            }



        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "机会详情"
        }
        mBinding.rvFollow.apply {
            layoutManager = LinearLayoutManager(this@OpportunityDetailActivity)
            adapter = followAdapter
            addItemDecoration(AxisItemDecoration(this@OpportunityDetailActivity))
        }
        mBinding.srlRoot.setEnableLoadMore(false)

    }

    override fun initListener() {
        super.initListener()
        mBinding.srlRoot.setOnRefreshListener(OnRefreshListener {
            mViewModel.getOppoDetail(oppoId)
        })
        followAdapter.onFollowItemClickListener = {
            FollowDetailActivity.start(this,it)
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getOppoDetail(oppoId)
    }

    private fun buttonClick(type:Int){
        when(type){
            1->{
                // TODO: 写跟进
            }
            2->{
                // TODO: 下发订单
                ActionActivity.start(this,2,oppoId)
            }
            3->{
                // TODO: 编辑机会
                AddOrUpdateOpportunityActivity.start(this,oppoId)
            }
            4->{
                // TODO: 转移机会
                ActionActivity.start(this,1,oppoId)
            }
            5->{
                // TODO: 归档机会

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK)
            mViewModel.getOppoDetail(oppoId)
    }
}