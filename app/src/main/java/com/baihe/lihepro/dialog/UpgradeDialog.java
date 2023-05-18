package com.baihe.lihepro.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.baihe.common.dialog.BaseDialog;
import com.baihe.common.dialog.DialogBuilder;
import com.baihe.lihepro.R;

/**
 * Author：xubo
 * Time：2020-08-18
 * Description：
 */
public class UpgradeDialog extends BaseDialog {
    public UpgradeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private boolean cancelable = false;
        private OnConfirmClickListener confirmListener;
        private OnCancelClickListener cancelListener;
        private Context context;
        private String title;
        private String content;
        private String version;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setOnConfirmClickListener(OnConfirmClickListener confirmListener) {
            this.confirmListener = confirmListener;
            return this;
        }

        public Builder setOnCancelClickListener(OnCancelClickListener cancelListener) {
            this.cancelListener = cancelListener;
            return this;
        }

        public UpgradeDialog build() {
            DialogBuilder<UpgradeDialog> dialogBuilder = new DialogBuilder<UpgradeDialog>(context, R.layout.dialog_upgrade, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT) {
                @Override
                public void createView(final Dialog dialog, View view) {
                    TextView upgrade_title_tv = view.findViewById(R.id.upgrade_title_tv);
                    TextView upgrade_version_tv = view.findViewById(R.id.upgrade_version_tv);
                    TextView upgrade_content_tv = view.findViewById(R.id.upgrade_content_tv);
                    TextView upgrade_cancel_tv = view.findViewById(R.id.upgrade_cancel_tv);
                    TextView upgrade_ok_tv = view.findViewById(R.id.upgrade_ok_tv);
                    if (!TextUtils.isEmpty(title)) {
                        upgrade_title_tv.setText(title);
                    }
                    if (!TextUtils.isEmpty(version)) {
                        upgrade_version_tv.setText("V" + version);
                    } else {
                        upgrade_version_tv.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(content)) {
                        upgrade_content_tv.setText(content);
                    }
                    if (cancelListener != null) {
                        upgrade_cancel_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelListener.onCancel(dialog);
                            }
                        });
                    } else {
                        upgrade_cancel_tv.setVisibility(View.GONE);
                    }
                    if (confirmListener != null) {
                        upgrade_ok_tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmListener.onConfirm(dialog);
                            }
                        });
                    } else {
                        upgrade_ok_tv.setVisibility(View.GONE);
                    }
                }

                @Override
                public UpgradeDialog createDialog(Context context, int themeResId) {
                    return new UpgradeDialog(context, themeResId);
                }
            }.setGravity(Gravity.CENTER).setCancelable(cancelable);
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
