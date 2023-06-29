package com.baihe.lib_home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.baihe.lib_common.viewmodel.BaseViewModel
import com.baihe.lib_framework.ext.TimeExt.formattedDate
import com.baihe.lib_framework.widget.state.ktx.LoadingState
import com.baihe.lib_home.DataEntity
import com.baihe.lib_home.HomeEntity
import com.baihe.lib_home.WaitingEntity
import com.baihe.lib_home.api.HomeRepository
import com.dylanc.loadingstateview.ViewType

class HomeViewModel:BaseViewModel() {
    private val repository :HomeRepository by lazy {
        HomeRepository(this)
    }
    val loadingStateLiveData:MutableLiveData<ViewType> by lazy {
        MutableLiveData<ViewType>()
    }

    val loadingDialogLiveData:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val waitingListLiveData:MutableLiveData<List<WaitingEntity>> by lazy {
        MutableLiveData<List<WaitingEntity>>()
    }

    val dataViewLiveData:MutableLiveData<DataEntity> by lazy {
        MutableLiveData<DataEntity>()
    }

    fun getHomeData(startDate: String=System.currentTimeMillis().formattedDate(),endDate: String=System.currentTimeMillis().formattedDate()){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
            _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val homeData = repository.getHomeData(startDate,endDate)
            if (homeData == null)
                loadingStateLiveData.value = ViewType.EMPTY
            else{
                waitingListLiveData.value = homeData.waitingEntity
                dataViewLiveData.value = homeData.dataEntity
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }

    fun getWaitingList(page:Int,type:String){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
            _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val waitingList = repository.getWaitingList(page,type)
            if (waitingList==null)
                loadingStateLiveData.value = ViewType.EMPTY
            else{
                waitingListLiveData.value = waitingList
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }

    fun getDataView(startDate:String,endDate:String){
        loadingDialogLiveData.value = true
        launchUI({_,_->
            loadingDialogLiveData.value = false
            dataViewLiveData.value = null
        }){
            val dataView = repository.getDataView(startDate,endDate)
            dataViewLiveData.value = dataView
            loadingDialogLiveData.value = false
        }
    }





}