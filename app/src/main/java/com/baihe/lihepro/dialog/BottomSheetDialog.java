package com.baihe.lihepro.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.baihe.lihepro.R;

public class BottomSheetDialog extends Dialog {
    private TextView option_one, option_two;
    private TextView option_cancel;


    private BottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public static BottomSheetDialog create(Context context) {
        BottomSheetDialog instace = new BottomSheetDialog(context);
        return instace;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_two_options_dialog);

        setCanceledOnTouchOutside(true);
        getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setWindowAnimations(R.style.dialog_style);
        getWindow().setGravity(Gravity.BOTTOM);

        option_one = findViewById(R.id.option_one);
        option_two = findViewById(R.id.option_two);
        option_cancel = findViewById(R.id.option_cancel);

        if(TextUtils.isEmpty(optionText1)) {
            option_one.setVisibility(View.GONE);
        } else {
            option_one.setVisibility(View.VISIBLE);
            option_one.setText(optionText1);
            option_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener1 != null) {
                        listener1.onClick(v);
                    }
                    dismiss();
                }
            });
        }

        if(TextUtils.isEmpty(optionText2)) {
            option_two.setVisibility(View.GONE);
        } else {
            option_two.setVisibility(View.VISIBLE);
            option_two.setText(optionText2);
            option_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener2 != null) {
                        listener2.onClick(v);
                    }
                    dismiss();
                }
            });
        }

        option_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    String optionText1, optionText2;
    View.OnClickListener listener1, listener2;


    public BottomSheetDialog optionText1(String text) {
        optionText1 = text;
        return this;
    }
    public BottomSheetDialog optionText2(String text) {
        optionText2 = text;
        return this;
    }

    public BottomSheetDialog listener1(View.OnClickListener listener1) {
        this.listener1 = listener1;
        return this;
    }
    public BottomSheetDialog listener2(View.OnClickListener listener2) {
        this.listener2 = listener2;
        return this;
    }




}
