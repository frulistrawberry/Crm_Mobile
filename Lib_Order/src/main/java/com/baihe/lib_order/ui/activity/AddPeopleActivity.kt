package com.baihe.lib_order.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.baihe.lib_common.constant.KeyConstant
import com.baihe.lib_common.constant.RequestCode
import com.baihe.lib_common.constant.RequestCode.REQUEST_ADD_PEOPLE
import com.baihe.lib_common.constant.StatusConstant
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.ui.widget.keyvalue.KeyValueEditLayout
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_order.R
import com.baihe.lib_order.databinding.OrderActivityAddPeopleBinding
import com.baihe.lib_order.ui.OrderViewModel

class AddPeopleActivity: BaseMvvmActivity<OrderActivityAddPeopleBinding, OrderViewModel>() {
    val orderId: String by lazy {
        intent.getStringExtra(KeyConstant.KEY_ORDER_ID)
    }

    companion object{
        fun start(context: Activity,  orderId:String){
            context.startActivityForResult(Intent(context,
                AddPeopleActivity::class.java).apply {
                putExtra(KeyConstant.KEY_ORDER_ID,orderId)
            }, REQUEST_ADD_PEOPLE
            )
        }

        fun start(context: Fragment,  orderId:String){
            context.startActivityForResult(
                Intent(context.requireContext(),
                    AddPeopleActivity::class.java).apply {
                    putExtra(KeyConstant.KEY_ORDER_ID,orderId)
                },REQUEST_ADD_PEOPLE)
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
            title = "添加人员"
        }
        val kvList = mutableListOf<KeyValueEntity>()
        kvList.apply {
            //跟进方式
            add(KeyValueEntity().also { item->
                item.name = "策划人员"
                item.is_channge = "2"
                item.is_true = "2"
                item.is_open = "1"
                item.type = "userlist"
                item.order_id = orderId
            })
            add(KeyValueEntity().also { item->
                item.name = "执行人员"
                item.is_channge = "2"
                item.is_true = "2"
                item.is_open = "1"
                item.type = "userlist"
                item.order_id = orderId
            })
            add(KeyValueEntity().also { item->
                item.name = "督导人员"
                item.is_channge = "2"
                item.is_true = "2"
                item.is_open = "1"
                item.type = "userlist"
                item.order_id = orderId
            })
            add(KeyValueEntity().also { item->
                item.name = "设计人员"
                item.is_channge = "2"
                item.is_true = "2"
                item.is_open = "1"
                item.type = "userlist"
                item.order_id = orderId
            })
            add(KeyValueEntity().also { item->
                item.name = "其他人员"
                item.is_channge = "2"
                item.is_true = "2"
                item.is_open = "1"
                item.type = "userlist"
                item.order_id = orderId
            })
        }
        mBinding.kvlOpportunity.data = kvList
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnCommit.click {
            val params = mBinding.kvlOpportunity.data
                for(index in 0 until mBinding.llCustomPeople.childCount ){
                    val kvl = mBinding.llCustomPeople.getChildAt(index) as KeyValueEditLayout
                    val nameKv = kvl.findEntityByName("角色名称")
                    val valueKv = kvl.findEntityByName("角色人员")
                    if (nameKv!=null && valueKv!=null){
                        val paramsKv = KeyValueEntity().apply {
                            name = nameKv.value
                            value = valueKv.value
                            defaultValue = valueKv.defaultValue
                            order_id = orderId

                        }
                        if (!paramsKv.name.isNullOrEmpty()&&!paramsKv.value.isNullOrEmpty()&&!paramsKv.defaultValue.isNullOrEmpty()){
                            params.add(paramsKv)
                        }
                    }
                }
            if (params!=null){
                mViewModel.addPeople(params)
            }
        }
        mBinding.btnAdd.click {
            val kvl = LayoutInflater.from(this).inflate(R.layout.order_add_people_item,mBinding.llCustomPeople,false) as KeyValueEditLayout
            kvl.data = mutableListOf<KeyValueEntity?>().apply {
                add(KeyValueEntity().also {item->
                    item.name = "角色名称"
                    item.is_channge = "2"
                    item.is_true = "2"
                    item.is_open = "1"
                    item.type = "input"
                })
                add(KeyValueEntity().also {item->
                    item.name = "角色人员"
                    item.is_channge = "2"
                    item.is_true = "2"
                    item.is_open = "1"
                    item.type = "userlist"
                })
            }
            mBinding.llCustomPeople.addView(kvl)

        }
    }
}