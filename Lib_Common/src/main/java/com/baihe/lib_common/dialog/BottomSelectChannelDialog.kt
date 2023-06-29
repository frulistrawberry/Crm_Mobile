package com.baihe.lib_common.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.databinding.DialogBottomChannelSelectBinding
import com.baihe.lib_common.databinding.DialogBottomSelectBinding
import com.baihe.lib_common.dialog.adapter.SelectChannelAdapter
import com.baihe.lib_common.dialog.adapter.SelectChannelParentAdapter
import com.baihe.lib_common.entity.ChannelEntity
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.viewmodel.CommonViewModel
import com.baihe.lib_framework.base.BaseActivity
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.ext.EditTextExt.textChangeFlow
import com.baihe.lib_framework.ext.RecyclerViewExt.divider
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.toast.TipsToast
import com.baihe.lib_framework.utils.DpToPx
import com.baihe.lib_framework.utils.ViewUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class BottomSelectChannelDialog {
    class Builder(context: Context):BaseDialog.Builder<Builder>(context){
        private val mBinding by lazy {
            DialogBottomChannelSelectBinding.inflate(LayoutInflater.from(context))
        }
        private val activity = context as BaseActivity
        private val mViewModel by lazy {
            ViewModelProvider(activity).get(CommonViewModel::class.java)
        }

        private val parentAdapter by lazy {
            SelectChannelParentAdapter()
        }

        lateinit var channelAdapter: SelectChannelAdapter

        private  var parentPosition:Int = 0
        private  var position:Int = -1
        private  var childPosition:Int = -1
        private var defValue = -1
        private var isInit = true
        private var channelList:List<ChannelEntity>? = null


        init {
            initViewModel()
            initView()
            initListener()
            initData()
        }

        private fun initViewModel(){
            mViewModel.loadingDialogLiveData.observe(activity){
                if (it)
                    activity.showLoadingDialog()
                else
                    activity.dismissLoadingDialog()
            }

            mViewModel.channelListLiveData.observe(activity){
                if (defValue!=-1){
                    for(parent in it){
                        for (channel in parent.children?:continue){
                            for(child in channel.children?:continue){
                                if (child.value==defValue){
                                    childPosition = channel.children!!.indexOf(child)
                                    position = parent.children!!.indexOf(channel)
                                    parentPosition = it.indexOf(parent)
                                    break
                                }

                            }

                        }
                    }
                }
                channelList = it
                parentAdapter.setData(it)
                parentAdapter.onItemSelectListener?.invoke(parentPosition)
            }
        }

        private fun initView(){
            setContentView(mBinding.root)
            setAnimStyle(BaseDialog.AnimStyle.BOTTOM)
            setWidth(WindowManager.LayoutParams.MATCH_PARENT)
            setHeight(WindowManager.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)

            mBinding.bottomChannelFirstLevelRv.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = parentAdapter.apply {
                    selectPosition = parentPosition
                }
                ViewUtils.setClipViewCornerRightRadius(this,DpToPx.dpToPx(20))
            }


        }

        private fun initListener(){
            mBinding.etSearch.textChangeFlow()
                .flowOn(Dispatchers.IO)
                .onEach {
                    val keywords = mBinding.etSearch.text.toString()
                    val result = mutableListOf<ChannelEntity>()
                    channelAdapter.keywords = keywords
                    if (channelList==null||channelList!!.isEmpty())
                        return@onEach
                    if (channelList!![parentPosition].children==null || channelList!![parentPosition].children!!.isEmpty())
                        return@onEach
                    channelList!![parentPosition].children!!.forEach {parent->
                        if (parent.children==null || parent.children!!.isEmpty())
                            return@onEach
                        val childList = mutableListOf<ChannelEntity>()
                        parent.children!!.forEach {
                            if (it.label.contains(keywords)){
                                childList.add(it)
                            }
                        }
                        if (childList.isNotEmpty()){
                            result.add(parent.apply {
                                children = childList
                            })
                        }
                    }
                    channelAdapter.needAllExpand = true
                    channelAdapter.setData(result)

                }
                .launchIn(activity.lifecycleScope)


            mBinding.bottomChannelCancelIv.click {
                dismiss()
            }

            parentAdapter.onItemSelectListener = {  position ->
                val parentChannelList = parentAdapter.getData()
                parentPosition = position
                if (!isInit){
                    this@Builder.position = -1
                    this@Builder.childPosition = -1
                }
                isInit = false
                var channelList = parentChannelList[position].children
                if (channelList==null || channelList.isEmpty()){
                    channelList = mutableListOf()
                }
                val channelAdapter = SelectChannelAdapter()
                mBinding.bottomChannelSecondLevelRv.apply {
                    layoutManager = LinearLayoutManager(context)
                    this@Builder.channelAdapter = channelAdapter.also {
                        it.selectPosition = this@Builder.position
                        it.setData(channelList)
                        it.childSelectPosition = childPosition
                        it.onItemSelectListener = {_,position,childPosition ->
                            this@Builder.position = position
                            this@Builder.childPosition = childPosition
                        }
                    }
                    adapter = this@Builder.channelAdapter
                }
            }

        }

        private fun initData(){
            mViewModel.getChannelList()
        }

        fun setDefValue(defValue: Int):Builder{
            this.defValue = defValue
            return this
        }

        fun setOnConfirmClickListener(onConfirm:(dialog: Dialog?, id:Int,label:String)->Unit): Builder {
            mBinding.bottomSelectConfirmTv.click {
                childPosition = channelAdapter.childSelectPosition
                if(parentPosition == -1){
                    TipsToast.showTips("请选择一级渠道")
                    return@click
                }
                if (position == -1){
                    TipsToast.showTips("请选择二级渠道")
                    return@click
                }
                if (childPosition == -1){
                    TipsToast.showTips("请选择三级渠道")
                    return@click
                }
                val channelList = channelAdapter.getData()
                if (channelList.isNotEmpty()){
                    val childChannelList = channelList[position].children
                    if (childChannelList!=null && childChannelList.isNotEmpty()){
                        onConfirm.invoke(dialog,childChannelList[channelAdapter.childSelectPosition].value,childChannelList[channelAdapter.childSelectPosition].label)
                    }
                }
            }
            return this
        }

    }
}