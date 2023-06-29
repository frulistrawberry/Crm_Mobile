package com.baihe.lib_customer

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.baihe.lib_common.http.response.ListData
import com.baihe.lib_common.viewmodel.BaseViewModel
import com.baihe.lib_common.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_customer.api.CustomerRepository
import com.dylanc.loadingstateview.ViewType

class CustomerViewModel:BaseViewModel() {
    private val customerRepository :CustomerRepository by lazy {
        CustomerRepository(this)
    }
    val loadingStateLiveData: MutableLiveData<ViewType> by lazy {
        MutableLiveData<ViewType>()
    }
    val loadingDialogLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val customerListLiveData:MutableLiveData<List<CustomerListItemEntity>> by lazy {
        MutableLiveData<List<CustomerListItemEntity>>()
    }
    val customerDetailLiveData:MutableLiveData<CustomerDetailEntity> by lazy {
        MutableLiveData<CustomerDetailEntity>()
    }

    val customerTempleLiveData:MutableLiveData<List<KeyValueEntity>> by lazy {
        MutableLiveData<List<KeyValueEntity>>()
    }

    val customerAddOrUpdateLiveData:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getCustomerList(page:Int,name:String = ""){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
                _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val customerListData = customerRepository.customerList(page,name)
            if (customerListData==null || customerListData.rows.isNullOrEmpty())
                loadingStateLiveData.value = ViewType.EMPTY
            else{
                customerListLiveData.value = customerListData.rows
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }

    fun getCustomerDetail(customerId:String){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
            _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val customerDetailData = customerRepository.customerDetail(customerId)
            if (customerDetailData==null){
                loadingStateLiveData.value = ViewType.EMPTY
            }else{
                customerDetailLiveData.value = customerDetailData
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }

    fun getCustomerTemple(customerId:String?){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
                _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val kvList = customerRepository.getCustomerTemple(customerId)
            if (kvList.isNullOrEmpty()){
                loadingStateLiveData.value = ViewType.EMPTY
            }else{
                customerTempleLiveData.value = kvList
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }

    fun addOrUpdateCustomer(params:LinkedHashMap<String,Any?>,customerId: String?=null){
        loadingDialogLiveData.value = true
        launchUI({
            _,_-> loadingDialogLiveData.value = false
        }) {
            if (customerId.isNullOrEmpty())
                customerRepository.addCustomer(params)
            else{
                params.put("customerId",customerId)
                customerRepository.updateCustomer(params)
            }
            customerAddOrUpdateLiveData.value = true
        }
    }
}