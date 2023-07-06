package com.baihe.lib_message

import androidx.lifecycle.MutableLiveData
import com.baihe.lib_common.entity.MessageEntity
import com.baihe.lib_common.entity.MessageInfoEntity
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
     var page = 1;

    val messagesInfoEntity: MutableLiveData<MessageInfoEntity> by lazy {
        MutableLiveData<MessageInfoEntity>()
    }

    private val messageRepository: MessageRepository by lazy {
        MessageRepository(this)
    }

    val loadingStateLiveData: MutableLiveData<ViewType> by lazy {
        MutableLiveData<ViewType>()
    }

    val setReadResultData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    /**
     * 获取消息列表
     * @param page
     * @param type 0 初始化  1 直接刷新数据 2 加载更多
     */
    fun getMessages(type: Int) {
        when (type) {
            0 -> {
                page = 1
                loadingStateLiveData.value = ViewType.LOADING
                launchUI({ _, _ ->
                    loadingStateLiveData.value = ViewType.ERROR
                }) {
                    val messageListData = messageRepository.getMessages(page)
                    if (messageListData == null) {
                        loadingStateLiveData.value = ViewType.EMPTY
                    } else {
                        messagesInfoEntity.value = messageListData
                        loadingStateLiveData.value = ViewType.CONTENT
                    }
                }
            }
            1 -> {
                page=1
                launchUI({ _, _ -> }) {
                    val messageListData = messageRepository.getMessages(page)
                    if (messageListData != null) {
                        messagesInfoEntity.value = messageListData
                        loadingStateLiveData.value = ViewType.CONTENT
                    }
                }
            }
            else -> {
                page++
                launchUI({ _, _ -> }) {
                    val messageListData = messageRepository.getMessages(page)
                    if (messageListData != null) {
                        messagesInfoEntity.value = messageListData
                        loadingStateLiveData.value = ViewType.CONTENT
                    }
                }
            }
        }
    }


    /**
     * 设置已读
     * @param type 0 全部 / 1 特定消息
     * @param noticeId 消息Id
     */
    fun setMessageRead(type: Int, noticeId: String?) {
        launchUI({ _, _ -> }) {
            val result = messageRepository.setMessageRead(type, noticeId)
            if (result != null) {
                setReadResultData.value = setReadResultData.value as Int + 1
            }
        }
    }

}