package com.baihe.lihepro.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.baihe.common.base.BaseActivity;
import com.baihe.lihepro.R;

public class LoginActivity extends BaseActivity {
    public static final String IS_TO_HOME = "IS_TO_HOME";
    private boolean isToHome = true;
    public static LoginActivity instance;

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    public static void start(Context context, boolean isToHome) {
        Intent starter = new Intent(context, LoginActivity.class);
        starter.putExtra(IS_TO_HOME, isToHome);
        if(context instanceof Application){
            starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_login);
        if (getIntent() != null) {
            isToHome = getIntent().getBooleanExtra(IS_TO_HOME, true);
        }
    }

}
