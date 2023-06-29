package com.baihe.lib_user

import androidx.lifecycle.MutableLiveData
import com.baihe.lib_common.viewmodel.BaseViewModel
import com.baihe.lib_user.api.UserRepository

class UserViewModel:BaseViewModel() {
    val loadingDialogLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val bossSeaLiveData:MutableLiveData<List<BossSeaEntity>> by lazy {
        MutableLiveData<List<BossSeaEntity>>()
    }
    private val userRepository :UserRepository by lazy {
        UserRepository(this)
    }

    fun getBossSea(){
        loadingDialogLiveData.value = true
        launchUI({_,_->
            loadingDialogLiveData.value = false
            bossSeaLiveData.value = null
        }){
            val bossSea = userRepository.getBossSea()
            bossSeaLiveData.value = bossSea
            loadingDialogLiveData.value = false
        }
    }
}