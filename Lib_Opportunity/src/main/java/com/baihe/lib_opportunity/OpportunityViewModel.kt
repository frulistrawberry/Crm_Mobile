package com.baihe.lib_opportunity

import androidx.lifecycle.MutableLiveData
import com.baihe.lib_common.viewmodel.BaseViewModel
import com.baihe.lib_opportunity.api.OpportunityRepository
import com.dylanc.loadingstateview.ViewType

class OpportunityViewModel: BaseViewModel() {
    private val opportunityRepository by lazy {
        OpportunityRepository(this)
    }
    val loadingStateLiveData: MutableLiveData<ViewType> by lazy {
        MutableLiveData<ViewType>()
    }
    val oppoListLiveData:MutableLiveData<List<OpportunityListItemEntity>> by lazy {
        MutableLiveData<List<OpportunityListItemEntity>>()
    }

    fun getOppoList(page:Int,isHistorical:String="0",keywords:String?=null,filter:LinkedHashMap<String,Any?>?=null){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
                _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val listData = opportunityRepository.opportunityList(page,isHistorical,keywords,filter)
            if (listData==null || listData.rows.isNullOrEmpty())
                loadingStateLiveData.value = ViewType.EMPTY
            else{
                oppoListLiveData.value = listData.rows
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }

//    fun getOppoDetail(oppoId:String)
}