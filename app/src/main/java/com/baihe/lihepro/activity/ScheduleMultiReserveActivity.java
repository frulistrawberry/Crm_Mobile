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
import androidx.fragment.app.Fragment;
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

public class ScheduleMultiReserveActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_hall_name;
    private TextView tv_start_date;
    private TextView tv_end_date;
    private LinearLayout ll_customer;
    private RelativeLayout rl_customer;
    private KeyValueLayout kv_customer;
    private LinearLayout ll_pay_info;
    private LinearLayout ll_no_pay;
    private RecyclerView rv_pay;
    private KeyValueEditLayout kv_reserve;
    private TextView btn_commit;

    private String hallName;
    private String hallId;
    private HallBookStatus hallBookStatus;
    private String customerId;
    private String customerName;
    private SchedulePayAdapter payAdapter;
    private List<PayItem> payInfo = null;
    private String startDate;
    private String endDate;
    private int startType;
    private int endType;
    private String reserveEndDate;
    private ImageView iv_arrow;
    private LinearLayout btn_to_pay;
    private int payShowType = 1;


    public static void start(Fragment context, String hallId, String hallName, String startDate, String endDate, int startType, int endType){
        Intent intent = new Intent(context.getContext(), ScheduleMultiReserveActivity.class);
        intent.putExtra("hallName",hallName);
        intent.putExtra("startDate",startDate);
        intent.putExtra("endDate",endDate);
        intent.putExtra("startType",startType);
        intent.putExtra("endType",endType);
        intent.putExtra("hallId",hallId);
        context.startActivityForResult(intent,101);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("预留档期");
        setContentView(R.layout.activity_multi_schedule_reserve);
        hallBookStatus = (HallBookStatus) getIntent().getSerializableExtra("hallBookStatus");
        hallName = getIntent().getStringExtra("hallName");
        hallId = getIntent().getStringExtra("hallId");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        startType = getIntent().getIntExtra("startType",-1);
        endType = getIntent().getIntExtra("endType",-1);

        findView();
        initView();
    }



    private void findView() {
        tv_hall_name = findViewById(R.id.tv_hall_name);
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
        tv_start_date = findViewById(R.id.tv_start_date);
        tv_end_date = findViewById(R.id.tv_end_date);
    }

    private void initView(){
        tv_hall_name.setText(hallName);
        btn_to_pay.setOnClickListener(this);
        iv_arrow.setImageDrawable(getResources().getDrawable(R.drawable.arrow_right));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        tv_start_date.setText(startDate+(startType==1?" 午宴":" 晚宴"));
        tv_end_date.setText(endDate+(endType==1?" 午宴":" 晚宴"));

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
        eventEntity.setParamKey("reserveEndDate");
        dateEntity.setEndCalendar("1");
        dateEntity.setEvent(eventEntity);
        List<KeyValueEntity> keyValueEntityList = new ArrayList<>();
        keyValueEntityList.add(dateEntity);
        kv_reserve.setData(keyValueEntityList);

        btn_commit.setOnClickListener(this);
        kv_customer.setOnClickListener(this);



    }

    boolean singleStatus = false;

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
            case R.id.btn_commit:
                Map<String,Object> endDateMap = kv_reserve.commit();
                reserveEndDate = (String) endDateMap.get("reserveEndDate");
                if (TextUtils.isEmpty(reserveEndDate)){
                    ToastUtils.toast("请选择预留截止日期");
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                try {
                    Date date1 = sdf.parse(reserveEndDate );
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    reserveEndDate = sdf1.format(date1)+" 23:59:59";
                } catch (ParseException e) {
                    reserveEndDate = null;
                    e.printStackTrace();
                }

                if (TextUtils.isEmpty(customerId)){
                    ToastUtils.toast("请选择客户");
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
                .putParamValue("reserveEndDate",reserveEndDate)
                .putParamValue("startDate",startDate)
                .putParamValue("endDate",endDate)
                .putParamValue("startType",startType)
                .putParamValue("endType",endType)
                .putParamValue("isExamine",(payInfo ==null || payInfo.size()==0) && payShowType == 3)
                .putParamValue("customerId",customerId);
        HttpRequest.create(UrlConstant.SCHEDULE_COMMIT_RESERVE_MULTI).putParam(jsonParam).get(new CallBack<ReserveSuccess>() {
            @Override
            public ReserveSuccess doInBackground(String response) {
                return JsonUtils.parse(response, ReserveSuccess.class);
            }

            @Override
            public void success(ReserveSuccess entity) {
                //  预留详情页
                setResult(RESULT_OK);
                ReserveDetailActivity.start(ScheduleMultiReserveActivity.this,""+entity.getReserveId());
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
