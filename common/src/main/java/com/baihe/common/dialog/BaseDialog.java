package com.baihe.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * Author：xubo
 * Time：2019-11-04
 * Description：
 */
public class BaseDialog extends Dialog {
    private DialogAction dialogAction;
    private Context context;

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (dialogAction != null) {
            dialogAction.onAttached(context, getWindow().getDecorView());
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (dialogAction != null) {
            dialogAction.onDetached(getWindow().getDecorView());
        }
    }

    public void setDialogAction(DialogAction dialogAction) {
        this.dialogAction = dialogAction;
    }

    public interface DialogAction {
        void onAttached(Context context, View contentView);

        void onDetached(View contentView);
    }
}
