package com.baihe.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.baihe.common.R;
import com.baihe.common.util.CommonUtils;

/**
 * Author：xubo
 * Time：2019-10-17
 * Description：
 */
public class LoadingDialog extends BaseDialog {
    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private boolean cancel = false;
        private String content;
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder isCancel(boolean cancel) {
            this.cancel = cancel;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public LoadingDialog build() {
            DialogBuilder<LoadingDialog> dialogBuilder = new DialogBuilder<LoadingDialog>(context, R.layout.dialog_loading, CommonUtils.getScreenWidth(context) / 2, CommonUtils.getScreenWidth(context) / 2) {
                @Override
                public void createView(final Dialog dialog, View view) {
                    ImageView loading_iv = view.findViewById(R.id.loading_iv);
                    AnimationDrawable drawable = (AnimationDrawable) loading_iv.getDrawable();
                    drawable.start();
                }

                @Override
                public LoadingDialog createDialog(Context context, int themeResId) {
                    return new LoadingDialog(context, themeResId);
                }
            }.setDialogStyle(R.style.LoadingDialog).setCancelable(cancel);
            return dialogBuilder.create();
        }
    }
}
