package com.baihe.lihepro.push;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.entity.MsgBean;
import com.baihe.common.log.LogUtils;
import com.baihe.common.manager.BackgroundManager;
import com.baihe.common.util.JsonUtils;
import com.baihe.lihepro.dialog.MsgDialog;
import com.baihe.lihepro.manager.AccountManager;
import com.baihe.lihepro.utils.SPUtils;
import com.google.gson.Gson;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2020-10-19
 * Description：
 */
public class PushMessageReceiver extends XGPushBaseReceiver {


    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
        LogUtils.d("LihePush",xgPushRegisterResult.getToken());
    }

    @Override
    public void onUnregisterResult(Context context, int i) {
        LogUtils.d("LihePush",i+"");
    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {
        LogUtils.d("LihePush",s);
    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {
        LogUtils.d("LihePush",s);
    }

    @Override
    public void onSetAccountResult(Context context, int i, String s) {
        LogUtils.d("LihePush",s);
    }

    @Override
    public void onDeleteAccountResult(Context context, int i, String s) {
        LogUtils.d("LihePush",s);
    }

    @Override
    public void onSetAttributeResult(Context context, int i, String s) {
        LogUtils.d("LihePush",s);
    }

    @Override
    public void onQueryTagsResult(Context context, int i, String s, String s1) {
        LogUtils.d("LihePush",s);
    }

    @Override
    public void onDeleteAttributeResult(Context context, int i, String s) {
        LogUtils.d("LihePush",s);
    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        LogUtils.d("LihePush",xgPushTextMessage.getContent());
    }

    @Override
    public void onNotificationClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        SharedPreferences userSp = SPUtils.getUserSP(context, AccountManager.newInstance().getUserId());
        userSp.edit().putBoolean("fromPush",true).apply();
        userSp.getInt("unreadMsg", 0);
        int unreadNum;
        String msgListJson = userSp.getString("msgList",null);
        List<MsgBean> msgList;
        if (TextUtils.isEmpty(msgListJson)){
            msgList = new ArrayList<>();
        }else {
            msgList = JsonUtils.parseList(msgListJson,MsgBean.class);
        }
        if (msgList.size()>0){
            int clickIndex = msgList.size()-1;
            for (MsgBean msg : msgList) {
                String customContent = xgPushClickedResult.getCustomContent();
                if (!TextUtils.isEmpty(customContent)) {
                    MsgBean msgEntity = JsonUtils.parse(customContent,MsgBean.class);
                    if (msgEntity != null) {
                        if (msg.message_id.equals(msgEntity.message_id)){
                            PushHelper.changeServiceUnreadCount(msgEntity.message_id);
                            clickIndex = msgList.indexOf(msg);
                        }
                    }
                }


            }
            msgList.remove(clickIndex);
        }
        userSp.edit().putString("msgList",new Gson().toJson(msgList)).apply();
        unreadNum = msgList.size();
        userSp.edit().putInt("unreadMsg",unreadNum).apply();

    }

    @Override
    public void onNotificationShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        SharedPreferences userSp = SPUtils.getUserSP(context, AccountManager.newInstance().getUserId());
        userSp.getInt("unreadMsg", 0);
        int unreadNum;
        String msgListJson = userSp.getString("msgList",null);
        List<MsgBean> msgList;
        if (TextUtils.isEmpty(msgListJson)){
            msgList = new ArrayList<>();
        }else {
            msgList = JsonUtils.parseList(msgListJson,MsgBean.class);
        }
        msgList.add(JsonUtils.parse(xgPushShowedResult.getCustomContent(),MsgBean.class));
        unreadNum = msgList.size();
        userSp.edit().putInt("unreadMsg",unreadNum).apply();
        userSp.edit().putString("msgList",new Gson().toJson(msgList)).apply();
        Activity curr = BackgroundManager.getCurrentActivity();
        if (curr==null)
            return;
        if ( !BackgroundManager.newInstance().isBackground() && curr instanceof BaseActivity){
            ((BaseActivity) curr).showMsgDialog(AccountManager.newInstance().getUserId());
        }

    }
}
