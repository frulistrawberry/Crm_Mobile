package com.baihe.lib_opportunity.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.baihe.lib_common.R
import com.baihe.lib_common.databinding.CommonSrlListBinding
import com.baihe.lib_common.databinding.CommonTabViewpagerBinding
import com.baihe.lib_common.ui.adapter.ViewPager2Adapter
import com.baihe.lib_common.ui.dialog.FilterDialog
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_common.ui.widget.state.ToolbarConfigExt.showSearch
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.base.BaseViewBindActivity
import com.baihe.lib_framework.ext.AnyExt.saveAs
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_opportunity.OpportunityViewModel
import com.baihe.lib_opportunity.ui.fragment.OpportunityListFragment
import com.google.android.material.tabs.TabLayoutMediator
import java.util.LinkedHashMap

class OpportunityListActivity: BaseViewBindActivity<CommonTabViewpagerBinding>() {
    private val adapter by lazy {
        ViewPager2Adapter(supportFragmentManager,lifecycle)
    }
    private val tabs by lazy {
        mutableListOf("跟进中","历史跟进","全部")
    }
    private val filterDialog by lazy{
        FilterDialog.Builder(this)
            .setTitle("销售机会筛选")
            .setData(filterTemple)
            .setOnCommitListener {
                refresh(it)
            }
            .create()
    }



    val filterTemple:List<KeyValueEntity> by lazy {
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.apply {
            add(KeyValueEntity().apply {
                name = "渠道来源"
                type = "channelFilter"
                paramKey = "channelId"
            })
            add(KeyValueEntity().apply {
                name = "机会状态"
                type = "collectionMultiple"
                paramKey = "reqPhase"
                option = mutableListOf<KeyValueEntity?>().apply {
                    add(KeyValueEntity().apply {
                        name = "待邀约"
                        value = "200"
                    })
                    add(KeyValueEntity().apply {
                        name = "客户待定"
                        value = "220"
                    })
                    add(KeyValueEntity().apply {
                        name = "客户有效"
                        value = "230"
                    })
                    add(KeyValueEntity().apply {
                        name = "邀约成功"
                        value = "240"
                    })
                    add(KeyValueEntity().apply {
                        name = "已进店"
                        value = "250"
                    })
                    add(KeyValueEntity().apply {
                        name = "已删除"
                        value = "260"
                    })
                }
            })
            add(KeyValueEntity().apply {
                name = "是否下发订单"
                type = "collection"
                paramKey = "existsOrder"
                option = mutableListOf<KeyValueEntity?>().apply {
                    add(KeyValueEntity().apply {
                        name = "是"
                        value = "1"
                    })
                    add(KeyValueEntity().apply {
                        name = "否"
                        value = "2"
                    })
                }
            })
            add(KeyValueEntity().apply {
                name = "创建时间"
                type = "dateTimeRangeFilter"
                paramKey = "createStartTime,createEndTime"
            })
            add(KeyValueEntity().apply {
                name = "客户首次有效"
                type = "collection"
                paramKey = "reqOnceValid"
                option = mutableListOf<KeyValueEntity?>().apply {
                    add(KeyValueEntity().apply {
                        name = "是"
                        value = "1"
                    })
                    add(KeyValueEntity().apply {
                        name = "否"
                        value = "0"
                    })
                }
            })
        }

        kvList
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
                val fragment = adapter.getFragments()[mBinding.viewPager.currentItem]
                (fragment as OpportunityListFragment).refresh(keywords)
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

    private fun refresh(params: LinkedHashMap<String, Any?>) {
        adapter.getFragments()[mBinding.viewPager.currentItem].let {
            (it as OpportunityListFragment).refresh()
        }
    }

    override fun initListener() {
        super.initListener()
        mBinding.tvFilter.click {
            filterDialog.show()
        }
    }
}