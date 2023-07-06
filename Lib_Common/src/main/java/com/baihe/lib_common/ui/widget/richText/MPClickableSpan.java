package com.baihe.lib_common.ui.widget.richText;

import android.text.style.ClickableSpan;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * 创建者： liufuxiang
 * 创建时间：2018/11/30 下午7:01
 * 类描述：直播消息点击span
 */
public abstract class MPClickableSpan<T> extends ClickableSpan {

    /**
     * 两次的时间间隔
     * 单位：毫秒ms
     */
    private long intervalTime = 1000;

    /**
     * 记录所有绑定该监听器View的最后一次点击时间
     * 解决Activity中多个控件绑定一个Listener时，区分单一控件和多个控件重复点击的问题。
     */
    private SparseArray<Long> lastClickViewArray = new SparseArray<>();

    private T obj;

    public MPClickableSpan(T obj) {
        super();
        this.obj = obj;
    }

    @Override
    public void onClick(@NonNull View v) {
        long currentTime = System.currentTimeMillis();
        // 获取该view最后一次的点击时间,默认为-1
        long lastClickTime = lastClickViewArray.get(v.getId(), -1L);

        if (currentTime - lastClickTime > intervalTime) {
            lastClickViewArray.put(v.getId(), currentTime);
            onSpanClicked(v, obj);
        }

    }


    public abstract void onSpanClicked(View widget, T obj);
}
