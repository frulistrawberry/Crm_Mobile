package com.baihe.lib_opportunity

import androidx.lifecycle.MutableLiveData
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_common.viewmodel.BaseViewModel
import com.baihe.lib_opportunity.api.OpportunityRepository
import com.dylanc.loadingstateview.ViewType

class OpportunityViewModel: BaseViewModel() {
    private val opportunityRepository by lazy {
        OpportunityRepository(this)
    }
    val loadingDialogLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val loadingStateLiveData: MutableLiveData<ViewType> by lazy {
        MutableLiveData<ViewType>()
    }
    val oppoListLiveData:MutableLiveData<List<OpportunityListItemEntity>> by lazy {
        MutableLiveData<List<OpportunityListItemEntity>>()
    }
    val oppoDetailLiveData:MutableLiveData<OpportunityDetailEntity> by lazy {
        MutableLiveData<OpportunityDetailEntity>()
    }
    val oppoTempleLiveData:MutableLiveData<List<KeyValueEntity>> by lazy {
        MutableLiveData<List<KeyValueEntity>>()
    }

    val oppoAddOrUpdateLiveData:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
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

    fun getOppoDetail(oppoId:String){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
                _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val data = opportunityRepository.opportunityDetail(oppoId)
            if (data==null)
                loadingStateLiveData.value = ViewType.EMPTY
            else{
                oppoDetailLiveData.value = data
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }

    fun getOppoTemple(oppoId: String?,customerId: String?){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
                _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val data = opportunityRepository.getOpportunityTemple(oppoId,customerId)
            if (data==null)
                loadingStateLiveData.value = ViewType.EMPTY
            else{
                oppoTempleLiveData.value = data.row
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }

    fun addOrUpdateOpportunity(params:LinkedHashMap<String,Any?>,oppoId: String?,customerId: String?){
        loadingDialogLiveData.value = true
        launchUI({
                _,_-> loadingDialogLiveData.value = false
        }) {
            if (oppoId.isNullOrEmpty())
                opportunityRepository.addOpportunity(params)
            else{
                params["reqId"] = oppoId
                params["customerId"] = customerId
                opportunityRepository.updateOpportunity(params)
            }
            oppoAddOrUpdateLiveData.value = true
        }
    }

    fun dispatchOrder(params:LinkedHashMap<String,Any?>){
        loadingDialogLiveData.value = true
        launchUI({
                _,_-> loadingDialogLiveData.value = false
        }) {

            opportunityRepository.dispatchOrder(params)

            oppoAddOrUpdateLiveData.value = true
        }
    }
    fun transferOppo(params:LinkedHashMap<String,Any?>){
        loadingDialogLiveData.value = true
        launchUI({
                _,_-> loadingDialogLiveData.value = false
        }) {

            opportunityRepository.transferOpportunity(params)

            oppoAddOrUpdateLiveData.value = true
        }
    }

    fun deleteOppo(params:String){
        loadingDialogLiveData.value = true
        launchUI({
                _,_-> loadingDialogLiveData.value = false
        }) {

            opportunityRepository.deleteOpportunity(params)

            oppoAddOrUpdateLiveData.value = true
        }
    }

}