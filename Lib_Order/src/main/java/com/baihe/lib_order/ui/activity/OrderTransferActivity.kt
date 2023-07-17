package com.baihe.lib_order.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.baihe.lib_common.constant.KeyConstant.KEY_ORDER_ID
import com.baihe.lib_common.constant.RequestCode.REQUEST_TRANSFER_ORDER
import com.baihe.lib_common.databinding.ActivityAddFollowBinding
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_order.ui.OrderViewModel

class OrderTransferActivity:
    BaseMvvmActivity<ActivityAddFollowBinding, OrderViewModel>() {

    private val orderId: String by lazy {
        intent.getStringExtra(KEY_ORDER_ID)
    }

    companion object{
        fun start(context: Activity,orderId:String){
            context.startActivityForResult(Intent(context,OrderTransferActivity::class.java).apply {
                putExtra(KEY_ORDER_ID,orderId)
            }, REQUEST_TRANSFER_ORDER)
        }
        fun start(fragment: Fragment,reqId:String){
            fragment.startActivityForResult(Intent(fragment.requireContext(),OrderTransferActivity::class.java).apply {
                putExtra(KEY_ORDER_ID,reqId)
            },REQUEST_TRANSFER_ORDER)


        }
    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.loadingDialogLiveData.observe(this){
            if (it)
                showLoadingDialog()
            else
                dismissLoadingDialog()
        }

        mViewModel.stateLiveData.observe(this){
            if (it){
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "订单转移"
        }
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.apply {
            add(KeyValueEntity().apply {
                name = "转移给"
                is_true = "2"
                is_open = "1"
                is_channge = "2"
                type = "userlist"
                paramKey = "follow_user_id"

            })
            add(KeyValueEntity().apply {
                name = "备注"
                is_true = "2"
                is_open = "1"
                is_channge = "2"
                type = "input"
                paramKey = "remarks"

            })
        }
        mBinding.kvlOpportunity.data = kvList
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnCommit.click {
            val params = mBinding.kvlOpportunity.commit()
            params?.let {
                params["order_id"] = orderId
                mViewModel.transferOrder(params)
            }


        }
    }
}