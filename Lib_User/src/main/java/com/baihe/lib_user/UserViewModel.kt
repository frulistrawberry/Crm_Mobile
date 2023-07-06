package com.baihe.lib_user

import androidx.lifecycle.MutableLiveData
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

    val pushSwitchLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val deleteAccountLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
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
    fun resetPassword(oldPas: String, newPas: String, confirmPas: String) {
        loadingDialogLiveData.value = true
        launchUI(errorBlock = { _, _ ->
            loadingDialogLiveData.value = false
            resetPasLiveData.value = false
        }) {
            val result = userRepository.submitPassword(oldPas, newPas, confirmPas)
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
            pushSwitchLiveData.value = false
        }) {
            val result = userRepository.setPushStatus(pushSwitch)
            pushSwitchLiveData.postValue(result == 1)
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
            deleteAccountLiveData.postValue(result == 1)
            loadingDialogLiveData.value = false
        }
    }
}