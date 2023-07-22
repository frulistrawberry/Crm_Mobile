package com.baihe.lib_message.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.provider.CustomerServiceProvider
import com.baihe.lib_common.provider.OpportunityServiceProvider
import com.baihe.lib_common.provider.OrderServiceProvider
import com.baihe.lib_framework.base.BaseMvvmFragment
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
 * @description：消息页面
 */
class MessageFragment : BaseMvvmFragment<MessageFragmentLayoutBinding, MessageFragmentViewModel>() {

    private val messageAdapter by lazy {
        MessageListAdapter()
    }


    override fun initView(view: View, savedInstanceState: Bundle?) {
        setToolbar("未读数", NavBtnType.NONE) {
            rightText("全部标记已读") {
                if (messageAdapter.getData().size != 0) {
                    mViewModel.setMessageRead(1, null)
                    messageAdapter.getData().size
                }
            }
        }
        mBinding?.rvList?.layoutManager = LinearLayoutManager(activity)
        mBinding?.rvList?.adapter = messageAdapter
        view.findViewById<TextView>(R.id.tv_right).setTextColor(Color.parseColor("#687FFF"))
        view.findViewById<TextView>(R.id.tv_right).textSize = 13f
    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.loadingStateLiveData.observe(this) {
            when (it) {
                ViewType.LOADING -> {
                    if (!mBinding?.smartRefreshLayout!!.isRefreshing)
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
                    showErrorView()
                    mBinding?.smartRefreshLayout!!.finishRefresh()
                }
                else -> LogUtil.d(it.name)
            }
        }

        mViewModel.messagesInfoEntity.observe(this) {
            if (mViewModel.page > 1) {
                mBinding?.smartRefreshLayout!!.finishLoadMore()
                messageAdapter.addAll(it.rows)
            } else {
                mBinding?.smartRefreshLayout!!.finishRefresh()
                messageAdapter.setData(it.rows)
            }
        }

        mViewModel.setReadResultData.observe(this) {
            mViewModel.getMessages(0)
            mViewModel.getMessageUnreadCount()
        }

        mViewModel.unreadCountLiveData.observe(this){
            updateToolbar {
                title = "未读(${it})"
            }
        }
    }


    override fun initListener() {
        super.initListener()
        mBinding?.smartRefreshLayout?.setOnRefreshListener {
            mViewModel.getMessages(1)
        }
        mBinding?.smartRefreshLayout?.setOnLoadMoreListener {
            mViewModel.getMessages(2)
        }
        messageAdapter.onItemClickListener = { _, position ->
            val item = messageAdapter.getData()[position]
            if (item.unRead) {
                mViewModel.setMessageRead(0, item.msgId)
            }
            //  前往消息详情页面 type  2 机会 3 订单 4 合同 5 客户
            when (item.type) {
                2 -> {
                    activity?.let {
                        OpportunityServiceProvider.toOpportunityDetail(
                            it,
                            item.dataId
                        )
                    }
                }
                3 -> {
                    // 订单
                    activity?.let {
                        OrderServiceProvider.toOrderDetail(
                            it,
                            item.dataId
                        )
                    }
                }
                4 -> {
                    // 合同
                    activity?.let { CustomerServiceProvider.toCustomerDetail(it, item.dataId) }
                }
                5 -> {
                    activity?.let { CustomerServiceProvider.toCustomerDetail(it, item.dataId) }
                }
            }
        }
    }


    override fun initData() {
        super.initData()
        mViewModel.getMessages(0)
        mViewModel.getMessageUnreadCount()
    }

}