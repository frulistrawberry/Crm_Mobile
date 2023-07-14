package com.baihe.lib_message.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_framework.base.BaseMvvmFragment
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_message.MessageFragmentViewModel
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
        mBinding?.rvList?.layoutManager = LinearLayoutManager(activity)
        mBinding?.rvList?.adapter = messageAdapter
        mBinding?.tvRightText?.setOnClickListener {
            if (messageAdapter.getData().size != 0) {
                mViewModel.setMessageRead(0, null)
                messageAdapter.getData().size
            }
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
                    showErrorView()
                    mBinding?.smartRefreshLayout!!.finishRefresh()
                }
                else -> LogUtil.d(it.name)
            }
        }

        mViewModel.messagesInfoEntity.observe(this) {
            mBinding?.tvTitle?.text = "未读（${it.total}）"
            if (mViewModel.page > 1) {
                mBinding?.smartRefreshLayout!!.finishLoadMore()
                messageAdapter.addAll(it.rows)
            } else {
                mBinding?.smartRefreshLayout!!.finishRefresh()
                messageAdapter.setData(it.rows)
            }
        }

        mViewModel.setReadResultData.observe(this) {
            mBinding?.smartRefreshLayout!!.autoRefresh()
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
            mViewModel.setMessageRead(1, item.noticeId)
            // todo 前往消息详情页面
        }
    }


    override fun initData() {
        super.initData()
        mViewModel.getMessages(0)
    }

}