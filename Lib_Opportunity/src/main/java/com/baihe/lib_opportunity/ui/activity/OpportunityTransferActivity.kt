package com.baihe.lib_opportunity.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.baihe.lib_common.constant.KeyConstant.KEY_CUSTOMER_ID
import com.baihe.lib_common.constant.KeyConstant.KEY_OPPO_ID
import com.baihe.lib_common.constant.RequestCode
import com.baihe.lib_common.constant.RequestCode.REQUEST_TRANSFER_OPPO
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_opportunity.OpportunityViewModel
import com.baihe.lib_opportunity.databinding.OppoCommonActionActivityBinding

class OpportunityTransferActivity:
    BaseMvvmActivity<OppoCommonActionActivityBinding, OpportunityViewModel>() {

    private val reqId: String by lazy {
        intent.getStringExtra(KEY_OPPO_ID)
    }
    private val customerId: String by lazy {
        intent.getStringExtra(KEY_CUSTOMER_ID)
    }
    companion object{
        fun start(context: Activity,reqId:String, customerId:String){
            context.startActivityForResult(Intent(context,OpportunityTransferActivity::class.java).apply {
                putExtra(KEY_OPPO_ID,reqId)
                putExtra(KEY_CUSTOMER_ID,customerId)
            },REQUEST_TRANSFER_OPPO)
        }
        fun start(fragment: Fragment,reqId:String, customerId: String){
            fragment.startActivityForResult(Intent(fragment.requireContext(),OpportunityTransferActivity::class.java).apply {
                putExtra(KEY_OPPO_ID,reqId)
                putExtra(KEY_CUSTOMER_ID,customerId)
            },REQUEST_TRANSFER_OPPO)


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
            title = "转移机会"
        }
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.apply {
            add(KeyValueEntity().apply {
                name = "转移给"
                is_true = "2"
                is_open = "1"
                is_channge = "2"
                type = "userlist"
                paramKey = "ownerId"

            })
            add(KeyValueEntity().apply {
                name = "备注"
                is_true = "2"
                is_open = "1"
                is_channge = "2"
                type = "input"
                paramKey = "comment"

            })
        }
        mBinding.kvlRoot.data = kvList
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnCommit.click {
            val params = mBinding.kvlRoot.commit()
            params?.let {
                params["reqId"] = reqId
                params["customerId"] = customerId
                mViewModel.transferOppo(params)
            }


        }
    }
}