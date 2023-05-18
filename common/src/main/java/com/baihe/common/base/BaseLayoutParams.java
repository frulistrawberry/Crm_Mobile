package com.baihe.common.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Author：xubo
 * Time：2020-07-27
 * Description：
 */
public class BaseLayoutParams extends ViewGroup.LayoutParams {
    public int topMargin;
    public int bottomMargin;
    public int leftMargin;
    public int rightMargin;

    public BaseLayoutParams(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    public BaseLayoutParams(int width, int height) {
        super(width, height);
    }

    public BaseLayoutParams(ViewGroup.LayoutParams source) {
        super(source);
    }
}
