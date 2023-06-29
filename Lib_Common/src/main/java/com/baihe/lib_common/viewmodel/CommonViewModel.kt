package com.baihe.lib_common.viewmodel

import androidx.lifecycle.MutableLiveData
import com.baihe.lib_common.api.CommonRepository
import com.baihe.lib_common.entity.ChannelEntity
import com.baihe.lib_common.entity.RecordUserEntity
import com.baihe.lib_framework.toast.TipsToast

class CommonViewModel:BaseViewModel() {

    val loadingDialogLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val channelListLiveData: MutableLiveData<List<ChannelEntity>> by lazy {
        MutableLiveData<List<ChannelEntity>>()
    }
    val recordUserListLiveData:MutableLiveData<List<RecordUserEntity>> by lazy {
        MutableLiveData<List<RecordUserEntity>>()
    }
    private val repository :CommonRepository by lazy {
        CommonRepository(this)
    }

    fun getChannelList(){
        launchUI({
            _,_ -> loadingDialogLiveData.value = false
        }){
            val result = repository.getUserChannelList()
            channelListLiveData.value = result
            loadingDialogLiveData.value = false
        }
    }

    fun getRecordUser(channelId:String){
        launchUI({
                _,_ -> loadingDialogLiveData.value = false
        }){
            val result = repository.getRecordUser(channelId)
            recordUserListLiveData.value = result?.recordUserList
            loadingDialogLiveData.value = false
        }
    }

    fun call(customerId:String){
        launchUI({
            _,_ -> loadingDialogLiveData.value = false
        }){
            repository.call(customerId)
            loadingDialogLiveData.value = false
            TipsToast.showTips("外呼成功，请等待接听")
        }
    }
}