package com.baihe.lib_opportunity.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import com.baihe.lib_common.R
import com.baihe.lib_common.ui.dialog.MoreActionDialog
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_framework.utils.ResUtils
import com.baihe.lib_opportunity.OpportunityViewModel
import com.baihe.lib_opportunity.databinding.OppoActivitySubDetailBinding
import com.dylanc.loadingstateview.ViewType

class OppoSubDetailActivity:
    BaseMvvmActivity<OppoActivitySubDetailBinding, OpportunityViewModel>() {
    private val oppoId by lazy {
        intent.getStringExtra("oppoId")
    }

    companion object{
        fun start(context: Context, oppoId:String){
            val intent = Intent(context,OppoSubDetailActivity::class.java)
            intent.apply {
                putExtra("oppoId",oppoId)
            }
            context.startActivity(intent)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "销售机会详情"
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
            mBinding.kvlBasic.setData(it.toAllShowArray())




        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getOppoDetail(oppoId)
    }
}