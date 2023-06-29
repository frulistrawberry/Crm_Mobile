package com.baihe.lib_user.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.baihe.lib_common.R
import com.baihe.lib_common.ext.ActivityExt.dismissLoadingDialog
import com.baihe.lib_common.ext.ActivityExt.showLoadingDialog
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_framework.base.BaseActivity
import com.baihe.lib_framework.base.BaseDialog
import com.baihe.lib_framework.ext.RecyclerViewExt.divider
import com.baihe.lib_framework.ext.ResourcesExt.color
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.utils.DpToPx
import com.baihe.lib_user.UserViewModel
import com.baihe.lib_user.databinding.UserDialogBossSeaBinding


class BossSeaDialog {
    @SuppressLint("NotifyDataSetChanged")
    class Builder(context: Context):BaseDialog.Builder<Builder>(context){
        private val activity = context as BaseActivity
        private val userViewModel by lazy {
           ViewModelProvider(activity).get(UserViewModel::class.java)
        }
        var onCompanySelectListener:((id:String,name:String)->Unit)? = null
        private val mBinding = UserDialogBossSeaBinding.inflate(
            LayoutInflater.from(context)
        )

       init {
           setContentView(mBinding.root)
           setGravity(Gravity.TOP)
           setAnimStyle(BaseDialog.AnimStyle.TOP)
           mBinding.tvBoss.text = UserServiceProvider.getCompanyName()
           mBinding.btnBossSea.click {
               dismiss()
           }
           userViewModel.loadingDialogLiveData.observe(activity){
               if (it)
                   activity.showLoadingDialog()
               else
                   activity.dismissLoadingDialog()
           }

           userViewModel.bossSeaLiveData.observe(activity){
               mBinding.rvBossSea.apply {
                   layoutManager = LinearLayoutManager(activity)
                   divider()
                   adapter = BossSeaListAdapter().apply {
                       onItemClickListener = { _, position ->
                           val name = it[position].name
                           val id = it[position].id
                           mBinding.tvBoss.text = name
                           val user = UserServiceProvider.getUser()
                           user?.company_id = id.toInt()
                           user?.company_name = name
                           UserServiceProvider.saveUser(user)
                           adapter?.notifyDataSetChanged()
                           onCompanySelectListener?.invoke(id,name)
                           dialog?.dismiss()
                       }
                       setData(it)
                       for (entity in it){
                           if (entity.id == UserServiceProvider.getCompanyId()){
                               mBinding.rvBossSea.scrollToPosition(it.indexOf(entity))
                           }
                       }
                   }

               }
           }

           addOnShowListener(object :BaseDialog.OnShowListener{
               override fun onShow(dialog: BaseDialog?) {
                   val adapter = mBinding.rvBossSea.adapter
                   if (adapter!=null){
                       val dataList = (adapter as BossSeaListAdapter).getData()
                       for (entity in dataList){
                           if (entity.id == UserServiceProvider.getCompanyId()){
                               mBinding.rvBossSea.scrollToPosition(dataList.indexOf(entity))
                           }
                       }
                   }
               }

           })

           userViewModel.getBossSea()

       }

    }
}