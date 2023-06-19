package com.baihe.lihepro.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.ImageUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.StringUtils;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.adapter.SchedulePayAdapter;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.AlertDialog;
import com.baihe.lihepro.dialog.ApproveDialog;
import com.baihe.lihepro.entity.ApproveEntity;
import com.baihe.lihepro.entity.ButtonTypeEntity;
import com.baihe.lihepro.entity.KeyValEventEntity;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.entity.schedule.CustomerInfo;
import com.baihe.lihepro.entity.schedule.PayInfo;
import com.baihe.lihepro.entity.schedule.PayItem;
import com.baihe.lihepro.entity.schedule.ReserveDetail;
import com.baihe.lihepro.entity.schedule.ReserveInfo;
import com.baihe.lihepro.entity.schedule.ReserveSuccess;
import com.baihe.lihepro.entity.schedule.ScheduleInfo;
import com.baihe.lihepro.entity.schedule.StatusText;
import com.baihe.lihepro.utils.Utils;
import com.baihe.lihepro.view.KeyValueLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ReserveDetailActivity extends BaseActivity {

    private TextView reserve_detail_schedule_title_tv;
    private TextView reserve_detail_customer_title_tv;
    private TextView reserve_detail_pay_title_tv;
    private TextView reserve_detail_reserve_title_tv;
    private TextView reserve_detail_botton1_tv;
    private TextView reserve_detail_botton2_tv;
    private TextView reserve_detail_botton3_tv;
    private LinearLayout reserve_detail_schedule_ll;
    private LinearLayout reserve_detail_customer_ll;
    private LinearLayout reserve_detail_reserve_ll;
    private LinearLayout reserve_detail_bottom_ll;
    private KeyValueLayout reserve_detail_schedule_kvl;
    private KeyValueLayout reserve_detail_customer_kvl;
    private KeyValueLayout reserve_detail_reserve_kvl;
    private RecyclerView reserve_detail_pay_rv;
    private LinearLayout btn_approve;
    private ImageView iv_arrow;
    private FrameLayout fl_bg;
    private TextView tv_status;
    private View v_bg;

    private NestedScrollView scrollView;

    private String reserveId;
    private ScheduleInfo scheduleInfo;
    private List<ApproveEntity> approveList;
    private String startDate;
    private String endDate;

    public static void start(Context context,String reserveId){
        Intent intent = new Intent(context, ReserveDetailActivity.class);
        intent.putExtra("reserveId",reserveId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("预留详情");
        reserveId = getIntent().getStringExtra("reserveId");
        setContentView(R.layout.activity_reserve_detail);
        init();
        listener();
        loadData();
    }

    private void init(){
        reserve_detail_schedule_title_tv = findViewById(R.id.reserve_detail_schedule_title_tv);
        reserve_detail_customer_title_tv = findViewById(R.id.reserve_detail_customer_title_tv);
        reserve_detail_pay_title_tv = findViewById(R.id.reserve_detail_pay_title_tv);
        reserve_detail_reserve_title_tv = findViewById(R.id.reserve_detail_reserve_title_tv);
        reserve_detail_botton1_tv = findViewById(R.id.reserve_detail_botton1_tv);
        reserve_detail_botton2_tv = findViewById(R.id.reserve_detail_botton2_tv);
        reserve_detail_botton3_tv = findViewById(R.id.reserve_detail_botton3_tv);
        reserve_detail_schedule_ll = findViewById(R.id.reserve_detail_schedule_ll);
        reserve_detail_customer_ll = findViewById(R.id.reserve_detail_customer_ll);
        reserve_detail_reserve_ll = findViewById(R.id.reserve_detail_reserve_ll);
        reserve_detail_bottom_ll = findViewById(R.id.reserve_detail_bottom_ll);
        reserve_detail_schedule_kvl = findViewById(R.id.reserve_detail_schedule_kvl);
        reserve_detail_customer_kvl = findViewById(R.id.reserve_detail_customer_kvl);
        reserve_detail_reserve_kvl = findViewById(R.id.reserve_detail_reserve_kvl);
        reserve_detail_pay_rv = findViewById(R.id.reserve_detail_pay_rv);
        btn_approve = findViewById(R.id.btn_approve);
        scrollView = findViewById(R.id.scrollView);
        tv_status = findViewById(R.id.tv_status);
        iv_arrow = findViewById(R.id.iv_arrow);
        fl_bg = findViewById(R.id.fl_bg);
        v_bg = findViewById(R.id.v_bg);
    }

    private void listener(){
        statusLayout.setOnStatusClickListener(new StatusChildLayout.OnStatusClickListener() {
            @Override
            public void onNetErrorClick() {
                loadData();
            }

            @Override
            public void onNetFailClick() {
                loadData();
            }

            @Override
            public void onExpandClick() {

            }
        });

        btn_approve.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View view) {
                if (approveList!=null && approveList.size()>0)
                new ApproveDialog(ReserveDetailActivity.this,approveList,scheduleInfo.getHallName(),scheduleInfo.getDate(),scheduleInfo.getSType() == 1?"午宴":"晚宴",
                        scheduleInfo.getEnd_date(),scheduleInfo.getEnd_type() == 1?"午":"晚",scheduleInfo.getStart_date(),scheduleInfo.getStart_type() == 1?"午":"晚",scheduleInfo.getIsMulti() == 1).show();

            }
        });
    }

    private void loadData(){

        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("reserveId",reserveId);

        HttpRequest.create(UrlConstant.SCHEDULE_GET_RESERVE_INFO).putParam(jsonParam).get(new CallBack<ReserveDetail>() {
            @Override
            public ReserveDetail doInBackground(String response) {
                if ("[]".equals(response)) {
                    return new ReserveDetail();
                } else {
                    return JsonUtils.parse(response, ReserveDetail.class);
                }
            }

            @Override
            public void success(ReserveDetail entity) {
                statusLayout.normalStatus();
                refreshReserveInfo(entity);
            }

            @Override
            public void error() {
                statusLayout.netErrorStatus();
            }

            @Override
            public void fail() {
                statusLayout.netFailStatus();
            }

            @Override
            public void before() {
                super.before();
                statusLayout.loadingStatus();
            }
        });
    }

    private void refreshReserveInfo(ReserveDetail entity) {
        scheduleInfo = entity.getScheduleInfo();

        StatusText statusText = entity.getStatusText();
        if (statusText != null) {
            fl_bg.setVisibility(View.VISIBLE);
            v_bg.setBackgroundColor(Color.parseColor(statusText.getBgColor()));
            tv_status.setText(statusText.getText());
            tv_status.setTextColor(Color.parseColor(statusText.getTextColor()));
            Drawable drawable = ImageUtils.tintDrawable(getResources().getDrawable(R.drawable.arrow_right_copy), ColorStateList.valueOf(Color.parseColor(statusText.getTextColor())));
            iv_arrow.setImageDrawable(drawable);
        }else {
            fl_bg.setVisibility(View.GONE);
        }
        approveList = entity.getApproveInfo();
        if (approveList!=null){
            List<ApproveEntity> temp = new ArrayList<>();
            for (ApproveEntity approveEntity : approveList) {
                if (!TextUtils.isEmpty(approveEntity.getDateTime())){
                    temp.add(approveEntity);
                }
            }
            Collections.sort(temp, new Comparator<ApproveEntity>() {
                @Override
                public int compare(ApproveEntity approveEntity, ApproveEntity t1) {
                    return -1;
                }
            });
            approveList = temp;
        }


        if (approveList == null || approveList.size()==0){
            iv_arrow.setVisibility(View.GONE);
        }else {
            iv_arrow.setVisibility(View.VISIBLE);
        }
        if (scheduleInfo != null) {
            reserve_detail_schedule_title_tv.setVisibility(View.VISIBLE);
            reserve_detail_schedule_ll.setVisibility(View.VISIBLE);
            List<KeyValueEntity> data = new ArrayList<>();
            KeyValueEntity entity1 = new KeyValueEntity();
            entity1.setKey("宴会厅");
            entity1.setVal(scheduleInfo.getHallName());
            KeyValueEntity entity2 = new KeyValueEntity();
            entity2.setKey("档期日期");
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(scheduleInfo.getDate());
                entity2.setVal(new SimpleDateFormat("yyyy年MM月dd日").format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            KeyValueEntity entity3 = new KeyValueEntity();
            entity3.setKey("档期时段");
            entity3.setVal(scheduleInfo.getSType()==1?"午宴":"晚宴");


            KeyValueEntity entity4 = new KeyValueEntity();
            entity4.setKey("档期开始");
            try {
                if (scheduleInfo.getStart_date()!=null){
                    Date date = new SimpleDateFormat("yyyy年MM月dd日").parse(scheduleInfo.getStart_date());
                    startDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    entity4.setVal(startDate+(scheduleInfo.getStart_type() == 1?" 午宴":" 晚宴"));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            KeyValueEntity entity5 = new KeyValueEntity();
            entity5.setKey("档期结束");

            try {
                if (scheduleInfo.getStart_date()!=null){
                    Date date = new SimpleDateFormat("yyyy年MM月dd日").parse(scheduleInfo.getEnd_date());
                    endDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    entity5.setVal(endDate+(scheduleInfo.getEnd_type() == 1?" 午宴":" 晚宴"));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


            data.add(entity1);
            if (scheduleInfo.getIsMulti() == 1){
                data.add(entity4);
                data.add(entity5);
            }else {
                data.add(entity2);
                data.add(entity3);
            }
            reserve_detail_schedule_kvl.setData(data);


        }else {
            reserve_detail_schedule_title_tv.setVisibility(View.GONE);
            reserve_detail_schedule_ll.setVisibility(View.GONE);
        }

        CustomerInfo customerInfo = entity.getCustomerInfo();


        if (customerInfo != null) {
            reserve_detail_customer_title_tv.setVisibility(View.VISIBLE);
            reserve_detail_customer_ll.setVisibility(View.VISIBLE);
            List<KeyValueEntity> data = new ArrayList<>();
            KeyValueEntity entity1 = new KeyValueEntity();
            entity1.setKey("客户编号");
            entity1.setVal(customerInfo.getCustomerId());
            KeyValueEntity entity2 = new KeyValueEntity();
            entity2.setKey("客户姓名");
            entity2.setVal(customerInfo.getCustomerName());
            KeyValueEntity entity3 = new KeyValueEntity();
            entity3.setKey("联系电话");
            entity3.setVal(StringUtils.blurPhone(customerInfo.getMobile()));
            entity3.setDefaultVal("1");

            KeyValEventEntity event = new KeyValEventEntity();
            event.setAction("unlockPhone");
            event.setParamKey("type");
            event.setPhoneNum(customerInfo.getPhoneNum());
            entity3.setEvent(event);
            data.add(entity1);
            data.add(entity2);
            data.add(entity3);
            reserve_detail_customer_kvl.setData(data);
            reserve_detail_customer_kvl.setOnUnlockMobileListener(new KeyValueLayout.OnUnlockMobileListener() {
                @Override
                public void unLock(View parentView, TextView mobile, TextView unlock, ImageView lockIcon, KeyValueEntity keyValueEntity) {
                    Utils.unLockMobile(parentView, mobile, unlock, lockIcon, keyValueEntity, customerInfo.getCustomerId(), ReserveDetailActivity.this);
                }
            });
        }else {
            reserve_detail_customer_title_tv.setVisibility(View.GONE);
            reserve_detail_customer_ll.setVisibility(View.GONE);
        }

        PayInfo payInfo = entity.getPayInfo();
        if (payInfo == null || payInfo.getShowType() == 0){
            reserve_detail_pay_title_tv.setVisibility(View.GONE);
            reserve_detail_pay_rv.setVisibility(View.GONE);
        }else {
            List<PayItem> payItems = payInfo.getPayInfo();
            if (payItems != null && payItems.size()>0) {
                reserve_detail_pay_title_tv.setVisibility(View.VISIBLE);
                reserve_detail_pay_rv.setVisibility(View.VISIBLE);
                reserve_detail_pay_rv.setLayoutManager(new LinearLayoutManager(this));
                reserve_detail_pay_rv.setAdapter(new SchedulePayAdapter(this,payItems));
            }else {
                reserve_detail_pay_title_tv.setVisibility(View.GONE);
                reserve_detail_pay_rv.setVisibility(View.GONE);
            }
        }

        ReserveInfo reserveInfo = entity.getReserveInfo();
        if (reserveInfo != null) {
            reserve_detail_reserve_title_tv.setVisibility(View.VISIBLE);
            reserve_detail_reserve_ll.setVisibility(View.VISIBLE);
            List<KeyValueEntity> data = new ArrayList<>();
            KeyValueEntity entity1 = new KeyValueEntity();
            entity1.setKey("提交时间");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            String dateTimeStr = "";
            String endDateStr = "";
            try {
                Date dateTime = format.parse(reserveInfo.getDateTime());
                Date endDate = format.parse(reserveInfo.getEndDate());
                dateTimeStr = format1.format(dateTime);
                endDateStr = format1.format(endDate);

            } catch (ParseException e) {
                e.printStackTrace();
                dateTimeStr= "";
                endDateStr= "";
            }
            entity1.setVal(dateTimeStr);
            KeyValueEntity entity2 = new KeyValueEntity();
            entity2.setKey("提交人");
            entity2.setVal(reserveInfo.getSubmitter());
            KeyValueEntity entity3 = new KeyValueEntity();
            entity3.setKey("预留至");
            entity3.setVal(endDateStr);
            data.add(entity1);
            data.add(entity2);
            data.add(entity3);
            reserve_detail_reserve_kvl.setData(data);
        }else {
            reserve_detail_reserve_title_tv.setVisibility(View.GONE);
            reserve_detail_reserve_ll.setVisibility(View.GONE);
        }
        List<ButtonTypeEntity> buttons = entity.getButtonType();
        if (buttons == null || buttons.size()==0) {
            reserve_detail_bottom_ll.setVisibility(View.GONE);
        }else if (buttons.size() == 1){
            reserve_detail_botton1_tv.setVisibility(View.GONE);
            reserve_detail_botton2_tv.setVisibility(View.GONE);
            reserve_detail_botton3_tv.setVisibility(View.VISIBLE);
            reserve_detail_botton3_tv.setText(buttons.get(0).getName());
            reserve_detail_botton3_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomClick(buttons.get(0).getType());
                }
            });
        }else if (buttons.size() == 2){
            reserve_detail_botton1_tv.setVisibility(View.GONE);
            reserve_detail_botton2_tv.setVisibility(View.VISIBLE);
            reserve_detail_botton3_tv.setVisibility(View.VISIBLE);
            reserve_detail_botton3_tv.setText(buttons.get(1).getName());
            reserve_detail_botton3_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomClick(buttons.get(1).getType());
                }
            });
            reserve_detail_botton2_tv.setText(buttons.get(0).getName());
            reserve_detail_botton2_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomClick(buttons.get(0).getType());
                }
            });
        }else if (buttons.size() == 3){
            reserve_detail_botton1_tv.setVisibility(View.VISIBLE);
            reserve_detail_botton2_tv.setVisibility(View.VISIBLE);
            reserve_detail_botton3_tv.setVisibility(View.VISIBLE);
            reserve_detail_botton3_tv.setText(buttons.get(2).getName());
            reserve_detail_botton3_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomClick(buttons.get(2).getType());
                }
            });
            reserve_detail_botton2_tv.setText(buttons.get(1).getName());
            reserve_detail_botton2_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomClick(buttons.get(1).getType());
                }
            });

            reserve_detail_botton1_tv.setText(buttons.get(0).getName());
            reserve_detail_botton1_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomClick(buttons.get(0).getType());
                }
            });
        }

        scrollView.scrollTo(0,0);
    }

    private void bottomClick(int type) {
        switch (type){
            case 3:
                cancelReserve();
                break;

            case 4:
                delayReserve();
                break;
            case 5:
                convert();
                break;
        }


    }

    private void convert() {
        ContractNewActivity.start(this,scheduleInfo.getDate(),scheduleInfo.getHallId(),
                scheduleInfo.getHallName(),scheduleInfo.getSType(),scheduleInfo.getStart_type(),scheduleInfo.getEnd_type(),startDate,endDate,scheduleInfo.getIsMulti());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK){
            finish();
        }
    }

    private void delayReserve() {
        new AlertDialog.Builder(this)
                .setContent("预留延期最多延长7天，是否立即申请？")
                .setCancelable(true)
                .setConfirmListener("立即申请", new AlertDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog) {
                        cancelOrDelay(1,reserveId);
                        dialog.dismiss();
                    }
                }).setCancelListener("暂不申请", new AlertDialog.OnCancelClickListener() {
            @Override
            public void onCancel(Dialog dialog) {
                dialog.dismiss();
            }
        }).build().show();
    }

    private void cancelReserve() {
        new AlertDialog.Builder(this)
                .setContent("取消后档期会立即释放，是否确认取消？")
                .setCancelable(true)
                .setConfirmListener("立即取消", new AlertDialog.OnConfirmClickListener() {
                    @Override
                    public void onConfirm(Dialog dialog) {
                        cancelOrDelay(2,reserveId);
                        dialog.dismiss();
                    }
                }).setCancelListener("暂不取消", new AlertDialog.OnCancelClickListener() {
            @Override
            public void onCancel(Dialog dialog) {
                dialog.dismiss();
            }
        }).build().show();
    }

    private void cancelOrDelay(int flag,String reserveId){
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("reserveId",reserveId).putParamValue("flag",flag);

        HttpRequest.create(UrlConstant.SCHEDULE_RESERVE_CANCEL_OR_DELAY).putParam(jsonParam).get(new CallBack<ReserveSuccess>() {
            @Override
            public ReserveSuccess doInBackground(String response) {
                if ("[]".equals(response)) {
                    return new ReserveSuccess();
                } else {
                    return JsonUtils.parse(response, ReserveSuccess.class);
                }
            }

            @Override
            public void success(ReserveSuccess entity) {
                loadData();
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
        });
    }

}
