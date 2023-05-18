package com.baihe.lihepro.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.ScheduleConstant;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.entity.schedule.ContractInfo;
import com.baihe.lihepro.entity.schedule.HallBookStatus;
import com.baihe.lihepro.entity.schedule.ReserveSuccess;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;


public class ScheduleBookActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_hall_name;
    private TextView tv_date;
    private TextView option_lunch;
    private TextView option_dinner;
    private LinearLayout ll_contract;
    private RelativeLayout rl_contract;
    private KeyValueLayout kv_contract;
    private TextView btn_commit;

    private String hallName;
    private String date;
    private String hallId;
    private HallBookStatus hallBookStatus;
    private String contractId;
    private int sType;
    private String customerId;

    public static void start(Activity context, String hallId, String hallName, String date, HallBookStatus hallBookStatus){
        Intent intent = new Intent(context, ScheduleBookActivity.class);
        intent.putExtra("hallName",hallName);
        intent.putExtra("date",date);
        intent.putExtra("hallBookStatus",hallBookStatus);
        intent.putExtra("hallId",hallId);
        context.startActivityForResult(intent,101);
    }

    public static void start(Context context, String hallId, String hallName, String date, HallBookStatus hallBookStatus,int sType){
        Intent intent = new Intent(context, ScheduleBookActivity.class);
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
        setTitleText("预订档期");
        setContentView(R.layout.actvity_schedule_book);
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
        ll_contract = findViewById(R.id.ll_contract);
        kv_contract = findViewById(R.id.kv_contract);
        rl_contract = findViewById(R.id.rl_contract);
        btn_commit = findViewById(R.id.btn_commit);
    }

    private void initView(){
        tv_hall_name.setText(hallName);
        tv_date.setText(date);
        initOptions();
        if (sType == 1){
            selectOptions(option_lunch);
        }else if (sType == 2)
            selectOptions(option_dinner);
        ll_contract.setOnClickListener(this);
        rl_contract.setOnClickListener(this);

        btn_commit.setOnClickListener(this);
        option_lunch.setOnClickListener(this);
        option_dinner.setOnClickListener(this);



    }

    boolean singleStatus;
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
            option_dinner.setVisibility(View.GONE);
            singleStatus = true;
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
            option_lunch.setVisibility(View.GONE);
            singleStatus = true;
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK){
            contractId = data.getStringExtra("contractId");
            getContractInfo(contractId);
        }
        if (requestCode == 101 && resultCode == 102){
            setResult(RESULT_OK);
            finish();
        }
    }

    private void getContractInfo(String contractId) {
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("contractId",contractId);
        HttpRequest.create(UrlConstant.SCHEDULE_GET_CONTRACT_INFO).putParam(jsonParam).get(new CallBack<ContractInfo>() {
            @Override
            public ContractInfo doInBackground(String response) {
                return JsonUtils.parse(response, ContractInfo.class);
            }

            @Override
            public void success(ContractInfo entity) {
                refreshContractInfo(entity);
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

    private void refreshContractInfo(ContractInfo entity) {
        ll_contract.setVisibility(View.GONE);
        rl_contract.setVisibility(View.VISIBLE);
        customerId = entity.getCustomerId();
        List<KeyValueEntity> data = new ArrayList<>();
        KeyValueEntity entity1 = new KeyValueEntity();
        entity1.setKey("合同编号");
        entity1.setVal(entity.getContractNum());
        KeyValueEntity entity2 = new KeyValueEntity();
        entity2.setKey("客户姓名");
        entity2.setVal(entity.getCustomerName());
        KeyValueEntity entity3 = new KeyValueEntity();
        entity3.setKey("联系电话");
        entity3.setVal(entity.getMobile());
        data.add(entity1);
        data.add(entity2);
        data.add(entity3);
        kv_contract.setData(data);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_contract:
            case R.id.rl_contract:
                ContractSearchActivity.start(this,date,hallId,hallName,sType);
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
                if (sType == 0){
                    ToastUtils.toast("请选择档期时段");
                    return;
                }
                if (contractId == null){
                    ToastUtils.toast("请选择合同");
                    return;
                }
                commit();
                break;
        }
    }

    private void commit(){
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("hallId",hallId)
                .putParamValue("hallName",hallName)
                .putParamValue("date",date)
                .putParamValue("contractId",contractId)
                .putParamValue("flag",1)
                .putParamValue("sType",sType)
                .putParamValue("customerId",customerId);
        HttpRequest.create(UrlConstant.SCHEDULE_COMMIT_BOOK).putParam(jsonParam).get(new CallBack<ReserveSuccess>() {
            @Override
            public ReserveSuccess doInBackground(String response) {
                return JsonUtils.parse(response, ReserveSuccess.class);
            }

            @Override
            public void success(ReserveSuccess entity) {
                // TODO: 预留详情页
                setResult(RESULT_OK);
                BookDetailActivity.start(ScheduleBookActivity.this,""+entity.getReserveId());
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
