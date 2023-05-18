package com.baihe.lihepro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baihe.common.base.BaseActivity;
import com.baihe.lihepro.R;
import com.baihe.lihepro.manager.InputEditManager;
import com.baihe.lihepro.utils.InputEditUtils;

/**
 * Author：xubo
 * Time：2020-08-09
 * Description：
 */
public class InputEditActivity extends BaseActivity {
    private static final String INTENT_TITLE_NAME = "INTENT_TITLE_NAME";
    private static final String INTENT_CONTENT_TEXT = "INTENT_CONTENT_TEXT";
    private static final String INTENT_CONTENT_HINT = "INTENT_CONTENT_HINT";
    private static final String INTENT_BIND_TAG = "INTENT_BIND_TAG";
    private static final String INTENT_INPUT_FILTER = "INTENT_INPUT_FILTER";

    public static void start(Context context, String title, String bindTag) {
        start(context, title, null, bindTag);
    }

    public static void start(Context context, String title, String content, String bindTag) {
        start(context, title, null, content, bindTag,null);
    }

    public static void start(Context context, String title, String hint, String content, String bindTag,String filter) {
        Intent intent = new Intent(context, InputEditActivity.class);
        if (!TextUtils.isEmpty(title)) {
            intent.putExtra(INTENT_TITLE_NAME, title);
        }
        if (!TextUtils.isEmpty(hint)) {
            intent.putExtra(INTENT_CONTENT_HINT, hint);
        }
        if (!TextUtils.isEmpty(content)) {
            intent.putExtra(INTENT_CONTENT_TEXT, content);
        }
        if (!TextUtils.isEmpty(filter))
            intent.putExtra(INTENT_INPUT_FILTER,filter);
        intent.putExtra(INTENT_BIND_TAG, bindTag);
        context.startActivity(intent);
    }

    private EditText input_edit_et;
    private TextView input_edit_confirm_tv;

    private String title;
    private String contentHint;
    private String bindTag;
    private String inputFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra(INTENT_TITLE_NAME);
        contentHint = getIntent().getStringExtra(INTENT_CONTENT_HINT);
        bindTag = getIntent().getStringExtra(INTENT_BIND_TAG);
        inputFilter = getIntent().getStringExtra(INTENT_INPUT_FILTER);
        if (TextUtils.isEmpty(contentHint)) {
            if (!TextUtils.isEmpty(title)) {
                contentHint = "请填写" + title;
            } else {
                contentHint = "";
            }
        }
        title = TextUtils.isEmpty(title) ? "" : ("填写" + title);
        setTitleText(title);
        setContentView(R.layout.activity_input_edit);
        init();
        initData();
        listener();
    }

    private void init() {
        input_edit_et = findViewById(R.id.input_edit_et);
        input_edit_confirm_tv = findViewById(R.id.input_edit_confirm_tv);
    }

    private void initData() {
        input_edit_et.setHint(contentHint);
        String content = getIntent().getStringExtra(INTENT_CONTENT_TEXT);
        if (!TextUtils.isEmpty(content)) {
            input_edit_et.setText(content);
        }
    }

    private void listener() {
        input_edit_confirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputContent = input_edit_et.getText().toString().trim();
                InputEditManager.newInstance().notifyObservers(bindTag, inputContent);
                finish();
            }
        });
        if (inputFilter!=null){
            input_edit_et.setFilters(new InputFilter[]{InputEditUtils.getInputFilter(inputFilter)});
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
