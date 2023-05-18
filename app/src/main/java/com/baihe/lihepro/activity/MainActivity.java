package com.baihe.lihepro.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.baihe.common.MsgClearEvent;
import com.baihe.common.MsgJumpEvent;
import com.baihe.common.base.BaseActivity;
import com.baihe.common.entity.MsgBean;
import com.baihe.common.log.LogUtils;
import com.baihe.common.manager.BackgroundManager;
import com.baihe.common.util.BlurUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.lihepro.BuildConfig;
import com.baihe.lihepro.R;
import com.baihe.lihepro.dialog.UpgradeDialog;
import com.baihe.lihepro.entity.UpgradeEntity;
import com.baihe.lihepro.fragment.HomeFragment;
import com.baihe.lihepro.fragment.MyFragment;
import com.baihe.lihepro.manager.AccountManager;
import com.baihe.lihepro.push.PushHelper;
import com.baihe.lihepro.utils.SPUtils;
import com.baihe.lihepro.view.HomeAddView;
import com.github.xubo.statusbarutils.StatusBarUtils;
import com.tencent.android.tpush.XGPushManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-07-22
 * Description：
 */
public class MainActivity extends BaseActivity implements BackgroundManager.BackgroundObserver {
    public static final String INTENT_UPGRADE = "INTENT_UPGRADE";
    public static final String INTENT_MSG = "INTENT_MSG";
    public static final int REQUEST_CODE_APPROVE = 1;

    public static void start(Context context, UpgradeEntity upgradeEntity) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(INTENT_UPGRADE, upgradeEntity);
        context.startActivity(intent);
    }

    public static void start(Context context, UpgradeEntity upgradeEntity,String message) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(INTENT_UPGRADE, upgradeEntity);
        intent.putExtra(INTENT_MSG, message);
        context.startActivity(intent);
    }

    private RadioGroup main_rg;
    private ImageView bottom_tab_center;
    private FragmentManager fragmentManager;
    private boolean isTransparenLight;

    private HomeAddView addView;
    private Bitmap blurredBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isTransparenLight = StatusBarUtils.setStatusBarTransparenLight(this);
        init();
        initData();
        listener();
        setContent(HomeFragment.TAG);
        initUpgrade();


        EventBus.getDefault().register(this);
        BackgroundManager.newInstance().subscribe(this);
        getPushMessage();


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getPushMessage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        BackgroundManager.newInstance().unBind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_APPROVE: {
//                    Fragment fragment = fragmentManager.findFragmentByTag(HomeFragment.TAG);
//                    if (fragment != null && fragment instanceof HomeFragment) {
//                        HomeFragment homeFragment = (HomeFragment) fragment;
//                        homeFragment.refresh();
//                    }
                }
                break;
            }
        }
    }

    private void getPushMessage(){
        message =  getIntent().getStringExtra(INTENT_MSG);
        LogUtils.d("LihePush","getPushMessage:"+message);
        if (message != null) {
            MsgBean msgBean = JsonUtils.parse(message,MsgBean.class);
            PushHelper.dealPushMessage(this,msgBean,true);
            message = null;
        }else {
            PushHelper.getServiceUnreadCount(AccountManager.newInstance().getUserId(),this);
        }

    }


    private void initUpgrade() {
        final UpgradeEntity upgradeEntity = getIntent().getParcelableExtra(INTENT_UPGRADE);
        if (upgradeEntity != null && getVersion(upgradeEntity.getVersion()) > getVersion(BuildConfig.VERSION_NAME)) {
            UpgradeDialog.Builder builder = new UpgradeDialog.Builder(context)
                    .setCancelable(false)
                    .setContent(upgradeEntity.getMsgContent())
                    .setVersion(upgradeEntity.getVersion())
                    .setOnConfirmClickListener(new UpgradeDialog.OnConfirmClickListener() {
                        @Override
                        public void onConfirm(Dialog dialog) {
                            Uri uri = Uri.parse(upgradeEntity.getUrl());
                            Intent intent3 = new Intent(Intent.ACTION_VIEW, uri);
                            intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent3);
                        }
                    });
            if (upgradeEntity.getForced() == 0) { //软更新
                builder.setOnCancelClickListener(new UpgradeDialog.OnCancelClickListener() {
                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                });
            }
            builder.build().show();
        }
    }

    private int getVersion(String versionName) {
        if (versionName != null) {
            if (versionName.contains(".")) {
                versionName = versionName.replace(".", "");
            }
            try {
                int version = Integer.parseInt(versionName);
                return version;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private void init() {
        main_rg = findViewById(R.id.main_rg);
        bottom_tab_center = findViewById(R.id.bottom_tab_center);
    }

    private void initData() {
        fragmentManager = getSupportFragmentManager();


    }



    private void listener() {
        main_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.main_home_rb:
                        setContent(HomeFragment.TAG);
                        break;
                    case R.id.main_my_rb:
                        setContent(MyFragment.TAG);
                        break;
                }
            }
        });
        bottom_tab_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blurredBitmap != null && !blurredBitmap.isRecycled()) {
                    blurredBitmap.recycle();
                }
                blurredBitmap = BlurUtils.blur(MainActivity.this);
                if (addView == null) {
                    addView = new HomeAddView(context);
                    addView.setListener(new HomeAddView.OnHomeAddViewClickListener() {
                        @Override
                        public void onNewCustomerClick() {
                            CustomerAddActivity.start(context);
                        }

                        @Override
                        public void onNewContractClick() {
                            ContractNewActivity.start(context);
                        }

                        @Override
                        public void onCloseClick() {
                        }
                    });
                }
                if (blurredBitmap != null) {
                    addView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
                }
                addView.show();
            }
        });
    }

    private void setContent(String fragmentTag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        List<Fragment> fragments = fragmentManager.getFragments();
        hideFragments(fragments, fragmentTransaction, fragmentTag);
        if (HomeFragment.TAG.equals(fragmentTag)) {
            HomeFragment fragment = (HomeFragment) fragmentManager.findFragmentByTag(HomeFragment.TAG);
            if (fragment == null) {
                fragment = new HomeFragment();
                fragmentTransaction.add(R.id.main_fl, fragment, fragmentTag);
            } else {
                fragmentTransaction.show(fragment);
            }
        } else if (MyFragment.TAG.equals(fragmentTag)) {
            MyFragment fragment = (MyFragment) fragmentManager.findFragmentByTag(MyFragment.TAG);
            if (fragment == null) {
                fragment = new MyFragment();
                fragmentTransaction.add(R.id.main_fl, fragment, fragmentTag);
            } else {
                fragmentTransaction.show(fragment);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void hideFragments(List<Fragment> fragments, FragmentTransaction fragmentTransaction, String showTag) {
        if (fragments == null || fragments.size() == 0) {
            return;
        }
        for (Fragment fragment : fragments) {
            String tag = fragment.getTag();
            if (!showTag.equals(tag)) {
                fragmentTransaction.hide(fragment);
            }
        }
    }


    @Subscribe
    public void onEvent(MsgJumpEvent event){
        Activity curr = BackgroundManager.getCurrentActivity();
        PushHelper.dealPushMessage(curr,event.customMsg,event.isToList);
    }

    @Subscribe
    public void onEvent(MsgClearEvent event){
        PushHelper.changeServiceUnreadCount(event.messageIds);
        XGPushManager.cancelAllNotifaction(this);
    }

    @Override
    public void change(boolean isBackground) {
        SharedPreferences userSp = SPUtils.getUserSP(context, AccountManager.newInstance().getUserId());
        boolean isFromPush = userSp.getBoolean("fromPush",false);
        LogUtils.d("LihePush","isBackground"+isBackground);
        LogUtils.d("fromPush","fromPush"+isFromPush);
        if (!isBackground && !isFromPush){
            int unreadNum = userSp.getInt("unreadMsg",0);
            Activity curr = BackgroundManager.getCurrentActivity();
            if (unreadNum>0){
                if (curr instanceof BaseActivity){
                    ((BaseActivity)curr).showMsgDialog(AccountManager.newInstance().getUserId());
                }
            }
        }
        userSp.edit().putBoolean("fromPush",false).apply();

    }
}
