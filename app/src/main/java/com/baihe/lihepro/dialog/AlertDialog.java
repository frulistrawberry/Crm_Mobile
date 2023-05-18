package com.baihe.lihepro.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.baihe.common.dialog.BaseDialog;
import com.baihe.common.dialog.DialogBuilder;
import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;

public class AlertDialog extends BaseDialog {
    public AlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private boolean cancelable = true;
        private Context context;
        private OnConfirmClickListener confirmListener;
        private OnCancelClickListener cancelListener;
        private String confirmText;
        private String cancelText;
        private String title;
        private String content;

        public Builder(Context context) {
            this.context = context;
        }

        public AlertDialog.Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public AlertDialog.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public AlertDialog.Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public AlertDialog.Builder setConfirmListener(OnConfirmClickListener confirmListener) {
            this.confirmListener = confirmListener;
            return this;
        }

        public AlertDialog.Builder setConfirmListener(String text, OnConfirmClickListener confirmListener) {
            this.confirmText = text;
            this.confirmListener = confirmListener;
            return this;
        }

        public AlertDialog.Builder setCancelListener(OnCancelClickListener cancelListener) {
            this.cancelListener = cancelListener;
            return this;
        }

        public AlertDialog.Builder setCancelListener(String text, OnCancelClickListener cancelListener) {
            this.cancelText = text;
            this.cancelListener = cancelListener;
            return this;
        }

        public AlertDialog build() {
            int width = CommonUtils.getScreenWidth(context) * 325 / 375;
            DialogBuilder<AlertDialog> dialogBuilder = new DialogBuilder<AlertDialog>(context, R.layout.dialog_tip, width, WindowManager.LayoutParams.WRAP_CONTENT) {
                @Override
                public void createView(final Dialog dialog, View view) {
                    TextView common_dialog_title = view.findViewById(R.id.common_dialog_title);
                    TextView common_dialog_msg = view.findViewById(R.id.common_dialog_msg);
                    TextView common_dialog_left = view.findViewById(R.id.common_dialog_left);
                    TextView common_dialog_right = view.findViewById(R.id.common_dialog_right);
                    ImageView common_dialog_close = view.findViewById(R.id.common_dialog_close);
                    View viewLine = view.findViewById(R.id.viewLine);
                    LinearLayout llBtn = view.findViewById(R.id.llBtn);
                    if (TextUtils.isEmpty(title)) {
                        common_dialog_title.setVisibility(View.GONE);
                    } else {
                        common_dialog_title.setVisibility(View.VISIBLE);
                        common_dialog_title.setText(title);
                    }
                    if (TextUtils.isEmpty(content)) {
                        common_dialog_msg.setVisibility(View.GONE);
                    } else {
                        common_dialog_msg.setVisibility(View.VISIBLE);
                        common_dialog_msg.setText(content);
                    }
                    if (cancelListener != null) {
                        common_dialog_left.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(cancelText)) {
                            common_dialog_left.setText(cancelText);
                        }
                        common_dialog_left.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelListener.onCancel(dialog);
                            }
                        });
                    } else {
                        common_dialog_left.setVisibility(View.GONE);
                    }
                    if (confirmListener != null) {
                        common_dialog_right.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(confirmText)) {
                            common_dialog_right.setText(confirmText);
                        }
                        common_dialog_right.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmListener.onConfirm(dialog);
                            }
                        });
                    } else {
                        common_dialog_right.setVisibility(View.GONE);
                    }
                    if (confirmListener == null && cancelListener == null) {
                        viewLine.setVisibility(View.GONE);
                        llBtn.setVisibility(View.GONE);
                    }
                    common_dialog_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }

                @Override
                public AlertDialog createDialog(Context context, int themeResId) {
                    return new AlertDialog(context, themeResId);
                }
            }.setCancelable(cancelable).setGravity(Gravity.CENTER);
            return dialogBuilder.create();
        }
    }

    public interface OnConfirmClickListener {
        void onConfirm(Dialog dialog);
    }

    public interface OnCancelClickListener {
        void onCancel(Dialog dialog);
    }
}
