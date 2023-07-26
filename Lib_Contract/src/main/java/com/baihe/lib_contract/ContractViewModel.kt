package com.baihe.lib_contract

import androidx.lifecycle.MutableLiveData
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
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
    val tempLiveData:MutableLiveData<ContractTemple> by lazy {
        MutableLiveData<ContractTemple>()
    }
    val resultLiveData:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
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

    fun getContractTemp(orderId: String?,contractId: String?=null){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
                _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val data = repository.getContractTemple(orderId,contractId)
            if (data==null)
                loadingStateLiveData.value = ViewType.EMPTY
            else{
                tempLiveData.value = data.row
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }

    fun addOrUpdateContract(params:LinkedHashMap<String,Any?>,orderId: String?,contractId: String?=null,attachment:List<String>){
        loadingDialogLiveData.value = true
        launchUI({
                _,_-> loadingDialogLiveData.value = false
        }) {

            if (!orderId.isNullOrEmpty()){
                params["order_id"] = orderId
            }
            if (contractId.isNullOrEmpty()){
                if (attachment.isEmpty()){
                    repository.addContract(params)
                }else{
                    repository.addContractWithAttachment(params,attachment)
                }
            }
            else{
                params["contract_id"] = contractId
                if (attachment.isEmpty()){
                    repository.updateContract(params)
                }else{
                    repository.updateContractWithAttachment(params,attachment)
                }
            }
            resultLiveData.value = true
        }
    }


}