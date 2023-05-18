package com.baihe.lihepro.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.StringUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.SchedulePayAdapter;
import com.baihe.lihepro.constant.ScheduleConstant;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.AlertDialog;
import com.baihe.lihepro.entity.KeyValEventEntity;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.entity.schedule.HallBookStatus;
import com.baihe.lihepro.entity.schedule.PayInfo;
import com.baihe.lihepro.entity.schedule.PayItem;
import com.baihe.lihepro.entity.schedule.ReserveSuccess;
import com.baihe.lihepro.view.KeyValueEditLayout;
import com.baihe.lihepro.view.KeyValueLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ScheduleReserveActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_hall_name;
    private TextView tv_date;
    private TextView option_lunch;
    private TextView option_dinner;
    private LinearLayout ll_customer;
    private RelativeLayout rl_customer;
    private KeyValueLayout kv_customer;
    private LinearLayout ll_pay_info;
    private LinearLayout ll_no_pay;
    private RecyclerView rv_pay;
    private KeyValueEditLayout kv_reserve;
    private TextView btn_commit;

    private String hallName;
    private String date;
    private String hallId;
    private HallBookStatus hallBookStatus;
    private String customerId;
    private String customerName;
    private SchedulePayAdapter payAdapter;
    private List<PayItem> payInfo = null;
    private int sType;
    private String endDate;
    private ImageView iv_arrow;
    private LinearLayout btn_to_pay;
    private int payShowType = 1;


    public static void start(Activity context, String hallId, String hallName, String date, HallBookStatus hallBookStatus){
        Intent intent = new Intent(context, ScheduleReserveActivity.class);
        intent.putExtra("hallName",hallName);
        intent.putExtra("date",date);
        intent.putExtra("hallBookStatus",hallBookStatus);
        intent.putExtra("hallId",hallId);
        context.startActivityForResult(intent,101);
    }

    public static void start(Context context, String hallId, String hallName, String date, HallBookStatus hallBookStatus,int sType){
        Intent intent = new Intent(context, ScheduleReserveActivity.class);
        intent.putExtra("hallName",hallName);
        intent.putExtra("date",date);
        intent.putExtra("hallBookStatus",hallBookStatus);
        intent.putExtra("hallId",hallId);
        intent.putExtra("sType",sType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("预留档期");
        setContentView(R.layout.activity_schedule_reserve);
        hallBookStatus = (HallBookStatus) getIntent().getSerializableExtra("hallBookStatus");
        hallName = getIntent().getStringExtra("hallName");
        date = getIntent().getStringExtra("date");
        hallId = getIntent().getStringExtra("hallId");
        sType = getIntent().getIntExtra("sType",-1);


        findView();
        initView();
    }



    private void findView() {
        tv_hall_name = findViewById(R.id.tv_hall_name);
        tv_date = findViewById(R.id.tv_date);
        option_lunch = findViewById(R.id.option_lunch);
        option_dinner = findViewById(R.id.option_dinner);
        ll_customer = findViewById(R.id.ll_customer);
        kv_customer = findViewById(R.id.kv_customer);
        rl_customer = findViewById(R.id.rl_customer);
        ll_pay_info = findViewById(R.id.ll_pay_info);
        ll_no_pay = findViewById(R.id.ll_no_pay);
        rv_pay = findViewById(R.id.rv_pay);
        kv_reserve = findViewById(R.id.kv_reserve);
        btn_commit = findViewById(R.id.btn_commit);
        iv_arrow = findViewById(R.id.iv_arrow);
        btn_to_pay = findViewById(R.id.btn_to_pay);
    }

    private void initView(){
        tv_hall_name.setText(hallName);
        btn_to_pay.setOnClickListener(this);
        iv_arrow.setImageDrawable(getResources().getDrawable(R.drawable.arrow_right));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = sdf.parse(date);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
            String dateStr = sdf1.format(date1);
            tv_date.setText(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        initOptions();
        if (sType == 1)
            selectOptions(option_lunch);
        else if (sType == 2)
            selectOptions(option_dinner);
        ll_customer.setOnClickListener(this);
        payAdapter = new SchedulePayAdapter(this);
        rv_pay.setLayoutManager(new LinearLayoutManager(this));
        rv_pay.setAdapter(payAdapter);
        KeyValueEntity dateEntity = new KeyValueEntity();
        KeyValEventEntity eventEntity = new KeyValEventEntity();
        dateEntity.setKey("预留至：");
        dateEntity.setVal("");
        dateEntity.setDefaultVal("");
        dateEntity.setShowStatus("1");
        eventEntity.setAction("datetime");
        eventEntity.setFormat("yyyy年MM月dd日");
        eventEntity.setParamKey("endDate");
        dateEntity.setEndCalendar("1");
        dateEntity.setEvent(eventEntity);
        List<KeyValueEntity> keyValueEntityList = new ArrayList<>();
        keyValueEntityList.add(dateEntity);
        kv_reserve.setData(keyValueEntityList);

        btn_commit.setOnClickListener(this);
        option_lunch.setOnClickListener(this);
        option_dinner.setOnClickListener(this);
        kv_customer.setOnClickListener(this);



    }

    boolean singleStatus = false;

    private void initOptions(){
        if (hallBookStatus.getDinnerStatus()!= ScheduleConstant.SCHEDULE_STATUS_NOT_SET){
            option_dinner.setVisibility(View.VISIBLE);
            if (hallBookStatus.getDinnerStatus() == 0){
                option_dinner.setTextColor(Color.parseColor("#4A4C5C"));
                option_dinner.setBackgroundResource(R.drawable.bg_option_def);
            }else {
                option_dinner.setTextColor(Color.parseColor("#AEAEBC"));
                option_dinner.setBackgroundResource(R.drawable.bg_option_unavailable);
            }
        }else {
            singleStatus = true;
            option_dinner.setVisibility(View.GONE);
        }

        if (hallBookStatus.getLunchStatus()!= ScheduleConstant.SCHEDULE_STATUS_NOT_SET){
            option_lunch.setVisibility(View.VISIBLE);
            if (hallBookStatus.getLunchStatus() == 0){
                option_lunch.setTextColor(Color.parseColor("#4A4C5C"));
                option_lunch.setBackgroundResource(R.drawable.bg_option_def);
            }else {
                option_lunch.setTextColor(Color.parseColor("#AEAEBC"));
                option_lunch.setBackgroundResource(R.drawable.bg_option_unavailable);
            }
        }else {
            singleStatus = true;
            option_lunch.setVisibility(View.GONE);
        }
        if (singleStatus && hallBookStatus.getDinnerStatus()!=ScheduleConstant.SCHEDULE_STATUS_NOT_SET){
            selectOptions(option_dinner);
            sType = 2;
        }

        if (singleStatus && hallBookStatus.getLunchStatus()!=ScheduleConstant.SCHEDULE_STATUS_NOT_SET){
            selectOptions(option_lunch);
            sType = 1;
        }

    }
    private void selectOptions(TextView v){
        v.setBackgroundResource(R.drawable.bg_option_select);
        v.setTextColor(Color.WHITE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getPayInfo();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_to_pay:
                AddPayCodeActivity.start(this,customerId,customerName);
                break;
            case R.id.kv_customer:
            case R.id.ll_customer:
                CustomerSearchActivity.start(this);
                break;
            case R.id.option_lunch:
                if (hallBookStatus.getLunchStatus()==0){
                    initOptions();
                    selectOptions((TextView) view);
                    sType = 1;
                }
                break;
            case R.id.option_dinner:
                if (hallBookStatus.getDinnerStatus()==0){
                    initOptions();
                    selectOptions((TextView) view);
                    sType = 2;
                }
                break;
            case R.id.btn_commit:
                Map<String,Object> endDateMap = kv_reserve.commit();
                endDate = (String) endDateMap.get("endDate");
                if (TextUtils.isEmpty(endDate)){
                    ToastUtils.toast("请选择预留截止日期");
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                try {
                    Date date1 = sdf.parse(endDate );
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    endDate = sdf1.format(date1)+" 23:59:59";
                } catch (ParseException e) {
                    endDate = null;
                    e.printStackTrace();
                }

                if (TextUtils.isEmpty(customerId)){
                    ToastUtils.toast("请选择客户");
                    return;
                }


                if (sType == 0){
                    ToastUtils.toast("请选择档期时段");
                    return;
                }
                if (payShowType == 1 && (payInfo == null || payInfo.size()==0)){
                    ToastUtils.toast("请先去付款");
                    return;
                }

                if (payShowType == 3 && (payInfo == null || payInfo.size() == 0)){
                    new AlertDialog.Builder(this).setContent("客户还未付款，档期预留需要审批，是否提交申请？")
                            .setConfirmListener("立即申请", new AlertDialog.OnConfirmClickListener() {
                                @Override
                                public void onConfirm(Dialog dialog) {
                                    commit();
                                    dialog.dismiss();
                                }
                            }).setCancelListener("暂不申请", new AlertDialog.OnCancelClickListener() {
                        @Override
                        public void onCancel(Dialog dialog) {
                            dialog.dismiss();
                        }
                    }).build().show();
                    return;
                }
                commit();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK){
            // TODO: 选择客户
            customerId = data.getStringExtra("customerId");
            customerName = data.getStringExtra("name");
            String phone = data.getStringExtra("phone");
            KeyValueEntity entity1 = new KeyValueEntity();
            entity1.setKey("客户编号");
            entity1.setVal(customerId);

            KeyValueEntity entity2 = new KeyValueEntity();
            entity2.setKey("客户姓名");
            entity2.setVal(customerName);

            KeyValueEntity entity3 = new KeyValueEntity();
            entity3.setKey("联系电话");
            entity3.setVal(StringUtils.blurPhone(phone));
            List<KeyValueEntity> entities = new ArrayList<>();
            entities.add(entity1);
            entities.add(entity2);
            entities.add(entity3);
            kv_customer.setData(entities);
            rl_customer.setVisibility(View.VISIBLE);
            ll_customer.setVisibility(View.GONE);
            getPayInfo();
        }
    }

    private void getPayInfo() {
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("customerId",customerId);
        HttpRequest.create(UrlConstant.SCHEDULE_GET_PAY_INFO).putParam(jsonParam).get(new CallBack<PayInfo>() {
            @Override
            public PayInfo doInBackground(String response) {
                return JsonUtils.parse(response, PayInfo.class);
            }

            @Override
            public void success(PayInfo entity) {
                refreshPayInfo(entity);
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

    private void refreshPayInfo(PayInfo entity) {
        payShowType = entity.getShowType();
        this.payInfo = entity.getPayInfo();
        if (payShowType == 2){
            ll_pay_info.setVisibility(View.GONE);
        }else {
            ll_pay_info.setVisibility(View.VISIBLE);
            if (entity.getPayInfo()==null || entity.getPayInfo().size()==0){
                ll_no_pay.setVisibility(View.VISIBLE);
                rv_pay.setVisibility(View.GONE);
            }else {
                ll_no_pay.setVisibility(View.GONE);
                rv_pay.setVisibility(View.VISIBLE);
                payAdapter.setData(entity.getPayInfo());
            }
        }
    }

    private void commit(){
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("hallId",hallId)
                .putParamValue("hallName",hallName)
                .putParamValue("date",date)
                .putParamValue("endDate",endDate)
                .putParamValue("sType",sType)
                .putParamValue("isExamine",(payInfo ==null || payInfo.size()==0) && payShowType == 3)
                .putParamValue("customerId",customerId);
        HttpRequest.create(UrlConstant.SCHEDULE_COMMIT_RESERVE).putParam(jsonParam).get(new CallBack<ReserveSuccess>() {
            @Override
            public ReserveSuccess doInBackground(String response) {
                return JsonUtils.parse(response, ReserveSuccess.class);
            }

            @Override
            public void success(ReserveSuccess entity) {
                //  预留详情页
                setResult(RESULT_OK);
                ReserveDetailActivity.start(ScheduleReserveActivity.this,""+entity.getReserveId());
                finish();
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
}
