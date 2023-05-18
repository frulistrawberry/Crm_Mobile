package com.baihe.lihepro.view.calendar;

import android.view.View;

import com.necer.calendar.BaseCalendar;
import com.necer.helper.CalendarHelper;
import com.necer.painter.CalendarAdapter;

import org.joda.time.LocalDate;

import java.util.List;


public abstract class CalendarAdapter2 extends CalendarAdapter {

    public abstract int getItemLayoutId();

    public abstract boolean isClickAble(LocalDate localDate);

    public void onClick(BaseCalendar calendar,MultiScheduleCalendar scheduleCalendar, LocalDate localDate, CalendarHelper helper,View itemView){

    }

}
