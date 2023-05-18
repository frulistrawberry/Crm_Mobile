package com.baihe.lihepro.view.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.ScheduleConstant;
import com.baihe.lihepro.entity.schedule.ScheduleDate;
import com.necer.entity.CalendarDate;
import com.necer.painter.CalendarAdapter;
import com.necer.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.Map;

public class ScheduleAdapter extends CalendarAdapter {

    private Map<LocalDate, ScheduleDate> mData;

    private String[] colors = {"#FF1E5A","#FF9B00","#8ADFFF"};
    private String[] levels = {"A","B","C"};
    int mLevelLimit = 4;


    public ScheduleAdapter(Map<LocalDate, ScheduleDate> mData) {
        this.mData = mData;
    }

    public void setData(Map<LocalDate, ScheduleDate> data,int levelLimit) {
        this.mData = data;
        this.mLevelLimit = levelLimit;
    }

    @Override
    public View getCalendarItemView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.item_calendar,null);
    }

    @Override
    public void onBindToadyView(View calendarItemView, LocalDate localDate, List<LocalDate> totalCheckedDateList) {
        calendarItemView.setVisibility(View.VISIBLE);
        View ll_content= calendarItemView.findViewById(R.id.ll_item);
        TextView tv_item = calendarItemView.findViewById(R.id.tv_item);
        View v_point = calendarItemView.findViewById(R.id.v_point);
        CirCleView cirCleView = calendarItemView.findViewById(R.id.v_circle);
        TextView v_level = calendarItemView.findViewById(R.id.v_level);
        v_point.setVisibility(View.GONE);
        tv_item.setText(String.valueOf(localDate.getDayOfMonth()));
        TextView tv_lunar = calendarItemView.findViewById(R.id.tv_lunar);
        CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
        tv_lunar.setText(calendarDate.lunar.lunarOnDrawStr);
        if (totalCheckedDateList.contains(localDate)) {

            ll_content.setBackgroundResource(R.drawable.calendar_round_blue);
            tv_item.setTextColor(Color.parseColor("#FFFFFF"));
            tv_lunar.setTextColor(Color.parseColor("#FFFFFF"));
            if (mData!=null){
                ScheduleDate date = mData.get(localDate);
                if (null != date){
                    if (date.getStatus() == ScheduleConstant.SCHEDULE_STATUS_AVAILABLE){
                        v_point.setVisibility(View.GONE);
                    }else {
                        v_point.setBackgroundResource(R.drawable.calendar_point_white);
                        v_point.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            tv_item.setTextColor(Color.parseColor("#2DB4E6"));
            tv_lunar.setTextColor(Color.parseColor("#2DB4E6"));
            ll_content.setBackgroundColor(Color.parseColor("#ffffff"));
            if (mData!=null){
                ScheduleDate date = mData.get(localDate);
                if (date != null) {
                    if (date.getStatus() != ScheduleConstant.SCHEDULE_STATUS_AVAILABLE){
                        ll_content.setBackgroundResource(R.drawable.calendar_round_gray);
                        v_point.setBackgroundResource(R.drawable.calendar_point);
                        v_point.setVisibility(View.VISIBLE);
                    }else {
                        v_point.setVisibility(View.GONE);
                        ll_content.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                }

            }
        }
        cirCleView.setVisibility(View.GONE);
        v_level.setVisibility(View.GONE);
        if (mData!=null){
            ScheduleDate date = mData.get(localDate);
            if (date != null) {
                if (mLevelLimit>3){
                    if (date.getLevel()<=3){
                        cirCleView.setColorStr(colors[date.getLevel()-1]);
                        v_level.setText(levels[date.getLevel()-1]);
                        cirCleView.setVisibility(View.VISIBLE);
                        v_level.setVisibility(View.VISIBLE);
                    }
                }else {
                    if (date.getLevel()<mLevelLimit){
                        cirCleView.setColorStr(colors[date.getLevel()-1]);
                        v_level.setText(levels[date.getLevel()-1]);
                        cirCleView.setVisibility(View.VISIBLE);
                        v_level.setVisibility(View.VISIBLE);
                    }
                }
            }
        }




    }

    @Override
    public void onBindCurrentMonthOrWeekView(View calendarItemView, LocalDate localDate, List<LocalDate> totalCheckedDateList) {
        calendarItemView.setVisibility(View.VISIBLE);
        View ll_content= calendarItemView.findViewById(R.id.ll_item);
        TextView tv_item = calendarItemView.findViewById(R.id.tv_item);
        View v_point = calendarItemView.findViewById(R.id.v_point);
        CirCleView cirCleView = calendarItemView.findViewById(R.id.v_circle);
        TextView v_level = calendarItemView.findViewById(R.id.v_level);
        v_point.setVisibility(View.GONE);

        tv_item.setText(String.valueOf(localDate.getDayOfMonth()));
        TextView tv_lunar = calendarItemView.findViewById(R.id.tv_lunar);
        CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
        tv_lunar.setText(calendarDate.lunar.lunarOnDrawStr);
        if (totalCheckedDateList.contains(localDate)) {
            ll_content.setBackgroundResource(R.drawable.calendar_round_blue);
            tv_item.setTextColor(Color.parseColor("#FFFFFF"));
            tv_lunar.setTextColor(Color.parseColor("#FFFFFF"));
            if (mData!=null){
                if (mData.get(localDate)!=null){
                    ScheduleDate date = mData.get(localDate);
                    if (date!=null){
                        if (date.getStatus() == ScheduleConstant.SCHEDULE_STATUS_AVAILABLE){
                            v_point.setVisibility(View.GONE);
                        }else {
                            v_point.setBackgroundResource(R.drawable.calendar_point_white);
                            v_point.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }
        } else {
            if (localDate.isAfter(LocalDate.now())){
                tv_item.setTextColor(Color.parseColor("#4A4C5C"));
                tv_lunar.setTextColor(Color.parseColor("#4A4C5C"));
            }else {
                tv_item.setTextColor(Color.parseColor("#C5C5C5"));
                tv_lunar.setTextColor(Color.parseColor("#C5C5C5"));
            }
            ll_content.setBackgroundColor(Color.parseColor("#ffffff"));
            if (mData!=null){
                ScheduleDate date = mData.get(localDate);
                if (date!=null){
                    if (date.getStatus() != ScheduleConstant.SCHEDULE_STATUS_AVAILABLE){
                        ll_content.setBackgroundResource(R.drawable.calendar_round_gray);
                        v_point.setBackgroundResource(R.drawable.calendar_point);
                        v_point.setVisibility(View.VISIBLE);
                    }else {
                        v_point.setVisibility(View.GONE);
                        ll_content.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                }

            }

        }

        cirCleView.setVisibility(View.GONE);
        v_level.setVisibility(View.GONE);
        if (mData!=null){
            ScheduleDate date = mData.get(localDate);
            if (date != null) {
                if (mLevelLimit>3){
                    if (date.getLevel()<=3){
                        cirCleView.setColorStr(colors[date.getLevel()-1]);
                        v_level.setText(levels[date.getLevel()-1]);
                        cirCleView.setVisibility(View.VISIBLE);
                        v_level.setVisibility(View.VISIBLE);
                    }
                }else {
                    if (date.getLevel()<mLevelLimit){
                        cirCleView.setColorStr(colors[date.getLevel()-1]);
                        v_level.setText(levels[date.getLevel()-1]);
                        cirCleView.setVisibility(View.VISIBLE);
                        v_level.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    @Override
    public void onBindLastOrNextMonthView(View calendarItemView, LocalDate localDate, List<LocalDate> totalCheckedDateList) {
        calendarItemView.setVisibility(View.INVISIBLE);
    }

}
