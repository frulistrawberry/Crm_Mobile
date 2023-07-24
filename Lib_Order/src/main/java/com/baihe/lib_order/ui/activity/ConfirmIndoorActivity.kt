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

class ConfirmIndoorActivity: BaseMvvmActivity<ActivityAddFollowBinding, OrderViewModel>() {
    val reqId by lazy {
        intent.getStringExtra(KeyConstant.KEY_OPPO_ID)
    }
    val orderId by lazy {
        intent.getStringExtra(KeyConstant.KEY_ORDER_ID)
    }

    companion object{
        fun start(context: Activity, reqId:String,  orderId:String){
            context.startActivityForResult(Intent(context, ConfirmIndoorActivity::class.java).apply {
                putExtra(KeyConstant.KEY_OPPO_ID,reqId)
                putExtra(KeyConstant.KEY_ORDER_ID,orderId)
            }, RequestCode.REQUEST_FOLLOW)
        }

        fun start(context: Fragment, reqId:String,  orderId:String){
            context.startActivityForResult(
                Intent(context.requireContext(),
                    ConfirmIndoorActivity::class.java).apply {
                    putExtra(KeyConstant.KEY_OPPO_ID,reqId)
                    putExtra(KeyConstant.KEY_ORDER_ID,orderId)
                },RequestCode.REQUEST_FOLLOW)
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
            title = "确认到店"
        }
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.apply {
            //跟进方式
            add(KeyValueEntity().also { item->
                item.name = "到店结果"
                item.is_channge = "2"
                item.is_true = "1"
                item.is_open = "1"
                item.type = "collection"
                item.paramKey = "result"
                item.option = mutableListOf<KeyValueEntity?>().also { options->
                    options.add(KeyValueEntity().also { option->
                        option.name = "有效到店"
                        option.value = "1"
                    })
                    options.add(KeyValueEntity().also { option->
                        option.name = "无效到店"
                        option.value = "2"
                    })
                }
            })
            //进店时间
            add(KeyValueEntity().also { item->
                item.name = "到店时间"
                item.is_channge = "2"
                item.is_true = "1"
                item.is_open = "1"
                item.type = "datetime"
                item.paramKey = "arrival_time"
            })
            add(KeyValueEntity().also { item->
                item.name = "是否需要回访"
                item.is_channge = "2"
                item.is_true = "2"
                item.is_open = "1"
                item.type = "collection"
                item.paramKey = ""
                item.option = mutableListOf<KeyValueEntity?>().also { options->
                    options.add(KeyValueEntity().also { option->
                        option.name = "需要"
                        option.value = "1"
                    })
                    options.add(KeyValueEntity().also { option->
                        option.name = "不需要"
                        option.value = "2"
                    })
                }
            })
            //下次回访时间
            add(KeyValueEntity().also { item->
                item.name = "下次回访时间"
                item.is_channge = "2"
                item.is_true = "2"
                item.is_open = "2"
                item.type = "datetime"
                item.paramKey = "next_contact_time"
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
        mBinding.kvlOpportunity.setOnItemActionListener(object :
            KeyValueEditLayout.OnItemActionListener(){
            override fun onEvent(
                keyValueEntity: KeyValueEntity,
                itemType: KeyValueEditLayout.ItemType?
            ) {
                keyValueEntity.paramKey?.let {
                    if (keyValueEntity.name == "是否需要回访"){
                        val kvEntity1 = mBinding.kvlOpportunity.findEntityByParamKey("next_contact_time")
                        when(keyValueEntity.value){
                            "1" ->{
                                kvEntity1.is_true = "2"
                                kvEntity1.is_open = "1"
                            }
                            "2"->{
                                kvEntity1.is_true = "2"
                                kvEntity1.is_open = "2"
                            }
                        }
                        mBinding.kvlOpportunity.refresh()
                    }
                }
            }

        })
    }
}