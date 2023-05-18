package com.baihe.lihepro.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseFragment;
import com.baihe.common.log.LogUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.ScheduleConstant;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.MultiScheduleDialog;
import com.baihe.lihepro.dialog.SingleScheduleDialog;
import com.baihe.lihepro.entity.schedule.HallItem;
import com.baihe.lihepro.entity.schedule.ScheduleDate;
import com.baihe.lihepro.entity.schedule.calendarnew.Entity;
import com.baihe.lihepro.entity.schedule.calendarnew.HomeHallItem;
import com.baihe.lihepro.entity.schedule.calendarnew.HomeReserveInfo;
import com.baihe.lihepro.view.calendar.MultiScheduleAdapter;
import com.baihe.lihepro.view.calendar.MultiScheduleCalendar;
import com.baihe.lihepro.view.calendar.MultiScheduleMonthCalendar;
import com.baihe.lihepro.view.calendar.SingleScheduleAdapter;
import com.necer.calendar.BaseCalendar;
import com.necer.enumeration.CheckModel;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.helper.CalendarHelper;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.painter.CalendarBackground;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiScheduleFragment extends BaseFragment implements OnCalendarChangedListener, MultiScheduleAdapter.OnCheckListener, MultiScheduleAdapter.OnScrollListener {
    private MultiScheduleMonthCalendar calendar;
    private LinearLayout ll_week_bar;
    private MultiScheduleAdapter adapter;
    private String startDate;
    private String endDate;
    private List<ScheduleDate> mData;

    private int companyLevel;
    private String chooseDate;
    private List<ScheduleDate> currentDate;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_multi_schedule_home;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser){
            if (adapter != null) {
                PopupWindow popupWindow = adapter.getPopupWindow();
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        }
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
        adapter = new MultiScheduleAdapter(null);
        adapter.setOnScrollListener(this);
        adapter.setListener(this);
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
        int max = 0;
        for (ScheduleDate mDatum : mData) {
            //遍历日期获取最大宴会厅数
            if (mDatum.getHallInfo()!=null && mDatum.getHallInfo().size()>max)
                max = mDatum.getHallInfo().size();
        }

        for (ScheduleDate mDatum : mData) {
            //添加空数据
            if (mDatum.getHallInfo() == null){
                List<HomeHallItem> hallItems = new ArrayList<>();
                for (int i = 0; i < max; i++) {
                    hallItems.add(new HomeHallItem(true));
                }
                mDatum.setHallInfo(hallItems);
            }
            if (mDatum.getHallInfo().size()<max){
                for (int i = mDatum.getHallInfo().size(); i <max ; i++) {
                    mDatum.getHallInfo().add(new HomeHallItem(true));
                }
            }
        }

        for (ScheduleDate mDatum : mData) {
            //遍历排序
            List<HomeHallItem> hallItems = mDatum.getHallInfo();
            HomeHallItem[] temp = new HomeHallItem[max];
            for (HomeHallItem hallItem : hallItems) {
                if (hallItem.isEmpty())
                    continue;
                HomeReserveInfo lunch = hallItem.getLunch();
                HomeReserveInfo dinner = hallItem.getDinner();



                if ((lunch!=null && lunch.getIsMulti() == 1) ||
                        (dinner!=null && dinner.getIsMulti() == 1)){
                    //当宴会厅有多档期的情况

                    if (lunch !=null && lunch.getIsMulti() == 1){
                        //午宴有多档期
                        if (!convertDate(lunch.getStart_date()).equals(mDatum.getDate())){
                            //档期开始日期不是这天的日期 需要找到档期开始日期该宴会厅的位置
                            boolean isFind = false;
                            for (ScheduleDate datum : mData) {
                                //遍历查找起始日期是哪天
                                if (datum.getDate().equals(convertDate(lunch.getStart_date()))){
                                    //找到档期开始日期那一天
                                    isFind = true;
                                    List<HomeHallItem> homeHallItems = datum.getHallInfo();
                                    for (HomeHallItem homeHallItem : homeHallItems) {
                                        //遍历查找对应宴会厅的位置
                                        if (homeHallItem!=null && ((homeHallItem.getLunch()!=null && lunch.getReserve_num().equals(homeHallItem.getLunch().getReserve_num()))||
                                                (homeHallItem.getDinner()!=null && lunch.getReserve_num().equals(homeHallItem.getDinner().getReserve_num())))){
                                            //找到档期开始那一天该宴会厅的位置
                                            int index = homeHallItems.indexOf(homeHallItem);
                                            if (temp[index] == null ){
                                                temp[index] = hallItem;
                                            }else {
                                                HomeHallItem hallItemTemp = temp[index];
                                                temp[index] = hallItem;
                                                for (int i = 0; i < temp.length; i++) {
                                                    if (temp[i] == null){
                                                        temp[i] = hallItemTemp;
                                                        break;
                                                    }
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            if (!isFind){
                                for (int i = 0; i < temp.length; i++) {
                                    if (temp[i] == null){
                                        temp[i] = hallItem;
                                        break;
                                    }
                                }
                            }
                        }else {
                            for (int i = 0; i < temp.length; i++) {
                                if (temp[i] == null){
                                    temp[i] = hallItem;
                                    break;
                                }
                            }
                        }
                    }
                    else if (dinner !=null && dinner.getIsMulti() == 1){
                        //晚宴有多档期
                        if (!convertDate(dinner.getStart_date()).equals(mDatum.getDate())){
                            //档期开始日期不是这天的日期 需要找到档期开始日期该宴会厅的位置
                            boolean isFind = false;
                            for (ScheduleDate datum : mData) {
                                //遍历查找起始日期是哪天
                                if (datum.getDate().equals(convertDate(dinner.getStart_date()))){
                                    //找到档期开始日期那一天
                                    isFind = true;
                                    List<HomeHallItem> homeHallItems = datum.getHallInfo();
                                    for (HomeHallItem homeHallItem : homeHallItems) {
                                        //遍历查找宴会厅位置
                                        if (homeHallItem!=null && (homeHallItem.getDinner()!=null && dinner.getReserve_num().equals(homeHallItem.getDinner().getReserve_num())||
                                                (homeHallItem.getLunch()!=null && dinner.getReserve_num().equals(homeHallItem.getLunch().getReserve_num())))){
                                            //找到档期开始那一天该宴会厅的位置
                                            int index = homeHallItems.indexOf(homeHallItem);
                                            if (temp[index] == null ){
                                                temp[index] = hallItem;
                                            }else {
                                                HomeHallItem hallItemTemp = temp[index];
                                                temp[index] = hallItem;
                                                for (int i = 0; i < temp.length; i++) {
                                                    if (temp[i] == null){
                                                        temp[i] = hallItemTemp;
                                                        break;
                                                    }
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    break;
                                }

                            }
                            if (!isFind){
                                for (int i = 0; i < temp.length; i++) {
                                    if (temp[i] == null){
                                        temp[i] = hallItem;
                                        break;
                                    }
                                }
                            }
                        }else {
                            for (int i = 0; i < temp.length; i++) {
                                if (temp[i] == null){
                                    temp[i] = hallItem;
                                    break;
                                }
                            }
                        }

                    }

                }
            }

            for (HomeHallItem hallItem : hallItems) {
                if (hallItem == null)
                    continue;
                if (hallItem.getDinner() != null && hallItem.getDinner().getIsMulti() == 0 && hallItem.getLunch()!=null && hallItem.getLunch().getIsMulti() == 0){
                    for (int i = 0; i < temp.length; i++) {
                        if (temp[i] == null){
                            temp[i] = hallItem;
                            break;
                        }
                    }

                }

                if (hallItem.getDinner() != null && hallItem.getDinner().getIsMulti() == 0 && hallItem.getLunch()==null ){
                    for (int i = 0; i < temp.length; i++) {
                        if (temp[i] == null){
                            temp[i] = hallItem;
                            break;
                        }
                    }

                }

                if (hallItem.getDinner() == null  &&  hallItem.getLunch()!=null && hallItem.getLunch().getIsMulti() == 0){
                    for (int i = 0; i < temp.length; i++) {
                        if (temp[i] == null){
                            temp[i] = hallItem;
                            break;
                        }
                    }

                }
            }

//            for (HomeHallItem hallItem : hallItems){
//                for (HomeHallItem homeHallItem : temp) {
//                    if (homeHallItem.isEmpty())
//                        continue;
//                    if (hallItem.equals(homeHallItem))
//                        hallItems.remove(hallItem);
//                }
//            }


//            for (HomeHallItem hallItem : hallItems) {
//                if (hallItem.isEmpty())
//                    continue;
//                if ((hallItem.getDinner()!=null && hallItem.getDinner().getIsMulti() == 1) || (hallItem.getLunch()!=null && hallItem.getLunch().getIsMulti() == 1)){
//                    for (HomeHallItem homeHallItem : temp) {
//                        if (homeHallItem.isEmpty()){
//                            homeHallItem.setEmpty(false);
//                            homeHallItem.setDinner(hallItem.getDinner());
//                            homeHallItem.setHallName(hallItem.getHallName());
//                            homeHallItem.setLunch(hallItem.getLunch());
//                            hallItems.remove(hallItem);
//                            break;
//                        }
//
//                    }
//                }
//            }
//            for (HomeHallItem hallItem : hallItems){
//                if (hallItem.isEmpty())
//                    continue;
//                for (HomeHallItem homeHallItem : temp) {
//                    if (homeHallItem.isEmpty()){
//                        homeHallItem.setEmpty(false);
//                        homeHallItem.setDinner(hallItem.getDinner());
//                        homeHallItem.setHallName(hallItem.getHallName());
//                        homeHallItem.setLunch(hallItem.getLunch());
//                        break;
//                    }
//                }
//            }
            mDatum.setHallInfo(Arrays.asList(temp));

        }

        Map<LocalDate,ScheduleDate> data = new HashMap<>();
        for (ScheduleDate scheduleDate : dateList) {
            data.put(scheduleDate.getLocalDate(),scheduleDate);
        }
        adapter.setData(data,companyLevel);
        calendar.notifyCalendar();
    }

    private String convertDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf1.format(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
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

        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("start",startDate).putParamValue("end",endDate);
        HttpRequest.create(UrlConstant.CALENDAR_TIME_SLOT).putParam(jsonParam).get(new CallBack<Entity>() {
            @Override
            public Entity doInBackground(String response) {
                if ("".equals(response)){
                    Entity data = new Entity();
                    return data;
                }

                return JsonUtils.parse(response, Entity.class);
            }

            @Override
            public void success(Entity entity) {

                if (entity!=null && entity.getHallinfo()!=null && entity.getHallinfo().size()>0 && entity.isOk()){
                    new MultiScheduleDialog(getActivity(),startDate,endDate,entity.getHallinfo(),MultiScheduleFragment.this,chooseDate!=null).show();
                }else {
                    ToastUtils.toast("该时段没有可用的档期或宴会厅");
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


        if (dateChangeBehavior == DateChangeBehavior.PAGE ||
                dateChangeBehavior == DateChangeBehavior.INITIALIZE || dateChangeBehavior == DateChangeBehavior.API){

            getCalendar(year+"-"+month);
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


    @Override
    public void onCheck(BaseCalendar calendar, MultiScheduleCalendar scheduleCalendar, LocalDate localDate, CalendarHelper helper) {
        List<LocalDate> checkList = calendar.getTotalCheckedDateList();
        LocalDate start = checkList.get(0);
        LocalDate end = checkList.get(checkList.size()-1);
        startDate = start.toString("yyyy-MM-dd");
        endDate = end.toString("yyyy-MM-dd");
        getHallList();

    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onScroll(RecyclerView recyclerView, LocalDate localDate, int dy) {
        MultiScheduleCalendar currentPage = calendar.getCurrentPage();
        List<LocalDate> currentWeekData = new ArrayList<>();
        Date date1 = localDate.toDate();
        if (mData != null) {
            for (ScheduleDate mDatum : mData) {
                Date date2 = mDatum.getLocalDate().toDate();
                Calendar calendar1 = Calendar.getInstance();
                Calendar calendar2 = Calendar.getInstance();
                calendar1.setTime(date1);
                calendar2.setTime(date2);
                if (calendar1.get(Calendar.WEEK_OF_MONTH)  == calendar2.get(Calendar.WEEK_OF_MONTH)){
                    currentWeekData.add(mDatum.getLocalDate());
                }
            }
            MultiScheduleCalendar.GridViewAdapter adapter = (MultiScheduleCalendar.GridViewAdapter) currentPage.getAdapter();
            Map<LocalDate,View> itemViews = null;
            if (adapter != null) {
                itemViews  = adapter.getItemViews();
            }

            for (LocalDate currentWeekDatum : currentWeekData) {
                    if (itemViews != null) {
                        View itemView = itemViews.get(currentWeekDatum);
                        if (itemView != null) {
                            RecyclerView hallList = itemView.findViewById(R.id.recyclerView);
                            if (hallList.equals(recyclerView))
                                continue;
                            if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE){
                                LogUtils.d("ScrollBy",dy+" "+ hallList.getTag());
                                hallList.scrollBy(0,dy);
                            }


                        }
                    }


            }
        }









    }
}
