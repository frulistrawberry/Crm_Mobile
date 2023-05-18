package com.baihe.lihepro.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baihe.common.base.BaseActivity;
import com.baihe.common.util.JsonUtils;
import com.baihe.http.HttpRequest;
import com.baihe.http.JsonParam;
import com.baihe.http.callback.CallBack;
import com.baihe.lihepro.R;
import com.baihe.lihepro.constant.UrlConstant;
import com.baihe.lihepro.entity.AgreementInfoEntity;
import com.baihe.lihepro.entity.KeyValueEntity;
import com.baihe.lihepro.entity.SumEntity;
import com.baihe.lihepro.view.KeyValueLayout;

import java.util.ArrayList;
import java.util.List;

public class ProtocolDetailActivity extends BaseActivity {

    public static final String INTENT_AGREEMENT_NUM = "INTENT_AGREEMENT_DATA";
    public static final String INTENT_CONTRACT_ID = "INTENT_CONTRACT_ID";
    public static final String INTENT_CONTRACT_CAN_EDIT = "INTENT_CONTRACT_CAN_EDIT";

    private TextView tv_agreement_num;
    private TextView btn_edit;
    private TextView tv_add_price;
    private TextView tv_del_price;
    private TextView tv_sum_price;
    private TextView protocol_del_title;
    private KeyValueLayout protocol_add_kvl;
    private KeyValueLayout protocol_del_kvl;



    private String agreementNum;
    private String contractId;
    private boolean canEdit;

    public static void start(Activity activity, String contractId, String agreementNum, int requestCode) {
        Intent intent = new Intent(activity, ProtocolDetailActivity.class);
        intent.putExtra(INTENT_CONTRACT_ID, contractId);
        intent.putExtra(INTENT_AGREEMENT_NUM, agreementNum);
        activity.startActivityForResult(intent, requestCode);
    }
    public static void start(Activity activity, String contractId, String agreementNum,boolean canEdit, int requestCode) {
        Intent intent = new Intent(activity, ProtocolDetailActivity.class);
        intent.putExtra(INTENT_CONTRACT_ID, contractId);
        intent.putExtra(INTENT_AGREEMENT_NUM, agreementNum);
        intent.putExtra(INTENT_CONTRACT_CAN_EDIT, canEdit);
        activity.startActivityForResult(intent, requestCode);
    }



    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contractId = getIntent().getStringExtra(INTENT_CONTRACT_ID);
        agreementNum = getIntent().getStringExtra(INTENT_AGREEMENT_NUM);
        canEdit = getIntent().getBooleanExtra(INTENT_CONTRACT_CAN_EDIT,false);
        setTitleText("查看协议");
        setContentView(R.layout.activity_protocol_detail);
        init();
        listener();
        loadData();

    }

    private SumEntity getPrice(List<List<KeyValueEntity>> dataList) {
        double price = 0D;
        SumEntity sumEntity = new SumEntity();
        for (List<KeyValueEntity> data : dataList) {
            //增项1，减项2
            String agreementType;
            double agreementAmount = 0.0;
            for (KeyValueEntity keyValueEntity : data) {
                agreementAmount+= Double.parseDouble(TextUtils.isEmpty(keyValueEntity.getVal())?"0.0":keyValueEntity.getVal());
            }
            if (0 == dataList.indexOf(data))
                agreementType = "1";
            else
                agreementType = "2";
            if (!TextUtils.isEmpty(agreementType)) {
                try {
                    if ("1".equals(agreementType)) {
                        sumEntity.setAddAmount(String.valueOf(agreementAmount));
                    } else if ("2".equals(agreementType)) {
                        sumEntity.setDelAmount(String.valueOf(agreementAmount));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        double addPrice = TextUtils.isEmpty(sumEntity.getAddAmount())?0:Double.parseDouble(sumEntity.getAddAmount());
        double delPrice = TextUtils.isEmpty(sumEntity.getDelAmount())?0:Double.parseDouble(sumEntity.getDelAmount());
        sumEntity.setSumAmount((addPrice + delPrice)+"");
        return sumEntity;
    }

    private void loadData() {
        JsonParam jsonParam = JsonParam.newInstance("params").putParamValue("agreementNum", agreementNum).putParamValue("contractId",contractId);
        HttpRequest.create(UrlConstant.GET_ARGUMENT_INFO).putParam(jsonParam).get(new CallBack<List<AgreementInfoEntity>>() {
            @Override
            public List<AgreementInfoEntity> doInBackground(String response) {
                return JsonUtils.parseList(response, AgreementInfoEntity.class);
            }

            @Override
            public void success(List<AgreementInfoEntity> protocolEntity) {
                List<List<KeyValueEntity>> showData = new ArrayList<>();
                for (AgreementInfoEntity agreementInfoEntity : protocolEntity) {
                    boolean isAdd = protocolEntity.indexOf(agreementInfoEntity) == 0;
                    showData.add(agreementInfoEntity.covertToKeyValListForWedding(isAdd));
                }
                tv_agreement_num.setText(agreementNum);
                if (showData!=null && showData.size()>0){
                    if (showData.size() == 1){
                        protocol_del_title.setVisibility(View.GONE);
                        protocol_del_kvl.setVisibility(View.GONE);
                        protocol_add_kvl.setData(showData.get(0));
                    }else {
                        protocol_del_title.setVisibility(View.VISIBLE);
                        protocol_del_kvl.setVisibility(View.VISIBLE);
                        protocol_add_kvl.setData(showData.get(0));
                        protocol_del_kvl.setData(showData.get(1));
                    }
                }
                SumEntity sumEntity = getPrice(showData);

                tv_add_price.setText("￥"+sumEntity.getAddAmount());
                tv_del_price.setText("￥"+sumEntity.getDelAmount());
                tv_sum_price.setText("￥"+sumEntity.getSumAmount());
                statusLayout.normalStatus();


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

    private void listener() {
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProtocolActivity.start(ProtocolDetailActivity.this,contractId,agreementNum,101);
            }
        });
    }

    private void init() {
        tv_agreement_num = findViewById(R.id.tv_agreement_num);
        btn_edit = findViewById(R.id.btn_edit);
        tv_add_price = findViewById(R.id.tv_add_price);
        tv_del_price = findViewById(R.id.tv_del_price);
        tv_sum_price = findViewById(R.id.tv_sum_price);
        protocol_del_title = findViewById(R.id.protocol_del_title);
        protocol_add_kvl = findViewById(R.id.protocol_add_kvl);
        protocol_del_kvl = findViewById(R.id.protocol_del_kvl);
        if (canEdit)
            btn_edit.setVisibility(View.VISIBLE);
        else
            btn_edit.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            loadData();
    }
}
