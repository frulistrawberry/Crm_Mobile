package com.baihe.lib_user

import androidx.lifecycle.MutableLiveData
import com.baihe.lib_common.entity.ResultEntity
import com.baihe.lib_common.viewmodel.BaseViewModel
import com.baihe.lib_user.api.UserRepository

class UserViewModel : BaseViewModel() {
    val loadingDialogLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val resetPasLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val bossSeaLiveData: MutableLiveData<List<BossSeaEntity>> by lazy {
        MutableLiveData<List<BossSeaEntity>>()
    }
    private val userRepository: UserRepository by lazy {
        UserRepository(this)
    }

    val versionLiveData: MutableLiveData<VersionEntity> by lazy {
        MutableLiveData<VersionEntity>()
    }
    val contractConfigLiveData:MutableLiveData<ResultEntity> by lazy {
        MutableLiveData<ResultEntity>()
    }


    val pushSwitchLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val deleteAccountLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getContractConfig(){
        launchUI({
                _,_->
        }){
            val result = userRepository.getCompanyConfig()
            contractConfigLiveData.value = result
        }

    }

    fun getBossSea() {
        loadingDialogLiveData.value = true
        launchUI({ _, _ ->
            loadingDialogLiveData.value = false
            bossSeaLiveData.value = null
        }) {
            val bossSea = userRepository.getBossSea()
            bossSeaLiveData.value = bossSea
            loadingDialogLiveData.value = false
        }
    }

    /**
     * 修改密码
     */
    fun resetPassword(userId: String?, oldPas: String, newPas: String, confirmPas: String) {
        loadingDialogLiveData.value = true
        launchUI(errorBlock = { _, _ ->
            loadingDialogLiveData.value = false
            resetPasLiveData.value = false
        }) {
            val result = userRepository.submitPassword(userId, oldPas, newPas, confirmPas)
            resetPasLiveData.postValue(result == 1)
            loadingDialogLiveData.value = false
        }
    }

    /**
     * 检查更新
     */
    fun checkVersion(currentVersion: String) {
        loadingDialogLiveData.value = true
        launchUI(errorBlock = { _, _ ->
            loadingDialogLiveData.value = false
        }) {
            val version = userRepository.checkAppVersion(currentVersion)
            versionLiveData.value = version
            loadingDialogLiveData.value = false
        }
    }

    /**
     * 设置是否开启推送
     */
    fun setPushTurnOnOrOff(checked: Boolean) {
        val pushSwitch = if (checked) {
            1
        } else {
            0
        }
        loadingDialogLiveData.value = true
        launchUI(errorBlock = { _, _ ->
            loadingDialogLiveData.value = false
            pushSwitchLiveData.value = -1
        }) {
            val result = userRepository.setPushStatus(pushSwitch)
            pushSwitchLiveData.value = result
            loadingDialogLiveData.value = false
        }
    }

    /**
     * 删除账号
     */
    fun deleteAccount() {
        loadingDialogLiveData.value = true
        launchUI(errorBlock = { _, _ ->
            loadingDialogLiveData.value = false
            deleteAccountLiveData.value = false
        }) {
            val result = userRepository.deleteAccount()
            if (!result.isNullOrEmpty() && result[0].toInt() == 2002) {
                deleteAccountLiveData.postValue(true)
            }
            loadingDialogLiveData.value = false
        }
    }
}