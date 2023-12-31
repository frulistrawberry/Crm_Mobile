package com.baihe.lib_common.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.baihe.lib_common.R
import com.baihe.lib_common.constant.KeyConstant
import com.baihe.lib_common.constant.RequestCode
import com.baihe.lib_common.databinding.ActivityAddFollowBinding
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.ui.widget.keyvalue.KeyValueEditLayout
import com.baihe.lib_common.ui.widget.keyvalue.KeyValueEditLayout.OnItemActionListener
import com.baihe.lib_common.ui.widget.keyvalue.adapter.AttachImageAdapter
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_common.viewmodel.CommonViewModel
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.ext.ViewExt.visible
import com.baihe.lib_framework.manager.AppManager
import com.baihe.lib_framework.utils.DpToPx
import com.baihe.lib_framework.utils.ViewUtils

class AddFollowActivity: BaseMvvmActivity<ActivityAddFollowBinding, CommonViewModel>() {
    val type by lazy{
        intent.getIntExtra("type",-1)
    }
    val reqId by lazy {
        intent.getStringExtra("reqId")
    }
    val customerId by lazy {
        intent.getStringExtra("customerId")
    }
    val orderStatus by lazy {
        intent.getStringExtra("orderStatus")
    }
    private val attachImageAdapter by lazy {
        AttachImageAdapter().apply {
            spanCount = 4
            val addImageView = LayoutInflater.from(this@AddFollowActivity)
                .inflate(R.layout.layout_keyvalue_item_attach, mBinding.rvAttach, false)
            val imageView = addImageView.findViewById<ImageView>(R.id.imageview)
            imageView.setImageResource(R.mipmap.ic_photo_add)
            ViewUtils.setClipViewCornerRadius(imageView,DpToPx.dpToPx(14))
            addFootView(addImageView, -1)
            imageView.click {
                PhotoPickActivity.start(this@AddFollowActivity)
            }
        }
    }
    var imageList = mutableListOf<String>()

    companion object{
        fun startOppoFollow(context: Context,reqId:String,customerId:String,orderStatus:String){
            if (context is Activity){
                context.startActivityForResult(Intent(context,AddFollowActivity::class.java).apply {
                    putExtra("reqId",reqId)
                    putExtra("customerId",customerId)
                    putExtra("orderStatus",orderStatus)
                    putExtra("type",1)
                },1009)
            }
        }

        fun startOppoFollow(context: Fragment,reqId:String,customerId:String,orderStatus:String){
            context.startActivityForResult(Intent(context.requireContext(),AddFollowActivity::class.java).apply {
                putExtra("reqId",reqId)
                putExtra("customerId",customerId)
                putExtra("orderStatus",orderStatus)
                putExtra("type",1)
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
            navIcon = R.mipmap.navigation_icon
        }
        mBinding.llAttachment.visible()

        val kvList = mutableListOf<KeyValueEntity>()
        if (orderStatus == null || orderStatus == "0"||orderStatus == "3"||orderStatus == "4"){
            kvList.apply {
                //跟进方式
                add(KeyValueEntity().also {item->
                    item.name = "跟进方式"
                    item.is_channge = "2"
                    item.is_true = "1"
                    item.is_open = "1"
                    item.type = "collection"
                    item.paramKey = "contact_way"
                    item.option = mutableListOf<KeyValueEntity?>().also {options->
                        options.add(KeyValueEntity().also {option->
                            option.name = "电话"
                            option.value = "1"
                        })
                        options.add(KeyValueEntity().also {option->
                            option.name = "微信"
                            option.value = "2"
                        })
                        options.add(KeyValueEntity().also {option->
                            option.name = "其他"
                            option.value = "3"
                        })
                    }
                })
                //跟进结果
                add(KeyValueEntity().also {item->
                    item.name = "跟进结果"
                    item.is_channge = "2"
                    item.is_true = "1"
                    item.is_open = "1"
                    item.type = "collection"
                    item.paramKey = "status"
                    item.option = mutableListOf<KeyValueEntity?>().also {options->
                        options.add(KeyValueEntity().also {option->
                            option.name = "客户有效"
                            option.value = "230"
                        })
                        options.add(KeyValueEntity().also {option->
                            option.name = "客户待定"
                            option.value = "220"
                        })
                        options.add(KeyValueEntity().also {option->
                            option.name = "客户无效"
                            option.value = "210"
                        })
                    }
                })
                //无效原因
                add(KeyValueEntity().also {item->
                    item.name = "无效原因"
                    item.is_channge = "2"
                    item.is_true = "2"
                    item.is_open = "2"
                    item.type = "followResult"
                    item.paramKey = "uncomment"
                    item.subParamKey = "unremark"
                    item.option = mutableListOf<KeyValueEntity?>().also {options->
                        options.add(KeyValueEntity().also {option->
                            option.name = "客户无需求"
                            option.value = "1"
                        })
                        options.add(KeyValueEntity().also {option->
                            option.name = "其他渠道已签约"
                            option.value = "2"
                        })
                    }
                })
                //预约进店
                add(KeyValueEntity().also {item->
                    item.name = "预约进店"
                    item.is_channge = "2"
                    item.is_true = "2"
                    item.is_open = "2"
                    item.type = "collection"
                    item.paramKey = "arrival_tyep"
                    item.option = mutableListOf<KeyValueEntity?>().also {options->
                        options.add(KeyValueEntity().also {option->
                            option.name = "暂不"
                            option.value = "2"
                        })
                        options.add(KeyValueEntity().also {option->
                            option.name = "预约进店"
                            option.value = "1"
                        })
                    }
                })
                //进店时间
                add(KeyValueEntity().also {item->
                    item.name = "进店时间"
                    item.is_channge = "2"
                    item.is_true = "2"
                    item.is_open = "2"
                    item.type = "datetime"
                    item.paramKey = "arrival_time"
                })
                //下次回访时间
                add(KeyValueEntity().also {item->
                    item.name = "下次回访时间"
                    item.is_channge = "2"
                    item.is_true = "2"
                    item.is_open = "1"
                    item.type = "datetime"
                    item.paramKey = "next_contact_time"
                })
                //沟通内容
                add(KeyValueEntity().also {item->
                    item.name = "沟通内容"
                    item.is_channge = "2"
                    item.is_true = "1"
                    item.is_open = "1"
                    item.type = "input"
                    item.paramKey = "comment"
                })
                //下发订单
                add(KeyValueEntity().also {item->
                    item.name = "下发订单"
                    item.is_channge = "2"
                    item.is_true = "2"
                    item.is_open = "1"
                    item.type = "userlist"
                    item.paramKey = "ownerId"
                })
            }
        }else{
            kvList.apply {
                add(KeyValueEntity().also {item->
                    item.name = "跟进方式"
                    item.is_channge = "2"
                    item.is_true = "1"
                    item.is_open = "1"
                    item.type = "collection"
                    item.paramKey = "contact_way"
                    item.option = mutableListOf<KeyValueEntity?>().also {options->
                        options.add(KeyValueEntity().also {option->
                            option.name = "电话"
                            option.value = "1"
                        })
                        options.add(KeyValueEntity().also {option->
                            option.name = "微信"
                            option.value = "2"
                        })
                        options.add(KeyValueEntity().also {option->
                            option.name = "其他"
                            option.value = "3"
                        })
                    }
                })
                add(KeyValueEntity().also {item->
                    item.name = "沟通内容"
                    item.is_channge = "2"
                    item.is_true = "1"
                    item.is_open = "1"
                    item.type = "input"
                    item.paramKey = "comment"
                })
            }
        }
        mBinding.kvlOpportunity.setData(kvList)
        mBinding.rvAttach.layoutManager = GridLayoutManager(this,4)
        mBinding.rvAttach.adapter = attachImageAdapter
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnCommit.click {
            if (type == 1){
                val params = mBinding.kvlOpportunity.commit()
                params?.put("reqId",reqId)
                params?.put("customerId",customerId)
                if (params!=null){
                    mViewModel.addReqFollow(params,imageList)
                }

            }
        }
        mBinding.kvlOpportunity.setOnItemActionListener(object :OnItemActionListener(){
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
                        val kvEntity4 = mBinding.kvlOpportunity.findEntityByParamKey("ownerId")
                        when(keyValueEntity.value){
                            "210"->{
                                kvEntity.is_open = "1"
                                kvEntity1.is_true = "2"

                                kvEntity1.is_open = "1"
                                kvEntity2.is_true = "2"

                                kvEntity2.is_open = "2"

                                kvEntity3.is_open = "2"

                                kvEntity4.is_open = "2"
                            }
                            "220" ->{
                                kvEntity.is_open = "2"

                                kvEntity1.is_true = "1"
                                kvEntity1.is_open = "1"

                                kvEntity2.is_true = "2"
                                kvEntity2.is_open = "2"

                                kvEntity3.is_true = "2"
                                kvEntity3.is_open = "2"

                                kvEntity4.is_true = "2"
                                kvEntity4.is_open = "2"
                            }
                            "230" ->{
                                kvEntity.is_open = "2"

                                kvEntity1.is_true = "2"
                                kvEntity1.is_open = "1"

                                kvEntity2.is_true = "1"
                                kvEntity2.is_open = "1"

                                kvEntity3.is_open = "2"

                                kvEntity4.is_true = "2"
                                kvEntity4.is_open = "1"
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.REQUEST_PHOTO && resultCode == RESULT_OK){
            data?.let {
                imageList.clear()
                imageList.addAll(data.getStringArrayListExtra(KeyConstant.KEY_PHOTOS))
                attachImageAdapter.addAll(imageList)
            }
        }
    }
}