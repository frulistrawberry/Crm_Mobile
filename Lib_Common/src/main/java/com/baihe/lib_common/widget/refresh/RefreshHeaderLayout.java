package com.baihe.lib_common.widget.refresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baihe.lib_common.R;
import com.baihe.lib_framework.utils.DpToPx;
import com.baihe.lib_framework.utils.ResUtils;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

/**
 * Author：xubo
 * Time：2020-07-24
 * Description：
 */
@SuppressLint("RestrictedApi")
public class RefreshHeaderLayout extends LinearLayout implements RefreshHeader {
    private View refreshView;
    private boolean isRefresh;

    public RefreshHeaderLayout(Context context) {
        this(context, null);
    }

    public RefreshHeaderLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeaderLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        refreshView = LayoutInflater.from(context).inflate(R.layout.common_refresh_loading, this, false);
        addView(refreshView);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {
        if (!isDragging || isRefresh) {
            return;
        }
        ImageView loading_iv = findViewById(R.id.loading_iv);
        loading_iv.setImageDrawable(ResUtils.getImageFromResource(R.drawable.icon_refresh_00000));
        TextView loading_tv = findViewById(R.id.loading_tv);

        int textHeight = loading_tv.getMeasuredHeight();
        int maxTranslationY = (height - textHeight + DpToPx.dpToPx( 5)) * 85 / 150;
        float scale = percent > 1 ? 1 : percent;

        int translationY = (int) (maxTranslationY * (1 - scale));
        loading_iv.setTranslationY(translationY);
        scale = 0.2f + 0.8f * scale;
        loading_iv.setScaleX(scale);
        loading_iv.setScaleY(scale);
        if (percent > 1) {
            loading_tv.setText("释放立即刷新");
        } else {
            loading_tv.setText("下拉可以刷新");
        }
    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        isRefresh = true;
        TextView loading_tv = findViewById(R.id.loading_tv);
        loading_tv.setText("正在刷新");
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int extendHeight) {
        ImageView loading_iv = findViewById(R.id.loading_iv);
        loading_iv.setImageDrawable(ResUtils.getImageFromResource(R.drawable.common_loading_refresh_anim));
        AnimationDrawable drawable = (AnimationDrawable) loading_iv.getDrawable();
        drawable.start();
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        isRefresh = false;
        ImageView loading_iv = findViewById(R.id.loading_iv);
        AnimationDrawable drawable = (AnimationDrawable) loading_iv.getDrawable();
        drawable.stop();
        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public boolean autoOpen(int duration, float dragRate, boolean animationOnly) {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

    }
}
