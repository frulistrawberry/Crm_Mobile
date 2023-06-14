package com.baihe.lib_home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.baihe.lib_common.viewmodel.BaseViewModel
import com.baihe.lib_framework.widget.state.ktx.LoadingState
import com.baihe.lib_home.DataEntity
import com.baihe.lib_home.HomeEntity
import com.baihe.lib_home.WaitingEntity
import com.baihe.lib_home.api.HomeRepository
import com.dylanc.loadingstateview.ViewType
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.flow.flow
import java.util.concurrent.Flow

class HomeViewModel:BaseViewModel() {
    private val homeRepository :HomeRepository by lazy {
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

    fun getHomeData(){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
            _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val homeData = homeRepository.getHomeData()
            if (homeData == null)
                loadingStateLiveData.value = ViewType.EMPTY
            else{
                waitingListLiveData.value = homeData.waitingEntity
                dataViewLiveData.value = homeData.dataEntity
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }

    fun getWaitingList(page:Int,pageSize:Int = 10){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
            _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val waitingList = homeRepository.getWaitingList(page)
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
            val dataView = homeRepository.getDataView(startDate,endDate)
            dataViewLiveData.value = dataView
            loadingDialogLiveData.value = false
        }
    }





}