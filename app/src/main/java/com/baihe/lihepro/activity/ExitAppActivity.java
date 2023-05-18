package com.baihe.lihepro.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.baihe.common.manager.TaskManager;

/**
 * Author：xubo
 * Time：2020-07-24
 * Description：退出应用Activity
 */

public class ExitAppActivity extends Activity {

    public static void start(Context context) {
        Intent intent = new Intent(context, ExitAppActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
        TaskManager.newInstance().runOnUi(new Runnable() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 100);
    }
}
