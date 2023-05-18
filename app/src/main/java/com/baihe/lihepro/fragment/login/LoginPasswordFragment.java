package com.baihe.lihepro.fragment.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.baihe.common.base.BaseFragment;
import com.baihe.common.textwatcher.FormatPhoneTextWatcher;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.StringUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.http.Http;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.LoginUserProtocolActivity;
import com.baihe.lihepro.activity.MainActivity;
import com.baihe.lihepro.activity.WebActivity;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.UpgradeEntity;
import com.baihe.lihepro.entity.UserEntity;
import com.baihe.lihepro.manager.AccountManager;
import com.baihe.lihepro.push.PushHelper;

public class LoginPasswordFragment extends BaseFragment implements View.OnClickListener {

    private EditText etPhone;
    private ImageView iv_phone_del;
    private EditText et_password;
    private ImageView iv_password_eye;
    private ImageView iv_password_del;
    private TextView tvLogin;
    private TextView tv_phone_login;
    private CheckBox ck_protocol;
    private TextView tv_privacy_policy;
    private TextView tv_user_protocol;
    private boolean isCheck;
    private boolean isShown;
    private String phoneNumber;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login_password;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidget(view);
        if (getArguments() != null) {
            LoginPasswordFragmentArgs args = LoginPasswordFragmentArgs.fromBundle(getArguments());
            phoneNumber = args.getPhoneNumber();
            isCheck = args.getIsCheck();
        }
        initListener();
    }

    private void initWidget(View view) {
        etPhone = view.findViewById(R.id.etPhone);
        iv_phone_del = view.findViewById(R.id.iv_phone_del);
        et_password = view.findViewById(R.id.et_password);
        iv_password_eye = view.findViewById(R.id.iv_password_eye);
        iv_password_del = view.findViewById(R.id.iv_password_del);
        tvLogin = view.findViewById(R.id.tvLogin);
        tv_phone_login = view.findViewById(R.id.tv_phone_login);
        ck_protocol = view.findViewById(R.id.ck_protocol);
        tv_privacy_policy = view.findViewById(R.id.tv_privacy_policy);
        tv_user_protocol = view.findViewById(R.id.tv_user_protocol);
        ck_protocol.setChecked(isCheck);

    }

    private void initListener() {
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
                if (editable.length()>0) {
                    iv_phone_del.setVisibility(View.VISIBLE);
                }else {
                    iv_phone_del.setVisibility(View.GONE);
                }
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0) {
                    iv_password_del.setVisibility(View.VISIBLE);
                }else {
                    iv_password_del.setVisibility(View.GONE);
                }
            }
        });
        ck_protocol.setOnCheckedChangeListener((compoundButton, b) -> isCheck = b);
        iv_phone_del.setOnClickListener(this);
        iv_password_del.setOnClickListener(this);
        iv_password_eye.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tv_phone_login.setOnClickListener(this);
        tv_privacy_policy.setOnClickListener(this);
        tv_user_protocol.setOnClickListener(this);
        if (TextUtils.isEmpty(phoneNumber))
        etPhone.setText(phoneNumber);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_phone_del:
                etPhone.setText("");
                etPhone.requestFocus();
                break;
            case R.id.iv_password_del:
                et_password.setText("");
                et_password.requestFocus();
                break;
            case R.id.iv_password_eye:
                if (isShown){
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_password_eye.setImageResource(R.drawable.icon_eye_close);
                }
                else{
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_password_eye.setImageResource(R.drawable.icon_eye_open);
                }
                et_password.setSelection(et_password.getText().length());
                isShown = !isShown;
                break;
            case R.id.tvLogin:
                String phoneNum = etPhone.getText().toString();
                String number = StringUtils.formatPhoneNum(phoneNum);
                String password = et_password.getText().toString();

                if (TextUtils.isEmpty(number)) {
                    ToastUtils.toast("请输入手机号");
                    return;
                }
                if (!StringUtils.isPhone(number)) {
                    ToastUtils.toast("手机号输入格式不正确");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    ToastUtils.toast("请输入密码");
                    return;
                }
                if (!isCheck){
                    ToastUtils.toast("请同意并勾选用户协议");
                    return;
                }
                login(number,password);
                break;
            case R.id.tv_phone_login:
                Navigation.findNavController(view).popBackStack();
                break;
            case R.id.tv_privacy_policy:
                WebActivity.start(getContext(), UrlConstant.PRIVACY_POLICY);
                break;
            case R.id.tv_user_protocol:
                LoginUserProtocolActivity.start(getContext());
                break;
        }
    }

    private void login(String phoneNum, String password) {
        AccountManager.newInstance().clearUser();
        Http.getInstance().getParamsInterceptor().setParam("userId", "");

        HttpRequest.create(UrlConstant.LOGIN_URL)
                .requestAlias("登录")
                .putParam(JsonParam.newInstance("params")
                        .putParamValue("userName", phoneNum).putParamValue("passWord", password)
                        .putParamValue("keeplogin", "1"))
                .get(new CallBack<UserEntity>() {

                    @Override
                    public void before() {
                        super.before();
                        showOptionLoading();
                    }

                    @Override
                    public UserEntity doInBackground(String response) {
                        UserEntity userEntity = JsonUtils.parse(response, UserEntity.class);
                        return userEntity;
                    }

                    @Override
                    public void success(final UserEntity userEntity) {
                        //更新user
                        AccountManager.newInstance().saveUser(userEntity);
                        //更新http拦截器里的userId
                        Http.getInstance().getParamsInterceptor().setParam("userId", userEntity.getUser_id());
                        //获取版本信息
                        getVersion();
                        //push绑定账号
                        PushHelper.bindAccount(getContext());
                    }

                    @Override
                    public void error() {
                        ToastUtils.toastNetError();
                    }

                    @Override
                    public void fail() {
                        ToastUtils.toastNetWorkFail();
                    }

                    @Override
                    public void after(UserEntity userEntity) {
                        super.after(userEntity);
                        if (userEntity == null) {
                            dismissOptionLoading();
                        }
                    }
                });
    }

    public void getVersion() {
        HttpRequest.create(UrlConstant.UPGRADE_URL).putParam("channel", "android").get(new CallBack<UpgradeEntity>() {
            @Override
            public UpgradeEntity doInBackground(String response) {
                return JsonUtils.parse(response, UpgradeEntity.class);
            }

            @Override
            public void success(UpgradeEntity entity) {

            }

            @Override
            public void error() {

            }

            @Override
            public void fail() {

            }

            @Override
            public void after(UpgradeEntity upgradeEntity) {
                super.after(upgradeEntity);
                dismissOptionLoading();
                MainActivity.start(getContext(), upgradeEntity);
                getActivity().finish();
            }

        });
    }
}
