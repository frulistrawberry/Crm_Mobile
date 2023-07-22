package com.baihe.lib_order.ui

import androidx.lifecycle.MutableLiveData
import com.baihe.lib_common.entity.CustomerDetailEntity
import com.baihe.lib_common.entity.OpportunityListItemEntity
import com.baihe.lib_common.entity.ResultEntity
import com.baihe.lib_common.provider.CustomerServiceProvider
import com.baihe.lib_common.provider.OpportunityServiceProvider
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
import com.baihe.lib_common.viewmodel.BaseViewModel
import com.baihe.lib_order.OrderDetailEntity
import com.baihe.lib_order.OrderListItemEntity
import com.baihe.lib_order.OrderTempleEntity
import com.baihe.lib_order.api.OrderRepository
import com.dylanc.loadingstateview.ViewType

class OrderViewModel: BaseViewModel() {
    private val repository by lazy {
        OrderRepository(this)
    }
    val loadingDialogLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val loadingStateLiveData: MutableLiveData<ViewType> by lazy {
        MutableLiveData<ViewType>()
    }
    val listLiveData:MutableLiveData<List<OrderListItemEntity>> by lazy {
        MutableLiveData<List<OrderListItemEntity>>()
    }
    val detailLiveData:MutableLiveData<OrderDetailEntity> by lazy {
        MutableLiveData<OrderDetailEntity>()
    }
    val oppoTempleLiveData:MutableLiveData<List<KeyValueEntity>> by lazy {
        MutableLiveData<List<KeyValueEntity>>()
    }

    val oppoListLiveData:MutableLiveData<List<OpportunityListItemEntity>> by lazy {
        MutableLiveData<List<OpportunityListItemEntity>>()
    }



    val createOrderLiveData:MutableLiveData<ResultEntity> by lazy {
        MutableLiveData<ResultEntity>()
    }

    val customerLiveData:MutableLiveData<CustomerDetailEntity> by lazy {
        MutableLiveData<CustomerDetailEntity>()
    }

    val stateLiveData:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val peopleLiveData:MutableLiveData<List<KeyValueEntity>> by lazy {
        MutableLiveData<List<KeyValueEntity>>()
    }



    fun getOrderList(page:Int,type:String="0",keywords:String?=null,filter:LinkedHashMap<String,Any?>?=null){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
                _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val listData = repository.orderList(page,type,keywords,filter)
            if (listData==null || listData.rows.isNullOrEmpty())
                loadingStateLiveData.value = ViewType.EMPTY
            else{
                listLiveData.value = listData.rows
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }

    fun createOrderWithOppo(reqId:String,customerId: String){
        loadingDialogLiveData.value = true
        launchUI({
                _,_->
            loadingDialogLiveData.value = false
        }){
            val  data = repository.addOrderWithOppo(reqId,customerId)
            createOrderLiveData.value = data
            loadingDialogLiveData.value = false
        }
    }

    fun createOrderWithoutOppo(params:LinkedHashMap<String,Any?>){
        loadingDialogLiveData.value = true
        launchUI({
                _,_->
            loadingDialogLiveData.value = false
        }){
            val data = repository.addOrderWithoutOppo(params)
            createOrderLiveData.value = data
            loadingDialogLiveData.value = false
        }
    }

    fun getOrderDetail(orderId:String){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
                _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val data = repository.orderDetail(orderId)
            if (data==null)
                loadingStateLiveData.value = ViewType.EMPTY
            else{
                detailLiveData.value = data
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }

    fun getOrderPeople(orderId:String){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
                _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val data = repository.orderPeopleInfo(orderId)
            if (data==null)
                loadingStateLiveData.value = ViewType.EMPTY
            else{
                peopleLiveData.value = data
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }

    fun addOrderFollow(params:LinkedHashMap<String,Any?>){
        loadingDialogLiveData.value = true
        launchUI({
                _,_-> loadingDialogLiveData.value = false
        }) {

            repository.followLog(params)

            stateLiveData.value = true
            loadingDialogLiveData.value = false
        }
    }

    fun addPeople(postData:List<KeyValueEntity>){
        loadingDialogLiveData.value = true
        launchUI({
                _,_-> loadingDialogLiveData.value = false
        }) {

            repository.addPeople(postData)

            stateLiveData.value = true
            loadingDialogLiveData.value = false
        }
    }

    fun confirmIndoor(params:LinkedHashMap<String,Any?>){
        loadingDialogLiveData.value = true
        launchUI({
                _,_-> loadingDialogLiveData.value = false
        }) {

            repository.comeIndoor(params)

            stateLiveData.value = true
            loadingDialogLiveData.value = false
        }
    }

    fun signOrder(params:LinkedHashMap<String,Any?>){
        loadingDialogLiveData.value = true
        launchUI({
                _,_-> loadingDialogLiveData.value = false
        }) {

            repository.sign(params)

            stateLiveData.value = true
            loadingDialogLiveData.value = false
        }
    }


    fun transferOrder(params:LinkedHashMap<String,Any?>){
        loadingDialogLiveData.value = true
        launchUI({
                _,_-> loadingDialogLiveData.value = false
        }) {

            repository.transferOrder(params)

            stateLiveData.value = true
            loadingDialogLiveData.value = false
        }
    }

    fun chargebackOrder(params:LinkedHashMap<String,Any?>,attachment:List<String>? = null){
        loadingDialogLiveData.value = true
        launchUI({
                _,_-> loadingDialogLiveData.value = false
        }) {

            if (attachment.isNullOrEmpty())
                repository.chargeBackOrder(params)
            else
                repository.chargeBackOrderWithAttachment(params,attachment)

            stateLiveData.value = true
            loadingDialogLiveData.value = false
        }
    }


    fun getOppoTemple(){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
                _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val data = OpportunityServiceProvider.getOppoTemple(this)
            if (data==null)
                loadingStateLiveData.value = ViewType.EMPTY
            else{
                oppoTempleLiveData.value = data.row
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }

    fun getOppoList(page:Int,keywords: String =""){
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({
                _,_-> loadingStateLiveData.value = ViewType.ERROR
        }){
            val listData = OpportunityServiceProvider.getOppoList(this,page,keywords)
            if (listData==null || listData.rows.isNullOrEmpty())
                loadingStateLiveData.value = ViewType.EMPTY
            else{
                oppoListLiveData.value = listData.rows
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }





    fun getCustomerInfo(customerId: String){
        loadingDialogLiveData.value = true
        launchUI({
                _,_-> loadingDialogLiveData.value = false
        }){
            val data = CustomerServiceProvider.getCustomerInfo(customerId,this)
            customerLiveData.value = data
            loadingDialogLiveData.value = false
        }
    }



}