package com.baihe.lihepro.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.view.FlowFixedLayout;

import java.util.List;


/**
 * Author：xubo
 * Time：2019-09-26
 * Description：底部弹起多选择Dialog
 */
public class BottomMultipleSelectDialog extends Dialog {
    public BottomMultipleSelectDialog(Context context) {
        super(context, R.style.CommonDialog);
    }

    public static class Builder {
        private Context context;
        private OnConfirmClickListener confirmListener;
        private OnCancelClickListener cancelListener;
        private String title;
        private boolean cancelable = true;

        private TextView bottom_select_cancel_tv;
        private TextView bottom_select_confirm_tv;
        private TextView bottom_select_title_tv;
        private FlowFixedLayout bottom_select_ffl;

        private FlowFixedLayout.FlowLabelAdapter flowLabelAdapter;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
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

        public Builder setFlowLabelAdapter(FlowFixedLayout.FlowLabelAdapter flowLabelAdapter) {
            this.flowLabelAdapter = flowLabelAdapter;
            return this;
        }

        public BottomMultipleSelectDialog build() {
            BottomMultipleSelectDialog dialog = new BottomMultipleSelectDialog(context);
            dialog.setContentView(R.layout.dialog_bottom_multiple_select);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(cancelable);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setWindowAnimations(R.style.dialog_style);
            window.setGravity(Gravity.BOTTOM);
            initView(dialog);
            initData();
            listener(dialog);
            return dialog;
        }

        public BottomMultipleSelectDialog show() {
            BottomMultipleSelectDialog dialog = build();
            dialog.show();
            return dialog;
        }

        private void initView(Dialog dialog) {
            bottom_select_cancel_tv = dialog.findViewById(R.id.bottom_select_cancel_tv);
            bottom_select_confirm_tv = dialog.findViewById(R.id.bottom_select_confirm_tv);
            bottom_select_title_tv = dialog.findViewById(R.id.bottom_select_title_tv);
            bottom_select_ffl = dialog.findViewById(R.id.bottom_select_ffl);
        }

        private void initData() {
            if (TextUtils.isEmpty(title)) {
                bottom_select_title_tv.setVisibility(View.GONE);
            } else {
                bottom_select_title_tv.setVisibility(View.VISIBLE);
                bottom_select_title_tv.getPaint().setFakeBoldText(true);
                bottom_select_title_tv.setText(title);
            }
            if (flowLabelAdapter != null) {
                bottom_select_ffl.setLabelAdapter(flowLabelAdapter);
            }
        }

        private void listener(final Dialog dialog) {
            bottom_select_ffl.setOnLabelClickListener(new FlowFixedLayout.OnLabelClickListener() {
                @Override
                public void onLabelClick(String text, int index) {

                }
            });
            bottom_select_cancel_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if (cancelListener != null) {
                        cancelListener.onCancel(dialog);
                    }
                }
            });

            bottom_select_confirm_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if (confirmListener != null) {
                        confirmListener.onConfirm(dialog, bottom_select_ffl.getSelectLabelsIndex());
                    }
                }
            });
        }

    }

    public interface OnConfirmClickListener {
        void onConfirm(Dialog dialog, List<Integer> selectPositions);
    }

    public interface OnCancelClickListener {
        void onCancel(Dialog dialog);
    }

}
