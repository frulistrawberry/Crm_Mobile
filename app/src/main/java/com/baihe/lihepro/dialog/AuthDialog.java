package com.baihe.lihepro.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.baihe.common.dialog.BaseDialog;
import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.LoginUserProtocolActivity;
import com.baihe.lihepro.activity.WebActivity;
import com.baihe.lihepro.constant.UrlConstant;

public class AuthDialog extends BaseDialog {

    private OnAuthListener onAuthListener;

    public AuthDialog(@NonNull Context context,OnAuthListener onAuthListener) {
        super(context, R.style.CommonDialog);
        this.onAuthListener = onAuthListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View dialogVIew = LayoutInflater.from(getContext()).inflate(R.layout.dialog_auth,null);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        getWindow().getAttributes().height = (int) (CommonUtils.getScreenWidth(getContext()) * 0.7);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = (int) (CommonUtils.getScreenWidth(getContext()) * 0.9);
        layoutParams.height = (int) (CommonUtils.getScreenWidth(getContext()) * 0.7);
        dialogVIew.setMinimumWidth((int) (CommonUtils.getScreenWidth(getContext()) * 0.9));
        getWindow().setAttributes(layoutParams);
        getWindow().setGravity(Gravity.CENTER);
        dialogVIew.findViewById(R.id.btn_ok).setOnClickListener(view -> {
            if (onAuthListener!=null)
                onAuthListener.onAgree();
            dismiss();
        });

        dialogVIew.findViewById(R.id.btn_cancel).setOnClickListener(view -> {
            if (onAuthListener != null) {
                onAuthListener.onRefuse();
            }
            dismiss();
        });

        TextView tv_msg = dialogVIew.findViewById(R.id.tv_msg);
        String msg = "亲爱的用户，感谢您信任并使用礼合CRM！\n" +
                "我们非常重视您的个人信息和隐私保护，依据最新法律要求，我们更新了《隐私政策》。\n" +
                "为向您提供更好的结婚服务，在使用我们的产品前，请您认真阅读完整版《礼合CRM用户协议》和《隐私政策》，并充分了解在使用本软件过程中可能收集、使用或共享您个人信息的情形。在此我们对您个人信息的使用进行如下特别提示，请您着重关注：\n" +
                "1、为了保证客户端的正常运行以及能够向您提供更全面的功能服务，我们将可能向系统申请以下权限：\n" +
                "申请获取设备ID用于识别用户信息；申请位置信息用于获取位置经纬度信息对账户操作留存记录；申请访问麦克风用于和语音沟通；申请访问摄像头、访问媒体资料库用于拍照或提供视频上传至系统保存；申请保存图片到相册用于保存婚礼商户信息图片；申请读取通话状态及网络连接状态用于检查用户联网；申请发送通知用于发送服务消息；申请访问日历用于设置婚礼服务提醒。\n" +
                "以上权限均为系统公开权限。并且我们在收取您的上述信息时会以弹窗或法律规定的其他形式征得您的明确同意。\n" +
                "2、如您想开启或关闭相关权限，您可在手机设置——应用程序管理——权限管理中更改（各型号机型设置路径可能存在不一致，请您参照使用手机厂商设置说明）。\n" +
                "\n" +
                "3、如您同意我们的协议及隐私政策内容，请点击同意并继续使用本软件，您在点击同意按钮后将视为您已知悉各项条款的内容，并同意礼合CRM平台关于用户个人信息的合理使用。我们将也将严格按照经您确认后的各项条款使用您的个人信息，并将不断完善技术和安全管理，保护您的个人信息。";

        SpannableString str = new SpannableString(msg);

        str.setSpan(new ClickableSpan() {

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);

                ds.setColor(Color.parseColor("#2DB4E6"));
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(@NonNull View view) {
                LoginUserProtocolActivity.start(getContext());
            }
        },msg.indexOf("《礼合CRM用户协议》"),msg.indexOf("《礼合CRM用户协议》")+"《礼合CRM用户协议》".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        str.setSpan(new ClickableSpan() {

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);

                ds.setColor(Color.parseColor("#2DB4E6"));
                ds.setUnderlineText(false);
            }


            @Override
            public void onClick(@NonNull View view) {
                WebActivity.start(getContext(), "隐私政策",UrlConstant.PRIVACY_POLICY);

            }
        },msg.indexOf("《隐私政策》",msg.indexOf("《礼合CRM用户协议》")),msg.indexOf("《隐私政策》",msg.indexOf("《礼合CRM用户协议》"))+"《隐私政策》".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        tv_msg.setText(str);
        tv_msg.setMovementMethod(LinkMovementMethod.getInstance());

        setContentView(dialogVIew);
    }

    public interface OnAuthListener{
        void onAgree();
        void onRefuse();
    }
}
