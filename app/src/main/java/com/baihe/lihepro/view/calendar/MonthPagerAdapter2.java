package com.baihe.lihepro.view.calendar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.necer.adapter.MonthPagerAdapter;
import com.necer.calendar.BaseCalendar;
import com.necer.enumeration.CalendarType;
import com.necer.view.ICalendarView;

import org.joda.time.LocalDate;

public class MonthPagerAdapter2 extends MonthPagerAdapter {

    private Context context;
    public MonthPagerAdapter2(Context context, BaseCalendar baseCalendar) {
        super(context, baseCalendar);
        this.context = context;

    }

    @Override
    protected CalendarType getCalendarType() {
        return CalendarType.MONTH;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ICalendarView iCalendarView;
        LocalDate pageInitializeDate = getPageInitializeDate(position);
        iCalendarView = new MultiScheduleCalendar(context, getCalendar(), pageInitializeDate, getCalendarType());
        ((View) iCalendarView).setTag(position);
        container.addView((View) iCalendarView);
        return iCalendarView;
    }
}
