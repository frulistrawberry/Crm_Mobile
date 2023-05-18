package com.baihe.lihepro.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baihe.common.base.BaseActivity;
import com.baihe.common.log.LogUtils;
import com.baihe.common.manager.TaskManager;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.http.Http;
import com.baihe.http.HttpRequest;
import com.baihe.http.callback.CallBack;
import com.baihe.http.cookie.CookieStore;
import com.baihe.http.interceptor.ParamsInterceptor;
import com.baihe.http.interceptor.ResultInterceptor;
import com.baihe.http.processor.ResultPreProcessor;
import com.baihe.lihepro.BuildConfig;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.SPConstant;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.AuthDialog;
import com.baihe.lihepro.entity.UpgradeEntity;
import com.baihe.lihepro.manager.AccountManager;
import com.baihe.lihepro.push.PushHelper;
import com.baihe.lihepro.utils.SPUtils;
import com.baihe.lihepro.utils.Utils;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

/**
 * Author：xubo
 * Time：2020-07-21
 * Description：
 */
public class SplashActivity extends BaseActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        if (!SPUtils.getPhoneSP(this).getBoolean(SPConstant.KEY_PRIVACY_POLICY,false)){
            new AuthDialog(this, new AuthDialog.OnAuthListener() {
                @Override
                public void onAgree() {
                    SPUtils.getPhoneSP(SplashActivity.this).edit().putBoolean(SPConstant.KEY_PRIVACY_POLICY,true).apply();
                    initTPush();
                    initHttp();
                    initShare();
                    CrashReport.initCrashReport(getApplicationContext(), "8222258819", BuildConfig.DEBUG);
                    start();

                }

                @Override
                public void onRefuse() {
                    Utils.exitApp(SplashActivity.this);
                }
            }).show();
         }else {
            initTPush();
            initHttp();
            initShare();
            CrashReport.initCrashReport(getApplicationContext(), "8222258819", BuildConfig.DEBUG);
            start();
        }

    }

    private void initShare() {
        UMConfigure.init(getApplicationContext(),UMConfigure.DEVICE_TYPE_PHONE,"");
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);

        // 微信设置
        PlatformConfig.setWeixin("wx9f01f8a48f449b7e","db5c10f99c9dcdaa5b6bb041e3bdd369");
        PlatformConfig.setWXFileProvider("com.baihe.lihepro.fileprovider");
    }

    @Override
    public void showMsgDialog(String userId) {

    }

    private void initTPush() {

        message = XGPushManager.getCustomContentFromIntent(this,getIntent());
        XGPushConfig.enableDebug(getApplicationContext(), BuildConfig.DEBUG);
        // 注意这里填入的是 Oppo 的 AppKey，不是AppId
        XGPushConfig.setOppoPushAppId(getApplicationContext(), "ea9afa6fc4a0466cbc96a8a1ed2640d6");
        // 注意这里填入的是 Oppo 的 AppSecret，不是 AppKey
        XGPushConfig.setOppoPushAppKey(getApplicationContext(), "b3c4ff4ac4734b419620764ed584fd18");

        XGPushConfig.setMiPushAppId(getApplicationContext(), "2882303761518222692");
        XGPushConfig.setMiPushAppKey(getApplicationContext(), "5641822277692");

        XGPushConfig.enableOtherPush(getApplicationContext(), true);
        XGPushManager.registerPush(getApplicationContext(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                LogUtils.d("LihePush", "注册成功，设备token为：" + data);
                //更新http拦截器里的device_token
                Http.getInstance().getParamsInterceptor().setParam("device_token", XGPushConfig.getToken(getApplicationContext()));
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                LogUtils.d("LihePush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
    }

    private void initHttp() {
        //请求失败默认不重复请求
        Http.getInstance().setRepeatRequestCount(0);
        //开启cookie
        Http.getInstance().cookie(new CookieStore(app, true));
        //公参拦截器
        ParamsInterceptor paramsInterceptor = new ParamsInterceptor.Builder()
                .addParam("userId", AccountManager.newInstance().getUserId())
                .addParam("apver", BuildConfig.VERSION_NAME)
                .addParam("appId", "20")
                .addParam("platform", "1020")
                .addParam("device_type", "1")
                .addParam("device_token", XGPushConfig.getToken(getApplicationContext()))
                .addParam("device", Build.BRAND + Build.MODEL)
                .build();
        Http.getInstance().setParamsInterceptor(paramsInterceptor);
        //结果拦截器，登录弹出
        Http.getInstance().setResultInterceptor(new ResultInterceptor() {
            @Override
            public boolean isIntercept(String response) {
                JSONObject responseJSONObject = JSON.parseObject(response);
                int code = responseJSONObject.getInteger("code");
                //登录弹出
                if (code == 1002 || code == 1005) {
                    return true;
                }
                return false;
            }

            @Override
            public void interceptAction(String response) {
                JSONObject responseJSONObject = JSON.parseObject(response);
                int code = responseJSONObject.getInteger("code");
                String msg = responseJSONObject.getString("msg");
                if (code == 1002 || code == 1005) {
                    //清除账号和cookie
                    PushHelper.logoutAccount();
                    AccountManager.newInstance().clearUser();
                    Http.getInstance().getCookieStore().clearAllCookies();

                    Utils.startTaskActivity(app, LoginActivity.class);
                    ToastUtils.toast(msg);
                }
            }
        });
        //结果预处理器
        Http.getInstance().setResultPreProcessor(new ResultPreProcessor() {
            @Override
            public String process(String response, boolean isToastError) {
                //处理数据
                JSONObject responseJSONObject = JSON.parseObject(response);
                int code = responseJSONObject.getInteger("code");
                //请求成功
                if (code == 200) {
                    String data = responseJSONObject.getJSONObject("data").getString("result");
                    return data;
                }
                if (isToastError) {
                    runOnUiThread(() -> {
                        String msg = responseJSONObject.getString("msg");
                        ToastUtils.toast(msg);
                    });

                }
                return null;
            }

            @Override
            public boolean interrupt(String processData) {
                if (processData == null) {
                    return true;
                }
                return false;
            }
        });
        Http.getInstance().setLogInterceptor();
    }

    private void start() {
        if (AccountManager.newInstance().getUser() != null) {
            //push绑定账号
            PushHelper.bindAccount(this);
            TaskManager.newInstance().runOnUi(new Runnable() {
                @Override
                public void run() {
                    getVersion();
                }
            }, 1500);
        } else {
            TaskManager.newInstance().runOnUi(new Runnable() {
                @Override
                public void run() {
                    if (SPUtils.getPhoneSP(context).getBoolean(SPConstant.KEY_GUIDE, true)) {
                        GuideActivity.start(context);
                    } else {
                        LoginActivity.start(context);
                    }
                    finish();
                }
            }, 2500);
        }
    }

    public void getVersion() {
        HttpRequest.create(UrlConstant.UPGRADE_URL).putParam("channel", "android").get(new CallBack<UpgradeEntity>() {
            @Override
            public UpgradeEntity doInBackground(String response) {
                return JsonUtils.parse(response, UpgradeEntity.class);
            }

            @Override
            public void success(UpgradeEntity entity) {

            }

            @Override
            public void error() {

            }

            @Override
            public void fail() {

            }

            @Override
            public void after(UpgradeEntity upgradeEntity) {
                super.after(upgradeEntity);
                MainActivity.start(context, upgradeEntity,message);
                finish();
            }
        });
    }
}
