package com.baihe.lib_opportunity.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.baihe.lib_common.constant.KeyConstant
import com.baihe.lib_common.constant.KeyConstant.KEY_CUSTOMER_ID
import com.baihe.lib_common.constant.KeyConstant.KEY_OPPO_ID
import com.baihe.lib_common.constant.RequestCode
import com.baihe.lib_common.constant.RequestCode.REQUEST_DISPATCH_ORDER
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.toast.TipsToast
import com.baihe.lib_opportunity.OpportunityViewModel
import com.baihe.lib_opportunity.databinding.OppoCommonActionActivityBinding

class DispatchOrderActivity:
    BaseMvvmActivity<OppoCommonActionActivityBinding, OpportunityViewModel>() {

    private val reqId by lazy {
        intent.getStringExtra(KEY_OPPO_ID)
    }
    private val customerId by lazy {
        intent.getStringExtra(KEY_CUSTOMER_ID)
    }
    companion object{
        fun start(context: Activity, reqId:String, customerId:String){
            context.startActivityForResult(Intent(context,DispatchOrderActivity::class.java).apply {
                putExtra(KEY_OPPO_ID,reqId)
                putExtra(KEY_CUSTOMER_ID,customerId)
            },REQUEST_DISPATCH_ORDER)

        }
        fun start(fragment: Fragment, reqId:String, customerId: String){
            fragment.startActivityForResult(Intent(fragment.requireContext(),DispatchOrderActivity::class.java).apply {
                putExtra(KEY_OPPO_ID,reqId)
                putExtra(KEY_CUSTOMER_ID,customerId)
            },REQUEST_DISPATCH_ORDER)



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

        mViewModel.oppoAddOrUpdateLiveData.observe(this){
            if (it){
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "下发订单"
        }
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.apply {
            add(KeyValueEntity().apply {
                name = "客户是否到店"
                is_true = "2"
                is_open = "1"
                is_channge = "2"
                type = "collection"
                paramKey = "arrival_status"
                option = mutableListOf<KeyValueEntity?>().apply {
                    add(KeyValueEntity().apply {
                        name = "单人到店"
                        value = "1"
                    })
                    add(KeyValueEntity().apply {
                        name = "双人到店"
                        value = "2"
                    })
                    add(KeyValueEntity().apply {
                        name = "未到店"
                        value = "3"
                    })
                }

            })
            add(KeyValueEntity().apply {
                name = "下发订单给"
                is_true = "2"
                is_open = "1"
                is_channge = "2"
                type = "userlist"
                paramKey = "ownerId"

            })
        }
        mBinding.kvlRoot.data = kvList
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnCommit.click {
            val params = mBinding.kvlRoot.commit()
            params?.let {
                if (params["ownerId"] == null){
                    TipsToast.showTips("请选择人员")
                    return@click
                }
                if (params["arrival_status"] == null){
                    TipsToast.showTips("请选择是否到店")
                    return@click
                }
                params["reqId"] = reqId
                params["customerId"] = customerId
                mViewModel.dispatchOrder(params)
            }
        }
    }
}