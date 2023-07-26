package com.baihe.lib_order.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.baihe.lib_common.constant.KeyConstant
import com.baihe.lib_common.constant.RequestCode
import com.baihe.lib_common.databinding.ActivityAddFollowBinding
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.provider.ContractServiceProvider
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_common.ui.widget.keyvalue.KeyValueEditLayout
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_order.ui.OrderViewModel

class PreSignActivity: BaseMvvmActivity<ActivityAddFollowBinding, OrderViewModel>() {
    val reqId by lazy {
        intent.getStringExtra(KeyConstant.KEY_OPPO_ID)
    }
    val orderId by lazy {
        intent.getStringExtra(KeyConstant.KEY_ORDER_ID)
    }

    val isCompanyNeedContract by lazy {
        UserServiceProvider.isCompanyNeedContract()
    }

    companion object{
        fun start(context: Activity, reqId:String,  orderId:String){
            context.startActivityForResult(Intent(context, PreSignActivity::class.java).apply {
                putExtra(KeyConstant.KEY_OPPO_ID,reqId)
                putExtra(KeyConstant.KEY_ORDER_ID,orderId)
            }, RequestCode.REQUEST_FOLLOW)
        }

        fun start(context: Fragment, reqId:String,  orderId:String){
            context.startActivityForResult(
                Intent(context.requireContext(),
                    PreSignActivity::class.java).apply {
                    putExtra(KeyConstant.KEY_OPPO_ID,reqId)
                    putExtra(KeyConstant.KEY_ORDER_ID,orderId)
                },RequestCode.REQUEST_FOLLOW)
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.stateLiveData.observe(this){
            if (it){
                if (isCompanyNeedContract)
                    ContractServiceProvider.toAddOrUpdateContract(this,orderId)
                else
                    SignActivity.start(this,orderId)
            }
        }
        mViewModel.loadingDialogLiveData.observe(this){
            if (it)
                showLoadingDialog()
            else
                dismissLoadingDialog()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar {
            title = "销售订单"
        }
        mBinding.btnCommit.text = "下一步"
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.apply {
            //进店时间
            add(KeyValueEntity().also { item->
                item.name = "到店时间"
                item.is_channge = "2"
                item.is_true = "1"
                item.is_open = "1"
                item.type = "datetime"
                item.paramKey = "arrival_time"
            })
            //沟通内容
            add(KeyValueEntity().also { item->
                item.name = "沟通内容"
                item.is_channge = "2"
                item.is_true = "1"
                item.is_open = "1"
                item.type = "input"
                item.paramKey = "remark"
            })
        }
        mBinding.kvlOpportunity.data = kvList
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnCommit.click {
            val params = mBinding.kvlOpportunity.commit()
            params?.put("req_id",reqId)
            params?.put("order_id",orderId)
            if (params!=null){
                mViewModel.confirmIndoor(params)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            setResult(RESULT_OK)
            finish()
        }
    }
}