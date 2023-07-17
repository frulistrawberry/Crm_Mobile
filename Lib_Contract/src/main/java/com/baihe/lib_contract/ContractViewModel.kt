package com.baihe.lib_contract

import androidx.lifecycle.MutableLiveData
import com.baihe.lib_common.viewmodel.BaseViewModel
import com.baihe.lib_contract.api.Repository
import com.dylanc.loadingstateview.ViewType

class ContractViewModel: BaseViewModel() {
    private val repository :Repository by lazy {
        Repository(this)
    }
    val loadingStateLiveData: MutableLiveData<ViewType> by lazy {
        MutableLiveData<ViewType>()
    }
    val loadingDialogLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val listLiveData:MutableLiveData<List<ContractListItemEntity>> by lazy {
        MutableLiveData<List<ContractListItemEntity>>()
    }

    val detailLiveData:MutableLiveData<ContractDetailEntity> by lazy {
        MutableLiveData<ContractDetailEntity>()
    }

    fun getContractList(page:Int,name:String = ""){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
                _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val customerListData = repository.contractList(page,name)
            if (customerListData==null || customerListData.rows.isNullOrEmpty())
                loadingStateLiveData.value = ViewType.EMPTY
            else{
                listLiveData.value = customerListData.rows
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }
    fun getContractDetail(contractId:String){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
                _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val data = repository.contractDetail(contractId)
            if (data==null)
                loadingStateLiveData.value = ViewType.EMPTY
            else{
                detailLiveData.value = data
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }
}