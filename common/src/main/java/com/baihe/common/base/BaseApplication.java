package com.baihe.common.base;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.multidex.MultiDexApplication;

import com.baihe.common.BuildConfig;
import com.baihe.common.R;
import com.baihe.common.log.LogUtils;
import com.baihe.common.util.CommonUtils;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.common.view.StatusLayout;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Author：xubo
 * Time：2020-03-01
 * Description：
 */
public class BaseApplication extends MultiDexApplication {
    static {
        StatusLayout.setDefaultLoadingLayoutId(R.layout.layout_net_loading, new StatusChildLayout.LoadingAction() {
            @Override
            public void onAttached(Context context, View loadingLayout) {
                ImageView loading_iv = loadingLayout.findViewById(R.id.loading_iv);
                AnimationDrawable drawable = (AnimationDrawable) loading_iv.getDrawable();
                drawable.start();
            }

            @Override
            public void onDetached(View loadingLayout) {
                ImageView loading_iv = loadingLayout.findViewById(R.id.loading_iv);
                AnimationDrawable drawable = (AnimationDrawable) loading_iv.getDrawable();
                drawable.stop();
            }
        });
    }

    public static BaseApplication app;

    public static BaseApplication getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initLog();
        MyToast.builder.init(this);
    }

    private void initLog() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder().showThreadInfo(true).methodCount(3).tag(LogUtils.LOG_TAG).build();
        AndroidLogAdapter logAdapter = new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.DEBUG;
            }
        };
        Logger.addLogAdapter(logAdapter);
    }

    public enum MyToast {
        builder;
        private Toast toast;
        private LayoutInflater inflater;
        private TextView toast_text_tv;
        private int bottom;

        private void init(Context context) {
            toast = new Toast(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.layout_toast, null);
            toast_text_tv = view.findViewById(R.id.toast_text_tv);
            toast.setView(view);
            bottom = CommonUtils.dp2pxForInt(context, 50);
        }

        public void display(String text, int duration) {
            if (!TextUtils.isEmpty(text)) {
                toast_text_tv.setText(text);
                toast.setDuration(duration);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, bottom);
                toast.show();
            }
        }
    }
}
