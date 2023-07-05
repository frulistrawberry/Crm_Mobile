package com.baihe.lib_opportunity.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.baihe.lib_common.R
import com.baihe.lib_common.databinding.CommonSrlListBinding
import com.baihe.lib_common.databinding.CommonTabViewpagerBinding
import com.baihe.lib_common.ui.adapter.ViewPager2Adapter
import com.baihe.lib_common.ui.widget.state.ToolbarConfigExt.showSearch
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.base.BaseViewBindActivity
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_opportunity.OpportunityViewModel
import com.baihe.lib_opportunity.ui.fragment.OpportunityListFragment
import com.google.android.material.tabs.TabLayoutMediator

class OpportunityListActivity: BaseViewBindActivity<CommonTabViewpagerBinding>() {
    private val adapter by lazy {
        ViewPager2Adapter(supportFragmentManager,lifecycle)
    }
    private val tabs by lazy {
        mutableListOf("跟进中","历史跟进","全部")
    }
    lateinit var keywords:String
    companion object{
        @JvmStatic
        fun start(context: Context){
            context.startActivity(Intent(context, OpportunityListActivity::class.java))
        }
    }
    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            showSearch(false) {
                keywords = it
            }
            rightIcon(R.mipmap.ic_create_black){
                AddOrUpdateOpportunityActivity.start(this@OpportunityListActivity)
            }

        }

        mBinding.llRight.visible()

        adapter.apply {
            this.addFragment(OpportunityListFragment.newFragment("1"))
                .addFragment(OpportunityListFragment.newFragment("2"))
                .addFragment(OpportunityListFragment.newFragment("0"))
        }
        mBinding.viewPager.adapter = adapter
        TabLayoutMediator(mBinding.layoutTab,mBinding.viewPager){ tab , position  ->
            tab.text = tabs[position]
            tab.view.isLongClickable = false
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
                tab.view.tooltipText = ""
        }.also {
            it.attach()
        }
    }
}