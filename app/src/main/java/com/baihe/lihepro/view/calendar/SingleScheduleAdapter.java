package com.baihe.lihepro.view.calendar;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseApplication;
import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.ScheduleConstant;
import com.baihe.lihepro.entity.schedule.HallItem;
import com.baihe.lihepro.entity.schedule.ScheduleDate;
import com.baihe.lihepro.entity.schedule.calendarnew.HomeHallItem;
import com.blankj.utilcode.util.ScreenUtils;
import com.huawei.secure.android.common.util.ScreenUtil;
import com.necer.calendar.BaseCalendar;
import com.necer.entity.CalendarDate;
import com.necer.enumeration.CalendarType;
import com.necer.helper.CalendarHelper;
import com.necer.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SingleScheduleAdapter extends CalendarAdapter2{
    private Map<LocalDate, ScheduleDate> mData;

    private String[] colors = {"#FF1E5A","#FF9B00","#8ADFFF"};
    private String[] levels = {"A","B","C"};
    int mLevelLimit = 4;
    private String hallNameColor;
    private String saleNameColor;

    public SingleScheduleAdapter(Map<LocalDate, ScheduleDate> mData) {
        this.mData = mData;
    }

    public void setData(Map<LocalDate, ScheduleDate> data,int levelLimit) {
        this.mData = data;
        this.mLevelLimit = levelLimit;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_calendar_new;
    }

    @Override
    public boolean isClickAble(LocalDate localDate) {
        ScheduleDate date = mData.get(localDate);
        if (date != null) {
            return date.getStatus() == ScheduleConstant.SCHEDULE_STATUS_AVAILABLE && !localDate.isBefore(LocalDate.now());

        }
        return false;
    }

    @Override
    public void onClick(BaseCalendar calendar,MultiScheduleCalendar scheduleCalendar, LocalDate localDate, CalendarHelper helper,View itemView) {
        super.onClick(calendar,scheduleCalendar,localDate,helper,itemView);
        if (isClickAble(localDate)){
            if (scheduleCalendar.getCalendarType() == CalendarType.MONTH && CalendarUtil.isLastMonth(localDate, helper.getPagerInitialDate())) {
                calendar.onClickLastMonthDate(localDate);
            } else if (scheduleCalendar.getCalendarType() == CalendarType.MONTH && CalendarUtil.isNextMonth(localDate, helper.getPagerInitialDate())) {
                calendar.onClickNextMonthDate(localDate);
            } else {
                calendar.onClickCurrentMonthOrWeekDate(localDate);
            }
        }

    }

    @Override
    public View getCalendarItemView(Context context) {
        return null;
    }


    @Override
    public void onBindToadyView(View calendarItemView, LocalDate localDate, List<LocalDate> totalCheckedDateList) {
        calendarItemView.setVisibility(View.VISIBLE);
        View ll_content= calendarItemView.findViewById(R.id.ll_item);
        TextView tv_item = calendarItemView.findViewById(R.id.tv_item);
        View v_point = calendarItemView.findViewById(R.id.v_point);
        CirCleView cirCleView = calendarItemView.findViewById(R.id.v_circle);
        TextView v_level = calendarItemView.findViewById(R.id.v_level);
        TextView tv_lunar = calendarItemView.findViewById(R.id.tv_lunar);
        RecyclerView hallList = calendarItemView.findViewById(R.id.recyclerView);
        RelativeLayout rl_day = calendarItemView.findViewById(R.id.rl_day);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rl_day.getLayoutParams();
        params.height = (ScreenUtils.getScreenWidth() - CommonUtils.dp2pxForInt(BaseApplication.app,28))/7;
        params.width = (ScreenUtils.getScreenWidth() - CommonUtils.dp2pxForInt(BaseApplication.app,28))/7;

        v_point.setVisibility(View.GONE);
        tv_item.setText(String.valueOf(localDate.getDayOfMonth()));


        CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
        tv_lunar.setText(calendarDate.lunar.lunarOnDrawStr);
        hallNameColor = "#CC4A4C5C";
        saleNameColor = "#AA4A4C5C";
        if (totalCheckedDateList.contains(localDate)) {

            ll_content.setBackgroundResource(R.drawable.calendar_round_blue);
            hallList.setBackgroundResource(R.drawable.bg_multi_calendar_list_white);
            tv_item.setTextColor(Color.parseColor("#FFFFFF"));
            tv_lunar.setTextColor(Color.parseColor("#FFFFFF"));
            if (mData!=null){
                ScheduleDate date = mData.get(localDate);
                if (null != date){
                    if (date.getStatus() == ScheduleConstant.SCHEDULE_STATUS_AVAILABLE){
                        v_point.setVisibility(View.GONE);
                    }else {
                        v_point.setBackgroundResource(R.drawable.calendar_point_white);
                        hallList.setBackgroundResource(R.drawable.bg_multi_calendar_list_gray);
                        hallNameColor = "#884A4C5C";
                        saleNameColor = "#884A4C5C";
                        v_point.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            tv_item.setTextColor(Color.parseColor("#2DB4E6"));
            tv_lunar.setTextColor(Color.parseColor("#2DB4E6"));
            ll_content.setBackgroundResource(R.drawable.bg_multi_calendar_white);
            hallList.setBackgroundResource(R.drawable.bg_multi_calendar_list_white);
            if (mData!=null){
                ScheduleDate date = mData.get(localDate);
                if (date != null) {
                    if (date.getStatus() != ScheduleConstant.SCHEDULE_STATUS_AVAILABLE){
                        ll_content.setBackgroundResource(R.drawable.bg_multi_calendar_gray);
                        hallList.setBackgroundResource(R.drawable.bg_multi_calendar_list_gray);
                        hallNameColor = "#884A4C5C";
                        saleNameColor = "#884A4C5C";
                        v_point.setBackgroundResource(R.drawable.calendar_point);
                        v_point.setVisibility(View.VISIBLE);
                    }else {
                        v_point.setVisibility(View.GONE);
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

                List<HomeHallItem> hallItems = date.getHallInfo();
                hallList.setLayoutManager(new LinearLayoutManager(calendarItemView.getContext()));
                if (hallItems != null) {
                    CalendarHallAdapter adapter = new CalendarHallAdapter(calendarItemView.getContext(), hallItems,hallNameColor,saleNameColor);
                    hallList.setAdapter(adapter);
                }else {
                    hallItems = new ArrayList<>();
                    CalendarHallAdapter adapter = new CalendarHallAdapter(calendarItemView.getContext(), hallItems,hallNameColor,saleNameColor);
                    hallList.setAdapter(adapter);
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

        RecyclerView hallList = calendarItemView.findViewById(R.id.recyclerView);
        RelativeLayout rl_day = calendarItemView.findViewById(R.id.rl_day);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rl_day.getLayoutParams();
        params.height = (ScreenUtils.getScreenWidth() - CommonUtils.dp2pxForInt(BaseApplication.app,28))/7;
        params.width = (ScreenUtils.getScreenWidth() - CommonUtils.dp2pxForInt(BaseApplication.app,28))/7;

        v_point.setVisibility(View.GONE);

        tv_item.setText(String.valueOf(localDate.getDayOfMonth()));
        TextView tv_lunar = calendarItemView.findViewById(R.id.tv_lunar);
        CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
        tv_lunar.setText(calendarDate.lunar.lunarOnDrawStr);
        hallNameColor = "#CC4A4C5C";
        saleNameColor = "#AA4A4C5C";
        if (totalCheckedDateList.contains(localDate)) {
            ll_content.setBackgroundResource(R.drawable.calendar_round_blue);
            tv_item.setTextColor(Color.parseColor("#FFFFFF"));
            tv_lunar.setTextColor(Color.parseColor("#FFFFFF"));
            if (localDate.isAfter(LocalDate.now())){
                hallList.setBackgroundResource(R.drawable.bg_multi_calendar_list_white);
            }else {
                hallList.setBackgroundResource(R.drawable.bg_multi_calendar_list_gray);
                hallNameColor = "#884A4C5C";
                saleNameColor = "#884A4C5C";
            }
            if (mData!=null){
                if (mData.get(localDate)!=null){
                    ScheduleDate date = mData.get(localDate);
                    if (date!=null){
                        if (date.getStatus() == ScheduleConstant.SCHEDULE_STATUS_AVAILABLE){
                            v_point.setVisibility(View.GONE);
                        }else {
                            v_point.setBackgroundResource(R.drawable.calendar_point_white);
                            hallList.setBackgroundResource(R.drawable.bg_multi_calendar_gray);
                            hallNameColor = "#884A4C5C";
                            saleNameColor = "#884A4C5C";
                            v_point.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }
        } else {
            if (localDate.isAfter(LocalDate.now())){
                tv_item.setTextColor(Color.parseColor("#4A4C5C"));
                tv_lunar.setTextColor(Color.parseColor("#4A4C5C"));
                hallList.setBackgroundResource(R.drawable.bg_multi_calendar_list_white);
            }else {
                tv_item.setTextColor(Color.parseColor("#C5C5C5"));
                tv_lunar.setTextColor(Color.parseColor("#C5C5C5"));
                hallList.setBackgroundResource(R.drawable.bg_multi_calendar_list_gray);
                hallNameColor = "#884A4C5C";
                saleNameColor = "#884A4C5C";
            }
            ll_content.setBackgroundResource(R.drawable.bg_multi_calendar_white);
            if (mData!=null){
                ScheduleDate date = mData.get(localDate);
                if (date!=null){
                    if (date.getStatus() != ScheduleConstant.SCHEDULE_STATUS_AVAILABLE){
                        ll_content.setBackgroundResource(R.drawable.bg_multi_calendar_gray);
                        hallList.setBackgroundResource(R.drawable.bg_multi_calendar_list_gray);
                        v_point.setBackgroundResource(R.drawable.calendar_point);
                        v_point.setVisibility(View.VISIBLE);
                        hallNameColor = "#884A4C5C";
                        saleNameColor = "#884A4C5C";
                    }else {
                        v_point.setVisibility(View.GONE);
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

                List<HomeHallItem> hallItems = date.getHallInfo();
                hallList.setLayoutManager(new LinearLayoutManager(calendarItemView.getContext()));
                if (hallItems != null) {
                    CalendarHallAdapter adapter = new CalendarHallAdapter(calendarItemView.getContext(), hallItems,hallNameColor,saleNameColor);
                    hallList.setAdapter(adapter);
                }else {
                    hallItems = new ArrayList<>();
                    CalendarHallAdapter adapter = new CalendarHallAdapter(calendarItemView.getContext(), hallItems,hallNameColor,saleNameColor);
                    hallList.setAdapter(adapter);
                }
            }
        }
    }

    @Override
    public void onBindLastOrNextMonthView(View calendarItemView, LocalDate localDate, List<LocalDate> totalCheckedDateList) {
        calendarItemView.setVisibility(View.INVISIBLE);
    }

}
