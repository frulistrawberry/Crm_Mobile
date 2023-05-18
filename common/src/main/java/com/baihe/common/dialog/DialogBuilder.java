package com.baihe.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.StyleRes;

import com.baihe.common.R;
import com.github.xubo.statusbarutils.StatusBarUtils;

/**
 * Author：xubo
 * Time：2019-11-04
 * Description：
 */
public abstract class DialogBuilder<SD extends BaseDialog> {
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
    private BaseDialog.DialogAction dialogAction;

    public DialogBuilder(Context context, View view) {
        this(context, view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public DialogBuilder(Context context, @LayoutRes int layoutResId) {
        this(context, layoutResId, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public DialogBuilder(Context context, @LayoutRes int layoutResId, int width, int height) {
        this(context, LayoutInflater.from(context).inflate(layoutResId, null), width, height);
    }

    public DialogBuilder(Context context, View view, int width, int height) {
        this.context = context;
        this.contentView = view;
        this.width = width;
        this.height = height;
        this.gravity = Gravity.CENTER;
        this.cancelable = true;
    }

    public DialogBuilder setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public DialogBuilder setAnimationRes(@StyleRes int animationRes) {
        this.animationRes = animationRes;
        return this;
    }

    public DialogBuilder setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public DialogBuilder setAttachOffset(int attachOffsetX, int attachOffsetY) {
        this.attachOffsetX = attachOffsetX;
        this.attachOffsetY = attachOffsetY;
        return this;
    }

    public DialogBuilder setAttachView(View attachView) {
        this.attachView = attachView;
        return this;
    }

    public DialogBuilder setDialogStyle(@StyleRes int themeResId) {
        this.themeResId = themeResId;
        return this;
    }

    public DialogBuilder setDialogAction(BaseDialog.DialogAction dialogAction) {
        this.dialogAction = dialogAction;
        return this;
    }

    public SD create() {
        SD dialog;
        if (themeResId != 0) {
            dialog = createDialog(context, themeResId);
        } else {
            dialog = createDialog(context, R.style.CommonDialog);
        }
        dialog.setContentView(contentView);
        dialog.setCancelable(cancelable);
        if (cancelable) {
            dialog.setCanceledOnTouchOutside(true);
        }
        dialog.setDialogAction(dialogAction);
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
        createView(dialog, contentView);
        return dialog;
    }

    public abstract SD createDialog(Context context, int themeResId);

    public void createView(Dialog dialog, View view) {

    }

    public SD show() {
        SD dialog = create();
        dialog.show();
        return dialog;
    }

}
