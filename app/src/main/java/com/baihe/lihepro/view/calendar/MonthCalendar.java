package com.baihe.lihepro.view.calendar;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.necer.calendar.ICalendar;
import com.necer.enumeration.CheckModel;
import com.necer.enumeration.MultipleCountModel;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarMultipleChangedListener;
import com.necer.listener.OnClickDisableDateListener;
import com.necer.painter.CalendarAdapter;
import com.necer.painter.CalendarBackground;
import com.necer.painter.CalendarPainter;
import com.necer.utils.Attrs;

import java.time.LocalDate;
import java.util.List;

public class MonthCalendar extends ViewPager implements ICalendar {

    private List<LocalDate> mData;
    private CheckModel mCheckModel;//选中模式

    public MonthCalendar(@NonNull Context context) {
        super(context);
    }

    public MonthCalendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCheckMode(CheckModel checkModel) {

    }

    @Override
    public CheckModel getCheckModel() {
        return null;
    }

    @Override
    public void setMultipleCount(int multipleCount, MultipleCountModel multipleCountModel) {

    }

    @Override
    public void setDefaultCheckedFirstDate(boolean isDefaultCheckedFirstDate) {

    }

    @Override
    public void jumpDate(String formatDate) {

    }

    @Override
    public void jumpDate(int year, int month, int day) {

    }

    @Override
    public void jumpMonth(int year, int month) {

    }

    @Override
    public void toLastPager() {

    }

    @Override
    public void toNextPager() {

    }

    @Override
    public void toToday() {

    }

    @Override
    public void setCalendarPainter(CalendarPainter calendarPainter) {

    }

    @Override
    public void setCalendarAdapter(CalendarAdapter calendarAdapter) {

    }

    @Override
    public void notifyCalendar() {

    }

    @Override
    public void setInitializeDate(String formatInitializeDate) {

    }

    @Override
    public void setDateInterval(String startFormatDate, String endFormatDate, String formatInitializeDate) {

    }

    @Override
    public void setDateInterval(String startFormatDate, String endFormatDate) {

    }

    @Override
    public void setOnCalendarChangedListener(OnCalendarChangedListener onCalendarChangedListener) {

    }

    @Override
    public void setOnCalendarMultipleChangedListener(OnCalendarMultipleChangedListener onCalendarMultipleChangedListener) {

    }

    @Override
    public void setOnClickDisableDateListener(OnClickDisableDateListener onClickDisableDateListener) {

    }

    @Override
    public Attrs getAttrs() {
        return null;
    }

    @Override
    public CalendarPainter getCalendarPainter() {
        return null;
    }

    @Override
    public CalendarAdapter getCalendarAdapter() {
        return null;
    }

    @Override
    public List<org.joda.time.LocalDate> getTotalCheckedDateList() {
        return null;
    }

    @Override
    public List<org.joda.time.LocalDate> getCurrPagerCheckDateList() {
        return null;
    }

    @Override
    public List<org.joda.time.LocalDate> getCurrPagerDateList() {
        return null;
    }

    @Override
    public void updateSlideDistance(int currentDistance) {

    }

    @Override
    public void setLastNextMonthClickEnable(boolean enable) {

    }

    @Override
    public void setScrollEnable(boolean scrollEnable) {

    }

    @Override
    public void setCalendarBackground(CalendarBackground calendarBackground) throws IllegalAccessException {

    }

    @Override
    public CalendarBackground getCalendarBackground() throws IllegalAccessException {
        return null;
    }

    @Override
    public void setCheckedDates(List<String> dateList) {

    }
}
