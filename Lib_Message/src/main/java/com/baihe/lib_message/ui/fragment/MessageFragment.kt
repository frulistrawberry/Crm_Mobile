package com.baihe.lib_message.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.entity.MessageEntity
import com.baihe.lib_common.ui.adapter.FollowListAdapter
import com.baihe.lib_common.ui.widget.state.ToolbarConfigExt.showSearch
import com.baihe.lib_framework.base.BaseMvvmFragment
import com.baihe.lib_framework.ext.ResourcesExt.color
import com.baihe.lib_framework.ext.ViewExt.gone
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_framework.widget.state.ktx.NavBtnType
import com.baihe.lib_message.MessageFragmentViewModel
import com.baihe.lib_message.R
import com.baihe.lib_message.databinding.MessageFragmentLayoutBinding
import com.baihe.lib_message.ui.adapter.MessageListAdapter
import com.dylanc.loadingstateview.ViewType


/**
 * @author xukankan
 * @date 2023/7/5 10:47
 * @email：xukankan@jiayuan.com
 * @description：
 */
class MessageFragment : BaseMvvmFragment<MessageFragmentLayoutBinding, MessageFragmentViewModel>() {
    private val messageAdapter by lazy {
        MessageListAdapter()
    }


    override fun initView(view: View, savedInstanceState: Bundle?) {
        mBinding?.rvList?.layoutManager = LinearLayoutManager(activity)
        mBinding?.rvList?.adapter = messageAdapter
        mBinding?.tvRightText?.setOnClickListener {

        }
    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.loadingStateLiveData.observe(this) {
            when (it) {
                ViewType.LOADING -> {
                    if (mBinding?.smartRefreshLayout!!.isRefreshing)
                        showLoadingView()
                }
                ViewType.CONTENT -> {
                    showContentView()
                    mBinding?.smartRefreshLayout!!.finishRefresh()
                }
                ViewType.EMPTY -> {
                    showEmptyView()
                    mBinding?.smartRefreshLayout!!.finishRefresh()
                }
                ViewType.ERROR -> {
                    showContentView()
                    val ist :ArrayList<MessageEntity>  = ArrayList(2)
                    ist.add(MessageEntity("","","true",false,""))
                    ist.add(MessageEntity("","","true",false,""))
                    messageAdapter.setData(ist)
                    mBinding?.smartRefreshLayout!!.finishRefresh()
                }
                else -> LogUtil.d(it.name)
            }
        }

        mViewModel.messagesEntity.observe(this) {
            messageAdapter.setData(it)
        }
    }


    override fun initData() {
        super.initData()
        mViewModel.getMessages()
    }

}