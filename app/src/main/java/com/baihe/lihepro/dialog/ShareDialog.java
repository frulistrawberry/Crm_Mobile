package com.baihe.lihepro.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.baihe.common.log.LogUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.lihepro.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

public class ShareDialog extends Dialog {


    public ShareDialog(@NonNull Context context) {
        super(context, R.style.CommonDialog);
    }

    public static class Builder  {
        private Activity context;
        private LinearLayout wechatShareBtn;
        private LinearLayout wechatCircleShareBtn;
        private LinearLayout qqShareBtn;
        private TextView cancelBtn;

        private String shareUrl;
        private String title;
        private String description;
        private String thumb;
        private UMWeb shareContent;
        protected ShareCallBack shareCallBack;

        public Builder(Activity context) {
            this.context = context;
        }

        public Builder setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setThumb(String thumb) {
            this.thumb = thumb;
            return this;
        }

        public ShareDialog build(){
            ShareDialog dialog = new ShareDialog(context);
            dialog.setContentView(R.layout.dialog_share);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setWindowAnimations(R.style.dialog_style);
            window.setGravity(Gravity.BOTTOM);
            initView(dialog);
            initData(dialog);
            setListener(dialog);
            return dialog;
        }

        private void initData(ShareDialog dialog) {

            shareContent = new UMWeb(shareUrl);
            shareContent.setTitle(title);
            shareContent.setDescription(description);
            if (!TextUtils.isEmpty(thumb))
                shareContent.setThumb(new UMImage(context,thumb));
            shareCallBack = new ShareCallBack(dialog);
        }

        private void setListener(final ShareDialog dialog) {
            wechatCircleShareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (shareContent != null) {
                        new ShareAction(context).withMedia(shareContent)
                                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                                .setCallback(shareCallBack)
                                .share();
                    }
                }
            });

            wechatShareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (shareContent != null) {
                        new ShareAction(context).withMedia(shareContent)
                                .setPlatform(SHARE_MEDIA.WEIXIN)
                                .setCallback(shareCallBack)
                                .share();
                    }
                }
            });

            qqShareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (shareContent != null) {
                        new ShareAction(context).withMedia(shareContent)
                                .setPlatform(SHARE_MEDIA.QQ)
                                .setCallback(shareCallBack)
                                .share();
                    }
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
        }

        private void initView(ShareDialog dialog) {
            wechatShareBtn = dialog.findViewById(R.id.wechat_share_btn);
            wechatCircleShareBtn = dialog.findViewById(R.id.wechat_circle_share_btn);
            qqShareBtn = dialog.findViewById(R.id.qq_share_btn);
            cancelBtn = dialog.findViewById(R.id.cancel_btn);
        }


    }

    public static class ShareCallBack implements UMShareListener{

        private ShareDialog dialog;

        public ShareCallBack(ShareDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            dialog.dismiss();
            ToastUtils.toast("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            ToastUtils.toast("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            LogUtils.d("share","分享取消");
        }
    }
}
