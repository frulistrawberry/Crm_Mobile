package com.baihe.common.base;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.baihe.common.MsgClearEvent;
import com.baihe.common.MsgJumpEvent;
import com.baihe.common.R;
import com.baihe.common.dialog.MsgDialog;
import com.baihe.common.entity.MsgBean;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.SPUtils;
import com.github.xubo.statusbarutils.StatusBarUtils;

import com.baihe.common.dialog.LoadingDialog;
import com.baihe.common.manager.BackgroundManager;
import com.baihe.common.view.StatusLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-03-01
 * Description：
 */
public class BaseActivity extends AppCompatActivity {
    protected String tagName;
    protected Context context;
    protected BaseApplication app;
    protected StatusLayout statusLayout;
    protected LoadingDialog loadingDialog;

    protected View titleView;
    private ViewGroup.LayoutParams titleParams;
    protected MsgDialog msgDialog;
    protected String message;

    public BaseActivity() {
        tagName = this.getClass().getSimpleName();
        context = this;
        app = BaseApplication.getInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBar();
        StatusBarUtils.setStatusBarColorLight(this, Color.WHITE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        BackgroundManager.newInstance().activityStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        BackgroundManager.newInstance().activityStop();
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = LayoutInflater.from(context).inflate(layoutResID, null);
        setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        BaseLayoutParams baseLayoutParams = new BaseLayoutParams(params);
        BaseLayout baseLayout = new BaseLayout(context, titleView, titleParams, view, baseLayoutParams);
        statusLayout = baseLayout.getStatusLayout();
        super.setContentView(baseLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setContentView(View view, BaseLayoutParams params){
        BaseLayout baseLayout = new BaseLayout(context, titleView, titleParams, view, params);
        statusLayout = baseLayout.getStatusLayout();
        super.setContentView(baseLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置Title文字，需setContentView方法之前调用
     *
     * @param text
     */
    protected void setTitleText(CharSequence text) {
        titleParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.commont_title_height));
        titleView = LayoutInflater.from(context).inflate(R.layout.layout_common_title, null);
        Toolbar title_tb = titleView.findViewById(R.id.title_tb);
        title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView title_text_tv = titleView.findViewById(R.id.title_text_tv);
        title_text_tv.getPaint().setFakeBoldText(true);
        title_text_tv.setText(text);
    }

    /**
     * 设置自定义Title，需setContentView方法之前调用
     *
     * @param layoutResID
     */
    protected void setTitleView(int layoutResID) {
        View view = LayoutInflater.from(context).inflate(layoutResID, null);
        setTitleView(view);
    }

    /**
     * 设置自定义Title，需setContentView方法之前调用
     *
     * @param view
     */
    protected void setTitleView(View view) {
        setTitleView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    /**
     * 设置自定义Title，需setContentView方法之前调用
     *
     * @param view
     * @param width
     * @param height
     */
    protected void setTitleView(View view, int width, int height) {
        titleParams = new ViewGroup.LayoutParams(width, height);
        titleView = LayoutInflater.from(context).inflate(R.layout.layout_common_title, null);
        Toolbar title_tb = titleView.findViewById(R.id.title_tb);
        title_tb.setVisibility(View.GONE);
        RelativeLayout title_custom_rl = titleView.findViewById(R.id.title_custom_rl);
        title_custom_rl.addView(view, new RelativeLayout.LayoutParams(width, height));
    }

    /**
     * 显示操作加载对话框
     */
    public void showOptionLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog.Builder(context).build();
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    /**
     * 关闭操作加载对话框
     */
    public void dismissOptionLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 隐藏bar
     */
    private void hideBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void showMsgDialog(final String userId){
        final SharedPreferences userSp = SPUtils.getUserSP(this,userId);
        if (msgDialog == null){
            msgDialog = new MsgDialog.Builder(this).setTitle("消息提醒").setConfirmListener(new MsgDialog.OnConfirmClickListener() {
                @Override
                public void onConfirm(Dialog dialog) {
                    String msgListJson = userSp.getString("msgList","[]");
                    List<MsgBean> msgList = JsonUtils.parseList(msgListJson,MsgBean.class);
                    int unreadNum = userSp.getInt("unreadMsg",0);
                    MsgBean msgBean = null;
                    if (msgList!=null && msgList.size()>0){
                        msgBean = msgList.get(msgList.size()-1);
                    }
                    StringBuilder sb = new StringBuilder();
                    if (msgList!=null){
                        for (MsgBean msg : msgList) {
                            sb.append(msg.message_id).append(",");
                        }
                    }

                    if (sb.length()>0){
                        sb.deleteCharAt(sb.lastIndexOf(","));
                        EventBus.getDefault().post(new MsgClearEvent(sb.toString()));
                    }
                    EventBus.getDefault().post(new MsgJumpEvent(unreadNum>1,msgBean));
                    userSp.edit().putInt("unreadMsg",0).apply();
                    userSp.edit().putString("msgList","[]").apply();
                    dialog.dismiss();
                }
            }).setOnCancelClickListener(new MsgDialog.OnCancelClickListener() {
                @Override
                public void onCancel(Dialog dialog) {
                    String msgListJson = userSp.getString("msgList","[]");
                    List<MsgBean> msgList = JsonUtils.parseList(msgListJson,MsgBean.class);
                    StringBuilder sb = new StringBuilder();
                    if (msgList!=null)
                    for (MsgBean msgBean : msgList) {
                        sb.append(msgBean.message_id).append(",");
                    }
                    if (sb.length()>0){
                        sb.deleteCharAt(sb.lastIndexOf(","));
                        EventBus.getDefault().post(new MsgClearEvent(sb.toString()));
                    }

                    userSp.edit().putInt("unreadMsg",0).apply();
                    userSp.edit().putString("msgList","[]").apply();
                    dialog.dismiss();
                }
            }).setCancelable(false).build();
        }
        if (!msgDialog.isShowing())
            msgDialog.show();
        int unreadNum = userSp.getInt("unreadMsg",0);
        msgDialog.setBadge(unreadNum);
        EventBus.getDefault().post("refresh_home_unread_count");
    }


}
