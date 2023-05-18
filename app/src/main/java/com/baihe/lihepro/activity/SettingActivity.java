package com.baihe.lihepro.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.http.Http;
import com.baihe.http.HttpRequest;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.BuildConfig;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.AlertDialog;
import com.baihe.lihepro.dialog.UpgradeDialog;
import com.baihe.lihepro.entity.UpgradeEntity;
import com.baihe.lihepro.manager.AccountManager;
import com.baihe.lihepro.push.PushHelper;
import com.baihe.lihepro.utils.Utils;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_update;
    private TextView tv_privacy_policy;
    private TextView tv_cancel;

    public static void start(Context context){
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("设置");
        setContentView(R.layout.activity_setting);
        initView();
        setListeners();

    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        TextView tv_version = findViewById(R.id.tv_version);
        rl_update = findViewById(R.id.rl_update);
        tv_privacy_policy = findViewById(R.id.tv_privacy_policy);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_version.setText("V"+BuildConfig.VERSION_NAME);
    }

    private void setListeners() {
        rl_update.setOnClickListener(this);
        tv_privacy_policy.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_update:
                getVersion();
                break;
            case R.id.tv_privacy_policy:
                WebActivity.start(this,"隐私政策",UrlConstant.PRIVACY_POLICY);
                break;
            case R.id.tv_cancel:
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setContent("您确定要注销账号吗？")
                        .setConfirmListener("确定", new AlertDialog.OnConfirmClickListener() {
                            @Override
                            public void onConfirm(Dialog dialog) {
                                cancel();
                            }
                        })
                        .setCancelListener("取消", new AlertDialog.OnCancelClickListener() {
                            @Override
                            public void onCancel(Dialog dialog) {
                                dialog.dismiss();
                            }
                        }).build().show();
                break;
        }
    }

    private int getVersion(String versionName) {
        if (versionName != null) {
            if (versionName.contains(".")) {
                versionName = versionName.replace(".", "");
            }
            try {
                int version = Integer.parseInt(versionName);
                return version;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public void getVersion() {
        HttpRequest.create(UrlConstant.UPGRADE_URL).putParam("channel", "android").get(new CallBack<UpgradeEntity>() {
            @Override
            public UpgradeEntity doInBackground(String response) {
                return JsonUtils.parse(response, UpgradeEntity.class);
            }

            @Override
            public void success(UpgradeEntity upgradeEntity) {
                if (upgradeEntity!=null){
                    if (upgradeEntity != null && getVersion(upgradeEntity.getVersion()) > getVersion(BuildConfig.VERSION_NAME)) {
                        UpgradeDialog.Builder builder = new UpgradeDialog.Builder(context)
                                .setCancelable(false)
                                .setContent(upgradeEntity.getMsgContent())
                                .setVersion(upgradeEntity.getVersion())
                                .setOnConfirmClickListener(new UpgradeDialog.OnConfirmClickListener() {
                                    @Override
                                    public void onConfirm(Dialog dialog) {
                                        Uri uri = Uri.parse(upgradeEntity.getUrl());
                                        Intent intent3 = new Intent(Intent.ACTION_VIEW, uri);
                                        intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent3);
                                    }
                                });
                        if (upgradeEntity.getForced() == 0) { //软更新
                            builder.setOnCancelClickListener(new UpgradeDialog.OnCancelClickListener() {
                                @Override
                                public void onCancel(Dialog dialog) {
                                    dialog.dismiss();
                                }
                            });
                        }
                        builder.build().show();
                    }else {
                        ToastUtils.toast("已是最新版本");
                    }

                }
            }

            @Override
            public void error() {
                ToastUtils.toast("网络错误");
            }

            @Override
            public void fail() {
                ToastUtils.toast("网络错误");
            }

            @Override
            public void after(UpgradeEntity upgradeEntity) {
                super.after(upgradeEntity);
            }
        });
    }

    public void cancel(){
        HttpRequest.create(UrlConstant.CANCEL_URL).get(new CallBack<String>() {
            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String upgradeEntity) {
                PushHelper.logoutAccount();
                AccountManager.newInstance().clearUser();
                Http.getInstance().getCookieStore().clearAllCookies();
                Utils.startTaskActivity(app, LoginActivity.class);
            }

            @Override
            public void error() {
                ToastUtils.toast("网络错误");
            }

            @Override
            public void fail() {
                ToastUtils.toast("网络错误");
            }

            @Override
            public void after(String upgradeEntity) {
                super.after(upgradeEntity);
            }
        });
    }
}
