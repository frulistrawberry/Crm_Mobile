package com.baihe.lihepro.dialog;

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
public class BottomThreeOptionDialog extends Dialog {
    private static int style = R.style.CommonDialog;
    private Activity mActivity;
    private TextView tv_cancel_button;
    private TextView tv_option_one;
    private TextView tv_option_two;
    private TextView tv_option_three;
    private OnItemClickListner onItemClickListner;
    private String[] itemTexts;

    /**
     * l_leftBtn1 左边button监听回调     l_rightBtn1 右边button回调不     title1 标题      content 内容
     *
     * @param activity
     */
    public BottomThreeOptionDialog(Activity activity, String[] itemTexts, final OnItemClickListner onItemClickListner) {
        super(activity, style);
        this.mActivity = activity;
        this.itemTexts = itemTexts;
        this.onItemClickListner = onItemClickListner;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_three_options_dialog);
        this.setCanceledOnTouchOutside(false);
        tv_option_one = findViewById(R.id.tv_option_one);
        tv_option_two = findViewById(R.id.tv_option_two);
        tv_option_three = findViewById(R.id.tv_option_three);
        tv_cancel_button = findViewById(R.id.tv_cancel_button);
        tv_option_one.setText(itemTexts[0]);
        tv_option_two.setText(itemTexts[1]);
        tv_option_three.setText(itemTexts[2]);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (mActivity.getResources().getDisplayMetrics().widthPixels);
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.dialog_style);
        dialogWindow.setGravity(Gravity.BOTTOM);

        tv_option_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity == null) return;
                if (!mActivity.isFinishing() && isShowing()) {
                    dismiss();
                }
                if (onItemClickListner != null) onItemClickListner.onOneItemClick(v);
            }
        });

        tv_option_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity == null) return;
                if (!mActivity.isFinishing() && isShowing()) {
                    dismiss();
                }
                if (onItemClickListner != null) onItemClickListner.onTwoItemClick(v);
            }
        });

        tv_option_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity == null) return;
                if (!mActivity.isFinishing() && isShowing()) {
                    dismiss();
                }
                if (onItemClickListner != null) onItemClickListner.onThreeItemClick(v);
            }
        });

        tv_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public interface OnItemClickListner {
        void onOneItemClick(View v);
        void onTwoItemClick(View v);
        void onThreeItemClick(View v);
    }

}