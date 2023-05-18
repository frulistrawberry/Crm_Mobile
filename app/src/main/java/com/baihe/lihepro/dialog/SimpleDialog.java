package com.baihe.lihepro.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.StyleRes;

import com.baihe.lihepro.R;
import com.github.xubo.statusbarutils.StatusBarUtils;


/**
 * Author：xubo
 * Time：2019-10-21
 * Description：简单Dialog
 */
public class SimpleDialog extends Dialog {
    public SimpleDialog(Context context) {
        this(context, R.style.CommonDialog);
    }

    public SimpleDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static abstract class Builder {
        private Context context;
        private boolean cancelable;
        private int animationRes;
        private int gravity;
        private View contentView;
        private int width;
        private int height;
        private View attachView;
        private int attachOffsetX;
        private int attachOffsetY;
        private int themeResId;

        public Builder(Context context, View view) {
            this(context, view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        public Builder(Context context, @LayoutRes int layoutResId) {
            this(context, layoutResId, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        public Builder(Context context, @LayoutRes int layoutResId, int width, int height) {
            this(context, LayoutInflater.from(context).inflate(layoutResId, null), width, height);
        }

        public Builder(Context context, View view, int width, int height) {
            this.context = context;
            this.contentView = view;
            this.width = width;
            this.height = height;
            this.gravity = Gravity.CENTER;
            this.cancelable = true;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setAnimationRes(@StyleRes int animationRes) {
            this.animationRes = animationRes;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setAttachOffset(int attachOffsetX, int attachOffsetY) {
            this.attachOffsetX = attachOffsetX;
            this.attachOffsetY = attachOffsetY;
            return this;
        }

        public Builder setAttachView(View attachView) {
            this.attachView = attachView;
            return this;
        }

        public Builder setDialogStyle(@StyleRes int themeResId) {
            this.themeResId = themeResId;
            return this;
        }

        private SimpleDialog create() {
            SimpleDialog dialog = null;
            if (themeResId != 0) {
                dialog = new SimpleDialog(context, themeResId);
            } else {
                dialog = new SimpleDialog(context);
            }
            dialog.setContentView(contentView);
            dialog.setCancelable(cancelable);
            if (cancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = width;
            params.height = height;
            if (animationRes != 0) {
                window.setWindowAnimations(animationRes);
            }
            window.setGravity(gravity);
            if (attachView != null) {
                int[] location = new int[2];
                attachView.getLocationOnScreen(location);
                int locationX = location[0] + attachView.getMeasuredWidth() / 2 + attachOffsetX;
                int locationY = location[1] + attachView.getMeasuredHeight() - StatusBarUtils.getStatusBarHeight(context) + attachOffsetY;
                params.x = locationX;
                params.y = locationY;

            }
            dialogCreate(dialog, contentView);
            return dialog;
        }

        public abstract void dialogCreate(Dialog dialog, View view);

        public SimpleDialog show() {
            SimpleDialog dialog = create();
            dialog.show();
            return dialog;
        }

    }

}
