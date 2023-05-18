package com.baihe.lihepro.view.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.baihe.common.log.LogUtils;
import com.necer.adapter.BasePagerAdapter;
import com.necer.calendar.BaseCalendar;
import com.necer.calendar.MonthCalendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MultiScheduleMonthCalendar extends MonthCalendar {

    public MultiScheduleMonthCalendar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        setOffscreenPageLimit(1);
    }

    @Override
    protected BasePagerAdapter getPagerAdapter(Context context, BaseCalendar baseCalendar) {
        return new MonthPagerAdapter2(context,baseCalendar);
    }

    @Override
    public void notifyCalendar() {
        super.notifyCalendar();
    }


    public MultiScheduleCalendar getCurrentPage(){
        return (MultiScheduleCalendar) getCurChild_vp(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;
    }

    private   View getCurChild_vp(ViewPager vp) {

        int childCnt = vp.getChildCount();
        int totalCnt = vp.getAdapter().getCount();
        int curItem = vp.getCurrentItem();

        int targetIndex = 0;

        // 若"已加载child未达到应有值",则在边界 、或总数达不到limit
        if (childCnt < vp.getOffscreenPageLimit() * 2 + 1) {
            // 若-项数不足-加载所有至limit,直接返回当前
            if (childCnt == totalCnt)
                targetIndex = curItem;
            else
            // 若足
            {
                // 若在左边界(即左边child数未达到limit)
                if (curItem - vp.getOffscreenPageLimit() < 0)
                    targetIndex = curItem;
                    // 右边界
                else
                    targetIndex = vp.getOffscreenPageLimit();
            }
        }
        // childCnt完整(即总项>childCnt,且不在边界)
        else
            targetIndex = vp.getOffscreenPageLimit();

        // 取-子元素
        List<View> vs = new ArrayList<View>();
        for (int i = 0; i < childCnt; i++)
            vs.add(vp.getChildAt(i));

        // 对子元素-排序,因默认排序-不一定正确(viewpager内部机制)
        Collections.sort(vs, new Comparator<View>() {
            @Override
            public int compare(View lhs, View rhs) {
                // TODO Auto-generated method stub
                if (lhs.getLeft() > rhs.getLeft())
                    return 1;
                else if (lhs.getLeft() < rhs.getLeft())
                    return -1;
                else
                    return 0;
            }
        });

        // debug
        // for (int i = 0; i<childCnt; i++)
        // System.out.println("nimei>>vp-"+i+".x:"+vs.get(i).getLeft());
        // System.out.println("nimei>>index:"+targetIndex);

        return vs.get(targetIndex);
    }

}
