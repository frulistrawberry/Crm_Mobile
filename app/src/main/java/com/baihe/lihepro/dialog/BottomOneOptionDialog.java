package com.baihe.lihepro.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.baihe.lihepro.R;


/**
 * all dialog format 从百合项目引入
 * l_leftBtn1 左边button监听回调     l_rightBtn1 右边button回调不     title1 标题      content 内容
 */
@SuppressLint("ResourceAsColor")
public class BottomOneOptionDialog extends Dialog {
    private static int style = R.style.CommonDialog;
    private Activity mActivity;
    private String optionText;
    private TextView cancel_button;
    private TextView option_one;
    private View.OnClickListener onClickListener;

    /**
     * l_leftBtn1 左边button监听回调     l_rightBtn1 右边button回调不     title1 标题      content 内容
     *
     * @param activity
     */
    public BottomOneOptionDialog(Activity activity, String optionText, final View.OnClickListener onClickListener) {
        super(activity, style);
        this.mActivity = activity;
        this.optionText = optionText;
        this.onClickListener = onClickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_one_options_dialog);
        this.setCanceledOnTouchOutside(false);
        option_one = findViewById(R.id.option_one);
        cancel_button = findViewById(R.id.cancel_button);
        option_one.setText(optionText);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels);
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.dialog_style);
        dialogWindow.setGravity(Gravity.BOTTOM);

        option_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity == null) return;
                if (!mActivity.isFinishing() && isShowing()) {
                    dismiss();
                }
                if (onClickListener != null) onClickListener.onClick(v);
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}