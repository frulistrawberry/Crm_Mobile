package com.baihe.lib_opportunity.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValEventEntity
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.base.BaseMvvmActivity
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
    companion object{
        fun start(context: Context,type:Int,reqId:String){
            if (context is Activity){
                context.startActivityForResult(Intent(context,ActionActivity::class.java).apply {
                    putExtra("TYPE",type)
                    putExtra("reqId",reqId)
                },1001)
            }

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
                    key = "转移给"
                    optional = "2"
                    showStatus = "1"
                    editable = "2"
                    event = KeyValEventEntity().apply {
                        action = "companyUser"
                        paramKey = "ownerId"
                    }

                })
                add(KeyValueEntity().apply {
                    key = "备注"
                    optional = "2"
                    showStatus = "1"
                    editable = "2"
                    event = KeyValEventEntity().apply {
                        action = "input"
                        paramKey = "comment"
                    }

                })
            }
        }else{
            kvList.apply {
                add(KeyValueEntity().apply {
                    key = "客户是否到店"
                    optional = "2"
                    showStatus = "1"
                    editable = "2"
                    event = KeyValEventEntity().apply {
                        action = "collection"
                        paramKey = "arrival_status"
                        options = mutableListOf<KeyValueEntity?>().apply {
                            add(KeyValueEntity().apply {
                                key = "单人到店"
                                `val` = "1"
                            })
                            add(KeyValueEntity().apply {
                                key = "双人到店"
                                `val` = "2"
                            })
                            add(KeyValueEntity().apply {
                                key = "未到店"
                                `val` = "3"
                            })
                        }
                    }

                })
                add(KeyValueEntity().apply {
                    key = "下发订单给"
                    optional = "2"
                    showStatus = "1"
                    editable = "2"
                    event = KeyValEventEntity().apply {
                        action = "companyUser"
                        paramKey = "ownerId"
                    }

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