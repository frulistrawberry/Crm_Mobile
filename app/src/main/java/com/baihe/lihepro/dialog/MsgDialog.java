package com.baihe.lihepro.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.util.CommonUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.HotelEntity;
import com.baihe.lihepro.entity.HotelListEntity;
import com.baihe.lihepro.entity.KeyValEventEntity;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.blankj.utilcode.util.ScreenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import q.rorbin.badgeview.QBadgeView;


/**
 * Author：xubo
 * Time：2019-09-26
 * Description：底部弹起选择Dialog
 */
public class MsgDialog extends Dialog {
    QBadgeView badgeView;
    public MsgDialog(Context context) {
        super(context, R.style.CommonDialog);
        badgeView = new QBadgeView(context);
    }

    public void setBadge(int num){
        ImageView msgIv = this.findViewById(R.id.iv_msg);
        badgeView.bindTarget(msgIv);
        badgeView.setBadgeNumber(num);
    }

    public static class Builder {
        private Context context;
        private OnCancelClickListener cancelListener;
        private OnConfirmClickListener confirmListener;
        private String title;
        private boolean cancelable = true;

        private TextView bottom_select_cancel_tv;
        private TextView bottom_select_confirm_tv;
        private TextView bottom_select_title_tv;


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



        public Builder setOnCancelClickListener(OnCancelClickListener cancelListener) {
            this.cancelListener = cancelListener;
            return this;
        }

        public Builder setConfirmListener(OnConfirmClickListener confirmListener) {
            this.confirmListener = confirmListener;
            return this;
        }

        public MsgDialog build() {
            MsgDialog dialog = new MsgDialog(context);
            dialog.setContentView(R.layout.dialog_msg);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(cancelable);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height  = (int) (ScreenUtils.getScreenHeight() * 0.4);
            window.setWindowAnimations(R.style.dialog_style);
            window.setGravity(Gravity.BOTTOM);
            initView(dialog);
            initData();
            listener(dialog);

            return dialog;
        }

        public MsgDialog show() {
            MsgDialog dialog = build();
            dialog.show();
            return dialog;
        }

        private void initView(Dialog dialog) {
            bottom_select_cancel_tv = dialog.findViewById(R.id.bottom_select_cancel_tv);
            bottom_select_confirm_tv = dialog.findViewById(R.id.bottom_select_confirm_tv);
            bottom_select_title_tv = dialog.findViewById(R.id.bottom_select_title_tv);
            bottom_select_confirm_tv.getPaint().setFakeBoldText(true);
        }

        private void initData() {
            if (TextUtils.isEmpty(title)) {
                bottom_select_title_tv.setVisibility(View.GONE);
            } else {
                bottom_select_title_tv.setVisibility(View.VISIBLE);
                bottom_select_title_tv.getPaint().setFakeBoldText(true);
                bottom_select_title_tv.setText(title);
            }

        }



        private void listener(final Dialog dialog) {
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
                    if (confirmListener != null) {
                        confirmListener.onConfirm(dialog);
                    }
                }
            });
        }



    }



    public interface OnConfirmClickListener {
        void onConfirm(Dialog dialog);
    }

    public interface OnCancelClickListener {
        void onCancel(Dialog dialog);
    }

}
