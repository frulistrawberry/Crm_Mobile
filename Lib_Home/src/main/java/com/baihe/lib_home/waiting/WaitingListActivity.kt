package com.baihe.lib_home.waiting

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.baihe.lib_common.adapter.ViewPager2Adapter
import com.baihe.lib_common.databinding.CommonTabViewpagerBinding
import com.baihe.lib_common.widget.state.ToolbarConfigExt.showSearch
import com.baihe.lib_framework.base.BaseViewBindActivity
import com.baihe.lib_framework.log.LogUtil
import com.google.android.material.tabs.TabLayoutMediator

class WaitingListActivity: BaseViewBindActivity<CommonTabViewpagerBinding>() {
    private val adapter by lazy {
        ViewPager2Adapter(supportFragmentManager,lifecycle)
    }
    private val tabs by lazy {
         mutableListOf("全部","已逾期","已完成")
    }
    companion object{
        @JvmStatic
        fun start(context: Context){
            context.startActivity(Intent(context,WaitingListActivity::class.java))
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "待办"
        }
        adapter.apply {
            this.addFragment(WaitingListFragment.newFragment("1"))
            .addFragment(WaitingListFragment.newFragment("2"))
            .addFragment(WaitingListFragment.newFragment("3"))
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