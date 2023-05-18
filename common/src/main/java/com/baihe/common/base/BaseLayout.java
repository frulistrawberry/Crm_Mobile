package com.baihe.common.base;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import com.baihe.common.view.StatusLayout;

/**
 * Author：xubo
 * Time：2019-10-23
 * Description：
 */
public class BaseLayout extends ViewGroup {
    private static final String TITLE_TAG = "title_tag";
    private static final String CONTENT_TAG = "content_tag";

    public BaseLayout(Context context, View titleView, LayoutParams titleParams, View contentView, BaseLayoutParams contentParams) {
        super(context);
        if (titleView != null && titleParams != null) {
            titleView.setTag(TITLE_TAG);
            addView(titleView, titleParams);
        }
        contentView.setTag(CONTENT_TAG);
        StatusLayout statusLayout = new StatusLayout(context);
        statusLayout.addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(statusLayout, 0, contentParams);
        //activity存在底色，需要把Content背景作为底色
        Drawable backgorundDrawable = contentView.getBackground();
        if (backgorundDrawable != null && backgorundDrawable instanceof ColorDrawable) {
            ColorDrawable colordDrawable = (ColorDrawable) backgorundDrawable;
            setBackgroundColor(colordDrawable.getColor());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int titleHeight = 0;
        int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            View childView = getChildAt(i);
            String tag = (String) childView.getTag();
            if (TITLE_TAG.equals(tag)) {
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                titleHeight = childView.getMeasuredHeight();
            } else {
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                int contentHeight = getMeasuredHeight();
                int contentWidth = getMeasuredWidth();

                //计算margin相关
                BaseLayoutParams params = (BaseLayoutParams) childView.getLayoutParams();
                int contentMaxHeight = getMeasuredHeight() - titleHeight - params.topMargin - params.bottomMargin;
                int contentMaxWidth = getMeasuredWidth() - params.leftMargin - params.rightMargin;

                contentHeight = contentHeight > contentMaxHeight ? contentMaxHeight : contentHeight;
                contentWidth = contentWidth > contentMaxWidth ? contentMaxWidth : contentWidth;

                int contentParentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(contentHeight, MeasureSpec.getMode(heightMeasureSpec));
                int contentParentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(contentWidth, MeasureSpec.getMode(heightMeasureSpec));
                measureChild(childView, contentParentWidthMeasureSpec, contentParentHeightMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int titleHeight = 0;
        int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            View childView = getChildAt(i);
            String tag = (String) childView.getTag();
            if (TITLE_TAG.equals(tag)) {
                titleHeight = childView.getMeasuredHeight();
                childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            } else {
                BaseLayoutParams params = (BaseLayoutParams) childView.getLayoutParams();
                childView.layout(params.leftMargin, titleHeight + params.topMargin, params.leftMargin + childView.getMeasuredWidth(), titleHeight + params.topMargin + childView.getMeasuredHeight());
            }
        }
    }

    /**
     * 获取状态控制View
     *
     * @return
     */
    public StatusLayout getStatusLayout() {
        View statusChild = getChildAt(0);
        if (statusChild instanceof StatusLayout) {
            return (StatusLayout) statusChild;
        }
        return null;
    }
}
