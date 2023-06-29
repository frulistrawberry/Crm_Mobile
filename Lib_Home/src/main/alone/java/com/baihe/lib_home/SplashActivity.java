package com.baihe.lib_home;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import com.baihe.lib_home.R;
import com.baihe.lib_common.provider.HomeServiceProvider;
import com.baihe.lib_common.provider.LoginServiceProvider;
import com.baihe.lib_common.provider.UserServiceProvider;
import com.baihe.lib_framework.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.home_activity_main;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        if (TextUtils.isEmpty(UserServiceProvider.getUserId()))
            LoginServiceProvider.INSTANCE.login(this);
        else
            HomeServiceProvider.toHome(this);
        finish();
    }
}
