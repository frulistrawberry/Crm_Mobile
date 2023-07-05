package com.baihe.lib_user

import androidx.lifecycle.MutableLiveData
import com.baihe.lib_common.service.ILoginService
import com.baihe.lib_common.ui.widget.keyvalue.entity.KeyValueEntity
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
        launchUI(errorBlock = { _, _ ->
            resetPasLiveData.value = false
        }) {
            val result = userRepository.submitPassword(oldPas, newPas, confirmPas)
            resetPasLiveData.postValue(result == 1)
        }
    }

    /**
     * 检查更新
     */
    fun checkVersion() {

    }

    /**
     * 设置是否开启推送
     */
    fun setPushTurnOnOrOff(checked: Boolean) {

    }

    /**
     * 删除账号
     */
    fun deleteAccount() {

    }
}