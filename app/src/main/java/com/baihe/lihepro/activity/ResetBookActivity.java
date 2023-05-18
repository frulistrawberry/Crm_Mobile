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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ResetBookActivity extends BaseActivity implements View.OnClickListener, OnTimeSelectListener {

    private TextView tv_hall_name;
    private TextView tv_date;
    private TextView option_lunch;
    private TextView option_dinner;
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
    private String bookId;
    private ImageView ivHall;
    private ImageView ivDate;
    private TimePickerView timePickerView;
    private HallItem hallItem;
    private List<HallItem> hallItems;
    private int dinnerStatus;
    private int lunchStatus;
    private LinearLayout ll_stype;
    private View v_sType;

    public static void start(Activity context,String bookId,String contractId, String hallId, String hallName, String date, int sType){
        Intent intent = new Intent(context, ResetBookActivity.class);
        intent.putExtra("hallName",hallName);
        intent.putExtra("date",date);
        intent.putExtra("sType",sType);
        intent.putExtra("hallId",hallId);
        intent.putExtra("bookId",bookId);
        intent.putExtra("contractId",contractId);
        context.startActivityForResult(intent,101);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("改期/换厅");
        setContentView(R.layout.actvity_reset_book);
        hallBookStatus = new HallBookStatus(0,0);
        hallName = getIntent().getStringExtra("hallName");
        date = getIntent().getStringExtra("date");
        hallId = getIntent().getStringExtra("hallId");
        bookId = getIntent().getStringExtra("bookId");
        contractId = getIntent().getStringExtra("contractId");


        findView();
        initView();
        getContractInfo(contractId);
    }

    private void findView() {
        tv_hall_name = findViewById(R.id.tv_hall_name);
        tv_date = findViewById(R.id.tv_date);
        option_lunch = findViewById(R.id.option_lunch);
        option_dinner = findViewById(R.id.option_dinner);
        kv_contract = findViewById(R.id.kv_contract);
        rl_contract = findViewById(R.id.rl_contract);
        btn_commit = findViewById(R.id.btn_commit);
        ivHall = findViewById(R.id.iv_hall);
        ivDate = findViewById(R.id.iv_date);
        ll_stype = findViewById(R.id.ll_stype);
        v_sType = findViewById(R.id.v_sType);
    }

    private void initView(){
        tv_hall_name.setText(hallName);
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
        querySType();


        rl_contract.setOnClickListener(this);

        btn_commit.setOnClickListener(this);
        option_lunch.setOnClickListener(this);
        option_dinner.setOnClickListener(this);
        tv_hall_name.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        ivHall.setVisibility(View.VISIBLE);
        ivDate.setVisibility(View.VISIBLE);
        LiheTimePickerBuilder pickerBuilder = DateDialogUtils.createPickerViewBuilder(this,this);

        timePickerView = pickerBuilder.build();



    }
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
        }

    }
    private void selectOptions(TextView v){
        v.setBackgroundResource(R.drawable.bg_option_select);
        v.setTextColor(Color.WHITE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 102){
            setResult(RESULT_OK);
            finish();
        }

        if (requestCode == 105 && resultCode == RESULT_OK){
            HallItem hallItem = (HallItem) data.getSerializableExtra("hall");
            hallId = hallItem.getId()+"";
            hallName = hallItem.getName();
            tv_hall_name.setText(hallName);
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
            case R.id.tv_date:
                timePickerView.show();
                break;
            case R.id.tv_hall_name:
                new HallSelectDialog.Builder(context)
                        .setTitle("请选择宴会厅")
                        .loadHotelList(null)
                        .setOnConfirmClickListener(new HallSelectDialog.OnConfirmClickListener() {
                            @Override
                            public void onConfirm(Dialog dialog, String selectText, String selectId) {
                                hallId = selectId;
                                hallName = selectText;
                                tv_hall_name.setText(hallName);
                                querySType();
                            }
                        }).setOnCancelClickListener(new HallSelectDialog.OnCancelClickListener() {
                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).build().show();
                break;

        }
    }



    private void commit(){
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
                BookDetailActivity.start(ResetBookActivity.this,bookId);
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

    @Override
    public void onTimeSelect(Date date, View v) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String time = format.format(date);
        tv_date.setText(time);
        this.date = format1.format(date);
        querySType();

    }

    private void querySType(){
        sType = 0;
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("hallId",hallId)
                .putParamValue("date",date);
        HttpRequest.create(UrlConstant.SCHEDULE_RESCHEDULE_HALL_RESERVE_INFO).putParam(jsonParam).get(new CallBack<HallItem>() {
            @Override
            public HallItem doInBackground(String response) {
                return JsonUtils.parse(response, HallItem.class);
            }

            @Override
            public void success(HallItem entity) {
                lunchStatus = entity.getLunchStatus();
                dinnerStatus = entity.getDinnerStatus();
                hallBookStatus = new HallBookStatus(lunchStatus,dinnerStatus);
                if (lunchStatus == -1 && dinnerStatus == -1){
                    ll_stype.setVisibility(View.GONE);
                    v_sType.setVisibility(View.GONE);
                }else {
                    ll_stype.setVisibility(View.VISIBLE);
                    v_sType.setVisibility(View.VISIBLE);
                }
                if (lunchStatus == -1){
                    option_lunch.setVisibility(View.GONE);
                    if (dinnerStatus == 0){
                        selectOptions(option_dinner);
                        sType = 2;
                    }
                }else {
                    option_lunch.setVisibility(View.VISIBLE);
                }
                if (dinnerStatus == -1){
                    option_dinner.setVisibility(View.GONE);
                    if (lunchStatus == 0){
                        selectOptions(option_lunch);
                        sType = 1;
                    }
                }else {
                    option_dinner.setVisibility(View.VISIBLE);
                }

                if (lunchStatus !=0){
                    option_lunch.setTextColor(Color.parseColor("#AEAEBC"));
                    option_lunch.setBackgroundResource(R.drawable.bg_option_unavailable);
                }else if (sType!=1){
                    option_lunch.setTextColor(Color.parseColor("#4A4C5C"));
                    option_lunch.setBackgroundResource(R.drawable.bg_option_def);
                }

                if (dinnerStatus!=0){
                    option_dinner.setTextColor(Color.parseColor("#AEAEBC"));
                    option_dinner.setBackgroundResource(R.drawable.bg_option_unavailable);
                }else  if (sType!=2){
                    option_dinner.setTextColor(Color.parseColor("#4A4C5C"));
                    option_dinner.setBackgroundResource(R.drawable.bg_option_def);
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


}
