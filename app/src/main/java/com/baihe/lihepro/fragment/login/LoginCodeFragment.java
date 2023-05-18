package com.baihe.lihepro.fragment.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;

import com.baihe.common.base.BaseFragment;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.StringUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.http.Http;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.MainActivity;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.UpgradeEntity;
import com.baihe.lihepro.entity.UserEntity;
import com.baihe.lihepro.manager.AccountManager;
import com.baihe.lihepro.push.PushHelper;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import me.leefeng.libverify.VerificationView;


public class LoginCodeFragment extends BaseFragment implements View.OnClickListener {
    public static final String CODE_STATUS_ERROE = "0";
    public static final String CODE_STATUS_SUCCESS = "1";

    private TextView tv_title;
    private VerificationView verificationView;
    private TextView tvGetCode;
    private TextView tvGetCodeHint;
    private String code_staus;
    private String phoneNumber;
    private boolean isToHome = true;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login_code;
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTitleView(view);
        initWidget(view);
        initData();
        initListener();
    }

    private void initTitleView(View rootView) {
        Toolbar title_tb = rootView.findViewById(R.id.title_tb);
        title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });
    }

    private void initWidget(View rootView) {
        tv_title = rootView.findViewById(R.id.tv_title);
        verificationView = rootView.findViewById(R.id.verificationView);
        tvGetCode = rootView.findViewById(R.id.tvGetCode);
        tvGetCodeHint = rootView.findViewById(R.id.tvGetCodeHint);
    }


    private void initData() {
        if (getArguments() != null) {
            LoginCodeFragmentArgs args = LoginCodeFragmentArgs.fromBundle(getArguments());
            code_staus = args.getCodeStaus();
            isToHome = args.getIsToHome();
            phoneNumber = args.getPhoneNumber();
        }

        if (CODE_STATUS_ERROE.equals(code_staus)) {
            tv_title.setText("验证码发送失败，请点击重新获取验证码");
            tvGetCode.setVisibility(View.VISIBLE);
            tvGetCodeHint.setVisibility(View.GONE);
        } else {
            tvGetCode.setVisibility(View.GONE);
            tvGetCodeHint.setVisibility(View.VISIBLE);
            tv_title.setText("验证码已发送至 +86 " + StringUtils.phoneNumberAddSpaceFormat(phoneNumber));
            showCountDownTimer();
        }
    }


    private void showCountDownTimer() {
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvGetCodeHint.setText(millisUntilFinished / 1000 + " 秒后重新获取验证码");
            }

            @Override
            public void onFinish() {
                tvGetCodeHint.setVisibility(View.GONE);
                tvGetCode.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void initListener() {
        tvGetCode.setOnClickListener(this);
        verificationView.setFinish(new Function1<String, Unit>() {
            @Override
            public Unit invoke(String s) {
                login(s);
                return null;
            }
        });
    }


    private void login(final String code) {
        if (TextUtils.isEmpty(code)) {
            ToastUtils.toast("请输入验证码");
            return;
        }

        //清空数据
        AccountManager.newInstance().clearUser();
        Http.getInstance().getParamsInterceptor().setParam("userId", "");

        HttpRequest.create(UrlConstant.LOGIN_URL)
                .requestAlias("登录")
                .putParam(JsonParam.newInstance("params")
                        .putParamValue("userName", phoneNumber).putParamValue("verifyCode", code)
                        .putParamValue("verifyType", "1")
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvGetCode:// 先检验手机号是否绑定再获取验证码
                getCode();
                break;
        }
    }

    private void getCode() {
        HttpRequest.create(UrlConstant.SEND_VERIFYCODE_URL)
                .requestAlias("发送验证码")
                .putParam(JsonParam.newInstance("params").putParamValue("mobile", phoneNumber).putParamValue("verifyType", "1"))
                .get(new CallBack<String>() {

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
                            tvGetCode.setVisibility(View.GONE);
                            tvGetCodeHint.setVisibility(View.VISIBLE);
                            ToastUtils.toast("短信验证码已发送，请查收");
                            tv_title.setText("验证码已发送至 +86 " + StringUtils.phoneNumberAddSpaceFormat(phoneNumber));
                            showCountDownTimer();
                        } else {
                            tv_title.setText("验证码发送失败，请点击重新获取验证码");
                            tvGetCode.setVisibility(View.VISIBLE);
                            tvGetCodeHint.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void error() {
                        ToastUtils.toastNetError();
                        tvGetCode.setVisibility(View.VISIBLE);
                        tvGetCodeHint.setVisibility(View.GONE);
                    }

                    @Override
                    public void fail() {
                        ToastUtils.toastNetWorkFail();
                        tvGetCode.setVisibility(View.VISIBLE);
                        tvGetCodeHint.setVisibility(View.GONE);
                    }
                });
    }
}
