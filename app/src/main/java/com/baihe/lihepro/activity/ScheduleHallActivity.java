package com.baihe.lihepro.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.JsonUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.RecommendDatesAdapter;
import com.baihe.lihepro.adapter.ScheduleBookActiveAdapter;
import com.baihe.lihepro.adapter.ScheduleHallAdapter;
import com.baihe.lihepro.constant.ScheduleConstant;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.DateDialogUtils;
import com.baihe.lihepro.entity.ButtonTypeEntity;
import com.baihe.lihepro.entity.schedule.BookActive;
import com.baihe.lihepro.entity.schedule.HallBookStatus;
import com.baihe.lihepro.entity.schedule.HallItem;
import com.baihe.lihepro.entity.schedule.ScheduleDate;
import com.baihe.lihepro.entity.schedule.ScheduleHallInfo;
import com.baihe.lihepro.view.LiheTimePickerBuilder;
import com.baihe.lihepro.view.calendar.ScheduleAdapter;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
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

public class ScheduleHallActivity extends BaseActivity implements OnCalendarChangedListener, OnCalendarStateChangedListener, View.OnClickListener, OnTimeSelectListener, ScheduleHallAdapter.OnItemClickListener {

    private TextView tv_date;
    private ImageView iv_arrow;
    private ImageView iv_halls;
    private ImageView iv_my;
    private Miui10Calendar calendar;
    private View v_flag;
    private LinearLayout ll_available_dates;
    private LinearLayout ll_available_halls;
    private RecyclerView rv_available_dates;
    private RecyclerView rv_available_halls;
    private TimePickerView timePickerView;
    private LinearLayout ll_week_bar;


    private LinearLayout ll_schedule_status;
    private LinearLayout ll_lunch_status;
    private LinearLayout ll_dinner_status;
    private LinearLayout ll_book_active;
    private RecyclerView rv_book_active;

    private LinearLayout ll_recommend;

    private TextView tv_lunch_status;
    private TextView tv_dinner_status;

    private LinearLayout bottom_ll;
    private TextView btn_book;
    private TextView btn_reserve;
    private TextView tv_title;
    private RelativeLayout rl_flag;
    private RelativeLayout rl_flag1;
    private NestedScrollView scrollView;

    private ScheduleAdapter adapter;
    private ScheduleHallAdapter availableAdapter;
    private RecommendDatesAdapter datesAdapter;
    private ScheduleBookActiveAdapter activeAdapter;
    private Toolbar contract_list_title_tb;
    private LinearLayout ll_empty_halls;

    private int[] textColors = {Color.WHITE,Color.parseColor("#E8920F"),Color.parseColor("#E97264")};
    private int[] backgrounds = {R.drawable.schedule_staus_available_bg,R.drawable.schedule_staus_reserved_bg,R.drawable.schedule_staus_booked_bg};
    private String[] contents = {"空闲","已预留","已预订"};

    private String hallId;
    private String initDate;
    private String selectedDate;
    private String hallName;
    private HallBookStatus hallBookStatus;
    private List<HallItem> hallItems;

    public static void start(Activity context, String hallId, String initDate, String hallName){
        Intent intent = new Intent(context, ScheduleHallActivity.class);
        intent.putExtra("hallId",hallId);
        intent.putExtra("initDate",initDate);
        intent.putExtra("hallName",hallName);
        context.startActivityForResult(intent,101);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(R.layout.activity_schedule_hall_title);
        setContentView(R.layout.activity_schedule_hall);
        hallId = getIntent().getStringExtra("hallId");
        initDate = getIntent().getStringExtra("initDate");
        hallName = getIntent().getStringExtra("hallName");
        findView();
        initView();
    }

    private void findView() {
        tv_date = findViewById(R.id.tv_date);
        iv_arrow = findViewById(R.id.iv_arrow);
        iv_halls = findViewById(R.id.iv_halls);
        iv_my = findViewById(R.id.iv_my);
        calendar = findViewById(R.id.calendar);
        v_flag = findViewById(R.id.v_flag);
        ll_schedule_status = findViewById(R.id.ll_schedule_status);
        tv_lunch_status = findViewById(R.id.tv_lunch_status);
        tv_dinner_status = findViewById(R.id.tv_dinner_status);
        ll_lunch_status = findViewById(R.id.ll_lunch_status);
        ll_dinner_status = findViewById(R.id.ll_dinner_status);
        ll_book_active = findViewById(R.id.ll_book_active);
        ll_recommend = findViewById(R.id.ll_recommend);
        rv_book_active = findViewById(R.id.rv_book_active);
        bottom_ll = findViewById(R.id.bottom_ll);
        btn_book = findViewById(R.id.btn_book);
        btn_reserve = findViewById(R.id.btn_reserve);
        ll_available_halls = findViewById(R.id.ll_available_halls);
        ll_available_dates = findViewById(R.id.ll_available_dates);
        rv_available_halls = findViewById(R.id.rv_available_halls);
        rv_available_dates = findViewById(R.id.rv_available_dates);
        contract_list_title_tb = findViewById(R.id.tb_title);
        ll_week_bar = findViewById(R.id.ll_week_bar);
        tv_title = findViewById(R.id.tv_title);
        rl_flag = findViewById(R.id.rl_flag);
        rl_flag1 = findViewById(R.id.rl_flag1);
        scrollView = findViewById(R.id.scrollView);
        ll_empty_halls = findViewById(R.id.ll_empty_halls);
    }

    private void initView() {
        tv_title.setText(hallName);

        contract_list_title_tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        availableAdapter = new ScheduleHallAdapter(this);

        availableAdapter.setOnItemClickListener(this);

        rv_available_halls.setLayoutManager(new LinearLayoutManager(this));

        rv_available_halls.setAdapter(availableAdapter);

        LinearLayoutManager recDatesManager = new LinearLayoutManager(this);
        recDatesManager.setOrientation(RecyclerView.HORIZONTAL);
        rv_available_dates.setLayoutManager(recDatesManager);
        datesAdapter = new RecommendDatesAdapter(this);
        datesAdapter.setListener(new RecommendDatesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, String item, int position) {
                calendar.jumpDate(item);

            }
        });
        rv_available_dates.setAdapter(datesAdapter);


        rv_book_active.setLayoutManager(new LinearLayoutManager(this));
        activeAdapter = new ScheduleBookActiveAdapter(this);
        rv_book_active.setAdapter(activeAdapter);

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
        pickerBuilder.addOnCancelClickListener(view -> iv_arrow.setImageResource(R.drawable.schedule_arrow_down));
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.YEAR, calendar1.get(Calendar.YEAR) + 2);
        pickerBuilder.setRangDate(Calendar.getInstance(),calendar1);
        timePickerView = pickerBuilder.build();

        timePickerView.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                iv_arrow.setImageResource(R.drawable.schedule_arrow_down);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                calendar.jumpDate(initDate);
            }
        },500);

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                rl_flag.setVisibility(scrollY >= rl_flag1.getTop()?View.VISIBLE:View.GONE);
            }
        });

    }

    private void getCalendar(String date,String hallId){
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("date",date)
                .putParamValue("hallId",hallId);
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

    private void getHallList(String date){
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("date",date);
        HttpRequest.create(UrlConstant.SCHEDULE_GET_HALL_INFO_BY_DATE).putParam(jsonParam).get(new CallBack<List<HallItem>>() {
            @Override
            public List<HallItem> doInBackground(String response) {
                return JsonUtils.parseList(response, HallItem.class);
            }

            @Override
            public void success(List<HallItem> entity) {
                initHallsDialog(entity);
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

    private void getHallInfo(String date,String hallId){
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("date",date)
                .putParamValue("hallId",hallId);
        HttpRequest.create(UrlConstant.SCHEDULE_GET_HALL_RESERVE_INFO).putParam(jsonParam).get(new CallBack<ScheduleHallInfo>() {
            @Override
            public ScheduleHallInfo doInBackground(String response) {
                return JsonUtils.parse(response, ScheduleHallInfo.class);
            }

            @Override
            public void success(ScheduleHallInfo entity) {
                refreshHallInfo(entity);
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
        adapter.setData(data,4);
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

    private void initHallsDialog(List<HallItem> hallList) {
        hallItems = hallList;
    }

    private void refreshHallInfo(ScheduleHallInfo hallInfo){
        HallItem thisHallInfo = hallInfo.getThisHallInfo();
        if (thisHallInfo!=null){
            hallName = thisHallInfo.getName();
            setTitleText(hallName);
            hallBookStatus = new HallBookStatus(hallInfo.getThisHallInfo().getLunchStatus(),hallInfo.getThisHallInfo().getDinnerStatus());
            if (hallInfo.getThisHallInfo().getDinnerStatus() == ScheduleConstant.SCHEDULE_STATUS_NOT_SET && hallInfo.getThisHallInfo().getLunchStatus() == ScheduleConstant.SCHEDULE_STATUS_NOT_SET){
                ll_schedule_status.setVisibility(View.GONE);
            } else {
                ll_schedule_status.setVisibility(View.VISIBLE);
                if (hallInfo.getThisHallInfo().getLunchStatus() == ScheduleConstant.SCHEDULE_STATUS_NOT_SET){
                    ll_lunch_status.setVisibility(View.GONE);
                }else {
                    ll_lunch_status.setVisibility(View.VISIBLE);
                    tv_lunch_status.setTextColor(textColors[hallInfo.getThisHallInfo().getLunchStatus()]);
                    tv_lunch_status.setBackgroundResource(backgrounds[hallInfo.getThisHallInfo().getLunchStatus()]);
                    tv_lunch_status.setText(contents[hallInfo.getThisHallInfo().getLunchStatus()]);
                }

                if (hallInfo.getThisHallInfo().getDinnerStatus() == ScheduleConstant.SCHEDULE_STATUS_NOT_SET){
                    ll_dinner_status.setVisibility(View.GONE);
                }else {
                    ll_dinner_status.setVisibility(View.VISIBLE);
                    tv_dinner_status.setTextColor(textColors[hallInfo.getThisHallInfo().getDinnerStatus()]);
                    tv_dinner_status.setBackgroundResource(backgrounds[hallInfo.getThisHallInfo().getDinnerStatus()]);
                    tv_dinner_status.setText(contents[hallInfo.getThisHallInfo().getDinnerStatus()]);
                }
            }
        }else {
            ll_schedule_status.setVisibility(View.GONE);
        }


        List<BookActive> dynamics = hallInfo.getDynamics();
        if (dynamics == null || dynamics.size()==0){
            dynamics = new ArrayList<>();
            BookActive bookActive = new BookActive();
            bookActive.setContent("暂无预订记录");
            dynamics.add(bookActive);
        }
        ll_book_active.setVisibility(View.VISIBLE);
        activeAdapter.setData(dynamics);

        if (thisHallInfo==null){
            ll_recommend.setVisibility(View.GONE);
            bottom_ll.setVisibility(View.GONE);
            ll_empty_halls.setVisibility(View.VISIBLE);
            ll_book_active.setVisibility(View.GONE);
//            ToastUtils.toast("这天没有档期和可用的宴会厅哦～");
            hallItems = null;
            return;
        }

        if (hallItems == null)
            getHallList(LocalDate.now().toString("yyyy-MM-dd"));

        ll_empty_halls.setVisibility(View.GONE);



        if ( thisHallInfo.getLunchStatus()!=0 && thisHallInfo.getDinnerStatus()!=0){
            if (hallInfo.getRecommend()!=null){
                ll_recommend.setVisibility(View.VISIBLE);
                if (hallInfo.getRecommend().getDate()!=null && hallInfo.getRecommend().getDate().size()>0){
                    ll_available_dates.setVisibility(View.VISIBLE);
                    datesAdapter.setData(hallInfo.getRecommend().getDate());
                }else {
                    ll_available_dates.setVisibility(View.GONE);
                }

                if (hallInfo.getRecommend().getHalls()!=null && hallInfo.getRecommend().getHalls().size()>0){
                    ll_available_halls.setVisibility(View.VISIBLE);
                    availableAdapter.setData(hallInfo.getRecommend().getHalls());
                }else {
                    ll_available_halls.setVisibility(View.GONE);
                }

            }else {
                ll_recommend.setVisibility(View.GONE);
            }

            bottom_ll.setVisibility(View.GONE);
        }else {
            ll_recommend.setVisibility(View.GONE);
            List<ButtonTypeEntity> buttonType = hallInfo.getButtonType();
            if (buttonType == null || buttonType.size()==0 ){
                bottom_ll.setVisibility(View.GONE);
            }else {
                bottom_ll.setVisibility(View.VISIBLE);
                if (buttonType.size() == 1){
                    btn_book.setVisibility(View.VISIBLE);
                    btn_reserve.setVisibility(View.GONE);
                    btn_book.setText(buttonType.get(0).getName());
                    btn_book.setOnClickListener(view -> bottomButtonClick(buttonType.get(0).getType()));

                }else if (buttonType.size() == 2){
                    btn_reserve.setVisibility(View.VISIBLE);
                    btn_book.setVisibility(View.VISIBLE);
                    btn_reserve.setText(buttonType.get(0).getName());
                    btn_book.setText(buttonType.get(1).getName());
                    btn_reserve.setOnClickListener(view -> bottomButtonClick(buttonType.get(0).getType()));
                    btn_book.setOnClickListener(view -> bottomButtonClick(buttonType.get(1).getType()));
                }
            }
        }

        if (new LocalDate(selectedDate).isBefore(LocalDate.now())){
            ll_empty_halls.setVisibility(View.GONE);
            ll_available_halls.setVisibility(View.GONE);
            bottom_ll.setVisibility(View.GONE);
        }
    }

    private void bottomButtonClick(int type){
        switch (type){
            case 1:
                // TODO: 预留
                ScheduleReserveActivity.start(this,hallId,hallName,selectedDate,hallBookStatus);
                break;
            case 2:
                // TODO: 预定
                ScheduleBookActivity.start(this,hallId,hallName,selectedDate,hallBookStatus);

                break;
        }
    }

    @Override
    public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
        if (dateChangeBehavior == DateChangeBehavior.PAGE ||
                dateChangeBehavior == DateChangeBehavior.INITIALIZE || dateChangeBehavior == DateChangeBehavior.API){
            String date = String.format("%d年%02d月",year,month);
            tv_date.setText(date);
            String dateParam = String.format("%d-%02d",year,month);
            getCalendar(dateParam,hallId);
        }

        if (dateChangeBehavior == DateChangeBehavior.INITIALIZE){
            getHallList(LocalDate.now().toString("yyyy-MM-dd"));
        }

        if (dateChangeBehavior == DateChangeBehavior.CLICK || dateChangeBehavior == DateChangeBehavior.API){
            if (localDate != null) {
                getHallInfo(localDate.toString("yyyy-MM-dd"),hallId);
                selectedDate = localDate.toString("yyyy-MM-dd");
                if (calendar.getCalendarState() == CalendarState.MONTH)
                    calendar.toWeek();
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
                HallListActivity.start(this,hallItems);
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

    @Override
    public void onItemClick(View v, HallItem item, int position) {
        hallId = String.valueOf(item.getId());
        hallName = item.getName();
        hallId = String.valueOf(item.getId());
        tv_title.setText(hallName);
        getHallInfo(selectedDate,hallId);
        getCalendar(selectedDate,hallId);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }

        if (requestCode == 105 && resultCode == RESULT_OK){
            HallItem hallItem = (HallItem) data.getSerializableExtra("hall");
            this.hallId = hallItem.getId()+"";
            hallName = hallItem.getName();
            tv_title.setText(hallName);
            getHallInfo(selectedDate == null?initDate:selectedDate,hallId);
            getCalendar(selectedDate == null?initDate:selectedDate,hallId);
        }
    }
}
