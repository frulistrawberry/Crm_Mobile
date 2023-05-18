package com.baihe.lihepro.view.calendar;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseApplication;
import com.baihe.common.util.CommonUtils;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.ScheduleConstant;
import com.baihe.lihepro.entity.schedule.ScheduleDate;
import com.baihe.lihepro.entity.schedule.calendarnew.HomeHallItem;
import com.blankj.utilcode.util.ScreenUtils;
import com.necer.calendar.BaseCalendar;
import com.necer.entity.CalendarDate;
import com.necer.enumeration.CalendarType;
import com.necer.helper.CalendarHelper;
import com.necer.utils.CalendarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MultiScheduleAdapter extends CalendarAdapter2{
    private Map<LocalDate, ScheduleDate> mData;

    private String[] colors = {"#FF1E5A","#FF9B00","#8ADFFF"};
    private String[] levels = {"A","B","C"};
    private int sunday = 7;
    private int saturday = 6;
    int mLevelLimit = 4;
    private TextView tv_item;
    private TextView tv_lunar;
    private RecyclerView hallList;
    private String hallNameColor;
    private String saleNameColor;
    private LocalDate localDate;
    private PopupWindow popupWindow;

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    private OnCheckListener listener;

    private OnScrollListener onScrollListener;

    public interface OnScrollListener{
        void onScroll(RecyclerView recyclerView, LocalDate localDate,int dy);
    }


//    private boolean checkSameWeek(LocalDate first,LocalDate second){
//        if (first.getYear() == second.getYear()){
//            return first.get(WeekFields.of(DayOfWeek.SUNDAY,1)) == second.get(WeekFields.of(DayOfWeek.SUNDAY,1));
//        }
//    }

    public void setListener(OnCheckListener listener) {
        this.listener = listener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public MultiScheduleAdapter(Map<LocalDate, ScheduleDate> mData) {
        this.mData = mData;
    }

    public void setData(Map<LocalDate, ScheduleDate> data,int levelLimit) {
        this.mData = data;
        this.mLevelLimit = levelLimit;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_calendar_multi;
    }

    @Override
    public boolean isClickAble(LocalDate localDate) {
        ScheduleDate date = mData.get(localDate);
        if (date != null) {
            return date.getStatus() != ScheduleConstant.SCHEDULE_STATUS_AVAILABLE;
        }
        return false;
    }

    public interface OnCheckListener {
        void onCheck(BaseCalendar calendar, MultiScheduleCalendar scheduleCalendar, LocalDate localDate, CalendarHelper helper);
    }

    @Override
    public void onClick(BaseCalendar calendar, MultiScheduleCalendar scheduleCalendar, LocalDate localDate, CalendarHelper helper,View itemView) {
        super.onClick(calendar, scheduleCalendar, localDate, helper,itemView);
        List<LocalDate> checkedDateList = calendar.getTotalCheckedDateList();
        if (localDate.isBefore(LocalDate.now()))
            return;
        if (checkedDateList.size() == 0){
            TextView textView = new TextView(calendar.getContext());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            textView.setTextColor(Color.WHITE);
            if (localDate.getDayOfWeek() == sunday)
            textView.setBackgroundResource(R.drawable.bg_text_left);
            else if (localDate.getDayOfWeek() == saturday)
                textView.setBackgroundResource(R.drawable.bg_text_right);
            else
                textView.setBackgroundResource(R.drawable.bg_text);

            textView.setText("请选择档期结束日期");
            textView.setGravity(Gravity.CENTER);

            popupWindow = new PopupWindow(textView, CommonUtils.dp2pxForInt(BaseApplication.app,150),ViewGroup.LayoutParams.WRAP_CONTENT);
//            popupWindow = new PopupWindow(textView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int popupWidth = textView.getMeasuredWidth();
            int popupHeight =  textView.getMeasuredHeight();
            int[] location = new int[2];


            itemView.getLocationOnScreen(location);
            popupWindow.showAtLocation(itemView, Gravity.NO_GRAVITY, (location[0]+itemView.getWidth()/2)-popupWidth/2,
                    location[1]-popupHeight);

            if (scheduleCalendar.getCalendarType() == CalendarType.MONTH && CalendarUtil.isLastMonth(localDate, helper.getPagerInitialDate())) {
                calendar.onClickLastMonthDate(localDate);
            } else if (scheduleCalendar.getCalendarType() == CalendarType.MONTH && CalendarUtil.isNextMonth(localDate, helper.getPagerInitialDate())) {
                calendar.onClickNextMonthDate(localDate);
            } else {
                calendar.onClickCurrentMonthOrWeekDate(localDate);
            }
        }else if (checkedDateList.size() == 1){
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
            LocalDate startDate = checkedDateList.get(0);

//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (startDate.isBefore(localDate)){
                List<LocalDate> dateList = new ArrayList<>();
                dateList.add(startDate);
                for (LocalDate date : helper.getCurrentDateList()) {
                    if (date.isAfter(startDate) && date.isBefore(localDate))
                        dateList.add(date);
                }
                dateList.add(localDate);
                calendar.getTotalCheckedDateList().clear();
                calendar.getTotalCheckedDateList().addAll(dateList);
                calendar.notifyCalendar();
            }else {

                List<LocalDate> dateList = new ArrayList<>();
                dateList.add(localDate);
                for (LocalDate date : helper.getCurrentDateList()) {
                    if (date.isAfter(localDate) && date.isBefore(startDate))
                        dateList.add(date);
                }
                dateList.add(startDate);
                calendar.getTotalCheckedDateList().clear();
                calendar.getTotalCheckedDateList().addAll(dateList);
                calendar.notifyCalendar();
            }
            if (listener!=null){
                listener.onCheck(calendar,scheduleCalendar,localDate,helper);
            }
        }else {
            checkedDateList.clear();
            if (scheduleCalendar.getCalendarType() == CalendarType.MONTH && CalendarUtil.isLastMonth(localDate, helper.getPagerInitialDate())) {
                calendar.onClickLastMonthDate(localDate);
            } else if (scheduleCalendar.getCalendarType() == CalendarType.MONTH && CalendarUtil.isNextMonth(localDate, helper.getPagerInitialDate())) {
                calendar.onClickNextMonthDate(localDate);
            } else {
                calendar.onClickCurrentMonthOrWeekDate(localDate);
            }

            TextView textView = new TextView(calendar.getContext());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            textView.setTextColor(Color.WHITE);
            if (localDate.getDayOfWeek() == sunday)
                textView.setBackgroundResource(R.drawable.bg_text_left);
            else if (localDate.getDayOfWeek() == saturday)
                textView.setBackgroundResource(R.drawable.bg_text_right);
            else
                textView.setBackgroundResource(R.drawable.bg_text);
            textView.setText("请选择档期结束日期");
            textView.setGravity(Gravity.CENTER);
            popupWindow = new PopupWindow(textView,  CommonUtils.dp2pxForInt(BaseApplication.app,150),ViewGroup.LayoutParams.WRAP_CONTENT);
//            popupWindow = new PopupWindow(textView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


            textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int popupWidth = textView.getMeasuredWidth();
            int popupHeight =  textView.getMeasuredHeight();
            int[] location = new int[2];


            itemView.getLocationOnScreen(location);
            popupWindow.showAtLocation(itemView, Gravity.NO_GRAVITY, (location[0]+itemView.getWidth()/2)-popupWidth/2,
                    location[1]-popupHeight);
        }

    }

    @Override
    public View getCalendarItemView(Context context) {
        return null;
    }

    private void  BindCommonView(View calendarItemView,LocalDate localDate, List<LocalDate> totalCheckedDateList){
        this.localDate = localDate;
        calendarItemView.setVisibility(View.VISIBLE);
        View ll_content= calendarItemView.findViewById(R.id.ll_item);
        tv_item = calendarItemView.findViewById(R.id.tv_item);
        View v_point = calendarItemView.findViewById(R.id.v_point);
        CirCleView cirCleView = calendarItemView.findViewById(R.id.v_circle);
        TextView v_level = calendarItemView.findViewById(R.id.v_level);
        tv_lunar = calendarItemView.findViewById(R.id.tv_lunar);
        hallList = calendarItemView.findViewById(R.id.recyclerView);
        View halfStartView = calendarItemView.findViewById(R.id.v_bg_left);
        View halfEndView = calendarItemView.findViewById(R.id.v_bg_right);
        View bgView = calendarItemView.findViewById(R.id.v_bg);
        ImageView startDate = calendarItemView.findViewById(R.id.start_date_iv);
        ImageView endDate = calendarItemView.findViewById(R.id.end_date_iv);

        RelativeLayout rl_day = calendarItemView.findViewById(R.id.rl_day);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_day.getLayoutParams();
        params.height = (ScreenUtils.getScreenWidth() - CommonUtils.dp2pxForInt(BaseApplication.app,28))/7;
        params.width = (ScreenUtils.getScreenWidth() - CommonUtils.dp2pxForInt(BaseApplication.app,28))/7;

        RelativeLayout rl_parent = calendarItemView.findViewById(R.id.rl_parent);
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) rl_parent.getLayoutParams();
        params1.height = (ScreenUtils.getScreenWidth() - CommonUtils.dp2pxForInt(BaseApplication.app,28))/7;

        v_point.setVisibility(View.GONE);
        tv_item.setText(String.valueOf(localDate.getDayOfMonth()));


        CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
        tv_lunar.setText(calendarDate.lunar.lunarOnDrawStr);

        cirCleView.setVisibility(View.GONE);
        v_level.setVisibility(View.GONE);

        v_point.setBackgroundResource(R.drawable.calendar_point);

        startDate.setVisibility(View.GONE);
        endDate.setVisibility(View.GONE);


        if (mData!=null){
            ScheduleDate date = mData.get(localDate);
            if (null != date){
                if (date.getStatus() == ScheduleConstant.SCHEDULE_STATUS_AVAILABLE){
                    v_point.setVisibility(View.GONE);
                }else {
                    v_point.setBackgroundResource(R.drawable.calendar_point_white);
                    v_point.setVisibility(View.VISIBLE);
                    hallNameColor = "#884A4C5C";
                    saleNameColor = "#884A4C5C";
                }
            }
        }

        if (totalCheckedDateList.contains(localDate)){
            // TODO: 选中状态
            if (totalCheckedDateList.indexOf(localDate) == 0){
                // TODO: 起始日期
                startDate.setVisibility(View.VISIBLE);
                endDate.setVisibility(View.GONE);
                tv_item.setTextColor(Color.parseColor("#FFFFFF"));
                tv_lunar.setTextColor(Color.parseColor("#FFFFFF"));
                bgView.setVisibility(View.GONE);
                ll_content.setBackgroundResource(R.drawable.calendar_round_blue);
                if (totalCheckedDateList.size()==1)
                    halfStartView.setVisibility(View.GONE);
                else
                    halfStartView.setVisibility(View.VISIBLE);
                halfEndView.setVisibility(View.GONE);
                if (mData!=null){
                    ScheduleDate date = mData.get(localDate);
                    if (null != date){
                        if (date.getStatus() == ScheduleConstant.SCHEDULE_STATUS_AVAILABLE){
                            v_point.setVisibility(View.GONE);
                        }else {
                            v_point.setBackgroundResource(R.drawable.calendar_point_white);
                            v_point.setVisibility(View.VISIBLE);
                            hallNameColor = "#884A4C5C";
                            saleNameColor = "#884A4C5C";
                        }
                    }
                }

            }else if (totalCheckedDateList.size()>1&&totalCheckedDateList.indexOf(localDate) == totalCheckedDateList.size()-1){
                // TODO: 结束日期
                startDate.setVisibility(View.GONE);
                endDate.setVisibility(View.VISIBLE);
                tv_item.setTextColor(Color.parseColor("#FFFFFF"));
                tv_lunar.setTextColor(Color.parseColor("#FFFFFF"));
                bgView.setVisibility(View.GONE);
                ll_content.setBackgroundResource(R.drawable.calendar_round_blue);
                halfStartView.setVisibility(View.GONE);
                halfEndView.setVisibility(View.VISIBLE);
                if (mData!=null){
                    ScheduleDate date = mData.get(localDate);
                    if (null != date){
                        if (date.getStatus() == ScheduleConstant.SCHEDULE_STATUS_AVAILABLE){
                            v_point.setVisibility(View.GONE);
                        }else {
                            v_point.setBackgroundResource(R.drawable.calendar_point_white);
                            v_point.setVisibility(View.VISIBLE);
                            hallNameColor = "#884A4C5C";
                            saleNameColor = "#884A4C5C";
                        }
                    }
                }

            }else {
                // TODO: 中间日期
                tv_item.setTextColor(Color.parseColor("#2DB4E6"));
                tv_lunar.setTextColor(Color.parseColor("#2DB4E6"));
                ll_content.setBackgroundColor(Color.parseColor("#00ffffff"));
                halfStartView.setVisibility(View.GONE);
                halfEndView.setVisibility(View.GONE);
                bgView.setVisibility(View.VISIBLE);
                if (mData!=null){
                    ScheduleDate date = mData.get(localDate);
                    if (null != date){
                        if (date.getStatus() == ScheduleConstant.SCHEDULE_STATUS_AVAILABLE){
                            v_point.setVisibility(View.GONE);
                        }else {
                            v_point.setBackgroundResource(R.drawable.calendar_point);
                            v_point.setVisibility(View.VISIBLE);
                            hallNameColor = "#884A4C5C";
                            saleNameColor = "#884A4C5C";
                        }
                    }
                }
            }
        }else {
            // TODO: 非选中状态
            ll_content.setBackgroundResource(R.drawable.bg_multi_calendar_white);
            if (mData!=null){
                ScheduleDate date = mData.get(localDate);
                if (date != null) {
                    if (date.getStatus() != ScheduleConstant.SCHEDULE_STATUS_AVAILABLE){
                        ll_content.setBackgroundResource(R.drawable.bg_multi_calendar_gray);
                        v_point.setBackgroundResource(R.drawable.calendar_point);
                        v_point.setVisibility(View.VISIBLE);
                        hallNameColor = "#884A4C5C";
                        saleNameColor = "#884A4C5C";
                    }else {
                        v_point.setVisibility(View.GONE);
                    }
                }

            }
            halfStartView.setVisibility(View.GONE);
            halfEndView.setVisibility(View.GONE);
            bgView.setVisibility(View.GONE);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(LocalDate.now().toDate());
        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (localDate.getDayOfWeek() == sunday || localDate.getDayOfMonth() == 1 ){
            hallList.setBackgroundResource(R.drawable.bg_multi_calendar_left_white);
        }else if (localDate.getDayOfWeek() == saturday || localDate.getDayOfMonth() == max){
            hallList.setBackgroundResource(R.drawable.bg_multi_calendar_right_white);
        }else {
            hallList.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if (mData!=null){
            ScheduleDate date = mData.get(localDate);
            if (date != null) {
                //档期级别
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
                //档期预定信息
                List<HomeHallItem> hallItems = date.getHallInfo();
                hallList.setLayoutManager(new LinearLayoutManager(calendarItemView.getContext()));




                if (hallItems != null) {
                    MultiCalendarHallAdapter adapter = new MultiCalendarHallAdapter(calendarItemView.getContext(),
                            hallItems,localDate.toString("yyyy年MM月dd日"),hallNameColor,saleNameColor);
                    hallList.setAdapter(adapter);
                }else {
                    hallItems = new ArrayList<>();
                    MultiCalendarHallAdapter adapter = new MultiCalendarHallAdapter(calendarItemView.getContext(),
                            hallItems,localDate.toString("yyyy年MM月dd日"),hallNameColor,saleNameColor);
                    hallList.setAdapter(adapter);
                }

                hallList.setTag(mData.get(localDate).getDate());

                hallList.clearOnScrollListeners();

                hallList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (onScrollListener != null) {
                            onScrollListener.onScroll(recyclerView,localDate,dy);
                        }

                    }
                });


            }
        }
    }



    @Override
    public void onBindToadyView(View calendarItemView, LocalDate localDate, List<LocalDate> totalCheckedDateList) {
        hallNameColor = "#CC4A4C5C";
        saleNameColor = "#AA4A4C5C";
        BindCommonView(calendarItemView,localDate,totalCheckedDateList);
        if (!totalCheckedDateList.contains(localDate)){
            tv_item.setTextColor(Color.parseColor("#2DB4E6"));
            tv_lunar.setTextColor(Color.parseColor("#2DB4E6"));
        }
    }

    @Override
    public void onBindCurrentMonthOrWeekView(View calendarItemView, LocalDate localDate, List<LocalDate> totalCheckedDateList) {
        hallNameColor = "#CC4A4C5C";
        saleNameColor = "#AA4A4C5C";
        if (localDate.isBefore(LocalDate.now())){
            hallNameColor = "#884A4C5C";
            saleNameColor = "#884A4C5C"; 
        }
        BindCommonView(calendarItemView,localDate,totalCheckedDateList);
        if (totalCheckedDateList.size()>0 && (totalCheckedDateList.indexOf(localDate)== 0 || totalCheckedDateList.indexOf(localDate) == totalCheckedDateList.size()-1))
            return;
        if (localDate.isAfter(LocalDate.now())){
            tv_item.setTextColor(Color.parseColor("#4A4C5C"));
            tv_lunar.setTextColor(Color.parseColor("#4A4C5C"));

        }else {
            tv_item.setTextColor(Color.parseColor("#C5C5C5"));
            tv_lunar.setTextColor(Color.parseColor("#C5C5C5"));

//            if (localDate.getDayOfWeek() == sunday ){
//                hallList.setBackgroundResource(R.drawable.bg_multi_calendar_left_gray);
//            }else if (localDate.getDayOfWeek() == monday){
//                hallList.setBackgroundResource(R.drawable.bg_multi_calendar_right_gray);
//            }else {
//                hallList.setBackgroundColor(Color.parseColor("#D3D4DE"));
//            }
        }
    }

    @Override
    public void onBindLastOrNextMonthView(View calendarItemView, LocalDate localDate, List<LocalDate> totalCheckedDateList) {
        calendarItemView.setVisibility(View.INVISIBLE);
    }


}
