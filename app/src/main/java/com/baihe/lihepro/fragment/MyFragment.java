package com.baihe.lihepro.fragment;

import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baihe.common.base.BaseFragment;
import com.baihe.common.util.JsonUtils;
import com.baihe.http.Http;
import com.baihe.http.HttpRequest;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.GlideApp;
import com.baihe.lihepro.R;
import com.baihe.lihepro.activity.LoginActivity;
import com.baihe.lihepro.activity.SettingActivity;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.CrmAlertDialog;
import com.baihe.lihepro.entity.UserEntity;
import com.baihe.lihepro.manager.AccountManager;
import com.baihe.lihepro.push.PushHelper;
import com.baihe.lihepro.utils.Utils;
import com.blankj.utilcode.util.ScreenUtils;

/**
 * Author：xubo
 * Time：2020-07-22
 * Description：
 */
public class MyFragment extends BaseFragment {
    public static final String TAG = MyFragment.class.getSimpleName();

    private RelativeLayout my_top_rl;
    private ImageView my_head_iv;
    private TextView my_entry_name_tv;
    private TextView my_entry_company_tv;
    private TextView my_entry_post_tv;
    private TextView my_entry_time_tv;
    private TextView my_department_tv;
    private TextView my_phone_tv;
    private TextView my_exit_tv;
    private ImageView iv_setting;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        listener();
        refresh();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refresh();
        }
    }

    private void refresh() {
        HttpRequest.create(UrlConstant.GET_USERINFOBY_ID_URL).get(new CallBack<UserEntity>() {
            @Override
            public UserEntity doInBackground(String response) {
                return JsonUtils.parse(response, UserEntity.class);
            }

            @Override
            public void success(UserEntity userEntity) {
                AccountManager.newInstance().saveUser(userEntity);
                initMember(userEntity);
            }

            @Override
            public void error() {

            }

            @Override
            public void fail() {

            }
        });
    }

    private void initView(View view) {
        my_top_rl = view.findViewById(R.id.my_top_rl);
        my_head_iv = view.findViewById(R.id.my_head_iv);
        my_entry_name_tv = view.findViewById(R.id.my_entry_name_tv);
        my_entry_company_tv = view.findViewById(R.id.my_entry_company_tv);
        my_entry_post_tv = view.findViewById(R.id.my_entry_post_tv);
        my_entry_time_tv = view.findViewById(R.id.my_entry_time_tv);
        my_entry_name_tv = view.findViewById(R.id.my_entry_name_tv);
        my_department_tv = view.findViewById(R.id.my_department_tv);
        my_phone_tv = view.findViewById(R.id.my_phone_tv);
        my_exit_tv = view.findViewById(R.id.my_exit_tv);
        iv_setting = view.findViewById(R.id.iv_setting);
    }

    private void initData() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getContext().getResources(), R.drawable.my_top_bg, options);
        int topHeight = (int) (ScreenUtils.getScreenWidth() * ((float) options.outHeight / (float) options.outWidth));
        my_top_rl.getLayoutParams().height = topHeight;
        initMember(AccountManager.newInstance().getUser());
    }

    private void listener() {
        my_exit_tv.setOnClickListener(v -> new CrmAlertDialog.Builder(getContext()).setContent("您确定退出吗？").setCancelListener(new CrmAlertDialog.OnCancelClickListener() {
            @Override
            public void onCancel(Dialog dialog) {
                dialog.dismiss();
            }
        }).setConfirmListener(dialog -> {
            dialog.dismiss();
            //清除账号和cookie
            PushHelper.logoutAccount();
            AccountManager.newInstance().clearUser();
            Http.getInstance().getCookieStore().clearAllCookies();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();


            Utils.startTaskActivity(app, LoginActivity.class);
        }).build().show());

        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.start(getContext());
            }
        });
    }

    private void initMember(UserEntity userEntity) {
        GlideApp.with(getContext()).load(userEntity.getAvatar()).placeholder(R.drawable.user_head_default).into(my_head_iv);
        my_entry_name_tv.setText(userEntity.getName());
        my_entry_company_tv.setText(userEntity.getCompany_name());
        my_entry_post_tv.setText(userEntity.getPosition_name());
        my_entry_time_tv.setText(TextUtils.isEmpty(userEntity.getCreate_time()) ? "未填写" : userEntity.getCreate_time());
        my_department_tv.setText(TextUtils.isEmpty(userEntity.getStructure_name()) ? "未填写" : userEntity.getStructure_name());
        my_phone_tv.setText(TextUtils.isEmpty(userEntity.getAccount()) ? "未填写" : userEntity.getAccount());
    }
}
