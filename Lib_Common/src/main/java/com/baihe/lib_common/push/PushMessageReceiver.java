package com.baihe.lib_common.push;

import android.content.Context;

import com.baihe.lib_framework.log.LogUtil;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;


/**
 * Author：xubo
 * Time：2020-10-19
 * Description：
 */
public class PushMessageReceiver extends XGPushBaseReceiver {


    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
        LogUtil.d("LihePush",xgPushRegisterResult.getToken());
    }

    @Override
    public void onUnregisterResult(Context context, int i) {
        LogUtil.d("LihePush",i+"");
    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {
        LogUtil.d("LihePush",s);
    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {
        LogUtil.d("LihePush",s);
    }

    @Override
    public void onSetAccountResult(Context context, int i, String s) {
        LogUtil.d("LihePush",s);
    }

    @Override
    public void onDeleteAccountResult(Context context, int i, String s) {
        LogUtil.d("LihePush",s);
    }

    @Override
    public void onSetAttributeResult(Context context, int i, String s) {
        LogUtil.d("LihePush",s);
    }

    @Override
    public void onQueryTagsResult(Context context, int i, String s, String s1) {
        LogUtil.d("LihePush",s);
    }

    @Override
    public void onDeleteAttributeResult(Context context, int i, String s) {
        LogUtil.d("LihePush",s);
    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        LogUtil.d("LihePush",xgPushTextMessage.getContent());
    }

    @Override
    public void onNotificationClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

    }

    @Override
    public void onNotificationShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

    }
}
