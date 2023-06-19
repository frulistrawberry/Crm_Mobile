package com.baihe.lib_common.widget.keyvalue;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class KeyValueItemLayout extends LinearLayout {
    public KeyValueItemLayout(Context context) {
        super(context);
    }

    public KeyValueItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyValueItemLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getKeyMeasureWidth() {
        if (getChildCount() > 0) {
            return getChildAt(0).getMeasuredWidth();
        }
        return 0;
    }

    public void setKeyWidth(int keyEqualWidth) {
        if (keyEqualWidth > 0 && getChildCount() > 0) {
            View childView = getChildAt(0);
            childView.getLayoutParams().width = keyEqualWidth;
        }
    }
}
