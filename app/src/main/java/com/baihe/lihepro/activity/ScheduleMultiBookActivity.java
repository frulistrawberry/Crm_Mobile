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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ScheduleMultiBookActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_hall_name;
    private TextView tv_start_date;
    private TextView tv_end_date;
    private LinearLayout ll_contract;
    private RelativeLayout rl_contract;
    private KeyValueLayout kv_contract;
    private TextView btn_commit;

    private String hallName;
    private String startDate;
    private String endDate;
    private int startType;
    private int endType;
    private String hallId;
    private String contractId;
    private String customerId;

    public static void start(Fragment context, String hallId, String hallName, String startDate, String endDate, int startType, int endType){
        Intent intent = new Intent(context.getContext(), ScheduleMultiBookActivity.class);
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
        setTitleText("预订档期");
        setContentView(R.layout.activity_multi_schedule_book);
        hallName = getIntent().getStringExtra("hallName");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        startType = getIntent().getIntExtra("startType",-1);
        endType = getIntent().getIntExtra("endType",-1);
        hallId = getIntent().getStringExtra("hallId");


        findView();
        initView();
    }

    private void findView() {
        tv_hall_name = findViewById(R.id.tv_hall_name);
        tv_start_date = findViewById(R.id.tv_start_date);
        tv_end_date = findViewById(R.id.tv_end_date);
        ll_contract = findViewById(R.id.ll_contract);
        kv_contract = findViewById(R.id.kv_contract);
        rl_contract = findViewById(R.id.rl_contract);
        btn_commit = findViewById(R.id.btn_commit);
    }

    private void initView(){
        tv_hall_name.setText(hallName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            tv_start_date.setText(sdf1.format(sdf.parse(startDate))+(startType==1?" 午宴":" 晚宴"));
            tv_end_date.setText(sdf1.format(sdf.parse(endDate))+(startType==1?" 午宴":" 晚宴"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ll_contract.setOnClickListener(this);
        rl_contract.setOnClickListener(this);

        btn_commit.setOnClickListener(this);



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
                ContractSearchActivity.start(this,hallId,hallName,startType,endType,startDate,endDate);
                break;
            case R.id.btn_commit:
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
                .putParamValue("startDate",startDate)
                .putParamValue("endDate",endDate)
                .putParamValue("contractId",contractId)
                .putParamValue("flag",1)
                .putParamValue("startType",startType)
                .putParamValue("endType",endType)
                .putParamValue("customerId",customerId);
        HttpRequest.create(UrlConstant.SCHEDULE_COMMIT_MULTI_BOOK).putParam(jsonParam).get(new CallBack<ReserveSuccess>() {
            @Override
            public ReserveSuccess doInBackground(String response) {
                return JsonUtils.parse(response, ReserveSuccess.class);
            }

            @Override
            public void success(ReserveSuccess entity) {
                // TODO: 预留详情页
                setResult(RESULT_OK);
                BookDetailActivity.start(ScheduleMultiBookActivity.this,""+entity.getReserveId());
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
