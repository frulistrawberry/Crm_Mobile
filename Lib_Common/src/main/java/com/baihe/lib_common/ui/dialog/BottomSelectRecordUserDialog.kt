package com.baihe.lib_common.ui.dialog

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.databinding.DialogBottomSelectBinding
import com.baihe.lib_common.ui.dialog.adapter.SelectDataAdapter
import com.baihe.lib_common.ui.dialog.adapter.SingleSelectAdapter
import com.baihe.lib_common.entity.RecordUserEntity
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.viewmodel.CommonViewModel
import com.baihe.lib_framework.base.BaseActivity
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.ext.RecyclerViewExt.divider
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.toast.TipsToast

class BottomSelectRecordUserDialog {
    class Builder(context: Context,channelId:String):BaseDialog.Builder<Builder>(context){
        private val mBinding by lazy {
            DialogBottomSelectBinding.inflate(LayoutInflater.from(context))
        }
        private lateinit var singleSelectAdapter: SingleSelectAdapter
        private var id:Int = -1
        private var name:String = ""
        private val activity = context as BaseActivity
        private val mViewModel by lazy {
            ViewModelProvider(activity).get(CommonViewModel::class.java)
        }
        private var defValue = -1
        private var recordUserList:List<RecordUserEntity>? = null

        init {
            setContentView(mBinding.root)
            setAnimStyle(BaseDialog.AnimStyle.BOTTOM)
            setWidth(WindowManager.LayoutParams.MATCH_PARENT)
            setHeight(WindowManager.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.BOTTOM)
            mBinding.bottomSelectListRv.layoutManager = LinearLayoutManager(context)
            mBinding.bottomSelectListRv.divider()
            mBinding.bottomSelectCancelIv.click {
                dismiss()
            }
            mBinding.bottomSelectTitleTv.text = "请选择提供人"
            mViewModel.loadingDialogLiveData.observe(activity){
                if (it)
                    activity.showLoadingDialog()
                else
                    activity.dismissLoadingDialog()
            }
            mViewModel.recordUserListLiveData.observe(activity){
                recordUserList = it
                var initSelectDataPosition = -1
                for (i in it.indices) {
                    val option = it[i]
                    if (!TextUtils.isEmpty(option.name) && option.id == defValue.toString()) {
                        initSelectDataPosition = i
                    }
                }
               val selectDataAdapter = object : SelectDataAdapter(){
                   override fun getCount(): Int {
                       if (it == null){
                           return 0
                       }
                       return it.size
                   }

                   override fun getText(dataPosition: Int): String? {
                       return it[dataPosition].name
                   }

                   override fun initSelectDataPosition(): Int {
                       return initSelectDataPosition
                   }

               }
                singleSelectAdapter = SingleSelectAdapter(selectDataAdapter)
                mBinding.bottomSelectListRv.adapter = singleSelectAdapter
            }

            mViewModel.getRecordUser(channelId)


        }

        fun setOnConfirmClickListener(onConfirm:(dialog: Dialog?, id:Int,value:String)->Unit): Builder {
            mBinding.bottomSelectConfirmTv.click {
                val position = singleSelectAdapter.selectPosition
                if (recordUserList!=null){
                    id = recordUserList!![position].id.toInt()
                    name = recordUserList!![position].name
                }
                if (id == -1 || name.isEmpty()){
                    TipsToast.showTips("请选择提供人")
                    return@click
                }
                onConfirm.invoke(dialog,id,name)
            }
            return this
        }

        fun setDefValue(defValue: Int): Builder {
            this.defValue = defValue
            return this
        }


    }
}