package com.baihe.lib_common.push;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import com.baihe.lib_common.provider.UserServiceProvider;
import com.baihe.lib_framework.helper.AppHelper;
import com.baihe.lib_framework.log.LogUtil;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;


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
        final String userId = UserServiceProvider.getUserId();
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        XGPushManager.bindAccount(context, userId, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                LogUtil.i("LihePush", "绑定TPush成功  uid=" + userId);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                LogUtil.i("LihePush", "绑定TPush失败  uid=" + userId);
            }
        });
    }

    /**
     * push账号退出
     */
    public static void logoutAccount() {


        XGPushManager.cancelAllNotifaction(AppHelper.getApplication());
        final String userId = UserServiceProvider.getUserId();
        if (TextUtils.isEmpty(userId)) {
            return;
        }


        XGPushManager.delAccount(AppHelper.getApplication(), userId, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                LogUtil.i("LihePush", "解绑TPush成功  uid=" + userId);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                LogUtil.i("LihePush", "解绑TPush失败  uid=" + userId);
            }
        });
    }













}
