package com.baihe.lihepro.fragment.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.baihe.common.base.BaseFragment;
import com.baihe.common.textwatcher.FormatPhoneTextWatcher;
import com.baihe.common.util.StringUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.LoginUserProtocolActivity;
import com.baihe.lihepro.activity.WebActivity;
import com.baihe.lihepro.constant.UrlConstant;

public class LoginPhoneFragment extends BaseFragment implements View.OnClickListener {
    private EditText etPhone;
    private TextView tvLogin;
    private boolean isToHome = true;

    private CheckBox ck_protocol;
    private TextView tv_user_protocol;
    private TextView tv_privacy_policy;
    private TextView tv_password_login;
    private ImageView iv_phone_del;
    private boolean isUserProtocolChecked;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login_phone;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidget(view);
        initListener();
    }

    private void initWidget(View rootView) {
        etPhone = rootView.findViewById(R.id.etPhone);
        tvLogin = rootView.findViewById(R.id.tvLogin);
        tv_password_login = rootView.findViewById(R.id.tv_password_login);
        iv_phone_del = rootView.findViewById(R.id.iv_phone_del);
        etPhone.setFocusable(true);
        etPhone.setFocusableInTouchMode(true);
        etPhone.requestFocus();
        tvLogin.setEnabled(true);

        tv_user_protocol = rootView.findViewById(R.id.tv_user_protocol);
        tv_privacy_policy = rootView.findViewById(R.id.tv_privacy_policy);
        ck_protocol = rootView.findViewById(R.id.ck_protocol);
        ck_protocol.setChecked(false);
        isUserProtocolChecked = false;
    }

    private void initListener() {
        tvLogin.setOnClickListener(this);
        tv_user_protocol.setOnClickListener(this);
        tv_password_login.setOnClickListener(this);
        tv_privacy_policy.setOnClickListener(this);
        iv_phone_del.setOnClickListener(this);
        etPhone.addTextChangedListener(new FormatPhoneTextWatcher(etPhone));
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0){

                }
            }
        });
        ck_protocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isUserProtocolChecked = isChecked;
            }
        });
    }


    private void login() {
        String phoneNum = etPhone.getText().toString().trim();
        //纯数字字符串
        String number = StringUtils.formatPhoneNum(phoneNum);

        if (TextUtils.isEmpty(number)) {
            ToastUtils.toast("请输入手机号");
            return;
        }
        if (!StringUtils.isPhone(number)) {
            ToastUtils.toast("手机号输入格式不正确");
            return;
        }

        if (!isUserProtocolChecked) {
            ToastUtils.toast("请同意并勾选用户协议");
            return;
        }
        getCode(number);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLogin:// 先检验手机号是否绑定再获取验证码
                login();
                break;
            case R.id.iv_phone_del:
                etPhone.setText("");
                etPhone.requestFocus();
                break;
            case R.id.tv_user_protocol:
                LoginUserProtocolActivity.start(getContext());
                break;
            case R.id.tv_privacy_policy:
                WebActivity.start(getContext(),UrlConstant.PRIVACY_POLICY);
                break;
            case R.id.tv_password_login:
                String phoneNum = etPhone.getText().toString().trim();
                String number = StringUtils.formatPhoneNum(phoneNum);
                LoginPhoneFragmentDirections.ActionLoginPhoneFragmentToLoginPasswordFragment action =
                        LoginPhoneFragmentDirections.actionLoginPhoneFragmentToLoginPasswordFragment(number);
                Navigation.findNavController(v).navigate(action);
                break;

        }
    }

    private void getCode(final String phoneNumber) {
        HttpRequest.create(UrlConstant.SEND_VERIFYCODE_URL).requestAlias("发送验证码").putParam(JsonParam.newInstance("params").putParamValue("mobile", phoneNumber).putParamValue("verifyType", "1")).get(new CallBack<String>() {

            @Override
            public void before() {
                super.before();
                showOptionLoading();
            }

            @Override
            public void after() {
                super.after();
                dismissOptionLoading();
            }

            @Override
            public String doInBackground(String response) {
                return response;
            }

            @Override
            public void success(String result) {
                if ("1".equals(result)) {
                    ToastUtils.toast("短信验证码已发送，请查收");
                    LoginPhoneFragmentDirections.ActionLoginPhoneFragmentToLoginCodeFragment action =
                            LoginPhoneFragmentDirections.actionLoginPhoneFragmentToLoginCodeFragment(isToHome,
                                    LoginCodeFragment.CODE_STATUS_SUCCESS,
                                    phoneNumber);
                    Navigation.findNavController(etPhone).navigate(action);
                } else {
                    ToastUtils.toast("验证码发送失败，请点击重新获取验证码");
                }
            }

            @Override
            public void error() {
                ToastUtils.toastNetError();
            }

            @Override
            public void fail() {
                ToastUtils.toastNetWorkFail();
            }
        });
    }


}


