package com.baihe.lib_order.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.baihe.lib_common.R
import com.baihe.lib_common.databinding.CommonTabViewpagerBinding
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_common.ui.adapter.ViewPager2Adapter
import com.baihe.lib_common.ui.dialog.AlertDialog
import com.baihe.lib_common.ui.widget.state.ToolbarConfigExt.showSearch
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.base.BaseViewBindActivity
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_order.ui.fragment.OrderListFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OrderListActivity: BaseViewBindActivity<CommonTabViewpagerBinding>() {
    private val adapter by lazy {
        ViewPager2Adapter(supportFragmentManager,lifecycle)
    }
    private val tabs by lazy {
        mutableListOf("跟进中","我跟进的","我参与的","全部")
    }



    lateinit var keywords:String
    companion object{
        @JvmStatic
        fun start(context: Context){
            context.startActivity(Intent(context, OrderListActivity::class.java))
        }
    }
    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            showSearch(false) {
                keywords = it
                val fragment = adapter.getFragments()[mBinding.viewPager.currentItem]
                (fragment as OrderListFragment).refresh(keywords)
            }
            rightIcon(R.mipmap.ic_create_black){
                if (UserServiceProvider.isCompanyNeedContract()){
                    AlertDialog.Builder(this@OrderListActivity)
                        .setContent("是否已有销售机会")
                        .setText(R.id.button1,"已有，去选择")
                        .setText(R.id.button2,"没有，去新增")
                        .setOnClickListener(R.id.button1,object :BaseDialog.OnClickListener{
                            override fun onClick(dialog: BaseDialog?, view: View) {
                                AddOrderActivity.start(this@OrderListActivity,AddOrderActivity.MODE_OPPO_SELECT)
                            }

                        })
                        .setOnClickListener(R.id.button2,object :BaseDialog.OnClickListener{
                            override fun onClick(dialog: BaseDialog?, view: View) {
                                AddOrderActivity.start(this@OrderListActivity,AddOrderActivity.MODE_OPPO_ADD)
                            }

                        }).create().show()
                }else{
                    AddOrderActivity.start(this@OrderListActivity,AddOrderActivity.MODE_OPPO_ADD)
                }
            }
        }
        mBinding.layoutTab.apply {
            tabMode = TabLayout.MODE_SCROLLABLE

        }
        mBinding.llRight.visible()
        adapter.apply {
            this.addFragment(OrderListFragment.newFragment("1"))
                .addFragment(OrderListFragment.newFragment("2"))
                .addFragment(OrderListFragment.newFragment("3"))
                .addFragment(OrderListFragment.newFragment("0"))
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
    private fun refresh(params: LinkedHashMap<String, Any?>) {
        adapter.getFragments()[mBinding.viewPager.currentItem].let {
            (it as OrderListFragment).refresh()
        }
    }

    override fun initListener() {
        super.initListener()

    }
}
