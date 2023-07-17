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
import com.baihe.lib_common.ui.widget.keyvalue.KeyValueEditLayout
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_order.ui.OrderViewModel

class SignActivity: BaseMvvmActivity<ActivityAddFollowBinding, OrderViewModel>() {

    val orderId by lazy {
        intent.getStringExtra(KeyConstant.KEY_ORDER_ID)
    }

    companion object{
        fun start(context: Activity,  orderId:String){
            context.startActivityForResult(Intent(context, SignActivity::class.java).apply {
                putExtra(KeyConstant.KEY_ORDER_ID,orderId)
            }, RequestCode.REQUEST_FOLLOW)
        }

        fun start(context: Fragment, orderId:String){
            context.startActivityForResult(
                Intent(context.requireContext(),
                    SignActivity::class.java).apply {
                    putExtra(KeyConstant.KEY_ORDER_ID,orderId)
                }, RequestCode.REQUEST_FOLLOW)
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.stateLiveData.observe(this){
            if (it){
                setResult(RESULT_OK)
                finish()
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
            title = "完成签约"
        }
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.apply {
            //进店时间
            add(KeyValueEntity().also { item->
                item.name = "签约时间"
                item.is_channge = "2"
                item.is_true = "1"
                item.is_open = "1"
                item.type = "datetime"
                item.paramKey = "sign_date"
            })
        }
        mBinding.kvlOpportunity.data = kvList
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnCommit.click {
            val params = mBinding.kvlOpportunity.commit()
            params?.put("order_id",orderId)
            if (params!=null){
                mViewModel.signOrder(params)
            }
        }

    }
}