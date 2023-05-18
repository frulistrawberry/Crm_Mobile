package com.baihe.lihepro.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.JsonUtils;
import com.baihe.common.util.ToastUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.dialog.BottomSelectDialog;
import com.baihe.lihepro.dialog.PersonSearchDialog;
import com.baihe.lihepro.entity.PayCodeCreateEntity;
import com.baihe.lihepro.entity.PayCodeOrderEntity;
import com.baihe.lihepro.entity.PayCodePlanEntity;

import java.util.List;

public class AddPayCodeActivity extends BaseActivity {
    private LinearLayout selectCustomerBtn;
    private TextView customerTv;
    private LinearLayout selectOrderBtn;
    private LinearLayout selectTypeBtn;
    private TextView typeTv;
    private TextView amountTv;
    private TextView buildBtn;
    private EditText amountEt;
    private TextView orderTv;
    private List<PayCodeOrderEntity> payCodeOrders;
    private List<PayCodePlanEntity> payCodePlans;

    private String customerId;
    private String category;
    private Object orderId;
    private Object planId;
    private int receivablesType;
    private String customerName;
    private boolean fromSchedule;



    public static void start(Context context){
        Intent intent = new Intent(context, AddPayCodeActivity.class);
        context.startActivity(intent);
    }

    public static void start(Activity context, String customerId, String customerName){
        Intent intent = new Intent(context, AddPayCodeActivity.class);
        intent.putExtra("customerId",customerId);
        intent.putExtra("customerName",customerName);
        context.startActivityForResult(intent,1001);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("生成付款码");
        setContentView(R.layout.activity_add_pay_code);
        initView();
        listener();
        this.customerId = getIntent().getStringExtra("customerId");
        this.customerName = getIntent().getStringExtra("customerName");
        if (customerId != null) {
            fromSchedule = true;
            loadOrderList(customerId);
        }
        if (customerName!=null){
            customerTv.setText(customerName);
        }
    }

    private void initView() {
        selectCustomerBtn = findViewById(R.id.ll_phone_value);
        customerTv = findViewById(R.id.tv_phone_value);
        selectOrderBtn = findViewById(R.id.ll_category_value);
        orderTv = findViewById(R.id.tv_category_value);
        selectTypeBtn = findViewById(R.id.ll_pay_type);
        typeTv = findViewById(R.id.tv_pay_type_value);
        amountTv = findViewById(R.id.tv_price);
        amountEt = findViewById(R.id.et_pay_amount);
        buildBtn = findViewById(R.id.tv_build);
    }

    private void listener() {
        selectCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PersonSearchDialog.Builder(AddPayCodeActivity.this)
                        .setCancelable(true)
                        .setTitle("选择用户信息")
                        .setOnConfirmClickListener(new PersonSearchDialog.OnConfirmClickListener() {
                            @Override
                            public void onConfirm(Dialog dialog, String text, String customerId) {
                                dialog.dismiss();
                                customerTv.setText(text);
                                loadOrderList(customerId);
                                AddPayCodeActivity.this.customerId = customerId;
                            }
                        }).setOnCancelClickListener(new PersonSearchDialog.OnCancelClickListener() {
                    @Override
                    public void onCancel(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).build().show();
            }
        });
        selectOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomSelectDialog.Builder(AddPayCodeActivity.this)
                        .setCancelable(true)
                        .setTitle("选择订单类型")
                        .setOnCancelClickListener(new BottomSelectDialog.OnCancelClickListener() {
                            @Override
                            public void onCancel(Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .setOnConfirmClickListener(new BottomSelectDialog.OnConfirmClickListener() {
                            @Override
                            public void onConfirm(Dialog dialog, int position) {
                                dialog.dismiss();
                                PayCodeOrderEntity entity = payCodeOrders.get(position);
                                payCodePlans = entity.getPlan_list();
                                orderTv.setText(entity.getCategore_name());
                                category = String.valueOf(entity.getCategory());
                                orderId = String.valueOf(entity.getId());
                                StringBuffer buffer = new StringBuffer();
                                buffer.append("订单金额：");
                                int length1 = buffer.length();
                                String money = entity.getAmount()==null?"0":entity.getAmount()+"";
                                if (TextUtils.isEmpty(money)||"请填写金额".equals(money))
                                    buffer.append("");
                                else
                                    buffer.append(money);
                                int length2 = buffer.length();
                                buffer.append("元");
                                String content = buffer.toString();
                                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#2DB4E6"));

                                SpannableString span = new SpannableString(content);
                                span.setSpan(colorSpan, length1, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                span.setSpan(new StyleSpan(Typeface.BOLD), length1, length2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                span.setSpan(new AbsoluteSizeSpan(24, true), length1, length2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                amountTv.setText(span);

                            }
                        })
                        .setSelectDataAdapter(new BottomSelectDialog.SelectDataAdapter() {
                            @Override
                            public int getCount() {
                                return payCodeOrders!=null?payCodeOrders.size():0;
                            }

                            @Override
                            public String getText(int dataPostion) {
                                return payCodeOrders.get(dataPostion).getCategore_name();
                            }

                        }).build().show();
            }
        });
        selectTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomSelectDialog.Builder(AddPayCodeActivity.this)
                        .setCancelable(true)
                        .setTitle("选择回款类型")
                        .setOnCancelClickListener(new BottomSelectDialog.OnCancelClickListener() {
                            @Override
                            public void onCancel(Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .setOnConfirmClickListener(new BottomSelectDialog.OnConfirmClickListener() {
                            @Override
                            public void onConfirm(Dialog dialog, int position) {
                                dialog.dismiss();
                                PayCodePlanEntity entity = payCodePlans.get(position);
                                typeTv.setText(entity.getTitle());
                                planId = entity.getPlan_id();
                                receivablesType = entity.getReceivables_type();

                            }
                        })
                        .setSelectDataAdapter(new BottomSelectDialog.SelectDataAdapter() {
                            @Override
                            public int getCount() {
                                return payCodePlans == null?0:payCodePlans.size();
                            }

                            @Override
                            public String getText(int dataPostion) {
                                return payCodePlans.get(dataPostion).getTitle();
                            }
                        })
                        .build().show();
            }
        });
        buildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String money = amountEt.getText().toString();
                createPayCode(customerId,category,orderId,planId,receivablesType,money);
            }
        });
    }



    private void loadOrderList(String customerId){
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("customerId", customerId);
        HttpRequest.create(UrlConstant.PAY_CODE_GET_ORDER_LIST).putParam(jsonParam).get(new CallBack<List<PayCodeOrderEntity>>() {
            @Override
            public List<PayCodeOrderEntity> doInBackground(String response) {
                return JsonUtils.parseList(response, PayCodeOrderEntity.class);
            }

            @Override
            public void success(List<PayCodeOrderEntity> entity) {
                payCodeOrders = entity;
            }

            @Override
            public void error() {
                payCodeOrders = null;
            }

            @Override
            public void fail() {
                payCodeOrders = null;
            }
        });
    }

    private void createPayCode(String customerId,String category,Object orderId,Object planId,int receivablesType,String money){
        JsonParam jsonParam = JsonParam.newInstance("params")
                .putParamValue("customerId", customerId)
                .putParamValue("category",category)
                .putParamValue("planId",planId)
                .putParamValue("receivablesType",receivablesType)
                .putParamValue("money",money)
                .putParamValue("orderId",orderId);
        HttpRequest.create(UrlConstant.PAY_CODE_CREATE_PAY_CODE).putParam(jsonParam).get(new CallBack<PayCodeCreateEntity>() {
            @Override
            public PayCodeCreateEntity doInBackground(String response) {
                return JsonUtils.parse(response, PayCodeCreateEntity.class);
            }

            @Override
            public void success(PayCodeCreateEntity entity) {
                if (fromSchedule){
                    PayCodeDetailActivity.start(AddPayCodeActivity.this,entity,1);
                }else
                    PayCodeDetailActivity.start(AddPayCodeActivity.this,entity);
            }

            @Override
            public void error() {
                ToastUtils.toast("生成付款码付款码失败，请联系管理员");
            }

            @Override
            public void fail() {
                ToastUtils.toast("生成付款码付款码失败，请联系管理员");

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }
    }
}
