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
import com.baihe.lib_common.ui.activity.PhotoPickActivity
import com.baihe.lib_common.ui.widget.keyvalue.KeyValueEditLayout
import com.baihe.lib_common.ui.widget.keyvalue.KeyValueLayout.OnItemActionListener
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_order.ui.OrderViewModel

class OrderChargebackActivity: BaseMvvmActivity<ActivityAddFollowBinding, OrderViewModel>() {
    private val orderId: String by lazy {
        intent.getStringExtra(KeyConstant.KEY_ORDER_ID)
    }

    companion object{
        fun start(context: Activity, orderId:String){
            context.startActivityForResult(Intent(context,OrderChargebackActivity::class.java).apply {
                putExtra(KeyConstant.KEY_ORDER_ID,orderId)
            }, RequestCode.REQUEST_TRANSFER_ORDER)
        }
        fun start(fragment: Fragment, reqId:String){
            fragment.startActivityForResult(Intent(fragment.requireContext(),OrderChargebackActivity::class.java).apply {
                putExtra(KeyConstant.KEY_ORDER_ID,reqId)
            }, RequestCode.REQUEST_TRANSFER_ORDER)


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
            title = "申请退单"
        }
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.apply {
            add(KeyValueEntity().apply {
                name = "应退金额"
                is_true = "1"
                is_open = "1"
                is_channge = "2"
                type = "amount"
                paramKey = "follow_user_id"

            })
            add(KeyValueEntity().apply {
                name = "上传凭证"
                is_true = "2"
                is_open = "1"
                is_channge = "2"
                type = "upload"
                paramKey = "file"

            })
            add(KeyValueEntity().apply {
                name = "备注"
                is_true = "2"
                is_open = "1"
                is_channge = "2"
                type = "input"
                paramKey = "extension_remark"

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
                mViewModel.chargebackOrder(params)
            }


        }
        mBinding.kvlOpportunity.setOnItemActionListener(object : KeyValueEditLayout.OnItemActionListener(){
            override fun onEvent(
                keyValueEntity: KeyValueEntity?,
                itemType: KeyValueEditLayout.ItemType?
            ) {
                if (keyValueEntity?.paramKey == "file"){
                    PhotoPickActivity.start(this@OrderChargebackActivity)
                }
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.REQUEST_PHOTO && resultCode == RESULT_OK){
            data?.let {
                val imageList = data.getStringArrayListExtra(KeyConstant.KEY_PHOTOS)
                imageList?.let {
                    imageList.forEach {
                        LogUtil.d("imageList",it)
                    }
                }
            }
        }
    }
}