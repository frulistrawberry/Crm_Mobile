package com.baihe.lihepro.manager;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.baihe.common.base.BaseApplication;
import com.baihe.common.manager.Task;
import com.baihe.common.manager.TaskManager;
import com.baihe.common.util.JsonUtils;
import com.baihe.lihepro.entity.UserEntity;
import com.baihe.lihepro.utils.SPUtils;
import com.baihe.lihepro.constant.SPConstant;

/**
 * Author：xubo
 * Time：2020-07-22
 * Description：
 */
public class AccountManager {
    private static class AccountManagerHolder {
        private static AccountManager accountManager = new AccountManager();
    }

    private Context context;

    private AccountManager() {
        this.context = BaseApplication.getInstance();
    }

    public static AccountManager newInstance() {
        return AccountManagerHolder.accountManager;
    }

    public UserEntity getUser() {
        String userJson = SPUtils.getPhoneSP(context).getString(SPConstant.KEY_USER, "");
        if (TextUtils.isEmpty(userJson)) {
            return null;
        } else {
            return JsonUtils.parse(userJson, UserEntity.class);
        }
    }

    public String getUserId() {
        String userId = SPUtils.getPhoneSP(context).getString(SPConstant.KEY_USER_ID, "");
        return TextUtils.isEmpty(userId) ? null : userId;
    }

    public void saveUser(final UserEntity userEntity) {
        if (userEntity == null) {
            return;
        }
        TaskManager.newInstance().runOnBackground(new Task() {
            @Override
            public void onRun() {
                SPUtils.getPhoneSP(context).edit().putString(SPConstant.KEY_USER_ID, userEntity.getUser_id()).apply();
                SPUtils.getPhoneSP(context).edit().putString(SPConstant.KEY_USER, JSON.toJSONString(userEntity)).apply();
            }
        });
    }

    public void clearUser() {
        String userId = SPUtils.getPhoneSP(context).getString(SPConstant.KEY_USER_ID, "");
        if (!TextUtils.isEmpty(userId)) {
            //清除用户配置信息
//            SPUtils.getUserSP(context, userId).edit().remove().apply();
        }
        SPUtils.getPhoneSP(context).edit().remove(SPConstant.KEY_USER).apply();
        SPUtils.getPhoneSP(context).edit().remove(SPConstant.KEY_USER_ID).apply();
    }
}
