package com.baihe.lihepro.push;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.app.NotificationManagerCompat;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.base.BaseApplication;
import com.baihe.common.entity.MsgBean;
import com.baihe.common.log.LogUtils;
import com.baihe.common.manager.BackgroundManager;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.SPUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.CrmApp;
import com.baihe.lihepro.activity.CustomerDetailActivity;
import com.baihe.lihepro.activity.MessageActivity;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.ReceivedCountEntity;
import com.baihe.lihepro.manager.AccountManager;
import com.google.gson.Gson;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import java.util.List;

/**
 * 信鸽初始化
 */
public class PushHelper {






    /**
     * push账号绑定
     *
     * @param context
     */
    public static void bindAccount(Context context) {
        final String userId = AccountManager.newInstance().getUserId();
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        XGPushManager.bindAccount(context, userId, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                LogUtils.i("LihePush", "绑定TPush成功  uid=" + userId);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                LogUtils.i("LihePush", "绑定TPush失败  uid=" + userId);
            }
        });
    }

    /**
     * push账号退出
     */
    public static void logoutAccount() {


        XGPushManager.cancelAllNotifaction(CrmApp.getInstance());
        final String userId = AccountManager.newInstance().getUserId();
        if (TextUtils.isEmpty(userId)) {
            return;
        }


        XGPushManager.delAccount(CrmApp.getInstance(), userId, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                LogUtils.i("LihePush", "解绑TPush成功  uid=" + userId);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                LogUtils.i("LihePush", "解绑TPush失败  uid=" + userId);
            }
        });
    }

    public static void getServiceUnreadCount(String userId,BaseActivity activity){
        HttpRequest.create(UrlConstant.RECEIVED_COUNT)
                .putParam(JsonParam.newInstance("params")
                        .putParamValue("id", userId))
                .get(new CallBack<ReceivedCountEntity>() {

                    @Override
                    public void before() {
                        super.before();
                    }

                    @Override
                    public ReceivedCountEntity doInBackground(String response) {
                        return JsonUtils.parse(response, ReceivedCountEntity.class);
                    }

                    @Override
                    public void success(final ReceivedCountEntity receivedCountEntity) {
                        if (receivedCountEntity.count == 0)
                            return;
                        List<MsgBean> msgList = receivedCountEntity.row;
                        SharedPreferences userSp = SPUtils.getUserSP(BaseApplication.getInstance(),userId);
                        userSp.edit().putInt("unreadMsg",receivedCountEntity.count).apply();
                        userSp.edit().putString("msgList",new Gson().toJson(msgList)).apply();
                        activity.showMsgDialog(AccountManager.newInstance().getUserId());

                    }

                    @Override
                    public void error() {
                    }

                    @Override
                    public void fail() {
                    }

                    @Override
                    public void after(ReceivedCountEntity userEntity) {
                        super.after(userEntity);
                    }
                });
    }

    public static void changeServiceUnreadCount(String messageIds){
        HttpRequest.create(UrlConstant.CHANGE_RECEIVED_COUNT)
                .putParam(JsonParam.newInstance("params")
                        .putParamValue("id", messageIds))
                .get(new CallBack<String>() {

                    @Override
                    public void before() {
                        super.before();
                    }

                    @Override
                    public String doInBackground(String response) {
                        return response;
                    }

                    @Override
                    public void success(final String userEntity) {
                    }

                    @Override
                    public void error() {
                    }

                    @Override
                    public void fail() {
                    }

                    @Override
                    public void after(String userEntity) {
                        super.after(userEntity);
                    }
                });
    }








    public static boolean dealPushMessage(Activity activity, MsgBean msg, boolean isToList) {

        if (isToList){
            MessageActivity.start(activity);
        }else if (msg!=null && msg.lead_status!=null && msg.customer_id!=null && msg.message_id!=null){
            int entryType = "1".equals(msg.lead_status)?CustomerDetailActivity.ENTRY_TYPE_CUSTOMER_SERVICE:CustomerDetailActivity.ENTRY_TYPE_REQUIREMENT;
            CustomerDetailActivity.start(activity,msg.customer_id,"",msg.lead_status,entryType);
            unreadStatus(msg.message_id);
        }
        return false;
    }

    private static void unreadStatus(String customerId){
        HttpRequest.create(UrlConstant.UNREAD_STATUS).putParam(JsonParam.newInstance("params").putParamValue("id",customerId).putParamValue("type",2)
        ).get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String o) {

            }

            @Override
            public void error() {

            }

            @Override
            public void fail() {

            }
        });
    }


}
