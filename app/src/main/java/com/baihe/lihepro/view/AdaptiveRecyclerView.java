package com.baihe.lihepro.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author：xubo
 * Time：2020-02-26
 * Description：高度自适应RecyclerView（Item无法复用）
 */
public class AdaptiveRecyclerView extends RecyclerView {
    public AdaptiveRecyclerView(@NonNull Context context) {
        super(context);
    }

    public AdaptiveRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdaptiveRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 1, MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);
    }
}
