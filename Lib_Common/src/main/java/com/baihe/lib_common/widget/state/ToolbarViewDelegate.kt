package com.baihe.lib_common.widget.state

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import com.baihe.lib_common.databinding.CommonTitleViewBinding
import com.baihe.lib_framework.helper.AppHelper
import com.baihe.lib_framework.utils.StatusBarSettingHelper
import com.baihe.lib_framework.widget.state.ktx.BaseToolbarViewDelegate
import com.dylanc.loadingstateview.NavBtnType
import com.dylanc.loadingstateview.ToolbarConfig
class ToolbarViewDelegate: BaseToolbarViewDelegate() {
    lateinit var mBinding: CommonTitleViewBinding

    override fun onCreateToolbar(inflater: LayoutInflater, parent: ViewGroup): View {
        mBinding = CommonTitleViewBinding.inflate(inflater,parent,false)
        mBinding.vStatusBar.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,
            StatusBarSettingHelper.
            getStatusBarHeight(AppHelper.
            getApplication()))
        return mBinding.root
    }
    override fun onBindToolbar(config: ToolbarConfig) {
        mBinding.apply {
            tvTitle.text = config.title?:""
            when(config.navBtnType){
                NavBtnType.ICON -> {
                    config.navIcon?.let { ivLeft.setImageResource(it) }
                    ivLeft.setOnClickListener(config.onNavClickListener)
                    tvLeft.visibility = View.GONE
                    ivLeft.visibility = View.VISIBLE
                }
                NavBtnType.TEXT -> {
                    tvLeft.text = config.navText
                    tvLeft.setOnClickListener(config.onNavClickListener)
                    tvLeft.visibility = View.VISIBLE
                    ivLeft.visibility = View.GONE
                }
                NavBtnType.ICON_TEXT -> {
                    config.navIcon?.let { ivLeft.setImageResource(it) }
                    tvLeft.text = config.navText
                    ivLeft.setOnClickListener(config.onNavClickListener)
                    tvLeft.setOnClickListener(config.onNavClickListener)
                    tvLeft.visibility = View.VISIBLE
                    ivLeft.visibility = View.VISIBLE
                }
                NavBtnType.NONE -> {
                    ivLeft.visibility = View.GONE
                    tvLeft.visibility = View.GONE
                }

            }

            if (config.rightText != null) {
                tvRight.text = config.rightText
                tvRight.setOnClickListener(config.onRightClickListener)
                tvRight.visibility = View.VISIBLE
            } else {
                tvRight.visibility = View.GONE
            }

            if (config.rightIcon != null) {
                ivRight.setImageResource(config.rightIcon!!)
                ivRight.setOnClickListener(config.onRightClickListener)
                ivRight.visibility = View.VISIBLE
            } else {
                ivRight.visibility = View.GONE
            }
        }
    }

}