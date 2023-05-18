package com.baihe.lihepro.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.JsonUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.ScheduleHallAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.DateDialogUtils;
import com.baihe.lihepro.entity.schedule.HallItem;
import com.baihe.lihepro.entity.schedule.ScheduleDate;
import com.baihe.lihepro.view.LiheTimePickerBuilder;
import com.baihe.lihepro.view.calendar.ScheduleAdapter;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.SPUtils;
import com.necer.calendar.BaseCalendar;
import com.necer.calendar.Miui10Calendar;
import com.necer.enumeration.CalendarState;
import com.necer.enumeration.CheckModel;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.listener.OnCalendarStateChangedListener;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleHomeActivity extends BaseActivity implements OnCalendarChangedListener, OnCalendarStateChangedListener, View.OnClickListener, OnTimeSelectListener, ScheduleHallAdapter.OnItemClickListener {

    private TextView tv_date;
    private ImageView iv_arrow;
    private ImageView iv_halls;
    private ImageView iv_my;
    private Miui10Calendar calendar;
    private View v_flag;
    private LinearLayout ll_available_halls;
    private LinearLayout ll_unavailable_halls;
    private RecyclerView rv_available_halls;
    private RecyclerView rv_unavailable_halls;
    private LinearLayout ll_empty_halls;
    private TimePickerView timePickerView;
    private LinearLayout ll_week_bar;

    private ScheduleAdapter adapter;
    private ScheduleHallAdapter availableAdapter;
    private ScheduleHallAdapter unavailableAdapter;
    private String selectDate;
    private int companyLevel;
    private Toolbar contract_list_title_tb;
    private List<HallItem> halls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_schedule_home_title);
        setContentView(R.layout.activity_schedule_home);
        companyLevel = SPUtils.getInstance().getInt("companyLevel");
        findView();
        initView();
    }

    private void initView() {

        contract_list_title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        availableAdapter = new ScheduleHallAdapter(this);
        unavailableAdapter = new ScheduleHallAdapter(this);
        availableAdapter.setOnItemClickListener(this);
        unavailableAdapter.setOnItemClickListener(this);
        rv_available_halls.setLayoutManager(new LinearLayoutManager(this));
        rv_unavailable_halls.setLayoutManager(new LinearLayoutManager(this));
        rv_available_halls.setAdapter(availableAdapter);
        rv_unavailable_halls.setAdapter(unavailableAdapter);

        calendar.setCheckMode(CheckModel.SINGLE_DEFAULT_UNCHECKED);
        adapter = new ScheduleAdapter(null);
        calendar.setCalendarAdapter(adapter);
        calendar.notifyCalendar();
        calendar.setOnCalendarChangedListener(this);
        calendar.setOnCalendarStateChangedListener(this);
        tv_date.setOnClickListener(this);
        iv_halls.setOnClickListener(this);
        iv_my.setOnClickListener(this);
        LiheTimePickerBuilder pickerBuilder = DateDialogUtils.createPickerViewBuilder(this,this);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 2);
        pickerBuilder.setRangDate(Calendar.getInstance(),calendar);
        pickerBuilder.addOnCancelClickListener(view -> iv_arrow.setImageResource(R.drawable.schedule_arrow_down));
        timePickerView = pickerBuilder.build();
        timePickerView.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                iv_arrow.setImageResource(R.drawable.schedule_arrow_down);
            }
        });


    }



    private void getCalendar(String date){
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("date",date);
        HttpRequest.create(UrlConstant.SCHEDULE_GET_CALENDAR).putParam(jsonParam).get(new CallBack<List<ScheduleDate>>() {
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
                showOptionLoading();
            }

            @Override
            public void after() {
                super.after();
                dismissOptionLoading();
            }
        });

    }
    private void getHallList(final boolean isInit,String date){
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("date",date);
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
                refreshHallList(entity,isInit);
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

    private void refreshCalendar(List<ScheduleDate> dateList){
        Map<LocalDate,ScheduleDate> data = new HashMap<>();
        for (ScheduleDate scheduleDate : dateList) {
            data.put(scheduleDate.getLocalDate(),scheduleDate);
        }
        adapter.setData(data,companyLevel);
        calendar.notifyCalendar();
    }

    private void refreshHallList(List<HallItem> hallList,boolean isInit){
        if (isInit){
            initHallsDialog(hallList);
            ll_empty_halls.setVisibility(View.GONE);
            ll_available_halls.setVisibility(View.GONE);
            ll_unavailable_halls.setVisibility(View.GONE);
        }else {


            if (new LocalDate(selectDate).isBefore(LocalDate.now())){
                ll_empty_halls.setVisibility(View.GONE);
                ll_available_halls.setVisibility(View.GONE);
                ll_unavailable_halls.setVisibility(View.GONE);
                return;
            }
            this.halls = hallList;
            if (hallList == null || hallList.size() == 0){
                ll_empty_halls.setVisibility(View.VISIBLE);
                ll_available_halls.setVisibility(View.GONE);
                ll_unavailable_halls.setVisibility(View.GONE);
//                ToastUtils.toast("这天没有档期和可用的宴会厅哦～");
            }else {
                ll_empty_halls.setVisibility(View.GONE);
                List<HallItem> availableHalls = new ArrayList<>();
                List<HallItem> unavailableHalls = new ArrayList<>();
                for (HallItem hallItem : hallList) {
                    if (hallItem.getLunchStatus() == 0 || hallItem.getDinnerStatus() == 0){
                        availableHalls.add(hallItem);
                    }else {
                        unavailableHalls.add(hallItem);
                    }
                }
                if (availableHalls.size()==0){
                    ll_available_halls.setVisibility(View.GONE);
                }else {
                    ll_available_halls.setVisibility(View.VISIBLE);
                    availableAdapter.setData(availableHalls);
                }

                if (unavailableHalls.size()==0){
                    ll_unavailable_halls.setVisibility(View.GONE);
                }else {
                    ll_unavailable_halls.setVisibility(View.VISIBLE);
                    unavailableAdapter.setData(unavailableHalls);
                }
            }
        }
    }

    private void initHallsDialog(List<HallItem> hallList) {
        halls = hallList;
    }

    private void findView() {
        tv_date = findViewById(R.id.tv_date);
        contract_list_title_tb = findViewById(R.id.contract_list_title_tb);
        iv_arrow = findViewById(R.id.iv_arrow);
        iv_halls = findViewById(R.id.iv_halls);
        iv_my = findViewById(R.id.iv_my);
        calendar = findViewById(R.id.calendar);
        v_flag = findViewById(R.id.v_flag);
        ll_available_halls = findViewById(R.id.ll_available_halls);
        ll_unavailable_halls = findViewById(R.id.ll_unavailable_halls);
        rv_available_halls = findViewById(R.id.rv_available_halls);
        rv_unavailable_halls = findViewById(R.id.rv_unavailable_halls);
        ll_week_bar = findViewById(R.id.ll_week_bar);
        ll_empty_halls = findViewById(R.id.ll_empty_halls);

    }

    @Override
    public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {

        if (localDate!=null){
            selectDate = localDate.toString("yyyy-MM-dd");
        }
        if (dateChangeBehavior == DateChangeBehavior.PAGE ||
                dateChangeBehavior == DateChangeBehavior.INITIALIZE || dateChangeBehavior == DateChangeBehavior.API){
            String date = String.format("%d年%02d月",year,month);
            tv_date.setText(date);
            getCalendar(year+"-"+month);
        }

        if (dateChangeBehavior == DateChangeBehavior.INITIALIZE){
            getHallList(true,LocalDate.now().toString("yyyy-MM-dd"));
        }else if (dateChangeBehavior == DateChangeBehavior.API || dateChangeBehavior == DateChangeBehavior.CLICK){
            if (localDate != null) {
                getHallList(false,localDate.toString("yyyy-MM-dd"));
                if (calendar.getCalendarState() == CalendarState.MONTH){
                    calendar.toWeek();
                }

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

    @Override
    public void onCalendarStateChange(CalendarState calendarState) {
        if (calendarState == CalendarState.MONTH){
            v_flag.setVisibility(View.GONE);
        }else {
            v_flag.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_date:
                iv_arrow.setImageResource(R.drawable.schedule_arrow_up);
                timePickerView.show();
                break;
            case R.id.iv_halls:
                HallListActivity.start(this,halls);
                break;
            case R.id.iv_my:
                MyScheduleListActivity.start(this);
                break;
        }
    }

    @Override
    public void onTimeSelect(Date date, View v) {
        iv_arrow.setImageResource(R.drawable.schedule_arrow_down);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time = format.format(date);
        calendar.jumpDate(time);
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

    @Override
    public void onItemClick(View v, HallItem item, int position) {
        ScheduleHallActivity.start(this,String.valueOf(item.getId()),selectDate,item.getName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK){

            getCalendar(selectDate);
            getHallList(false,selectDate);
        }
        if (requestCode == 105 && resultCode == RESULT_OK){
            HallItem hallItem = (HallItem) data.getSerializableExtra("hall");
            ScheduleHallActivity.start(this,hallItem.getId()+"",selectDate,hallItem.getName());
        }
    }
}
