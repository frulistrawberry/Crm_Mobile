package com.baihe.lihepro.view.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.log.LogUtils;
import com.necer.calendar.BaseCalendar;
import com.necer.enumeration.CalendarType;
import com.necer.helper.CalendarHelper;
import com.necer.painter.CalendarAdapter;
import com.necer.painter.CalendarBackground;
import com.necer.utils.CalendarUtil;
import com.necer.utils.DrawableUtil;
import com.necer.view.ICalendarView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MultiScheduleCalendar extends RecyclerView implements ICalendarView {
    private final CalendarHelper mCalendarHelper;
    private final List<LocalDate> mDateList;
    private CalendarAdapter mCalendarAdapter;
    private final GridViewAdapter mAdapter;
    private BaseCalendar mCalendar;
    private int mCurrentDistance = -1;
    private int mLastX;
    private int mLastY;

    public MultiScheduleCalendar(@NonNull Context context, BaseCalendar calendar, LocalDate initialDate, CalendarType calendarType) {
        super(context);
        mCalendar = calendar;
        mCalendarHelper = new CalendarHelper(calendar, initialDate, calendarType);
        mCalendarAdapter = mCalendarHelper.getCalendarAdapter();
        mDateList = mCalendarHelper.getDateList();
        setLayoutManager(new GridLayoutManager(context,7));
        mAdapter = new GridViewAdapter();
        setAdapter(mAdapter);

    }



    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        CalendarBackground calendarBackground = mCalendarHelper.getCalendarBackground();
        drawBackground(canvas, calendarBackground);
    }

    //绘制背景
    private void drawBackground(Canvas canvas, CalendarBackground calendarBackground) {
        int currentDistance = mCurrentDistance == -1 ? mCalendarHelper.getInitialDistance() : mCurrentDistance;
        Drawable backgroundDrawable = calendarBackground.getBackgroundDrawable(mCalendarHelper.getMiddleLocalDate(), currentDistance, mCalendarHelper.getCalendarHeight());
        Rect backgroundRectF = mCalendarHelper.getBackgroundRectF();
        backgroundDrawable.setBounds(DrawableUtil.getDrawableBounds(backgroundRectF.centerX(), backgroundRectF.centerY(), backgroundDrawable));
        backgroundDrawable.draw(canvas);
    }




    @Override
    public LocalDate getPagerInitialDate() {
        return mCalendarHelper.getPagerInitialDate();
    }

    @Override
    public LocalDate getMiddleLocalDate() {
        return mCalendarHelper.getMiddleLocalDate();
    }

    @Override
    public int getDistanceFromTop(LocalDate localDate) {
        return mCalendarHelper.getDistanceFromTop(localDate);
    }

    @Override
    public LocalDate getPivotDate() {
        return mCalendarHelper.getPivotDate();
    }

    @Override
    public int getPivotDistanceFromTop() {
        return mCalendarHelper.getPivotDistanceFromTop();
    }

    @Override
    public List<LocalDate> getCurrPagerCheckDateList() {
        return mCalendarHelper.getCurrentSelectDateList();
    }

    @Override
    public void updateSlideDistance(int currentDistance) {
        this.mCurrentDistance = currentDistance;
        invalidate();
    }

    @Override
    public List<LocalDate> getCurrPagerDateList() {
        return mCalendarHelper.getCurrentDateList();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void notifyCalendarView() {
       mAdapter.notifyDataSetChanged();
    }

    //周或者月的第一天
    @Override
    public LocalDate getCurrPagerFirstDate() {
        return mCalendarHelper.getCurrPagerFirstDate();
    }

    @Override
    public CalendarType getCalendarType() {
        return mCalendarHelper.getCalendarType();
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {

        return false;
    }

    public  class GridViewAdapter extends RecyclerView.Adapter<Holder>{

        public Map<LocalDate, View> getItemViews() {
            return itemViews;
        }

        private final Map<LocalDate,View> itemViews;

        public GridViewAdapter() {
            itemViews = new LinkedHashMap<>();
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (mCalendarAdapter == null)
                return null;
            if (mCalendarAdapter instanceof CalendarAdapter2){
                View itemView = LayoutInflater.from(getContext()).inflate(((CalendarAdapter2) mCalendarAdapter).getItemLayoutId(),parent,false);
                Holder holder = new Holder(itemView);
//                holder.setIsRecyclable(false);
                return holder;

            }else {
                return null;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            if (mAdapter!=null){
                LocalDate localDate = mDateList.get(position);
                itemViews.put(localDate,holder.itemView);
                holder.bindView(position);

            }
        }


        @Override
        public int getItemCount() {
            return mCalendarAdapter==null?0:mDateList.size();
        }


    }
    public class Holder extends RecyclerView.ViewHolder{

        public Holder(@NonNull View itemView) {
            super(itemView);
        }


        public void bindView(int position){
            LocalDate localDate = mDateList.get(position);

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCalendarAdapter instanceof CalendarAdapter2){
                        ((CalendarAdapter2) mCalendarAdapter).onClick(mCalendar,MultiScheduleCalendar.this,localDate,mCalendarHelper,itemView);
                    }

                }
            });
            if (mCalendarHelper.isAvailableDate(localDate)) {
                if (mCalendarHelper.isCurrentMonthOrWeek(localDate)) {  //当月日期
                    if (CalendarUtil.isToday(localDate)) {  //当天
                        mCalendarAdapter.onBindToadyView(itemView, localDate, mCalendarHelper.getAllSelectListDate());
                    } else { //不是当天的当月其他日期
                        mCalendarAdapter.onBindCurrentMonthOrWeekView(itemView, localDate, mCalendarHelper.getAllSelectListDate());
                    }
                } else {  //不是当月的日期
                    mCalendarAdapter.onBindLastOrNextMonthView(itemView, localDate, mCalendarHelper.getAllSelectListDate());
                }
            } else { //日期区间之外的日期
                mCalendarAdapter.onBindDisableDateView(itemView, localDate);
            }

        }


    }


}
