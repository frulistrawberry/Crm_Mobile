package com.baihe.lib_common.viewmodel

import androidx.lifecycle.MutableLiveData
import com.baihe.lib_common.api.CommonRepository
import com.baihe.lib_common.entity.*
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_framework.toast.TipsToast
import com.dylanc.loadingstateview.ViewType

class CommonViewModel:BaseViewModel() {

    val loadingDialogLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val loadingStateLiveData: MutableLiveData<ViewType> by lazy {
        MutableLiveData<ViewType>()
    }
    val channelListLiveData: MutableLiveData<List<KeyValueEntity>> by lazy {
        MutableLiveData<List<KeyValueEntity>>()
    }
    val recordUserListLiveData:MutableLiveData<List<RecordUserEntity>> by lazy {
        MutableLiveData<List<RecordUserEntity>>()
    }
    val followDetailLiveData:MutableLiveData<FollowEntity> by lazy{
        MutableLiveData<FollowEntity>()
    }

    val cityListLiveData:MutableLiveData<List<CityEntity>> by lazy {
        MutableLiveData<List<CityEntity>>()
    }

    val stateLiveData:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val photoLiveData:MutableLiveData<List<LocalPhotoEntity>> by lazy {
        MutableLiveData<List<LocalPhotoEntity>>()
    }
    val uploadLiveData:MutableLiveData<List<String?>> by lazy {
        MutableLiveData<List<String?>>()
    }
    private val repository :CommonRepository by lazy {
        CommonRepository(this)
    }

    fun getPhotoList(){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
                _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val data = repository.getPhotoList()
            if (data==null){
                loadingStateLiveData.value = ViewType.EMPTY
            }else{
                photoLiveData.value = data
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
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

    fun getCompanyUser(){
        launchUI({
                _,_ -> loadingDialogLiveData.value = false
        }){
            val result = repository.getCompanyUser()
            recordUserListLiveData.value = result
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

    fun getFollowDetail(followId:String){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
                _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val data = repository.getFollowDetail(followId)
            if (data==null){
                loadingStateLiveData.value = ViewType.EMPTY
            }else{
                followDetailLiveData.value = data
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }
    fun addReqFollow(params:LinkedHashMap<String,Any?>,filePathList: List<String>?){
        loadingDialogLiveData.value = true
        launchUI({
                _,_-> loadingDialogLiveData.value = false
        }) {

            if (filePathList.isNullOrEmpty())
                repository.addFollow(params)
            else
                repository.addReqFollowWithAttachment(params,filePathList)
            stateLiveData.value = true
            loadingDialogLiveData.value = false
        }
    }

    fun getCityList(){
        loadingDialogLiveData.value = true
        launchUI({
            _,_-> loadingDialogLiveData.value = false
        }){
            val cityList = repository.getCityList()
            cityListLiveData.value = cityList
            loadingDialogLiveData.value = false
        }
    }
}