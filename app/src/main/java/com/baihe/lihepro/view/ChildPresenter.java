package com.baihe.lihepro.view;

import android.content.Context;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.log.LogUtils;

public class ChildPresenter extends RecyclerView {

    private float lastY;
    private boolean canScroll = true;
    public ChildPresenter(@NonNull Context context) {
        super(context);
    }

    public ChildPresenter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_DOWN){
                    lastY = ev.getY();
                }
                float y = ev.getY();
                LogUtils.d("Schedule","y="+y);
                boolean canScrollDown = canScrollVertically(-1);
                boolean canScrollTop = canScrollVertically(1);
                canScroll =(canScrollDown && y>lastY) || (canScrollTop && y<=lastY);
                LogUtils.d("Schedule","canScrollDown="+canScrollDown+" canScrollTop="+canScrollTop+" scrollDirection="+(y>lastY?"向下":"向上"));
                LogUtils.d("Schedule","intercept="+canScroll);
//                if (intercept)
//                    getParent().getParent().requestDisallowInterceptTouchEvent(true);
                LogUtils.d("Schedule","lastY="+lastY);


                return false;
            }
        });
    }




    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtils.d("Schedule","action="+ev.getAction());
//        if (ev.getAction() == MotionEvent.ACTION_DOWN){
//            lastY = ev.getY();
//        }
//        float y = ev.getY();
//        LogUtils.d("Schedule","y="+y);
//        boolean canScrollDown = canScrollVertically(-1);
//        boolean canScrollTop = canScrollVertically(1);
//        boolean intercept =(canScrollDown && y>lastY) || (canScrollTop && y<=lastY);
//        LogUtils.d("Schedule","canScrollDown="+canScrollDown+" canScrollTop="+canScrollTop+" scrollDirection="+(y>lastY?"向下":"向上"));
//        LogUtils.d("Schedule","intercept="+intercept);
//        if (intercept)
//            getParent().getParent().requestDisallowInterceptTouchEvent(true);
//        LogUtils.d("Schedule","lastY="+lastY);

        getParent().getParent().requestDisallowInterceptTouchEvent(canScroll);




        return super.dispatchTouchEvent(ev);
    }
}
