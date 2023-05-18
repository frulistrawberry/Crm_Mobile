package com.baihe.lihepro.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.StringUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.ScheduleConstant;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.DateDialogUtils;
import com.baihe.lihepro.dialog.HallSelectDialog;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.entity.schedule.ContractInfo;
import com.baihe.lihepro.entity.schedule.HallBookStatus;
import com.baihe.lihepro.entity.schedule.HallItem;
import com.baihe.lihepro.entity.schedule.ReserveSuccess;
import com.baihe.lihepro.view.KeyValueLayout;
import com.baihe.lihepro.view.LiheTimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.SPUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NewResetBookActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_hall_name;
    private TextView tv_startDate;
    private TextView tv_endDate;
    private TextView startDateLabel;
    private TextView endDateLabel;
    private TextView btn_commit;
    private LinearLayout ll_schedule;
    private RelativeLayout rl_contract;
    private KeyValueLayout kv_contract;


    private String hallName;
    private String hallId;
    private String contractId;
    private String customerId;
    private String bookId;
    private String startDate;
    private String endDate;
    private int startType;
    private int endType;
    private String reserve_num;
    private int isMulti;
    private String date;
    private int sType;


    public static void start(Activity context,String bookId,String contractId, String hallId,String reserve_num,
                             String hallName,int startType,int endType,
                             String startDate,String endDate,
                             String date,int isMulti,int sType){
        Intent intent = new Intent(context, NewResetBookActivity.class);
        intent.putExtra("hallName",hallName);
        intent.putExtra("hallId",hallId);
        intent.putExtra("bookId",bookId);
        intent.putExtra("contractId",contractId);
        intent.putExtra("startType",startType);
        intent.putExtra("endType",endType);
        intent.putExtra("startDate",startDate);
        intent.putExtra("endDate",endDate);
        intent.putExtra("reserve_num",reserve_num);
        intent.putExtra("date",date);
        intent.putExtra("isMulti",isMulti);
        intent.putExtra("sType",sType);
        context.startActivityForResult(intent,101);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("改期/换厅");
        setContentView(R.layout.actvity_reset_book_new);
        hallName = getIntent().getStringExtra("hallName");
        hallId = getIntent().getStringExtra("hallId");
        bookId = getIntent().getStringExtra("bookId");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        date = getIntent().getStringExtra("date");
        startType = getIntent().getIntExtra("startType",-1);
        endType = getIntent().getIntExtra("endType",-1);
        sType = getIntent().getIntExtra("sType",-1);
        isMulti = getIntent().getIntExtra("isMulti",0);
        reserve_num = getIntent().getStringExtra("reserve_num");
        contractId = getIntent().getStringExtra("contractId");


        findView();
        initView();
        getContractInfo(contractId);
    }

    private void findView() {
        tv_hall_name = findViewById(R.id.tv_hall_name);
        tv_startDate = findViewById(R.id.tv_start_date);
        tv_endDate = findViewById(R.id.tv_end_date);
        ll_schedule = findViewById(R.id.ll_schedule);
        tv_hall_name = findViewById(R.id.tv_hall_name);
        tv_hall_name = findViewById(R.id.tv_hall_name);
        rl_contract = findViewById(R.id.rl_contract);
        kv_contract = findViewById(R.id.kv_contract);
        startDateLabel = findViewById(R.id.startDateLabel);
        endDateLabel = findViewById(R.id.endDateLabel);

        btn_commit = findViewById(R.id.btn_commit);

    }

    private void initView(){


        rl_contract.setOnClickListener(this);

        btn_commit.setOnClickListener(this);
        ll_schedule.setOnClickListener(this);
        fillData();
    }


    public void fillData(){
        tv_hall_name.setText(hallName);
        if (isMulti == 1){
            startDateLabel.setText("档期开始：");
            endDateLabel.setText("档期结束：");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
            if (startDate.contains("年")){
                try {
                    Date date1 = sdf1.parse(startDate);
                    startDate = sdf.format(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (endDate.contains("年")){
                try {
                    Date date1 = sdf1.parse(endDate);
                    endDate = sdf.format(date1);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            try {
                Date date = sdf.parse(startDate);
                String dateStr = sdf1.format(date);
                tv_startDate.setText(dateStr+(startType == 1?" 午宴":" 晚宴"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                Date date = sdf.parse(endDate);
                String dateStr = sdf1.format(date);
                tv_endDate.setText(dateStr+(endType == 1?" 午宴":" 晚宴"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else {
            startDateLabel.setText("档期日期：");
            endDateLabel.setText("档期时段：");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date1 = sdf.parse(date);
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
                String dateStr = sdf1.format(date1);
                tv_startDate.setText(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tv_endDate.setText(sType == 1?" 午宴":" 晚宴");
        }




    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 102){
            setResult(RESULT_OK);
            finish();
        }

        if (requestCode == 105 && resultCode == RESULT_OK){
            this.startType = data.getIntExtra("startType",-1);
            this.endType = data.getIntExtra("endType",-1);
            this.hallId = data.getStringExtra("hallId");
            this.hallName = data.getStringExtra("hallName");
            this.startDate = data.getStringExtra("startDate");
            this.endDate = data.getStringExtra("endDate");
            this.date = data.getStringExtra("date");
            this.sType = data.getIntExtra("sType",-1);
            this.isMulti = data.getIntExtra("isMulti",0);
            fillData();
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
        entity3.setVal(StringUtils.blurPhone(entity.getMobile()));
        data.add(entity1);
        data.add(entity2);
        data.add(entity3);
        kv_contract.setData(data);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){


            case R.id.btn_commit:

                commit();
                break;
            case R.id.ll_schedule:
                int companyLevel = SPUtils.getInstance().getInt("companyLevel");
                if (isMulti == 1)
                    MultiScheduleHomeActivity.start(this,companyLevel,startDate,true);
                else
                    MultiScheduleHomeActivity.start(this,companyLevel,date,false);
                break;


        }
    }



    private void commit(){
        if (isMulti == 1){
            JsonParam jsonParam = JsonParam.newInstance("params")
                    .putParamValue("hallId",hallId)
                    .putParamValue("hallName",hallName)
                    .putParamValue("startDate",startDate)
                    .putParamValue("endDate",endDate)
                    .putParamValue("startType",startType)
                    .putParamValue("endType",endType)
                    .putParamValue("contractId",contractId)
                    .putParamValue("flag",3)
                    .putParamValue("bookId",bookId)
                    .putParamValue("reserve_num",reserve_num)
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
                    BookDetailActivity.start(NewResetBookActivity.this,bookId);
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
        }else {
            JsonParam jsonParam = JsonParam.newInstance("params")
                    .putParamValue("hallId",hallId)
                    .putParamValue("hallName",hallName)
                    .putParamValue("date",date)
                    .putParamValue("contractId",contractId)
                    .putParamValue("flag",3)
                    .putParamValue("sType",sType)
                    .putParamValue("bookId",bookId)
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
                    BookDetailActivity.start(NewResetBookActivity.this,bookId);
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






}
