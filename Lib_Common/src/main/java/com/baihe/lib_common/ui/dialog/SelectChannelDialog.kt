package com.baihe.lib_common.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.databinding.DialogBottomChannelSelectBinding
import com.baihe.lib_common.ui.dialog.adapter.SelectChannelAdapter
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.ui.dialog.adapter.MultiSelectAdapter
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_common.viewmodel.CommonViewModel
import com.baihe.lib_framework.base.BaseActivity
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.ext.EditTextExt.textChangeFlow
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.utils.DpToPx
import com.baihe.lib_framework.utils.ViewUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SelectChannelDialog {
    class Builder(context: Context):BaseDialog.Builder<Builder>(context){
        private val mBinding by lazy {
            DialogBottomChannelSelectBinding.inflate(LayoutInflater.from(context))
        }
        private val activity = context as BaseActivity
        private val mViewModel by lazy {
            ViewModelProvider(activity).get(CommonViewModel::class.java)
        }

        private val channelList by lazy {
            mutableListOf<KeyValueEntity>()
        }

        private val leftAdapter by lazy {
            SelectChannelAdapter().apply {
                onItemSelectListener = { position ->
                    val data = getData()
                    rightAdapter.setData(data[position].children)
                }
            }
        }

        private val rightAdapter by lazy {
            MultiSelectAdapter(context)
        }
        private var defaultValue: String? = null

        init {
            initViewModel()
            initView()
            initListener()
        }
        private fun setData(data:List<KeyValueEntity>?):Builder {
            var defLeftPosition = 0
            var defParenPosition = -1
            var defChildPosition = -1
            data?.forEach{parent ->
                if (parent.value == defaultValue){
                    defLeftPosition = data.indexOf(parent)
                    return@forEach
                }
            }
            leftAdapter.selectPosition = defLeftPosition
            leftAdapter.setData(data)
            val childData = data?.let {
                data[defLeftPosition].children
            }

            childData?.let {
                childData.forEach{parent ->
                    if (parent.value == defaultValue){
                        defParenPosition = data.indexOf(parent)
                        return@forEach
                    }
                    parent.children?.let {
                        parent.children.forEach { child ->
                            if (child.value == defaultValue){
                                defParenPosition = data.indexOf(parent)
                                defChildPosition = parent.children.indexOf(child)
                                return@forEach
                            }
                        }
                    }
                }
                rightAdapter.selectPosition = defParenPosition
                rightAdapter.childSelectPosition = defChildPosition
                rightAdapter.setData(childData)
            }



            return this
        }

        fun setDefaultValue(defaultValue:String?): Builder {
            this.defaultValue = defaultValue
            return this
        }

        private fun initViewModel(){
            mViewModel.loadingDialogLiveData.observe(activity){
                if (it)
                    activity.showLoadingDialog()
                else
                    activity.dismissLoadingDialog()
            }

            mViewModel.channelListLiveData.observe(activity){
                channelList.clear()
                channelList.addAll(it)
                setData(channelList)
            }
        }

        private fun initView(){
            setContentView(mBinding.root)
            setAnimStyle(BaseDialog.AnimStyle.BOTTOM)
            setWidth(WindowManager.LayoutParams.MATCH_PARENT)
            setHeight(WindowManager.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
            mBinding.bottomChannelLeftRv.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = leftAdapter
                ViewUtils.setClipViewCornerRightRadius(this,DpToPx.dpToPx(20))
            }
            mBinding.bottomChannelRightRv.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = rightAdapter
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        private fun initListener(){
            mBinding.etSearch.textChangeFlow()
                .flowOn(Dispatchers.IO)
                .onEach {
                    rightAdapter.keywords = mBinding.etSearch.text.toString()
                    val data = rightAdapter.getData()
                    val result = mutableListOf<KeyValueEntity>()
                    data.forEach{datum->
                        datum.label?.let {label->
                            if (label.contains(rightAdapter.keywords)){
                                result.add(datum)
                            }else{
                                datum.children?.forEach {child->
                                    child.label.let {
                                       if (label.contains(rightAdapter.keywords)){
                                           result.add(datum)
                                       }
                                   }
                                }
                            }
                        }
                    }
                    rightAdapter.setData(result)
                }
                .launchIn(activity.lifecycleScope)
            mBinding.bottomChannelCancelIv.click {
                dismiss()
            }
        }



        fun setOnConfirmClickListener(onConfirm:(dialog: Dialog?, value:String,label:String)->Unit): Builder {
            mBinding.bottomSelectConfirmTv.click {
                dismiss()
                if (rightAdapter.selectPosition == -1)
                    return@click
                val data = rightAdapter.getData()
                var value:String
                var label:String
                data.let {
                    value = data[rightAdapter.selectPosition].value
                    label = data[rightAdapter.selectPosition].label
                }
                if (rightAdapter.childSelectPosition!=-1){
                    data.let {
                        value = data[rightAdapter.selectPosition]
                            .children[rightAdapter.childSelectPosition]
                            .value
                        label = data[rightAdapter.selectPosition]
                            .children[rightAdapter.childSelectPosition]
                            .label
                    }
                }
                onConfirm.invoke(dialog, value,label)
            }
            return this
        }

        override fun create(): BaseDialog {
            mViewModel.getChannelList()
            return super.create()
        }

    }
}