package com.baihe.lihepro.activity;

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

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.ImageUtils;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.StringUtils;
import com.baihe.common.view.StatusChildLayout;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.AlertDialog;
import com.baihe.lihepro.dialog.ApproveDialog;
import com.baihe.lihepro.entity.ApproveEntity;
import com.baihe.lihepro.entity.ButtonTypeEntity;
import com.baihe.lihepro.entity.KeyValEventEntity;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.entity.schedule.BookInfo;
import com.baihe.lihepro.entity.schedule.ContractInfo;
import com.baihe.lihepro.entity.schedule.ReserveDetail;
import com.baihe.lihepro.entity.schedule.ReserveSuccess;
import com.baihe.lihepro.entity.schedule.ScheduleInfo;
import com.baihe.lihepro.entity.schedule.StatusText;
import com.baihe.lihepro.manager.AccountManager;
import com.baihe.lihepro.utils.Utils;
import com.baihe.lihepro.view.KeyValueLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class BookDetailActivity extends BaseActivity {

    private TextView reserve_detail_schedule_title_tv;
    private TextView reserve_detail_customer_title_tv;
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
    private String bookId;
    private ScheduleInfo scheduleInfo;
    private ScheduleInfo NewScheduleInfo;
    private FrameLayout fl_bg;
    private ImageView iv_arrow;
    private TextView tv_status;
    private View v_bg;
    private String contractId;
    private NestedScrollView scrollView;
    private LinearLayout btn_approve;
    private List<ApproveEntity> approveList;
    private String reserve_num;


    public static void start(Context context, String bookId){
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra("bookId",bookId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("预订详情");
        bookId = getIntent().getStringExtra("bookId");
        setContentView(R.layout.activity_book_detail);
        init();
        listener();
        loadData();
    }

    private void init(){
        reserve_detail_schedule_title_tv = findViewById(R.id.reserve_detail_schedule_title_tv);
        reserve_detail_customer_title_tv = findViewById(R.id.reserve_detail_customer_title_tv);
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
        iv_arrow = findViewById(R.id.iv_arrow);
        scrollView = findViewById(R.id.scrollView);
        tv_status = findViewById(R.id.tv_status);
        fl_bg = findViewById(R.id.fl_bg);
        v_bg = findViewById(R.id.v_bg);
        btn_approve = findViewById(R.id.btn_approve);

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
            @Override
            public void onClick(View view) {
                if (approveList!=null && approveList.size()>0){
                    if (NewScheduleInfo == null){
                        new ApproveDialog(BookDetailActivity.this,approveList,scheduleInfo.getHallName(),scheduleInfo.getDate(),scheduleInfo.getSType() == 1?"午宴":"晚宴",null,
                                scheduleInfo.getEnd_date(),scheduleInfo.getEnd_type() == 1?"午":"晚",scheduleInfo.getStart_date(),scheduleInfo.getStart_type() == 1?"午":"晚",scheduleInfo.getIsMulti() == 1).show();
                    }else {
                        String tag = "";
                        if (NewScheduleInfo.isNow())
                            tag = "现";
                        else
                            tag = "原";
                        new ApproveDialog(BookDetailActivity.this,approveList,NewScheduleInfo.getHallName(),NewScheduleInfo.getDate(),NewScheduleInfo.getSType() == 1?"午宴":"晚宴",tag,
                                NewScheduleInfo.getEnd_date(),NewScheduleInfo.getEnd_type() == 1?"午":"晚",NewScheduleInfo.getStart_date(),NewScheduleInfo.getStart_type() == 1?"午":"晚",NewScheduleInfo.getIsMulti() == 1).show();

                    }
                }
            }
        });
    }

    private void loadData(){

        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("bookId",bookId);

        HttpRequest.create(UrlConstant.SCHEDULE_GET_BOOK_INFO).putParam(jsonParam).get(new CallBack<ReserveDetail>() {
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

        BookInfo bookInfo = entity.getBookInfo();
        if (bookInfo != null) {
            reserve_num = bookInfo.getBookId();
        }
        scheduleInfo = entity.getScheduleInfo();
        NewScheduleInfo = entity.getNewScheduleInfo();
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
            entity4.setVal(scheduleInfo.getStart_date()+(scheduleInfo.getStart_type()==1?" 午宴":" 晚宴"));

            KeyValueEntity entity5 = new KeyValueEntity();
            entity5.setKey("档期结束");
            entity5.setVal(scheduleInfo.getEnd_date()+(scheduleInfo.getEnd_type()==1?" 午宴":" 晚宴"));
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

        ContractInfo customerInfo = entity.getContarctInfo();


        if (customerInfo != null) {
            contractId = customerInfo.getContractId();
            reserve_detail_customer_title_tv.setVisibility(View.VISIBLE);
            reserve_detail_customer_ll.setVisibility(View.VISIBLE);
            List<KeyValueEntity> data = new ArrayList<>();
            KeyValueEntity entity1 = new KeyValueEntity();
            entity1.setKey("合同编号");
            entity1.setVal(customerInfo.getContractNum());
            KeyValEventEntity eventEntity = new KeyValEventEntity();
            eventEntity.setAction("goOrder");
            entity1.setEvent(eventEntity);
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
            reserve_detail_customer_kvl.setOnOrderListener(new KeyValueLayout.OnOrderListener() {
                @Override
                public void go(KeyValueEntity keyValueEntity) {
                    ContractDetailActivity.start(BookDetailActivity.this,contractId,1002);
                }
            });
            reserve_detail_customer_kvl.setOnUnlockMobileListener(new KeyValueLayout.OnUnlockMobileListener() {
                @Override
                public void unLock(View parentView, TextView mobile, TextView unlock, ImageView lockIcon, KeyValueEntity keyValueEntity) {
                    Utils.unLockMobile(parentView, mobile, unlock, lockIcon, keyValueEntity, customerInfo.getCustomerId(), BookDetailActivity.this);
                }
            });
        }else {
            reserve_detail_customer_title_tv.setVisibility(View.GONE);
            reserve_detail_customer_ll.setVisibility(View.GONE);
        }


        BookInfo reserveInfo = entity.getBookInfo();
        if (reserveInfo != null) {
            reserve_detail_reserve_title_tv.setVisibility(View.VISIBLE);
            reserve_detail_reserve_ll.setVisibility(View.VISIBLE);
            List<KeyValueEntity> data = new ArrayList<>();
            KeyValueEntity entity1 = new KeyValueEntity();
            entity1.setKey("预订单号");
            entity1.setVal(reserveInfo.getBookId());
            KeyValueEntity entity2 = new KeyValueEntity();
            entity2.setKey("预订时间");
            try {
                Date dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(reserveInfo.getDateTime());
                entity2.setVal(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(dateTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            KeyValueEntity entity3 = new KeyValueEntity();
            entity3.setKey("预订人");
            entity3.setVal(reserveInfo.getBooker());
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
            case 6:
                reset();
                break;
            case 7:

                new AlertDialog.Builder(this)
                        .setContent("您也可通过改期/换厅修改订单， 是否确认退订?")
                        .setCancelable(true)
                        .setConfirmListener("立即申请", new AlertDialog.OnConfirmClickListener() {
                            @Override
                            public void onConfirm(Dialog dialog) {
                                cancelBook();
                                dialog.dismiss();
                            }
                        }).setCancelListener("暂不申请", new AlertDialog.OnCancelClickListener() {
                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).build().show();
                break;
        }
    }

    private void reset() {
        if (!"34".equals(AccountManager.newInstance().getUser().getCompany_id()))
            ResetBookActivity.start(this,bookId,contractId,scheduleInfo.getHallId(),scheduleInfo.getHallName(),scheduleInfo.getDate(),scheduleInfo.getSType());
        else  {
            NewResetBookActivity.start(this,bookId,contractId,scheduleInfo.getHallId(),reserve_num,
                    scheduleInfo.getHallName(),
                    scheduleInfo.getStart_type(),scheduleInfo.getEnd_type(),scheduleInfo.getStart_date(),scheduleInfo.getEnd_date(),scheduleInfo.getDate(),scheduleInfo.getIsMulti(),scheduleInfo.getSType());
        }
    }

    private void cancelBook(){
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("bookId",bookId).putParamValue("contractId",contractId);

        HttpRequest.create(UrlConstant.SCHEDULE_BOOK_CANCEL).putParam(jsonParam).get(new CallBack<ReserveSuccess>() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK){
            finish();
        }
    }
}
