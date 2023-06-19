package com.baihe.lib_home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.baihe.lib_framework.base.BaseActivity;
import com.baihe.lib_framework.utils.StatusBarSettingHelper;
import com.baihe.lib_home.home.HomeFragment;

public class MainActivity extends BaseActivity {

    public static void start(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onReload() {
    }

    @Override
    public int getLayoutResId() {
        return R.layout.home_activity_main;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        StatusBarSettingHelper.setRootViewFitsSystemWindows(this,false);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
    }
}
