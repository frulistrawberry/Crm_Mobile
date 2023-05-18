package com.baihe.lihepro.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.util.CommonUtils;

public class MaxRecyclerView extends RecyclerView {
    public MaxRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MaxRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        heightSpec = MeasureSpec.makeMeasureSpec(CommonUtils.dp2pxForInt(getContext(),200),MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec);

    }
}
