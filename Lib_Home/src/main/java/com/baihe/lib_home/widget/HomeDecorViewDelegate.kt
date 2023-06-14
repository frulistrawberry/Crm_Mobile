package com.baihe.lib_home.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import com.baihe.lib_common.utils.ViewUtil
import com.baihe.lib_framework.helper.AppHelper
import com.baihe.lib_framework.utils.StatusBarSettingHelper
import com.baihe.lib_framework.utils.getColorFromResource
import com.baihe.lib_home.R
import com.baihe.lib_home.databinding.HomeTitleBarBinding
import com.dylanc.loadingstateview.LoadingStateView

class HomeDecorViewDelegate:LoadingStateView.DecorViewDelegate() {
    lateinit var mBinding: HomeTitleBarBinding


    override fun getContentParent(decorView: View): ViewGroup {
        return mBinding.contentParent

    }

    @SuppressLint("InflateParams")
    override fun onCreateDecorView(context: Context, inflater: LayoutInflater): View {
        mBinding = HomeTitleBarBinding.inflate(inflater)

        return mBinding.root.apply {
            mBinding.vStatusBar.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                StatusBarSettingHelper.getStatusBarHeight(
                    AppHelper.getApplication()
                )
            )
            mBinding.nsRoot.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener {
                    _, _, scrollY, _, _ ->
                var percent: Float = scrollY * 1f / mBinding.flHeader.height
                if (percent>1)
                    percent = 1f
                val bgColor = ViewUtil.getColor(percent, Color.TRANSPARENT,Color.WHITE)
                val textColor = ViewUtil.getColor(percent,Color.WHITE, getColorFromResource(R.color.COLOR_4A4C5C))
                val addIcon = ViewUtil.getIcon(R.mipmap.home_ic_add,percent,Color.WHITE,Color.BLACK)
                val arrowIcon = ViewUtil.getIcon(R.mipmap.home_arrow_down_white,percent,Color.WHITE,Color.BLACK)
                mBinding.llTitle.setBackgroundColor(bgColor)
                mBinding.tvBoss.setTextColor(textColor)
                mBinding.btnCreate.setImageDrawable(addIcon)
                mBinding.ivArrow.setImageDrawable(arrowIcon)
            })

        }

    }

}