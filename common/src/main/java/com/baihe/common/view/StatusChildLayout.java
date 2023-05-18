package com.baihe.common.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author：xubo
 * Time：2019-10-22
 * Description：
 */
public class StatusChildLayout extends ViewGroup {
    private View netErrorLayout;
    private View netFailLayout;
    private View expandLayout;
    private View loadingLayout;
    private OnStatusClickListener onStatusClickListener;
    private LoadingAction loadingAction;
    private LayoutInflater inflater;
    private Context context;

    public StatusChildLayout(Context context) {
        super(context);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY);
        if (netErrorLayout != null) {
            netErrorLayout.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
        if (netFailLayout != null) {
            netFailLayout.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
        if (expandLayout != null) {
            expandLayout.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
        if (loadingLayout != null) {
            loadingLayout.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (netErrorLayout != null) {
            netErrorLayout.layout(0, 0, netErrorLayout.getMeasuredWidth(), netErrorLayout.getMeasuredHeight());
        }
        if (netFailLayout != null) {
            netFailLayout.layout(0, 0, netFailLayout.getMeasuredWidth(), netFailLayout.getMeasuredHeight());
        }
        if (expandLayout != null) {
            expandLayout.layout(0, 0, expandLayout.getMeasuredWidth(), expandLayout.getMeasuredHeight());
        }
        if (loadingLayout != null) {
            loadingLayout.layout(0, 0, loadingLayout.getMeasuredWidth(), loadingLayout.getMeasuredHeight());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            if (loadingLayout != null && loadingAction != null) {
                loadingAction.onDetached(loadingLayout);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 正常状态
     */
    protected void normal() {
        setVisibility(View.GONE);
        if (loadingLayout != null && loadingAction != null) {
            loadingAction.onDetached(loadingLayout);
        }
    }

    /**
     * 网络异常状态
     */
    protected void netError() {
        if (netErrorLayout != null) {
            setVisibility(View.VISIBLE);
            if (netFailLayout != null) {
                netFailLayout.setVisibility(View.GONE);
            }
            if (expandLayout != null) {
                expandLayout.setVisibility(View.GONE);
            }
            if (loadingLayout != null) {
                loadingLayout.setVisibility(View.GONE);
            }
            netErrorLayout.setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }
        if (loadingLayout != null && loadingAction != null) {
            loadingAction.onDetached(loadingLayout);
        }
    }

    /***
     * 服务器异常状态
     */
    protected void netFail() {
        if (netFailLayout != null) {
            setVisibility(View.VISIBLE);
            if (netErrorLayout != null) {
                netErrorLayout.setVisibility(View.GONE);
            }
            if (expandLayout != null) {
                expandLayout.setVisibility(View.GONE);
            }
            if (loadingLayout != null) {
                loadingLayout.setVisibility(View.GONE);
            }
            netFailLayout.setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }
        if (loadingLayout != null && loadingAction != null) {
            loadingAction.onDetached(loadingLayout);
        }
    }

    /**
     * 扩展状态
     */
    protected void expand() {
        if (expandLayout != null) {
            setVisibility(View.VISIBLE);
            if (netErrorLayout != null) {
                netErrorLayout.setVisibility(View.GONE);
            }
            if (netFailLayout != null) {
                netFailLayout.setVisibility(View.GONE);
            }
            if (loadingLayout != null) {
                loadingLayout.setVisibility(View.GONE);
            }
            expandLayout.setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
        }
        if (loadingLayout != null && loadingAction != null) {
            loadingAction.onDetached(loadingLayout);
        }
    }

    /**
     * 加载状态
     */
    protected void loading() {
        if (loadingLayout != null) {
            setVisibility(View.VISIBLE);
            if (netErrorLayout != null) {
                netErrorLayout.setVisibility(View.GONE);
            }
            if (netFailLayout != null) {
                netFailLayout.setVisibility(View.GONE);
            }
            if (expandLayout != null) {
                expandLayout.setVisibility(View.GONE);
            }
            loadingLayout.setVisibility(View.VISIBLE);
            if (loadingLayout != null && loadingAction != null) {
                loadingAction.onAttached(context, loadingLayout);
            }
        } else {
            setVisibility(View.GONE);
        }
    }

    /**
     * 设置网络异常布局
     *
     * @param netErrorLayoutId
     */
    protected void setNetErrorLayout(int netErrorLayoutId) {
        setNetErrorLayout(null, netErrorLayoutId);
    }

    /**
     * 设置网络异常布局
     *
     * @param netErrorLayout
     */
    protected void setNetErrorLayout(View netErrorLayout) {
        setNetErrorLayout(null, netErrorLayout);
    }

    /**
     * 设置网络异常布局
     *
     * @param status
     * @param netErrorLayoutId
     */
    protected void setNetErrorLayout(StatusLayout.Status status, int netErrorLayoutId) {
        View netErrorLayout = inflater.inflate(netErrorLayoutId, null);
        setNetErrorLayout(status, netErrorLayout);
    }

    /**
     * 设置网络异常布局
     *
     * @param netErrorLayout
     */
    protected void setNetErrorLayout(StatusLayout.Status status, View netErrorLayout) {
        if (netErrorLayout == null) {
            return;
        }
        if (this.netErrorLayout != null) {
            removeView(this.netErrorLayout);
        }
        addView(netErrorLayout);
        netErrorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onStatusClickListener != null) {
                    onStatusClickListener.onNetErrorClick();
                }
            }
        });
        this.netErrorLayout = netErrorLayout;
        requestLayout();
        statusRest(status);
    }

    /**
     * 设置服务器异常布局
     *
     * @param netFailLayoutId
     */
    protected void setNetFailLayout(int netFailLayoutId) {
        setNetFailLayout(null, netFailLayoutId);
    }

    /**
     * 设置服务器异常布局
     *
     * @param netFailLayout
     */
    protected void setNetFailLayout(View netFailLayout) {
        setNetFailLayout(null, netFailLayout);
    }

    /**
     * 设置服务器异常布局
     *
     * @param status
     * @param netFailLayoutId
     */
    protected void setNetFailLayout(StatusLayout.Status status, int netFailLayoutId) {
        View netFailLayout = inflater.inflate(netFailLayoutId, null);
        setNetFailLayout(status, netFailLayout);
    }

    /**
     * 设置服务器异常布局
     *
     * @param status
     * @param netFailLayout
     */
    protected void setNetFailLayout(StatusLayout.Status status, View netFailLayout) {
        if (netFailLayout == null) {
            return;
        }
        if (this.netFailLayout != null) {
            removeView(this.netFailLayout);
        }
        addView(netFailLayout);
        netFailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onStatusClickListener != null) {
                    onStatusClickListener.onNetFailClick();
                }
            }
        });
        this.netFailLayout = netFailLayout;
        requestLayout();
        statusRest(status);
    }

    /**
     * 设置扩展布局
     *
     * @param expandLayoutId
     */
    protected void setExpandLayout(int expandLayoutId) {
        setExpandLayout(null, expandLayoutId);
    }

    /**
     * 设置扩展布局
     *
     * @param expandLayout
     */
    protected void setExpandLayout(View expandLayout) {
        setExpandLayout(null, expandLayout);
    }

    /**
     * 设置扩展布局
     *
     * @param status
     * @param expandLayoutId
     */
    protected void setExpandLayout(StatusLayout.Status status, int expandLayoutId) {
        if (expandLayoutId == 0) {
            return;
        }
        View expandLayout = inflater.inflate(expandLayoutId, null);
        setExpandLayout(status, expandLayout);
    }

    /**
     * 设置扩展布局
     *
     * @param status
     * @param expandLayout
     */
    protected void setExpandLayout(StatusLayout.Status status, View expandLayout) {
        if (expandLayout == null) {
            return;
        }
        if (this.expandLayout != null) {
            removeView(this.expandLayout);
        }
        addView(expandLayout);
        expandLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onStatusClickListener != null) {
                    onStatusClickListener.onExpandClick();
                }
            }
        });
        this.expandLayout = expandLayout;
        requestLayout();
        statusRest(status);
    }

    /**
     * 设置加载布局
     *
     * @param loadingLayoutId
     */
    protected void setLoadingLayout(int loadingLayoutId) {
        setLoadingLayout(null, loadingLayoutId, null);
    }

    /**
     * 设置加载布局
     *
     * @param loadingLayout
     */
    protected void setLoadingLayout(View loadingLayout) {
        setLoadingLayout(null, loadingLayout, null);
    }

    /**
     * 设置加载布局
     *
     * @param loadingLayoutId
     * @param loadingAction
     */
    protected void setLoadingLayout(int loadingLayoutId, LoadingAction loadingAction) {
        setLoadingLayout(null, loadingLayoutId, loadingAction);
    }

    /**
     * 设置加载布局
     *
     * @param loadingLayout
     * @param loadingAction
     */
    protected void setLoadingLayout(View loadingLayout, LoadingAction loadingAction) {
        setLoadingLayout(null, loadingLayout, loadingAction);
    }

    /**
     * 设置加载布局
     *
     * @param status
     * @param loadingLayoutId
     */
    protected void setLoadingLayout(StatusLayout.Status status, int loadingLayoutId) {
        setLoadingLayout(status, loadingLayoutId, null);
    }

    /**
     * 设置加载布局
     *
     * @param status
     * @param loadingLayout
     */
    protected void setLoadingLayout(StatusLayout.Status status, View loadingLayout) {
        setLoadingLayout(status, loadingLayout, null);
    }

    /**
     * 设置加载布局
     *
     * @param status
     * @param loadingLayoutId
     * @param loadingAction
     */
    protected void setLoadingLayout(StatusLayout.Status status, int loadingLayoutId, LoadingAction loadingAction) {
        if (loadingLayoutId == 0) {
            return;
        }
        View loadingLayout = inflater.inflate(loadingLayoutId, null);
        setLoadingLayout(status, loadingLayout, loadingAction);
    }

    /**
     * 设置加载布局
     *
     * @param status
     * @param loadingLayout
     * @param loadingAction
     */
    protected void setLoadingLayout(StatusLayout.Status status, View loadingLayout, LoadingAction loadingAction) {
        if (loadingLayout == null) {
            return;
        }
        if (this.loadingLayout != null) {
            if (this.loadingAction != null) {
                this.loadingAction.onDetached(this.loadingLayout);
            }
            removeView(this.loadingLayout);
        }
        addView(loadingLayout);
        this.loadingLayout = loadingLayout;
        this.loadingAction = loadingAction;
        requestLayout();
        statusRest(status);
    }

    /**
     * 去除加载布局
     *
     * @param status
     */
    protected void removeLoadingLayout(StatusLayout.Status status) {
        if (loadingLayout == null) {
            return;
        }
        if (loadingLayout != null) {
            removeView(loadingLayout);
        }
        this.loadingLayout = null;
        this.loadingAction = null;
        statusRest(status);
    }

    /**
     * 状态重置
     *
     * @param status
     */
    private void statusRest(StatusLayout.Status status) {
        if (status == null) {
            return;
        }
        switch (status.getValue()) {
            case -1:
                loading();
                break;
            case 0:
                normal();
                break;
            case 1:
                netError();
                break;
            case 2:
                netFail();
                break;
            case 3:
                expand();
                break;
        }
    }

    /**
     * 设置点击监听
     *
     * @param onStatusClickListener
     */
    protected void setOnStatusClickListener(final OnStatusClickListener onStatusClickListener) {
        this.onStatusClickListener = onStatusClickListener;
        netErrorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onStatusClickListener != null) {
                    onStatusClickListener.onNetErrorClick();
                }
            }
        });
        netFailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onStatusClickListener != null) {
                    onStatusClickListener.onNetFailClick();
                }
            }
        });
        if (expandLayout != null) {
            expandLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onStatusClickListener != null) {
                        onStatusClickListener.onExpandClick();
                    }
                }
            });
        }
    }

    /**
     * 获取监听
     *
     * @return
     */
    public OnStatusClickListener getOnStatusClickListener() {
        return onStatusClickListener;
    }

    /**
     * 获取加载布局View
     *
     * @return
     */
    public View getLoadingLayout() {
        return loadingLayout;
    }

    /**
     * 点击监听
     */
    public interface OnStatusClickListener {
        void onNetErrorClick();

        void onNetFailClick();

        void onExpandClick();
    }

    /**
     * 加载布局操作
     */
    public interface LoadingAction {
        /**
         * 加载布局展示设置的动作(加载动画开始可在这执行)
         *
         * @param context
         * @param loadingLayout
         */
        void onAttached(Context context, View loadingLayout);

        /**
         * 加载布局消失设置的动作(加载动画结束或资源销毁可在这执行)
         *
         * @param loadingLayout
         */
        void onDetached(View loadingLayout);
    }
}
