package com.baihe.lib_login.login.viewmodel

import androidx.lifecycle.MutableLiveData
import com.baihe.http.EasyConfig
import com.baihe.http.EasyHttp
import com.baihe.lib_common.entity.UserEntity
import com.baihe.lib_common.provider.UserServiceProvider
import com.baihe.lib_common.viewmodel.BaseViewModel
import com.baihe.lib_framework.ext.saveAs
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_framework.toast.TipsToast
import com.baihe.lib_login.api.LoginRepository

class LoginViewModel:BaseViewModel() {
    private val repository by lazy {
        LoginRepository(this)
    }
    val loginLiveData by lazy {
        MutableLiveData<Boolean>()
    }
    val verifyCodeLiveData by lazy {
        MutableLiveData<Boolean>()
    }

    fun passWordLogin(userName:String,passWord:String){
        launchUI(errorBlock = {
                _, _ ->
            loginLiveData.value = false
        }){
            val userEntity = repository.passWordLogin(userName,passWord)
            saveUser(userEntity)
            loginLiveData.value = true
        }
    }

    fun verifyCodeLogin(userName: String,verifyCode:String){
        launchUI(errorBlock = {
            _,_ ->
            loginLiveData.postValue(false)
        }){
            val userEntity = repository.verifyCodeLogin(userName,verifyCode)
            saveUser(userEntity)
            loginLiveData.postValue( true)
        }
    }

    fun sendVerifyCode(mobile:String){
        launchUI(errorBlock = {
                _, _ ->
            verifyCodeLiveData.postValue(false)
        }){
            val result = repository.sendVerifyCode(mobile)
            verifyCodeLiveData.postValue( result == 1)
        }
    }

    private fun saveUser(user:UserEntity?){
        UserServiceProvider.saveUser(user)
    }

}