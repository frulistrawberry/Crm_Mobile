package com.baihe.lib_message

import androidx.lifecycle.MutableLiveData
import com.baihe.lib_common.entity.MessageEntity
import com.baihe.lib_common.viewmodel.BaseViewModel
import com.baihe.lib_login.api.MessageRepository
import com.dylanc.loadingstateview.ViewType

/**
 * @author xukankan
 * @date 2023/7/5 10:56
 * @email：xukankan@jiayuan.com
 * @description：
 */
class MessageFragmentViewModel : BaseViewModel() {
    val messagesEntity: MutableLiveData<List<MessageEntity>> by lazy {
        MutableLiveData<List<MessageEntity>>()
    }

    private val messageRepository: MessageRepository by lazy {
        MessageRepository(this)
    }

    val loadingStateLiveData: MutableLiveData<ViewType> by lazy {
        MutableLiveData<ViewType>()
    }

    /**
     * 获取消息列表
     */
    fun getMessages() {
        loadingStateLiveData.value = ViewType.LOADING
        launchUI({ _, _ ->
            loadingStateLiveData.value = ViewType.ERROR
        }) {
            val messageListData = messageRepository.getMessages()
            if (messageListData == null) {
                loadingStateLiveData.value = ViewType.EMPTY
            } else {
                messagesEntity.value = messageListData.rows
                loadingStateLiveData.value = ViewType.CONTENT
            }
        }
    }

}