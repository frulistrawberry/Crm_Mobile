package com.baihe.lib_opportunity.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.base.BaseMvvmFragment
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.toast.TipsToast
import com.baihe.lib_opportunity.OpportunityViewModel
import com.baihe.lib_opportunity.databinding.OppoCommonActionActivityBinding

class ActionActivity: BaseMvvmActivity<OppoCommonActionActivityBinding, OpportunityViewModel>() {
    val type by lazy {
        intent.getIntExtra("TYPE",-1)
    }
    val reqId by lazy {
        intent.getStringExtra("reqId")
    }
    val customerId by lazy {
        intent.getStringExtra("customerId")
    }
    companion object{
        fun start(context: Context,type:Int,reqId:String,customerId:String){
            if (context is Activity){
                context.startActivityForResult(Intent(context,ActionActivity::class.java).apply {
                    putExtra("TYPE",type)
                    putExtra("reqId",reqId)
                    putExtra("customerId",customerId)
                },1001)
            }

        }
        fun start(fragment: Fragment, type:Int, reqId:String,customerId: String){
            fragment.startActivityForResult(Intent(fragment.requireContext(),ActionActivity::class.java).apply {
                    putExtra("TYPE",type)
                    putExtra("reqId",reqId)
                    putExtra("customerId",customerId)
                },1001)


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
            if (type == 1)
                title = "转移机会"
            else
                title = "下发订单"
        }
        val kvList = mutableListOf<KeyValueEntity>()
        if (type == 1){
            kvList.apply {
                add(KeyValueEntity().apply {
                    name = "转移给"
                    is_true = "2"
                    is_open = "1"
                    is_channge = "2"
                    type = "companyUser"
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
        }else{
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
                    type = "companyUser"
                    paramKey = "ownerId"

                })
            }
        }
        mBinding.kvlRoot.setData(kvList)
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnCommit.click {
            val params = mBinding.kvlRoot.commit()
            params?.put("reqId",reqId)
            params?.put("customerId",customerId)
            if (type == 1){
                if (params?.get("ownerId") == null){
                    TipsToast.showTips("请选择人员")
                    return@click
                }
                if (params?.get("comment") == null){
                    TipsToast.showTips("请填写备注")
                    return@click
                }
                mViewModel.transferOppo(params!!)
            }else{
                if (params?.get("ownerId") == null){
                    TipsToast.showTips("请选择人员")
                    return@click
                }
                if (params?.get("arrival_status") == null){
                    TipsToast.showTips("请选择是否到店")
                    return@click
                }
                mViewModel.dispatchOrder(params!!)
            }
        }
    }
}