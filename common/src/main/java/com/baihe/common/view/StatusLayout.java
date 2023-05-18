package com.baihe.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.baihe.common.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2019-10-22
 * Description：
 */
public class StatusLayout extends ViewGroup {
    private static final String STATUSLAYOUT_PARCELABLE = "statuslayout_parcelable";
    private static final String STATUSLAYOUT_SAVE_STATUS = "statuslayout_save_status";
    private static final long LOADING_MIN_TIME = 500;

    public enum Status {
        LOADING(-1), NORMAL(0), NET_ERROR(1), NET_FAIL(2), EXPAND(3);

        private int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        static Status valueOf(int value) {
            switch (value) {
                case -1:
                    return LOADING;
                case 0:
                    return NORMAL;
                case 1:
                    return NET_ERROR;
                case 2:
                    return NET_FAIL;
                case 3:
                    return EXPAND;
                default:
                    return LOADING;
            }
        }
    }

    private Status status = Status.NORMAL;
    private List<View> childViews;
    private StatusChildLayout statusChildLayout;
    private StatusChildLayout.OnStatusClickListener onStatusClickListener;
    private long loadingStartTime;
    private Handler handler = new Handler();

    private static int defaultNetErrorLayoutId = R.layout.layout_net_error;
    private static int defaultNetFailLayoutId = R.layout.layout_net_fail;
    private static int defaultExpandLayoutId;
    private static int defaultLoadingLayoutId;
    private static StatusChildLayout.LoadingAction defaultLoadingAction;

    public StatusLayout(Context context) {
        this(context, null);
    }

    public StatusLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        StatusChildLayout statusChildLayout = new StatusChildLayout(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StatusLayout);
        int netError_layout_id = typedArray.getResourceId(R.styleable.StatusLayout_netError_layout, 0);
        int netFail_layout_id = typedArray.getResourceId(R.styleable.StatusLayout_netFail_layout, 0);
        int expand_layout_id = typedArray.getResourceId(R.styleable.StatusLayout_expand_layout, 0);
        if (netError_layout_id != 0) {
            statusChildLayout.setNetErrorLayout(netError_layout_id);
        } else {
            statusChildLayout.setNetErrorLayout(defaultNetErrorLayoutId);
        }
        if (netFail_layout_id != 0) {
            statusChildLayout.setNetFailLayout(netFail_layout_id);
        } else {
            statusChildLayout.setNetFailLayout(defaultNetFailLayoutId);
        }
        if (expand_layout_id != 0) {
            statusChildLayout.setExpandLayout(expand_layout_id);
        } else {
            statusChildLayout.setExpandLayout(defaultExpandLayoutId);
        }
        statusChildLayout.setLoadingLayout(defaultLoadingLayoutId, defaultLoadingAction);
        typedArray.recycle();
        addView(statusChildLayout, 0);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initChildViews();
    }

    /**
     * 子View初始化
     */
    private void initChildViews() {
        childViews = new ArrayList<>();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView instanceof StatusChildLayout) {
                statusChildLayout = (StatusChildLayout) childView;
                if (onStatusClickListener != null) {
                    statusChildLayout.setOnStatusClickListener(onStatusClickListener);
                }
            } else {
                childViews.add(childView);
            }
        }
        initStatus();
    }

    /**
     * 状态初始化
     */
    private void initStatus() {
        switch (status) {
            case LOADING:
                loadingStatus();
                break;
            case NORMAL:
                normalStatus();
                break;
            case NET_ERROR:
                netErrorStatus();
                break;
            case NET_FAIL:
                netFailStatus();
                break;
            case EXPAND:
                expandStatus();
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initChildViews();
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView instanceof StatusChildLayout) {
                childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            } else {
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
        }
    }

    /**
     * 网络异常状态
     */
    public void netErrorStatus() {
        long time = System.currentTimeMillis() - loadingStartTime;
        if (status == Status.LOADING && time < LOADING_MIN_TIME) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    netErrorStatus();
                }
            }, LOADING_MIN_TIME - time);
            return;
        }
        status = Status.NET_ERROR;
        if (statusChildLayout != null) {
            statusChildLayout.netError();
        }
        if (childViews != null) {
            for (int i = 0; i < childViews.size(); i++) {
                childViews.get(i).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 服务器异常状态
     */
    public void netFailStatus() {
        long time = System.currentTimeMillis() - loadingStartTime;
        if (status == Status.LOADING && time < LOADING_MIN_TIME) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    netFailStatus();
                }
            }, LOADING_MIN_TIME - time);
            return;
        }
        status = Status.NET_FAIL;
        if (statusChildLayout != null) {
            statusChildLayout.netFail();
        }
        if (childViews != null) {
            for (int i = 0; i < childViews.size(); i++) {
                childViews.get(i).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 扩展状态
     */
    public void expandStatus() {
        long time = System.currentTimeMillis() - loadingStartTime;
        if (status == Status.LOADING && time < LOADING_MIN_TIME) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    expandStatus();
                }
            }, LOADING_MIN_TIME - time);
            return;
        }
        status = Status.EXPAND;
        if (statusChildLayout != null) {
            statusChildLayout.expand();
        }
        if (childViews != null) {
            for (int i = 0; i < childViews.size(); i++) {
                childViews.get(i).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 加载状态
     */
    public void loadingStatus() {
        loadingStartTime = System.currentTimeMillis();
        status = Status.LOADING;
        if (statusChildLayout != null) {
            statusChildLayout.loading();
        }
        if (childViews != null) {
            for (int i = 0; i < childViews.size(); i++) {
                childViews.get(i).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 正常状态
     */
    public void normalStatus() {
        long time = System.currentTimeMillis() - loadingStartTime;
        if (status == Status.LOADING && time < LOADING_MIN_TIME) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    normalStatus();
                }
            }, LOADING_MIN_TIME - time);
            return;
        }
        status = Status.NORMAL;
        if (childViews != null) {
            for (int i = 0; i < childViews.size(); i++) {
                childViews.get(i).setVisibility(View.VISIBLE);
            }
        }
        if (statusChildLayout != null) {
            statusChildLayout.normal();
        }
    }

    /**
     * 获取状态
     *
     * @return
     */
    public Status getStatus() {
        return status;
    }

    /**
     * 设置网络异常布局
     *
     * @param netErrorLayoutId
     */
    public StatusLayout setNetErrorLayout(int netErrorLayoutId) {
        if (statusChildLayout != null) {
            statusChildLayout.setNetErrorLayout(status, netErrorLayoutId);
        }
        return this;
    }

    /**
     * 设置网络异常布局
     *
     * @param netErrorLayout
     */
    public StatusLayout setNetErrorLayout(View netErrorLayout) {
        if (statusChildLayout != null) {
            statusChildLayout.setNetErrorLayout(status, netErrorLayout);
        }
        return this;
    }

    /**
     * 设置服务器异常布局
     *
     * @param netFailLayoutId
     */
    public StatusLayout setNetFailLayout(int netFailLayoutId) {
        if (statusChildLayout != null) {
            statusChildLayout.setNetFailLayout(status, netFailLayoutId);
        }
        return this;
    }

    /**
     * 设置服务器异常布局
     *
     * @param netFailLayout
     */
    public StatusLayout setNetFailLayout(View netFailLayout) {
        if (statusChildLayout != null) {
            statusChildLayout.setNetFailLayout(status, netFailLayout);
        }
        return this;
    }

    /**
     * 设置扩展布局
     *
     * @param expandLayoutId
     */
    public StatusLayout setExpandLayout(int expandLayoutId) {
        if (statusChildLayout != null) {
            statusChildLayout.setExpandLayout(status, expandLayoutId);
        }
        return this;
    }

    /**
     * 设置扩展布局
     *
     * @param expandLayout
     */
    public StatusLayout setExpandLayout(View expandLayout) {
        if (statusChildLayout != null) {
            statusChildLayout.setExpandLayout(status, expandLayout);
        }
        return this;
    }


    /**
     * 设置加载布局
     *
     * @param loadingLayoutId
     */
    public void setLoadingLayout(int loadingLayoutId) {
        if (statusChildLayout != null) {
            statusChildLayout.setLoadingLayout(status, loadingLayoutId);
        }
    }

    /**
     * 设置加载布局
     *
     * @param loadingLayout
     */
    public void setLoadingLayout(View loadingLayout) {
        if (statusChildLayout != null) {
            statusChildLayout.setLoadingLayout(status, loadingLayout);
        }
    }

    /**
     * 设置加载布局
     *
     * @param loadingLayoutId
     * @param loadingAction
     */
    public void setLoadingLayout(int loadingLayoutId, StatusChildLayout.LoadingAction loadingAction) {
        if (statusChildLayout != null) {
            statusChildLayout.setLoadingLayout(status, loadingLayoutId, loadingAction);
        }
    }

    /**
     * 设置加载布局
     *
     * @param loadingLayout
     * @param loadingAction
     */
    public void setLoadingLayout(View loadingLayout, StatusChildLayout.LoadingAction loadingAction) {
        if (statusChildLayout != null) {
            statusChildLayout.setLoadingLayout(status, loadingLayout, loadingAction);
        }
    }

    /**
     * 关闭显示加载布局(如果设置了全局加载布局,可以使用该方法关闭当前页面的加载布局)
     *
     * @return
     */
    public StatusLayout closeDisplayLoadingLayout() {
        if (statusChildLayout != null) {
            statusChildLayout.removeLoadingLayout(status);
        }
        return this;
    }

    /**
     * 设置状态监听
     *
     * @param onStatusClickListener
     */
    public StatusLayout setOnStatusClickListener(StatusChildLayout.OnStatusClickListener onStatusClickListener) {
        this.onStatusClickListener = onStatusClickListener;
        if (statusChildLayout != null) {
            statusChildLayout.setOnStatusClickListener(onStatusClickListener);
        }
        return this;
    }

    /**
     * 获取监听
     *
     * @return
     */
    public StatusChildLayout.OnStatusClickListener getOnStatusClickListener() {
        return onStatusClickListener;
    }

    /**
     * 设置默认网络异常布局
     *
     * @param defaultNetErrorLayoutId
     */
    public static void setDefaultNetErrorLayoutId(int defaultNetErrorLayoutId) {
        StatusLayout.defaultNetErrorLayoutId = defaultNetErrorLayoutId;
    }

    /**
     * 设置默认服务器异常布局
     *
     * @param defaultNetFailLayoutId
     */
    public static void setDefaultNetFailLayoutId(int defaultNetFailLayoutId) {
        StatusLayout.defaultNetFailLayoutId = defaultNetFailLayoutId;
    }

    /**
     * 设置默认扩展布局
     *
     * @param defaultExpandLayoutId
     */
    public static void setDefaultExpandLayoutId(int defaultExpandLayoutId) {
        StatusLayout.defaultExpandLayoutId = defaultExpandLayoutId;
    }

    /**
     * 设置默认加载布局
     * 默认给应用内所有StatusLayout添加加载布局,如果单个页面想关闭可以通过{@link StatusLayout#closeDisplayLoadingLayout}关闭
     *
     * @param defaultLoadingLayoutId
     */
    public static void setDefaultLoadingLayoutId(int defaultLoadingLayoutId) {
        setDefaultLoadingLayoutId(defaultLoadingLayoutId, null);
    }

    /**
     * 设置默认加载布局
     * 默认给应用内所有StatusLayout添加加载布局,如果单个页面想关闭可以通过{@link StatusLayout#closeDisplayLoadingLayout}关闭
     *
     * @param defaultLoadingLayoutId
     * @param defaultLoadingAction
     */
    public static void setDefaultLoadingLayoutId(int defaultLoadingLayoutId, StatusChildLayout.LoadingAction defaultLoadingAction) {
        StatusLayout.defaultLoadingLayoutId = defaultLoadingLayoutId;
        StatusLayout.defaultLoadingAction = defaultLoadingAction;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        Parcelable superParcelable = super.onSaveInstanceState();
        bundle.putParcelable(STATUSLAYOUT_PARCELABLE, superParcelable);
        bundle.putInt(STATUSLAYOUT_SAVE_STATUS, status.getValue());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        Parcelable superParcelable = bundle.getParcelable(STATUSLAYOUT_PARCELABLE);
        int statusValue = bundle.getInt(STATUSLAYOUT_SAVE_STATUS);
        super.onRestoreInstanceState(superParcelable);
        Status status = Status.valueOf(statusValue);
        switch (status.getValue()) {
            case -1:
                initStatus();
                break;
            case 0:
                normalStatus();
                break;
            case 1:
                netErrorStatus();
                break;
            case 2:
                netFailStatus();
                break;
            case 3:
                expandStatus();
                break;
        }
    }

    @Override
    public void addView(View child) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("StatusLayout最多只能有一个childView");
        }
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("StatusLayout最多只能有一个childView");
        }
        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("StatusLayout最多只能有一个childView");
        }
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 1) {
            throw new IllegalStateException("StatusLayout最多只能有一个childView");
        }
        super.addView(child, index, params);
    }
}
