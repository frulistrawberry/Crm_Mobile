package com.baihe.lib_order.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.baihe.lib_common.constant.KeyConstant
import com.baihe.lib_common.constant.KeyConstant.KEY_CUSTOMER_ID
import com.baihe.lib_common.constant.KeyConstant.KEY_OPPO_ID
import com.baihe.lib_common.constant.KeyConstant.KEY_ORDER_ID
import com.baihe.lib_common.constant.RequestCode
import com.baihe.lib_common.constant.StatusConstant.ORDER_PHASE_CUSTOMER_EFFECTIVE
import com.baihe.lib_common.constant.StatusConstant.ORDER_PHASE_CUSTOMER_TBD
import com.baihe.lib_common.constant.StatusConstant.ORDER_PHASE_INVALID_CUSTOMER
import com.baihe.lib_common.databinding.ActivityAddFollowBinding
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.ui.widget.keyvalue.KeyValueEditLayout
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_order.ui.OrderViewModel

class AddFollowActivity: BaseMvvmActivity<ActivityAddFollowBinding, OrderViewModel>() {
    val reqId by lazy {
        intent.getStringExtra(KeyConstant.KEY_OPPO_ID)
    }
    val customerId by lazy {
        intent.getStringExtra(KeyConstant.KEY_CUSTOMER_ID)
    }
    val orderId by lazy {
        intent.getStringExtra(KeyConstant.KEY_ORDER_ID)
    }

    companion object{
        fun start(context: Activity, reqId:String, customerId:String, orderId:String){
            context.startActivityForResult(Intent(context, AddFollowActivity::class.java).apply {
                putExtra(KEY_OPPO_ID,reqId)
                putExtra(KEY_CUSTOMER_ID,customerId)
                putExtra(KEY_ORDER_ID,orderId)
            },RequestCode.REQUEST_FOLLOW)
        }

        fun start(context: Fragment, reqId:String, customerId:String, orderId:String){
            context.startActivityForResult(
                Intent(context.requireContext(),
                    AddFollowActivity::class.java).apply {
                    putExtra(KEY_OPPO_ID,reqId)
                    putExtra(KEY_CUSTOMER_ID,customerId)
                    putExtra(KEY_ORDER_ID,orderId)
            },1009)
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
            title = "录入跟进"
        }
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.apply {
            //跟进方式
            add(KeyValueEntity().also { item->
                item.name = "跟进方式"
                item.is_channge = "2"
                item.is_true = "1"
                item.is_open = "1"
                item.type = "collection"
                item.paramKey = "contact_way"
                item.option = mutableListOf<KeyValueEntity?>().also { options->
                    options.add(KeyValueEntity().also { option->
                        option.name = "电话"
                        option.value = "1"
                    })
                    options.add(KeyValueEntity().also { option->
                        option.name = "微信"
                        option.value = "2"
                    })
                    options.add(KeyValueEntity().also { option->
                        option.name = "其他"
                        option.value = "3"
                    })
                }
            })
            //跟进结果
            add(KeyValueEntity().also { item->
                item.name = "跟进结果"
                item.is_channge = "2"
                item.is_true = "1"
                item.is_open = "1"
                item.type = "collection"
                item.paramKey = "status"
                item.option = mutableListOf<KeyValueEntity?>().also { options->
                    options.add(KeyValueEntity().also { option->
                        option.name = "客户有效"
                        option.value = ORDER_PHASE_CUSTOMER_EFFECTIVE
                    })
                    options.add(KeyValueEntity().also { option->
                        option.name = "客户待定"
                        option.value = ORDER_PHASE_CUSTOMER_TBD
                    })
                    options.add(KeyValueEntity().also { option->
                        option.name = "客户无效"
                        option.value = ORDER_PHASE_INVALID_CUSTOMER
                    })
                }
            })
            //无效原因
            add(KeyValueEntity().also { item->
                item.name = "无效原因"
                item.is_channge = "2"
                item.is_true = "2"
                item.is_open = "2"
                item.type = "followResult"
                item.paramKey = "uncomment"
                item.subParamKey = "unremark"
                item.option = mutableListOf<KeyValueEntity?>().also { options->
                    options.add(KeyValueEntity().also { option->
                        option.name = "客户无需求"
                        option.value = "1"
                    })
                    options.add(KeyValueEntity().also { option->
                        option.name = "其他渠道已签约"
                        option.value = "2"
                    })
                }
            })
            //预约进店
            add(KeyValueEntity().also { item->
                item.name = "预约进店"
                item.is_channge = "2"
                item.is_true = "2"
                item.is_open = "2"
                item.type = "collection"
                item.paramKey = "arrival_tyep"
                item.option = mutableListOf<KeyValueEntity?>().also { options->
                    options.add(KeyValueEntity().also { option->
                        option.name = "暂不"
                        option.value = "2"
                    })
                    options.add(KeyValueEntity().also { option->
                        option.name = "预约进店"
                        option.value = "1"
                    })
                }
            })
            //进店时间
            add(KeyValueEntity().also { item->
                item.name = "进店时间"
                item.is_channge = "2"
                item.is_true = "2"
                item.is_open = "2"
                item.type = "datetime"
                item.paramKey = "arrival_time"
            })
            //下次回访时间
            add(KeyValueEntity().also { item->
                item.name = "下次回访时间"
                item.is_channge = "2"
                item.is_true = "2"
                item.is_open = "1"
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
                item.paramKey = "comment"
            })
        }
        mBinding.kvlOpportunity.data = kvList
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnCommit.click {
            val params = mBinding.kvlOpportunity.commit()
            params?.put("reqId",reqId)
            params?.put("customerId",customerId)
            params?.put("orderId",orderId)
            if (params!=null){
                mViewModel.addOrderFollow(params)
            }
        }
        mBinding.kvlOpportunity.setOnItemActionListener(object :
            KeyValueEditLayout.OnItemActionListener(){
            override fun onEvent(
                keyValueEntity: KeyValueEntity,
                itemType: KeyValueEditLayout.ItemType?
            ) {
                keyValueEntity.paramKey?.let {
                    if (keyValueEntity.paramKey == "status"){
                        val kvEntity = mBinding.kvlOpportunity.findEntityByParamKey("uncomment")
                        val kvEntity1 = mBinding.kvlOpportunity.findEntityByParamKey("next_contact_time")
                        val kvEntity2 = mBinding.kvlOpportunity.findEntityByParamKey("arrival_tyep")
                        val kvEntity3 = mBinding.kvlOpportunity.findEntityByParamKey("arrival_time")
                        when(keyValueEntity.value){
                            ORDER_PHASE_INVALID_CUSTOMER->{
                                kvEntity.is_open = "1"
                                kvEntity1.is_true = "2"

                                kvEntity1.is_open = "1"
                                kvEntity2.is_true = "2"

                                kvEntity2.is_open = "2"

                                kvEntity3.is_open = "2"

                            }
                            ORDER_PHASE_CUSTOMER_TBD ->{
                                kvEntity.is_open = "2"

                                kvEntity1.is_true = "1"
                                kvEntity1.is_open = "1"

                                kvEntity2.is_true = "2"
                                kvEntity2.is_open = "2"

                                kvEntity3.is_true = "2"
                                kvEntity3.is_open = "2"

                            }
                            ORDER_PHASE_CUSTOMER_EFFECTIVE ->{
                                kvEntity.is_open = "2"

                                kvEntity1.is_true = "2"
                                kvEntity1.is_open = "1"

                                kvEntity2.is_true = "1"
                                kvEntity2.is_open = "1"

                                kvEntity3.is_open = "2"

                            }
                        }
                        mBinding.kvlOpportunity.refresh()
                    }
                }

                if (keyValueEntity.paramKey == "arrival_tyep"){
                    val kvEntity = mBinding.kvlOpportunity.findEntityByParamKey("arrival_time")
                    if (keyValueEntity.value=="1"){
                        kvEntity.is_open = "1"
                        kvEntity.is_true = "1"
                    }else{
                        kvEntity.is_open = "2"
                    }
                    mBinding.kvlOpportunity.refreshItem(kvEntity)
                }
            }

        })
    }
}