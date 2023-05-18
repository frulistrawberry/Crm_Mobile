package com.baihe.lihepro.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseFragment;
import com.baihe.common.util.JsonUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.ScheduleConstant;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.SingleScheduleDialog;
import com.baihe.lihepro.entity.schedule.HallItem;
import com.baihe.lihepro.entity.schedule.ScheduleDate;
import com.baihe.lihepro.view.calendar.MultiScheduleCalendar;
import com.baihe.lihepro.view.calendar.MultiScheduleMonthCalendar;
import com.baihe.lihepro.view.calendar.ScheduleAdapter;
import com.baihe.lihepro.view.calendar.SingleScheduleAdapter;
import com.necer.calendar.BaseCalendar;
import com.necer.enumeration.CalendarState;
import com.necer.enumeration.CheckModel;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.painter.CalendarBackground;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleScheduleFragment extends BaseFragment implements OnCalendarChangedListener {
    private MultiScheduleMonthCalendar calendar;
    private LinearLayout ll_week_bar;
    private SingleScheduleAdapter adapter;
    private String selectDate;
    private List<ScheduleDate> mData;
    private String chooseDate;

    private int companyLevel;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_multi_schedule_home;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        listener();
    }

    public void jump(String date){
        String[] temp = date.split("-");
        calendar.jumpMonth(Integer.valueOf(temp[0]),Integer.valueOf(temp[1]));
    }

    private void initView(View view) {
        companyLevel = getArguments().getInt("companyLevel",0);
        chooseDate = getArguments().getString("chooseDate");
        calendar = view.findViewById(R.id.calendar);
        ll_week_bar = view.findViewById(R.id.ll_week_bar);
        calendar.setCheckMode(CheckModel.SINGLE_DEFAULT_UNCHECKED);
        adapter = new SingleScheduleAdapter(null);
        calendar.setCalendarBackground(new CalendarBackground() {
            @Override
            public Drawable getBackgroundDrawable(LocalDate localDate, int currentDistance, int totalDistance) {
                return new ColorDrawable(Color.parseColor("#F6F6F8"));
            }
        });
        calendar.setCalendarAdapter(adapter);
        calendar.setOnCalendarChangedListener(this);
        if (chooseDate!=null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    jump(chooseDate);

                }
            },500);
        }
    }



    private void initData() {


    }

    private void listener() {
    }

    private void getCalendar(String date){
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("date",date);
        HttpRequest.create(UrlConstant.SCHEDULE_GET_CALENDAR_NEW).putParam(jsonParam).get(new CallBack<List<ScheduleDate>>() {
            @Override
            public List<ScheduleDate> doInBackground(String response) {
                return JsonUtils.parseList(response, ScheduleDate.class);
            }

            @Override
            public void success(List<ScheduleDate> entity) {
                if (entity != null) {
                    refreshCalendar(entity);
                }
            }

            @Override
            public void error() {

            }

            @Override
            public void fail() {

            }

            @Override
            public void before() {
                super.before();
            }

            @Override
            public void after() {
                super.after();
            }
        });

    }

    private void refreshCalendar(List<ScheduleDate> dateList){
        this.mData = dateList;
        Map<LocalDate,ScheduleDate> data = new HashMap<>();
        for (ScheduleDate scheduleDate : dateList) {
            data.put(scheduleDate.getLocalDate(),scheduleDate);
        }
        adapter.setData(data,companyLevel);
        calendar.notifyCalendar();
    }

    private void notifyWeekBar(LocalDate localDate){
        for (int i = 0; i < ll_week_bar.getChildCount(); i++) {
            ((TextView)ll_week_bar.getChildAt(i)).setTextColor(Color.parseColor("#8E8E8E"));
        }

        if (localDate!=null){
            int index =  localDate.dayOfWeek().get();
            if (index!=7)
                ((TextView)ll_week_bar.getChildAt(index)).setTextColor(Color.parseColor("#2DB4E6"));
            else
                ((TextView)ll_week_bar.getChildAt(0)).setTextColor(Color.parseColor("#2DB4E6"));

        }

    }


    private void getHallList(){
        for (ScheduleDate mDatum : mData) {
            if (selectDate.equals(mDatum.getDate())){
                if (mDatum.getStatus()!= ScheduleConstant.SCHEDULE_STATUS_AVAILABLE)
                    return;
                if (mDatum.getLocalDate().isBefore(LocalDate.now()))
                    return;
            }
        }

        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("date",selectDate);
        HttpRequest.create(UrlConstant.SCHEDULE_GET_HALL_INFO_BY_DATE).putParam(jsonParam).get(new CallBack<List<HallItem>>() {
            @Override
            public List<HallItem> doInBackground(String response) {
                if ("".equals(response)){
                    List<HallItem> data = new ArrayList<>();
                    return data;
                }

                return JsonUtils.parseList(response, HallItem.class);
            }

            @Override
            public void success(List<HallItem> entity) {
                if (entity!=null && entity.size()>0){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");

                        new SingleScheduleDialog(getActivity(),selectDate,entity,chooseDate!=null).show();

                }
            }

            @Override
            public void error() {

            }

            @Override
            public void fail() {

            }

            @Override
            public void before() {
                super.before();
                showOptionLoading();
            }

            @Override
            public void after() {
                super.after();
                dismissOptionLoading();
            }
        });
    }


    @Override
    public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {

        if (localDate!=null){
            selectDate = localDate.toString("yyyy-MM-dd");
        }
        if (dateChangeBehavior == DateChangeBehavior.PAGE ||
                dateChangeBehavior == DateChangeBehavior.INITIALIZE || dateChangeBehavior == DateChangeBehavior.API){

            getCalendar(year+"-"+month);
        }

        if (dateChangeBehavior == DateChangeBehavior.CLICK){
            if (selectDate!=null){
                getHallList();
            }
        }




        if (dateChangeBehavior == DateChangeBehavior.INITIALIZE){
            notifyWeekBar(LocalDate.now());
        }else if (dateChangeBehavior == DateChangeBehavior.PAGE || dateChangeBehavior == DateChangeBehavior.CLICK) {
            if (LocalDate.now().monthOfYear().get() == month){
                if (localDate!=null)
                    notifyWeekBar(localDate);
                else
                    notifyWeekBar(LocalDate.now());

            }else {
                notifyWeekBar(localDate);
            }
        }else if (dateChangeBehavior == DateChangeBehavior.API){
            notifyWeekBar(localDate);
        }
    }


}
